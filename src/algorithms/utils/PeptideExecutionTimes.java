package algorithms.utils;

/**
 *	new add 
 */
public class PeptideExecutionTimes {
	private String id;
	private String peptideName;
	private long numAtoms;
	private double coverRatio;
	
	private long completeTime;
	  private long isomorphismTime;
	    private long isomorStrictMatchingTime;
	    private long isomorLightMatchingTime;
	  private long tilingTime;
	
	public PeptideExecutionTimes(String peptideName) {
		super();
		this.peptideName = peptideName;
		
		this.completeTime = 0;
		  this.isomorphismTime = 0;
			 this.isomorStrictMatchingTime = 0;
			 this.isomorLightMatchingTime = 0;
		  this.tilingTime = 0;
	}
	
	
	
	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getPeptideName() {
		return peptideName;
	}
	
	
	
	public void setPeptideName(String peptideName) {
		this.peptideName = peptideName;
	}



	public long getNumAtoms() {
		return numAtoms;
	}



	public void setNumAtoms(long numAtoms) {
		this.numAtoms = numAtoms;
	}



	public double getCoverRatio() {
		return coverRatio;
	}



	public void setCoverRatio(double coverRatio) {
		this.coverRatio = coverRatio;
	}



	public long getCompleteTime() {
		return this.completeTime;
	}



	public void setCompleteTime() {		
		this.completeTime = this.getIsomorphismTime() + this.getTilingTime();
	}

	
	public void setCompleteTime(long isomorphismTime, long tilingTime){
		this.completeTime = isomorphismTime + tilingTime;
	}
	
	
	public void setCompleteTime(long completeTime){
		this.completeTime = completeTime;
	}


	public long getIsomorphismTime() {
		return this.isomorphismTime;
	}



	public void setIsomorphismTime() {
		long preIsomorphismTime = this.getIsomorphismTime();
		this.isomorphismTime = this.getIsomorStrictMatchingTime() + this.getIsomorLightMatchingTime() + preIsomorphismTime;
	}

	

	public void setIsomorphismTime(long isomorStrictMatchingTime, long isomorLightMatchingTime) {
		long preIsomorphismTime = this.getIsomorphismTime();
		this.isomorphismTime = isomorStrictMatchingTime + isomorLightMatchingTime + preIsomorphismTime;
	}
	
	
	public void setIsomorphismTime(long isomorphismTime){
		this.isomorphismTime = isomorphismTime;
	}
	
	

	public long getIsomorStrictMatchingTime() {
		return isomorStrictMatchingTime;
	}



	public void setIsomorStrictMatchingTime(long isomorStrictMatchingTime) {
		long preIsomorStrictMatchingTime = this.getIsomorStrictMatchingTime();
		this.isomorStrictMatchingTime = isomorStrictMatchingTime + preIsomorStrictMatchingTime;
	}



	public long getIsomorLightMatchingTime() {
		return isomorLightMatchingTime;
	}



	public void setIsomorLightMatchingTime(long isomorLightMatchingTime) {
		long preIsomorLightMatchingTime = this.getIsomorLightMatchingTime();
		this.isomorLightMatchingTime = isomorLightMatchingTime + preIsomorLightMatchingTime;
	}



	public long getTilingTime() {
		return tilingTime;
	}



	public void setTilingTime(long tilingTime) {
		long preTilingTime = this.getTilingTime();
		this.tilingTime = tilingTime + preTilingTime;
	}
	
	public String toString(){
		System.out.printf("%10s, %20s, %10s, %30s", "***"+this.getId(), this.getPeptideName(), this.getNumAtoms(), this.getCompleteTime()+"{ "+
				this.getIsomorphismTime()+"[ "+this.getIsomorStrictMatchingTime()+", "+this.getIsomorLightMatchingTime() +" ], "+
				this.getTilingTime()+" }");
		System.out.println();
		
		return this.getId()+", "+this.getPeptideName()+", "+this.getNumAtoms()+", "+this.getCompleteTime()+"{ "+
				this.getIsomorphismTime()+"[ "+this.getIsomorStrictMatchingTime()+", "+this.getIsomorLightMatchingTime() +" ], "+
					this.getTilingTime()+" }";
	}


}
