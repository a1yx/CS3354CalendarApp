import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
//import java.io.InputStream;
//import java.io.OutputStream;

public class Servlet extends HttpServlet{
	private static final long serialVersionUID = 2978630166424049845L;

	private static MysqlDataSource dataSource;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		InputStream input = request.getInputStream();
		byte[] req = null;
		
		while(input.read() != -1){
			req = new byte[input.available()];
			input.read(req);
		}
		
		String s = new String(req);
		input.close();
		String[] array = s.split(",");
	
		dataSource = new MysqlDataSource();
		dataSource.setServerName("sql4.freemysqlhosting.net");
		dataSource.setDatabaseName("sql458181");
		dataSource.setUser("sql458181");
		dataSource.setPassword("iY1%xT1!");
		Connection con = null;
		try{
			con = dataSource.getConnection();
		}
		catch(Exception e){
			OutputStream output = response.getOutputStream();
			output.write("Database Error".getBytes());
			output.close();
		}
		
		if(array[0].equals("ADD")){
			try{
				String statement = "INSERT INTO eventData (userID, uuid, name, description,"
						+ " location, startTime, endTime, startDate, endDate, category) "
						+ "VALUES ('"+ array[1] + "','"+ array[2] + "','"+ array[3] +"','"+ array[4] +"','"+ array[5] +"','"+ array[6] +"','"+ array[7] +"','"+ array[8] +"','"+ array[9] +"','"+ array[10] +"')";
				
				Statement stmt = con.createStatement();
				boolean success = stmt.execute(statement);
				
				OutputStream output = response.getOutputStream();
				output.write(new String(String.valueOf(success)).getBytes());
				output.close();
				
			}
			catch(Exception e){
				printError(e, response.getOutputStream());
				return;
			}
		}
		
		else if(array[0].equals("EDIT")){
			
			try{
				String statement = "UPDATE eventData SET name='" + array[3] + "', description='" + array[4] + "', location='" + array[5] + "', startTime='" + array[6]
						+ "', endTime='" + array[7] + "', startDate='" + array[8] + "', endDate='" + array[9] + "', category='" + array[10] + "' WHERE uuid='" 
						+ array[2] + "' AND userID='" + array[1] + "'";
				Statement stmt = con.createStatement();
				boolean success = stmt.execute(statement);
				
				OutputStream output = response.getOutputStream();
				output.write(new String(String.valueOf(success)).getBytes());
				output.close();
			}
			catch(Exception e){
				printError(e, response.getOutputStream());
				return;
			}
			
		}
		else if(array[0].equals("REMOVE")){
			
			try{
				String statement = "DELETE FROM eventData WHERE uuid='" + array[2] + "' AND userID='" + array[1] + "'";
				Statement stmt = con.createStatement();
				boolean success = stmt.execute(statement);
				
				OutputStream output = response.getOutputStream();
				output.write(new String(String.valueOf(success)).getBytes());
				output.close();
			}
			catch(Exception e){
				printError(e, response.getOutputStream());
				return;
			}
			
		}

	}
	
	public void printError(Exception e, OutputStream out){
		try{
			StringWriter error = new StringWriter();
			e.printStackTrace(new PrintWriter(error));
			out.write(new String(error.toString()).getBytes());
			out.close();
			return;
		}
		catch(Exception ex){
			return;
		}
		
	}
		
//		String[] received = s.split(",");
//		
//		String op = received[0];
//		String id = received[1];
		
//		try{
//			dataSource = new MysqlDataSource();
//			dataSource.setServerName("mavcal.pagebit.net");
//			dataSource.setDatabaseName("xe33717c2x_calev");
//			dataSource.setUser("xe33717c2x");
//			dataSource.setPassword("b3hc0646YK");
//			java.sql.Connection con = dataSource.getConnection();
//			s = "Success";
//		}
//		catch(Exception e){
//			s = "Failure";
//		}

//		if(op.equals("ADD")){
//			String uuid, name, description, location, startTime, endTime, startDate, endDate, category;
//			
//			uuid = received[2];
//			name = received[3];
//			description = received[4];
//			location = received[5];
//			startTime = received[6];
//			endTime = received[7];
//			startDate = received[8];
//			endDate = received[9];
//			category = received[10];
//			//Code to add event to sql server here
//		}
//		else if(op.equals("DELETE")){
//			String uuid = received[2];
//			//Code to delete event from sql server here
//		}
		

