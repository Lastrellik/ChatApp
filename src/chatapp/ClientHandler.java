package chatapp;

import java.io.*;
import java.net.*;

import com.google.gson.JsonSyntaxException;

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
			String receivedData = Networking.receiveData(clientSocket);
			String userName = "";
			try{
				userName = Networking.deserializeMessage(receivedData).getContents();
			} catch (JsonSyntaxException j){ //Throws when connecting user isn't a chat client.
				System.err.println("Rejected socket: " + receivedData);
				continue;
			}
			int newClientID = getAndIncrementIDCounter();
			Networking.sendData(String.valueOf(newClientID), clientSocket);
			ChatClient client = new ChatClient(userName, newClientID, clientSocket);
			beginClientThread(client);
			new Thread(new MessageHandler(newClientID, clientSocket, server)).start();
			informUsersOfNewUser(userName);
			informNewUserOfConnectedUsers(client);
		}
	}

	private void beginClientThread(ChatClient client) {
		server.addClient(client);
		new Thread(client).start();
	}

	public static int getAndIncrementIDCounter() {
		return clientUniqueID++;
	}	

	private void informUsersOfNewUser(String userName) {
		Message message = new Message("+" + userName);
		message.setUpdateFromServer(true);
		server.addMessage(message);
	}
	
	private void informNewUserOfConnectedUsers(ChatClient client){
		if (server.getConnectedClients().size() == 1) return; //first user to join
		Message message = new Message("");
		message.setUpdateFromServer(true);
		StringBuilder csvOfUsernames = new StringBuilder("+");
		String prefix = "";
		for (ChatClient c : server.getConnectedClients().values()){
			if (c == client) continue;
			csvOfUsernames.append(prefix);
			prefix = ",";//To avoid an extra comma in the beginning
			csvOfUsernames.append(c.getUsername());
		}
		message.setContents(csvOfUsernames.toString());
		Networking.sendMessage(message, client.getSocket());
	}

}
