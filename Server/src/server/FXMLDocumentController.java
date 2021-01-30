
package server;

import Database.Database;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    try {
        Database.dbConnect();
    } catch (ClassNotFoundException ex) {
        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
    } catch (SQLException ex) {
        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
    }
            myServer=new Server(5000);
    }

@FXML
private void stopServerConnection(ActionEvent event) {
    try {
        Database.dbDisconnect();
    } catch (SQLException ex) {
        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
    }
         myServer.closeServer();
         Platform.exit();
     
    }

    
}
