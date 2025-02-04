package aadhi_bus;
//anandhabaskar465@gmail.com
//admin@bus14
//fhari2004@gmail.com
//hari@2004
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Booking {

	public static void bookSeats(Scanner sc,int passengerId)
	{
	    try {
	        System.out.print("Enter travel date (yyyy-MM-dd): ");
	        String travelDateString=sc.next();
	        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	        java.util.Date utilDate=sdf.parse(travelDateString);
	        java.sql.Date travelDate=new java.sql.Date(utilDate.getTime());
	        Connection con=DbConnection.getConnection();
	        String busQuery="SELECT bus_no,travel_date FROM bus_dates WHERE travel_date=?";
	        PreparedStatement busStmt=con.prepareStatement(busQuery);
	        busStmt.setDate(1,travelDate);
	        ResultSet rs=busStmt.executeQuery();
	        System.out.println("\nAvailable buses on "+travelDateString+":");
	        boolean found=false;
	        while(rs.next())
	        {
	            found=true;
	            int busNo=rs.getInt("bus_no");
	            System.out.println("Bus No:"+busNo+" | Travel Date:"+rs.getString("travel_date"));
	        }
	        if(!found)
	        {
	            System.out.println("No buses available on this date.");
	            return;
	        }
	        System.out.print("\nEnter bus number to proceed: ");
	        int busNo=sc.nextInt();
	        int busCapacity=getBusCapacity(busNo);
	        int availableSeats=getAvailableSeats(busNo,travelDate);
	        System.out.println("Available seats: "+availableSeats+" / "+busCapacity);
	        if(availableSeats==0)
	        {
	            System.out.println("No seats available on this bus for the selected date.");
	            return;
	        }
	        viewSeatStructure(busNo,travelDate);
	        System.out.print("Enter the number of seats you want to book: ");
	        int numSeats=sc.nextInt();
	        if (numSeats>availableSeats) {
	            System.out.println("Not enough seats available. Only " + availableSeats + " seats are available.");
	            return;
	        }
	        double totalAmount=0;
	        for(int i=0;i<numSeats;i++)
	        {
	            int seatNo;
	            boolean seatBooked=false;
	            do
	            {
	                System.out.print("Enter seat number to book (1-"+busCapacity+"):");
	                seatNo=sc.nextInt();
	                if (seatNo<1 || seatNo>busCapacity)
	                {
	                    System.out.println("Invalid seat number. Please choose a seat between 1 and "+busCapacity+".");
	                    seatBooked=false;
	                }
	                else if(isSeatBooked(busNo,seatNo,travelDate))
	                {
	                    System.out.println("Seat "+seatNo+" is already booked. Please choose a different seat.");
	                    seatBooked=false;
	                }
	                else
	                {
	                    seatBooked=true;
	                    totalAmount+=bookSeat(busNo,seatNo,travelDate,passengerId);
	                }
	            } while(!seatBooked);
	        }
	        System.out.println("Total amount: "+totalAmount);
	        System.out.print("Do you want to confirm booking? (y/n): ");
	        char proceed=sc.next().charAt(0);
	        if (proceed=='y' || proceed=='Y')
	        {
	            System.out.println("Booking successful! Total amount: "+totalAmount);
	        }
	        else
	        {
	            System.out.println("Booking cancelled.");
	        }
	    }
	    catch(Exception e)
	    {
	        System.out.println("Error while booking seats: "+e.getMessage());
	    }
	}

	private static int getAvailableSeats(int busNo, java.sql.Date travelDate)throws SQLException {
	    Connection con=DbConnection.getConnection();
	    String query="SELECT capacity FROM bus WHERE bus_no = ?";
	    PreparedStatement stmt=con.prepareStatement(query);
	    stmt.setInt(1,busNo);
	    ResultSet rs=stmt.executeQuery();
	    if(rs.next())
	    {
	        int busCapacity=rs.getInt("capacity");
	        String bookedSeatsQuery="SELECT COUNT(*) FROM booking WHERE bus_no = ? AND travel_date = ?";
	        PreparedStatement bookedStmt=con.prepareStatement(bookedSeatsQuery);
	        bookedStmt.setInt(1,busNo);
	        bookedStmt.setDate(2,travelDate);
	        ResultSet rs1=bookedStmt.executeQuery();
	        rs1.next();
	        int bookedSeats=rs1.getInt(1);
	        return busCapacity - bookedSeats;
	    }
	    else
	    {
	        throw new SQLException("Bus not found.");
	    }
	}

	private static int getBusCapacity(int busNo)throws SQLException
	{
	    Connection con=DbConnection.getConnection();
	    String query="SELECT capacity FROM bus WHERE bus_no = ?";
	    PreparedStatement stmt=con.prepareStatement(query);
	    stmt.setInt(1,busNo);
	    ResultSet rs=stmt.executeQuery();

	    if(rs.next())
	    {
	        return rs.getInt("capacity");
	    }
	    else
	    {
	        throw new SQLException("Bus not found.");
	    }
	}

	private static boolean isSeatBooked(int busNo,int seatNo,java.sql.Date travelDate)throws SQLException
	{
	    Connection con=DbConnection.getConnection();
	    String checkQuery="SELECT * FROM booking WHERE bus_no=? AND seat_no=? AND travel_date=?";
	    PreparedStatement checkStmt=con.prepareStatement(checkQuery);
	    checkStmt.setInt(1,busNo);
	    checkStmt.setInt(2,seatNo);
	    checkStmt.setDate(3,travelDate);
	    ResultSet rs=checkStmt.executeQuery();
	    return rs.next();
	}

	private static double bookSeat(int busNo,int seatNo,java.sql.Date travelDate,int passengerId)throws SQLException {
	    Connection con=DbConnection.getConnection();
	    String priceQuery="SELECT price FROM bus WHERE bus_no=?";
	    PreparedStatement priceStmt=con.prepareStatement(priceQuery);
	    priceStmt.setInt(1,busNo);
	    ResultSet priceRs=priceStmt.executeQuery();
	    priceRs.next();
	    double price=priceRs.getDouble("price");
	    String bookingQuery="INSERT INTO booking(bus_no,seat_no,travel_date,passenger_id) VALUES (?, ?, ?, ?)";
	    PreparedStatement bookingStmt = con.prepareStatement(bookingQuery);
	    bookingStmt.setInt(1,busNo);
	    bookingStmt.setInt(2,seatNo);
	    bookingStmt.setDate(3,travelDate);
	    bookingStmt.setInt(4,passengerId);
	    bookingStmt.executeUpdate();
	    return price;
	}

	public static void cancelSeats(Scanner sc,int passengerId)
	{
	    try
	    {
	        Connection con=DbConnection.getConnection();
	        double totalRefundAmount=0.0;
	        while(true)
	        {
	        	System.out.println("\nFetching your bookings...");
	            String query = "SELECT b.booking_id,b.bus_no,b.seat_no,bd.travel_date,bu.price "+
	                           "FROM booking b JOIN bus_dates bd ON b.bus_no=bd.bus_no AND b.travel_date=bd.travel_date " +
	                           "JOIN bus bu ON b.bus_no=bu.bus_no  WHERE b.passenger_id=? AND bd.travel_date>=CURDATE()";
	            PreparedStatement pst=con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			    pst.setInt(1,passengerId);
			    ResultSet rs=pst.executeQuery();
	            System.out.println("Your upcoming bookings:");
	            int i=1;
	            boolean hasBookings=false;
	            while(rs.next())
	            {
	            	hasBookings = true;
		            System.out.println(i+". Bus No: "+rs.getInt("bus_no") +" | Seat No: "+rs.getInt("seat_no") +
		                               " | Travel Date: "+rs.getDate("travel_date") +" | Fare: ₹"+rs.getDouble("price"));
		            i++;
		        }
	            if(!hasBookings)
	            {
	            	System.out.println("You have no upcoming bookings to cancel.");
		            return;
		        }
	            System.out.print("\nEnter the number corresponding to the booking you want to cancel: ");
		        int bookingChoice=sc.nextInt();
		        if(bookingChoice<1 || bookingChoice>=i)
		        {
		        	System.out.println("Invalid choice. Please try again.");
		            continue;
		        }
		        rs.absolute(bookingChoice);
		        int bookingId=rs.getInt("booking_id");
		        double refundAmount=rs.getDouble("price");
		        String deleteQuery="DELETE FROM booking WHERE booking_id=?";
		        PreparedStatement deleteStmt=con.prepareStatement(deleteQuery);
		        deleteStmt.setInt(1,bookingId);
		        int rowsAffected=deleteStmt.executeUpdate();
		        if (rowsAffected>0)
		        {
		        	totalRefundAmount+=refundAmount;
		            System.out.println("Booking canceled successfully.");
		        }
		        else
		        {
		            System.out.println("Error in canceling the booking. Please try again.");
		        }
		        System.out.print("\nDo you want to cancel another booking? (yes/no): ");
		        sc.nextLine();
		        String userChoice=sc.nextLine().trim().toLowerCase();	
		        if(!userChoice.equals("yes"))
		        {
		                break;
		        }
	        }
	        System.out.println("Your refund amount of ₹"+totalRefundAmount+" will be processed within 5-7 business days." );
	    }
	    catch(Exception e)
	    {
	        System.out.println("Error while canceling seats: "+e.getMessage());
	    }
	}
 
    public static void viewSeatStructure(int busNo,java.sql.Date travelDate)
    {
        try  {
            Connection con=DbConnection.getConnection();
            String query="SELECT capacity FROM bus WHERE bus_no =?";
            PreparedStatement stmt=con.prepareStatement(query);
            stmt.setInt(1,busNo);
            ResultSet rs=stmt.executeQuery();
            if (rs.next())
            {
                int capacity=rs.getInt("capacity");
                char[] seats=new char[capacity];
                Arrays.fill(seats,'*'); 
                String bookingQuery="SELECT seat_no FROM booking WHERE bus_no = ? AND travel_date = ?";
                PreparedStatement bookingStmt=con.prepareStatement(bookingQuery);
                bookingStmt.setInt(1,busNo);
                bookingStmt.setDate(2,travelDate);
                ResultSet bookedSeats=bookingStmt.executeQuery();
                while(bookedSeats.next())
                {
                    int bookedSeat=bookedSeats.getInt("seat_no");
                    seats[bookedSeat-1]='B';
                }
                System.out.println("\nSeat Structure (B = Booked,* = Available):");
                for(int i=0;i< seats.length;i++)
                {
                    System.out.printf("%2d(%c) ",i + 1,seats[i]);
                    if((i + 1)%4 == 2)
                    {
                        System.out.print("      ");
                    }
                    if((i + 1)%4 == 0)
                    {
                        System.out.println(); 
                    }
                }
                if(capacity%4 != 0)
                {
                    System.out.println();
                }
            }
            else
            {
                System.out.println("No bus found with the given number.");
            }

        }catch(Exception e)
        {
            System.out.println("Error while viewing seat structure: "+e.getMessage());
        }
    }
  
}