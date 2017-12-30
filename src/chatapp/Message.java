package chatapp;

import com.google.gson.Gson;

public class Message {
	private String contents;
	private int recipientID = 0;
	private boolean isPublic = true;
	private String ownerUserName;
	
	public Message(String contents){
		this.contents = contents;
	}
	
	public Message(String contents, int recipientID){
		this.contents = contents;
		this.recipientID = recipientID;
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
	
	public boolean isPublic(){
		return isPublic;
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
