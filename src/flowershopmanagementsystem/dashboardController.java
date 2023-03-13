/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flowershopmanagementsystem;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Jean_Nico .T
 */
public class dashboardController implements Initializable {

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button availableFlowers_addBtn;

    @FXML
    private Button availableFlowers_btn;

    @FXML
    private Button availableFlowers_clearBtn;

    @FXML
    private TableColumn<flowersData, String> availableFlowers_col_flowerID;

    @FXML
    private TableColumn<flowersData, String> availableFlowers_col_flowerName;

    @FXML
    private TableColumn<flowersData, String> availableFlowers_col_prince;

    @FXML
    private TableColumn<flowersData, String> availableFlowers_col_status;

    @FXML
    private Button availableFlowers_deleteBtn;

    @FXML
    private TextField availableFlowers_flowerID;

    @FXML
    private TextField availableFlowers_flowerName;

    @FXML
    private AnchorPane availableFlowers_form;

    @FXML
    private ImageView availableFlowers_imageView;

    @FXML
    private Button availableFlowers_impotBtn;

    @FXML
    private TextField availableFlowers_prince;

    @FXML
    private TextField availableFlowers_search;

    @FXML
    private TableView<flowersData> availableFlowers_tableView;

    @FXML
    private ComboBox<?> availableFlowers_status;

    @FXML
    private Button availableFlowers_updateBtn;

    @FXML
    private Button close;

    @FXML
    private Label home_availableFlowers;

    @FXML
    private Button home_btn;

    @FXML
    private BarChart<?, ?> home_chart;

    @FXML
    private AnchorPane home_form;

    @FXML
    private Label home_totalCustomers;

    @FXML
    private Label home_totalIncome;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button minimize;

    @FXML
    private Button purchase_btn;

    @FXML
    private TableColumn<?, ?> purchase_col_flowerID;

    @FXML
    private TableColumn<?, ?> purchase_col_flowerName;

    @FXML
    private TableColumn<?, ?> purchase_col_prince;

    @FXML
    private TableColumn<?, ?> purchase_col_quantity;

    @FXML
    private ComboBox<?> purchase_flowerID;

    @FXML
    private ComboBox<?> purchase_flowerName;

    @FXML
    private AnchorPane purchase_form;

    @FXML
    private Button purchase_playBtn;

    @FXML
    private Spinner<?> purchase_quantity;

    @FXML
    private TableView<?> purchase_tableview;

    @FXML
    private Label purchase_total;

    @FXML
    private Label username;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private Image image;

