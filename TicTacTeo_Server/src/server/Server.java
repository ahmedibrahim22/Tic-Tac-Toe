/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import Helper_Package.*;
//import Database.Database;
import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.runtime.ParserException;



/**
 *
 * @author Noura Houssien
 */
public class Server {
    private final int port;
    private ServerSocket sSocket;
    Server(int port)
    {
       this.port=port;
       startServer();
    }
  
    private void startServer()
    {
        new Thread(() -> {
        System.out.println("server running...");
        try {
            sSocket=new ServerSocket(port);
            while (true) {                
                Socket socket= sSocket.accept();
                System.out.println("New client connected ");
                //class server thread to create new thread with each new client
                new ServerThread(socket).start();
            }
           
        } catch (IOException ex) {
            System.out.println("Error in server socket");
            ex.getStackTrace();
        }
        }).start();
    }
    
    public void closeServer()
    {
            try {
                sSocket.close();
                System.out.println("sever closed.");
                
            } catch (IOException ex) {
                System.out.println("Error while closing sever");
                ex.getStackTrace();
            }
    }
}
class ServerThread extends Thread
{
   private final Socket socket;
   private DataInputStream dis;
   private PrintStream ps;
   private Player newPlayer;
//   private Database db;
   static Vector<ServerThread> playersVector =new Vector <>();
   
   public ServerThread(Socket s)
   {
     this.socket=s;
   }
    @Override
    public void run()
    {
       try {
           dis = new DataInputStream(socket.getInputStream());
           ps = new PrintStream(socket.getOutputStream(),true);
           playersVector.add(this);
           String message;
 
            while(true) {
                message = dis.readLine();
                if(!message.isEmpty())
                {
                    try {
                        jsonMessageHandler(message);
                    } catch (ParseException ex) {
                        ex.getStackTrace();
                        System.out.println("error while call json message hundler ");
                    }
                }
            } 
       } catch (IOException ex) {
           ex.getStackTrace();
           System.out.println("server can not connect with client");
           try {
               socket.close();
               dis.close();
               ps.close();
               playersVector.remove(this);
               newPlayer=new Player();
               newPlayer.setStatus(false);
//               db.updatePlayerStatus(newPlayer.getUserName(),0); //update status of player to be offline
               System.out.println("player is leaved and become offline");
           } catch (IOException e) {
               System.out.println("Error while closing socket connection from server");
               e.getStackTrace();
           }
       }
    }
   
    
    
    
    private void jsonMessageHandler(String data) throws ParseException {
         
         Gson gson=new Gson();
         InsideXOGame msgObject=gson.fromJson(data,InsideXOGame.class);
        
         String s ;
        switch (msgObject.getTypeOfOperation()) {
            case RecordedMessages.LOGIN:
                handelLogInRequest(msgObject);
                break;
            case RecordedMessages.SIGNUP:
                handelSinUpRequest(msgObject);
                break;
            default:
                
        
        }
      
    }
    
    
    
    
   private void  handelLogInRequest(InsideXOGame objMsg)
   {
      
       Gson g=new Gson();
       Player player;
       String userName,password;
       int playerId=0;
       player = objMsg.getPlayer();
       userName=player.getUserName();
       password=player.getPassword();
//     playerId=db.login(userName,password); //this function will return -1 if login faild
       if(playerId!=-1)
       {
//         db.updatePlayerStatus(playerId,1);
           newPlayer.setStatus(true);
           newPlayer.setUserName(userName);
           newPlayer.setPassword(password);
           objMsg.setOperationResult(true);
           objMsg.setTypeOfOperation(RecordedMessages.LOG_IN_ACCEPTED);
           ps.println(g.toJson(objMsg.toString()));
          
       }
       else{
           //should handel in player to receve LOG_IN_REJECTED 
        objMsg.setOperationResult(false);
       }
       
   }
   private void handelSinUpRequest(InsideXOGame objMsg)
   {
       Gson g=new Gson();
       Player player;
       String userName,email,password;
       int successRegister=0;
       player = objMsg.getPlayer();
       userName=player.getUserName();
       email=player.getEmail();
       password=player.getPassword();
       String[] inputData={userName,email,password};
//        successRegister=db.register(inputData); 
       if(successRegister==1)
       {
         objMsg.setOperationResult(true);
         objMsg.setTypeOfOperation(RecordedMessages.SIGN_UP_ACCEPTED);
       }
       else
       {
          objMsg.setOperationResult(false);
          objMsg.setTypeOfOperation(RecordedMessages.SIGN_UP_REJECTED);
       }
       ps.println(g.toJson(objMsg.toString()));
   }
   private void broatCast(String msg)
   {
     for(ServerThread ch : ServerThread.playersVector)
     {
        ch.ps.println("Server: " +msg);
     }
   }
}

