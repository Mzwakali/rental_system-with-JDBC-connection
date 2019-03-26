import java.net.*;  
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.*;  

import com.mysql.jdbc.ResultSet;
public class ServerApp{  
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String   DB_URL = "jdbc:mysql://localhost/management";
   //  Database credentials
   static final String USER = "root";
   static final String PASS = "";
   
   static Connection conn = null;
   static Statement stmt = null;
	
	static ServerSocket Serversocket;  
	static Socket Clientsocket;  
	static DataInputStream din;  
	static DataOutputStream dout;  
	
	private static void CreateDatabase(String DB_name)
	   {
		    
		      try {
		    	  String DB_URL = "jdbc:mysql://localhost/";
		   	   
			   	   System.out.println("Connecting to database...");
			   	   conn = DriverManager.getConnection(DB_URL, USER, PASS);
			   	   
			   	   System.out.println("Creating database...");
			   	   stmt = conn.createStatement();
			   	   
			   	   String sql = "CREATE DATABASE " + DB_name;
			   	   stmt.executeUpdate(sql);
			   	   System.out.println("Database created successfully...");
			   	   stmt.close();
			   	   conn.close();
			      
		      } 
		      catch (SQLException e) {
					// TODO Auto-generated catch block
		    	  System.out.println(DB_name + "database already exists");
			  }
		   	
	   }
	   
	   private static void CreateTable(String DB_name, String TBL_name, String column)
	   {
		   
		   String DB_URL = "jdbc:mysql://localhost/" + DB_name;
		   
		   try {
					conn = DriverManager.getConnection(DB_URL, USER, PASS);
					stmt = conn.createStatement();
					String sql = "CREATE TABLE " + TBL_name + column;
			              
					stmt.executeUpdate(sql);
					System.out.println("Created " + TBL_name + "table in " + DB_name + " database...");
		   	} catch (SQLException e) {
				// TODO Auto-generated catch block
		   	 System.out.println(TBL_name + "table already exists");
			}
		  
	   }
	   
