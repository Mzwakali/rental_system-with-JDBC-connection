
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.*;  
import javax.swing.table.DefaultTableModel;

public class ClientApp implements ActionListener{  
	JFrame f;  
	JButton Btn_Cus;
	JButton Btn_Dvd;
	JButton Btn_Rent;
	JButton Btn_Return;
	JButton Btn_Report;
	static JTable TBL_CUS;
	static JTable TBL_MOVIE;
	JTextField TF_Search;  
	JTextField TF_CustNum;  
	JTextField TF_DvdNum;  
	static ArrayList<DVD> DVDlist;
	JComboBox categ;
	
	ClientApp()
	{
		 myclient.connect();
		 
		f=new JFrame();  
		 DVDlist = new ArrayList<DVD>();
	     Btn_Cus = new JButton("Add a new customer");  
	     Btn_Dvd = new JButton("Add a new DVD");  
	     Btn_Rent = new JButton("Rent a DVD");  
	     Btn_Return = new JButton("Return a DVD"); 
	     Btn_Report = new JButton("Report");
	     
	    JLabel LB_cus, LB_movie, LB_custNum, LB_dvdNum, LB_category;  
	    
	    LB_custNum = new JLabel("Customer Number :");  
	    LB_custNum.setBounds(100,30,110,40);
	    LB_dvdNum = new JLabel("DVD Number :");  
	    LB_dvdNum.setBounds(330,30,90,40);
	    f.add(LB_custNum);
	    f.add(LB_dvdNum);
	    TF_CustNum = new JTextField("");  
	    TF_CustNum.setBounds(235,30,60,40);
	    TF_CustNum.setToolTipText("Input movie name to search");
	    
	    TF_DvdNum = new JTextField("");  
	    TF_DvdNum.setBounds(425,30,60,40);
	    TF_DvdNum.setToolTipText("Input movie name to search");
	    f.add(TF_CustNum);
	    f.add(TF_DvdNum);
	    Btn_Cus.setBounds(180,530,160,40);
	    Btn_Dvd.setBounds(700,530,130,40);
	    
	    Btn_Rent.setBounds(530,30,130,40);
	    Btn_Return.setBounds(680,30,130,40);
	    Btn_Report.setBounds(830,30,130,40);
	    f.add(Btn_Report);
	    Btn_Cus.addActionListener(this);
	    Btn_Dvd.addActionListener(this);
	    Btn_Rent.addActionListener(this);
	    Btn_Return.addActionListener(this);
	    Btn_Report.addActionListener(this);
	    
	    LB_cus = new JLabel("Registered all customers");  
	    LB_cus.setBounds(200,80, 150,30);
	    LB_movie = new JLabel("Registered all Movies");  
	    LB_movie.setBounds(550,130, 150,30);
	    
	    
	    LB_category = new JLabel("category :");  
	    LB_category.setBounds(550,90, 100,30);
	    
	    String categoryItems[]={"horror","Sci-fi","Drama","Romance","Comedy","Action","Cartoon"};        
	    categ = new JComboBox(categoryItems);  
	    categ.addActionListener(this);
	    categ.setBounds(680,90, 100,30);
	    f.add(LB_category);
	    f.add(categ);
	    
	    LB_category.setBounds(580,90, 100,30);
	    TF_Search = new JTextField("");  
	    TF_Search.setBounds(680,130,250,25);
	    TF_Search.setToolTipText("Input movie name to search");
	    TF_Search.addKeyListener(new KeyAdapter()
	    {
	        public void keyPressed(KeyEvent ke)
	        {
	            if(!(ke.getKeyChar()==27||ke.getKeyChar()==65535))//this section will execute only when user is editing the JTextField
	            {
	            	JTextField a = (JTextField) ke.getSource();
	            	String movie_title = a.getText();
	            	
	            	String Dvdcategory = (String)categ.getSelectedItem(); 
	            	int keychar = (int)ke.getKeyChar();
	            	 if((keychar != 8) && (keychar != 10) && (keychar != 127)) /// backspace, enter, delete
	            	 {
	            		 movie_title = movie_title + String.valueOf(ke.getKeyChar());
	            	 }
	            	
	            	ArrayList<DVD> tmpDVDlist = new ArrayList<DVD>();
	            	for(int i = 0; i < DVDlist.size(); i ++)
	            	{
	            		if(((DVDlist.get(i).getTitle().contains(movie_title) == true) || (movie_title.equals("") == true))&&(DVDlist.get(i).getCategory().equals(Dvdcategory) == true))
	            		{
	            			tmpDVDlist.add(DVDlist.get(i));
	            		}
	            	}
	            	UpdateDvdTable(tmpDVDlist);
	            	
	                //System.out.println(movie_title);
	            }
	        }
	    });
	    
	    
		
		TBL_CUS = new JTable();    
		TBL_CUS.setBounds(60,110,400,350);      
		TBL_CUS.setEnabled(false);
		
		TBL_MOVIE = new JTable();    
		TBL_MOVIE.setBounds(550,160,400,350);      
		TBL_MOVIE.setEnabled(false);
		
		JScrollPane ScP_CUS = new JScrollPane(TBL_CUS);  
		JScrollPane ScP_MOVIE = new JScrollPane(TBL_MOVIE);  
		
		ScP_CUS.setBounds(60,110,400,400);
		ScP_MOVIE.setBounds(550,160,400,350);
		
	    f.add(Btn_Cus);   
	    f.add(Btn_Dvd);
	    f.add(Btn_Rent);
	    f.add(Btn_Return);
	    
	    f.add(LB_cus);
	    f.add(LB_movie);
	    f.add(ScP_CUS);
	    f.add(ScP_MOVIE);
	    
	    f.add(TF_Search);
	    
	    
	      
        f.setLayout(null);  
        //setting grid layout of 3 rows and 3 columns  
      
        f.setSize(1000,620);  
        f.setVisible(true);  
        f.setLocationRelativeTo(null);
        f.setTitle("Management");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        InitTableView();
	}
	
