package aadhi_bus;

import java.sql.*;
import java.util.*;

public class BusOperations {
    public static void addBus(Scanner sc)
    {
        try(Connection con=DbConnection.getConnection()) 
        {
        	viewavailablebuses();         
            System.out.print("Enter bus number:");
            int busNo=sc.nextInt();
            sc.nextLine(); 
            System.out.print("Enter bus name:");
            String busName=sc.nextLine();
            System.out.print("Enter route:");
            String route=sc.nextLine();
            System.out.print("Enter capacity:");
            int capacity=sc.nextInt();
            System.out.print("Enter price: ");
            double price=sc.nextDouble();
            String query="INSERT INTO bus(bus_no, bus_name, route, capacity, price) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst=con.prepareStatement(query);
            pst.setInt(1,busNo);
            pst.setString(2,busName);
            pst.setString(3,route);
            pst.setInt(4,capacity);
            pst.setDouble(5,price);
            pst.executeUpdate();
            System.out.println("Bus added successfully!");             
        }
        catch(SQLException e)
        {
            System.out.println("Error adding bus :"+e.getMessage());
        }
    }

    public static void editBus(Scanner sc)
    {
    	try(Connection con=DbConnection.getConnection())
    	{
    		viewavailablebuses();  
            System.out.print("Enter bus number to edit:");
            int busNo=sc.nextInt();
            sc.nextLine();
            String checkQuery="SELECT * FROM bus WHERE bus_no =?";
            PreparedStatement checkStmt=con.prepareStatement(checkQuery);
            checkStmt.setInt(1,busNo);
            ResultSet checkRs=checkStmt.executeQuery();
            if(checkRs.next())
            {
            	System.out.println("Current details for bus"+busNo+":");
            	System.out.println("Name:"+checkRs.getString("bus_name"));
            	System.out.println("Route:"+checkRs.getString("route"));
            	System.out.println("Capacity:"+checkRs.getInt("capacity"));
            	System.out.println("Price:"+checkRs.getDouble("price"));
                System.out.print("Enter new bus name:");
                String busName=sc.nextLine();
                System.out.print("Enter new route:");
                String route=sc.nextLine();
                System.out.print("Enter new capacity:");
                int capacity=sc.nextInt();
                System.out.print("Enter new price:");
                double price=sc.nextDouble();
                String updateQuery="UPDATE bus SET bus_name=?,route=?,capacity=?,price=? WHERE bus_no=?";
                PreparedStatement updateStmt = con.prepareStatement(updateQuery);
                updateStmt.setString(1,busName);
                updateStmt.setString(2,route);
                updateStmt.setInt(3,capacity);
                updateStmt.setDouble(4,price);
                updateStmt.setInt(5,busNo);
                updateStmt.executeUpdate();
                System.out.println("Bus details updated successfully!");
            }
            else
            {
                System.out.println("Bus not found.");
            }
        }
        catch(SQLException e)
        {
            System.out.println("Error editing bus:"+e.getMessage());
        }
    }

    public static void deleteBus(Scanner sc)
    {
    	try(Connection con=DbConnection.getConnection())
    	{
    		viewavailablebuses();
            System.out.print("Enter bus number to delete: ");
            int busNo=sc.nextInt();
            String checkQuery="SELECT * FROM bus WHERE bus_no = ?";
            PreparedStatement checkStmt=con.prepareStatement(checkQuery);
            checkStmt.setInt(1,busNo);
            ResultSet rs1=checkStmt.executeQuery();
            if(rs1.next())
            {
            	String deleteQuery="DELETE FROM bus WHERE bus_no = ?";
                PreparedStatement deleteStmt=con.prepareStatement(deleteQuery); 
                deleteStmt.setInt(1,busNo);
                deleteStmt.executeUpdate();
                System.out.println("Bus deleted successfully!");                 
            }
            else
            {
                System.out.println("Bus not found.");
            }           
        }
        catch(SQLException e)
        {
            System.out.println("Error deleting bus: "+e.getMessage());
        }
        
    }

	public static void addBusDates(Scanner sc)
{
		try(Connection con=DbConnection.getConnection())
    	{
    		viewavailablebuses();
	        System.out.print("Enter bus number to add dates: ");
	        int busNo=sc.nextInt();
	        String fetchDatesQuery="SELECT travel_date FROM bus_dates WHERE bus_no = ?";
	        PreparedStatement pst=con.prepareStatement(fetchDatesQuery);
	        pst.setInt(1,busNo);
	        ResultSet rs=pst.executeQuery();
	        System.out.println("Existing Dates for Bus "+busNo+":");
	        while(rs.next())
	        {
	            System.out.println(rs.getString("travel_date"));
	        }
	        String insertDateQuery="INSERT INTO bus_dates(bus_no, travel_date) VALUES (?, ?)";
	        pst=con.prepareStatement(insertDateQuery);
	        while(true)
	        {
	            System.out.print("Enter new travel date (YYYY-MM-DD): ");
	            String date=sc.next();
	            pst.setInt(1,busNo);
	            pst.setString(2,date);
	            pst.executeUpdate();
	            System.out.println("Date added successfully!");
	            System.out.print("Do you want to add another date? (yes/no):");
	            if(!sc.next().equalsIgnoreCase("yes"))
	            {
	                break;
	            }
	        }
	    }
	    catch(SQLException e)
	    {
	        System.out.println("Error while adding dates to bus:"+e.getMessage());
	    }
	}

