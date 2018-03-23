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
		Socket socket = new Socket();
		int timeoutInMillis = 5000; //10 seconds
		try {
			socket.connect(new InetSocketAddress(hostName, port), timeoutInMillis);
		} catch (UnknownHostException e) {
			throw new UnknownHostException();
		} catch (SocketTimeoutException s){
			throw new IOException("Connection to the server timed out");
		} catch (ConnectException c){
			throw new IOException("Connection refused. Incorrect server address or server is offline.");
		} catch (IOException i){
			i.printStackTrace();
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