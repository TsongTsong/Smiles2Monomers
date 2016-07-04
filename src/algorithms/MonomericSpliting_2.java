package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openscience.cdk.Molecule;

import algorithms.isomorphism.ChainsFamilyMatching;
import algorithms.isomorphism.FamilyMatcher;
import algorithms.isomorphism.Isomorphism;
import algorithms.isomorphism.MatchingType;
import algorithms.isomorphism.chains.ChainsDB;
import algorithms.isomorphism.indepth.DeepenMatcher;
import algorithms.isomorphism.indepth.RemoveByDistance;
import algorithms.isomorphism.indepth.RemoveStrategy;
import algorithms.utils.Coverage;
import algorithms.utils.Match;
import algorithms.utils.PeptideExecutionTimes;
import coverageCalculator.Calculator;
import coverageCalculator.CalculatorMIP;
import coverageCalculator.CalculatorTM;
import coverageCalculator.ProcessCoverageGain;
import db.FamilyDB;
import db.PolymersDB;
import model.Family;
import model.OtherChemicalObject;
import model.Polymer;
import model.graph.ContractedGraph;

public class MonomericSpliting_2 {

	public static boolean verbose = false;
	
	private Polymer polymer;// Current calculated polymer
	private Coverage coverage;// Current calculated coverage of a current calculated polymer
	private List<Coverage> covList;// Current coverage list calculated from a current calculated coverage
	private FamilyDB families;
	
	private boolean allowLightMatchs;
	
	private FamilyMatcher matcher;
	private RemoveStrategy remover;

	private int retry;// Extension Times of light matching
	private int currentSerachDepth;// Depth of light matching
	
	private ProcessCoverageGain processCoverageGain;
	private Calculator calculator;

	private Map<String, ArrayList<Coverage>> polsCoveragesList;
	
	private boolean useDiffAtomsNumToleration;
	private int diffAtomsNumToleration;
	
	private boolean continueSearch;
	
	private Map<Coverage, ArrayList<int[]>> depthDistance;// Save current coverage's 'searched depths' and its 'remove distances', int[0]=depth, int[1]=distance
	private Map<Coverage, ArrayList<Coverage>> covListOfCov;// Save coverage list searched from a current coverage
	
	public static PeptideExecutionTimes[] pepsExecutionTimes;
	
	public MonomericSpliting_2(FamilyDB families, ChainsDB chains, int removeDistance, int retryCount, Calculator calculator) {
		this.families = families;
		this.matcher = new ChainsFamilyMatching(chains);
		this.remover = new RemoveByDistance(removeDistance);
		this.retry = retryCount;
		
		this.processCoverageGain = new ProcessCoverageGain();
		this.calculator = calculator;
		
		this.polsCoveragesList  = new HashMap<String, ArrayList<Coverage>>();
		
		this.diffAtomsNumToleration = 1;
		
		this.depthDistance = new HashMap<Coverage, ArrayList<int[]>>();
		this.covListOfCov = new HashMap<Coverage, ArrayList<Coverage>>();
		
		
		this.allowLightMatchs = true;
		this.useDiffAtomsNumToleration = true;
		
	}
	
	public Coverage[] computeCoverages (PolymersDB polDB) {		
		pepsExecutionTimes = new PeptideExecutionTimes[polDB.size()];
		int i = 0;
		
		for (Polymer pol : polDB.getObjects()) {
			this.polymer = pol;
			
			PeptideExecutionTimes peptideExecutionTimes = new PeptideExecutionTimes(this.polymer.getName());
			long loadingTime = System.currentTimeMillis();
			
			this.computeCoverage();
			
			loadingTime = System.currentTimeMillis() - loadingTime;
			peptideExecutionTimes.setCompleteTime(loadingTime);
			pepsExecutionTimes[i++] = peptideExecutionTimes;
			
			this.onlyRetainOne(polsCoveragesList.get(this.polymer.getName()));
			this.removeRepetitiveCoverages(polsCoveragesList.get(this.polymer.getName()));
			
		}
		
		if(this.calculator instanceof CalculatorTM){
			
		}
		else if(this.calculator instanceof CalculatorMIP){
			
		}
		
		int size = 0;
		for(ArrayList<Coverage> cs: this.polsCoveragesList.values()){
			size += cs.size();
		}
		Coverage[] covs = new Coverage[size];
		
		int count = 0;
		for(ArrayList<Coverage> cs: this.polsCoveragesList.values()){
			for(Coverage c : cs){
				covs[count]=c;
				
				//covs[count].getCorrectness(families);
				//covs[count].getCoverageRatio();
				//covs[count].getChemicalObject().getName();
				
				count++;
			}
		}
		
		return covs;
	}

