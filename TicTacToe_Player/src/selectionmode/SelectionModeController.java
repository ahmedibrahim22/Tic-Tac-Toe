/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selectionmode;

import Helper_Package.InsideXOGame;
import Helper_Package.Player;
import Helper_Package.RecordedMessages;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import login.loginController;
import tictactoe_player.TicTacToe_Player;

/**
 *
 * @author Ahmed Ibrahim
 */
public class SelectionModeController implements Initializable {
    PrintStream PSFromController;
    @FXML
    private Text logedInUserName;
    @FXML
    
    

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PSFromController = TicTacToe_Player.ps;
        logedInUserName.setText(loginController.username);
        
    }    

    @FXML
    private void singlePlayer(ActionEvent event)throws IOException {
        FXMLLoader levelSelection=new FXMLLoader();
        levelSelection.setLocation(getClass().getResource("/selectlevel/SelectLevel.fxml"));
        Parent  levelSelectionroot = levelSelection.load();
        Scene scenelevelSelection = new Scene(levelSelectionroot);
        Stage signupstage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        signupstage.hide(); 
        signupstage.setScene(scenelevelSelection);
        signupstage.show();  
    }

    @FXML
    private void multiplayer(ActionEvent event) {
        Player player = new Player();
        player.setUserName(loginController.username); 
        InsideXOGame xoMessage =new InsideXOGame (RecordedMessages.GET_PLAYERS,player);
        Gson g = new Gson();
        String s = g.toJson(xoMessage);
        PSFromController.println(s);
    }

    @FXML
    private void logout(ActionEvent event) {
        try
        {
            Player player=new Player();
            player.setUserName(loginController.username);
            InsideXOGame xoMessage =new InsideXOGame (RecordedMessages.LOGOUT,player);
            Gson g = new Gson();
            String s = g.toJson(xoMessage);
            PSFromController.println(s);
            FXMLLoader signinpage=new FXMLLoader();
            signinpage.setLocation(getClass().getResource("/signin/signIn.fxml"));
            Parent  signinpageroot = signinpage.load();
            Scene scenesignin = new Scene( signinpageroot);
            Stage signinstage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            signinstage.hide();
            signinstage.setScene(scenesignin);
            signinstage.show();             
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    @FXML
    private void minimize(ActionEvent event) {
        ((Stage)((Button)event.getSource()).getScene().getWindow()).setIconified(true);
    }

    @FXML
    private void exit(ActionEvent event) {
        Player player=new Player();
        player.setUserName(loginController.username);
        InsideXOGame xointerface =new InsideXOGame (RecordedMessages.LOGOUT,player);
        Gson g = new Gson();
        String s = g.toJson(xointerface);
        PSFromController.println(s);        
        Platform.exit();
    }
}
