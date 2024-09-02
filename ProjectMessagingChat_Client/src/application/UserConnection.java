package application;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

// Classe que cria a conexão RMI com outro usuário
public class UserConnection {
	
	public UserInterface userConnection;
	private Main main;
	private String userUrl;
	
	public UserConnection(Main main) {
		this.main = main;
	}
	
	// Definir a url do usuário
	public void setUrl(String url) {
		this.userUrl = url;
	}
	
	// Enviar a mensagem para o destinatário
	public void sendMessage(Contact sender, String message) {
		
		try {
			// Se o destinatário estiver online, a mensagem vai diretamente para ele
			this.userConnection = (UserInterface) Naming.lookup(userUrl);
			if(this.userConnection.getReceiverStatus().equals("online")) {
				this.userConnection.sendMessage(sender, message);
			}
			
			// Se o destinatário não estiver online, a mensagem vai para o servidor
			else {
				this.main.serverConnection.sendMessage(sender, this.main.selectedContact, message);
			}
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// Se o destinatário não estiver ligado, a mensagem vai para o servidor
			this.main.serverConnection.sendMessage(sender, this.main.selectedContact, message);
		}
	}

}
