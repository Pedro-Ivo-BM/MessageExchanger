package RPC;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RPCServerInterface extends Remote{
	void createQueueForNewUser(String userName) throws RemoteException;
	void saveOfflineUserPendingMessage(String userName, String message) throws RemoteException;
	List<String> getUserPendingMessages(String userName) throws RemoteException;
}