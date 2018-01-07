package chatapp;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer implements Runnable{
	private ServerSocket serverSocket;
	private HashMap<Integer, ChatClient> connectedClients;
	private BlockingQueue<Message> messages;
	
	public ChatServer(int port){
		try {
			serverSocket = new ServerSocket(port);
			connectedClients = new HashMap<>();
			messages = new LinkedBlockingQueue<>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		beginClientHandler();
		while(true){
			distributeMessages();
			pause(100);
		}
	}

	private void beginClientHandler() {
		Thread t = new Thread(new ClientHandler(this));
		t.start();
	}

	private void distributeMessages() {
		while(hasNewMessage()){
			Message currentMessage = messages.poll();
			for(ChatClient chatClient : connectedClients.values()){
				if(currentMessage.isPublic() || currentMessage.getRecipientID() == chatClient.getID()) {
					Networking.sendMessage(currentMessage, chatClient.getSocket());	
				}
			}
		}
	}
	
	public void disconnectClient(int clientID){
		System.out.println("Disconnected with client ID: " + clientID);
		String clientUserName = connectedClients.get(clientID).getUsername();
		connectedClients.remove(clientID);
		messages.add(new Message(clientUserName + " has disconnected"));
	}
	
	private void pause(int millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public boolean hasNewMessage(){
		return messages.size() > 0;
	}
	
	public Message getNewMessage(){
		return messages.remove();
	}
	
	public void addMessage(Message message){
		messages.add(message);
	}
	
	public void addClient(ChatClient client){
		connectedClients.put(client.getID(), client);
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
}
