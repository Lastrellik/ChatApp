package chatapp;

import java.net.Socket;
import java.util.Arrays;

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
	
	public static String receiveData(Socket dataSocket){
		byte[] messageBuffer = new byte[1024];
		try {
			dataSocket.getInputStream().read(messageBuffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(messageBuffer).trim();
	}
	
	public static Message deserializeMessage(String serializedData){
		Gson gson = new Gson();
		return gson.fromJson(serializedData, Message.class);
	}
}