	private static void InitTableView()
	{
		 
		 try {
			 String r_str = myclient.Send_Receive("5"); // send all customer table data
			 ArrayList<Customer> Customer_list = new ArrayList<Customer>();
		     if(r_str.equals("ok"))
		     {
		    	 while(r_str.equals("end") != true)
		    	 {
		    		 r_str = myclient._Receive();
		    		 if(r_str.equals("end") != true)
		    		 {
		    			// deserialize
					     byte b[] = r_str.getBytes(); 
					     ByteArrayInputStream bi = new ByteArrayInputStream(b);
					     ObjectInputStream si = new ObjectInputStream(bi);
						
					     Customer custobj = (Customer) si.readObject();
					     Customer_list.add(custobj);
					     //System.out.println(custobj.toString());
		    		 }
		    		 
		    	 }
		    	 
		     }
		     ClientApp.UpdateCustTable(Customer_list);
		     
		     r_str = myclient.Send_Receive("6"); // send all dvd table data
			 ArrayList<DVD> dvd_list = new ArrayList<DVD>();
		     if(r_str.equals("ok"))
		     {
		    	 while(r_str.equals("end") != true)
		    	 {
		    		 r_str = myclient._Receive();
		    		 if(r_str.equals("end") != true)
		    		 {
		    			// deserialize
					     byte b[] = r_str.getBytes(); 
					     ByteArrayInputStream bi = new ByteArrayInputStream(b);
					     ObjectInputStream si = new ObjectInputStream(bi);
						
					     DVD dvdobj = (DVD) si.readObject();
					     dvd_list.add(dvdobj);
					     //System.out.println(custobj.toString());
		    		 }
		    		 
		    	 }
		     }
		     
		     ClientApp.UpdateDvdTable(dvd_list);
		     ClientApp.DVDlist = dvd_list;
	     } 
		 catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		 }
		 catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	
	public static void UpdateCustTable(ArrayList<Customer> custList)
	{
		DefaultTableModel dtm = new DefaultTableModel(0, 0);
		String header[] = new String[] { "CustomerNumber", "Firstname", "Surname",
		           "PhoneNumber", "Credit", "CanRent" };

		//add header in table model     
		dtm.setColumnIdentifiers(header);
		//set model into the table object
		TBL_CUS.setModel(dtm);
		
		// add row dynamically into the table      
		for (int count = 0; count < custList.size(); count++) {
			String cusN = Integer.toString(custList.get(count).getCustNumber());
			String fN = custList.get(count).getName();
			String sN = custList.get(count).getSurname();
			String pN = custList.get(count).getPhoneNum();
			String cr = Double.toString(custList.get(count).getCredit());
			String canr = Boolean.toString(custList.get(count).canRent());
		    
			dtm.addRow(new Object[] { cusN, fN, sN, pN, cr, canr });
		}
		
	}
	
	
	public static void UpdateDvdTable(ArrayList<DVD> dvdList)
	{
		
		DefaultTableModel dtm_dvd = new DefaultTableModel(0, 0);
		String header[] = new String[] { "dvdNumber", "title", "category",
		           "price", "newRelease", "availableForRent" };

		
		//add header in table model     
		dtm_dvd.setColumnIdentifiers(header);
		//set model into the table object
		TBL_MOVIE.setModel(dtm_dvd);
		
		// add row dynamically into the table      
		for (int count = 0; count < dvdList.size(); count++) {
			String dvdN = Integer.toString(dvdList.get(count).getDvdNumber());
			String title = dvdList.get(count).getTitle();
			String category = dvdList.get(count).getCategory();
			Double price = dvdList.get(count).getPrice();
			Boolean newRelease = dvdList.get(count).isNewRelease();
			Boolean canrent = dvdList.get(count).isAvailable();
		    
			dtm_dvd.addRow(new Object[] { dvdN, title, category, Double.toString(price), Boolean.toString(newRelease), Boolean.toString(canrent) });
		}
		
	}
	public  String  ShowInputDialog() {  
		String returnDate = JOptionPane.showInputDialog(f,"Input Date to return (example: 2018/09/20):");
		return returnDate;
	}
	public static void main(String[] args) {  
		new ClientApp();
	}
	
