package chatapp;

import java.io.*;
import java.net.*;


public class ChatClient implements Runnable {
	private OutputStream outputToServer;
	private Socket socket;
	private int ID;
	private ClientUI clientUI;
	private boolean hasUI = false;
	private String username;
	private UserTableModel userTableModel;

	public ChatClient(String username, int ID, Socket socket) {
		this.username = username;
		this.ID = ID;
		this.socket = socket;
		initializeOutputStream();
	}

	public ChatClient(String username) {
		this.username = username;
	}
	
	public void initializeOutputStream(){
		try {
			outputToServer = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void connectToServer(String hostname, int port) throws IOException{
		setSocket(Networking.connectToServer(hostname, port));
		initializeOutputStream();
	}
	
	public void disconnectFromServer(){
		Networking.disconnectFromServer(socket);
	}
	

	//The client sends the username to register with the server and gets a unique ID in return
	public void registerWithServer(String username){
		Networking.sendData(username, getSocket());
		try {
			setID(Integer.parseInt(Networking.deserializeMessage(Networking.receiveData(getSocket())).getContents()));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void beginThread(){
		new Thread(this).start();
	}

	@Override
	public void run() {
		if (!hasUI) return;
		Networking.sendData(username + " has connected", socket);
		while (true) {
			try{
				Message deserializedMessage = Networking.deserializeMessage(Networking.receiveData(socket));
				if (deserializedMessage.isUpdateFromServer()){
					String message = deserializedMessage.getContents();
					if (message.startsWith("-")){
						userTableModel.removeRow(message.substring(1));
					}
					if (message.startsWith("+")){
						userTableModel.addRow(message.substring(1));
					}
					continue;
				}
				if (deserializedMessage.getOwnerUserName() != null) {
					clientUI.appendToOutput(deserializedMessage.getOwnerUserName() + ": " + 
											deserializedMessage.getContents() + "\n");
				} else {
					clientUI.appendToOutput(deserializedMessage.getContents() + "\n");
				}
			} catch (SocketException s){
				clientUI.appendToOutput("Disconnected from the server\n");
				return;
			}
		}
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public Socket getSocket() {
		return socket;
	}
	
	public void setSocket(Socket socket){
		this.socket = socket;
	}

	public OutputStream getOutputStream() {
		return outputToServer;
	}

	public void setUI(ClientUI UI) {
		this.clientUI = UI;
		setHasUI(true);
	}

	public boolean isHasUI() {
		return hasUI;
	}

	public void setHasUI(boolean hasUI) {
		this.hasUI = hasUI;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public UserTableModel getUserTableModel() {
		return userTableModel;
	}

	public void setUserTableModel(UserTableModel userTableModel) {
		this.userTableModel = userTableModel;
	}
	
	
}
