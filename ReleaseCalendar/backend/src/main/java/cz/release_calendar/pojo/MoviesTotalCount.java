package cz.release_calendar.pojo;

public class MoviesTotalCount {

	private Long previousTotal;
	private Long nextTotal;
	
// Konstruktor /////////////////////////////////////////////////////////////////////////////
	
	public MoviesTotalCount() {
		
	}

// Gettery + Settery ///////////////////////////////////////////////////////////////////////
	
	public Long getPreviousTotal() {
		return previousTotal;
	}
	
	public void setPreviousTotal(Long previousTotal) {
		this.previousTotal = previousTotal;
	}

	public Long getNextTotal() {
		return nextTotal;
	}

	public void setNextTotal(Long nextTotal) {
		this.nextTotal = nextTotal;
	}
	
}
