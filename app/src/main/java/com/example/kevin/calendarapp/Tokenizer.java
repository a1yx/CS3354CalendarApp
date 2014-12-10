package com.example.kevin.calendarapp;

/**
 * Takes in a string and will split it into parts
 * based on the delimeter specified
 *
 * @author Kevin Szwagiel
 */
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
	
	/**
	 * The main function. Will read a string up to the delimeter.
	 * Removes the part of the string that was read
	 *
	 * @return The string up to the next delimeter
	 */
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
