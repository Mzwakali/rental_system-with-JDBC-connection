
import java.io.*;
import java.util.ArrayList;
public class deserial {

   public static void main(String [] args) {
	   ArrayList<Customer> plants = null;
	   Customer plant = null;
	   DVD movie = null;
	   Rental rent = null;
	    try {
	        ObjectInputStream in = new ObjectInputStream(new FileInputStream("Customers.ser"));
	        plant = (Customer) in.readObject(); 
	        in.close();
	        
	        ObjectInputStream in1 = new ObjectInputStream(new FileInputStream("Movies.ser"));
	        movie = (DVD) in1.readObject(); 
	        in1.close();
	        
	        ObjectInputStream in2 = new ObjectInputStream(new FileInputStream("rental.ser"));
	        rent = (Rental) in2.readObject(); 
	        in2.close();
	    }
	    catch(Exception e) {}
	   
	
		  System.out.println("Deserialized customer...");
	      System.out.println("Customer number: " + Integer.toString(plant.getCustNumber()));
	      System.out.println("firstName: " +plant.getName());
	      System.out.println("surname: " +plant.getSurname());
	      System.out.println("phoneNum: " + plant.getPhoneNum());
	      
	      System.out.println("Deserialized movie...");
	      System.out.println("dvdNumber: " + Integer.toString(movie.getDvdNumber()));
	      System.out.println("title: " +movie.getTitle());
	      System.out.println("category: " +movie.getCategory());
	      
	      System.out.println("Deserialized rent...");
	      System.out.println("rentalNumber: " + Integer.toString(rent.getRentalNumber()));
	      System.out.println("dateRented: " +rent.getDateRented());
	      System.out.println("dateReturned: " +rent.getDateReturned());
	      System.out.print(movie.isAvailable());
	      Boolean s = movie.isAvailable();
	      System.out.print(Boolean.toString(s));
	      
   }
}