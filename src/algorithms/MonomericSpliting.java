package algorithms;

import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.IAtom;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import algorithms.isomorphism.ChainsFamilyMatching;
import algorithms.isomorphism.FamilyMatcher;
import algorithms.isomorphism.Isomorphism;
import algorithms.isomorphism.MatchingType;
import algorithms.isomorphism.chains.ChainsDB;
import algorithms.isomorphism.conditions.ConditionToExtend;
import algorithms.isomorphism.conditions.ExtendsNTimes;
import algorithms.isomorphism.indepth.DeepenMatcher;
import algorithms.isomorphism.indepth.Modulation_2;
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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class MonomericSpliting {

	public static boolean verbose = false;
	private Coverage coverage;// Current coverage
	private Map<String, ArrayList<Coverage>> pepsCoveragesList;
	private Map<Coverage, ArrayList<Integer>> covSearchedDepth;// Save searched Depths of a coverage
	private Map<Coverage, ArrayList<Coverage>> covToListCovs;// Save coverage list searched from a coverage
	private List<Coverage> pepCoveragesList_1;// Modulation after strict matching
	private List<Coverage> pepCoveragesList_n;// Modulation after each light matching
	private Map<String, ArrayList<PeptideExecutionTimes>> pepsExecutionTimes;
	private double coverRatio;// Which should be defined as a parameter by user
	private FamilyDB families;
	private boolean allowLightMatchs;
	private FamilyMatcher matcher;
	private ConditionToExtend condition;
	private RemoveStrategy remover;
	private Modulation_2 modulation;
	private String timesFile="results/peptidesExecutionTimes.json";
	private PeptideExecutionTimes[] peps_times;
	public static int countPeps;// Number of peptides
	private double ratio;
	public static int numFullCoverage;// = 100%
	public static int numPartCoverage_1;// 80% <= numPartCoverage_1 < 100%
	public static int numPartCoverage_2;// < 80%
	private int retry;
	
	private PeptideExecutionTimes pepTimes;
	private Polymer pep;
	private long savedTilingTime;
	private List<Polymer> findNoCoverPols;
	private double decrease;
	private boolean continueSearch;
	private int diffAtomsNumToleration;// Number of different covered atoms between two coverage
	private boolean useDiffAtomsNumToleration;
	private boolean stopOnceFullCoverGot;// False : can get other possibilities of full coverages and part coverages
	private int num;// Number of light
	
	// Variables for using GLPK
	private ArrayList<Integer> atomsOfPol;
	private ArrayList<Match> matchesOfPol;
	private ArrayList<Integer> sizeOfMatches;
	
	private int[] selected;
	private ArrayList<Match> matchesToUse;
	
	private String atomsSet;
	private String matchesSet;
	private String matchSize;
	private String atomsOwners;
	
	private String modelDirAbsPath;
	private String modelFileAbsPath;
	
	private void clearVaribles(){
		this.atomsOfPol.clear();
		this.matchesOfPol.clear();
		this.sizeOfMatches.clear();
		
		this.matchesToUse.clear();
		
		this.atomsSet="";
		this.matchesSet="";
		this.matchSize="";
		this.atomsOwners="";
		
	}
	
	/*
	 * Use of GLPK for coverages
	 */
	public MonomericSpliting(FamilyDB families, ChainsDB chains){
		this.families = families;
		this.matcher = new ChainsFamilyMatching(chains);
		atomsOfPol = new ArrayList<>();
		matchesOfPol = new ArrayList<>();
		sizeOfMatches = new ArrayList<>();
		matchesToUse = new ArrayList<>();
	}
	
	public Coverage[] computeCoverages2(PolymersDB polDB){
		Coverage[] covs = new Coverage[polDB.size()];// One polymer one best coverage 
		
		for (Polymer pol : polDB.getObjects()){
			System.out.println("###### "+pol.getName() +  "  "+pol.getMolecule().getAtomCount());
			this.computeCoverage2(pol);
			break;//test
		}
		
		return covs;
	}
	
	public void computeCoverage2(Polymer pep){
		this.coverage = new Coverage(pep);
		this.matcher.setChemicalObject(pep);
		Isomorphism.setMappingStorage(true);
		
		this.prepareData(pep, "strong");
		this.useTemplateGLPK(pep);
		
		//for test (linux), to add for windows
		Runtime rt = Runtime.getRuntime();
		try {
			String str = "glpsol -m "+ this.modelFileAbsPath + " -o " + this.modelDirAbsPath + "/ResultGLPK_" + pep.getName().replace(' ', '_') +".sol";
			System.out.println(str);
			Process pr = rt.exec(str);
			pr.waitFor();
			//BufferedReader br = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			//String line = null;
			//while ((line = br.readLine()) != null)
				//System.out.println(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.getResults(pep);
		
		System.out.println("length :"+this.selected.length);
		for(int i : this.selected){
			System.out.print(i + " ");
		}
		
		for(int i=0; i<this.selected.length; i++){
			if(this.selected[i]==1){
				this.matchesToUse.add(this.matchesOfPol.get(i));
				//System.out.println("++++++ : "+ this.matchesOfPol.get(i).getAtoms().toString());//test
			}
		}
		this.coverage.calculateMIPCoverage(this.matchesToUse);
		/*for(Match m : this.coverage.getUsedMatches()){
			System.out.println("------ : " + m.getAtoms().toString());
		}*/
		
		//System.out.println("xxxxxxxxxxxxxx"+this.coverage.getCoverageRatio());
		
		while(this.coverage.getCoverageRatio() < 1.0){
			
			
		}
		
		Isomorphism.setMappingStorage(false);
	}
	
	// Prepare data of "atomSet, matchesSet, matchSize, atomsOwners" 
	// and change their format to string for using useTemplateGLPK function
	private void prepareData(Polymer pep, String matchingType){
		for(IAtom a : pep.getMolecule().atoms()){
			System.out.println("****** "+pep.getMolecule().getAtomNumber(a));
			this.atomsOfPol.add(pep.getMolecule().getAtomNumber(a));
		}
		this.atomsSet = toSet4GLPK('a', this.atomsOfPol);
		
		if(matchingType.equals("strong")){
			this.matchAllFamilies(MatchingType.STRONG);// Yi kai shi jiu mei you strict matching de case
		}
		else if(matchingType.equals("light")){
			this.matchAllFamilies(MatchingType.LIGHT);
		}
		
	    outer:
		for(Match m : this.coverage.getMatches()){
			for(Match m2 : this.matchesOfPol){
				if(m.toString().equals(m2.toString())){
					continue outer;
				}
			}
			this.matchesOfPol.add(m);
			System.out.println("====== "+m.toString());
		}
		this.matchesSet = toSet4GLPK('m', this.matchesOfPol);
		
		for(Match m : this.matchesOfPol){
			this.sizeOfMatches.add(m.size());
		}
		this.matchSize = toParam4GLPK(this.matchesOfPol);
		
		this.atomsOwners = toMatrix4GLPK();
		
		this.selected = new int[this.matchesOfPol.size()];
	}
	
	private void getResults(Polymer pep){
		try {
			BufferedReader br = new BufferedReader(new FileReader("TemplateModelResult_GLPK/ResultGLPK_" + pep.getName().replace(' ', '_') +".sol"));
			while(!br.readLine().equals("   No. Column name       Activity     Lower bound   Upper bound")){
				br.readLine();
			}
			br.readLine();
			
			String line = "";
			int k = 0;
			for(int i=0; i<this.matchesOfPol.size(); i++){
				line = this.remove(br.readLine(), ' ');
				//System.out.println("line+++++: "+line);
				this.selected[k++] = Integer.valueOf(line.substring(line.length()-3, line.length()-2));
			}
			br.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String remove(String resource, char ch){  
        StringBuffer buffer=new StringBuffer();      
        int position=0;      
        char currentChar;       

        while(position<resource.length()){               
              currentChar=resource.charAt(position++);        
              if(currentChar!=ch) buffer.append(currentChar); 
        }
        return buffer.toString();       
	}    
	
	private int atomInMatch(int numOfAtom, Match match){
		Object[] atomsInMatch = match.getAtoms().toArray();
		for(int i=0; i<atomsInMatch.length; i++){
			if(numOfAtom == (int)(atomsInMatch[i])){
				return 1;
			}
		}
		return 0;
	}
	
	private String toSet4GLPK(char a_m, ArrayList<?> al){
		String set = "";
		for(int i=0; i<al.size(); i++){
			//set += " " + a_m + al.get(i);
			set += " " + a_m + i;
		}
		set += '\n';
		return set;
	}
	
	private String toParam4GLPK(ArrayList<Match> al){
		String set = "";
		for(int i=0; i<al.size(); i++){
			//set += " m" + al.get(i) + " " + al.get(i).size() + '\n';
			set += " m" + i + " " + al.get(i).getAtoms().size() + '\n';
		}
		return set;
	}
	
	private String toMatrix4GLPK(){
		String atomsOwners = "  "+this.matchesSet.substring(0, this.matchesSet.length()-1) + " :=\n";
		for(int i=0; i<this.atomsOfPol.size(); i++){
			int numAtom = this.atomsOfPol.get(i);
			atomsOwners += " a"+numAtom;
			for(int j=0; j<this.matchesOfPol.size();j++){
				atomsOwners += " "+atomInMatch(numAtom, this.matchesOfPol.get(j));
			}
			atomsOwners += '\n';
		}
		return atomsOwners;
	}
	
	private void useTemplateGLPK(Polymer pep){	
		try {
			BufferedReader br = new BufferedReader(new FileReader("TemplateModelResult_GLPK/TemplateGLPK.mod"));
			File outModelFile = new File("TemplateModelResult_GLPK/ModelGLPK_"+ pep.getName().replace(' ', '_') +".mod");
			BufferedWriter bw = new BufferedWriter(new FileWriter(outModelFile));
			
			this.modelFileAbsPath = outModelFile.getAbsolutePath();
			this.modelDirAbsPath = new File("TemplateModelResult_GLPK").getAbsolutePath();
		
			String line;
			int count = 0;
			while(!((line=br.readLine()).equals("#end of template file"))){
				switch(count){
					case 17:
						bw.write(this.atomsSet);
						break;
					case 20:
						bw.write(this.matchesSet);
						break;
					case 24:
						bw.write(this.matchSize);
						break;
					case 27:
						bw.write(this.atomsOwners);
						break;
					default:
						bw.write(line+'\n');
						break;
				}
			
				count++;
			}
			bw.close();
			br.close();
			
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	
	/*
	 * Use of Tiling & Modulation for coverages
	 */
	public MonomericSpliting(FamilyDB families, ChainsDB chains, int removeDistance, int retryCount, int modulationDepth) {
		this.families = families;
		this.matcher = new ChainsFamilyMatching(chains);
		this.remover = new RemoveByDistance(removeDistance);
		this.coverRatio = 0.9;
		//this.modulation = new Modulation_2(modulationDepth, this.coverRatio);
		this.retry = retryCount;

		pepsCoveragesList  = new HashMap<String, ArrayList<Coverage>>();
		pepsExecutionTimes = new HashMap<String, ArrayList<PeptideExecutionTimes>>();
		findNoCoverPols = new ArrayList<>();
		decrease = 0.1;
		covSearchedDepth = new HashMap<Coverage, ArrayList<Integer>>();
		covToListCovs = new HashMap<Coverage, ArrayList<Coverage>>();
		
		diffAtomsNumToleration = 1;
		useDiffAtomsNumToleration = true;
		stopOnceFullCoverGot = true;	
		this.num = 0;
	}
	
	@SuppressWarnings("unchecked")
	public Coverage[] computeCoverages (PolymersDB polDB) {
		for (Polymer pol : polDB.getObjects()) {
			pepsCoveragesList.put(pol.getName(), new ArrayList<Coverage>());
			pepsExecutionTimes.put(pol.getName(), new ArrayList<PeptideExecutionTimes>());
			pepCoveragesList_1 = new ArrayList<Coverage>();
			this.pep=pol;
			
			this.continueSearch = true;	
			this.num = 0;
			this.computeCoverage(pol);
			
			if(pepsCoveragesList.get(pol.getName()).size() == 0){
				findNoCoverPols.add(pol);
				System.out.println("find one polymer having no coverage");
			}
			countPeps++;
		}
		int co=0;
		while(findNoCoverPols.size() != 0){
			
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++ search for polymers who havn't coverages finded"+co++);
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++ "+findNoCoverPols.size());
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++ search for polymers who havn't coverages finded");
			this.coverRatio = this.coverRatio - this.decrease;
			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& "+ this.coverRatio);
			if(this.coverRatio <= 0){
				for(Polymer p : findNoCoverPols){
					System.out.println("############################ "+ p.getName());
				}
				break;
			}
			
			for(int i=0; i<findNoCoverPols.size(); i++){
				Polymer pol = findNoCoverPols.get(i);
				continueSearch = true;
				this.computeCoverage(pol);
				if(pepsCoveragesList.get(pol.getName()).size() != 0){
					findNoCoverPols.remove(pol);
				}
			}
		}
		
		System.out.println("= = = = = =Number of peptides : " + countPeps+ " Peptides having no coverage: "+findNoCoverPols.size());
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
		
		if(stopOnceFullCoverGot){
			for(ArrayList<Coverage> cs: pepsCoveragesList.values()){
				for(Coverage c : (ArrayList<Coverage>)cs.clone()){
					if(c.getCoverageRatio()==1.0){
						onlyRetain_1(cs);
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
	@SuppressWarnings("unchecked")
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
		
		//long interval2 = this.calculateTimeInterval("strict", "getCoverageRatio");
		long interval2 = this.calculateTimeInterval("strict", "calculateGreedyCoverage");		
		pepTimes.setTilingTime(interval2);
		
		if (ratio < 1.0) {
			if (verbose)
				System.out.println("++Modulation");
			
			long interval3 = this.calculateTimeInterval("strict", "modulate");
			pepTimes.setTilingTime(interval3);
		}
		else if(ratio == 1.0){
			pepCoveragesList_1 = new ArrayList<Coverage>();
			pepCoveragesList_1.add(this.coverage);
			
			pepsCoveragesList.get(pep.getName()).addAll(pepCoveragesList_1);
			covToListCovs.put(this.coverage, (ArrayList<Coverage>) pepCoveragesList_1);// Save coverage list
		}
		
		// To add case for 'stopOnceFullCoverGot==false': pepCoveragesList_1.size()>1 && pepCoveragesList_1.get(i).getCoverageRatio() != 1.0
		if (!this.allowLightMatchs || 
				(pepCoveragesList_1.size()==1 && pepCoveragesList_1.get(0).getCoverageRatio() == 1.0)) {
			
			Isomorphism.setMappingStorage(false);
			
			pepTimes.setIsomorphismTime(pepTimes.getIsomorStrictMatchingTime());
			pepTimes.setCompleteTime(pepTimes.getIsomorphismTime()+pepTimes.getTilingTime());
			
			pepsExecutionTimes.get(pep.getName()).add((PeptideExecutionTimes) pepTimes.clone());
			
			pepsCoveragesList.get(pep.getName()).addAll((ArrayList<Coverage>) ((ArrayList<Coverage>) pepCoveragesList_1).clone());
			
			return;
		}
			
		savedTilingTime = pepTimes.getTilingTime();
		pepTimes.setTilingTime(0);
		pepTimes.setCompleteTime(0);
		
		// Step 2 : Light matching
		
		/*if (this.condition == null)
			this.condition = new ExtendsNTimes(this.retry);
		else
			this.condition.init();
		*/
		
		LightMatching(this.pepCoveragesList_1, this.retry);
		
		System.out.println("//////////////////////////////////////////////////////////////////////// : "+num);
		pepTimes.setIsomorLightMatchingTime(pepTimes.getIsomorLightMatchingTime()/num);
		pepTimes.setIsomorphismTime(pepTimes.getIsomorLightMatchingTime()+pepTimes.getIsomorStrictMatchingTime());
		pepTimes.setTilingTime(pepTimes.getTilingTime()/num + savedTilingTime);
		pepTimes.setCompleteTime(pepTimes.getIsomorphismTime()+pepTimes.getTilingTime());
		pepsExecutionTimes.get(pep.getName()).add((PeptideExecutionTimes) pepTimes.clone());
		
		Isomorphism.setMappingStorage(false);
	}
	
	@SuppressWarnings("unchecked")
	private void LightMatching(List<Coverage> pepCoveragesList, int retry){
		
		System.out.println("*********************one light matching start, depth : "+ retry +"************************");//test
		System.out.println("******** pepCoveragesList.size: "+pepCoveragesList.size());
		
		if(retry == 1){
			num += pepCoveragesList.size();
		}
		
		//to remove stopOnceFullCoverGot ???
		if(stopOnceFullCoverGot == true && pepCoveragesList.size()==1 && pepCoveragesList.get(0).getCoverageRatio()==1.0){
			continueSearch = false;
			return;
		}
		
		if(retry<=0){
			System.out.println("****************************one light matching stop 2************************");//test
			return;
		}
		
		label:
		for(Coverage cover: pepCoveragesList){    
			
			int retry_saved = retry;
			System.out.println("**********************one search start : "+ cover.getCoverageRatio()+ " ************************");//test
			this.coverage = cover;// Current running coverage 
			pepCoveragesList_n = new ArrayList<Coverage>();
			
			if(covSearchedDepth.keySet().size() != 0){
				for(Coverage cov : covSearchedDepth.keySet()){
					if(cover.equals(cov)){
						System.out.println("##################################################### two coverages are equal");
						if(!covSearchedDepth.get(cov).contains(retry)){										
							pepCoveragesList_n = covToListCovs.get(cov);
							covSearchedDepth.get(cov).add(retry);
							System.out.println("##################################################### add new seached depth for a coverage 2");	
							
							System.out.println("****************************reuse a calculated result start************************");//test
							LightMatching(pepCoveragesList_n, retry-1);
							retry = retry_saved;
							System.out.println("****************************reuse a calculated result stop************************");//test
							continue label;
						}
						else{
							System.out.println("##################################################### calculer another coverage directly");
							if(retry - 1 >=1){
								num++;
							}
							continue label;
						}
					}
				}
			}
			
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
				
			long interval4 = this.calculateTimeInterval("light", "matchAllFamilies.LIGHT");
			pepTimes.setIsomorLightMatchingTime(pepTimes.getIsomorLightMatchingTime()+interval4);
				
			long interval5 = this.calculateTimeInterval("light", "calculateGreedyCoverage");
			pepTimes.setTilingTime(pepTimes.getTilingTime()+interval5);
				
			if (this.coverage.getCoverageRatio() < 1.0) {
				if (verbose)
					System.out.println("++Modulation");
					
				long interval6 = this.calculateTimeInterval("light", "modulate");
				pepTimes.setTilingTime(pepTimes.getTilingTime()+interval6);
					
				pepsCoveragesList.get(pep.getName()).addAll((ArrayList<Coverage>) ((ArrayList<Coverage>) pepCoveragesList_n).clone());
				covToListCovs.put(cover, (ArrayList<Coverage>) pepCoveragesList_n);// Save coverage list  
					
				covSearchedDepth.put(cover, new ArrayList<Integer>());
				covSearchedDepth.get(cover).add(retry);
				System.out.println("##################################################### add new seached depth for a coverage 1");
				
				System.out.println("************************pepCoveragesList_n : (results list of one depth search) ******************************");//test
				showElements(pepCoveragesList_n);
			}
			else if (this.coverage.getCoverageRatio() == 1.0){
				pepCoveragesList_n = new ArrayList<Coverage>();
				pepCoveragesList_n.add(this.coverage);
				
				pepsCoveragesList.get(pep.getName()).addAll(pepCoveragesList_n);
				covToListCovs.put(cover, (ArrayList<Coverage>) pepCoveragesList_n);// Save coverage list
				
				covSearchedDepth.put(cover, new ArrayList<Integer>());
				covSearchedDepth.get(cover).add(retry);
			}
				
			this.remover.nextLevel();
				
			LightMatching(pepCoveragesList_n, retry-1);
			retry = retry_saved;
			if(stopOnceFullCoverGot == true && continueSearch == false){
				System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& Search process terminated");
				return;
			}
		}
		System.out.println("****************************one light matching stop 1************************");//test
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
			//else if(detail.equals("getCoverageRatio")){
			else if(detail.equals("calculateGreedyCoverage")){
				long startTime = System.currentTimeMillis();
				this.coverage.calculateGreedyCoverage();//new add
				long endTime = System.currentTimeMillis();
				this.ratio = this.coverage.getCoverageRatio();
				interval = (endTime-startTime);
			}
			else if(detail.equals("modulate")){
				long startTime = System.currentTimeMillis();
				pepCoveragesList_1 = this.modulation.modulate(this.coverage.clone());
				long endTime = System.currentTimeMillis();
				interval = (endTime-startTime);
				removeRepetition(pepCoveragesList_1);
				
				if(stopOnceFullCoverGot == true){
					onlyRetain_1(pepCoveragesList_1);
				}

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
				
				if(stopOnceFullCoverGot == true){
					onlyRetain_1(pepCoveragesList_n);
				}				
			}
			break;
		}
		
		return interval;
			
	}
	
	// Class Coverage has been changed too
	private void removeRepetition(List<Coverage> lcov){
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
	
	@SuppressWarnings("unchecked")
	private void onlyRetain_1(List<Coverage> pepCoveragesList){
		for(Coverage ce : (ArrayList<Coverage>) ((ArrayList<Coverage>) pepCoveragesList).clone()){
			if(ce.getCoverageRatio() == 1.0){
				List<Coverage> exemple = new ArrayList<>();
				exemple.add(ce);
				pepCoveragesList.retainAll(exemple);
			}
		}
	}
	
	private void showElements(List<Coverage> lcov){
		for(Coverage c : lcov){
			System.out.println(c.getChemicalObject().getName()+" "+c.getCoverageRatio());
		}
		System.out.println();
	}
	
}
