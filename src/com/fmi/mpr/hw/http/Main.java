package com.fmi.mpr.hw.http;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		HttpServer server;

		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				System.out.print("Please enter server port: ");

				int port = scanner.nextInt();

				try {
					server = new HttpServer(port);

					break;
				} catch (Exception e) {
					System.out.println("Could not initialize server");
				}
			}
		}

		server.listen();
	}
}