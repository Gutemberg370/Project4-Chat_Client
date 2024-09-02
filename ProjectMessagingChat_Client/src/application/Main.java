package application;
	
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;


public class Main extends Application {
	
	public TextArea messageArea = new TextArea();
	
	public TextField messageField = new TextField();
	
	public GridPane gridpane = new GridPane();
	
	@FXML ComboBox onlineStatusComboBox = new ComboBox();
	
	public List<Contact> contactList = new ArrayList<>();
	
	public Contact myContact;
	
	public Contact selectedContact;
	
	private String message;
	
	public ServerConnection serverConnection = new ServerConnection(this);
	
	private UserConnection userConnection = new UserConnection(this);
	
	public SupportFunctions supportFunctions = new SupportFunctions(this);
	
	public ObservableList<String> contactStatus = FXCollections.observableArrayList("online","offline");
	
		
	// Criar página de login no servidor
    private Parent createServerLogin() {
    	Pane root = new Pane();
    	
    	BackgroundFill backgroundFill = new BackgroundFill(Color.valueOf("#E27D60"), new CornerRadii(10), new Insets(10));

    	Background background = new Background(backgroundFill);
    	
    	root.setBackground(background);
    	
    	root.setPrefSize(544, 492);
    	
    	Label serverLabel = new Label("LOGIN NO SERVIDOR");
    	serverLabel.setFont(new Font("Monaco",36));
    	serverLabel.setLayoutX(110);
    	serverLabel.setLayoutY(40);
    	
    	Label title = new Label("Insira o seu nome, o ip do servidor a se conectar, \n o nome do servidor e a porta. Então clique no botão abaixo \n para fazer o login.");
    	title.setFont(new Font("Arial",18));
    	title.setLayoutX(45);
    	title.setLayoutY(120);
    	title.setTextAlignment(TextAlignment.CENTER);
    	
    	Label userName = new Label("Nome  :");
    	userName.setFont(new Font("Arial",13));
    	userName.setLayoutX(142);
    	userName.setLayoutY(225);
    	
    	TextField userNameInput = new TextField();
    	userNameInput.setLayoutX(195);
    	userNameInput.setLayoutY(220);
    	userNameInput.setMinWidth(220);
    	
    	Label serverIp = new Label("Ip do Servidor  :");
    	serverIp.setFont(new Font("Arial",13));
    	serverIp.setLayoutX(95);
    	serverIp.setLayoutY(265);
    	
    	TextField serverIpInput = new TextField("192.168.0.14");
    	serverIpInput.setLayoutX(195);
    	serverIpInput.setLayoutY(260);
    	serverIpInput.setMinWidth(220);
    	
    	Label serverName = new Label("Nome do Servidor :");
    	serverName.setFont(new Font("Arial",13));
    	serverName.setLayoutX(75);
    	serverName.setLayoutY(305);
    	
    	TextField serverNameInput = new TextField("Server");
    	serverNameInput.setLayoutX(195);
    	serverNameInput.setLayoutY(300);
    	serverNameInput.setMinWidth(220);
    	
    	Label serverPort = new Label("Porta do Servidor :");
    	serverPort.setFont(new Font("Arial",13));
    	serverPort.setLayoutX(75);
    	serverPort.setLayoutY(345);
    	
    	TextField serverPortInput = new TextField("6000");
    	serverPortInput.setLayoutX(195);
    	serverPortInput.setLayoutY(340);
    	serverPortInput.setMinWidth(220);
    	
    	Button loginButton = new Button("Fazer Login no Servidor");
    	loginButton.setLayoutX(210);
    	loginButton.setLayoutY(390);
    	loginButton.setMinWidth(150);
    	loginButton.setOnAction(event -> {	
        	this.myContact = new Contact(userNameInput.getText(), "online");
        	this.serverConnection.setUrl(serverIpInput.getText(), serverPortInput.getText(), serverNameInput.getText());
        	
        	Stage window = (Stage)loginButton.getScene().getWindow();
        	Scene scene = new Scene(createUserConnection());
        	scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        	window.setScene(scene);
        	window.setResizable(false);
			
        });
    	
    	root.getChildren().addAll(serverLabel, title, userName, userNameInput, serverIp, serverIpInput, serverName, serverNameInput,
    			serverPort, serverPortInput, loginButton);
    	
    	return root;
    }
    
