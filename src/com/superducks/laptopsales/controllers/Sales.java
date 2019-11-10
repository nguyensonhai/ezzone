package com.superducks.laptopsales.controllers;

import com.superducks.laptopsales.Class.ConnectDatabase;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Sales {
    public TableView mainTable;
    public ComboBox cbxCategories;
    public ComboBox cbxProducts;
    public ImageView btnAdd;
    public TableColumn clmType;
    public TableColumn clmProducts;
    public TableColumn clmAmount;
    public TableColumn clmPrice;
    public TextField txtAmount;
    ObservableList<ClassSales> dataTable = FXCollections.observableArrayList();

    public void initialize() {
        getDataComboboxCategories();
        getDataComboboxProducts();
    }

    void getDataComboboxCategories() {
        String sql = "select * from categories";
        try {
            ResultSet rs = ConnectDatabase.Connect().createStatement().executeQuery(sql);
            while (rs.next()) {
                cbxCategories.getItems().add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cbxCategories.getSelectionModel().selectFirst();
    }

    void getDataComboboxProducts() {
        String category = cbxCategories.getValue().toString();
        String findCategogyID = "select * from categories where name ='"+category+"';";
        String categoryID = "";
        try {
            ResultSet rst = ConnectDatabase.Connect().createStatement().executeQuery(findCategogyID);
            while (rst.next()) {
                categoryID = rst.getString("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql = "select * from products where category_id ='"+categoryID+"';";
        try {
            ResultSet rs = ConnectDatabase.Connect().createStatement().executeQuery(sql);
            while (rs.next()) {
                cbxProducts.getItems().add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cbxProducts.getSelectionModel().selectFirst();
    }

    public void cbxCategories_Action(ActionEvent actionEvent) {
        if(cbxProducts.getItems().size() > 0)
            cbxProducts.getItems().clear();
        getDataComboboxProducts();
    }

    public void btnAdd_Click(MouseEvent mouseEvent) {
        String type, products;
        Integer amount, price;
        type = cbxCategories.getValue().toString();
        products = cbxProducts.getValue().toString();
        amount = Integer.parseInt(txtAmount.getText());
        dataTable.add(new ClassSales(type, products, amount,0));
        clmType.setCellValueFactory(new PropertyValueFactory<ClassSales, String>("Type"));
        clmProducts.setCellValueFactory(new PropertyValueFactory<ClassSales, String>("Products"));
        clmAmount.setCellValueFactory(new PropertyValueFactory<ClassSales, Integer>("Amount"));
        clmPrice.setCellValueFactory(new PropertyValueFactory<ClassSales, Integer>("Price"));
        mainTable.setItems(dataTable);
    }

    public class ClassSales {
        private final SimpleStringProperty Type;
        private final SimpleStringProperty Products;
        private final SimpleIntegerProperty Amount;
        private final SimpleIntegerProperty Price;

        private ClassSales(String Type, String Products, Integer Amount, Integer Price) {
            this.Type = new SimpleStringProperty(Type);
            this.Products = new SimpleStringProperty(Products);
            this.Amount = new SimpleIntegerProperty(Amount);
            this.Price = new SimpleIntegerProperty(Price);
        }

        public String getType() {
            return Type.get();
        }

        public SimpleStringProperty typeProperty() {
            return Type;
        }

        public void setType(String type) {
            this.Type.set(type);
        }

        public String getProducts() {
            return Products.get();
        }

        public SimpleStringProperty productsProperty() {
            return Products;
        }

        public void setProducts(String products) {
            this.Products.set(products);
        }

        public int getAmount() {
            return Amount.get();
        }

        public SimpleIntegerProperty amountProperty() {
            return Amount;
        }

        public void setAmount(int amount) {
            this.Amount.set(amount);
        }

        public int getPrice() {
            return Price.get();
        }

        public SimpleIntegerProperty priceProperty() {
            return Price;
        }

        public void setPrice(int total) {
            this.Price.set(total);
        }
    }
}
