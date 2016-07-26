package main;

import java.io.File;
import java.util.Map;

import algorithms.MonomericSpliting;
import algorithms.isomorphism.chains.ChainsDB;
import algorithms.utils.Coverage;
import coverageCalculator.Calculator;
import coverageCalculator.CalculatorMIP;
import coverageCalculator.CalculatorMIP_TM;
import coverageCalculator.CalculatorTM;
import db.FamilyDB;
import db.MonomersDB;
import db.PolymersDB;
import db.RulesDB;
import io.html.Coverages2HTML;
import io.imgs.ImagesGeneration;
import io.imgs.PictureCoverageGenerator.ColorsMap;
import io.loaders.json.CoveragesJsonLoader;
import io.loaders.json.FamilyChainIO;
import io.loaders.json.MonomersJsonLoader;
import io.loaders.json.PeptidesCovRatioCorrJsonLoader;
import io.loaders.json.PeptidesExecutionTimesJsonLoader;
import io.loaders.json.PolymersJsonLoader;
import io.loaders.json.ResidueJsonLoader;
import io.loaders.json.RulesJsonLoader;
import io.loaders.serialization.MonomersSerialization;
import io.zip.OutputZiper;

public class ProcessPolymers {

	public static void main(String[] args) {
		//----------------- Parameters ---------------------------
		String monoDBname = "data/monomers.json";
		String pepDBname = "data/polymers.json";
		String rulesDBname = "data/rules.json";
		String residuesDBname = "data/residues.json";
		String chainsDBFile = "data/chains.json";
		
		String timesFileName="results/peptidesExecutionTimes.json";
		String covRatioCorr_TM_FileName="results/pepsCovRatioCorr_TM.json";
		String covRatioCorr_MIP_FileName="results/pepsCovRatioCorr_MIP.json";
		
		String outfile = "results/coverages.json";
		String outfolderName = "results/";
		String imgsFoldername = "images/";
		boolean html = false;
		boolean stats = false;
		boolean merge = false;
		boolean zip = false;
		boolean tm = false;
		boolean mip = false;
		boolean mip_tm = false;
		
		String serialFolder = "data/serials/";
		
		boolean lightMatch = true;
		boolean verbose = false;
		int removeDistance = 2;
		int retryCount = 2;
		int modulationDepth = 2;
		
		// Parsing
		loop:
		for (int idx=0 ; idx<args.length ; idx++) {
			if (args[idx].startsWith("-")) {
				switch (args[idx]) {
				case "-rul":
					rulesDBname = args[idx+1];
					break;
				case "-mono":
					monoDBname = args[idx+1];
					break;
				case "-poly":
					pepDBname = args[idx+1];
					break;
				case "-res":
					residuesDBname = args[idx+1];
					break;
				case "-cha":
					chainsDBFile = args[idx+1];
					break;
				case "-serial":
					serialFolder = args[idx+1];
					break;
				case "-outfile":
					outfile = args[idx+1];
					break;
				case "-outfolder":
					outfolderName = args[idx+1];
					break;
				case "-imgs":
					imgsFoldername = args[idx+1];
					break;
				case "-strict":
					lightMatch = false;
					continue loop;
				case "-v":
					verbose = true;
					continue loop;
				case "-html":
					html = true;
					continue loop;
				case "-stats":
					stats = true;
					continue loop;
				case "-merge":
					merge = true;
					continue loop;
				case "-zip":
					zip = true;
					continue loop;
				case "-tm":
					tm = true;
					continue loop;
				case "-mip":
					mip = true;
					continue loop;
				case "-mip_tm":
					mip_tm = true;
					continue loop;

				default:
					System.err.println("Wrong option " + args[idx]);
					System.exit(1);
					break;
				}
				
				idx++;
			} else {
				System.err.println("Wrong parameter " + args[idx]);
				System.exit(1);
			}
		}
		
		// Merge coverage's ratio and correctness from TM and from MIP
		if(merge){
			RatioCorrMerger.merge(covRatioCorr_TM_FileName, covRatioCorr_MIP_FileName);
			return;
		}		
		
		//------------------- Loadings ------------------------
		System.out.println("--- Loading ---");
		// Maybe loading can be faster for the learning base, using serialized molecules instead of CDK SMILES parsing method.
		long loadingTime = System.currentTimeMillis();
		MonomersDB monoDB = new MonomersJsonLoader(false).loadFile(monoDBname);
		MonomersSerialization ms = new MonomersSerialization();
		ms.deserialize(monoDB, serialFolder + "monos.serial");
		
		boolean d2 = html || zip;
		PolymersJsonLoader pjl = new PolymersJsonLoader(monoDB, d2);
		PolymersDB polDB = pjl.loadFile(pepDBname);
		
		RulesDB rulesDB = RulesJsonLoader.loader.loadFile(rulesDBname);
		
		ResidueJsonLoader rjl = new ResidueJsonLoader(rulesDB, monoDB);
		
		FamilyDB families = rjl.loadFile(residuesDBname); // Need optimizations
		FamilyChainIO fcio = new FamilyChainIO(families);
		ChainsDB chains = fcio.loadFile(chainsDBFile);
		
		loadingTime = System.currentTimeMillis() - loadingTime;
		System.out.println("Loading time : " + (loadingTime/1000) + "s");
		
		
		//------------------- Spliting ------------------------
		System.out.println("--- Monomers search ---");
		
		long searchTime = System.currentTimeMillis();
		MonomericSpliting.setVerbose(verbose);
		Coverage[] covs = null;
		MonomericSpliting split = null;
		Calculator cal = null;
		
		if(tm){
			cal = new CalculatorTM(modulationDepth);
		}
		else if(mip){
			cal = new CalculatorMIP();
		}
		else if(mip_tm){
			cal = new CalculatorMIP_TM(modulationDepth);
		}
		
		split = new MonomericSpliting(families, chains, removeDistance, retryCount, cal);
		
		split.setAllowLightMatchs(lightMatch);
		covs = split.computeCoverages(polDB);
		
		searchTime = System.currentTimeMillis() - searchTime;
		System.out.println("Search time : " + (searchTime/1000) + "s");
		
		
		//------------------- Output ------------------------
		System.out.println("--- Output creations ---");
		long outputTime = System.currentTimeMillis();
		
		// Creation of the out directory
		File outfolder = new File(outfolderName);
		if (!outfolder.exists())
			outfolder.mkdir();
		
		CoveragesJsonLoader cjl = new CoveragesJsonLoader(polDB, families);
		cjl.saveFile(covs, outfile);
		
		// Images and Statistics generation
		if (html || stats || merge || zip) {
			File imgsFolder = new File(imgsFoldername);
			if (!imgsFolder.exists())
				imgsFolder.mkdir();
			
			ImagesGeneration ig = new ImagesGeneration();
			ig.generateMonomerImages(imgsFolder, monoDB);
			
			Map<Coverage, ColorsMap> colors = ig.generatePeptidesImages(imgsFolder, covs);
			
			if (html) {
				// HTML
				Coverages2HTML c2h = new Coverages2HTML(covs, monoDB, families);
				File htmlFile  = new File(outfolderName + "/s2m.html");
				c2h.createResults(htmlFile, imgsFolder, colors);
			}
			
			if(stats) {
				//PeptidesExecutionTimesJsonLoader petjl = new PeptidesExecutionTimesJsonLoader();
				//petjl.saveFile(MonomericSpliting.pepsExecutionTimes, timesFileName);
				
				PeptidesCovRatioCorrJsonLoader pcrcjl = new PeptidesCovRatioCorrJsonLoader();
				
				if(cal instanceof CalculatorTM){
					pcrcjl.saveFile(MonomericSpliting.pepCovRatioCorrArray, covRatioCorr_TM_FileName);

				}else if(cal instanceof CalculatorMIP){
					pcrcjl.saveFile(MonomericSpliting.pepCovRatioCorrArray, covRatioCorr_MIP_FileName);			
				}				
			}
			
			if (zip) {
				// Zip File
				OutputZiper oz = new OutputZiper(outfolderName + "/s2m.zip");
				oz.createZip(imgsFolder.getPath(), outfile, pepDBname, monoDBname, residuesDBname, timesFileName, colors);
			}
		}			
		
		outputTime = System.currentTimeMillis() - outputTime;
		System.out.println("Ouputing time : " + (outputTime/1000) + "s");
		System.out.println("--- Ended ---");
		
	}

}
