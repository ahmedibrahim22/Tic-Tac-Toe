/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tictactoe_player.TicTacToe_Player;

/**
 *
 * @author Ahmed Ibrahim
 */
public class loginController implements Initializable {
    public static String username;
    public static boolean myTurn = false;
    String password;
    @FXML
        Label errorMsg;
    @FXML
        TextField loginusername;
    @FXML
        PasswordField loginpassword;
    @FXML
        Button login;
    @FXML
        Text checkusername;
    @FXML
        Text checkpassword;
    @FXML
        Button signup;
        Stage window;
        PrintStream PSFromController;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PSFromController = TicTacToe_Player.ps;
    }  
     

    @FXML
    private void login(ActionEvent event) {
        username = loginusername.getText();
        password = loginpassword.getText();
       if(username.equals("") || password.equals(""))
       {
            if (username.equals(""))
            {
                checkusername.setVisible(true);
            }
            else
            {
                checkusername.setVisible(false);
            }
            if(password.equals(""))
            {
                checkpassword.setVisible(true);
            }
            else
            {
                checkpassword.setVisible(false);
            }
       }
       else
       {
            checkusername.setVisible(false);
            checkpassword.setVisible(false);
            Player player=new Player();
            player.setUserName(username);
            player.setPassword(password);
            InsideXOGame xoMessage =new InsideXOGame (RecordedMessages.LOGIN,player);
            Gson g = new Gson();
            String s = g.toJson(xoMessage);
            PSFromController.println(s);
       }
    }

    @FXML
    void signup(ActionEvent event)throws IOException {
        FXMLLoader signuppage=new FXMLLoader();
        signuppage.setLocation(getClass().getResource("/signup/SignUp.fxml"));
        Parent  signuppageroot = signuppage.load();
        Scene scenesignup = new Scene(signuppageroot);
        Stage signupstage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        signupstage.hide();
        signupstage.setScene(scenesignup);
        signupstage.show();
    }
    
    private void minimize(ActionEvent event) {
        ((Stage)((Button)event.getSource()).getScene().getWindow()).setIconified(true);
    }

    private void exit(ActionEvent event) {
        Platform.exit();
    } 
}
