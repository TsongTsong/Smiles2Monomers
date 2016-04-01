package io.loaders.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import algorithms.utils.PeptideExecutionTimes;
import db.PeptidesExecutionTimesDB;
/**
 * new add
 */
public class PeptidesExecutionTimesJsonLoader extends AbstractJsonLoader<PeptidesExecutionTimesDB, PeptideExecutionTimes>{
	
	@Override
	protected PeptidesExecutionTimesDB createDB() {
		return new PeptidesExecutionTimesDB();
	}
	
	@SuppressWarnings("unchecked")
	protected JSONArray getArrayOfElements(PeptideExecutionTimes pet) {
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();

		//System.out.println("*** "+pet);//test
		pet.toString();//test
		obj.put("PeptideID", pet.getId());
		obj.put("PepNumAtoms", pet.getNumAtoms());
		obj.put("PeptideName", pet.getPeptideName());
		//obj.put("PepCoverageRatio", pet.getCoverRatio());
		obj.put("PepExecutionCompleteTime", pet.getCompleteTime());
		obj.put("PepExecIsomorphismTime", pet.getIsomorphismTime());
		obj.put("PepExecIsomorStrictMatchTime", pet.getIsomorStrictMatchingTime());
		obj.put("PepExecIsomorLightMatchTime", pet.getIsomorLightMatchingTime());
		obj.put("PepExecTilingTime", pet.getTilingTime());
		
		array.add(obj);
		return array;
	}
	
	protected String getObjectId(PeptideExecutionTimes tObj) {
		return String.valueOf(tObj.getId());
	}
	
	protected PeptideExecutionTimes objectFromJson(JSONObject obj){
		PeptideExecutionTimes pet=new PeptideExecutionTimes((String)obj.get("PeptideName"));
		
		pet.setId((String)obj.get("PeptideID"));
		pet.setNumAtoms((long) (obj.get("PepNumAtoms")));
		//pet.setCoverRatio((double)obj.get("PepCoverageRatio"));
		pet.setCompleteTime((long)obj.get("PepExecutionCompleteTime"));
		pet.setIsomorphismTime((long)obj.get("PepExecIsomorphismTime"));
		pet.setIsomorStrictMatchingTime((long)obj.get("PepExecIsomorStrictMatchTime"));
		pet.setIsomorLightMatchingTime((long)obj.get("PepExecIsomorLightMatchTime"));
		pet.setTilingTime((long)obj.get("PepExecTilingTime"));
		
		return pet;
	}
	
}
