package com.fmi.mpr.hw.http;

import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		HttpServer server;
		
		while(true) {
			System.out.print("Please enter server port: ");
			
			int port = scanner.nextInt();
			
			try {
				server = new HttpServer(port);
				
				break;
			} catch (Exception e) {
				System.out.println("Could not initialize server");
			}
		}
		
		server.listen();
	}
}