 // Criar página de login do usuário
    private Parent createUserConnection() {
    	Pane root = new Pane();
    	
    	BackgroundFill backgroundFill = new BackgroundFill(Color.valueOf("#E27D60"), new CornerRadii(10), new Insets(10));

    	Background background = new Background(backgroundFill);
    	
    	root.setBackground(background);
    	
    	root.setPrefSize(524, 402);
    	
    	Label loginLabel = new Label("LOGIN");
    	loginLabel.setFont(new Font("Monaco",36));
    	loginLabel.setLayoutX(210);
    	loginLabel.setLayoutY(40);
    	
    	Label title = new Label("Insira o seu ip e a porta. Então clique no botão abaixo \n para fazer o login.");
    	title.setFont(new Font("Arial",18));
    	title.setLayoutX(45);
    	title.setLayoutY(120);
    	title.setTextAlignment(TextAlignment.CENTER);
    	
    	Label userIp = new Label("Ip :");
    	userIp.setFont(new Font("Arial",13));
    	userIp.setLayoutX(145);
    	userIp.setLayoutY(225);
    	
    	TextField userIpInput = new TextField("192.168.0.14");
    	userIpInput.setLayoutX(175);
    	userIpInput.setLayoutY(220);
    	userIpInput.setMinWidth(220);
    	
    	Label userPort = new Label("Porta :");
    	userPort.setFont(new Font("Arial",13));
    	userPort.setLayoutX(125);
    	userPort.setLayoutY(265);
    	
    	TextField userPortInput = new TextField("6001");
    	userPortInput.setLayoutX(175);
    	userPortInput.setLayoutY(260);
    	userPortInput.setMinWidth(220);
    	
    	Button loginButton = new Button("Fazer Login");
    	loginButton.setLayoutX(190);
    	loginButton.setLayoutY(320);
    	loginButton.setMinWidth(150);
    	loginButton.setOnAction(event -> {	
    		
    		// Registrar contato logado no servidor e retornar contatos e mensagens do contato
    		this.contactList = this.serverConnection.registerContact(myContact).getMyContacts();  		

        	Stage window = (Stage)loginButton.getScene().getWindow();
        	Scene scene = new Scene(createClientPage());
        	scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        	window.setScene(scene);
        	window.setResizable(false);
        	
        	// Criar registro RMI próprio do cliente
			try {
				Registry rmiregistry;
				rmiregistry = LocateRegistry.createRegistry(Integer.valueOf(userPortInput.getText()));
				rmiregistry.bind(this.myContact.getName(), new User(this));
				this.myContact.setUrl(userIpInput.getText(),userPortInput.getText(),this.myContact.getName());
			} catch (AlreadyBoundException | NumberFormatException | RemoteException e) {
				e.printStackTrace();
			} 
        	
			
        });
    	
    	root.getChildren().addAll(loginLabel, title, userIp, userIpInput, userPort, userPortInput, loginButton);
    	
    	return root;
    }
	
