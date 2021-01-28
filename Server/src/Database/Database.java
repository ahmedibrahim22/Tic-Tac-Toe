/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import Database.Player;

/**
 *
 * @author Ahmed_Maher
 * 
 */
public class Database {
    static Connection con =null;
    static String db_name="xo_netwok_game";
    static String url="jdbc:mysql://localhost:3306/"+db_name;
    static String username="maher";
    static String password="password1234";
    
    //this function created to connect to the database
    public static void dbConnect() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        con= DriverManager.getConnection(url, username, password); 
    }
    
    
    //this function created to disconnect to the database
    public static void dbDisconnect() throws  SQLException {
        con.close();
    }
     
    
    public static int login(String username , String password) throws SQLException{
        
        PreparedStatement statement;
        statement = con.prepareStatement("Select id from player where username = ? and password = ?");
        statement.setString(1, username);
        statement.setString(2, password);

        ResultSet rs = statement.executeQuery();
        int id=-1;
        if(rs.next()){
            id = rs.getInt("id");
        }
        
        return id;
    }   

    //this function take array of strings that represent player data and save this data into the database
    public static int register(String[] arr) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {    
        int success_register=1;
        int email_exist=valiateEmail(arr[1]);
        int userName_exist=valiateUsername(arr[0]);
        if(email_exist==0 && userName_exist ==0){
            PreparedStatement statement;
            statement = con.prepareStatement("insert into Player(`username`,`email`,`password`)values(?,?,?)");
            statement.setString(1, arr[0]);
            statement.setString(2, arr[1]);
            statement.setString(3, arr[2]);
            statement.executeUpdate();
        }
        else{
            success_register= 0;
        }
        return success_register;
    }      
    
    // this function takes the game id , player id and the states as an array[9] of strings and save the game in the database
    public static void saveGame(int gameId,int player1_id ,int player2_id,String[] maze) throws SQLException, IndexOutOfBoundsException, IllegalAccessException{
        PreparedStatement statement;
        statement = con.prepareStatement("insert into game_info (`game_id`,`value1`,`value2`,`value3`,"
                + "`value4`,`value5`,`value6`,`value7`,`value8`,`value9`,`player1_id`,`player2_id`)"
                + "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)");
        statement.setInt(1, gameId);
        for(int i = 2; i <= 10; i++){
            statement.setString(i, maze[i - 2]);
        }
        statement.setInt(11, player1_id);
        statement.setInt(12, player2_id);
        statement.executeUpdate();
    }
    
    
    //this function take a paused game id and return the game states in as an array[9] of strings 
    public static String[] loadGame(int gameId, int player1_id , int player2_id) throws SQLException, IndexOutOfBoundsException{
        String[] maze = new String[9];
        PreparedStatement statement;
        statement = con.prepareStatement("select * from game_info where game_id = ? and player1_id= ? and player2_id = ?");
        statement.setInt(1, gameId);
        statement.setInt(2, player1_id);
        statement.setInt(3, player2_id);
        ResultSet rs = statement.executeQuery();
        if(rs.next()){
            for(int i=0;i<9;i++){
                maze[i]=rs.getString("value"+(i+1));
            }
        }
        return maze;
    }
    
    //this function takes player id and return result set with all ids of his paused games 
