package aadhi_bus;

import java.sql.*;
import java.util.*;

public class Login
{
    public static void loginUser()
    {
        Scanner sc=new Scanner(System.in);
        boolean loggedIn=false;

        while(!loggedIn)
        {
            System.out.print("Enter your email :");
            String email=sc.next();
            System.out.print("Enter your password :");
            String password=sc.next();
            try
            {
                Connection con=DbConnection.getConnection();
                String query="SELECT * FROM users WHERE email=? AND password=?";
                PreparedStatement pst=con.prepareStatement(query);
                pst.setString(1,email);
                pst.setString(2,password);
                ResultSet rs=pst.executeQuery();
                if(rs.next())
                {
                    String userType=rs.getString("user_type");
                    System.out.println("Login successful!!!!!");
                    loggedIn=true; 
                    if(userType.equals("passenger"))
                    {
                        int passengerId=rs.getInt("id");
                        PassengerOperations.showPassengerMenu(rs.getString("name"),passengerId);
                    }
                    else
                    {
                    	AdminOperations adminOperations=new AdminOperations();
                        adminOperations.showAdminMenu(sc);
                    }
                }
                else
                {
                    System.out.println("Invalid email or password.Please try again.\n");
                }
            }
            catch(SQLException e)
            {
                System.out.println("Error during login :"+e.getMessage());
            }
        }
    }
}
