/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ibrahim
 */
public class PlayerHandler extends Thread{
    
    private int Id;
    private String username;
    private String email;
    private String password;
    private int points;
    private int status;
    private int opponentId;
    private int gameId;
    private int gameStatus;
    
    static Vector<PlayerHandler> players = new Vector<>();
    DataInputStream dis;
    PrintStream ps;
    
    public PlayerHandler(Socket playerSocket) throws IOException{
        ps = new PrintStream(playerSocket.getOutputStream());
        dis = new DataInputStream(playerSocket.getInputStream());
        players.add(this);
        start();
    }
    
    public void run(){
        while(true){
            String msg;
            try {
                msg = dis.readLine();
                sendToAll(msg);
            } catch (IOException ex) {
                Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void sendToAll(String msg){
        for(PlayerHandler ch : players){
            ch.ps.println(msg);
        }
    }
}