//    public static ResultSet loadAllPlayerSavedGames(int playerId) throws SQLException, IndexOutOfBoundsException{
//        PreparedStatement statement;
//        statement = con.prepareStatement("select * from game_info where player_id= ?");
//        statement.setInt(1, playerId);
//        return statement.executeQuery();
//    }
    
    //this function take player id and return the status of this player
    public static int getPlayerStatus(int playerId) throws SQLException,IndexOutOfBoundsException{
        PreparedStatement statement;
        statement = con.prepareStatement("Select player_status from player where id = ?");
        statement.setInt(1, playerId);
        ResultSet rs = statement.executeQuery();
        int status=0;
        if(rs.next()){
            status = rs.getInt("player_status");
        }
        
        return status;
    }
    
    //this function take player id and score value and update in the database the score of this player by increase it with the new value
    public static void updatePlayerScore(int playerId, int value) throws SQLException {
        PreparedStatement statement;
        statement = con.prepareStatement("update player " +
                "set player_points = (player_points + ? )" + 
                "where id = ?");
        statement.setInt(1, value);
        statement.setInt(2, playerId);
        statement.executeUpdate();
    }
    
    //this function return all players in the database in observable list to put it in table view in GUI
    public static ObservableList<Player> getPlayers() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        ObservableList<Player> players = FXCollections.observableArrayList();
        Statement stmt = con.createStatement();
        String queryString = new String("select * from player");
        ResultSet rs = stmt.executeQuery(queryString);
        while (rs.next()) {
           int id = rs.getInt(1);
           String name= rs.getString(2);
           String email= rs.getString(3);
           String password= rs.getString(4);
           int points = rs.getInt(5);
           int status = rs.getInt(6);
           Player p = new Player(id,name,email,password,points);
           p.setClassify(points);
           p.setStatus(status);
           players.add(p);
        }  
        return players;
    }    
    //this function created to add game between two players in data base
    public static void addPlayersGame(int player1Id , int player2Id ,int winnerId) throws SQLException{
        PreparedStatement statement;
        statement = con.prepareStatement("insert into Game(`player1_id`,`player2_id`,`winner_id`,`status`)values(?,?,?,?)");
        statement.setInt(1, player1Id);
        statement.setInt(2, player2Id);
        statement.setInt(3, winnerId);        
        statement.setInt(4, 1);
        statement.executeUpdate();
    }
    // this function is created to change the status of player
     public static void updatePlayerStatus(int playerId, int statusValue) throws SQLException {
        PreparedStatement statement;
        statement = con.prepareStatement("update player set player_status= ? where id = ? ");
               
        statement.setInt(1, statusValue);
        statement.setInt(2, playerId);
        statement.executeUpdate();
    }
    
    // this function is created to validate email email must be unique for each user
    public static int valiateEmail(String email)throws SQLException{
        PreparedStatement statement;
        statement = con.prepareStatement("select * from player where BINARY email = ? ");
        statement.setString(1, email);
        ResultSet rs = statement.executeQuery();
        int size =0;
        if (rs != null) 
        {
          rs.last();    
          size = rs.getRow();
          
        } 
        return size;
    }
    
    
     // this function is created to validate username username must be unique for each user
    public static int valiateUsername(String userName)throws SQLException{
        PreparedStatement statement;
        statement = con.prepareStatement("select * from player where BINARY username = ? ");
        statement.setString(1, userName);
        ResultSet rs = statement.executeQuery();
        int size =0;
        if (rs != null) 
        {
          rs.last();    
          size = rs.getRow();
          
        } 
        return size;
    }
    
    
     //this function take player userName and return the status of this player
    public static int getPlayerStatus(String playerUsername) throws SQLException,IndexOutOfBoundsException{
        PreparedStatement statement;
        statement = con.prepareStatement("Select player_status from player where username = ?");
        statement.setString(1, playerUsername);
        ResultSet rs = statement.executeQuery();
        int status=0;
        if(rs.next()){
            status = rs.getInt("player_status");
        }
        
        return status;
    }
    
  // this function is created to change the status of player here tale playerUser name as a parameter
     public static void updatePlayerStatus(String  playerUsername, int statusValue) throws SQLException {
        PreparedStatement statement;
        statement = con.prepareStatement("update player set player_status= ? where username = ? ");
         statement.setInt(1, statusValue);
        statement.setString(2, playerUsername);
        statement.executeUpdate();
    }
     
    //this function take player username and score value and update in the database the score of this player by increase it with the new value
    public static void updatePlayerScore(String playerUsername, int value) throws SQLException {
        PreparedStatement statement;
        statement = con.prepareStatement("update player " +
                "set player_points = (player_points + ? )" + 
                "where username = ?");
        statement.setInt(1, value);
        statement.setString(2, playerUsername);
        statement.executeUpdate();
    }
    
    public static int getPoints(int player_id) throws SQLException{
        PreparedStatement statement;
        statement = con.prepareStatement("Select player_points from player where id = ?");
        statement.setInt(1, player_id);
        ResultSet rs = statement.executeQuery();
        int points=0;
        if(rs.next()){
            points = rs.getInt("player_points");
        }
        
        return points;
        
        
    }
    
  
    
    
}




