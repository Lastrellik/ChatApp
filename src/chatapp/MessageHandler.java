package chatapp;

import java.io.*;
import java.net.*;

import com.google.gson.Gson;

public class MessageHandler implements Runnable{
	private InputStream inputFromServer;
	private ChatServer chatServer;
	private Gson gson;
	private int clientID;
	
	public MessageHandler(int clientID, Socket socket, ChatServer server){
		this.clientID = clientID;
		this.chatServer = server;
		gson = new Gson();
		try {
			inputFromServer = socket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(true){
			try {
				byte[] buffer = new byte[1024];
				int returnCode = inputFromServer.read(buffer);//blocks until input data is available
				if (returnCode == -1){//client has disconnected
					chatServer.disconnectClient(clientID);
					return;
				}
				if(buffer[0] != '{') continue;
				Message message = gson.fromJson(new String(buffer).trim(),  Message.class);
				chatServer.addMessage(message);
			} catch (IOException e) {
				chatServer.disconnectClient(clientID);
				return;
			}
		}
	}

}