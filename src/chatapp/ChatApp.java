package chatapp;

public class ChatApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*beginServer();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		beginClientUI();
	}
	
	private static void beginServer(){
		Thread serverThread = new Thread(new ChatServer(8000));
		serverThread.start();
	}
	
	private static void beginClientUI(){
		ClientUI clientUI = new ClientUI();
		clientUI.setVisible(true);		
	}

}
