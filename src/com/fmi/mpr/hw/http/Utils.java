package com.fmi.mpr.hw.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public final class Utils {
	private Utils() {
		// Prevent instantiation
	}
	
	public static String getResourceAsString(String name) throws IOException {
		String result = "";
		
		try(InputStream input = HttpServer.class.getResourceAsStream(name);
				InputStreamReader inputReader = new InputStreamReader(input);
				BufferedReader bufferedInput = new BufferedReader(inputReader)) {
			StringBuilder builder = new StringBuilder();
			String currLine;
			
			do {
				currLine = bufferedInput.readLine();
				if(currLine == null) {
					break;
				}
				
				builder.append(currLine);
				builder.append("\r\n");
			}
			while(true);
			
			result = builder.toString();
		}
		
		return result;
	}
	
	public static Map<String, String> getHeaders(BufferedReader reader) throws IOException {
		Map<String, String> headers = new HashMap<>();
		String currLine;
		
		do {
			currLine = reader.readLine();
			if(currLine == null || currLine.isEmpty()) {
				break;
			}

			int splitIndex = currLine.indexOf(":");
			
			if(splitIndex == -1) {
				continue;
			}
			
			String key = currLine.substring(0, splitIndex);
			String value = currLine.substring(splitIndex);
			
			headers.put(key, value);
		}
		while(true);
			
		return headers;
	}
}
