package chatapp;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JTextArea;

public class ChatClient implements Runnable{
	private InputStream inputFromServer;
	private OutputStream outputToServer;
	private Socket socket;
	private int ID;
	private JTextArea outputPanel;
	private boolean hasUI = false;
	private String username;
	
	public ChatClient(String username){
		this.username = username;
		this.ID = ClientHandler.getAndIncrementIDCounter();
	}
	
	public ChatClient(String username, Socket socket){
		this(username);
		this.socket = socket;
		try {
			inputFromServer = socket.getInputStream();
			outputToServer = socket.getOutputStream();	
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@Override
	public void run() {
		try {
			if(!hasUI) return;
			byte[] buffer = new byte[1024];
			outputPanel.append(username + " has connected\n");
			while(true){
				inputFromServer.read(buffer);
				String serializedMessage = new String(buffer).trim();
				Message deserializedMessage = Networking.deserializeMessage(serializedMessage);
				if(deserializedMessage.getOwnerUserName() != null) {
					outputPanel.append(deserializedMessage.getOwnerUserName() + ": " + deserializedMessage.getContents() + "\n");
				} else {
					outputPanel.append(deserializedMessage.getContents() + " has connected\n");
				}
				Arrays.fill(buffer, " ".getBytes()[0]);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void connectToServer(String hostName, int port) throws UnknownHostException{
		try {
			socket = new Socket(hostName, port);
			inputFromServer = socket.getInputStream();
			outputToServer = socket.getOutputStream();
		
		} catch (UnknownHostException e){
			throw new UnknownHostException();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getID() {
		return ID;
	}

	public Socket getSocket() {
		return socket;
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
