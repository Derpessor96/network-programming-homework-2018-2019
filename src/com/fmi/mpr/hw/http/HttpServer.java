package com.fmi.mpr.hw.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class HttpServer {
	private ServerSocket server;
	private String fileUploadView;
	private String notFoundView;
	private String serverErrorView;

	public HttpServer(int port) throws IOException {
		server = new ServerSocket(port);
		loadViews();
		System.out.println("Server configured on port " + port);
	}

	public void listen() {
		while (true) {
			try {
				System.out.println("Server attempting to listen...");

				try (Socket socket = server.accept()) {
					System.out.println("Received connection from " + socket.getRemoteSocketAddress());
					
					process(socket);
				}
			} catch (Exception e) {
				System.out.println("Error while receiving connection!");
			}
		}
	}

	private void process(Socket socket) throws IOException {
		InputStreamReader inputReader = null;
		BufferedReader bufferedReader = null;
		
		try {
			inputReader = new InputStreamReader(socket.getInputStream());
			bufferedReader = new BufferedReader(inputReader);
			
			String firstLine = bufferedReader.readLine();
			
			if(firstLine == null) {
				System.out.println("Received bad request!");
				sendNotFound(socket); // TODO: send something else
			}
			
			String[] httpRequest = firstLine.split("\\s+");
			
			if(httpRequest.length != 3) {
				System.out.println("Received bad request!");
				sendNotFound(socket); // TODO: send something else
			}
			
			String method = httpRequest[0];
			String path = httpRequest[1];
			
			Map<String, String> headers = Utils.getHeaders(bufferedReader);
			System.out.println(headers);
			
			// TODO: add download
			// TODO: distinguish download/get upload form
			if(method.equals("POST")) {
				System.out.println("Received POST request!");
				uploadFile(socket);
			}
			else if(method.equals("GET")
					&& path.equals("/")) {
				System.out.println("Received GET request!");
				sendUploadView(socket);
			}
			else {
				System.out.println("Unknown request type!");
				sendNotFound(socket);
			}
		}
		catch(Exception e) {
			sendServerError(socket);
			
			if(bufferedReader != null) {
				bufferedReader.close();
			}
			if(inputReader != null) {
				inputReader.close();
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
	
	private void sendNotFound(Socket socket) throws IOException {
		try(OutputStream output = socket.getOutputStream();
				PrintStream printOutput = new PrintStream(output, true)) {
			printOutput.println("HTTP/1.1 404 Not Found");
			printOutput.println();
			
			printOutput.println(notFoundView);
		}
	}
	
	private void sendServerError(Socket socket) throws IOException {
		try(OutputStream output = socket.getOutputStream();
				PrintStream printOutput = new PrintStream(output, true)) {
			printOutput.println("HTTP/1.1 500 Internal Server Error");
			printOutput.println();
			
			printOutput.println(serverErrorView);
		}
	}
	
	private void loadViews() throws IOException {
		fileUploadView = Utils.getResourceAsString("/fileUpload.html");
		System.out.println("Loaded fileUpoloadView: ");
		System.out.println(fileUploadView);

		notFoundView = Utils.getResourceAsString("/notFound.html");
		System.out.println("Loaded notFoundView: ");
		System.out.println(notFoundView);
		
		serverErrorView = Utils.getResourceAsString("/serverError.html");
		System.out.println("Loaded notFoundView: ");
		System.out.println(serverErrorView);
	}
}
