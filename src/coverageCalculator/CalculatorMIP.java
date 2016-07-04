package coverageCalculator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.interfaces.IAtom;
import algorithms.utils.Coverage;
import algorithms.utils.Match;
import model.Polymer;

public class CalculatorMIP implements Calculator{
	private ArrayList<Integer> atomsOfPol;
	private ArrayList<Match> matchesOfPol;
	private ArrayList<Integer> sizeOfMatches;
	
	private String atomsSet;
	private String matchesSet;
	private String matchSize;
	private String atomsOwners;
	
	private int[] resultArray;
	private ArrayList<Match> matchesToUse;
	
	private String modelDirAbsPath;
	private String modelFileAbsPath;
	
	public CalculatorMIP(){
		atomsOfPol = new ArrayList<>();
		matchesOfPol = new ArrayList<>();
		sizeOfMatches = new ArrayList<>();
		matchesToUse = new ArrayList<>();
	}

	@Override
	public List<Coverage> process(Polymer pol, Coverage cov) {
		this.clearVaribles();
		this.prepareData(pol, cov);
		this.useTemplateGLPK(pol);
		
		//for test (linux), to add for windows
		Runtime rt = Runtime.getRuntime();
		try {
			String str = "glpsol -m "+ this.modelFileAbsPath + " -o " + this.modelDirAbsPath + "/ResultGLPK_" + pol.getName().replace(' ', '_') +".sol";
			Process pr = rt.exec(str);
			pr.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.getResults(pol);
		
		for(int i=0; i<this.resultArray.length; i++){
			if(this.resultArray[i]==1){
				this.matchesToUse.add(this.matchesOfPol.get(i));
			}
		}
		cov.calculateMIPCoverage(this.matchesToUse);
		List<Coverage> covs = new ArrayList<Coverage>();
		covs.add(cov);
		
		return covs;
	}

	// Prepare data of "atomSet, matchesSet, matchSize, atomsOwners" 
	// and change their format to string for using useTemplateGLPK function later
	private void prepareData(Polymer pol, Coverage cov){
		for(IAtom a : pol.getMolecule().atoms()){
			this.atomsOfPol.add(pol.getMolecule().getAtomNumber(a));
		}
		this.atomsSet = toSet4GLPK('a', this.atomsOfPol);
		
	    outer:
		for(Match m : cov.getMatches()){
			for(Match m2 : this.matchesOfPol){
				if(m.toString().equals(m2.toString())){
					continue outer;
				}
			}
			this.matchesOfPol.add(m);
		}
		this.matchesSet = toSet4GLPK('m', this.matchesOfPol);
		
		for(Match m : this.matchesOfPol){
			this.sizeOfMatches.add(m.size());
		}
		this.matchSize = toParam4GLPK(this.matchesOfPol);
		
		this.atomsOwners = toMatrix4GLPK();
		
		this.resultArray = new int[this.matchesOfPol.size()];
	}
	
	private void getResults(Polymer pol){
		try {
			BufferedReader br = new BufferedReader(new FileReader("TemplateModelResult_GLPK/ResultGLPK_" + pol.getName().replace(' ', '_') +".sol"));
			while(!br.readLine().equals("   No. Column name       Activity     Lower bound   Upper bound")){
				br.readLine();
			}
			br.readLine();
			
			String line = "";
			int k = 0;
			for(int i=0; i<this.matchesOfPol.size(); i++){
				line = this.removeChar(br.readLine(), ' ');
				this.resultArray[k++] = Integer.valueOf(line.substring(line.length()-3, line.length()-2));
			}
			br.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String removeChar(String resource, char ch){  
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
			set += " " + a_m + i;
		}
		set += '\n';
		return set;
	}
	
	private String toParam4GLPK(ArrayList<Match> al){
		String set = "";
		for(int i=0; i<al.size(); i++){
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
	
	private void useTemplateGLPK(Polymer pol){	
		try {
			BufferedReader br = new BufferedReader(new FileReader("TemplateModelResult_GLPK/TemplateGLPK.mod"));
			File outModelFile = new File("TemplateModelResult_GLPK/ModelGLPK_"+ pol.getName().replace(' ', '_') +".mod");
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
	
	private void clearVaribles(){
		this.atomsOfPol.clear();
		this.matchesOfPol.clear();
		this.sizeOfMatches.clear();
		this.matchesToUse.clear();
		
		this.atomsSet="";
		this.matchesSet="";
		this.matchSize="";
		this.atomsOwners="";
		
		this.modelDirAbsPath="";
		this.modelFileAbsPath="";
	}
}
