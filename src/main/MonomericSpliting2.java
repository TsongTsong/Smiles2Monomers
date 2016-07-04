package main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MonomericSpliting2 {
	private String atomsSet;
	private String matchesSet;
	private String matchSize;
	private String atomsOwners;
	
	public MonomericSpliting2(){
		this.atomsSet = " a1 a2 a3 a4\n";
		this.matchesSet = " m1 m2 m3\n";
		this.matchSize = " m1 2\n m2 3\n m3 2\n";
		this.atomsOwners = "   m1 m2 m3 :=\n a1 1 0 1\n a2 1 1 1\n a3 0 1 0\n a4 1 1 0\n";
	}
	
	public void useTemplateGLPK(){	
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("TemplateAndModel_GLPK/TemplateGLPK.mod"));
			BufferedWriter bw = new BufferedWriter(new FileWriter("TemplateAndModel_GLPK/ModelGLPK.mod"));
		
			String line;
			int count = 0;
			while(!((line=br.readLine()).equals("#end of template file"))){
				switch(count){
					case 17:
						bw.write(this.atomsSet);
						System.out.println("add a line 18");
						break;
					case 20:
						bw.write(this.matchesSet);
						System.out.println("add a line 21");
						break;
					case 24:
						bw.write(this.matchSize);
						System.out.println("add a line 25");
						break;
					case 27:
						bw.write(this.atomsOwners);
						System.out.println("add a line 28");
						break;
					default:
						bw.write(line+'\n');
						System.out.println("add a line");
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
	
	
	
	public static void main(String[] args) {
		MonomericSpliting2 ms = new MonomericSpliting2();
		ms.useTemplateGLPK();
	}

}
