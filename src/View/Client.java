package View;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextPane;

import RPC.RPCClientController;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.function.Consumer;
import java.awt.event.ActionEvent;

public class Client {
	RPCClientController clientController = new RPCClientController();

	private JFrame frame;
	private JLabel userTitle;
	private JTextField chatMessageTextfield;
	private JTextField chatReceiverTextfield;
	private JTextField addContactTextfield;
	private JTextField deleteContactTextfield;
	private JTextPane chatPane;

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
	public Client(String clientId) {
		clientController.clientId = clientId;
		clientController.initializeRPC();
		clientController.stayOnline();
		clientController.reactToDirectMessage = reactionToDirectMessage;
		clientController.createUserQueue();
		
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

					userTitle.setText("Cliente: " + clientController.clientId);

					JButton changeClientStatusButton = new JButton("On");
					changeClientStatusButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							if (clientController.isClientOnline) {
								clientController.stayOffline();
								changeClientStatusButton.setText("Off");
							} else {
								clientController.stayOnline();
								changeClientStatusButton.setText("On");
								String pendingMessages = clientController.getAndlistPendingMessagesForView();
								setLocalChatText(pendingMessages);
							}
						}
					});
					changeClientStatusButton.setBounds(332, 48, 89, 23);
					frame.getContentPane().add(changeClientStatusButton);

					JLabel chatLabel = new JLabel("chat");
					chatLabel.setBounds(206, 82, 77, 14);
					frame.getContentPane().add(chatLabel);

					JScrollPane scrollPane = new JScrollPane();
					scrollPane.setBounds(35, 107, 364, 348);
					frame.getContentPane().add(scrollPane);

					chatPane = new JTextPane();
					scrollPane.setViewportView(chatPane);

					chatMessageTextfield = new JTextField();
					chatMessageTextfield.setBounds(131, 466, 262, 20);
					frame.getContentPane().add(chatMessageTextfield);
					chatMessageTextfield.setColumns(10);

					chatReceiverTextfield = new JTextField();
					chatReceiverTextfield.setBounds(131, 497, 262, 20);
					frame.getContentPane().add(chatReceiverTextfield);
					chatReceiverTextfield.setColumns(10);

					JButton chatSendMessageButton = new JButton("Enviar");
					chatSendMessageButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							String message = chatMessageTextfield.getText();
							String contact = chatReceiverTextfield.getText();
							if(clientController.isAContact(contact)) {
								try {
									clientController.sendDirectMessage(message, contact);
									setLocalChatText("VOCÊ: "+message+"\n");
								} catch (RemoteException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								chatMessageTextfield.setText("");
							}else {
								setLocalChatText("SISTEMA: Contato não existe!\n");
							}
							
							
						}
					});
					chatSendMessageButton.setBounds(170, 527, 89, 23);
					frame.getContentPane().add(chatSendMessageButton);

					JLabel lblNewLabel = new JLabel("Mensagem:");
					lblNewLabel.setBounds(31, 469, 100, 14);
					frame.getContentPane().add(lblNewLabel);

					JLabel lblNewLabel_1 = new JLabel("Para:");
					lblNewLabel_1.setBounds(31, 500, 100, 14);
					frame.getContentPane().add(lblNewLabel_1);

					JScrollPane scrollPane_1 = new JScrollPane();
					scrollPane_1.setBounds(469, 107, 181, 353);
					frame.getContentPane().add(scrollPane_1);

					JTextPane contactsPane = new JTextPane();
					scrollPane_1.setViewportView(contactsPane);

					JLabel lblNewLabel_2 = new JLabel("Contatos");
					lblNewLabel_2.setBounds(536, 82, 46, 14);
					frame.getContentPane().add(lblNewLabel_2);

					addContactTextfield = new JTextField();
					addContactTextfield.setBounds(536, 480, 144, 20);
					frame.getContentPane().add(addContactTextfield);
					addContactTextfield.setColumns(10);

					deleteContactTextfield = new JTextField();
					deleteContactTextfield.setBounds(536, 516, 144, 20);
					frame.getContentPane().add(deleteContactTextfield);
					deleteContactTextfield.setColumns(10);

					JButton addContactButton = new JButton("Adicionar");
					addContactButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							String newContact = addContactTextfield.getText();
							clientController.addContact(newContact);
							addContactTextfield.setText("");
							contactsPane.setText(clientController.listContactsForView());
						}
					});
					addContactButton.setBounds(437, 479, 89, 23);
					frame.getContentPane().add(addContactButton);

					JButton deleteContactButton = new JButton("Deletar");
					deleteContactButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							String Contact = deleteContactTextfield.getText();
							clientController.deleteContact(Contact);
							deleteContactTextfield.setText("");
							contactsPane.setText(clientController.listContactsForView());
						}
					});
					deleteContactButton.setBounds(437, 515, 89, 23);
					frame.getContentPane().add(deleteContactButton);
					
					///Depois de criar a view, checa aqui se tem mensagens pendentes pela primeira vez
					String pendingMessages = clientController.getAndlistPendingMessagesForView();
					setLocalChatText(pendingMessages);
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
		frame.setBounds(100, 100, 706, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		userTitle = new JLabel("Client");
		userTitle.setBounds(332, 23, 137, 14);
		frame.getContentPane().add(userTitle);
	}
	
	Consumer<String> reactionToDirectMessage = (message) -> {
		String previusText = chatPane.getText();

		if (previusText == null) {
			previusText = "";
		}

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(previusText);

		stringBuilder.append(message);

		String newText = stringBuilder.toString();

		chatPane.setText(newText);
		
	};
	
	void setLocalChatText(String localMessage){
		String previusText = chatPane.getText();

		if (previusText == null) {
			previusText = "";
		}

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(previusText);

		stringBuilder.append(localMessage);

		String newText = stringBuilder.toString();

		chatPane.setText(newText);
		
	}
}
