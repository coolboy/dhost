package dhost.net;

import java.util.*;
import java.lang.Math;


/*The class is constructed on any Collection of Peer objects.
 * The following method is used to determine where to send a message:
 * 
 * Vector<Integer> getDestinationVector(Integer thisPeerID, Integer msgSenderPeerID, Integer msgOriginPeerID)
 * 
 * It takes three peer ID's as arguments, and returns a vector of the peer ID's that
 * the message should be sent to. The input arguments are, in order, the peer ID of the
 * local peer, the peer ID of the peer that sent the message to the local peer, and the
 * peer ID of the peer the message originated at. Two or more can be the same peer ID,
 * for example if the local peer is the origin of the message, all three arguments will
 * be the same.
 */

public class NetworkMap{

	//vector of Peers in the network
	 private Vector<Peer> peerVector;
	 
	 //maps Integer peerIDs to the Integer index of that peer in the peerVector vector
	 private HashMap<Integer,Integer> IDtoIndex;
	 
	 //contains all the groupings of peers, where a group is represented
	 //by a vector of Integer peerID's
	 private Vector<Vector<Integer>> peerGroups;
	 private HashMap<Integer, Integer> IDtoGroupNumMap;
	 
	 //maps each Peer(via peerID) to a vector containing the peerIDs of the
	 //Peers outside of this peer's group that this peer is responsible for
	 //sending messages to.
	 private HashMap<Integer, Vector<Integer>> interGroupConnections;


	 public NetworkMap(Collection<Peer> peers){
		 peerVector = new Vector<Peer>();
		 peerVector.addAll(peers);
		 Collections.sort(peerVector);
		 initializeIndexIDMap();
		 initializeTree();
	 }



	 private void initializeTree(){
		 createSqrtNGroupMap();
	 }

	 private void initializeIndexIDMap(){
		 IDtoIndex  = new HashMap<Integer,Integer>();
		 for(int i= 0; i < peerVector.size();i++){
			 IDtoIndex.put((peerVector.get(i)).getID(), i);
		 }
	 }
	 
	 

	 
	 private void createSqrtNGroupMap(){
		 //choose number of groups (and nodes per group) to be
		 //the truncated-to-int square root of total number of peers
		 int numberOfGroups = (int)Math.sqrt((double)peerVector.size());
		 peerGroups = new Vector<Vector<Integer>>(numberOfGroups);
		 for(int i =0;i<numberOfGroups;i++){
			 peerGroups.add(new Vector<Integer>(numberOfGroups+2));
		 }
		 assignSqrtNPeerGroups();
		 assignInterGroupConnections();
		 
	 }
	 
	 //assigns the rounded-down-square-root of N peers to each group, then round robin's the
	 //remaining nodes, which will be no more than 2 more peers per group
	 //also fills in the HashMap that maps peerIDs to group numbers
	 private void assignSqrtNPeerGroups(){
		 int peerVectorIndex = 0;
		 IDtoGroupNumMap = new  HashMap<Integer, Integer>();
		 //the for loop can just be removed to round robin every peer
		 for(int i =0; i < peerGroups.size();i++){
			 for(int k = 0; k<peerGroups.size();k++){
				 (peerGroups.get(i)).add((peerVector.get(peerVectorIndex).getID()));
				 IDtoGroupNumMap.put((peerVector.get(peerVectorIndex).getID()), new Integer(i));
				 peerVectorIndex++;
			 }
		 }
		 int peerGroupsIndex = 0;
		 while(peerVectorIndex<peerVector.size()){
			 (peerGroups.get(peerGroupsIndex%peerGroups.size())).add((peerVector.get(peerVectorIndex).getID()));
			 IDtoGroupNumMap.put((peerVector.get(peerVectorIndex).getID()), new Integer(peerGroupsIndex%peerGroups.size()));
			 peerVectorIndex++;
			 peerGroupsIndex++;
		 }
	 }
	 
