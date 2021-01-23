/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playerlist;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 *
 * @author Ahmed Ibrahim
 */
public class PlayerListController implements Initializable {
    
    private Label label;
    @FXML
    private TableView<?> playersTable;
    @FXML
    private TableColumn<?, ?> userNameCol;
    @FXML
    private TableColumn<?, ?> scoreCol;
    @FXML
    private TableColumn<?, ?> statusCol;
    @FXML
    private TableColumn<?, ?> isPlayingCol;
    
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    

    @FXML
    private void refreshList(ActionEvent event) {
    }

    @FXML
    private void GetNames(ActionEvent event) {
    }

    @FXML
    private void back(ActionEvent event) {
    }
    
}
