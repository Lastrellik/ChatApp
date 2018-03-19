package chatapp;

import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable{
	private ChatServer server;
	static int clientUniqueID = 0;

	public ClientHandler(ChatServer server){
		this.server = server;
	}
	
	@Override
	public void run() {
		try {
			handleIncomingClient();
		} catch (IOException e) {
			e.printStackTrace();
		}				
	}

	private void handleIncomingClient() throws IOException {
		while (true){
			Socket clientSocket = server.getServerSocket().accept(); //Blocks until a client connects
			String userName = Networking.deserializeMessage(Networking.receiveData(clientSocket)).getContents();
			int newClientID = getAndIncrementIDCounter();
			Networking.sendData(String.valueOf(newClientID), clientSocket);
			beginClientThread(userName, clientSocket, newClientID);
			new Thread(new MessageHandler(newClientID, clientSocket, server)).start();
		}
	}

	private void beginClientThread(String userName, Socket clientSocket, int clientID) {
		ChatClient client = new ChatClient(userName, clientID, clientSocket);
		server.addClient(client);
		new Thread(client).start();
	}

	public static int getAndIncrementIDCounter() {
		return clientUniqueID++;
	}	

}
