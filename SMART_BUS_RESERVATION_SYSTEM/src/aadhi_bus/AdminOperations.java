package aadhi_bus;
//anandhabaskar465@gmail.com
//admin@bus14
//fhari2004@gmail.com
//hari@2004
import java.sql.*;
import java.util.*;
import java.util.Scanner;

public class AdminOperations
{
    public void showAdminMenu(Scanner sc)
    {
        int choice=-1;
        while (true)
        {
            System.out.println("\nAdmin Menu:\n 1. Add a Bus\n2. Edit Bus Details\n3. Delete Bus Details\n4. Add Dates for Bus\n5. Edit Dates for Bus\n6. Delete Dates for Bus\n7. Logout");
            System.out.print("Choose an option: ");
            if (sc.hasNextInt())
            {
                choice = sc.nextInt();
                sc.nextLine();
                switch (choice)
                {
                    case 1:
                    	BusOperations.addBus(sc);
                        break;
                    case 2:
                    	BusOperations.editBus(sc);
                        break;
                    case 3:
                    	BusOperations.deleteBus(sc);
                        break;
                    case 4:
                    	BusOperations.addBusDates(sc);
                        break;
                    case 5:
                    	BusOperations.editBusDate(sc);
                        break;
                    case 6:
                    	BusOperations.deleteBusDate(sc);
                        break;
                    case 7:
                        System.out.println("Logging out. Goodbye!");
                        return; 
                    default:
                        System.out.println("Invalid option. Please choose between 1 and 8.");
                }
            } 
            else
            {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); 
            }
        }
    }
}
