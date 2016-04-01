package algorithms;

import org.openscience.cdk.Molecule;

import algorithms.isomorphism.ChainsFamilyMatching;
import algorithms.isomorphism.FamilyMatcher;
import algorithms.isomorphism.Isomorphism;
import algorithms.isomorphism.MatchingType;
import algorithms.isomorphism.chains.ChainsDB;
import algorithms.isomorphism.conditions.ConditionToExtend;
import algorithms.isomorphism.conditions.ExtendsNTimes;
import algorithms.isomorphism.indepth.DeepenMatcher;
import algorithms.isomorphism.indepth.Modulation;
import algorithms.isomorphism.indepth.RemoveByDistance;
import algorithms.isomorphism.indepth.RemoveStrategy;
import algorithms.utils.Coverage;
import algorithms.utils.Match;
import algorithms.utils.PeptideExecutionTimes;
import db.FamilyDB;
import db.PolymersDB;
import io.loaders.json.PeptidesExecutionTimesJsonLoader;
import model.Family;
import model.OtherChemicalObject;
import model.Polymer;
import model.graph.ContractedGraph;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.HashMap;

public class MonomericSpliting {

	public static boolean verbose = false;
	
	private Coverage coverage;
	private Map<String, ArrayList<Coverage>> pepsCoveragesList;//new add 2
	private List<Coverage> pepCoveragesList_1;//new add 2, modulation after strict matching
	private List<Coverage> pepCoveragesList_2;//new add 2, modulation after light  matching
	private Map<String, ArrayList<PeptideExecutionTimes>> pepsExecutionTimes;//new add 2
	private double coverRatio = 0.9;//new add 2, which should be defined as a parameter by user
	
	private FamilyDB families;
	private boolean allowLightMatchs;
	
	private FamilyMatcher matcher;
	private ConditionToExtend condition;
	private RemoveStrategy remover;
	private Modulation modulation;
	
	private String timesFile="results/peptidesExecutionTimes.json";//new add
	private PeptideExecutionTimes[] peps_times;//new add
	public static int count;//new add, = number of peptides
	private double ratio;//new add
	public static int numFullCoverage;// = 100%
	public static int numPartCoverage_1;// 80% <= numPartCoverage_1 < 100%
	public static int numPartCoverage_2;// < 80%

	private int retry;
	
	public MonomericSpliting(FamilyDB families, ChainsDB chains, int removeDistance, int retryCount, int modulationDepth) {
		this.families = families;
		this.matcher = new ChainsFamilyMatching(chains);
		this.remover = new RemoveByDistance(removeDistance);
		this.modulation = new Modulation(modulationDepth, this.coverRatio);//new add 2
		this.retry = retryCount;
		
		pepsCoveragesList  = new HashMap<String, ArrayList<Coverage>>();//new add 2
		pepsExecutionTimes = new HashMap<String, ArrayList<PeptideExecutionTimes>>();//new add 2
	}
	
	public Coverage[] computeCoverages (PolymersDB polDB) {
		//Coverage[] covs = new Coverage[polDB.size()];
		//peps_times=new PeptideExecutionTimes[polDB.size()];//new add
		//int idx=0;
		for (Polymer pol : polDB.getObjects()) {
			pepsCoveragesList.put(pol.getName(), new ArrayList<Coverage>());
			pepsExecutionTimes.put(pol.getName(), new ArrayList<PeptideExecutionTimes>());
			pepCoveragesList_1 = new ArrayList<Coverage>();//new add 2
			this.computeCoverage(pol);
			//covs[idx] = this.getCoverage();
			//idx += 1;	
		}
		
		//System.out.println("****"+idx);
		System.out.println("====Number of peptides : "+count);//new add
		PeptidesExecutionTimesJsonLoader petjl=new PeptidesExecutionTimesJsonLoader();//new add

		int size = 0;
		for(ArrayList<PeptideExecutionTimes> pt: pepsExecutionTimes.values()){
			size += pt.size();
		}
		peps_times = new PeptideExecutionTimes[size];
		int count = 0;
		for(ArrayList<PeptideExecutionTimes> pt: pepsExecutionTimes.values()){
			for(PeptideExecutionTimes p : pt){
				peps_times[count]=p;
				count++;
			}
		}
		petjl.saveFile(peps_times, timesFile);//new add
		
		for(Entry<String, ArrayList<Coverage>>  entry: pepsCoveragesList.entrySet()){
			ArrayList<Coverage> ac = entry.getValue();
			for(int i=0; i<= ac.size()-2; i++){
				for(int j=i+1; j<=ac.size()-1; j++){
					if(ac.get(i).equals(ac.get(j))){
						pepsCoveragesList.get(entry.getKey()).remove(j);
					}
				}
			}
		}
		
		size = 0;
		for(ArrayList<Coverage> cs: pepsCoveragesList.values()){
			size += cs.size();
		}
		Coverage[] covs = new Coverage[size];
		count = 0;
		for(ArrayList<Coverage> cs: pepsCoveragesList.values()){
			for(Coverage c : cs){
				covs[count]=c;
				count++;
			}
		}
		
		System.out.println("Results\\\\\\\\\\\\\\\\\\\"");
		for(Entry<String, ArrayList<Coverage>> c: pepsCoveragesList.entrySet()){
			for(int i=0; i<c.getValue().size();i++){
				System.out.println(c.getKey()+"  "+c.getValue().get(i).getCoverageRatio());
			}
			
		}
		
		return covs;
	}

