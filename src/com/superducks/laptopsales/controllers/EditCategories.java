package com.superducks.laptopsales.controllers;

import com.superducks.laptopsales.Class.AlertMessage;
import com.superducks.laptopsales.Class.ConnectDatabase;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EditCategories {
    static boolean changed = false;
    public static String categoryID;
    public static String categoryName;
    static Stage mainStage = new Stage();
    public static int chage;
    public TextField txtcategoryID;
    public TextField txtcategoryName;
    public ImageView btnAdd;
    public ImageView btnOut;
    public ImageView btnAccept;
    public ImageView btnNonAdd;
    public ImageView btnNonAccept;

    public void initialize(){
        if(chage==1){
            btnAccept.setVisible(true);
            showDataWithEdit();
        }else{
            txtcategoryID.setEditable(true);
            btnAdd.setVisible(true);
        }
    }

    private void showDataWithEdit() {
        txtcategoryID.setText(categoryID);
        txtcategoryName.setText(categoryName);
    }


    public void btnAcceptClicked(MouseEvent mouseEvent) {
        if(AlertMessage.showAlertYesNo()) {
            String sql = "UPDATE categories SET name = '" + txtcategoryName.getText() + "' WHERE id = '" + txtcategoryID.getText() + "'";
            try {
                ConnectDatabase.Connect().prepareStatement(sql).executeUpdate();
                AlertMessage.showAlert("Updated all information", "tick");
                changed = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnAddClicked(MouseEvent mouseEvent) {
        String sqlCheck = "select * from categories where id ='" +txtcategoryID.getText() +"';";
        try {
            ResultSet rs = ConnectDatabase.Connect().createStatement().executeQuery(sqlCheck);
            if(!rs.next()) {
                if(AlertMessage.showAlertYesNo()) {
                    String sql = "INSERT INTO categories(id,name) VALUES ('" + txtcategoryID.getText() + "', '" + txtcategoryName.getText() + "')";
                    try {
                        ConnectDatabase.Connect().prepareStatement(sql).executeUpdate();
                        AlertMessage.showAlert("Added new category", "tick");
                        txtcategoryID.setText("");
                        txtcategoryName.setText("");
                        changed = true;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            else
                AlertMessage.showAlert("This Category ID already existed, please choose another", "error");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnOutClicked(MouseEvent mouseEvent) {
        mainStage.close();
    }
}

