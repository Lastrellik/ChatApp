package chatapp;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.UnknownHostException;

public class ClientUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField userNameTextField;
	private JTextField hostNameTextField;
	private JTextField portTextField;
	private ChatClient client;

	/**
	 * Create the frame.
	 */
	public ClientUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 725, 420);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		JLabel lblDesiredUsername = new JLabel("Desired Username:");
		panel.add(lblDesiredUsername);
		
		userNameTextField = new JTextField();
		userNameTextField.setText("Test");
		panel.add(userNameTextField);
		userNameTextField.setColumns(10);
		
		JLabel lblHostname = new JLabel("Hostname:");
		panel.add(lblHostname);
		
		hostNameTextField = new JTextField();
		hostNameTextField.setText("lastrellik.noip.me");
		panel.add(hostNameTextField);
		hostNameTextField.setColumns(10);
		
		JLabel lblPort = new JLabel("Port: ");
		panel.add(lblPort);
		
		portTextField = new JTextField();
		portTextField.setText("8000");
		panel.add(portTextField);
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
		

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		sendMessageArea.setLineWrap(true);
		panel_1.add(sendMessageArea);
		
		final JButton btnSend = new JButton("Send");
		btnSend.setEnabled(false);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendTextToServer(sendMessageArea);
			}
		});
		panel_1.add(btnSend, BorderLayout.EAST);
		
		final JTextArea txtrconnectToA = new JTextArea();
		txtrconnectToA.append("Welcome! Connect to a server to begin chatting\n");
		txtrconnectToA.setEditable(false);
		
		final JButton btnConnect = new JButton("Connect");
		btnConnect.setSelected(true);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ChatClient client = buildClient(txtrconnectToA);
				btnConnect.setEnabled(false);
				try {
					client.connectToServer(hostNameTextField.getText(), Integer.parseInt(portTextField.getText()));
					Networking.sendData(userNameTextField.getText(), client.getSocket());
				} catch (NumberFormatException e) {
					alertFailedConnection(txtrconnectToA, btnConnect);
					return;
				} catch (UnknownHostException e) {
					alertFailedConnection(txtrconnectToA, btnConnect);
					return;
				}
				Thread t = new Thread(client);
				t.start();
				enableSendingMessages(sendMessageArea, btnSend, btnConnect);
			}

		});
		panel.add(btnConnect);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setViewportView(txtrconnectToA);
		
	}

	private ChatClient buildClient(final JTextArea txtrconnectToA) {
		client = new ChatClient(userNameTextField.getText().trim());
		client.setOutputPanel(txtrconnectToA);
		client.setHasUI(true);
		return client;
	}
	
	private void alertFailedConnection(final JTextArea txtrconnectToA, final JButton btnConnect) {
		txtrconnectToA.append("Failed to connect to server");
		btnConnect.setEnabled(true);
	}

	private void sendTextToServer(final JTextArea sendMessageArea) {
		Message message = new Message(sendMessageArea.getText().trim());
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
