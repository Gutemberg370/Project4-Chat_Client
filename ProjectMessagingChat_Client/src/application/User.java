package application;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

// Implementação da interface de conexão RMI entre os clientes
public class User extends UnicastRemoteObject implements UserInterface{
	
	private Main main;

	protected User(Main main) throws RemoteException {
		super();
		this.main = main;
	}
	
	//Função de UI que cria um novo contato na lista de contatos do contato que recebe a mensagem
	
	@Override
	public void createContact(Contact sender, String message) throws RemoteException{
		
		//Adiciona o novo contato na lista do destinatário
		Contact newContact = sender;
		newContact.addMessage(message);
		this.main.contactList.add(newContact);
		
		//Adiciona o novo contato na interface do destinatário
		Runnable createNewGridRow = () -> {
            Platform.runLater(() -> {	
				
				int contactId = this.main.contactList.size()-1;
	    		
	        	Button selectContact = new Button();
	        	       	
	            Image imgSelect = new Image(getClass().getResourceAsStream("arrow.png"));
	            ImageView imgViewSelect = new ImageView(imgSelect);
	            imgViewSelect.setFitHeight(20);
	            imgViewSelect.setFitWidth(35);
	            imgViewSelect.setPreserveRatio(true);
	            selectContact.setGraphic(imgViewSelect);
	            
	            selectContact.setPadding(new Insets(-1, -1, -1, -1));
	            
	            selectContact.setOnAction(event -> {
                	if(this.main.selectedContact != null) {
                    	this.main.supportFunctions.setContactColor(Color.BLACK);
                	}
                	this.main.selectedContact = this.main.contactList.get(contactId);
                	this.main.supportFunctions.setContactColor(Color.GREEN);
	            	this.main.messageArea.setText(null);
                	if(this.main.myContact.getStatus().equals("online")) {
                    	this.main.messageField.setEditable(true);
                    	this.main.messageField.setDisable(false);
                	}
	            	this.main.selectedContact.getMessages().forEach(msg -> this.main.messageArea.appendText(msg + '\n'));
	            });
	            
	            Button deleteContact = new Button();
		       	
	            Image imgDelete = new Image(getClass().getResourceAsStream("delete.jpg"));
	            ImageView imgViewDelete = new ImageView(imgDelete);
	            imgViewDelete.setFitHeight(20);
	            imgViewDelete.setFitWidth(35);
	            imgViewDelete.setPreserveRatio(true);
	            deleteContact.setGraphic(imgViewDelete);
	            
	            deleteContact.setPadding(new Insets(-1, -1, -1, -1));
	            
	            deleteContact.setOnAction(event -> {
	            	
	            	this.main.serverConnection.notifyContactDeleted(this.main.myContact, this.main.contactList.get(contactId));
			        Runnable deleteUserContact = () -> {
			            Platform.runLater(() -> {
			            	this.main.gridpane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == contactId);
			            	this.main.supportFunctions.updateGridpane();
			            });
			        };
			        Thread deleteUserContactThread = new Thread(deleteUserContact);
			        deleteUserContactThread.setDaemon(true);
			        deleteUserContactThread.start();
	            	if(this.main.selectedContact != null && this.main.contactList.get(contactId).getName().equals(this.main.selectedContact.getName())) {
                		this.main.selectedContact = null;
                		this.main.messageField.setEditable(false);
                		this.main.messageField.setDisable(true);
                		this.main.messageArea.setText(null);
	            	}
	        		this.main.contactList.remove(contactId);
	            });
	            
                Label contactLabel = new Label(this.main.contactList.get(contactId).getName());
                contactLabel.setFont(new Font("Arial",14));
	            
	    		this.main.gridpane.add(contactLabel, 0, contactId);  
	    		this.main.gridpane.add(selectContact, 1, contactId); 
	    		this.main.gridpane.add(deleteContact, 2, contactId);
            });
    };
    Thread createNewGridRowThread = new Thread(createNewGridRow);
    createNewGridRowThread.setDaemon(true);
    createNewGridRowThread.start();
	}
	
	// Função que retorna o status do contato destinatário
	
	@Override
	public String getReceiverStatus() throws RemoteException{
		return this.main.myContact.getStatus();
	}
	
	// Função que envia a mensagem para o destinatário
	
	@Override
	public void sendMessage(Contact sender, String message) throws RemoteException{
		
		Boolean hasContact = false;
		
		for(int i = 0; i < this.main.contactList.size(); i++) {
			if(this.main.contactList.get(i).getName().equals(sender.getName())) {
				this.main.contactList.get(i).addMessage(message);
				hasContact = true;
				if(this.main.selectedContact != null && this.main.contactList.get(i).getName().equals(this.main.selectedContact.getName())) {
					this.main.messageArea.appendText(message + '\n');
				}
			}

		};
		
		if(!hasContact) {
			createContact(sender,message);
		}			
		
	}
}
