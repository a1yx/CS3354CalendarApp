package com.example.calendarevent2;

public class Tokenizer {
	private String string;
	private String token;
	private char delim;
	
	// Default constructor
	public Tokenizer() {
		string = "";
		delim = ',';
		token = "";
	}
	
	// Constructor with string
	public Tokenizer(String istring) {
		string = istring;
		delim = ',';
		token = "";
	}
	
	// Constructor with string and delimiter
	public Tokenizer(String istring, char delimiter) {
		string = istring;
		delim = delimiter;
		token = "";
	}
	
	// Set a new string to the Tokenizer
	public void setString(String istring) {
		string = istring;
	}
	
	// Read in and return the next token, stopping at delimiter or end of file
	public String next() {
		token = "";
		
		if(string.length() <= 0)
			return token;
		
		while(string.charAt(0) != delim) {
			token += string.charAt(0);
			string = string.substring(1);
			
			if(string.length() <= 0)
				return token;
		}
		
		string = string.substring(1);
		return token;
	}
}
