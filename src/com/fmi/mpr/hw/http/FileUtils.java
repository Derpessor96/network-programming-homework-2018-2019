package com.fmi.mpr.hw.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class FileUtils {
	private FileUtils() {
		// Prevent instantiation
	}
	
	public static String getResourceAsString(String name) throws IOException {
		String result = "";
		
		try(InputStream input = HttpServer.class.getResourceAsStream(name);
				InputStreamReader inputReader = new InputStreamReader(input);
				BufferedReader bufferedInput = new BufferedReader(inputReader)) {
			StringBuilder builder = new StringBuilder();
			String currLine = "";
			
			while(currLine != null) {
				currLine = bufferedInput.readLine();
				builder.append(currLine);
			}
			
			result = builder.toString();
		}
		
		return result;
	}
}
