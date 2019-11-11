package com.superducks.laptopsales.controllers;

import com.superducks.laptopsales.Class.AlertMessage;
import com.superducks.laptopsales.Class.ConnectDatabase;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Sales {
    static Stage salesStage = new Stage();
    public TableView<ClassSales> mainTable;
    public ComboBox<String> cbxCategories;
    public ComboBox<String> cbxProducts;
    public ImageView btnAdd;
    public TableColumn<ClassSales, String> clmType;
    public TableColumn<ClassSales, String> clmProducts;
    public TableColumn<ClassSales, Integer> clmAmount;
    public TableColumn<ClassSales, Integer> clmPrice;
    public TextField txtAmount;
    public ImageView btnRemove;
    public Label lblTotalePrice;
    public TextField txtCustomer;
    public TextField txtPhone;
    public Button btnConfirm;
    public Button btnClearAll;
    public ImageView btnNonAdd;
    public ImageView btnNonRemove;
    ObservableList<ClassSales> dataTable = FXCollections.observableArrayList();


    public void initialize() {
        getDataComboboxCategories();
        getDataComboboxProducts();
        check();
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


    public void btnAdd_Click(MouseEvent mouseEvent) {
        addTableRow();
    }

    public void btnRemove_Click(MouseEvent mouseEvent) {
        if(AlertMessage.showAlertYesNo()) {
            removeTableRow();
        }
    }

    private void addTableRow() {
        String type, products;
        Integer amount, price = 0;
        type = cbxCategories.getValue().toString();
        products = cbxProducts.getValue().toString();
        amount = Integer.parseInt(txtAmount.getText());

        String sql = "select * from products where name = '" + products + "';";
        try {
            ResultSet rs = ConnectDatabase.Connect().createStatement().executeQuery(sql);
            if(rs.next())
                price = Integer.parseInt(rs.getString("price"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //add Row to Table
        int row = mainTable.getItems().size();
        ClassSales cs;
        for(int i = 0 ; i < row ; i ++) {
            cs = dataTable.get(i);
            if(cs.getType().equals(cbxCategories.getValue().toString()) && cs.getProducts().equals(cbxProducts.getValue().toString())) {
                dataTable.remove(cs);
                break;
            }
        }
        dataTable.add(new ClassSales(type, products, amount, amount * price));
        clmType.setCellValueFactory(new PropertyValueFactory<ClassSales, String>("Type"));
        clmProducts.setCellValueFactory(new PropertyValueFactory<ClassSales, String>("Products"));
        clmAmount.setCellValueFactory(new PropertyValueFactory<ClassSales, Integer>("Amount"));
        clmPrice.setCellValueFactory(new PropertyValueFactory<ClassSales, Integer>("Price"));
        mainTable.setItems(dataTable);
        mainTable.getSelectionModel().selectFirst();
        check();
        totalPrice();
    }

    private void removeTableRow() {
        ClassSales s = mainTable.getSelectionModel().getSelectedItem();
        s.getProducts();
        dataTable.remove(s);
        mainTable.setItems(dataTable);
        mainTable.getSelectionModel().selectFirst();
        check();
        totalPrice();
    }

    private void check() {
        checkAdd();
        checkRemove();
    }

    private void tableRow_Clicked () {
        ClassSales s = mainTable.getSelectionModel().getSelectedItem();
        cbxCategories.setValue(s.getType());
        cbxProducts.setValue(s.getProducts());
        txtAmount.setText(""+s.getAmount());
    }

    private void totalPrice() {
        int totalPice = 0;
        int size = mainTable.getItems().size();
        for(int i = 0; i < size; i ++) {
            totalPice += dataTable.get(i).getPrice();
        }
        lblTotalePrice.setText("Total Price: "+totalPice+" vnd");
    }

    public void cbxValue_Changed(ActionEvent actionEvent) {
        check();
    }

    private void checkAdd() {
        if(isInteger(txtAmount.getText())) {

            if (Integer.parseInt(txtAmount.getText()) > 0 && Integer.parseInt(txtAmount.getText()) <= 100 && cbxProducts.getItems().size() > 0) {
                btnAdd.setVisible(true);
                btnNonAdd.setVisible(false);
            } else {
                btnAdd.setVisible(false);
                btnNonAdd.setVisible(true);
            }
        } else {
            btnAdd.setVisible(false);
            btnNonAdd.setVisible(true);
        }
    }

    private boolean isInteger( String input ) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }


    public void cbxCategoriesValue_Changed(ActionEvent actionEvent) {
        if(cbxProducts.getItems().size() > 0)
            cbxProducts.getItems().clear();
        getDataComboboxProducts();
        check();
    }

    public void mainTable_MouseClicked(MouseEvent mouseEvent) {
        tableRow_Clicked();
        check();
    }

    private void checkRemove() {
        if(mainTable.getItems().size() > 0) {
            btnRemove.setVisible(true);
            btnNonRemove.setVisible(false);
        }
        else {
            btnRemove.setVisible(false);
            btnNonRemove.setVisible(true);
        }
    }

    public void txtAmount_textChanged(KeyEvent keyEvent) {
        check();
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
