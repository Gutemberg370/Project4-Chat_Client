package application;

import java.rmi.Remote;
import java.rmi.RemoteException;

// Interface da conexão RMI entre clientes
public interface UserInterface extends Remote{
	
	public void createContact(Contact sender, String Message) throws RemoteException;
	
	public String getReceiverStatus() throws RemoteException;

	public void sendMessage(Contact sender, String Message) throws RemoteException;
	
}
