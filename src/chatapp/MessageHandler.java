package chatapp;

import java.io.*;
import java.net.*;

import com.google.gson.Gson;

public class MessageHandler implements Runnable{
	private InputStream inputFromServer;
	private ChatServer chatServer;
	private Gson gson;
	
	public MessageHandler(Socket socket, ChatServer server){
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
		int disconnectedClientID = 0;
		while(true){
			try {
				byte[] buffer = new byte[1024];
				inputFromServer.read(buffer);//blocks until input data is available
				if(buffer[0] != '{') continue;
				Message message = gson.fromJson(new String(buffer).trim(),  Message.class);
				System.out.println(message);
				disconnectedClientID = message.getSenderID();
				chatServer.addMessage(message);
			} catch (IOException e) {
				chatServer.disconnectClient(disconnectedClientID);
				e.printStackTrace();
				return;
			} 
		}
	}

}