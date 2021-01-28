/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signup;

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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tictactoe_player.TicTacToe_Player;

/**
 * FXML Controller class
 *
 * @author 20100
 */
public class SignUpController implements Initializable {
    PrintStream PSFromController;

    @FXML
    private Label errorMsg;
    @FXML
    private Text checkpass;
    @FXML
    private Text confirmpass;
    @FXML
    private Text checkuname;
    @FXML
    private Text checkemail;
    @FXML
    private TextField username;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmationpassword;

    public boolean check1()
    {
        boolean flag = true;
        checkuname.setVisible(false);
        checkpass.setVisible(false);
        checkemail.setVisible(false);
        confirmpass.setVisible(false);
        if(username.getText().equals(""))
        {
            checkuname.setVisible(true);
            flag = false;
        }
        if(password.getText().equals(""))
        {
            checkpass.setVisible(true); 
            flag = false;
        }
        if(email.getText().equals(""))
        {
            checkemail.setVisible(true); 
            flag = false;
        }
        if(((confirmationpassword.getText()).equals(password.getText())) == false)
        {
            confirmpass.setVisible(true); 
            flag = false;
        }

        return flag;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PSFromController = TicTacToe_Player.ps;
    }
    
    

    @FXML
    private void register(ActionEvent event)throws IOException {
        boolean b=check1();
       Player player=new Player(username.getText(),password.getText(),email.getText());
            InsideXOGame xointerface =new InsideXOGame (RecordedMessages.SIGNUP,player);
            Gson g = new Gson();
            String s = g.toJson(xointerface);
            PSFromController.println(s);  
    }
    
    
    /*need to be done**************/
    @FXML
    private void back(ActionEvent event) {
    }
    
     @FXML
    private void minimize(ActionEvent event) {
        ((Stage)((Button)event.getSource()).getScene().getWindow()).setIconified(true);
    }

    @FXML
    private void exit(ActionEvent event) {
        Platform.exit();
    }

}

 
