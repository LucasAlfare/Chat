package controller;

import org.controlsfx.control.PopOver;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import system.Cliente;
import system.Servidor;

public class ChatController {

	private PopOver popOver;

    @FXML
    private TextField txf_nome;

    @FXML
    private Button btn_servidor;

    @FXML
    private TextArea ta_chat;

    @FXML
    private TextField txf_text;

    @FXML
    void btn_enter(ActionEvent event) {
    	try {
    		enviaMensagem();
    	} catch (Exception e) {
    		Alert al = new Alert(AlertType.ERROR, e.getMessage());
    		al.setHeaderText("Não foi possível conectar!");
			al.show();
    	}
    }

    @FXML
    private void btn_servidor(ActionEvent event) {
    	try {
	    	if (popOver == null) {
	            popOver = new PopOver();
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Servidor.fxml"));
	            BorderPane p = loader.load();
	            loader.<ServidorController>getController().criaServidor(ta_chat);
	            popOver.setOnCloseRequest(error -> {
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
	            popOver.setContentNode(p);
	            popOver.setAutoFix(true);
	            popOver.setAutoHide(true);
	            popOver.setHideOnEscape(true);
	            popOver.setDetachable(false);
	        }
	        popOver.show(btn_servidor);
    	} catch (Exception e) {
    		Alert al = new Alert(AlertType.ERROR, e.getMessage());
    		al.setHeaderText("Não foi possível conectar!");
			al.show();
    	}
    }

    @FXML
    private void initialize() {
    	txf_text.setOnKeyPressed(action -> {
    		if (action.getCode() == KeyCode.ENTER) {
    			try {
					enviaMensagem();
				} catch (Exception e) {
					Alert al = new Alert(AlertType.ERROR, e.getMessage());
					al.setHeaderText("Não foi possível conectar!");
					al.show();
				}
    		}
    	});
    }

    private void enviaMensagem() throws Exception {
    	if (txf_text.getText().isEmpty())
    		return;

    	String nome = (txf_nome.getText().isEmpty()) ? "Usuário" : txf_nome.getText();
    	if (Servidor.conexaoServidor() != null && Servidor.conexaoServidor().isBound()) {
    		Servidor.envia(nome, txf_text.getText());
    	} else if (Cliente.conexaoCliente() != null && Cliente.conexaoCliente().isConnected()) {
    		Cliente.envia(nome, txf_text.getText());
    	}
    	txf_text.clear();
    }
}
