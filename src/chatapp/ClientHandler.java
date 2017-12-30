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
			while (true){
				Socket clientSocket = server.getServerSocket().accept();
				ChatClient client = new ChatClient("ServerClient", clientSocket);
				MessageHandler messageHandler = new MessageHandler(clientSocket, server);
				server.addClient(client);
				Thread clientThread = new Thread(client);
				Thread messageHandlerThread = new Thread(messageHandler);
				clientThread.start();
				messageHandlerThread.start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}				
	}

	public static int getAndIncrementIDCounter() {
		return clientUniqueID++;
	}	

}
