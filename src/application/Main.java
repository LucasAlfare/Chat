package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import system.Cliente;
import system.Servidor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Stage stage = new Stage();
			stage.setTitle("Chat Online");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Chat.fxml"));
			BorderPane root = loader.load();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setOnCloseRequest(event -> {
				try {
					if (Servidor.conexaoServidor() != null && Servidor.conexaoServidor().isBound()) {
						Servidor.encerra();
					} else if (Cliente.conexaoCliente() != null && Cliente.conexaoCliente().isConnected()) {
						Cliente.encerra();
					}
				} catch (Exception e) {
					Alert al = new Alert(AlertType.ERROR, e.getMessage());
					al.setHeaderText("Não foi possível conectar!");
					al.show();
				}
			});
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
