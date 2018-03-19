package chatapp;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;

public class ClientUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea sendMessageArea;
	private JTextArea messageArea;
	private JButton btnSend;
	private ChatClient client;

	/**
	 * Create the frame.
	 */
	public ClientUI() {
		setTitle("ChatApp");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 420);
		setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu connectionMenu = new JMenu("Connections");
		menuBar.add(connectionMenu);
		
		JMenuItem connectMenuItem = new JMenuItem("Connect");
		connectMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showConnectUI();
			}

		});
		connectionMenu.add(connectMenuItem);
		
		JMenuItem disconnectMenuItem = new JMenuItem("Disconnect");
		disconnectMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				client.disconnectFromServer();
				disableSendingMessages();
			}
		});
		connectionMenu.add(disconnectMenuItem);
		
		JSeparator separator = new JSeparator();
		connectionMenu.add(separator);
		
		JMenuItem quitMenuItem = new JMenuItem("Quit");
		connectionMenu.add(quitMenuItem);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		sendMessageArea = new JTextArea();
		sendMessageArea.setEnabled(false);
		sendMessageArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int enterKeyCode = 10;
				if(e.getKeyCode() != enterKeyCode) return; 
				sendTextToServer(sendMessageArea);
			}
		});
		

		JPanel chatInputPanel = new JPanel();
		contentPane.add(chatInputPanel, BorderLayout.SOUTH);
		chatInputPanel.setLayout(new BorderLayout(0, 0));
		
		sendMessageArea.setLineWrap(true);
		chatInputPanel.add(sendMessageArea);
		
		btnSend = new JButton("Send");
		btnSend.setEnabled(false);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendTextToServer(sendMessageArea);
			}
		});
		chatInputPanel.add(btnSend, BorderLayout.EAST);
		
		messageArea = new JTextArea();
		messageArea.setLineWrap(true);
		messageArea.append("Welcome! Connect to a server to begin chatting\n");
		messageArea.setEditable(false);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setViewportView(messageArea);
		new SmartScroller(scrollPane);
		
	}
	
	public void showConnectUI() {
		ConnectUI connectUI = new ConnectUI(this);
		connectUI.setVisible(true);
		connectUI.setLocationRelativeTo(this);
	}

	public void initializeClient(String username, String hostname, int port) {
		ChatClient client = buildClient(username);
		try {
			client.connectToServer(hostname, port);
			client.registerWithServer(username);
			client.beginThread();
		} catch (Exception e) {
			alertFailedConnection(messageArea);
			e.printStackTrace();
			return;
		} 
	}
	
	private ChatClient buildClient(String username) {
		client = new ChatClient(username);
		client.setUI(this);
		return client;
	}
	
	public void appendToOutput(String outputMessage){
		messageArea.append(outputMessage);
	}
	
	private void alertFailedConnection(final JTextArea txtrconnectToA) {
		txtrconnectToA.append("Failed to connect to server\n");
	}

	private void sendTextToServer(final JTextArea sendMessageArea) {
		Message message = new Message(sendMessageArea.getText().trim());
		message.setIsPublic(true);
		message.setOwnerUserName(client.getUsername());
		Networking.sendMessage(message, client.getSocket());
		sendMessageArea.setText("");
	}

	public void enableSendingMessages() {
		sendMessageArea.setEnabled(true);
		btnSend.setEnabled(true);
	}
	
	public void disableSendingMessages() {
		sendMessageArea.setEnabled(false);
		btnSend.setEnabled(false);
	}

}
