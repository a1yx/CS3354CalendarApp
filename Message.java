import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/*
 * Message Class --> Used to shuttle information between the server and the application
 * Holds the actual message, which usually has multiple parts, the user that sent the message, and the hashed SHA-512 password of the person
 */
public class Message {

	String msg;
	String usr;
	String hash;
	Date sentTime;
	String mac;
	
	public Message(String m, String u, String ha){
		
		this.msg = m;
		this.usr = u;
		this.hash = ha;
		this.sentTime = new Date();
		
		try{
			//Creates a hash made up of the contents of the message and its timestamp
			char[] msgChars = msg.toCharArray();
			char[] usrChars = usr.toCharArray();
			char[] hashChars = hash.toCharArray();
			char[] timeChars = String.valueOf(sentTime.getTime()).toCharArray();
			char[] allChars = new char[msgChars.length + usrChars.length + hashChars.length + timeChars.length];
		
			System.arraycopy(msgChars, 0, allChars, 0, msgChars.length);
			System.arraycopy(usrChars, 0, allChars, msgChars.length, usrChars.length);
			System.arraycopy(hashChars, 0, allChars, msgChars.length + usrChars.length, hashChars.length);
			System.arraycopy(timeChars, 0, allChars, msgChars.length + usrChars.length + hashChars.length, timeChars.length);
			
			int iterations = 5000;
			byte[] salt = getSalt();
			String oSalt = toHex(salt);
			PBEKeySpec pbeks = new PBEKeySpec(allChars, oSalt.getBytes(), iterations, 64*8);
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] hash = skf.generateSecret(pbeks).getEncoded();
			this.mac = iterations + ":" + (oSalt) + ":" + toHex(hash);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private byte[] getSalt(){
		
		SecureRandom sr = new SecureRandom();
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt;
		
	}
	
	private static String toHex(byte[] a){
		BigInteger b = new BigInteger(1,a);
		String hex = b.toString(16);
		int paddingLength = (a.length * 2) - hex.length();
		if(paddingLength > 0){
			return "0" + hex;
		}
		else{
			return hex;
		}
	}
	
	public String getMessage(){
		return this.msg;
	}
	
	public String getUser(){
		return this.usr;
	}
	
	public String getHash(){
		return this.hash;
	}
	
	public Date getDate(){
		return this.sentTime;
	}
	
	public String getMac(){
		return this.mac;
	}
	
}
