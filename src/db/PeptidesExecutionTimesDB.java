package db;

import algorithms.utils.PeptideExecutionTimes;

public class PeptidesExecutionTimesDB extends DB<PeptideExecutionTimes>{
	public PeptidesExecutionTimesDB() {
		super();
	}
	
	@Override
	public DB<PeptideExecutionTimes> createNew() {
		return new PeptidesExecutionTimesDB();
	}
	
	@Override
	public void addObject(String id, PeptideExecutionTimes o) {
		if (!this.database.containsKey(id))	
			super.addObject(id, o);
		else {

		}
	}
	
}