	public static void editBusDate(Scanner sc)
	{
		try(Connection con=DbConnection.getConnection())
    	{
    		viewavailablebuses();
	        System.out.print("Enter bus number to edit dates:");
	        int busNo=sc.nextInt();
	        String fetchDatesQuery="SELECT id,travel_date FROM bus_dates WHERE bus_no = ?";
	        PreparedStatement pst=con.prepareStatement(fetchDatesQuery);
	        pst.setInt(1,busNo);
	        ResultSet rs=pst.executeQuery();
	        System.out.println("Existing Dates for Bus"+busNo+":");
	        Map<Integer,Integer>dateIdMap=new HashMap<>();
	        int index=1;
	        while(rs.next())
	        {
	            int id=rs.getInt("id");
	            String date=rs.getString("travel_date");
	            System.out.println(index+". "+date);
	            dateIdMap.put(index,id);
	            index++;
	        }
	        if (dateIdMap.isEmpty())
	        {
	            System.out.println("No dates found for the specified bus.");
	            return;
	        }

	        System.out.print("Enter the number corresponding to the date you want to update:");
	        int choice=sc.nextInt();
	        if(!dateIdMap.containsKey(choice))
	        {
	            System.out.println("Invalid choice.");
	            return;
	        }
	        int dateId=dateIdMap.get(choice);
	        System.out.print("Enter the new date (YYYY-MM-DD):");
	        String newDate=sc.next();
	        String updateDateQuery="UPDATE bus_dates SET travel_date=? WHERE id=?";
	        pst=con.prepareStatement(updateDateQuery);
	        pst.setString(1,newDate);
	        pst.setInt(2,dateId);
	        pst.executeUpdate();
	        System.out.println("Date updated successfully.");	        
	    }
	    catch(SQLException e)
	    {
	        System.out.println("Error while editing dates for bus:"+e.getMessage());
	        
	    }
	}

	public static void deleteBusDate(Scanner sc)
	{
		try(Connection con=DbConnection.getConnection())
    	{
    		viewavailablebuses();
	        System.out.print("Enter bus number to delete dates: ");
	        int busNo=sc.nextInt();
	        String fetchDatesQuery="SELECT id,travel_date FROM bus_dates WHERE bus_no=?";
	        PreparedStatement  pst=con.prepareStatement(fetchDatesQuery);
	        pst.setInt(1,busNo);
	        ResultSet rs=pst.executeQuery();
	        System.out.println("Existing Dates for Bus "+busNo+":");
	        Map<Integer,Integer>dateIdMap=new HashMap<>();
	        int index=1;
	        while(rs.next())
	        {
	            int id=rs.getInt("id");
	            String date=rs.getString("travel_date");
	            System.out.println(index+". "+date);
	            dateIdMap.put(index,id);
	            index++;
	        }
	        if(dateIdMap.isEmpty())
	        {
	            System.out.println("No dates found for the specified bus.");
	            return;
	        }
	        System.out.print("Enter the number corresponding to the date you want to delete: ");
	        int choice=sc.nextInt();
	        if (!dateIdMap.containsKey(choice))
	        {
	            System.out.println("Invalid choice.");
	            return;
	        }
	        int dateId = dateIdMap.get(choice);
	        String deleteDateQuery="DELETE FROM bus_dates WHERE id = ?";
	        pst=con.prepareStatement(deleteDateQuery);
	        pst.setInt(1, dateId);
	        pst.executeUpdate();
	        System.out.println("Date deleted successfully.");
	    }
	    catch(SQLException e)
	    {
	        System.out.println("Error:"+e.getMessage());
	    }
	}
    
	public static void viewavailablebuses()
	{
		try
        {
        	Connection con=DbConnection.getConnection();
            String listQuery="SELECT * FROM bus";
            PreparedStatement listStmt=con.prepareStatement(listQuery);
            ResultSet rs=listStmt.executeQuery();
            System.out.println("Available Buses:");
            while(rs.next())
            {
            	System.out.println("Bus No: "+rs.getInt("bus_no")+",Name: "+rs.getString("bus_name")+
                                    ",Route: "+rs.getString("route")+",Capacity: "+rs.getInt("capacity") +
                                    ",Price: "+rs.getDouble("price"));
            }
        }
		catch(Exception e){
	        System.out.println("Error while viewing Available buses: " + e.getMessage());
	    }
		
	}
	
}
