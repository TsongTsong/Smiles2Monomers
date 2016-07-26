package algorithms.utils;

public class PeptideExecutionTimes implements Cloneable{
	private String id;
	private String peptideName;
	private long numAtoms;
	
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
		return this.id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getPeptideName() {
		return this.peptideName;
	}


	public void setPeptideName(String peptideName) {
		this.peptideName = peptideName;
	}


	public long getNumAtoms() {
		return this.numAtoms;
	}


	public void setNumAtoms(long numAtoms) {
		this.numAtoms = numAtoms;
	}


	public long getCompleteTime() {
		return this.completeTime;
	}


	public void setCompleteTime(long completeTime) {
		this.completeTime = completeTime;
	}


	public long getIsomorphismTime() {
		return this.isomorphismTime;
	}


	public void setIsomorphismTime(long isomorphismTime) {
		this.isomorphismTime = isomorphismTime;
	}


	public long getIsomorStrictMatchingTime() {
		return this.isomorStrictMatchingTime;
	}


	public void setIsomorStrictMatchingTime(long isomorStrictMatchingTime) {
		this.isomorStrictMatchingTime = isomorStrictMatchingTime;
	}


	public long getIsomorLightMatchingTime() {
		return this.isomorLightMatchingTime;
	}


	public void setIsomorLightMatchingTime(long isomorLightMatchingTime) {
		this.isomorLightMatchingTime = isomorLightMatchingTime;
	}


	public long getTilingTime() {
		return this.tilingTime;
	}


	public void setTilingTime(long tilingTime) {
		this.tilingTime = tilingTime;
	}


	public String toString(){
		return this.getId()+", "+this.getPeptideName()+", "+this.getNumAtoms()+", "+this.getCompleteTime()+"{ "+
				this.getIsomorphismTime()+"[ "+this.getIsomorStrictMatchingTime()+", "+this.getIsomorLightMatchingTime() +" ], "+
					this.getTilingTime()+" }";
	}

	public Object clone(){  
	    try{  
	        return super.clone();  
	    }catch(Exception e){ 
	        return null; 
	    }
	}


}
