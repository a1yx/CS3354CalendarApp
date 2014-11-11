import java.io.IOException;
//import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
//import java.io.OutputStream;
import java.sql.ResultSet;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class Servlet extends HttpServlet{
	private static final long serialVersionUID = 2978630166424049845L;

	private static MysqlDataSource dataSource;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		response.getOutputStream().println("Works");
		
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		dataSource = new MysqlDataSource();
		dataSource.setServerName("sql5.freemysqlhosting.net");
		dataSource.setDatabaseName("sql555364");
		
		String choice;
		try{
			ObjectInputStream ois = new ObjectInputStream(request.getInputStream());
			Message requestMessage = (Message)ois.readObject();
			
			ObjectOutputStream oos = new ObjectOutputStream(response.getOutputStream());
			String[] options = requestMessage.getMessage().split(",");
			choice = options[0];
		
			//Makes sure message is intact and unmodified
			//boolean v = verifyMessage(requestMessage);
			
			//if(!v) return;
			
			/*
			 * Currently, this just confirms that the user name and password are valid,
			 * will be expanded to update the data stored on either the server or the application
			 */
			if(choice.equals("sync")){

				try{
					//Set db info
					dataSource.setUser("sql555364");
					dataSource.setPassword("vE8*tS4!");
					
					//Gets connection
					java.sql.Connection con = dataSource.getConnection();
		
//					//Uses prepared statements to help vs SQLi
					String statement = "SELECT * FROM UserInfo WHERE name= ? ";
					java.sql.PreparedStatement ps = con.prepareStatement(statement);
					ps.setString(1, requestMessage.getUser());
					ResultSet rs = ps.executeQuery();
//					
//					//Returns a failure message if the user account doesn't exist
					if(!rs.next()){
						Message rm = new Message("dne","Server","placeholder");
						oos.writeObject(rm);
						rs.close();
						ps.close();
						con.close();
						return;
					}
//					
					else{
						String hash = rs.getString("password");
						if(hash.equals(requestMessage.getHash())){
							Message rm = new Message("true","Server","placeholder");
							oos.writeObject(rm);
							rs.close();
							ps.close();
							con.close();
							return;
						}
						else{
							Message rm = new Message("wrong pass","Server","placeholder");
							oos.writeObject(rm);
							rs.close();
							ps.close();
							con.close();
							return;
						}
					}
				}
				catch(Exception e){
					//Returns a message to the app that it couldn't sync
					Message rm = new Message("false","Server","placeholder");
					oos.writeObject(rm);
					return;
				}	
			}
//			/*
//			 * Adds a new user account, checks the database for an already existing user
//			 * and if it doesn't exist, inserts the user info into the db
//			 */
			
			else if(choice.equals("register")){
				
				dataSource.setUser("sql555364");
				dataSource.setPassword("vE8*tS4!");
				
				//Gets connection
				java.sql.Connection con = dataSource.getConnection();
	
//				//Uses prepared statements to help vs SQLi
				String statement = "INSERT INTO UserInfo (name, password) VALUES ( ? , ? )";
				java.sql.PreparedStatement ps = con.prepareStatement(statement);
				ps.setString(1, requestMessage.getUser());
				ps.setString(2, options[1]);
				ps.execute();
				
				Message rm = new Message("registered","Server","place");
				oos.writeObject(rm);
				return;
				
			}
			else{
				Message rm = new Message("false","Server","place");
				oos.writeObject(rm);
				return;
			}
		}
		catch(Exception e){

		}
	}
	
	/*
	 * This method generates a fresh hash from the message contents and compares it to
	 * the hash provided by the message, determines whether the whole message was intact
	 * and returns a boolean response
	 */
	private static boolean verifyMessage(Message m){
		
		//Gets appropriate hash info from message
		String[] hashInfo = m.getMac().split(":");
		String hash;
		int iterations = Integer.parseInt(hashInfo[0]);
		byte[] salt = hashInfo[1].getBytes();
		String comparable = hashInfo[2];
		
		//Stores all message info in arrays
		char[] msgChars = m.getMessage().toCharArray();
		char[] usrChars = m.getUser().toCharArray();
		char[] hashChars = m.getHash().toCharArray();
		char[] timeChars = String.valueOf(m.getDate().getTime()).toCharArray();
		char[] allChars = new char[msgChars.length + usrChars.length + hashChars.length + timeChars.length];
	
		//Transfers all of these arrays into one array to be hashed
		System.arraycopy(msgChars, 0, allChars, 0, msgChars.length);
		System.arraycopy(usrChars, 0, allChars, msgChars.length, usrChars.length);
		System.arraycopy(hashChars, 0, allChars, msgChars.length + usrChars.length, hashChars.length);
		System.arraycopy(timeChars, 0, allChars, msgChars.length + usrChars.length + hashChars.length, timeChars.length);
		
		//Hashes the information, and compares the result to the expected hash
		try{
			PBEKeySpec pbeks = new PBEKeySpec(allChars, salt, iterations, 64*8);
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] hashBytes = skf.generateSecret(pbeks).getEncoded();
			hash = toHex(hashBytes);
			System.out.println(iterations + ":" + toHex(salt) + ":" + (hash));
		}
		catch(Exception e){ return false; }
		
		//Return true if the hash is unchanged and false if not
		if(hash.equals(comparable)) return true;
		else return false;
	}
	
	//Transforms byte array to a proper length string, includes padding if necessary
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
}