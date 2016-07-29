package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.icu.math.BigDecimal;

import algorithms.utils.PeptideCovRatioCorr;
import io.loaders.json.PeptidesCovRatioCorrJsonLoader;

public class RatioCorrMerger {
	private static Map<String, ArrayList<PeptideCovRatioCorr>> ratioCorrMap_TM;
	private static Map<String, ArrayList<PeptideCovRatioCorr>> ratioCorrMap_MIP;
	private static Map<String, ArrayList<PeptideCovRatioCorr>> ratioCorrMap_MIPTM;
	private static String covRatioCorr_TM_MIP_FileName="results/pepsCovRatioCorr_TMvsMIPvsMIPTM.json";
	
	public static void merge(String path_ratiocorr_tm, String path_ratiocorr_mip, String path_ratiocorr_miptm){
		ratioCorrMap_TM = new HashMap<String, ArrayList<PeptideCovRatioCorr>>();
		ratioCorrMap_MIP = new HashMap<String, ArrayList<PeptideCovRatioCorr>>();
		ratioCorrMap_MIPTM = new HashMap<String, ArrayList<PeptideCovRatioCorr>>();
		
		PeptidesCovRatioCorrJsonLoader pcrcjl1 = new PeptidesCovRatioCorrJsonLoader();
		List<PeptideCovRatioCorr>  ratioCorrList_TM= pcrcjl1.loadFile(path_ratiocorr_tm).getObjects();
		
		for(PeptideCovRatioCorr pcrc : ratioCorrList_TM){
			String pepName = pcrc.getPepName();
			if(!ratioCorrMap_TM.containsKey(pepName)){				
				ratioCorrMap_TM.put(pepName, new ArrayList<PeptideCovRatioCorr>());
				ratioCorrMap_TM.get(pepName).add(pcrc);
			}
			else{
				ratioCorrMap_TM.get(pepName).add(pcrc);
			}
		}

		PeptidesCovRatioCorrJsonLoader pcrcjl2 = new PeptidesCovRatioCorrJsonLoader();
		List<PeptideCovRatioCorr>  ratioCorrList_MIP = pcrcjl2.loadFile(path_ratiocorr_mip).getObjects();
		for(PeptideCovRatioCorr pcrc : ratioCorrList_MIP){
			String pepName = pcrc.getPepName();
			if(!ratioCorrMap_MIP.containsKey(pepName)){
				ratioCorrMap_MIP.put(pepName, new ArrayList<PeptideCovRatioCorr>());
				ratioCorrMap_MIP.get(pepName).add(pcrc);
			}
			else{
				ratioCorrMap_MIP.get(pepName).add(pcrc);
			}
		}

		PeptidesCovRatioCorrJsonLoader pcrcjl3 = new PeptidesCovRatioCorrJsonLoader();
		List<PeptideCovRatioCorr>  ratioCorrList_MIPTM = pcrcjl3.loadFile(path_ratiocorr_miptm).getObjects();
		for(PeptideCovRatioCorr pcrc : ratioCorrList_MIPTM){
			String pepName = pcrc.getPepName();
			if(!ratioCorrMap_MIPTM.containsKey(pepName)){
				ratioCorrMap_MIPTM.put(pepName, new ArrayList<PeptideCovRatioCorr>());
				ratioCorrMap_MIPTM.get(pepName).add(pcrc);
			}
			else{
				ratioCorrMap_MIPTM.get(pepName).add(pcrc);
			}
		}
		
		for(List<PeptideCovRatioCorr>  values_ratioCorrMap_TM : ratioCorrMap_TM.values()){
			String pepName = values_ratioCorrMap_TM.get(0).getPepName();
			if(values_ratioCorrMap_TM.size() >= ratioCorrMap_MIP.get(pepName).size()){
				for(int i=0; i<ratioCorrMap_MIP.get(pepName).size(); i++){
					values_ratioCorrMap_TM.get(i).setCovRatio_MIP(
							ratioCorrMap_MIP.get(pepName).get(i).getCovRatio_MIP());
					values_ratioCorrMap_TM.get(i).setCovCorrectness_MIP(
							ratioCorrMap_MIP.get(pepName).get(i).getCovCorrectness_MIP());
				}
				
			}else{
				for(int i=0; i<ratioCorrMap_MIP.get(pepName).size(); i++){
					if(i+1 > ratioCorrMap_TM.get(pepName).size()){
						PeptideCovRatioCorr pcrc = new PeptideCovRatioCorr();
						pcrc.setPepName(pepName);
						pcrc.setCovRatio_MIP(ratioCorrMap_MIP.get(pepName).get(i).getCovRatio_MIP());
						pcrc.setCovCorrectness_MIP(ratioCorrMap_MIP.get(pepName).get(i).getCovCorrectness_MIP());
						ratioCorrMap_TM.get(pepName).add(pcrc);
						
					}else{
						values_ratioCorrMap_TM.get(i).setCovRatio_MIP(
								ratioCorrMap_MIP.get(pepName).get(i).getCovRatio_MIP());
						values_ratioCorrMap_TM.get(i).setCovCorrectness_MIP(
								ratioCorrMap_MIP.get(pepName).get(i).getCovCorrectness_MIP());			
					}
						
				}
			}
			
			if(values_ratioCorrMap_TM.size() >= ratioCorrMap_MIPTM.get(pepName).size()){
				for(int i=0; i<ratioCorrMap_MIPTM.get(pepName).size(); i++){
					values_ratioCorrMap_TM.get(i).setCovRatio_MIPTM(
							ratioCorrMap_MIPTM.get(pepName).get(i).getCovRatio_MIPTM());
					values_ratioCorrMap_TM.get(i).setCovCorrectness_MIPTM(
							ratioCorrMap_MIPTM.get(pepName).get(i).getCovCorrectness_MIPTM());
				}
				
			}else{
				for(int i=0; i<ratioCorrMap_MIPTM.get(pepName).size(); i++){
					if(i+1 > ratioCorrMap_TM.get(pepName).size()){
						PeptideCovRatioCorr pcrc = new PeptideCovRatioCorr();
						pcrc.setPepName(pepName);
						pcrc.setCovRatio_MIPTM(ratioCorrMap_MIPTM.get(pepName).get(i).getCovRatio_MIPTM());
						pcrc.setCovCorrectness_MIPTM(ratioCorrMap_MIPTM.get(pepName).get(i).getCovCorrectness_MIPTM());
						ratioCorrMap_TM.get(pepName).add(pcrc);
						
					}else{
						values_ratioCorrMap_TM.get(i).setCovRatio_MIPTM(
								ratioCorrMap_MIPTM.get(pepName).get(i).getCovRatio_MIPTM());
						values_ratioCorrMap_TM.get(i).setCovCorrectness_MIPTM(
								ratioCorrMap_MIPTM.get(pepName).get(i).getCovCorrectness_MIPTM());			
					}
						
				}
			}
		}
		
		int TM_Ratio_1=0, TM_Ratio_2=0;	
		int TM_Corr_1=0, TM_Corr_2=0;		
		int MIP_Ratio_1=0, MIP_Ratio_2=0; 
		int MIP_Corr_1=0, MIP_Corr_2=0;	
		int MIPTM_Ratio_1=0, MIPTM_Ratio_2=0; 
		int MIPTM_Corr_1=0, MIPTM_Corr_2=0;
		int numOfPeps=0;
		for(List<PeptideCovRatioCorr> pcrcList : ratioCorrMap_TM.values()){
			numOfPeps++;
			if(Double.valueOf(pcrcList.get(0).getCovRatio_TM()) == 1.0){
				TM_Ratio_1++;
			}else if(Double.valueOf(pcrcList.get(0).getCovRatio_TM()) >= 0.9){
				TM_Ratio_2++;
			}
			if(Double.valueOf(pcrcList.get(0).getCovCorrectness_TM()) == 1.0){
				TM_Corr_1++;
			}else if(Double.valueOf(pcrcList.get(0).getCovCorrectness_TM()) >= 0.9){
				TM_Corr_2++;
			}
			
			if(Double.valueOf(pcrcList.get(0).getCovRatio_MIP()) == 1.0){
				MIP_Ratio_1++;
			}else if(Double.valueOf(pcrcList.get(0).getCovRatio_MIP()) >= 0.9){
				MIP_Ratio_2++;
			}
			if(Double.valueOf(pcrcList.get(0).getCovCorrectness_MIP()) == 1.0){
				MIP_Corr_1++;
			}else if(Double.valueOf(pcrcList.get(0).getCovCorrectness_MIP()) >= 0.9){
				MIP_Corr_2++;
			}
			
			if(Double.valueOf(pcrcList.get(0).getCovRatio_MIPTM()) == 1.0){
				MIPTM_Ratio_1++;
			}else if(Double.valueOf(pcrcList.get(0).getCovRatio_MIPTM()) >= 0.9){
				MIPTM_Ratio_2++;
			}
			if(Double.valueOf(pcrcList.get(0).getCovCorrectness_MIPTM()) == 1.0){
				MIPTM_Corr_1++;
			}else if(Double.valueOf(pcrcList.get(0).getCovCorrectness_MIPTM()) >= 0.9){
				MIPTM_Corr_2++;
			}
		}
		double tr1=formatData((double)TM_Ratio_1*100/numOfPeps);
		double tr2=formatData((double)TM_Ratio_2*100/numOfPeps);
		double tc1=formatData((double)TM_Corr_1*100/numOfPeps);
		double tc2=formatData((double)TM_Corr_2*100/numOfPeps);
		
		double mr1=formatData((double)MIP_Ratio_1*100/numOfPeps);
		double mr2=formatData((double)MIP_Ratio_2*100/numOfPeps);
		double mc1=formatData((double)MIP_Corr_1*100/numOfPeps);
		double mc2=formatData((double)MIP_Corr_2*100/numOfPeps);
		
		double mtr1=formatData((double)MIPTM_Ratio_1*100/numOfPeps);
		double mtr2=formatData((double)MIPTM_Ratio_2*100/numOfPeps);
		double mtc1=formatData((double)MIPTM_Corr_1*100/numOfPeps);
		double mtc2=formatData((double)MIPTM_Corr_2*100/numOfPeps);
		System.out.println(
				"Under this case( Peptides Number:"+numOfPeps+" ) :\n"+			
						"TM2   ratio=1.0      Num : "+TM_Ratio_1+ "      "+ tr1 +"%\n"+
						"TM2   0.9<=ratio<1.0 Num : "+TM_Ratio_2+ "      "+ tr2 +"%\n"+
						"TM2   corr=1.0       Num : "+TM_Corr_1+ "       "+ tc1 +"%\n"+
						"TM2   0.9<=corr<1.0  Num : "+TM_Corr_2+ "       "+ tc2 +"%\n\n"+
						
						"MIP   ratio=1.0      Num : "+MIP_Ratio_1+ "     "+ mr1 +"%\n"+
						"MIP   0.9<=ratio<1.0 Num : "+MIP_Ratio_2+ "     "+ mr2 +"%\n"+
						"MIP   corr=1.0       Num : "+MIP_Corr_1+ "      "+ mc1 +"%\n"+
						"MIP   0.9<=corr<1.0  Num : "+MIP_Corr_2+ "      "+ mc2 +"%\n\n"+
						
						"MIPTM ratio=1.0      Num : "+MIPTM_Ratio_1+ "   "+ mtr1 +"%\n"+
						"MIPTM 0.9<=ratio<1.0 Num : "+MIPTM_Ratio_2+ "   "+ mtr2 +"%\n"+
						"MIPTM corr=1.0       Num : "+MIPTM_Corr_1+ "    "+ mtc1 +"%\n"+
						"MIPTM 0.9<=corr<1.0  Num : "+MIPTM_Corr_2+ "    "+ mtc2 +"%\n"
				);
		System.out.println("Number of Ratio & Correctness got.\n");
		
		
		// Save ratio and correctness of TM, MIP and MIP-TM to file
		int size = 0;
		for(List<PeptideCovRatioCorr> pcrcList : ratioCorrMap_TM.values()){
			size += pcrcList.size();
		}
		PeptideCovRatioCorr[] pcrcArray = new PeptideCovRatioCorr[size];
		int j =0;
		for(List<PeptideCovRatioCorr> pcrcList : ratioCorrMap_TM.values()){
			for(int i=0; i<pcrcList.size(); i++){
				pcrcArray[j++] = pcrcList.get(i);
			}			
		}
		
		PeptidesCovRatioCorrJsonLoader pcrcjl4 = new PeptidesCovRatioCorrJsonLoader();
		pcrcjl4.saveFile(pcrcArray, covRatioCorr_TM_MIP_FileName);
		
		System.out.println("Merge process terminated.");
		
	}
	
	private static double formatData(double d){
		return new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
}
