package chatapp;

import com.google.gson.Gson;

public class Message {
	private String contents;
	private int recipientID = 0;
	private int senderID = 0;
	private boolean isPublic = true;
	private String ownerUserName;
	
	public Message(String contents){
		this.contents = contents;
	}
	
	public Message(String contents, int senderID, int recipientID){
		this.contents = contents;
		this.recipientID = recipientID;
		this.senderID = senderID;
		this.isPublic = false;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public int getRecipientID() {
		return recipientID;
	}
	
	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}

	public boolean isPublic(){
		return isPublic;
	}
	
	public void setIsPublic(boolean isPublic){
		this.isPublic = isPublic;
	}

	public String getOwnerUserName() {
		return ownerUserName;
	}

	public void setOwnerUserName(String ownerUserName) {
		this.ownerUserName = ownerUserName;
	}
	
	@Override
	public String toString(){
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
