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
	private JMenuItem connectMenuItem;
	private JTable table;
	private UserTableModel userTableModel;

	/**
	 * Create the frame.
	 */
	public ClientUI() {
		setBackground(Color.WHITE);
		setTitle("ChatApp");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 420);
		setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu connectionMenu = new JMenu("Connections");
		menuBar.add(connectionMenu);
		
		connectMenuItem = new JMenuItem("Connect");
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
				enableConnectMenuItem();
			}
		});
		connectionMenu.add(disconnectMenuItem);
		
		JSeparator separator = new JSeparator();
		connectionMenu.add(separator);
		
		JMenuItem quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		connectionMenu.add(quitMenuItem);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
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
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setViewportView(messageArea);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		new SmartScroller(scrollPane);

		userTableModel = new UserTableModel();
		table = new JTable(userTableModel);
		table.setShowGrid(false);
		table.setTableHeader(null);
		JScrollPane userScrollPane = new JScrollPane(table);
		userScrollPane.getViewport().setBackground(Color.WHITE);
		userScrollPane.setPreferredSize(new Dimension(150, 0));
		userScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		contentPane.add(userScrollPane, BorderLayout.EAST);

		
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
			enableSendingMessages();
			disableConnectMenuItem();
		} catch (Exception e) {
			alertFailedConnection(e.getLocalizedMessage());
			enableConnectMenuItem();
			return;
		} 
	}
	
	private ChatClient buildClient(String username) {
		client = new ChatClient(username);
		client.setUI(this);
		client.setUserTableModel(userTableModel);
		return client;
	}
	
	public void appendToOutput(String outputMessage){
		messageArea.append(outputMessage + "\n");
	}
	
	private void alertFailedConnection(String message) {
		appendToOutput(message);
		disableSendingMessages();
	}

	private void sendTextToServer(final JTextArea sendMessageArea) {
		Message message = new Message(sendMessageArea.getText().trim());
		message.setIsPublic(true);
		message.setOwnerUserName(client.getUsername());
		Networking.sendMessage(message, client.getSocket());
		sendMessageArea.setText("");
	}

	private void enableSendingMessages() {
		sendMessageArea.setEnabled(true);
		btnSend.setEnabled(true);
	}
	
	private void disableSendingMessages() {
		sendMessageArea.setEnabled(false);
		btnSend.setEnabled(false);
	}
	
	private void disableConnectMenuItem(){
		connectMenuItem.setEnabled(false);
	}
	
	private void enableConnectMenuItem(){
		connectMenuItem.setEnabled(true);
	}
}
