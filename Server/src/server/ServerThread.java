/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import Helper_Package.InsideXOGame;
import Helper_Package.Player;
import Helper_Package.RecordedMessages;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Database;

/**
 *
 * @author ibrahim
 */

public class ServerThread extends Thread
{
   private final Socket socket;
   private InputStream is;
   private OutputStream os;
   private PrintWriter writer;
   static Vector<ServerThread> clientsVector =new Vector <>();
   public ServerThread(Socket s)
   {
     this.socket=s;
   }
    @Override
    public void run()
    {
       try {
           is=socket.getInputStream();
           BufferedReader reader = new BufferedReader(new InputStreamReader(is));
           os=socket.getOutputStream();
           writer=new PrintWriter(os,true);
           clientsVector.add(this);
           String message;
 
            while(true) {
                message = reader.readLine();
                if(!message.isEmpty())
                {
                    try {
                        jsonMessageHandler(message);
                    } catch (ParseException ex) {
                        Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
//                broatCast(message);
            } 
       } catch (IOException ex) {
           ex.getMessage();
       }
       finally{
           try {
               socket.close();
               is.close();
               os.close();
               
               System.out.println("close sever socket");
           } catch (IOException ex) {
               System.out.println("Error while closing socket connection from server");
               ex.getMessage();
           }
       }
    }
   
    
    
    private void jsonMessageHandler(String data) throws ParseException {
         
         Gson gson=new Gson();
         InsideXOGame msgObject=gson.fromJson(data,InsideXOGame.class);
          Player p;
          String s ;
        switch (msgObject.getTypeOfOperation().toString()) {
            case RecordedMessages.LOGIN:
                p=msgObject.getPlayer();
                s = gson.toJson(p);
                
                //send string to database login fun
                //send acknowledgement to player true or false
                if(true)
                {
                writer.println(RecordedMessages.LOG_IN_ACCEPTED);
                }
                if(false)
                {
//                writer.println(RecordedMessages.LOG_IN_REJECTED);adding rejected login to recorded message
                }
                
                break;
            case RecordedMessages.SIGNUP:
                 p=msgObject.getPlayer();
                 s = gson.toJson(p);
                //send string to database signup fun and return true or false
                 //send acknowledgement to player 
                 if(true)
                 {
                 writer.println(RecordedMessages.SIGN_UP_ACCEPTED);
                 }
                 if(false)
                 {
                     writer.println(RecordedMessages.SIGN_UP_REJECTED);
                 }
                break;
            case RecordedMessages.SINGLE_MODE_GAME_FINISHED:
                  p=msgObject.getPlayer();
                  s = gson.toJson(p);
                  //send string to database set score fun
               
                break;
        
        }

    }
   private void broatCast(String msg)
   {
     for(ServerThread ch : ServerThread.clientsVector)
     {
        ch.writer.println("Server: " +msg);
     }
   }
}