    public void availableFlowersAdd() {

        String sql = "INSERT INTO flowers (flower_id, name, status, price, image, date)"
                + "VALUES(?,?,?,?,?,?)";

        connect = database.connectDb();

        try {

            Alert alert;

            if (availableFlowers_flowerID.getText().isEmpty()
                    || availableFlowers_flowerName.getText().isEmpty()
                    || availableFlowers_status.getSelectionModel().getSelectedItem() == null
                    || availableFlowers_prince.getText().isEmpty()
                    || getData.path == null || getData.path == "") {

                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {

                // CHECK IF THE FLOWER ID IS ALREADY EXIST
                String checkData = "SELECT flower_id FROM flowers WHERE flower_id = '"
                        + availableFlowers_flowerID.getText() + "'";

                statement = connect.createStatement();
                result = statement.executeQuery(checkData);

                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Flowe ID: " + availableFlowers_flowerID.getText() + "Was already exist!");
                    alert.showAndWait();
                } else {

                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, availableFlowers_flowerID.getText());
                    prepare.setString(2, availableFlowers_flowerName.getText());
                    prepare.setString(3, (String) availableFlowers_status.getSelectionModel().getSelectedItem());
                    prepare.setString(4, availableFlowers_prince.getText());

                    String uri = getData.path;
                    uri = uri.replace("\\", "\\\\");
                    prepare.setString(5, uri);

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    prepare.setString(6, String.valueOf(sqlDate));

                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Succeccfully Added!");
                    alert.showAndWait();

                    //SHOW UPDATE TABLEVIEW
                    availableFlowersShowListData();

                    // CLEAR ALL FIELDS
                    availableFlowersClear();

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void availableFlowersUpdate() {

        String uri = getData.path;
        uri = uri.replace("\\", "\\\\");

        String sql = "UPDATE flowers SET name = '"
                + availableFlowers_flowerName.getText() + "', status = '"
                + availableFlowers_status.getSelectionModel().getSelectedItem() + "', price = '"
                + availableFlowers_prince.getText() + "', image = '"
                + uri + "' WHERE flower_id = '" + availableFlowers_flowerID.getText() + "'";

        connect = database.connectDb();

        try {

            Alert alert;

            if (availableFlowers_flowerID.getText().isEmpty()
                    || availableFlowers_flowerName.getText().isEmpty()
                    || availableFlowers_status.getSelectionModel().getSelectedItem() == null
                    || availableFlowers_prince.getText().isEmpty()
                    || getData.path == null || getData.path == "") {

                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to Update Flower ID: " + availableFlowers_flowerID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {

                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    //SHOW UPDATE TABLEVIEW
                    availableFlowersShowListData();

                    // CLEAR ALL FIELDS
                    availableFlowersClear();

                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void availableFlowersDelete() {

        String sql = "DELETE FROM flowers WHERE flower_id = '"
                + availableFlowers_flowerID.getText() + "'";

        connect = database.connectDb();

        try {
            
            Alert alert;

            if (availableFlowers_flowerID.getText().isEmpty()
                    || availableFlowers_flowerName.getText().isEmpty()
                    || availableFlowers_status.getSelectionModel().getSelectedItem() == null
                    || availableFlowers_prince.getText().isEmpty()
                    || getData.path == null || getData.path == "") {

                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to Delete Flower ID: " + availableFlowers_flowerID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {

                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    //SHOW UPDATE TABLEVIEW
                    availableFlowersShowListData();

                    // CLEAR ALL FIELDS
                    availableFlowersClear();

                }

            }
            
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void availableFlowersClear() {

        availableFlowers_flowerID.setText("");
        availableFlowers_flowerName.setText("");
        availableFlowers_status.getSelectionModel().clearSelection();
        availableFlowers_prince.setText("");
        getData.path = "";

        availableFlowers_imageView.setImage(null);

    }

    String listStatus[] = {"Available", "Not Available"};

    public void availableFlowersStatus() {

        List<String> listS = new ArrayList<>();

        for (String data : listStatus) {

            listS.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listS);
        availableFlowers_status.setItems(listData);

    }

    public void availableFlowersInsertImage() {

        FileChooser open = new FileChooser();
        open.setTitle("Open Image File");
        open.getExtensionFilters().add(new ExtensionFilter("Image File", "*jpg", "*png"));

        File file = open.showOpenDialog(main_form.getScene().getWindow());

        if (file != null) {

            getData.path = file.getAbsolutePath();

            image = new Image(file.toURI().toString(), 116, 139, false, true);
            availableFlowers_imageView.setImage(image);

        }

    }

    public ObservableList<flowersData> availableFlowersListData() {

        ObservableList<flowersData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM flowers";

        connect = database.connectDb();

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            flowersData flower;

            while (result.next()) {
                flower = new flowersData(result.getInt("flower_id"),
                        result.getString("name"), result.getString("status"),
                        result.getDouble("price"), result.getString("image"),
                        result.getDate("date"));

                listData.add(flower);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return listData;
    }

    private ObservableList<flowersData> availableFlowersList;

    public void availableFlowersShowListData() {
        availableFlowersList = availableFlowersListData();

        availableFlowers_col_flowerID.setCellValueFactory(new PropertyValueFactory<>("flowerId"));
        availableFlowers_col_flowerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        availableFlowers_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        availableFlowers_col_prince.setCellValueFactory(new PropertyValueFactory<>("price"));

        availableFlowers_tableView.setItems(availableFlowersList);
    }
//=================================

    
    public void availableFlowersSearch(){
    
        FilteredList<flowersData> filter = new FilteredList<>(availableFlowersList, e -> true);
        
        availableFlowers_search.textProperty().addListener((observable, oldValue, newValue) ->{
        
            filter.setPredicate(predicateFlowerData ->{
                
                if(newValue.isEmpty() || newValue == null){
                
                    return true;
                }
                
                String searchkey = newValue.toLowerCase();
                
                if(predicateFlowerData.getFlowerId().toString().contains(searchkey)){
                
                    return true;
                    
                }else if(predicateFlowerData.getName().toString().contains(searchkey)){
                
                    return true;
                }else if(predicateFlowerData.getStatus().contains(searchkey)){
                
                    return true;
                }else if(predicateFlowerData.getPrice().toString().contains(searchkey)){
                
                    return true;
                }else return false;
                
//                return false;
                
            });
            
        });
        
        SortedList<flowersData> sortList = new SortedList<>(filter);
        
        sortList.comparatorProperty().bind(availableFlowers_tableView.comparatorProperty());
        
        availableFlowers_tableView.setItems(sortList);
        
        
    }
    
    
    public void availableFlowersSelect() {

        flowersData flower = availableFlowers_tableView.getSelectionModel().getSelectedItem();
        int num = availableFlowers_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        availableFlowers_flowerID.setText(String.valueOf(flower.getFlowerId()));
        availableFlowers_flowerName.setText(flower.getName());
        availableFlowers_prince.setText(String.valueOf(flower.getPrice()));

        getData.path = flower.getImage();

        String uri = "file:" + flower.getImage();

        image = new Image(uri, 116, 139, false, true);
        availableFlowers_imageView.setImage(image);
//        getData.path = flower.getImage();
    }
//    ===========================

//    ====================================
    public void displayUsername() {

        String user = getData.username;
        username.setText(user.substring(0, 1).toUpperCase() + user.substring(1));
    }
//   ============================================== 

    //========================= FONCTION CHANGE FENETRE =======================
    public void switchForm(ActionEvent event) {

        if (event.getSource() == home_btn) {

            home_form.setVisible(true);
            availableFlowers_form.setVisible(false);
            purchase_form.setVisible(false);

            home_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #d3133d, #a4262f)");
            availableFlowers_btn.setStyle("-fx-background-color: transparent");
            purchase_btn.setStyle("-fx-background-color: transparent");

        } else if (event.getSource() == availableFlowers_btn) {

            home_form.setVisible(false);
            availableFlowers_form.setVisible(true);
            purchase_form.setVisible(false);

            availableFlowers_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #d3133d, #a4262f)");
            home_btn.setStyle("-fx-background-color: transparent");
            purchase_btn.setStyle("-fx-background-color: transparent");

            // TO SHOW THE UPDATE TABLEVIEW ONCE YOU CLICKED THE AVAILABLE FLOWERS BUTTON
            availableFlowersShowListData();
            availableFlowersStatus();
            availableFlowersSearch();

        } else if (event.getSource() == purchase_btn) {

            home_form.setVisible(false);
            availableFlowers_form.setVisible(false);
            purchase_form.setVisible(true);

            purchase_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #d3133d, #a4262f)");
            availableFlowers_btn.setStyle("-fx-background-color: transparent");
            home_form.setStyle("-fx-background-color: transparent");

        }
    }

    // ================ FIN FONCTION CHANGE FENETRE =================
    private double x = 0;
    private double y = 0;

    public void logout() {

        try {

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {

                //HIDE YOUR DASHBOARD FORM
                logoutBtn.getScene().getWindow().hide();

                // LINK YOUR LOGIN FORM
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                Scene scene = new Scene(root);
                Stage stage = new Stage();

                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getSceneX() - x);
                    stage.setY(event.getSceneY() - y);

                    stage.setOpacity(.8);
                });

                root.setOnMouseReleased((MouseEvent event) -> {
                    stage.setOpacity(1);
                });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //
    public void close() {
//        System.exit(0);
        logout();
    }

    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayUsername();

        availableFlowersShowListData();
        availableFlowersStatus();
    }

}
