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

public class MonomericSpliting {

	public static boolean verbose = false;
	
	private Coverage coverage;
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
		this.modulation = new Modulation(modulationDepth);
		this.retry = retryCount;
		
		
	}
	
	public Coverage[] computeCoverages (PolymersDB polDB) {
		Coverage[] covs = new Coverage[polDB.size()];
		peps_times=new PeptideExecutionTimes[polDB.size()];//new add
		int idx=0;
		for (Polymer pol : polDB.getObjects()) {
			this.computeCoverage(pol);
			covs[idx] = this.getCoverage();
			idx += 1;	
		}
		
		//System.out.println("****"+idx);;
		System.out.println("====Number of peptides : "+count);//new add
		PeptidesExecutionTimesJsonLoader petjl=new PeptidesExecutionTimesJsonLoader();//new add
		petjl.saveFile(peps_times, timesFile);//new add
		
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
		Coverage save = this.coverage.clone();
		
		// conditions to go to light matching
		if (!this.allowLightMatchs || ratio == 1.0) {
			Isomorphism.setMappingStorage(false);
			
			pepTimes.setIsomorphismTime();//new add
			pepTimes.setCompleteTime();//new add
			
			peps_times[count]=pepTimes;//new add
			count++;//new add
			
			if(ratio == 1.0){
				numFullCoverage++;//new add
			}
			pepTimes.setCoverRatio(ratio);//new add
			
			return;
		}
			
		
		// Step 2 : Light matching
		// Initialization
		// TODO : Why if/else ?
		if (this.condition == null)
			this.condition = new ExtendsNTimes(this.retry);
		else
			this.condition.init();
		
		this.remover.init();
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
			
			
			if (this.coverage.getCoverageRatio() > save.getCoverageRatio())
				save = this.coverage.clone();
			
			this.remover.nextLevel();
		}
		Isomorphism.setMappingStorage(false);
		
		if (save.getCoverageRatio() > this.coverage.getCoverageRatio())
			this.coverage = save;
		
		pepTimes.setIsomorphismTime();//new add
		pepTimes.setCompleteTime();//new add
		
		peps_times[count]=pepTimes;//new add
		count++;//new add
		
		this.ratio = this.coverage.getCoverageRatio();
		if(ratio < 0.8){
			numPartCoverage_2++;
		}
		else if(ratio == 1.0){
			numFullCoverage++;
		}
		else{
			numPartCoverage_1++;
		}
		pepTimes.setCoverRatio(ratio);//new add

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
				this.coverage = this.modulation.modulate(this.coverage);
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
				this.coverage = this.modulation.modulate(this.coverage);
				long endTime = System.currentTimeMillis();
				interval = (endTime-startTime);
			}
			break;
		}
		
		return interval;
			
	}
	
}