	/**
	 * Calculate an object Coverage with all matches from families. 
	 */
	public void computeCoverage() {
		this.coverage = new Coverage(this.polymer);
		this.matcher.setChemicalObject(this.polymer);
		Isomorphism.setMappingStorage(true);
		this.polsCoveragesList.put(this.polymer.getName(), new ArrayList<Coverage>());
		
		// Step 1 : Strict Matching
		if (verbose) {
			System.out.println("+Strict matching");
			System.out.println("++Search residues");
		}
		this.matchAllFamilies(MatchingType.STRONG);
		
		this.covList = this.processCoverageGain.process(this.polymer, this.coverage, this.calculator);
		
		this.onlyRetainOne(this.covList);// Only Retain those who have the ratio that is = 1.0
		this.removeRepetitiveCoverages(this.covList);
		this.polsCoveragesList.get(this.polymer.getName()).addAll((ArrayList<Coverage>) ((ArrayList<Coverage>) covList).clone());
		
		// Conditions to go to light matching
		if (!this.allowLightMatchs) {
			Isomorphism.setMappingStorage(false);
			return;
		}
		else{
			for(Coverage cov : this.covList){
				if(cov.getCoverageRatio()==1.0){
					Isomorphism.setMappingStorage(false);
					return;
				}
			}
		}			
		// Step 2 : Light matching	
		this.remover.init();
		
		// Successive matchings
		this.currentSerachDepth = 0;
		this.lightMatchingAndCalculation(this.covList, this.retry, this.remover.getCurrentDistance());
		
		Isomorphism.setMappingStorage(false);		
	}
	
	/**
	 * This method includes 'lightmatching' and 'the calculation of MIP, TM, or MIP_TM'
	 */
	private void lightMatchingAndCalculation(List<Coverage> covList, int retry, int removeDistance){		
		for(Coverage cov : covList){
			if(cov.getCoverageRatio()==1.0){
				this.continueSearch = false;
				return;
			}
		}
		
		if(retry<=0){
			return;
		}
		
		if (verbose) {
			this.currentSerachDepth++;
			System.out.println("+Light matching, depth " + this.currentSerachDepth);
			System.out.println("++Search residues");
		}
		
		label:
		for(Coverage cover: covList){    		
			int retry_saved = retry;
			int removeDistance_saved = removeDistance;
			this.coverage = cover; 
			this.covList = new ArrayList<Coverage>();
			
			if(this.depthDistance.keySet().size() != 0){
				for(Coverage cov : this.depthDistance.keySet()){
					if(this.coverage.equals(cov)){
						for(int i=0; i<this.depthDistance.get(cov).size(); i++){
							if(this.depthDistance.get(cov).get(i)[0] == retry && 
									this.depthDistance.get(cov).get(i)[1] == this.remover.getCurrentDistance()){
								continue label;
							}
						}
						this.covList = this.covListOfCov.get(cov);
						int[] depDist = {retry, this.remover.getCurrentDistance()};
						this.depthDistance.get(cov).add(depDist.clone());
						
						this.remover.nextLevel();
						this.lightMatchingAndCalculation(this.covList, retry-1, this.remover.getCurrentDistance());
						retry = retry_saved;
						removeDistance = removeDistance_saved;
						continue label;
					}
				}
			}			
			// Contract the atomic graph to monomeric graph
			ContractedGraph contracted = new ContractedGraph(this.coverage);
			// Remove monomers from the current solution to try with other matching strategy
			this.remover.remove(this.coverage, contracted);
			// Create a masked molecule to only search on free polymer areas
			Molecule mol = DeepenMatcher.mask (this.coverage);
			this.coverage.setCurrentMaskedMol(mol);
			OtherChemicalObject tmp = new OtherChemicalObject(mol);
			this.matcher.setChemicalObject(tmp);
			
			this.matchAllFamilies(MatchingType.LIGHT);
			
			this.covList = this.processCoverageGain.process(this.polymer, this.coverage, this.calculator);

			this.onlyRetainOne(this.covList);
			this.removeRepetitiveCoverages(this.covList);
			this.polsCoveragesList.get(this.polymer.getName()).addAll((ArrayList<Coverage>) ((ArrayList<Coverage>) this.covList).clone());
			
			this.covListOfCov.put(cover, (ArrayList<Coverage>) this.covList);			
			int[] depthDist = {retry, this.remover.getCurrentDistance()};
 			this.depthDistance.put(cover, new ArrayList<int[]>());
 			this.depthDistance.get(cover).add(depthDist.clone());
				
			this.remover.nextLevel();
			this.lightMatchingAndCalculation(this.covList, retry-1, this.remover.getCurrentDistance());
			retry = retry_saved;
			removeDistance = removeDistance_saved;
			
			if(continueSearch == false){
				this.continueSearch = true;
				return;
			}
		}
	}
	
