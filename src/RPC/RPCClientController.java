package RPC;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.jms.JMSException;

public class RPCClientController implements RPCClientInterface {
	private Registry registry = null;
	private Remote remoteObject = null;
	private RPCServerInterface serverMom = null;
	public boolean isClientOnline = false;
	public String clientId = "";
	public List<String> contatos = new ArrayList<>();
	public Consumer<String> reactToDirectMessage;

	public RPCClientController() {
	}

	public void initializeRPC() {
		try {
			registry = LocateRegistry.getRegistry();
			remoteObject = UnicastRemoteObject.exportObject(this, 0);
			serverMom = (RPCServerInterface) registry.lookup("ServerMOM");
		} catch (RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Erro initializeRPC");
			e.printStackTrace();
		}
	}

	public void stayOnline() {

		try {
			registry.rebind(clientId, remoteObject);
			this.isClientOnline = true;
		} catch (Exception e) {
			System.out.println("Erro stayOnline");
			e.printStackTrace();
		}
	}

	public void stayOffline() {
		try {
			registry.unbind(clientId);
			this.isClientOnline = false;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void addContact(String newContact) {
		contatos.add(newContact);
	}

	public void deleteContact(String contact) {
		contatos.remove(contact);
	}

	public boolean isAContact(String contact) {
		return contatos.contains(contact);
	}

	public String listContactsForView() {
		StringBuilder contactsNames = new StringBuilder();
		contatos.forEach(contact -> {

			contactsNames.append("Contato : " + contact + ". \n");

		});
		return contactsNames.toString();
	}

	@Override
	public String sendMessage(String message) throws RemoteException {
		if (isClientOnline) {
			reactToDirectMessage.accept(message + "\n");
			return message;
		}
		throw new RemoteException();

	}

	public void sendDirectMessage(String message, String contact) throws RemoteException {
		try {
			RPCClientInterface client = (RPCClientInterface) registry.lookup(contact);
			String retorno = client.sendMessage(clientId + ": " + message);
			System.out.println("sendDirectMessage " + retorno);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("sendDirectMessage RemoteException " + e.toString());
			serverMom.saveOfflineUserPendingMessage(contact, (clientId + ": " + message));

		}

	}

	public void createUserQueue() {
		try {
			serverMom.createQueueForNewUser(clientId);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String> getUserPendingMessages() {
		try {
			return serverMom.getUserPendingMessages(clientId);
		} catch (RemoteException e) {
			List<String> messagesError = new ArrayList<>();
			e.printStackTrace();
			return messagesError;
		}
	}
	
	public String getAndlistPendingMessagesForView() {
		List<String> messages = getUserPendingMessages();
		StringBuilder messageBuilder = new StringBuilder();
		messages.forEach(message -> {

			messageBuilder.append(message + ". \n");

		});
		return messageBuilder.toString();
	}

}
