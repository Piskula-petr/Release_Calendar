package cz.release_calendar.pojo;

public class MoviesTotalCount {

	private long previousTotal;
	private long nextTotal;
	
// Konstruktor /////////////////////////////////////////////////////////////////////////////
	
	public MoviesTotalCount() {
		
	}

// Gettery + Settery ///////////////////////////////////////////////////////////////////////
	
	public long getPreviousTotal() {
		return previousTotal;
	}
	
	public void setPreviousTotal(long previousTotal) {
		this.previousTotal = previousTotal;
	}

	public long getNextTotal() {
		return nextTotal;
	}

	public void setNextTotal(long nextTotal) {
		this.nextTotal = nextTotal;
	}
	
}