	// Função que cria a página do cliente
	private Parent createClientPage() {
		
    	Pane root = new Pane();
    	
    	BackgroundFill backgroundFill = new BackgroundFill(Color.valueOf("#ADD8E6"), new CornerRadii(10), new Insets(10));

    	Background background = new Background(backgroundFill);
    	
    	root.setBackground(background);
    	
    	root.setPrefSize(644, 592);
    	
    	Label name = new Label(this.myContact.getName());
    	name.setFont(new Font("Arial",30));
    	name.setLayoutX(40);
    	name.setLayoutY(20);
        
    	this.onlineStatusComboBox.setItems(contactStatus);
    	this.onlineStatusComboBox.getSelectionModel().selectFirst();
    	this.onlineStatusComboBox.setLayoutX(40);
    	this.onlineStatusComboBox.setLayoutY(70);
    	this.onlineStatusComboBox.setMinWidth(120);
    	
    	this.onlineStatusComboBox.setOnAction((event) -> {	
    		
    	    this.myContact.setStatus(this.onlineStatusComboBox.getValue().toString());
    	    
    	    ContactCatalog newContactList = this.serverConnection.notifyStatusChange(myContact);
    	    
    	    // Caso a lista vinda do servidor não seja vazia, deve-se atualizar a lista de mensagens
    	    // do contato com as mensagens guardadas no servidor
    	    if(newContactList != null && newContactList.getMyContacts().size() > 0) {
    	    	
        		List<Contact> requestedContactList = newContactList.getMyContacts();
        		
        		requestedContactList.forEach(cont ->{
        			Boolean hasContact = false;
        			// Caso já tenha o contato na lista
        			for(Contact listContact: this.contactList) {
        				if(listContact.getName().equals(cont.getName())) {
        					cont.getMessages().forEach(msg -> listContact.addMessage(msg));
        					hasContact = true;
        				}
        			}
        			// Caso não tenha o contato na lista
        			if(hasContact == false) {
						
						Contact newContact = new Contact(cont.getName(), "-");
						
						newContact.setUrl(cont.getUrl());
						
						cont.getMessages().forEach(msg -> newContact.addMessage(msg));
						
						this.contactList.add(newContact);
        			}
        		});
        		
        		// Atualizar o grid com os novos contatos adicionados
    	    	this.supportFunctions.updateGridpane();
    	    }
    	    
            this.messageField.setEditable(false);
            this.messageField.setDisable(true);   	    	

    	});
    	
    	Rectangle r1 = new Rectangle();
    	r1.setX(10);
    	r1.setY(120);
    	r1.setWidth(200);
    	r1.setHeight(10);
    	r1.setFill(Color.BLUE);
    	
    	Rectangle r2 = new Rectangle();
    	r2.setX(210);
    	r2.setY(10);
    	r2.setWidth(10);
    	r2.setHeight(573);
    	r2.setFill(Color.BLUE);
    	
    	Label contactCatalogLabel = new Label("Catálogo de contatos");
    	contactCatalogLabel.setFont(new Font("Arial",18));
    	contactCatalogLabel.setLayoutX(20);
    	contactCatalogLabel.setLayoutY(140);
    	
    	Button addNewContactButton = new Button("Adicionar novo contato");
    	addNewContactButton.setMinWidth(150);
    	addNewContactButton.setLayoutX(35);
    	addNewContactButton.setLayoutY(530);
    	addNewContactButton.setOnAction(event -> {
        	Stage window = (Stage)addNewContactButton.getScene().getWindow();
        	Scene scene = new Scene(establishContactConnection());
        	scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        	window.setScene(scene);
        	window.setResizable(false);
        });
    	
    	// Criar a lista de contatos na interface gráfica
    	this.gridpane.getChildren().clear();  	
    	
    	for(int i = 0; i < contactList.size(); i++) {
    		   		   		
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
            	if(this.selectedContact != null) {
                	this.supportFunctions.setContactColor(Color.BLACK);
            	}
            	this.selectedContact = contactList.get(contactId);
            	this.supportFunctions.setContactColor(Color.GREEN);
            	this.messageArea.setText(null);
            	if(this.myContact.getStatus().equals("online")) {
                	messageField.setEditable(true);
                	messageField.setDisable(false);
            	}
            	this.selectedContact.getMessages().forEach(message -> this.messageArea.appendText(message + '\n'));
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
            	
            	this.serverConnection.notifyContactDeleted(myContact, contactList.get(contactId));
		        Runnable deleteUserContact = () -> {
		            Platform.runLater(() -> {
		            	this.gridpane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == contactId);
		            	this.supportFunctions.updateGridpane();
		            });
		        };
		        Thread deleteUserContactThread = new Thread(deleteUserContact);
		        deleteUserContactThread.setDaemon(true);
		        deleteUserContactThread.start();
            	if(selectedContact != null && contactList.get(contactId).getName().equals(selectedContact.getName())) {
            		selectedContact = null;
                    this.messageField.setEditable(false);
                    this.messageField.setDisable(true);
                    this.messageArea.setText(null);
            	}
        		contactList.remove(contactId);
            });
            
            Label contactLabel = new Label(contactList.get(i).getName());
            contactLabel.setFont(new Font("Arial",14));
            
    		this.gridpane.add(contactLabel, 0, i);  
    		this.gridpane.add(selectContact, 1, i); 
    		this.gridpane.add(deleteContact, 2, i); 
    	}
    	    	
    	this.gridpane.setHgap(10); 
    	this.gridpane.setVgap(10);
    	this.gridpane.setLayoutX(25);
    	this.gridpane.setLayoutY(170);   	
    	
    	this.messageArea.setWrapText(true);
    	this.messageArea.setStyle("-fx-font-size: 15;");
        this.messageArea.setPrefHeight(525);
        this.messageArea.setPrefWidth(400);
        this.messageArea.setEditable(false);  
        
        this.messageField.setEditable(false);
        this.messageField.setDisable(true);
        this.messageField.setOnAction(event -> {
        	this.message = this.myContact.getName() + ": ";
        	this.message += this.messageField.getText();
        	this.messageField.clear();       	
        	this.messageArea.appendText(this.message + "\n");
        	this.selectedContact.addMessage(this.message);
        	
        	try {
        		// Enviando a mensagem diretamente para outro usuário
        		this.userConnection.setUrl(this.selectedContact.getUrl());
				this.userConnection.sendMessage(this.myContact, this.message);
			} catch (Exception e) {
				e.printStackTrace();
			}
        	
        	
        });
     		
        VBox chatBox = new VBox(0, this.messageArea, messageField);
        chatBox.setLayoutX(225);
        chatBox.setLayoutY(15);
  	
    	root.getChildren().addAll(name, onlineStatusComboBox, r1, r2, contactCatalogLabel, gridpane, addNewContactButton, chatBox);
    	
    	return root;
	}
	
	// Página para adicionar um novo contato na lista de contatos
	private Parent establishContactConnection() {
    	Pane root = new Pane();
    	
    	BackgroundFill backgroundFill = new BackgroundFill(Color.valueOf("#E27D60"), new CornerRadii(10), new Insets(10));

    	Background background = new Background(backgroundFill);
    	
    	root.setBackground(background);
    	
    	root.setPrefSize(544, 400);
    	
    	Label contactLabel = new Label("CRIAR NOVO CONTATO");
    	contactLabel.setFont(new Font("Monaco",36));
    	contactLabel.setLayoutX(80);
    	contactLabel.setLayoutY(40);
    	
    	Label title = new Label("Insira o nome do contato, o ip do contato e a porta do contato.\n Então clique no botão abaixo para fazer a conexão.");
    	title.setFont(new Font("Arial",18));
    	title.setLayoutX(25);
    	title.setLayoutY(120);
    	title.setTextAlignment(TextAlignment.CENTER);
    	
    	Label contactName = new Label("Nome do Contato  :");
    	contactName.setFont(new Font("Arial",13));
    	contactName.setLayoutX(85);
    	contactName.setLayoutY(205);
    	
    	TextField contactNameInput = new TextField();
    	contactNameInput.setLayoutX(205);
    	contactNameInput.setLayoutY(202);
    	contactNameInput.setMinWidth(220);
    	
    	Label contactIp = new Label("Ip do Contato  :");
    	contactIp.setFont(new Font("Arial",13));
    	contactIp.setLayoutX(105);
    	contactIp.setLayoutY(245);
    	
    	TextField contactIpInput = new TextField("192.168.0.14");
    	contactIpInput.setLayoutX(205);
    	contactIpInput.setLayoutY(242);
    	contactIpInput.setMinWidth(220);
    	
    	Label contactPort = new Label("Porta do Contato :");
    	contactPort.setFont(new Font("Arial",13));
    	contactPort.setLayoutX(85);
    	contactPort.setLayoutY(285);
    	
    	TextField contactPortInput = new TextField("6002");
    	contactPortInput.setLayoutX(205);
    	contactPortInput.setLayoutY(282);
    	contactPortInput.setMinWidth(220);
    	
    	Button createContactButton = new Button("Criar Contato");
    	createContactButton.setLayoutX(210);
    	createContactButton.setLayoutY(330);
    	createContactButton.setMinWidth(150);
    	createContactButton.setOnAction(event -> {	
    		
    		// Adicionar novo contato a lista e notificar servidor
        	Contact newContact = new Contact(contactNameInput.getText(),"-");
        	newContact.setUrl(contactIpInput.getText(), contactPortInput.getText(), contactNameInput.getText());
        	this.contactList.add(newContact);
        	this.serverConnection.notifyNewContact(myContact, newContact);
        	
        	Stage window = (Stage)createContactButton.getScene().getWindow();
        	Scene scene = new Scene(createClientPage());
        	scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        	window.setScene(scene);
        	window.setResizable(false);
        	
			
        });
    	
    	root.getChildren().addAll(contactLabel, title, contactName, contactNameInput, contactIp, contactIpInput,
    			contactPort, contactPortInput, createContactButton);
    	
    	return root;
    }
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Scene clientPage = new Scene(createServerLogin());
			primaryStage.setTitle("Cliente");;
			primaryStage.setScene(clientPage);
			primaryStage.setResizable(false);
			primaryStage.show();
			clientPage.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
