package application;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ServerConnection {
	
	public ServerInterface serverConnection;
	private Main main;
	private String serverUrl;
	
	public ServerConnection(Main main) {
		this.main = main;
	}
	
	// Definir a url do servidor
	public void setUrl(String ip, String port, String name) {
		String url = String.format("rmi://%s:%s/%s", ip, port, name);
		this.serverUrl = url;
	}
	
	// Registrar o contato logado no servidor
	public ContactCatalog registerContact(Contact contact) {
		try {
			this.serverConnection = (ServerInterface) Naming.lookup(serverUrl);
			return this.serverConnection.registerContactOnServer(contact);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// Notificar ao servidor a mudança de status do usuário
	public ContactCatalog notifyStatusChange(Contact sender) {
		try {
			this.serverConnection = (ServerInterface) Naming.lookup(serverUrl);
			return this.serverConnection.notifyOnlineStatus(sender);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	// Notificar ao servidor a adição de um novo contato à lista
	public void notifyNewContact(Contact sender, Contact newContact) {
		try {
			this.serverConnection = (ServerInterface) Naming.lookup(serverUrl);
			this.serverConnection.registerNewContact(sender, newContact);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	// Notificar ao servidor o deletamento de um contato da lista
	public void notifyContactDeleted(Contact sender, Contact deletedContact) {
		try {
			this.serverConnection = (ServerInterface) Naming.lookup(serverUrl);
			this.serverConnection.deleteContact(sender, deletedContact);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	// Enviar uma mensagem para um usuário offline
	public void sendMessage(Contact sender, Contact receiver, String message) {
		
		try {
			this.serverConnection = (ServerInterface) Naming.lookup(serverUrl);
			this.serverConnection.writeOfflineMessage(sender, receiver, message);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}

}
