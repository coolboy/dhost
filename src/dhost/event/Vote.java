package dhost.event;

public class Vote {
	private String voteID;
	
	private long expirationTime;
	
	public Vote(String voteID, long timeout){
		this.voteID = voteID;
	
		this.expirationTime = System.currentTimeMillis()+timeout;
	}
	
	public boolean isExpired(){
		
		if(System.currentTimeMillis()> expirationTime){
			return true;
		}
		else return false;
	}
	
	public String getVoteID(){
		return voteID;
	}
	
	public boolean equals(Vote otherVote){
		return this.voteID.equals(otherVote.getVoteID());
	}
}
