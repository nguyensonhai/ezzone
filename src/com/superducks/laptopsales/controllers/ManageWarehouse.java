package com.superducks.laptopsales.controllers;

import com.superducks.laptopsales.Class.ConnectDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class ManageWarehouse {
    public Label label;
    public TilePane tpn;
    public ComboBox cbSearch;
    public TextField txtTo;
    public TextField txtForm;
    public ComboBox<String> cbcategory;
    static Stage mainStage = new Stage();

    public void initialize() {
        showcbSearch();
        String sql = "select * from products";
        showData(sql);
    }

    ArrayList<String> data;

    private void showcbSearch() {
        data = new ArrayList<>();
        ObservableList<String> list = FXCollections.observableArrayList();
        String sql = "Select *from categories ";
        try {
            ResultSet rs = ConnectDatabase.Connect().createStatement().executeQuery(sql);
            while (rs.next()) {
                data.add(rs.getString(1));
                list.add(rs.getString(2));
            }
            list.add("All Products");
            cbcategory.setItems(list);
            cbcategory.setValue("All Products");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showData(String sql) {
        ObservableList<VBox> listIMG = FXCollections.observableArrayList();
        try {
            ResultSet rs = ConnectDatabase.Connect().createStatement().executeQuery(sql);
            while (rs.next()) {
                VBox vBox = new VBox();
                vBox.setAlignment(Pos.CENTER);
                vBox.setPrefWidth(200);
                String pid = rs.getString(1), cid = rs.getString(2);
                ImageView imageView = new ImageView(new Image(rs.getString(6)));
                imageView.setFitWidth(130);
                imageView.setFitHeight(130);
                imageView.setBlendMode(BlendMode.MULTIPLY);
                imageView.setOnMouseClicked(e -> event(pid, cid));
                Label nameProduct = new Label(rs.getString(3));
                Label text = new Label(String.format("%,2d", rs.getInt(7)) + "vnd");
                nameProduct.setMaxWidth(200);
                nameProduct.setMaxHeight(100);
                nameProduct.setAlignment(Pos.CENTER);
                nameProduct.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 14));
                text.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 16));
                int remaining = 0, sold = 0;
                String sqlFind = "select * from warehouse where product_id=" + pid +";";
                ResultSet rst = Objects.requireNonNull(ConnectDatabase.Connect()).createStatement().executeQuery(sqlFind);
                if(rst.next()) {
                    remaining = rst.getInt("products_remaining");
                    sold = rst.getInt("products_sold");
                }
                Label remaining_sold = new Label("Available: "+remaining+" - Sold: "+sold);
                remaining_sold.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 16));
                vBox.getChildren().add(imageView);
                vBox.getChildren().add(nameProduct);
                vBox.getChildren().add(text);
                vBox.getChildren().add(remaining_sold);
                listIMG.add(vBox);
            }
            tpn.getChildren().clear();
            tpn.getChildren().addAll(listIMG);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void event(String pid, String cid) {
        EditWarehouse.product_id = pid;
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(LoginForm.class.getClassLoader().getResource("com/superducks/laptopsales/fxmls/EditWarehouse.fxml")));
            EditWarehouse.mainStage.setTitle("Edit Products Remaining");
            EditWarehouse.mainStage.setScene(new Scene(root));
            Image icon = new Image("/com/superducks/laptopsales/icons/web_ui_color/compose.png");
            EditWarehouse.mainStage.getIcons().add(icon);
            EditWarehouse.mainStage.setResizable(false);
            EditWarehouse.mainStage.showAndWait();
            if(EditWarehouse.change) {
                String sql = "";
                int index = cbcategory.getSelectionModel().getSelectedIndex();
                if(cbcategory.getValue().equals("All Products")) {
                    sql = "select * from products";
                }
                else
                    sql = "SELECT * FROM products where category_id like'%" + data.get(index) + "%'";
                showData(sql);
                EditWarehouse.change = false;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void Search(MouseEvent mouseEvent) {
        String sql = "SELECT * FROM products where price between '" + Integer.parseInt(txtForm.getText()) + "' and '" + Integer.parseInt(txtTo.getText()) + "'";
        showData(sql);
    }

    public void CbCategoryClick(ActionEvent actionEvent) {
        String sql = "";
        int index = cbcategory.getSelectionModel().getSelectedIndex();
        if(cbcategory.getValue().equals("All Products")) {
            sql = "select * from products";
        }
        else
            sql = "SELECT * FROM products where category_id like'%" + data.get(index) + "%'";
        showData(sql);
    }

    public void btnThemPie(MouseEvent mouseEvent) {

    }
}
