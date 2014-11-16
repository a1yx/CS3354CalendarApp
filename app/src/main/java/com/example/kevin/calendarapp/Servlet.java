import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
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
		else if(array[0].equals("DOWNLOAD")){
			
			try{
				String statement = "SELECT * FROM eventData WHERE userID='" + array[1] + "'";
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(statement);
				
				OutputStream output = response.getOutputStream();
				
				if(!rs.isBeforeFirst()){
					output.write("NONE".getBytes());
					output.close();
					return;
				}
				
				while(rs.next()){
					String send = " " + rs.getString("name") + "," + rs.getString("description") + "," + rs.getString("location") + "," + rs.getString("startTime") +
							"," + rs.getString("endTime") + "," + rs.getString("startDate") + "," + rs.getString("endDate") + "," + rs.getString("category") + "," +
							rs.getString("uuid") + "\n";
					output.write(send.getBytes());
				}
				output.close();
				return;
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
		
}