	/**
	 * Function to match independently all families onto the polymer.
	 * Can easily be parallelized
	 * @param matchType Strict/Light
	 */
	private void matchAllFamilies (MatchingType matchType) {
		
		for (Family family : this.families.getFamilies()) {
			/*/ Display
			if (verbose) {
				System.out.println("In " + polName);
				System.out.println("  Family " + i++ + "/" + nbFamilies);
			}/**/
			
			this.matcher.setAllowLightMatch(matchType);
			Coverage cov = this.matcher.matchFamilly(family);
			
			if (matchType.equals(MatchingType.LIGHT)) {
				for (Match match : cov.getMatches()) {
					Match transformed = DeepenMatcher.transform(match, this.coverage.getCurrentMaskedMol(),
							this.coverage.getChemicalObject().getMolecule());
					
					this.coverage.addMatch(transformed);
				}
			} else
				this.coverage.addMatches(cov);
			
			/*if (verbose)
				System.out.println("  Search " + matchType.getClass().getCanonicalName() +
						" for family " + family.getName() + " in " + (System.currentTimeMillis()-time) + "\n");/**/
		}
	}
	

	public Coverage getCoverage() {
		return this.coverage;
	}
	
	public FamilyDB getFamilies() {
		return families;
	}
	
	public void setAllowLightMatchs(boolean allowLightMatchs) {
		this.allowLightMatchs = allowLightMatchs;
	}

	public static void setVerbose(boolean verbose) {
		MonomericSpliting.verbose = verbose;
	}
	
	private void removeRepetitiveCoverages(List<Coverage> lcov){
		if(lcov.size() == 1){
			return;
		}
		@SuppressWarnings("unchecked")
		List<Coverage> lcov_clone = (ArrayList<Coverage>) ((ArrayList<Coverage>) lcov).clone();		
			for(int i=0; i<= lcov_clone.size()-2; i++){
				for(int j=i+1; j<=lcov_clone.size()-1; j++){
					
					if(lcov_clone.get(i).equals(lcov_clone.get(j))){
						lcov.set(j, null);
					}
					
					if(useDiffAtomsNumToleration){
						if( (getDifferentAtomsNumber(lcov_clone.get(i), lcov_clone.get(j)) > 0) && 
								(getDifferentAtomsNumber(lcov_clone.get(i), lcov_clone.get(j)) <= this.diffAtomsNumToleration)){
							
							lcov.set(j, null);
						}
					}
				}
			}
			List<Coverage> nullList=new ArrayList<>();
			nullList.add(null);
			lcov.removeAll(nullList);			
	}
	
	private int getDifferentAtomsNumber(Coverage cov, Coverage cov2){
		Set<Integer> set = new HashSet<Integer>();		
		for(Object o : cov.getUsedMatches().toArray()){
			set.addAll(((Match) o).getAtoms());			
		}
		for(Object o2 : cov2.getUsedMatches().toArray()){
			set.removeAll(((Match) o2).getAtoms());					
		}		
		return set.size();	
	}
	
	private void onlyRetainOne(List<Coverage> pepCoveragesList){
		List<Coverage> exemple = new ArrayList<>();
		boolean existOne = false;
		for(Coverage ce : (ArrayList<Coverage>) ((ArrayList<Coverage>) pepCoveragesList).clone()){
			if(ce.getCoverageRatio() == 1.0){
				existOne = true;
				exemple.add(ce);
			}
		}
		if(existOne){
			pepCoveragesList.retainAll(exemple);
		}
	}
}
