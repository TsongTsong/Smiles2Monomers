package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import algorithms.utils.PeptideCovRatioCorr;
import io.loaders.json.PeptidesCovRatioCorrJsonLoader;

public class RatioCorrMerger {
	private static Map<String, ArrayList<PeptideCovRatioCorr>> ratioCorrMap_TM;
	private static Map<String, ArrayList<PeptideCovRatioCorr>> ratioCorrMap_MIP;
	private static String covRatioCorr_TM_MIP_FileName="results/pepsCovRatioCorr_TMvsMIP.json";
	
	public static void merge(String path_ratiocorr_tm, String path_ratiocorr_mip){
		ratioCorrMap_TM = new HashMap<String, ArrayList<PeptideCovRatioCorr>>();
		ratioCorrMap_MIP = new HashMap<String, ArrayList<PeptideCovRatioCorr>>();
		
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
		/*for(List<PeptideCovRatioCorr> lp : ratioCorrMap_TM.values()){
			for(int i=0; i<lp.size(); i++){
				System.out.println("#################################################"+lp.get(i).getPepName());
			}
			
		}*/
		
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
		/*for(List<PeptideCovRatioCorr> lp : ratioCorrMap_MIP.values()){
			for(int i=0; i<lp.size(); i++){
				System.out.println("*************************************************"+lp.get(i).getPepName());
			}
			
		}*/
		
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
		}
		
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
		
		PeptidesCovRatioCorrJsonLoader pcrcjl3 = new PeptidesCovRatioCorrJsonLoader();
		pcrcjl3.saveFile(pcrcArray, covRatioCorr_TM_MIP_FileName);
		
		System.out.println("Merge process terminated.");
		
	}
	
}
