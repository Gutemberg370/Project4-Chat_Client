package application;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

// Funções relacionadas com a atualização da interface gráfica
public class SupportFunctions {
	
	private Main main;
	
	SupportFunctions(Main main){
		this.main = main;
	}
	
	// Função que define a cor para o contato selecionado pelo usuário
	public void setContactColor(Color color) {
		
		for(int i = 0; i < this.main.contactList.size(); i++) {
    		if(this.main.contactList.get(i).getName().equals(this.main.selectedContact.getName())) {
    			for (Node child : this.main.gridpane.getChildren()) {
    			    Integer r = GridPane.getRowIndex(child);
    			    Integer c = GridPane.getColumnIndex(child);
    			    int row = r == null ? 0 : r;
    			    int column = c == null ? 0 : c;
    			    if (row == i && column == 0 && (child instanceof Label)) {
    			        ((Labeled) child).setTextFill(color);              			        
    			        break;
    			    }
    			}
    			
    		} 
		}
		
	}
	
	// Função que reconstrói a lista de contatos para fins de atualização
	public void updateGridpane() {
		
		Runnable updateGrid = () -> {
            Platform.runLater(() -> {	
            	
            	this.main.gridpane.getChildren().clear(); 
            	
            	for(int i = 0; i < this.main.contactList.size(); i++) {
    		   		
            		int contactId = i;
            		
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
                        	setContactColor(Color.BLACK);
                    	}
                    	this.main.selectedContact = this.main.contactList.get(contactId);
                    	setContactColor(Color.GREEN);
                    	this.main.messageArea.setText(null);
                    	if(this.main.myContact.getStatus().equals("online")) {
                        	this.main.messageField.setEditable(true);
                        	this.main.messageField.setDisable(false);
                    	}
                    	this.main.selectedContact.getMessages().forEach(message -> this.main.messageArea.appendText(message + '\n'));
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
        		            	updateGridpane();
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
                    
                    Label contactLabel = new Label(this.main.contactList.get(i).getName());
                    contactLabel.setFont(new Font("Arial",14));
                    
                    this.main.gridpane.add(contactLabel, 0, i);  
                    this.main.gridpane.add(selectContact, 1, i); 
                    this.main.gridpane.add(deleteContact, 2, i);
            	}
            });
    };
    Thread updateGridThread = new Thread(updateGrid);
    updateGridThread.setDaemon(true);
    updateGridThread.start();

	}

}
