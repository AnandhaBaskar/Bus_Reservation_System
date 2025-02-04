package aadhi_bus;

import java.sql.*;

public class InitalizeTables {
	
	InitalizeTables() 
	{
		try(Connection con=DbConnection.getConnection()) 
        {
        
			String query="CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT PRIMARY KEY,"+
					"name VARCHAR(50),email VARCHAR(100) UNIQUE,password VARCHAR(50),"
					+"user_type ENUM('passenger', 'admin') NOT NULL);";
            PreparedStatement pst=con.prepareStatement(query);         
            pst.executeUpdate();
            
            
            
            query="CREATE TABLE IF NOT EXISTS bus(bus_no INT PRIMARY KEY,bus_name VARCHAR(255), route VARCHAR(255),"
            		+"capacity INT,price DECIMAL(10, 2));";            
            pst=con.prepareStatement(query);         
            pst.executeUpdate();
            
            
            
            query="CREATE TABLE IF NOT EXISTS booking (booking_id INT AUTO_INCREMENT PRIMARY KEY,bus_no INT,seat_no INT,"
            		+ "travel_date DATE,passenger_id INT,FOREIGN KEY (passenger_id) REFERENCES users(id),"
            		+"FOREIGN KEY (bus_no) REFERENCES bus(bus_no));";
            pst=con.prepareStatement(query);         
            pst.executeUpdate();
            
            query="CREATE TABLE IF NOT EXISTS bus_dates(id INT AUTO_INCREMENT PRIMARY KEY,bus_no INT,travel_date DATE,"
            		+ "FOREIGN KEY (bus_no) REFERENCES bus(bus_no) ON DELETE CASCADE);";
            pst=con.prepareStatement(query);         
            pst.executeUpdate();
            
        }
        catch(SQLException e)
        {
            System.out.println("Error adding bus :"+e.getMessage());
        }
		
	}
}
