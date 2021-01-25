/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author Ahmed_Maher
 *
 * @author Ibrahim_Magdy
 * 
 */
public class Database {
    static Connection con =null;
    static String db_name="xo_netwok_game";
    static String url="jdbc:mysql://localhost:3306/"+db_name;
    static String username="maher";
    static String password="password1234";
    
    
    public static void dbConnect() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();

        }
        catch(ClassNotFoundException e){
            System.out.println("Where is your JDBC Driver ?");
            e.printStackTrace();
            throw e;
        }
            System.out.println("JDBC Registerd");
            
        try{
           con= DriverManager.getConnection(url, username, password); 
            System.out.println("Connection Success");

        }
        catch(SQLException e){
            System.out.println("Connection Field Check output" + e);
            throw e;

        }

        
    }
    
     public static void dbDisconnect() throws  SQLException {
        try{
            if(con != null && !con.isClosed()){
                con.close();
            }
        }
        catch(Exception e){
            throw e;
        }
    }
     
    
   
 

    
    public static int register(String[] arr) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
      
        dbConnect();
        PreparedStatement statement;
        statement = con.prepareStatement("insert into Player(`username`,`email`,`password`)values(?,?,?)");
        statement.setString(1, arr[0]);
        statement.setString(2, arr[1]);
        statement.setString(3, arr[2]);
        int status = statement.executeUpdate();
        if(status ==1){
            dbDisconnect();
            return 1;
        }
        else{
            return 0;
        }

    }      
    
    // this function takes the game id and the states as an array[9] of integgers and save the game in the database
    public static void saveGame(int gameId, int[] maze) throws SQLException, IndexOutOfBoundsException{
        PreparedStatement statement;
        statement = con.prepareStatement("insert into game_info values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        statement.setInt(1, gameId);
        for(int i = 2; i < 10; i++){
            statement.setInt(i, maze[i - 2]);
        }
    }
    
    //this function take a paused game id and return the game states in as an array[9] of integers 
    public static int[] loadGame(int gameId) throws SQLException, IndexOutOfBoundsException{
        int[] maze = new int[9];
        PreparedStatement statement;
        statement = con.prepareStatement("select * from game_info where game_id = ?");
        statement.setInt(1, gameId);
        ResultSet rs = statement.executeQuery();
        if(rs.next()){
            for(int i = 0; i < 10; i++){
                maze[i] = rs.getInt(i + 2);
            }
        }
        return maze;
    }
    
    //this function takes player id and return result set with all ids of his paused games 
    public static ResultSet loadAllPlayerSavedGames(int playerId) throws SQLException, IndexOutOfBoundsException{
        int[] maze = new int[9];
        PreparedStatement statement;
        statement = con.prepareStatement("select * from game_info gf " +
                "inner join game g on g.id = gf.game_id " +
                "where g.player1_id = ? or player2_id = ?");
        statement.setInt(1, playerId);
        statement.setInt(1, playerId);
        return statement.executeQuery();
    }
    
    //to now the player status
    public static int getPlayerStatus(int playerId) throws SQLException{
        PreparedStatement statement;
        statement = con.prepareStatement("Select player_status from player where id = ?");
        statement.setInt(1, playerId);
        ResultSet rs = statement.executeQuery();
        return rs.getInt("player_status");
    }
    
    //to update player score
    public static void getPlayerStatus(int playerId, int value) throws SQLException{
        PreparedStatement statement;
        statement = con.prepareStatement("update player " +
                "set player_points = (player_points + ? )" + 
                "where id = ?");
        statement.setInt(1, value);
        statement.setInt(2, playerId);
        statement.executeUpdate();
    }
    
    //to update player score
//    public static void isUserNameAvailable(int playerId, int value) throws SQLException{
//        PreparedStatement statement;
//        statement = con.prepareStatement("update player " +
//                "set player_points = (player_points + ? )" + 
//                "where id = ?");
//        statement.setInt(1, value);
//        statement.setInt(2, playerId);
//        statement.executeUpdate();
//    }
    
}




