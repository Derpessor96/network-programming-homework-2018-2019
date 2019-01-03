package com.fmi.mpr.hw.http;

import java.io.IOException;
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
					System.out.println("Recieved connection from " + socket.getLocalAddress());
				}
			} catch (Exception e) {
				System.out.println("Error while recieving connection!");
			}
		}
	}
}
