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
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MonomericSpliting {

	public static boolean verbose = false;
	private Coverage coverage;// Current coverage
	private Map<String, ArrayList<Coverage>> pepsCoveragesList;
	private List<Coverage> pepCoveragesList_1;// Modulation after strict matching
	private List<Coverage> pepCoveragesList_n;// Modulation after each light matching
	private Map<String, ArrayList<PeptideExecutionTimes>> pepsExecutionTimes;
	private double coverRatio = 0.9;// Which should be defined as a parameter by user
	private FamilyDB families;
	private boolean allowLightMatchs;
	private FamilyMatcher matcher;
	private ConditionToExtend condition;
	private RemoveStrategy remover;
	private Modulation modulation;
	private String timesFile="results/peptidesExecutionTimes.json";
	private PeptideExecutionTimes[] peps_times;
	public static int countPeps;// = Number of peptides
	private double ratio;
	public static int numFullCoverage;// = 100%
	public static int numPartCoverage_1;// 80% <= numPartCoverage_1 < 100%
	public static int numPartCoverage_2;// < 80%
	private int retry;
	
	private PeptideExecutionTimes pepTimes;
	private Polymer pep;
	private long savedTilingTime;
	
	
	public MonomericSpliting(FamilyDB families, ChainsDB chains, int removeDistance, int retryCount, int modulationDepth) {
		this.families = families;
		this.matcher = new ChainsFamilyMatching(chains);
		this.remover = new RemoveByDistance(removeDistance);
		this.modulation = new Modulation(modulationDepth, this.coverRatio);
		this.retry = retryCount;
		
		pepsCoveragesList  = new HashMap<String, ArrayList<Coverage>>();
		pepsExecutionTimes = new HashMap<String, ArrayList<PeptideExecutionTimes>>();
	}
	
	public Coverage[] computeCoverages (PolymersDB polDB) {
		for (Polymer pol : polDB.getObjects()) {
			pepsCoveragesList.put(pol.getName(), new ArrayList<Coverage>());
			pepsExecutionTimes.put(pol.getName(), new ArrayList<PeptideExecutionTimes>());
			pepCoveragesList_1 = new ArrayList<Coverage>();
			this.pep=pol;
			this.computeCoverage(pol);
			countPeps++;
		}
		
		System.out.println("= = = = = =Number of peptides : " + countPeps);
		PeptidesExecutionTimesJsonLoader petjl=new PeptidesExecutionTimesJsonLoader();

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
		petjl.saveFile(peps_times, timesFile);
		
		Set<Entry<String, ArrayList<Coverage>>> set = pepsCoveragesList.entrySet();
		for(Entry<String, ArrayList<Coverage>>  entry: set){
			String key = entry.getKey();
			@SuppressWarnings("unchecked")
			ArrayList<Coverage> ac = (ArrayList<Coverage>) entry.getValue().clone();
			if(ac.size()==0 || ac.size()==1){
				continue;
			}
			for(int i=0; i<= ac.size()-2; i++){
				for(int j=i+1; j<=ac.size()-1; j++){
					if(ac.get(i).equals(ac.get(j))){
						pepsCoveragesList.get(key).set(j, null);
					}
				}
			}
			
			List<Coverage> nullList=new ArrayList<>();
			nullList.add(null);
			pepsCoveragesList.get(key).removeAll(nullList);
			
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
		
		System.out.println("************************R-e-s-u-l-t-s*************************");
		for(Entry<String, ArrayList<Coverage>> c: pepsCoveragesList.entrySet()){
			if(c.getValue().size()==0){
				System.out.println(c.getKey());
				continue;
			}
			
			for(int i=0; i<c.getValue().size();i++){
				System.out.println(c.getKey()+"  "+c.getValue().get(i).getCoverageRatio());
				HashSet<Match> hm = c.getValue().get(i).getUsedMatches();
				System.out.println("-----------------------------------------------");
				for(Object m : hm.toArray()){
					System.out.println(((Match)m).toString());
				}
				System.out.println("-----------------------------------------------");
				System.out.println();
			}
			
		}
		return covs;
	}

	/**
	 * Calculate an object Coverage with all matches from families. 
	 * @param pep Peptide to match
	 */
	public void computeCoverage(Polymer pep) {

		pepTimes=new PeptideExecutionTimes(pep.getName());
		pepTimes.setId(pep.getId());
		pepTimes.setNumAtoms(pep.getMolecule().getAtomCount());// ???

		this.coverage = new Coverage(pep);
		this.matcher.setChemicalObject(pep);
		Isomorphism.setMappingStorage(true);
		
		// Step 1 : Strict Matching
		if (verbose) {
			System.out.println("+Strict matching");
			System.out.println("++Search residues");
		}
		
		long interval1 = this.calculateTimeInterval("strict", "matchAllFamilies.STRONG");
		pepTimes.setIsomorStrictMatchingTime(interval1);
		
		long interval2 = this.calculateTimeInterval("strict", "getCoverageRatio");
		pepTimes.setTilingTime(interval2);
		
		if (ratio < 1.0) {
			if (verbose)
				System.out.println("++Modulation");
			
			long interval3 = this.calculateTimeInterval("strict", "modulate");
			pepTimes.setTilingTime(interval3);
		}
		
		if (!this.allowLightMatchs || ratio == 1.0) {
			Isomorphism.setMappingStorage(false);
			
			pepTimes.setIsomorphismTime();
			pepTimes.setCompleteTime();
			pepsExecutionTimes.get(pep.getName()).add((PeptideExecutionTimes) pepTimes.clone());
			
			List<Coverage> lc =new ArrayList<>();
			lc.add(this.coverage);
			pepsCoveragesList.get(pep.getName()).addAll(lc);
			
			return;
		}
			
		savedTilingTime = pepTimes.getTilingTime();
		
		
		// Step 2 : Light matching
		
		/*if (this.condition == null)
			this.condition = new ExtendsNTimes(this.retry);
		else
			this.condition.init();
		*/
		
		System.out.println("************************pepCoveragesList_1 : ( "+pep.getName()+" )******************************");//test
		showElements(this.pepCoveragesList_1);//test
		
		LightMatching(this.pepCoveragesList_1, this.retry);
		
		Isomorphism.setMappingStorage(false);

	}
	
	private void LightMatching(List<Coverage> pepCoveragesList, int retry){
		
		System.out.println("*********************one light matching start, depth : "+ retry +"************************");//test

		if(retry<=0){
			System.out.println("****************************one depth search stop************************");//test
			return;
		}
		
		for(Coverage cover: pepCoveragesList){
			int retry_saved = retry;
			System.out.println("**********************one depth search start : "+ cover.getCoverageRatio()+ " ************************");//test
			this.coverage = cover;
			pepCoveragesList_n = new ArrayList<Coverage>();
			
			// Successive matchings
			this.remover.init();
			int depth = 0;
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
			long interval4 = this.calculateTimeInterval("light", "matchAllFamilies.LIGHT");
			pepTimes.setIsomorLightMatchingTime(interval4);
				
			long interval5 = this.calculateTimeInterval("light", "calculateGreedyCoverage");
			pepTimes.setTilingTime(interval5);
				
			if (this.coverage.getCoverageRatio() < 1.0) {
				if (verbose)
					System.out.println("++Modulation");
					
				long interval6 = this.calculateTimeInterval("light", "modulate");
				pepTimes.setTilingTime(interval6);
					
				pepsCoveragesList.get(pep.getName()).addAll(pepCoveragesList_n);
					
				System.out.println("************************pepCoveragesList_n : (results list of one depth search) ******************************");//test
				showElements(pepCoveragesList_n);
			}
				
			pepTimes.setIsomorphismTime();
			pepTimes.setCompleteTime();
			pepsExecutionTimes.get(pep.getName()).add((PeptideExecutionTimes) pepTimes.clone());
				
			pepTimes.resetTilingTime(savedTilingTime);
			pepTimes.resetIsomorLightMatchingTime(0);
			pepTimes.setIsomorphismTime(0);// Reset
			pepTimes.resetCompleteTime(0);
				
			this.remover.nextLevel();
				
			LightMatching(pepCoveragesList_n, retry-1);
			retry = retry_saved;
		}
		System.out.println("****************************one light matching stop************************");//test
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
	
	@SuppressWarnings("unchecked")
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
				pepCoveragesList_1 = this.modulation.modulate(this.coverage.clone());
				long endTime = System.currentTimeMillis();
				interval = (endTime-startTime);
				removeRepetition(pepCoveragesList_1);
				
				//test
				/*if(pepCoveragesList_1.size()>=3){
					pepCoveragesList_1 = new ArrayList<Coverage>(pepCoveragesList_1.subList(0, 3));
				}
				else
					pepCoveragesList_1 = new ArrayList<Coverage>(pepCoveragesList_1.subList(0, 1));
				*/
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
					pepCoveragesList_n.add(cov);
				}
				long endTime = System.currentTimeMillis();
				interval = (endTime-startTime);
				removeRepetition(pepCoveragesList_n);
				
				//test
				/*if(pepCoveragesList_n.size()>=3){
					pepCoveragesList_n = new ArrayList<Coverage>(pepCoveragesList_n.subList(0, 3));
				}
				else
					pepCoveragesList_n = new ArrayList<Coverage>(pepCoveragesList_n.subList(0, 1));
				*/
			}
			break;
		}
		
		return interval;
			
	}
	
	private void removeRepetition(List<Coverage> lcov){
		@SuppressWarnings("unchecked")
		List<Coverage> lcov_clone = (ArrayList<Coverage>) ((ArrayList<Coverage>) lcov).clone();
		
			for(int i=0; i<= lcov_clone.size()-2; i++){
				for(int j=i+1; j<=lcov_clone.size()-1; j++){
					if(lcov_clone.get(i).equals(lcov_clone.get(j))){
						lcov.set(j, null);
					}
				}
			}
			List<Coverage> nullList=new ArrayList<>();
			nullList.add(null);
			lcov.removeAll(nullList);
			
			
			/*for(Coverage cov : lcov){
				System.out.println(cov.getChemicalObject().getName()+"****"+cov.getCoverageRatio());
			HashSet<Match> hm = (cov).getUsedMatches();
			System.out.println("------------------new start-----------------------------");
			for(Object m : hm.toArray()){
				System.out.println(((Match)m).toString());
			}
			System.out.println("--------------------new end---------------------------");}*/
			
	}
	
	private void showElements(List<Coverage> lcov){
		for(Coverage c : lcov){
			System.out.println(c.getChemicalObject().getName()+" "+c.getCoverageRatio());
		}
		System.out.println();
	}
	
}