	public  void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource().equals(categ) == true)
		{
			
        	String movie_title = TF_Search.getText();
        	
        	String Dvdcategory = (String)categ.getSelectedItem(); 
        	
        	ArrayList<DVD> tmpDVDlist = new ArrayList<DVD>();
        	for(int i = 0; i < DVDlist.size(); i ++)
        	{
        		if(((DVDlist.get(i).getTitle().contains(movie_title) == true) || (movie_title.equals("") == true))&&(DVDlist.get(i).getCategory().equals(Dvdcategory) == true))
        		{
        			tmpDVDlist.add(DVDlist.get(i));
        		}
        	}
        	UpdateDvdTable(tmpDVDlist);
		}
		
		if(e.getSource().equals(Btn_Cus) == true )
		{
			new Customer_ADD_Frame();
		}
		if(e.getSource().equals(Btn_Dvd) == true )
		{
			new DVD_ADD_Frame();
		}
		if(e.getSource().equals(Btn_Report) == true )
		{
			
			new Report_Rental();
		}
		
		if(e.getSource().equals(Btn_Rent) == true )
		{
			try {
				Integer Cust = Integer.valueOf(TF_CustNum.getText());
				Integer Dvd = Integer.valueOf(TF_DvdNum.getText());
				
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
	            LocalDateTime now = LocalDateTime.now();  
	            String curTime = dtf.format(now);
	            
				Rental rent = new Rental (0, curTime, Cust, Dvd);
				
				ByteArrayOutputStream bo = new ByteArrayOutputStream();
			    ObjectOutputStream so = new ObjectOutputStream(bo);
			    so.writeObject(rent);
			    so.flush();
			    String serializedObject = bo.toString();
			     
			    
			    String r_str = myclient.Send_Receive("3"); // rent
			    if(r_str.equals("ok"))
			    {
			    	r_str = myclient.Send_Receive(serializedObject);
			    }
			    
			     ArrayList<Customer> Customer_list = new ArrayList<Customer>();
			     ArrayList<DVD> dvd_list = new ArrayList<DVD>();
			     
			     if(r_str.equals("ok") == true)
			     {
			    	 while(r_str.equals("end") != true)
			    	 {
			    		 r_str = myclient._Receive();
			    		 if(r_str.equals("end") != true)
			    		 {
			    			// deserialize
						     byte b[] = r_str.getBytes(); 
						     ByteArrayInputStream bi = new ByteArrayInputStream(b);
						     ObjectInputStream si = new ObjectInputStream(bi);
							
						     Customer custobj = (Customer) si.readObject();
							
						     Customer_list.add(custobj);
						     //System.out.println(custobj.toString());
			    		 }
			    		 
			    	 }
			    	 
			    	 ClientApp.UpdateCustTable(Customer_list);
			    	 
			    	
			    	 r_str = "ok";
			    	 while(r_str.equals("end") != true)
			    	 {
			    		 r_str = myclient._Receive();
			    		 if(r_str.equals("end") != true)
			    		 {
			    			// deserialize
						     byte b[] = r_str.getBytes(); 
						     ByteArrayInputStream bi = new ByteArrayInputStream(b);
						     ObjectInputStream si = new ObjectInputStream(bi);
						     DVD dvdobj = (DVD) si.readObject();
						     
						     dvd_list.add(dvdobj);
						     //System.out.println(custobj.toString());
			    		 }
			    		 
			    	 }
			    	 ClientApp.UpdateDvdTable(dvd_list);
				     ClientApp.DVDlist = dvd_list;
				     
				    
				    JOptionPane.showMessageDialog(null, "Rent operation is successed!","Message", 0);  
				  
			     }
			     else{
			    	 if(r_str.equals("no_price") == true)
			    	 {
			    		 JOptionPane.showMessageDialog(null, "Customer's Credit is less than DVD Price! Cant rent!","Message", 0);
			    	 }
			    	 else{
			    		 JOptionPane.showMessageDialog(null, "Rent operation is failed!","Message", 0); 
			    	 }
			    	  
			     }
			     
			   
			   
			  
		     
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catch(NumberFormatException exp)
			{
				JOptionPane.showConfirmDialog(f, "Customer number and dvd number is not inputed properly! Please input again!");
			}
			catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		if(e.getSource().equals(Btn_Return) == true )
		{
			try{
			
			
				Integer Cust = Integer.valueOf(TF_CustNum.getText());
				Integer Dvd = Integer.valueOf(TF_DvdNum.getText());
				
				String returnDate = ShowInputDialog();
				if(returnDate == null)
				{
					System.out.println("dddddd");
					
				}else{
					String r_str = myclient.Send_Receive("4"); // rent
				    
				    if(r_str.equals("ok"))
				    {
				    	r_str = myclient.Send_Receive(Cust.toString()); // cust id
				    }
				    if(r_str.equals("ok"))
				    {
				    	r_str = myclient.Send_Receive(Dvd.toString()); // dvd id
				    }
				    if(r_str.equals("ok"))
				    {
				    	r_str = myclient.Send_Receive(returnDate); // return date
				    }
	
				     ArrayList<Customer> Customer_list = new ArrayList<Customer>();
				     ArrayList<DVD> dvd_list = new ArrayList<DVD>();
				     
				     r_str = myclient._Receive();
				     if(r_str.equals("ok"))
				     {
				    	 while(r_str.equals("end") != true)
				    	 {
				    		 r_str = myclient._Receive();
				    		 if(r_str.equals("end") != true)
				    		 {
				    			// deserialize
							     byte b[] = r_str.getBytes(); 
							     ByteArrayInputStream bi = new ByteArrayInputStream(b);
							     ObjectInputStream si = new ObjectInputStream(bi);
								
							     Customer custobj = (Customer) si.readObject();
								
							     Customer_list.add(custobj);
							     //System.out.println(custobj.toString());
				    		 }
				    		 
				    	 }
				    	 
				    	 ClientApp.UpdateCustTable(Customer_list);
				    	 
				    	
				    	 r_str = "ok";
				    	 while(r_str.equals("end") != true)
				    	 {
				    		 r_str = myclient._Receive();
				    		 if(r_str.equals("end") != true)
				    		 {
				    			// deserialize
							     byte b[] = r_str.getBytes(); 
							     ByteArrayInputStream bi = new ByteArrayInputStream(b);
							     ObjectInputStream si = new ObjectInputStream(bi);
							     DVD dvdobj = (DVD) si.readObject();
							     
							     dvd_list.add(dvdobj);
							     //System.out.println(custobj.toString());
				    		 }
				    		 
				    	 }
				    	 ClientApp.UpdateDvdTable(dvd_list);
					     ClientApp.DVDlist = dvd_list;
					     
					     JOptionPane.showMessageDialog(null, "Return operation is successed!","Message", 0);  
					  
				     }
				     else{
				    	 JOptionPane.showMessageDialog(null, "Return operation is failed!","Message", 0);
				     }
				     
				}
				
			    
			}
			catch(NumberFormatException exp)
			{
				JOptionPane.showConfirmDialog(f, "Customer number and dvd number is not inputed properly! Please input again!");
			}
			catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
		
		
	}  
}  

class Customer_ADD_Frame extends JFrame implements ActionListener{
	private JButton Btn_ok;
	private JButton Btn_close;
	JTextField FirstName;  
	JTextField SurName;
    JTextField PhoneNumber;  
    JTextField Credit;
	Customer_ADD_Frame()
	{
		JLabel LbFN = new JLabel("First Name : ");
		JLabel LbSN = new JLabel("SurName : ");
		JLabel LbPN = new JLabel("Phone Number : ");
		JLabel LbCredit = new JLabel("Credit : ");
		
		 FirstName = new JTextField("");  
		 SurName = new JTextField("");
	     PhoneNumber = new JTextField("");  
	     Credit = new JTextField("");  
	    
	    LbFN.setBounds(30,40,80,30);
	    FirstName.setBounds(120, 40, 100, 30);
	    LbSN.setBounds(230,40,80,30);
	    SurName.setBounds(320, 40, 100, 30);
	    LbPN.setBounds(30,80,80,30);
	    PhoneNumber.setBounds(120, 80, 100, 30);
	    LbCredit.setBounds(230,80,80,30);
	    Credit.setBounds(320, 80, 100, 30);
	    
		Btn_ok = new JButton("O K");
		Btn_close = new JButton("CLOSE");
		Btn_ok.setBounds(120, 130, 100, 30);
		Btn_close.setBounds(240, 130, 100, 30);
		
		Btn_ok.addActionListener(this);
		Btn_close.addActionListener(this);
		
		this.add(LbFN);
		this.add(FirstName);
		this.add(LbSN);
		this.add(SurName);
		this.add(LbPN);
		this.add(PhoneNumber);
		this.add(LbCredit);
		this.add(Credit);
		
	    this.add(Btn_ok);
	    this.add(Btn_close);
	   
	    this.setLayout(null);  
        //setting grid layout of 3 rows and 3 columns  
      
        this.setSize(500,240);  
        this.setVisible(true);
        
        setLocationRelativeTo(null);
        setTitle("ADD CUSTOMER");
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource().equals(Btn_ok) == true )
		{
			String Fn = FirstName.getText();
			String Sn = SurName.getText();
			String Pn = PhoneNumber.getText();
			String cd = Credit.getText();
			ArrayList<Customer> Customer_list = new ArrayList<Customer>();
			
			String serializedObject = "";
			
			Customer cus = new Customer(0, Fn, Sn, Pn, Double.valueOf(cd), true);
			Customer obj = null;
			 // serialize the object
			 try {
			     ByteArrayOutputStream bo = new ByteArrayOutputStream();
			     ObjectOutputStream so = new ObjectOutputStream(bo);
			     so.writeObject(cus);
			     so.flush();
			     serializedObject = bo.toString();
			     
			    
			     String r_str = myclient.Send_Receive("1"); // add customer command
			     if(r_str.equals("ok"))
			     {
			    	 r_str = myclient.Send_Receive(serializedObject);
			     }
			     if(r_str.equals("ok"))
			     {
			    	 
			    	 while(r_str.equals("end") != true)
			    	 {
			    		 r_str = myclient._Receive();
			    		 if(r_str.equals("end") != true)
			    		 {
			    			// deserialize
						     byte b[] = r_str.getBytes(); 
						     ByteArrayInputStream bi = new ByteArrayInputStream(b);
						     ObjectInputStream si = new ObjectInputStream(bi);
						     Customer custobj = (Customer) si.readObject();
						     Customer_list.add(custobj);
						     //System.out.println(custobj.toString());
			    		 }
			    		 
			    	 }
			    	 
			     }
			     ClientApp.UpdateCustTable(Customer_list);
			   
			 } catch (Exception exp) {
			     System.out.println(exp);
			 }
			 
			setVisible(false);
		}
		
		if(e.getSource().equals(Btn_close) == true)
		{
			setVisible(false);
		}
		
	}
}

