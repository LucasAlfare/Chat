package system;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import javafx.scene.control.TextArea;

public class Cliente extends Thread {
	static Socket connection;
	static TextArea text;
	static PrintStream print;

	public Cliente(String ip, int porta, TextArea ta) throws Exception {
		connection = new Socket(ip, porta);
		if (!connection.isConnected())
			throw new Exception("Não foi possível conectar.");

		text = ta;
		print = new PrintStream(connection.getOutputStream());
	}

	public static Socket conexaoCliente() throws Exception {
		return connection;
	}

	public void executa() throws Exception {
		Scanner scan = new Scanner(connection.getInputStream());

		while (scan.hasNextLine()) {
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

	public static void envia(String usr, String tx) throws Exception {
		if (connection.isClosed()) {
			text.setText(text.getText() + "Servidor desconectado!\n");
			text.end();
			encerra();
			return;
		}

		String message = text.getText() + usr + ": " + tx + "\n";
		text.setText(message);
		text.end();
		print.println(message);
	}

	public static void encerra() throws Exception {
		if (connection != null) {
			connection.close();
			connection = null;
		}
	}

	public void run() {
		try {
			if (connection.isClosed()) {
				text.setText(text.getText() + "Servidor desconectado!\n");
				text.end();
				encerra();
				Thread.currentThread().interrupt();
			}

			executa();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
