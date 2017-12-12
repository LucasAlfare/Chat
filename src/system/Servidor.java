package system;

import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javafx.scene.control.TextArea;

public class Servidor extends Thread {
	static ServerSocket connection;
	static TextArea text;
	static Socket cliente;
	static int clientes = 0;

	public Servidor(int porta, TextArea ta) throws Exception {
		connection = new ServerSocket(porta);
		if (!connection.isBound())
			throw new Exception("Não foi possível conectar.");

		text = ta;
	}

	public static ServerSocket conexaoServidor() throws Exception {
		return connection;
	}

	public void executa() throws Exception {
		cliente = connection.accept();
		Scanner scan = new Scanner(cliente.getInputStream());

		while (scan.hasNextLine()) {
			if (cliente.isClosed()) {
				text.setText(text.getText() + "Cliente desconectado!\n");
				text.end();
				encerra();
				break;
			}

			if (connection.isClosed()) {
				text.setText(text.getText() + "Servidor desconectado!\n");
				text.end();
				encerra();
				break;
			}

			text.setText(text.getText() + scan.nextLine() + "\n");
			text.end();
		}
		scan.close();
	}

	public static void envia(String cl, String tx) throws Exception {
		if (cliente == null || cliente.isClosed()) {
			text.setText(text.getText() + "Cliente desconectado!\n");
			text.end();
			return;
		}

		if (connection.isClosed()) {
			text.setText(text.getText() + "Servidor desconectado!\n");
			encerra();
			return;
		}

		PrintStream message = new PrintStream(cliente.getOutputStream());
		String msg = cl + ": " + tx;
		text.setText(text.getText() + msg + "\n");
		text.end();
		message.println(msg);
	}

	public static void encerra() throws Exception {
		if (connection != null) {
			connection.close();
			connection = null;
		}

		if (cliente != null) {
			cliente.close();
			cliente = null;
		}
	}

	public void run() {
		try {
			if (cliente != null && cliente.isClosed()) {
				text.setText(text.getText() + "Cliente desconectado!\n");
				text.end();
				encerra();
				Thread.currentThread().interrupt();
			}

			if (connection.isClosed()) {
				text.setText(text.getText() + "Servidor desconectado!\n");
				text.end();
				encerra();
				Thread.currentThread().interrupt();
			}

			executa();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
}
