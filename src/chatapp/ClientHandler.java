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
			beginClientThread(clientSocket);
			new Thread(new MessageHandler(clientSocket, server)).start();
		}
	}

	private void beginClientThread(Socket clientSocket) {
		ChatClient client = new ChatClient(getAndIncrementIDCounter(), clientSocket);
		server.addClient(client);
		new Thread(client).start();
	}

	public static int getAndIncrementIDCounter() {
		return clientUniqueID++;
	}	

}