	   private static  void prepare_DB()
	   {
			   CreateDatabase("Management");
			   
			   String column = "(custNumber INTEGER not NULL, " +
			              " firstName VARCHAR(255), " + 
			              " surname VARCHAR(255), " +
			              " phoneNum VARCHAR(255), " +
			              " credit DOUBLE, "  + 
			              " canRent BOOL, " +
			              " PRIMARY KEY ( custNumber ))";
		   	  CreateTable("Management","Customer",column);
		   	  
		   	  column = "(dvdNumber INTEGER not NULL, " +
		              " title VARCHAR(255), " + 
		              " category VARCHAR(255), " +
		              " price DOUBLE, "  + 
		              " newRelease BOOL, " +
		              " availableForRent BOOL, " +
		              " PRIMARY KEY ( dvdNumber ))";
		   	  CreateTable("Management","DVD",column);
		   	  
		   	  column = "(rentalNumber INTEGER not NULL, " +
		              " dateRented VARCHAR(255), " + 
		              " dateReturned VARCHAR(255), " +
		              " custNumber INTEGER, "  + 
		              " dvdNumber INTEGER, " +
		              " totalPenaltyCost DOUBLE, " +
		              " PRIMARY KEY ( rentalNumber ))";
		   	  CreateTable("Management","Rental",column);
			   	  
			
	   }
	public static void Add_customer(Customer cust)
	{
		try {
			stmt = conn.createStatement();
		
			String sql = "INSERT INTO Customer " +
	                "VALUES (" + Integer.toString(cust.getCustNumber()) + ", '" + cust.getName() + "' "
	                + ", '" + cust.getSurname() + "' " + ", '" + cust.getPhoneNum() + "' "
	                + ", " + Double.toString(cust.getCredit()) + ", " + cust.canRent() + ")";
	  	  	stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void Add_dvd(DVD dvd)
	{
		try {
			stmt = conn.createStatement();
		
			String sql = "INSERT INTO DVD " +
	                  "VALUES (" + Integer.toString(dvd.getDvdNumber()) + ", '" + dvd.getTitle() + "' "
	                  + ", '" + dvd.getCategory() + "' " 
	                  + ", " + Double.toString(dvd.getPrice()) + ", " + dvd.isNewRelease()
	                  + ", " + dvd.isAvailable()+ ")";
	    	 stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void Add_Rental(Rental rent)
	{
		try {
			stmt = conn.createStatement();
			String sql = "INSERT INTO Rental " +
		            "VALUES (" + Integer.toString(rent.getRentalNumber()) + ", '" + rent.getDateRented() + "' "
		            + ", '" + rent.getDateReturned() + "', " + Integer.toString(rent.getCustNumber()) 
		            + ", " + Integer.toString(rent.getDvdNumber())
		            + ", " + Double.toString(rent.getTotalPenaltyCost()) + ")";
			stmt.executeUpdate(sql);
			  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static int getID(String TableName)
	{
		int rowCount = 0;
		String sql = "select count(*) from " + TableName;
		
		ResultSet rs;
		try {
			rs = (ResultSet)stmt.executeQuery(sql);
		
			rs.next();
	  	  	rowCount = rs.getInt(1);
	  	  	
			return rowCount;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error in executing query");
		}
		return -1;
		
	}
	
	private static void sendAllTableData(String TableName)
	{
		String sql = "select * from " + TableName;
		
		ResultSet rs;
		try {
			rs = (ResultSet)stmt.executeQuery(sql);
		
			while(rs.next() == true)
			{
				if(TableName.equals("Customer"))
				{
					int custid = rs.getInt(1);
					String Fn = rs.getString(2);
					String Sn = rs.getString(3);
					String Pn = rs.getString(4);
					Double Cr = rs.getDouble(5);
					Boolean canRent = rs.getBoolean(6);
					
					Customer cust = new Customer(custid, Fn, Sn, Pn, Cr, canRent);
					
					String cust_str = "";
					ByteArrayOutputStream bo = new ByteArrayOutputStream();
				    ObjectOutputStream so;
				    so = new ObjectOutputStream(bo);
				    so.writeObject(cust);
				    so.flush();
				    cust_str = bo.toString();
					dout.writeUTF(cust_str); 
					dout.flush();
				}
				
				if(TableName.equals("DVD"))
				{
					int dvdid = rs.getInt(1);
					String title = rs.getString(2);
					String categorystr = rs.getString(3);
					Double price = rs.getDouble(4);
					Boolean newRealse = rs.getBoolean(5);
					Boolean canRent = rs.getBoolean(6);
					int category = 1;
					if(categorystr.equals("horror"))
					{
						category = 1;
					}
					if(categorystr.equals("Sci-fi"))
					{
						category = 2;
					}
					if(categorystr.equals("Drama"))
					{
						category = 3;
					}
					if(categorystr.equals("Romance"))
					{
						category = 4;
					}
					if(categorystr.equals("Comedy"))
					{
						category = 5;
					}
					if(categorystr.equals("Action"))
					{
						category = 6;
					}
					if(categorystr.equals("Cartoon"))
					{
						category = 7;
					}
					DVD dvd = new DVD(dvdid, title, category, newRealse, canRent);
					
					String dvd_str = "";
					ByteArrayOutputStream bo = new ByteArrayOutputStream();
				    ObjectOutputStream so;
				    so = new ObjectOutputStream(bo);
				    so.writeObject(dvd);
				    so.flush();
				    dvd_str = bo.toString();
					dout.writeUTF(dvd_str); 
					dout.flush();
				}
				
				if(TableName.equals("Rental"))
				{
					int rentid = rs.getInt(1);
					String dRen = rs.getString(2);
					String dRet = rs.getString(3);
					int cN = rs.getInt(4);
					int dN = rs.getInt(5);
					Double pC = rs.getDouble(6);
					
					Rental rent = new Rental(rentid, dRen, dRet, cN, dN);
					
					String rent_str = "";
					ByteArrayOutputStream bo = new ByteArrayOutputStream();
				    ObjectOutputStream so;
				    so = new ObjectOutputStream(bo);
				    so.writeObject(rent);
				    so.flush();
				    rent_str = bo.toString();
					dout.writeUTF(rent_str); 
					dout.flush();
				}
				
			}
			dout.writeUTF("end"); 
			dout.flush();
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error in executing query");
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static int canRent(int custN, int dvdN)
	{
		int canrent = 0;
		
		try {
			String sql = "select canRent from Customer where custNumber = " + String.valueOf(custN);
			ResultSet rs = (ResultSet)stmt.executeQuery(sql);
			Boolean canRent_cust = false;
			if(rs.next() != false)
			{
				canRent_cust = rs.getBoolean(1);
			}
			
			
			
			sql = "select availableForRent from DVD where dvdNumber = " + String.valueOf(dvdN);
			rs = (ResultSet)stmt.executeQuery(sql);
			Boolean canRent_dvd = false;
			if(rs.next() != false)
			{
				canRent_dvd = rs.getBoolean(1);
			}
			
			
			sql = "select price from DVD where dvdNumber = " + String.valueOf(dvdN);
			rs = (ResultSet)stmt.executeQuery(sql);
			Double dvd_price = 0.0;
			if(rs.next() != false)
			{
				dvd_price = rs.getDouble(1);
			}
			
			sql = "select credit from Customer where custNumber = " + String.valueOf(custN);
			rs = (ResultSet)stmt.executeQuery(sql);
			Double cust_credit = 0.0;
			if(rs.next() != false)
			{
				cust_credit = rs.getDouble(1);
			}
			
			if(cust_credit < dvd_price)
			{
				canrent = 2;
			}
			
			if((canRent_cust == true)&&(canRent_dvd == true)&&(cust_credit >= dvd_price))
			{
				canrent = 1;
				
				sql = "UPDATE Customer SET canRent = false WHERE custNumber = " + String.valueOf(custN);
				stmt.executeUpdate(sql);
				
				sql = "UPDATE DVD SET availableForRent = false WHERE dvdNumber = " + String.valueOf(dvdN);
				stmt.executeUpdate(sql);
				double newcredit = cust_credit - dvd_price;
				sql = "UPDATE Customer SET credit = " + Double.toString(newcredit) + " WHERE custNumber = " + String.valueOf(custN);
				stmt.executeUpdate(sql);
			} 
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return canrent;
	}
	
	private static Boolean canReturn(int custN, int dvdN, String returnDate)
	{
		Boolean canreturn = false;
		
		try {
			String sql = "select canRent from Customer where custNumber = " + String.valueOf(custN);
			ResultSet rs = (ResultSet)stmt.executeQuery(sql);
			Boolean canRent_cust = true;
			if(rs.next() != false)
			{
				canRent_cust = rs.getBoolean(1);
			}
			 
			
			sql = "select availableForRent from DVD where dvdNumber = " + String.valueOf(dvdN);
			rs = (ResultSet)stmt.executeQuery(sql);
			Boolean canRent_dvd  = true;
			if(rs.next() != false)
			{
				canRent_dvd = rs.getBoolean(1);
			}
			
			sql = "select rentalNumber, dateRented from Rental where dvdNumber = " + String.valueOf(dvdN) + " AND custNumber = "+ String.valueOf(custN);
			rs = (ResultSet)stmt.executeQuery(sql);
			String dateRented ;
			int rentalnum;
			if(rs.next() != false)
			{
				rentalnum = rs.getInt(1);
				dateRented = rs.getString(2);
			}
			else
			{
				return false;
			}
			
			sql = "select credit from Customer where custNumber = " + String.valueOf(custN);
			rs = (ResultSet)stmt.executeQuery(sql);
			Double cust_credit = 0.0;
			if(rs.next() != false)
			{
				cust_credit = rs.getDouble(1);
			}
            //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
            //LocalDateTime now = LocalDateTime.now();  
            //String curTime = dtf.format(now);
			Rental obj = new Rental();
			obj.setDateRented(dateRented);
			obj.setDateReturned(returnDate);
			if((canRent_cust == false)&&(canRent_dvd == false))
			{
				canreturn = true;
				
				sql = "UPDATE Customer SET canRent = true WHERE custNumber = " + String.valueOf(custN);
				stmt.executeUpdate(sql);
				
				sql = "UPDATE DVD SET availableForRent = true WHERE dvdNumber = " + String.valueOf(dvdN);
				stmt.executeUpdate(sql);
				System.out.println(obj.getDateReturned());
				sql = "UPDATE Rental SET dateReturned = '" + obj.getDateReturned() + "' , totalPenaltyCost = " +
						Double.toString(obj.getTotalPenaltyCost()) + " WHERE custNumber = " + String.valueOf(custN) + " AND dvdNumber = " + String.valueOf(dvdN);
				stmt.executeUpdate(sql);
				
				if(obj.getTotalPenaltyCost() > 0)
				{
					double newCredit = 0.0;
					if(cust_credit > obj.getTotalPenaltyCost())
					{
						 newCredit = cust_credit - obj.getTotalPenaltyCost();
					}
					else
					{
						newCredit = 0.0;
					}
					sql = "UPDATE Customer SET credit = " + Double.toString(newCredit) + " WHERE custNumber = " + String.valueOf(custN);
					stmt.executeUpdate(sql);
				}
			} 
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return canreturn;
	}
	
	public static String Handle_request(String Rstr)
	{
		String rstr;
		try {
			
			System.out.println("client says: "+Rstr);  
			
			if(Rstr.equals("1") == true) // adding customer
			{
				dout.writeUTF("ok");  
				dout.flush(); 
				
				rstr=din.readUTF();  
				// deserialize
			    byte b[] = rstr.getBytes(); 
			    ByteArrayInputStream bi = new ByteArrayInputStream(b);
			    ObjectInputStream si = new ObjectInputStream(bi);
			    
				Customer obj = (Customer) si.readObject();
				int id = getID("Customer");
				if(id != -1)
				{
					obj.setCustNumber(id);
				}
				
				Add_customer(obj);
				
				System.out.println(obj.getName() + "is added successfully to customer table");
				dout.writeUTF("ok");  
				dout.flush();
				sendAllTableData("Customer");
				
				 
			}
			if(Rstr.equals("2") == true) // adding DVD
			{
				dout.writeUTF("ok");  
				dout.flush(); 
				
				rstr=din.readUTF();  
				// deserialize
			    byte b[] = rstr.getBytes(); 
			    ByteArrayInputStream bi = new ByteArrayInputStream(b);
			    ObjectInputStream si = new ObjectInputStream(bi);
			    
				DVD obj = (DVD) si.readObject();
				int id = getID("DVD");
				if(id != -1)
				{
					obj.setDvdNumber(id);
				}
				Add_dvd(obj);
				System.out.println(obj.getTitle() + "is added successfully to DVD table");
				dout.writeUTF("ok");  
				dout.flush(); 
				sendAllTableData("DVD");
			}
			
			if(Rstr.equals("3") == true) // rent
			{
				dout.writeUTF("ok");  
				dout.flush(); 
				
				rstr=din.readUTF();  
				// deserialize
			    byte b[] = rstr.getBytes(); 
			    ByteArrayInputStream bi = new ByteArrayInputStream(b);
			    ObjectInputStream si = new ObjectInputStream(bi);
			    
				Rental obj = (Rental) si.readObject();
				
				int ret_value = canRent(obj.getCustNumber(), obj.getDvdNumber());
				if(ret_value == 1)
				{
					dout.writeUTF("ok");  
					dout.flush();
					
					int id = getID("Rental");
					obj.setRentalNumber(id);
					Add_Rental(obj);
					
					System.out.println("A rent is added successfully to Rental table");
					
					
					sendAllTableData("Customer");
					sendAllTableData("DVD");
					
				}
				else
				{
					if(ret_value == 2)
					{
						dout.writeUTF("no_price");  
						dout.flush(); 
					}
					else{
						dout.writeUTF("no");  
						dout.flush(); 
					}
					
				}
				
			}
			
			if(Rstr.equals("4") == true) // return
			{
				dout.writeUTF("ok");  
				dout.flush(); 
				
				rstr = din.readUTF();  
				Integer cusID = Integer.valueOf(rstr);
				dout.writeUTF("ok");  
				dout.flush(); 
				
				rstr = din.readUTF();  
				Integer dvdID = Integer.valueOf(rstr);
				dout.writeUTF("ok");  
				dout.flush(); 
				
				rstr = din.readUTF();  
				String returnDate = rstr;
				dout.writeUTF("ok");  
				dout.flush(); 
			    
				Boolean isOk = canReturn(cusID, dvdID, returnDate);
				if(isOk == true)
				{
					dout.writeUTF("ok");  
					dout.flush();
					sendAllTableData("Customer");
					sendAllTableData("DVD");
					System.out.println("DVD is Returned successfully");
					
				}
				else
				{
					dout.writeUTF("no");  
					dout.flush(); 
				}
				
			}
			if(Rstr.equals("5") == true) // send customer table
			{
				dout.writeUTF("ok");  
				dout.flush(); 
				sendAllTableData("Customer");
				
			}
			if(Rstr.equals("6") == true) // send dvd table
			{
				dout.writeUTF("ok");  
				dout.flush(); 
				sendAllTableData("DVD");
				
			}
			if(Rstr.equals("7") == true) // send rental table
			{
				dout.writeUTF("ok");  
				dout.flush(); 
				sendAllTableData("Rental");
			}
			
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public static void main(String args[]){  
		try{
			Class.forName(JDBC_DRIVER);
			prepare_DB();
			
			Serversocket = new ServerSocket(3333);  
			
			System.out.println("Listening...");
			
			Clientsocket = Serversocket.accept();  
			din=new DataInputStream(Clientsocket.getInputStream());  
			dout=new DataOutputStream(Clientsocket.getOutputStream()); 
			
			 
			
			String Rstr="",str2="";  
			while(!Rstr.equals("stop")){  
				Rstr = din.readUTF();
				Handle_request(Rstr);
			}  
			
			
			din.close();  
			dout.close();
			Clientsocket.close();  
			Serversocket.close();  
			
			conn.close();
			stmt.close();
		}catch(Exception e){
			System.out.println(e);
		}
	
	}
}  