	 //assigns inter group communication responsibilities so that every group
	 //has a peer assigned to every other group
	 private void assignInterGroupConnections(){
		 interGroupConnections= new HashMap<Integer, Vector<Integer>>();
		 for(int j= 0; j<peerVector.size();j++){
			 interGroupConnections.put(peerVector.get(j).getID(),new Vector<Integer>(2));
		 }
		 for(int i =0;i<peerGroups.size();i++){
			 for(int k = i+1;k<peerGroups.size();k++){
				 connectPeerGroups(i,k);
			 }
		 }
		 
	 }
	 
	 //selects a peer from group a and a peer from group b,
	 //and assigns them the responsibility of sending messages to each other
	 private void connectPeerGroups(int groupA,int groupB){
		 Integer groupAPeerID = selectResponsiblePeer(groupA);
		 Integer groupBPeerID = selectResponsiblePeer(groupB);
		 interGroupConnections.get(groupAPeerID).add(groupBPeerID);
		 interGroupConnections.get(groupBPeerID).add(groupAPeerID);
	 }
	 
	 //returns the peerID of a Peer in group 'groupIndex' with the lowest
	 //number of already assigned connections to other groups.
	 private Integer selectResponsiblePeer(int groupIndex){
		 
		 Integer lowestLoadID = peerGroups.get(groupIndex).get(0);
		 int lowestLoad =  interGroupConnections.get(lowestLoadID).size();
		 for(int i = 1;i<peerGroups.get(groupIndex).size();i++){
			 if(lowestLoad==0)return lowestLoadID;
			 if(lowestLoad>interGroupConnections.get(peerGroups.get(groupIndex).get(i)).size()){
				 lowestLoadID = peerGroups.get(groupIndex).get(i);
				 lowestLoad =  interGroupConnections.get(lowestLoadID).size();
			 }
		 }
		 return lowestLoadID;
	 
	 }
	 
	 public boolean peersAreInSameGroup(Integer peerAID, Integer peerBID){
		 return IDtoGroupNumMap.get(peerAID).equals(IDtoGroupNumMap.get(peerBID));
	 }
	 
	
	 /*
	  * Method that is essentially the whole point of this class. It serves as the routing function
	  * for a given message. Takes the peerID of the peer that recieved a message and is calling
	  * the function, the peerID of the peer whom the message was recieved from, and the peerID of
	  * the peerID of the peer who originally generated the message.
	  * is calling it, the peerID of the 
	  */
	 public Vector<Integer> getDestinationVector(Integer thisPeerID, Integer msgSenderPeerID, Integer msgOriginPeerID){
		 
		 Vector<Integer> destVector = new Vector<Integer>();
		 if(thisPeerID.equals(msgOriginPeerID)){
			 destVector.addAll(peerGroups.get(IDtoGroupNumMap.get(thisPeerID)));
			 destVector.removeElement(thisPeerID);
			 destVector.addAll(interGroupConnections.get(thisPeerID));			 
		 }
		 else if(peersAreInSameGroup(thisPeerID,msgSenderPeerID)){
			 if(peersAreInSameGroup(thisPeerID,msgOriginPeerID)){
				 destVector.addAll(interGroupConnections.get(thisPeerID));
			 }
			 else return destVector;//leave destvector empty, meaning i do not need to pass the message on to anyone
				 
			 
		 }
		 else{
			 destVector.addAll(peerGroups.get(IDtoGroupNumMap.get(thisPeerID)));
			 destVector.removeElement(thisPeerID);
		 }
		 
		 
		 return destVector;
		 
		 
	 }
	 
	 //debug method, prints the peer ids of all members of group groupNum
	 public void printGroup(int groupNum){
		 if(groupNum>=peerGroups.size()){
			 System.out.println("Group number out of range");
		 }
		 else{
		 for(int i=0;i<peerGroups.get(groupNum).size();i++){
			 System.out.println(peerGroups.get(groupNum).get(i));
		 }
		 }
	 }



}