class DVD_ADD_Frame extends JFrame implements ActionListener{
	private JButton Btn_ok;
	private JButton Btn_close;
	JTextField Title;  
	JComboBox CB_category; 
	JCheckBox ChB_new;
	
    DVD_ADD_Frame()
	{
		JLabel LbTl = new JLabel("Title : ");
		JLabel LbCategory = new JLabel("Category : ");
		
		
		String categoryItems[]={"horror","Sci-fi","Drama","Romance","Comedy","Action","Cartoon"};        
	    
		Title = new JTextField("");  
		CB_category = new JComboBox(categoryItems); 
		ChB_new = new JCheckBox("New Release");
		Btn_ok = new JButton("O K");
		Btn_close = new JButton("CLOSE");
		
	    Title.setBounds(120, 40, 200, 30);
	    LbTl.setBounds(30,40,50,30);
	    
	    ChB_new.setBounds(30,80,100,30);
	    LbCategory.setBounds(140,80,70,30);
	    CB_category.setBounds(210,80,100,30);
	    
		Btn_ok.setBounds(60, 130, 100, 30);
		Btn_close.setBounds(180, 130, 100, 30);
		
		Btn_ok.addActionListener(this);
		Btn_close.addActionListener(this);
		
		this.add(LbTl);
		this.add(Title);
		this.add(ChB_new);
		this.add(LbCategory);
		this.add(CB_category);
		
		
	    this.add(Btn_ok);
	    this.add(Btn_close);
	   
	    this.setLayout(null);  
        //setting grid layout of 3 rows and 3 columns  
      
        this.setSize(380,200);  
        this.setVisible(true);
        
        setLocationRelativeTo(null);
        setTitle("ADD DVD");
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource().equals(Btn_ok) == true )
		{
			String title = Title.getText();
			Boolean isnewRelease = ChB_new.isSelected();
			int categ = CB_category.getSelectedIndex() + 1;  
			System.out.println(isnewRelease);
			System.out.println(categ);
			String serializedObject = "";
			
			ArrayList<DVD> DVD_list = new ArrayList<DVD>();
			
			DVD dvd = new DVD(0, title, categ, isnewRelease, true);
			//DVD dvdobj = null;
			 // serialize the object
			 try {
			     ByteArrayOutputStream bo = new ByteArrayOutputStream();
			     ObjectOutputStream so = new ObjectOutputStream(bo);
			     so.writeObject(dvd);
			     so.flush();
			     serializedObject = bo.toString();
			     
			    
			     String r_str = myclient.Send_Receive("2"); // add customer command
			     if(r_str.equals("ok"))
			     {
			    	 r_str = myclient.Send_Receive(serializedObject);
			     }
			     if(r_str.equals("ok"))
			     {
			    	 
			    	 while(r_str.equals("end") != true)
			    	 {
			    		 r_str = myclient._Receive();
			    		 if(r_str.equals("end") != true)
			    		 {
			    			// deserialize
						     byte b[] = r_str.getBytes(); 
						     ByteArrayInputStream bi = new ByteArrayInputStream(b);
						     ObjectInputStream si = new ObjectInputStream(bi);
						     DVD dvdobj = (DVD) si.readObject();
						     DVD_list.add(dvdobj);
						     //System.out.println(custobj.toString());
			    		 }
			    		 
			    	 }
			    	 
			     }
			     
			     ClientApp.UpdateDvdTable(DVD_list);
			     ClientApp.DVDlist = DVD_list;
			     
			 } catch (Exception exp) {
			     System.out.println(exp);
			 }
			 
			setVisible(false);
		}
		
