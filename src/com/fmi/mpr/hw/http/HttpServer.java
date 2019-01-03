package com.fmi.mpr.hw.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
	private ServerSocket server;
	private String fileUploadView;

	public HttpServer(int port) throws IOException {
		server = new ServerSocket(port);
		loadFileUploadView();
		System.out.println("Server configured on port " + port);
	}

	public void listen() {
		while (true) {
			try {
				System.out.println("Server attempting to listen...");

				try (Socket socket = server.accept()) {
					System.out.println("Received connection from " + socket.getLocalAddress());
					
					process(socket);
				}
			} catch (Exception e) {
				System.out.println("Error while receiving connection!");
			}
		}
	}

	private void process(Socket socket) throws IOException {
		try(InputStream socketStream = socket.getInputStream();
				InputStreamReader inputReader = new InputStreamReader(socketStream);
				BufferedReader bufferedReader = new BufferedReader(inputReader)) {
			String firstLine = bufferedReader.readLine();
			
			// TODO: distinguish download/get upload form
			if(firstLine == null) {
				System.out.println("Received empty request!");
			}
			else if(firstLine.startsWith("POST")) {
				System.out.println("Received POST request!");
				uploadFile(socket);
			}
			else if(firstLine.startsWith("GET")) {
				System.out.println("Received GET request!");
				sendUploadView(socket);
			}
			else {
				System.out.println("Unknown request type!");
			}
		}
	}

	private void sendUploadView(Socket socket) throws IOException {
		try(OutputStream output = socket.getOutputStream();
				PrintStream printOutput = new PrintStream(output, true)) {
			printOutput.println("HTTP/1.1 200 OK");
			printOutput.println();
			printOutput.println(fileUploadView);
		}
	}

	private void uploadFile(Socket socket) throws IOException {
		try(OutputStream output = socket.getOutputStream();
				PrintStream printOutput = new PrintStream(output, true)) {
			printOutput.println("HTTP/1.1 200 OK");
			printOutput.println();
			// TODO: receive file
			
			printOutput.println("Uploaded file successfully!");
		}
	}
	
	private void loadFileUploadView() throws IOException {
		fileUploadView = Utils.getResourceAsString("/fileUpload.html");
		System.out.println("Loaded fileUpoloadView: ");
		System.out.println(fileUploadView);
	}
}
