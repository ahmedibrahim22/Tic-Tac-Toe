/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author FOX
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private TableView<?> tableView;
    @FXML
    private TableColumn<?, ?> userName;
    @FXML
    private TableColumn<?, ?> score;
    @FXML
    private TableColumn<?, ?> status;
    @FXML
    private Button turnONBtn;
    @FXML
    private Button turnOFFBtn;

    Server myServer;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    
    @FXML
    private void startServerConnection(ActionEvent event) {
            myServer=new Server(3333);
                myServer.startConnection();
            
    }

    @FXML
    private void stopServerConnection(ActionEvent event) {
         try {
            myServer.getServerSocket().close();
        } catch (IOException ex) {
            System.out.println("Cannot close the server");
            ex.getMessage();
        }
    }

    
}
