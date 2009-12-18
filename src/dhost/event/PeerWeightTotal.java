package dhost.event;

public class PeerWeightTotal implements Comparable<PeerWeightTotal> {
	private Double weightTotal;
	private int peerID;
	
	public PeerWeightTotal(Integer peerID){
		this.peerID = peerID;
		weightTotal = 0.0;
	}
	
	public Double getWeightTotal(){
		return weightTotal;
	}
	public void addWeight(double x){
		weightTotal+=x;
	}
	public int getPeerID(){
		return peerID;
	}
	
	public int compareTo(PeerWeightTotal anotherWeightTotal)
	{ 
		return this.weightTotal.compareTo(anotherWeightTotal.getWeightTotal());
	}
	public String toString(){
		return "PeerID: "+peerID+" weight: "+weightTotal+"\n";
	}
}
