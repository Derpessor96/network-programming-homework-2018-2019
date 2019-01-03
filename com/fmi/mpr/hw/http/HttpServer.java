package com.fmi.mpr.hw.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
	private ServerSocket server;

	public HttpServer(int port) throws IOException {
		server = new ServerSocket(port);
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
			
			
			if(firstLine == null) {
				System.out.println("Received empty request!");
			}
			else if(firstLine.startsWith("POST")) {
				System.out.println("Received POST request!");
			}
			else if(firstLine.startsWith("GET")) {
				System.out.println("Received GET request!");
			}
			else {
				System.out.println("Unknown request type!");
			}
		}
	}
}
