/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package doggroomingdb;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import java.sql.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
/**
 * @author Jane Perez
 */
public class DogGroomingDB extends Application  {
    Stage window;
    private ObservableList<ObservableList> data;
    private TableView tableview;  
    Scene scene1, scene2;
    Button buttonEntry;
    Button buttonShow;
    Button goBack;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)  {
        // TODO code application logic here
        launch(args);
    }
    // 
    public void connectData() {
      
       Connection myConn = null;
       Statement myStat = null;
       ResultSet myRs = null;
       String w = "02Pjb02!01APbj190202!A$".substring(11, 21);
        data = FXCollections.observableArrayList();
        try {
            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dog_grooming","root", w);
           
           myStat = myConn.createStatement();
           
           myRs = myStat.executeQuery("select * from appointments");
 
            // Creating dynamic table
            for (int i = 0; i < myRs.getMetaData().getColumnCount(); i++) {
                
                final int j = i;
                TableColumn col = new TableColumn(myRs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
 
                tableview.getColumns().addAll(col);
            }
 
          
            while (myRs.next()) {
                
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= myRs.getMetaData().getColumnCount(); i++) {
                    
                    row.add(myRs.getString(i));
                }
                System.out.println("Row [1] added " + row);
                data.add(row);
 
            }
 
            // Add data to tableview
            tableview.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }
    
    
    @Override
    public void start(Stage primaryStage) throws SQLException{
        window = primaryStage;
        String p= "02Pjb02!01APbj190202!A$".substring(11, 21);
        
        
       Connection myConn = null;
       Statement myStat = null;
       ResultSet myRs = null;
       
       tableview = new TableView();
        
       
       Label titleLabel= new Label("Jane's Dog Grooming"); 
        Label DogNameLabel=new Label("Dog Name: ");
        Label StylistNameLabel=new Label("Chosen Stylist: ");
        Label serviceLabel=new Label("Sevice Needed: ");
        Label timeLabel=new Label("Time At: ");
        Label dateLabel=new Label("Date On: ");
        Label roomLabel=new Label("Room Number: ");
        TextField timeText=new TextField();
        TextField dateText=new TextField();
        
        ChoiceBox<String> dogNameBox = new ChoiceBox<>();
        try{
           myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dog_grooming","root", p);
           
           myStat = myConn.createStatement();
           
           myRs = myStat.executeQuery("select dog_name from dog_info");
           // Add data to combobox
           while(myRs.next()){
               dogNameBox.getItems().addAll(myRs.getString("dog_name"));
           }
       } catch(Exception exc) {
           exc.printStackTrace();
       } finally {
           if(myRs != null) {
               myRs.close();
           }
           if(myStat != null){
               myStat.close();
           }
       }
        
        ChoiceBox<String> stylistNameBox = new ChoiceBox<>();
        try{
           myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dog_grooming","root", p);
           
           myStat = myConn.createStatement();
           
           myRs = myStat.executeQuery("select stylist_name from stylist_employee");
           // Add data to combobox
           while(myRs.next()){
               stylistNameBox.getItems().addAll(myRs.getString("stylist_name"));
           }
       } catch(Exception exc) {
           exc.printStackTrace();
       } finally {
           if(myRs != null) {
               myRs.close();
           }
           if(myStat != null){
               myStat.close();
           }
       }
       
        ChoiceBox<String> serviceBox = new ChoiceBox<>();
        serviceBox.getItems().addAll("Standard Service Bath", "Gold Package", "Platinum Package", "Diamond Package");
        
        ChoiceBox<String> roomBox = new ChoiceBox<>();
        roomBox.getItems().addAll("101","102","103","104","105");
        
        buttonEntry = new Button("Submit Appointment");
        buttonShow = new Button("Show Appointments made");
        goBack = new Button("Back");
        
        buttonEntry.setOnAction(new EventHandler<ActionEvent>(){
           
            @Override
            public void handle(ActionEvent event)
            {
       
            try{
          Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dog_grooming","root", p);
           
           Statement myStat = myConn.createStatement();
           //Query to insert data of textfields and comboBoxs
            String sql = "insert into appointments (dog_name,stylist_name,service_type,time,date,room_number) values"
                   + "('" + dogNameBox.getValue() + "','"+ stylistNameBox.getValue()+ "','"+ serviceBox.getValue()+"','"+ timeText.getText() +"','"+dateText.getText()+"', '"+ roomBox.getValue() +"')";
           String result = sql;
           //excute query
           myStat.executeUpdate(result);
           
           JOptionPane.showMessageDialog(null, "Submitted Successfully");
       } catch(Exception exc) {
           exc.printStackTrace();
       } 
       
            }
        });
        
        buttonShow.setOnAction(e->{
              //Refresh Tablebview
                connectData();
                window.setScene(scene2);
        });
        
        GridPane grid = new GridPane();
                grid.add(DogNameLabel,0,0);
                grid.add(dogNameBox, 1,0);
                grid.add(StylistNameLabel,0,1);
                grid.add(stylistNameBox,1,1);
                grid.add(serviceLabel,0,2);
                grid.add(serviceBox,1,2);
                grid.add(timeLabel,0,3);
                grid.add(timeText,1,3);
                grid.add(dateLabel,0,4);
                grid.add(dateText,1,4);
                grid.add(roomLabel,0,5);
                grid.add(roomBox,1,5);
                
        HBox buttonlayout = new HBox(20,buttonEntry, buttonShow);
       
        VBox layout1 = new VBox(20,titleLabel,grid, buttonlayout);
        scene1 = new Scene(layout1, 500, 500);
        
        layout1.setAlignment(Pos.CENTER);
        grid.setAlignment(Pos.CENTER);
        buttonlayout.setAlignment(Pos.CENTER);
        
        goBack.setOnAction(e->{
            //Clear All fields
            timeText.clear();
            dateText.clear();
            stylistNameBox.setValue(null);
            serviceBox.setValue(null);
            roomBox.setValue(null);
            dogNameBox.setValue(null);
            
            window.setScene(scene1);
        });
        
        VBox layout2 = new VBox(20, goBack,tableview);
        scene2 = new Scene(layout2, 700,500);
        
        window.setScene(scene1);
        window.setTitle("Jane's Dog Grooming");
        window.show();
    }
}
