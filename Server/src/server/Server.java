/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import Helper_Package.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import static com.oracle.jrockit.jfr.ContentType.Class;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.parser.JSONParser;



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
    }
    public ServerSocket getServerSocket()
    {
        return this.sSocket;
    }
    public void startConnection()
    {
        
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
            System.out.println("Error while create new server socket");
            ex.getMessage();
        }
        finally{
            try {
                sSocket.close();
                 System.out.println("closing sever sockeet");
                
            } catch (IOException ex) {
                System.out.println("Error while closing sever socket");
                ex.getMessage();
            }
        }
    }
}