	/**
	 * Calculate an object Coverage with all matches from families. 
	 * @param pep Peptide to match
	 */
	public void computeCoverage(Polymer pep) {

		PeptideExecutionTimes pepTimes=new PeptideExecutionTimes(pep.getName());//new add
		pepTimes.setId(pep.getId());//new add
		pepTimes.setNumAtoms(pep.getMolecule().getAtomCount());//new add ; ???

		this.coverage = new Coverage(pep);
		this.matcher.setChemicalObject(pep);
		Isomorphism.setMappingStorage(true);
		
		// Step 1 : Strict Matching
		if (verbose) {
			System.out.println("+Strict matching");
			System.out.println("++Search residues");
		}
		
		long interval1 = this.calculateTimeInterval("strict", "matchAllFamilies.STRONG");//new add
		pepTimes.setIsomorStrictMatchingTime(interval1);//new add
		
		long interval2 = this.calculateTimeInterval("strict", "getCoverageRatio");//new add
		pepTimes.setTilingTime(interval2);//new add
		
		if (ratio < 1.0) {
			if (verbose)
				System.out.println("++Modulation");
			
			long interval3 = this.calculateTimeInterval("strict", "modulate");//new add
			pepTimes.setTilingTime(interval3);//new add
		}
		
		//test
		/*for(Coverage c : this.pepCoveragesList_1){
			System.out.println(c.getChemicalObject().getName()+"  ||||  "+c.getCoverageRatio());
		}*/
		//Coverage save = this.coverage.clone();
		
		// conditions to go to light matching
		if (!this.allowLightMatchs || ratio == 1.0) {
			Isomorphism.setMappingStorage(false);
			
			pepTimes.setIsomorphismTime();//new add
			pepTimes.setCompleteTime();//new add
			pepsExecutionTimes.get(pep.getName()).add((PeptideExecutionTimes) pepTimes.clone());
			
			//System.out.println("1111 Peptide NAME : "+pep.getName());
			List<Coverage> lc =new ArrayList<>();
			lc.add(this.coverage);
			pepsCoveragesList.get(pep.getName()).addAll(lc);
			
			//peps_times[count]=pepTimes;//new add
			//count++;//new add
			
			//if(ratio == 1.0){
			//	numFullCoverage++;//new add
			//}
			//pepTimes.setCoverRatio(ratio);//new add
			
			//test
				//System.out.println(this.coverage.getId()+"  ||||  "+this.coverage.getCoverageRatio());
			
			return;
		}
			
		long savedTilingTime = pepTimes.getTilingTime();
		
		// Step 2 : Light matching
		// Initialization
		// TODO : Why if/else ?
		if (this.condition == null)
			this.condition = new ExtendsNTimes(this.retry);
		else
			this.condition.init();
		
		this.remover.init();
		
		//new add 2
		/*if(this.pepCoveragesList_1 == null){
			this.pepCoveragesList_1.add(this.coverage);
		}*/
		for(Coverage cover: this.pepCoveragesList_1){
			this.coverage = cover;
			pepCoveragesList_2 = new ArrayList<Coverage>();//new add 2
			
			// Successive matchings
			int depth = 0;
			while (this.coverage.getCoverageRatio() < 1.0 &&
					this.condition.toContinue(this.coverage)) {
				depth++;
				// Contract the atomic graph to monomeric graph
				ContractedGraph contracted = new ContractedGraph(this.coverage);
				// Remove monomers from the current solution to try with other matching strategy
				this.remover.remove(this.coverage, contracted);
				
				// Create a masked molecule to only search on free polymer areas
				Molecule mol = DeepenMatcher.mask (this.coverage);
				this.coverage.setCurrentMaskedMol(mol);
				OtherChemicalObject tmp = new OtherChemicalObject(mol);
				this.matcher.setChemicalObject(tmp);
				
				if (verbose) {
					System.out.println("+Light matching, depth " + depth);
					System.out.println("++Search residues");
				}
				
				// Compute for all families with light matching
				long interval4 = this.calculateTimeInterval("light", "matchAllFamilies.LIGHT");//new add
				pepTimes.setIsomorLightMatchingTime(interval4);//new add
				
				// Re-compute coverage
				long interval5 = this.calculateTimeInterval("light", "calculateGreedyCoverage");//new add
				pepTimes.setTilingTime(interval5);//new add
				
				if (this.coverage.getCoverageRatio() < 1.0) {
					if (verbose)
						System.out.println("++Modulation");
					
					long interval6 = this.calculateTimeInterval("light", "modulate");//new add
					pepTimes.setTilingTime(interval6);//new add
				}
				
				pepTimes.setIsomorphismTime();//new add
				pepTimes.setCompleteTime();//new add
				//pepTimes.setCoverRatio(ratio);//new add
				pepsExecutionTimes.get(pep.getName()).add((PeptideExecutionTimes) pepTimes.clone());
				
				pepTimes.resetTilingTime(savedTilingTime);
				
				//if (this.coverage.getCoverageRatio() > save.getCoverageRatio())
				//	save = this.coverage.clone();
				
				this.remover.nextLevel();
			}
			//System.out.println("finish .... ");
		}
		
		//System.out.println("2222 Peptide NAME : "+pep.getName());
		pepsCoveragesList.get(pep.getName()).addAll(pepCoveragesList_2);
		
		Isomorphism.setMappingStorage(false);
		
		//if (save.getCoverageRatio() > this.coverage.getCoverageRatio())
		//	this.coverage = save;
		
		//pepTimes.setIsomorphismTime();//new add
		//pepTimes.setCompleteTime();//new add
		
		//peps_times[count]=pepTimes;//new add
		//count++;//new add
		
		/*this.ratio = this.coverage.getCoverageRatio();
		if(ratio < 0.8){
			numPartCoverage_2++;
		}
		else if(ratio == 1.0){
			numFullCoverage++;
		}
		else{
			numPartCoverage_1++;
		}*/
		//pepTimes.setCoverRatio(ratio);//new add

	}
	
