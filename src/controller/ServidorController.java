package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import system.Cliente;
import system.Servidor;

public class ServidorController {
	private TextArea ta;
	private Thread thread;

    @FXML
    private TextField txf_chost;

    @FXML
    private TextField txf_cport;

    @FXML
    private Button btn_connect;

    @FXML
    private TextField txt_port;

    @FXML
    private Button btn_cria;

    @FXML
    private void act_connect(ActionEvent event) {
    	try {
	    	if (btn_connect.getText().contains("Conectar")) {
	    		Cliente cl = new Cliente(txf_chost.getText(), Integer.valueOf(txf_cport.getText()), ta);
	    		btn_connect.setText("Desconectar");
	    		thread = new Thread(cl);
	    		thread.start();
	    		txt_port.setDisable(true);
	    		txf_chost.setDisable(true);
	    		txf_cport.setDisable(true);
	    		btn_cria.setDisable(true);
	    	} else {
	    		if (thread != null)
	    			thread.interrupt();
	    		btn_connect.setText("Conectar");
	    		txt_port.setDisable(false);
	    		txf_chost.setDisable(false);
	    		txf_cport.setDisable(false);
	    		btn_cria.setDisable(false);
	    		Cliente.encerra();
	    	}
	    	ta.clear();
    	} catch (Exception e) {
    		Alert al = new Alert(AlertType.ERROR, e.getMessage());
    		al.setHeaderText("Não foi possível conectar!");
			al.show();
    	}
    }

    @FXML
    private void act_cria(ActionEvent event) {
    	try {
	    	if (btn_cria.getText().contains("Criar")) {
	    		Servidor sv = new Servidor(Integer.valueOf(txt_port.getText()), ta);
	    		thread = new Thread(sv);
	    		thread.start();
	    		btn_cria.setText("Desligar");
	    		txt_port.setDisable(true);
	    		txf_chost.setDisable(true);
	    		txf_cport.setDisable(true);
	    		btn_connect.setDisable(true);
	    	} else {
	    		if (thread != null)
	    			thread.interrupt();
	    		btn_cria.setText("Criar");
	    		txt_port.setDisable(false);
	    		txf_chost.setDisable(false);
	    		txf_cport.setDisable(false);
	    		btn_connect.setDisable(false);
	    		Servidor.encerra();
	    	}
	    	ta.clear();
    	} catch (Exception e) {
    		Alert al = new Alert(AlertType.ERROR, e.getMessage());
    		al.setHeaderText("Não foi possível conectar!");
			al.show();
    	}
    }

	public void criaServidor(TextArea textArea) {
		this.ta = textArea;
	}

}
