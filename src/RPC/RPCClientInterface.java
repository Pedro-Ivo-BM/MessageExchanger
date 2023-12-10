package RPC;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RPCClientInterface extends Remote{
	String sendMessage(String message) throws RemoteException;
}
