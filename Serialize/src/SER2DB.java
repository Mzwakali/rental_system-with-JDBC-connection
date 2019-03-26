import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.sql.*;

public class SER2DB {
   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
  
   //  Database credentials
   static final String USER = "root";
   static final String PASS = "";
   
   static Connection conn = null;
   static Statement stmt = null;
   
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
   
   public static void main(String[] args) {
   
  
      //STEP 2: Register JDBC driver
      try {
		
    	   Class.forName("com.mysql.jdbc.Driver");
    	   CreateDatabase("Manage");
    	   
    	  
    	   Customer cust = null;
	   	   DVD movie = null;
	   	   Rental rent = null;
	   	    
		    ObjectInputStream in = new ObjectInputStream(new FileInputStream("Customers.ser"));
		    cust = (Customer) in.readObject(); 
		    in.close();
		    
		    ObjectInputStream in1 = new ObjectInputStream(new FileInputStream("Movies.ser"));
		    movie = (DVD) in1.readObject(); 
		    in1.close();
		    
		    ObjectInputStream in2 = new ObjectInputStream(new FileInputStream("rental.ser"));
		    rent = (Rental) in2.readObject(); 
		    in2.close();
	   	   
   	   
	   	  String column = "(custNumber INTEGER not NULL, " +
	              " firstName VARCHAR(255), " + 
	              " surname VARCHAR(255), " +
	              " phoneNum VARCHAR(255), " +
	              " credit DOUBLE, "  + 
	              " canRent BOOL, " +
	              " PRIMARY KEY ( custNumber ))";
	   	  CreateTable("Manage","myCustomer",column);
	   	  
	   	  column = "(dvdNumber INTEGER not NULL, " +
	              " title VARCHAR(255), " + 
	              " category VARCHAR(255), " +
	              " price DOUBLE, "  + 
	              " newRelease BOOL, " +
	              " availableForRent BOOL, " +
	              " PRIMARY KEY ( dvdNumber ))";
	   	  CreateTable("Manage","myDVD",column);
	   	  
	   	  column = "(rentalNumber INTEGER not NULL, " +
	              " dateRented VARCHAR(255), " + 
	              " dateReturned VARCHAR(255), " +
	              " custNumber INTEGER, "  + 
	              " dvdNumber INTEGER, " +
	              " totalPenaltyCost DOUBLE, " +
	              " PRIMARY KEY ( rentalNumber ))";
	   	  CreateTable("Manage","myRental",column);
	   	  
    	  String sql = "INSERT INTO myCustomer " +
                  "VALUES (" + Integer.toString(cust.getCustNumber()) + ", '" + cust.getName() + "' "
                  + ", '" + cust.getSurname() + "' " + ", '" + cust.getPhoneNum() + "' "
                  + ", " + Double.toString(cust.getCredit()) + ", " + cust.canRent() + ")";
    	  stmt.executeUpdate(sql);
    	  
    	  sql = "INSERT INTO myDVD " +
                  "VALUES (" + Integer.toString(movie.getDvdNumber()) + ", '" + movie.getTitle() + "' "
                  + ", '" + movie.getCategory() + "' " 
                  + ", " + Double.toString(movie.getPrice()) + ", " + movie.isNewRelease()
                  + ", " + movie.isAvailable()+ ")";
    	  stmt.executeUpdate(sql);
    	  
    	  sql = "INSERT INTO myRental " +
                  "VALUES (" + Integer.toString(rent.getRentalNumber()) + ", '" + rent.getDateRented() + "' "
                  + ", '" + rent.getDateReturned() + "', " + Integer.toString(rent.getCustNumber()) 
                  + ", " + Integer.toString(rent.getDvdNumber())
                  + ", " + Double.toString(rent.getTotalPenaltyCost()) + ")";
    	  stmt.executeUpdate(sql);
    	   
    	  System.out.println("All data is loaded in tables");
    	  
    	  
    	  ResultSet  rs = stmt.executeQuery("SELECT COUNT(*) FROM myCustomer");
    	  // get the number of rows from the result set
    	  rs.next();
    	  int rowCount = rs.getInt(1);
    	  System.out.println("Records count in Customer table");
    	  System.out.println(rowCount);
    	  
    	  rs = stmt.executeQuery("SELECT COUNT(*) FROM myDVD");
    	  rs.next();
    	  rowCount = rs.getInt(1);
    	  System.out.println("Records count in DVD table");
    	  System.out.println(rowCount);
    	  
    	  rs = stmt.executeQuery("SELECT COUNT(*) FROM myRental");
    	  rs.next();
    	  rowCount = rs.getInt(1);
    	  System.out.println("Records count in Rental table");
    	  System.out.println(rowCount);

    	  rs.close();
    	  stmt.close();
	      conn.close();
		
		
      } 
      catch (SQLException e) {
			// TODO Auto-generated catch block
    	  System.out.println("Error!");
      }
      catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	  }
      catch(Exception e) {}
  
   }//end main
   
}//end JDBCExample