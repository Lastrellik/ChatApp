package chatapp;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ConnectUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtServerAddress;
	private JTextField textServerPort;
	private JTextField txtUsername;

	/**
	 * Launch the application.
	 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConnectUI frame = new ConnectUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public ConnectUI(final ClientUI clientUI) {
		setTitle("Connect");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 362, 173);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel pnlServerPort = new JPanel();
		contentPane.add(pnlServerPort, BorderLayout.NORTH);
		pnlServerPort.setLayout(new GridLayout(2,2, 5, 5));
		
		JLabel lblServerAddress = new JLabel("Server Address:");
		pnlServerPort.add(lblServerAddress);
		lblServerAddress.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel lblServerPort = new JLabel("Server Port:");
		pnlServerPort.add(lblServerPort);
		
		txtServerAddress = new JTextField();
		txtServerAddress.setText("lastrellik.noip.me");
		pnlServerPort.add(txtServerAddress);
		txtServerAddress.setColumns(20);
		
		textServerPort = new JTextField();
		textServerPort.setText("8000");
		pnlServerPort.add(textServerPort);
		textServerPort.setColumns(10);
		
		JPanel pnlUsername = new JPanel();
		contentPane.add(pnlUsername, BorderLayout.CENTER);
		pnlUsername.setLayout(new GridLayout(2, 1, 5, 5));
		
		JLabel lblUsername = new JLabel("Username:");
		pnlUsername.add(lblUsername);
		
		txtUsername = new JTextField();
		addWindowListener( new WindowAdapter() {
			public void windowOpened(WindowEvent e){
				txtUsername.requestFocus();
			}
		});
		pnlUsername.add(txtUsername);
		
		JPanel pnlButtonPanel = new JPanel();
		contentPane.add(pnlButtonPanel, BorderLayout.SOUTH);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clientUI.initializeClient(txtUsername.getText().trim(), 
									      txtServerAddress.getText().trim(), 
									      Integer.parseInt(textServerPort.getText().trim()));
				setVisible(false);
			}
		});
		btnConnect.setSelected(true);
		pnlButtonPanel.add(btnConnect);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		pnlButtonPanel.add(btnCancel);
		getRootPane().setDefaultButton(btnConnect);
	}

}
