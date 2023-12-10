package View;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClientMenu {

	private JFrame frame;
	private JTextField userNameTextfield;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					View window = new View();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public ClientMenu() {
		// initialize();
	}

	/**
	 * Create the application.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public void renderView() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initialize();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 685, 607);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("MENU DO CLIENTE");
		lblNewLabel.setBounds(294, 28, 139, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Entrar como:");
		lblNewLabel_1.setBounds(299, 219, 77, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		userNameTextfield = new JTextField();
		userNameTextfield.setBounds(258, 244, 139, 20);
		frame.getContentPane().add(userNameTextfield);
		userNameTextfield.setColumns(10);
		
		JButton loginButton = new JButton("ENTRAR");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String clientId = userNameTextfield.getText();
				Client client = new Client(clientId);
				client.renderView();
				
				userNameTextfield.setText("");
			}
		});
		loginButton.setBounds(287, 287, 89, 23);
		frame.getContentPane().add(loginButton);
	}
}
