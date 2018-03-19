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
	private JTextField userNameTextField;
	private JTextField hostNameTextField;
	private JTextField portTextField;
	private JTextArea messageArea;
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
				ConnectUI connectUI = new ConnectUI();
				connectUI.setLocationRelativeTo((Component) arg0.getSource());
				connectUI.setVisible(true);
				
			}
		});
		connectionMenu.add(connectMenuItem);
		
		JMenuItem disconnectMenuItem = new JMenuItem("Disconnect");
		connectionMenu.add(disconnectMenuItem);
		
		JSeparator separator = new JSeparator();
		connectionMenu.add(separator);
		
		JMenuItem quitMenuItem = new JMenuItem("Quit");
		connectionMenu.add(quitMenuItem);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel connectionPanel = new JPanel();
		contentPane.add(connectionPanel, BorderLayout.NORTH);
		
		JLabel lblDesiredUsername = new JLabel("Desired Username:");
		connectionPanel.add(lblDesiredUsername);
		
		userNameTextField = new JTextField();
		userNameTextField.setText("Test");
		connectionPanel.add(userNameTextField);
		userNameTextField.setColumns(10);
		
		JLabel lblHostname = new JLabel("Hostname:");
		connectionPanel.add(lblHostname);
		
		hostNameTextField = new JTextField();
		hostNameTextField.setText("lastrellik.noip.me");
		connectionPanel.add(hostNameTextField);
		hostNameTextField.setColumns(10);
		
		JLabel lblPort = new JLabel("Port: ");
		connectionPanel.add(lblPort);
		
		portTextField = new JTextField();
		portTextField.setText("8000");
		connectionPanel.add(portTextField);
		portTextField.setColumns(10);
		
		final JTextArea sendMessageArea = new JTextArea();
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
		
		final JButton btnSend = new JButton("Send");
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
		
		final JButton btnConnect = new JButton("Connect");
		btnConnect.setSelected(true);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ChatClient client = buildClient(userNameTextField.getText().trim());
				btnConnect.setEnabled(false);
				try {
					client.connectToServer(hostNameTextField.getText(), Integer.parseInt(portTextField.getText()));
					client.registerWithServer(userNameTextField.getText());
					client.beginThread();
				} catch (Exception e) {
					alertFailedConnection(messageArea, btnConnect);
					e.printStackTrace();
					return;
				} 
				enableSendingMessages(sendMessageArea, btnSend, btnConnect);
			}

		});
		connectionPanel.add(btnConnect);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setViewportView(messageArea);
		new SmartScroller(scrollPane);
		
	}
	
	private ChatClient buildClient(String username) {
		client = new ChatClient(username);
		client.setUI(this);
		return client;
	}
	
	public void appendToOutput(String outputMessage){
		messageArea.append(outputMessage);
	}
	
	private void alertFailedConnection(final JTextArea txtrconnectToA, final JButton btnConnect) {
		txtrconnectToA.append("Failed to connect to server\n");
		btnConnect.setEnabled(true);
	}

	private void sendTextToServer(final JTextArea sendMessageArea) {
		Message message = new Message(sendMessageArea.getText().trim());
		message.setIsPublic(true);
		message.setOwnerUserName(client.getUsername());
		Networking.sendMessage(message, client.getSocket());
		sendMessageArea.setText("");
	}

	private void enableSendingMessages(final JTextArea sendMessageArea, final JButton btnSend,
			final JButton btnConnect) {
		btnConnect.setEnabled(false);
		portTextField.setEnabled(false);
		userNameTextField.setEnabled(false);
		hostNameTextField.setEnabled(false);
		sendMessageArea.setEnabled(true);
		btnSend.setEnabled(true);
	}

}
