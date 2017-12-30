package chatapp;

import java.net.Socket;
import com.google.gson.Gson;
import java.io.*;

public class Networking {
	
	public static void sendData(String data, Socket recipient ){
		Message message = new Message(data);
		sendMessage(message, recipient);
	}
	
	public static void sendMessage(Message message, Socket recipient){
		Gson gson = new Gson();
		try {
			OutputStream out = recipient.getOutputStream();
			out.write((gson.toJson(message) + "\n").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Message deserializeMessage(String serializedData){
		Gson gson = new Gson();
		return gson.fromJson(serializedData, Message.class);
	}
}