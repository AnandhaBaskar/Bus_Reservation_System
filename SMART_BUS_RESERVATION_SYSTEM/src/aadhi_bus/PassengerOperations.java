package aadhi_bus;
//anandhabaskar465@gmail.com
//admin@bus14
//fhari2004@gmail.com
//hari@2004
import java.sql.*;
import java.util.Scanner;

public class PassengerOperations
{

    public static void showPassengerMenu(String passengerName,int passengerId)
    {
        Scanner sc=new Scanner(System.in);
        while(true)
        {
            System.out.println("\nWelcome, "+passengerName+"!");
            System.out.println("1. Book a Seat\n 2.Cancel Booking\n 3.Logout");
            System.out.print("Choose an option: ");
            int choice=sc.nextInt();
            switch(choice)
            {        
                case 1:
                    Booking.bookSeats(sc,passengerId);
                    break;
                case 2:
                    Booking.cancelSeats(sc,passengerId);
                    break;
                case 3:
                    System.out.println("Logging out...\n");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
