
package server;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class for server 
GUI
 *
 * @author Noura Houssien
 */
public class FXMLDocumentController 
implements Initializable {

@FXML
private Button turnONBtn;
@FXML
private Button turnOFFBtn;

Server myServer;
   
@Override
public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    
@FXML
private void startServerConnection(ActionEvent event) {
            myServer=new Server(5000);
    }

@FXML
private void stopServerConnection(ActionEvent event) {
         myServer.closeServer();
         Platform.exit();
     
    }

    
}