		if(e.getSource().equals(Btn_close) == true)
		{
			setVisible(false);
		}
		
	}
}


class Report_Rental extends JFrame implements ActionListener{
	private JButton Btn_search;

	private JCheckBox RemainItemSee;
	JTextField Date_search;  
	JTable TBL_Rental;
	
	ArrayList<Rental> RentalList = new ArrayList<Rental>();
	 
    Report_Rental()
	{
		JLabel Lbdate = new JLabel("    date : ");
		Date_search = new JTextField("");  
		RemainItemSee = new JCheckBox("See the outstanding rentals");
		Btn_search = new JButton("Search");
		TBL_Rental = new JTable();    
		    
		
		
		RemainItemSee.setBounds(80,40,230,30);
		Lbdate.setBounds(80,80,80,30);
		Date_search.setBounds(170,80,120,30);
		Btn_search.setBounds(300, 80, 100, 30);
		TBL_Rental.setBounds(50,120,380,250);
		
		TBL_Rental.setEnabled(false);
		RemainItemSee.addActionListener(this);
		Btn_search.addActionListener(this);
		
		this.add(Lbdate);
		this.add(Date_search);
		this.add(Btn_search);
		this.add(TBL_Rental);
		this.add(RemainItemSee);
	    this.add(Btn_search);
	    
	    JScrollPane ScP_Rental = new JScrollPane(TBL_Rental);  
		ScP_Rental.setBounds(50,120,380,250);
	    this.add(ScP_Rental);
	   
	    this.setLayout(null);  
        //setting grid layout of 3 rows and 3 columns  
      
        this.setSize(500,420);  
        this.setVisible(true);
        
        setLocationRelativeTo(null);
        setTitle("ADD CUSTOMER");
        
        InitTableView();
	}
    
