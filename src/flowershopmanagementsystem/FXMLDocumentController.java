/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flowershopmanagementsystem;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Jean_Nico .T
 */
public class FXMLDocumentController implements Initializable {
 
    
       @FXML
    private Button close;

    @FXML
    private Button loginBtn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;
    
    
    //DATABASE TOOLS
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    
    public void login(){
        String sql = "SELECT * FROM admin WHERE username = ? and password = ?";
        connect = database.coonectDb();
        
        try{
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, username.getText());
            prepare.setString(2, password.getText());
            
            Alert alert;
            if(username.getText().isEmpty() || password.getText().isEmpty()){
                alert = new Alert (AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blan kfield");
                alert.showAndWait();
            }else{
                 if(result.next()){
                     //IF CORRECT USERNAME AND PASSWORD THEN PROCEDE TO DASHBOARD
                     
                     //LINK YOUR DASHBOARD FORM
                     Parent root = FXMLLoader.load(getClass().getResource(""));
                     Stage stage = new Stage();
                     Scene scene = new Scene(root);
                     
                     
                     
                     stage.setScene(scene);
                     stage.show();
                 }else{
                     //IF NOT THEN ERROR MESSAGE WILL APPEAR
                 }
            }
            
        }catch(Exception e) {e.fillInStackTrace();}
    }
    
    public void close(){
    System.exit(0);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