//		
//		String choice;
//		try{
//			ObjectInputStream ois = new ObjectInputStream(request.getInputStream());
//			Message requestMessage = (Message)ois.readObject();
//			
//			ObjectOutputStream oos = new ObjectOutputStream(response.getOutputStream());
//			String[] options = requestMessage.getMessage().split(",");
//			choice = options[0];
//		
//			//Makes sure message is intact and unmodified
//			//boolean v = verifyMessage(requestMessage);
//			
//			//if(!v) return;
//			
//			/*
//			 * Currently, this just confirms that the user name and password are valid,
//			 * will be expanded to update the data stored on either the server or the application
//			 */
//			if(choice.equals("sync")){
//
//				try{
//					//Set db info
//					dataSource.setUser("sql555364");
//					dataSource.setPassword("vE8*tS4!");
//					
//					//Gets connection
//					java.sql.Connection con = dataSource.getConnection();
//		
////					//Uses prepared statements to help vs SQLi
//					String statement = "SELECT * FROM UserInfo WHERE name= ? ";
//					java.sql.PreparedStatement ps = con.prepareStatement(statement);
//					ps.setString(1, requestMessage.getUser());
//					ResultSet rs = ps.executeQuery();
////					
////					//Returns a failure message if the user account doesn't exist
//					if(!rs.next()){
//						Message rm = new Message("dne","Server","placeholder");
//						oos.writeObject(rm);
//						rs.close();
//						ps.close();
//						con.close();
//						return;
//					}
////					
//					else{
//						String hash = rs.getString("password");
//						if(hash.equals(requestMessage.getHash())){
//							Message rm = new Message("true","Server","placeholder");
//							oos.writeObject(rm);
//							rs.close();
//							ps.close();
//							con.close();
//							return;
//						}
//						else{
//							Message rm = new Message("wrong pass","Server","placeholder");
//							oos.writeObject(rm);
//							rs.close();
//							ps.close();
//							con.close();
//							return;
//						}
//					}
//				}
//				catch(Exception e){
//					//Returns a message to the app that it couldn't sync
//					Message rm = new Message("false","Server","placeholder");
//					oos.writeObject(rm);
//					return;
//				}	
//			}
//			
////			/*
////			 * Adds a new user account, checks the database for an already existing user
////			 * and if it doesn't exist, inserts the user info into the db
////			 */
//			
//			else if(choice.equals("register")){
//				
//				dataSource.setUser("sql555364");
//				dataSource.setPassword("vE8*tS4!");
//				
//				//Gets connection
//				java.sql.Connection con = dataSource.getConnection();
//	
////				//Uses prepared statements to help vs SQLi
//				String statement = "INSERT INTO UserInfo (name, password) VALUES ( ? , ? )";
//				java.sql.PreparedStatement ps = con.prepareStatement(statement);
//				ps.setString(1, requestMessage.getUser());
//				ps.setString(2, options[1]);
//				ps.execute();
//				
//				Message rm = new Message("registered","Server","place");
//				oos.writeObject(rm);
//				return;
//				
//			}
//			else{
//				Message rm = new Message("false","Server","place");
//				oos.writeObject(rm);
//				return;
//			}
//		}
//		catch(Exception e){
//
//		}
	}
	
	/*
	 * This method generates a fresh hash from the message contents and compares it to
	 * the hash provided by the message, determines whether the whole message was intact
	 * and returns a boolean response
	 */
//	private static boolean verifyMessage(Message m){
//		
//		//Gets appropriate hash info from message
//		String[] hashInfo = m.getMac().split(":");
//		String hash;
//		int iterations = Integer.parseInt(hashInfo[0]);
//		byte[] salt = hashInfo[1].getBytes();
//		String comparable = hashInfo[2];
//		
//		//Stores all message info in arrays
//		char[] msgChars = m.getMessage().toCharArray();
//		char[] usrChars = m.getUser().toCharArray();
//		char[] hashChars = m.getHash().toCharArray();
//		char[] timeChars = String.valueOf(m.getDate().getTime()).toCharArray();
//		char[] allChars = new char[msgChars.length + usrChars.length + hashChars.length + timeChars.length];
//	
//		//Transfers all of these arrays into one array to be hashed
//		System.arraycopy(msgChars, 0, allChars, 0, msgChars.length);
//		System.arraycopy(usrChars, 0, allChars, msgChars.length, usrChars.length);
//		System.arraycopy(hashChars, 0, allChars, msgChars.length + usrChars.length, hashChars.length);
//		System.arraycopy(timeChars, 0, allChars, msgChars.length + usrChars.length + hashChars.length, timeChars.length);
//		
//		//Hashes the information, and compares the result to the expected hash
//		try{
//			PBEKeySpec pbeks = new PBEKeySpec(allChars, salt, iterations, 64*8);
//			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
//			byte[] hashBytes = skf.generateSecret(pbeks).getEncoded();
//			hash = toHex(hashBytes);
//			System.out.println(iterations + ":" + toHex(salt) + ":" + (hash));
//		}
//		catch(Exception e){ return false; }
//		
//		//Return true if the hash is unchanged and false if not
//		if(hash.equals(comparable)) return true;
//		else return false;
//	}
//	
//	//Transforms byte array to a proper length string, includes padding if necessary
//	private static String toHex(byte[] a){
//		BigInteger b = new BigInteger(1,a);
//		String hex = b.toString(16);
//		int paddingLength = (a.length * 2) - hex.length();
//		if(paddingLength > 0){
//			return "0" + hex;
//		}
//		else{
//			return hex;
//		}
//	}