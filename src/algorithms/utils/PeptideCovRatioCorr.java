package algorithms.utils;

/**
 * Ratio & Correctness of a coverage of a Polymer
 */
public class PeptideCovRatioCorr {
	private String id;
	private String pepName;
	private String covRatio_TM;
	private String covCorrectness_TM;
	private String covRatio_MIP;
	private String covCorrectness_MIP;
	private String covRatio_MIPTM;
	private String covCorrectness_MIPTM;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPepName() {
		return pepName;
	}

	public void setPepName(String pepName) {
		this.pepName = pepName;
	}

	public String getCovRatio_TM() {
		return covRatio_TM;
	}

	public void setCovRatio_TM(String covRatio_TM) {
		this.covRatio_TM = covRatio_TM;
	}

	public String getCovCorrectness_TM() {
		return covCorrectness_TM;
	}

	public void setCovCorrectness_TM(String covCorrectness_TM) {
		this.covCorrectness_TM = covCorrectness_TM;
	}

	public String getCovRatio_MIP() {
		return covRatio_MIP;
	}

	public void setCovRatio_MIP(String covRatio_MIP) {
		this.covRatio_MIP = covRatio_MIP;
	}

	public String getCovCorrectness_MIP() {
		return covCorrectness_MIP;
	}

	public void setCovCorrectness_MIP(String covCorrectness_MIP) {
		this.covCorrectness_MIP = covCorrectness_MIP;
	}

	public String getCovRatio_MIPTM() {
		return covRatio_MIPTM;
	}

	public void setCovRatio_MIPTM(String covRatio_MIPTM) {
		this.covRatio_MIPTM = covRatio_MIPTM;
	}

	public String getCovCorrectness_MIPTM() {
		return covCorrectness_MIPTM;
	}

	public void setCovCorrectness_MIPTM(String covCorrectness_MIPTM) {
		this.covCorrectness_MIPTM = covCorrectness_MIPTM;
	}

	public PeptideCovRatioCorr clone(){
		PeptideCovRatioCorr pcc = new PeptideCovRatioCorr();
		
		pcc.setId(this.id);
		pcc.setPepName(this.pepName);
		pcc.setCovRatio_TM(this.covRatio_TM);
		pcc.setCovCorrectness_TM(this.covCorrectness_TM);
		pcc.setCovRatio_MIP(this.covRatio_MIP);
		pcc.setCovCorrectness_MIP(this.covCorrectness_MIP);
		pcc.setCovRatio_MIPTM(this.covRatio_MIPTM);
		pcc.setCovCorrectness_MIPTM(this.covCorrectness_MIPTM);
		
		return pcc;
		
	}
	
}
