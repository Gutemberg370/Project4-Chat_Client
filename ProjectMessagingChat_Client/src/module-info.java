module ProjectMessagingChat_Client {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires java.rmi;
	
	opens application to javafx.graphics, javafx.fxml;
	exports application to java.rmi;
}
