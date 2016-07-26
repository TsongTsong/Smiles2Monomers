package db;

import algorithms.utils.PeptideCovRatioCorr;

public class PeptidesCovRatioCorrDB extends DB<PeptideCovRatioCorr>{
	public PeptidesCovRatioCorrDB() {
		super();
	}
	
	@Override
	public DB<PeptideCovRatioCorr> createNew() {
		return new PeptidesCovRatioCorrDB();
	}
	
	@Override
	public void addObject(String id, PeptideCovRatioCorr o) {
		if (!this.database.containsKey(id))	
			super.addObject(id, o);
		else {

		}
	}
	
}
