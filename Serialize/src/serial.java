
import java.io.*;
import java.util.ArrayList;
public class serial {


    
   public static void main(String [] args) {
	  demoClass e = new demoClass();
      e.name = "0";
      e.address = "AJ Thando, Cape Town";
      e.SSN = 11122333;
      e.number = 101;
      
      demoClass e1 = new demoClass();
      e1.name = "1";
      e1.address = "dddddddd";
      e1.SSN = 333;
      e1.number = 22;
      
      demoClass e2 = new demoClass();
      e2.name = "2";
      e2.address = "dddddddd";
      e2.SSN = 333;
      e2.number = 22;
      
      ArrayList<demoClass> n = new ArrayList<demoClass>();
      n.add(e);
      n.add(e1);
      n.add(e2);
      try {
    	  
    	  FileOutputStream fileOut = new FileOutputStream("employee.ser");
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(e);
	        out.close();
	        fileOut.close();
         
         System.out.println("Serialized data is saved in employee.ser");
      } catch (IOException i) {
         i.printStackTrace();
      }
   }
}