	/**
	 * Function to match independently all families onto the polymer.
	 * Can easily be parallelized
	 * @param matchType Strict/Light
	 */
	private void matchAllFamilies (MatchingType matchType) {
		/*int nbFamilies = this.families.getFamilies().size();
		int i=1;
		String polName = this.coverage.getChemicalObject().getName();/**/
		
		for (Family family : this.families.getFamilies()) {
			/*/ Display
			if (verbose) {
				System.out.println("In " + polName);
				System.out.println("  Family " + i++ + "/" + nbFamilies);
			}/**/
			
			// Time initialization
			//long time = System.currentTimeMillis();
			
			// Matching
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
	
	public void setConditionToExtend (ConditionToExtend condition) {
		this.condition = condition;
	}

	public static void setVerbose(boolean verbose) {
		MonomericSpliting.verbose = verbose;
	}
	
	//new add
	private long calculateTimeInterval(String matchingType, String detail){
		long interval=2;

		switch(matchingType){
		case "strict": 
			if(detail.equals("matchAllFamilies.STRONG")){
				long startTime = System.currentTimeMillis();
				this.matchAllFamilies(MatchingType.STRONG);
				long endTime = System.currentTimeMillis();
				interval = (endTime-startTime);
			}
			else if(detail.equals("getCoverageRatio")){
				long startTime = System.currentTimeMillis();
				this.ratio = this.coverage.getCoverageRatio();
				long endTime = System.currentTimeMillis();
				interval = (endTime-startTime);
			}
			else if(detail.equals("modulate")){
				long startTime = System.currentTimeMillis();
				pepCoveragesList_1 = this.modulation.modulate(this.coverage.clone());//return ArrayList<Coverage> 
				long endTime = System.currentTimeMillis();
				interval = (endTime-startTime);
			}
			break;
		case "light":
			if(detail.equals("matchAllFamilies.LIGHT")){
				long startTime = System.currentTimeMillis();
				this.matchAllFamilies(MatchingType.LIGHT);
				long endTime = System.currentTimeMillis();
				interval = (endTime-startTime);
			}
			else if(detail.equals("calculateGreedyCoverage")){
				long startTime = System.currentTimeMillis();
				this.coverage.calculateGreedyCoverage();
				long endTime = System.currentTimeMillis();
				interval = (endTime-startTime);
			}
			else if(detail.equals("modulate")){
				long startTime = System.currentTimeMillis();
				for(Coverage cov : this.modulation.modulate(this.coverage.clone())){
					pepCoveragesList_2.add(cov);
				}
				//pepsCoveragesList.get(this.coverage.getId()).addAll(pepCoveragesList_2);
				long endTime = System.currentTimeMillis();
				interval = (endTime-startTime);
			}
			break;
		}
		
		return interval;
			
	}
	
}
