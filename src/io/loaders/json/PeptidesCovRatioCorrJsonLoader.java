package io.loaders.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import algorithms.utils.PeptideCovRatioCorr;
import db.PeptidesCovRatioCorrDB;

public class PeptidesCovRatioCorrJsonLoader extends AbstractJsonLoader<PeptidesCovRatioCorrDB, PeptideCovRatioCorr>{
	
	@Override
	protected PeptidesCovRatioCorrDB createDB() {
		return new PeptidesCovRatioCorrDB();
	}
	
	@SuppressWarnings("unchecked")
	protected JSONArray getArrayOfElements(PeptideCovRatioCorr pcrc) {
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		obj.put("ID", pcrc.getId());
		obj.put("PolName", pcrc.getPepName());
		obj.put("MIP_Ratio", pcrc.getCovRatio_MIP());
		obj.put("MIP_Correctness", pcrc.getCovCorrectness_MIP());		
		obj.put("TM_Ratio", pcrc.getCovRatio_TM());	
		obj.put("TM_Correctness", pcrc.getCovCorrectness_TM());		
		obj.put("MIPTM_Ratio", pcrc.getCovRatio_MIPTM());
		obj.put("MIPTM_Correctness", pcrc.getCovCorrectness_MIPTM());
	
		array.add(obj);
		return array;
	}
	
	protected PeptideCovRatioCorr objectFromJson(JSONObject obj){
		PeptideCovRatioCorr pcrc = new PeptideCovRatioCorr();
		
		pcrc.setId(String.valueOf(obj.get("ID")));
		pcrc.setPepName((String) obj.get("PolName"));
		pcrc.setCovRatio_TM((String) obj.get("TM_Ratio"));
		pcrc.setCovCorrectness_TM((String) obj.get("TM_Correctness"));	
		pcrc.setCovRatio_MIP((String) obj.get("MIP_Ratio"));
		pcrc.setCovCorrectness_MIP((String) obj.get("MIP_Correctness"));
		pcrc.setCovRatio_MIPTM((String) obj.get("MIPTM_Ratio"));
		pcrc.setCovCorrectness_MIPTM((String) obj.get("MIPTM_Correctness"));
		
		return pcrc;
	}

	@Override
	protected String getObjectId(PeptideCovRatioCorr tObj) {
		return String.valueOf(tObj.getId());
	}
	
}
