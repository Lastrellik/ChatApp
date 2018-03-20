package chatapp;

import java.net.*;
import com.google.gson.*;
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
	
	public static String receiveData(Socket dataSocket) throws SocketException{
		byte[] messageBuffer = new byte[1024];
		try {
			dataSocket.getInputStream().read(messageBuffer);
		} catch (SocketException s) {
			throw new SocketException();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(messageBuffer).trim();
	}
	
	public static Message deserializeMessage(String serializedData) throws JsonSyntaxException{
		Message message = new Message("");
		try{
			Gson gson = new Gson();
			message = gson.fromJson(serializedData, Message.class);
		} catch (JsonSyntaxException j){//data wasn't a message
			throw new JsonSyntaxException("Connecting user wasn't a chat client");
		}
		return message;
	}
	
	public static Socket connectToServer(String hostName, int port) throws IOException {
		Socket socket = null;
		try {
			socket = new Socket(hostName, port);
			
		} catch (UnknownHostException e) {
			throw new UnknownHostException();

		} catch (IOException e) {
			throw new IOException();
		}
		return socket;
	}
	
	public static void disconnectFromServer(Socket socket){
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}