    private  void UpdateRentalTable(ArrayList<Rental> Rental_list)
	{
		
		DefaultTableModel dtm_rent = new DefaultTableModel(0, 0);
		String header[] = new String[] { "rentalNumber", "dateRented", "dateReturned",
		           "custNumber", "dvdNumber", "totalpenaltyCost" };

		
		//add header in table model     
		dtm_rent.setColumnIdentifiers(header);
		//set model into the table object
		TBL_Rental.setModel(dtm_rent);
		
		// add row dynamically into the table      
		for (int count = 0; count < Rental_list.size(); count++) {
			String dvdN = Integer.toString(Rental_list.get(count).getDvdNumber());
			String custN = Integer.toString(Rental_list.get(count).getCustNumber());
			String dateRented = Rental_list.get(count).getDateRented();
			String dateReturned = Rental_list.get(count).getDateReturned();
			String RentN =  Integer.toString(Rental_list.get(count).getRentalNumber());
			String PenalCost = Double.toString(Rental_list.get(count).getTotalPenaltyCost());
			
			dtm_rent.addRow(new Object[] { RentN, dateRented, dateReturned, custN, dvdN, PenalCost });
		}
		
	}
    
    private  void InitTableView()
	{
		 
		 try {
			 String r_str = myclient.Send_Receive("7"); // send all rental table data
			 ArrayList<Rental> Rental_list = new ArrayList<Rental>();
		     if(r_str.equals("ok"))
		     {
		    	 while(r_str.equals("end") != true)
		    	 {
		    		 r_str = myclient._Receive();
		    		 if(r_str.equals("end") != true)
		    		 {
		    			// deserialize
					     byte b[] = r_str.getBytes(); 
					     ByteArrayInputStream bi = new ByteArrayInputStream(b);
					     ObjectInputStream si = new ObjectInputStream(bi);
						
					     Rental rentobj = (Rental) si.readObject();
					     Rental_list.add(rentobj);
					     //System.out.println(custobj.toString());
		    		 }
		    		 
		    	 }
		    	 
		     }
		     UpdateRentalTable(Rental_list);
		     RentalList = Rental_list;
	     } 
		 catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		 }
		 catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
    
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource().equals(Btn_search) == true )
		{
			String date = Date_search.getText();
			Boolean checked = RemainItemSee.isSelected();
			if(checked == true)
			{
				ArrayList<Rental> tmp_Rlist = new ArrayList<Rental>();
				
				for (int count = 0; count < RentalList.size(); count++) {
					
					String dateReturned = RentalList.get(count).getDateReturned();
					String dateRented = RentalList.get(count).getDateRented();
					if((dateReturned.equals("NA") == true) &&((dateRented.contains(date) == true)||(date.equals("") == true)))
					{
						tmp_Rlist.add(RentalList.get(count));
					}
					
				}
				UpdateRentalTable(tmp_Rlist);
			}
			else
			{
				ArrayList<Rental> tmp_Rlist = new ArrayList<Rental>();
				
				for (int count = 0; count < RentalList.size(); count++) {
					
					
					String dateRented = RentalList.get(count).getDateRented();
					if((dateRented.contains(date) == true)||(date.equals("") == true))
					{
						tmp_Rlist.add(RentalList.get(count));
					}
					
				}
				UpdateRentalTable(tmp_Rlist);
				
			}
			
		}
		if(e.getSource().equals(RemainItemSee) == true )
		{
			Boolean checked = RemainItemSee.isSelected();
			if(checked == true)
			{
				ArrayList<Rental> tmp_Rlist = new ArrayList<Rental>();
				for (int count = 0; count < RentalList.size(); count++) {
					
					String dateReturned = RentalList.get(count).getDateReturned();
					
					if(dateReturned.equals("NA") == true)
					{
						tmp_Rlist.add(RentalList.get(count));
					}
					
				}
				UpdateRentalTable(tmp_Rlist);
			}
			else
			{
				UpdateRentalTable(RentalList);
			}
			
			
		}
		
		
	}
}


class myclient {
	
	static Socket sock;  
	static DataInputStream din;  
	static DataOutputStream dout;  
	
	public static void connect(){  
		try{
			sock = new Socket("localhost",3333);  
		    din = new DataInputStream(sock.getInputStream());  
			dout = new DataOutputStream(sock.getOutputStream());  
			
		}catch(Exception e){System.out.println(e);}  
	}
	
	public static String Send_Receive(String sendStr){
		String ReceiveStr="";
		try {
			dout.writeUTF(sendStr);
			dout.flush();
			ReceiveStr = din.readUTF();
			return ReceiveStr;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return ReceiveStr;
		 
	}
	
	public static String _Receive(){
		String ReceiveStr="";
		try {
			ReceiveStr = din.readUTF();
			return ReceiveStr;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("receving error!");
		}  
		return ReceiveStr;
		 
	}
		
    public static void Close()
    {
    	try {
			dout.close();
			sock.close();  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
    }
	
	
}



  
