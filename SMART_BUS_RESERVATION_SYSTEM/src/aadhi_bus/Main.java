package aadhi_bus;

import java.util.*;
public class Main
{
    public static void main(String[] args)
    {
    	new InitalizeTables();
        Scanner sc=new Scanner(System.in);       
        while(true)
        {
        	System.out.println("Welcome to the Bus Reservation System!");
            System.out.println(" 1.Register\n 2.Login\n 3.Exit");
            System.out.print("Choose an option:");
            int choice=sc.nextInt();
            switch(choice)
            {
                case 1:
                    Registration.registerUser();
                    break;
                case 2:
                    Login.loginUser();
                    break;
                case 3:
                    System.out.println("Thank you for using the Bus Reservation System!\n Created By Anandha Baskar");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
