package chatapp;

import java.io.*;
import java.net.*;

import javax.swing.JTextArea;

public class ChatClient implements Runnable {
	private OutputStream outputToServer;
	private Socket socket;
	private int ID;
	private JTextArea outputPanel;
	private boolean hasUI = false;
	private String username;

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

	@Override
	public void run() {
		if (!hasUI) return;
		Networking.sendData(username + " has connected", socket);
		while (true) {
			Message deserializedMessage = Networking.deserializeMessage(Networking.receiveData(socket));
			if (deserializedMessage.getOwnerUserName() != null) {
				outputPanel.append(
						deserializedMessage.getOwnerUserName() + ": " + deserializedMessage.getContents() + "\n");
			} else {
				outputPanel.append(deserializedMessage.getContents() + "\n");
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

	public void setOutputPanel(JTextArea outputPanel) {
		this.outputPanel = outputPanel;
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
}
