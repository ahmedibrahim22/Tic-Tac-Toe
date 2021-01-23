/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe_player;

import Helper_Package.InsideXOGame;
import Helper_Package.RecordedMessages;
import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import login.loginController;


/**
 *
 * @author Ahmed Ibrahim
 */
public class TicTacToe_Player extends Application {
   
    DataInputStream dis;
    public static loginController LI;
    public static PrintStream ps;
    Socket mySocket;
    @Override
    public void start(Stage stage) throws Exception {
        try{
            mySocket = new Socket("127.0.0.1", 5000);
            dis = new DataInputStream(mySocket.getInputStream());
            ps = new PrintStream(mySocket.getOutputStream());
            new Thread(()->{
                while (true){
                    try {
                        String recivedMsg = dis.readLine();
                        System.out.println(recivedMsg);
                        Gson g = new Gson();
                        InsideXOGame xoMessage;
                        xoMessage = g.fromJson(recivedMsg, InsideXOGame.class);
                        
                        //to switch to selection mode scene
                        if(xoMessage.getTypeOfOperation().equals(RecordedMessages.NEW_PLAYER_LOGGED_IN))
                        {
                            Platform.runLater(()->{
                                try {
                                    moveToSelectionScene(stage,xoMessage);
                                } catch (IOException ex) {
                                    System.err.println("coudn't switch");
                                    ex.printStackTrace();
                                }
                            });
                        }
                        
                        //to switch to login scene
                        else if (xoMessage.getTypeOfOperation().equals(RecordedMessages.SIGN_UP_ACCEPTED))
                        {
                            System.err.println("Register here");
                            Platform.runLater(()->{
                            moveToLogInScene(stage);
                            });                           
                        }

                        else if (xoMessage.getTypeOfOperation().equals(RecordedMessages.INVITATION_REJECTED))
                        {
                            
                            loginController.myTurn = false;
                        }
                        
                        //to switch to player with computer scene
                        else if(xoMessage.getTypeOfOperation().equals(RecordedMessages.PLAYING_SINGLE_MODE))
                        {
                            Platform.runLater(()->{
                                try 
                                {
                                    moveToPlayWithComputerScene(stage);
                                } 
                                catch (IOException ex) {
                                    System.err.println("coudn't switch");
                                    ex.printStackTrace();
                                }
                            });
                        }

                    }
                    catch (IOException ex) {
                         try
                         {
                            this.dis.close();
                            ps.close();
                            this.mySocket.close();
                            break;                             
                         }
                         catch (IOException exception)
                         {
                             exception.printStackTrace();
                         }
                    }
                }
            }).start();
        } catch (IOException ex){
            System.err.println("Server Is Off");
            ex.printStackTrace();
        }

        //loader to start login page
        FXMLLoader loginInLoader=new FXMLLoader();
        loginInLoader.setLocation(getClass().getResource("/login/login.fxml"));
        Parent root = loginInLoader.load();
        LI = loginInLoader.getController();
        
        //main root scene which will start firstly 
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Tic Tac Toe Game");
        stage.setResizable(false);
        stage.getIcons().add(new Image("logo.png"));
        stage.show();
    }
    

    
    //**functions to move from GUI to another GUI**//
    //1st function to move to selection mode scene
    void moveToSelectionScene(Stage stage, InsideXOGame xoMessage) throws IOException{
        
        FXMLLoader selectionModeLoader=new FXMLLoader();
        selectionModeLoader.setLocation(getClass().getResource("/selectionmode/SelectionMode.fxml"));
        Parent selectionModeRoot = selectionModeLoader.load();
        Scene selectionModeScene = new Scene(selectionModeRoot);
        stage.hide();
        stage.setScene(selectionModeScene);
        stage.show();
    }
    
    
    //2nd function to move to play with computer scene
    void moveToPlayWithComputerScene(Stage stage)throws IOException{
        FXMLLoader playWithComputerLoader = new FXMLLoader();
        playWithComputerLoader.setLocation(getClass().getResource("/playwithcomputer/PlayWithComputer.fxml"));
        Parent playWithComputerRoot = playWithComputerLoader.load();
        Scene playWithComputerScene = new Scene(playWithComputerRoot);
        stage.hide();
        stage.setScene(playWithComputerScene);
        stage.show();
    }
    
    
    //3rd function to move to log in scene
    void moveToLogInScene(Stage stage){
        try
        {
           FXMLLoader logInLoader = new FXMLLoader();
           logInLoader.setLocation(getClass().getResource("/login/login.fxml"));
           Parent logInRoot = logInLoader.load();
           LI = logInLoader.getController();
           Scene logInScene = new Scene(logInRoot);
           stage.hide();
           stage.setScene(logInScene);
           stage.show();                     
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }   
    }
    

    @Override
    public void stop(){
        System.out.println("Stage is closing");
        
        // Save file
        Platform.exit();

    }
    public static void main(String[] args) {
         Application.launch(args);
    }
    
}
