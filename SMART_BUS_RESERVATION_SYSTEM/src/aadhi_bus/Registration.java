package aadhi_bus;

import java.sql.*;
import java.util.*;

public class Registration
{
    public static void registerUser()
    {
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter your name :");
        String name=sc.nextLine();
        String email;
        while(true)
        {
            System.out.print("Enter your email :");
            email=sc.nextLine();
            if (isValidEmail(email)) break;
            System.out.println("Invalid email format. Please try again.");
        }
        System.out.println("Select user type : 1. Passenger 2. Admin");
        int userType=sc.nextInt();
        String password;
        if(userType==1)
        {
            System.out.print("Enter your password :");
            password=sc.next();
        }
        else
        {
            password="admin@bus14";
        }
        try
        {
            Connection con=DbConnection.getConnection();
            String query="INSERT INTO users (name,email,password,user_type) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1,name);
            pst.setString(2,email);
            pst.setString(3,password);
            pst.setString(4,userType==1?"passenger":"admin");
            pst.executeUpdate();
            System.out.println("Registration successful! Please login to continue.");
        }
        catch(SQLException e)
        {
            System.out.println("Error during registration :"+e.getMessage());
        }
    }

    private static boolean isValidEmail(String email)
    {
        String regex="^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,6}$";
        return email !=null && email.matches(regex);
    }
}

