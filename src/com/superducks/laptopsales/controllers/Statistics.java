package com.superducks.laptopsales.controllers;

import com.superducks.laptopsales.Class.ConnectDatabase;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class Statistics {
    static Stage mainStage = new Stage();
    public TableView tblAccounts;
    public TableView tblBills;
    public TableColumn clmUsername;
    public TableColumn clmFullname;
    public TableColumn clmBillID;
    public TableColumn clmBuyer;
    public TableColumn clmDateCreated;
    public TableColumn clmTotalPrice;
    public PieChart pieChartStatistics;
    public TableColumn clmUser;
    public ImageView btnNonView;
    public ImageView btnView;
    public BarChart barChartStatistics;
    private ObservableList<Accounts> dataAccounts = FXCollections.observableArrayList();
    ObservableList<PieChart.Data> pieChartData =FXCollections.observableArrayList();

    public void initialize() {
        showTableAccounts();
        showTableBills();
        check();
        pieChartData.clear();
        Accounts accounts=(Accounts) tblAccounts.getSelectionModel().getSelectedItem();
        showPieChartWithUser(accounts.getId());
        Bills bl = (Bills) tblBills.getSelectionModel().getSelectedItem();
        showBarChart(bl.getBillID());
    }

    private void showBarChart(int billID) {
        String sql="call showBarChart("+billID+")";
        CallableStatement cs = null;
        XYChart.Series dataSeries = new XYChart.Series();
        try {
            cs = Objects.requireNonNull(ConnectDatabase.Connect()).prepareCall(sql);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                dataSeries.getData().add(new XYChart.Data(rs.getString(1), rs.getInt(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        barChartStatistics.getData().clear();
        barChartStatistics.getData().add(dataSeries);
    }

    private void showPieChartWithUser(int user){
        pieChartData.clear();
        String sql="call showPieChart("+user+")";
        try {
            CallableStatement cs = ConnectDatabase.Connect().prepareCall(sql);
            ResultSet rs=cs.executeQuery();
            while(rs.next()) {
                PieChart.Data slice1 = new PieChart.Data(rs.getString(1) +" ("+rs.getInt(2)+")", rs.getInt(2));
                pieChartData.add(slice1);
            }
            pieChartStatistics.getData().clear();
            pieChartStatistics.getData().addAll(pieChartData);
            pieChartStatistics.setLegendSide(Side.BOTTOM);
            pieChartStatistics.setClockwise(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void showTableAccounts() {
        String sql = "select * from accounts";
        try {
            ResultSet rs = Objects.requireNonNull(ConnectDatabase.Connect()).createStatement().executeQuery(sql);
            while (rs.next()) {
                dataAccounts.add(new Accounts(rs.getInt("id"),rs.getString("username"), rs.getString("fullname")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clmUsername.setCellValueFactory(new PropertyValueFactory<Accounts, String>("username"));
        clmFullname.setCellValueFactory(new PropertyValueFactory<Accounts, String>("fullname"));
        tblAccounts.setItems(dataAccounts);
        tblAccounts.getSelectionModel().selectFirst();
        check();
    }

    private void showTableBills() {
        ObservableList<Bills> dataBills = FXCollections.observableArrayList();
        Accounts db = (Accounts) tblAccounts.getSelectionModel().getSelectedItem();
        String username = "";
        String sqlUsername = "select * from accounts where username = '"+db.getUsername()+"';";
        try {
            ResultSet us = Objects.requireNonNull(ConnectDatabase.Connect()).createStatement().executeQuery(sqlUsername);
            if(us.next()) {
                username = us.getString("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int billID = 0;
        String user = "", buyer = "", datetime = "", totalPrice = "";
        String sqlBill = "select * from bill where user="+username+";";
        try {
            ResultSet rs = Objects.requireNonNull(ConnectDatabase.Connect()).createStatement().executeQuery(sqlBill);
            while (rs.next()) {
                billID = rs.getInt("id");
                String sqlUser = "select * from accounts where id = "+rs.getInt("user")+";";
                ResultSet rst = Objects.requireNonNull(ConnectDatabase.Connect()).createStatement().executeQuery(sqlUser);
                if(rst.next())
                    user = rst.getString("fullname");
                buyer = rs.getString("customer_name");
                datetime = rs.getString("date");
                totalPrice = getFormattedAmount(rs.getInt("total"));
                dataBills.add(new Bills(billID,user,buyer,datetime,totalPrice));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clmBillID.setCellValueFactory(new PropertyValueFactory<Bills, Integer>("billID"));
        clmUser.setCellValueFactory(new PropertyValueFactory<Bills, String>("user"));
        clmBuyer.setCellValueFactory(new PropertyValueFactory<Bills, String>("buyer"));
        clmDateCreated.setCellValueFactory(new PropertyValueFactory<Bills, String>("datetime"));
        clmTotalPrice.setCellValueFactory(new PropertyValueFactory<Bills, String>("totalPrice"));
        tblBills.setItems(dataBills);
        tblBills.getSelectionModel().selectFirst();
        check();
    }

    private static String getFormattedAmount(int amount) {
        StringBuilder formatted_value = new StringBuilder();
        boolean isNavigate = amount < 0;
        amount = Math.abs(amount);
        while (amount > 999) {
            int du = amount % 1000;
            amount = amount / 1000;
            formatted_value.insert(0, String.format(Locale.getDefault(), ".%,03d", du));
        }
        if(isNavigate){
            formatted_value.insert(0, String.format(Locale.getDefault(), "-%,d", amount));
        } else {
            formatted_value.insert(0, String.format(Locale.getDefault(), "%,d", amount));
        }
        return String.format(Locale.getDefault(), "%s", formatted_value.toString());
    }

    public void btnView_Clicked(MouseEvent mouseEvent) {
        Bills bl = (Bills) tblBills.getSelectionModel().getSelectedItem();
        SellOrder.showForm(bl.getBillID());
    }

    public void tblAccounts_Clicked(MouseEvent mouseEvent) {
        showTableBills();
        Accounts db = (Accounts) tblAccounts.getSelectionModel().getSelectedItem();
        showPieChartWithUser(db.getId());
        Bills bl = (Bills) tblBills.getSelectionModel().getSelectedItem();
        showBarChart(bl.getBillID());
    }

    private void check() {
        if(tblBills.getItems().size()>0) {
            btnView.setVisible(true);
            btnNonView.setVisible(false);
        }
        else {
            btnView.setVisible(false);
            btnNonView.setVisible(true);
        }
    }

    public void tblBills_Clicked(MouseEvent mouseEvent) {
        Bills bl = (Bills) tblBills.getSelectionModel().getSelectedItem();
        showBarChart(bl.getBillID());
    }

    public static class Accounts {
        private int id;
        private final SimpleStringProperty username;
        private final SimpleStringProperty fullname;

        public Accounts(int id, String username, String fullname) {
            this.id = id;
            this.username = new SimpleStringProperty(username);
            this.fullname = new SimpleStringProperty(fullname);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        private Accounts(String uName, String fName) {
            this.username = new SimpleStringProperty(uName);
            this.fullname = new SimpleStringProperty(fName);
        }

        public String getUsername() {
            return username.get();
        }

        public void setUsername(String fName) {
            username.set(fName);
        }

        public String getFullname() {
            return fullname.get();
        }

        public void setFullname(String fName) {
            fullname.set(fName);
        }

    }

    public static class Bills {
        private final  SimpleIntegerProperty billID;
        private final SimpleStringProperty user;
        private final SimpleStringProperty buyer;
        private final SimpleStringProperty datetime;
        private final SimpleStringProperty totalPrice;

        public int getBillID() {
            return billID.get();
        }

        public SimpleIntegerProperty billIDProperty() {
            return billID;
        }

        public void setBillID(int billID) {
            this.billID.set(billID);
        }

        public String getUser() {
            return user.get();
        }

        public SimpleStringProperty userProperty() {
            return user;
        }

        public void setUser(String user) {
            this.user.set(user);
        }

        public String getBuyer() {
            return buyer.get();
        }

        public SimpleStringProperty buyerProperty() {
            return buyer;
        }

        public void setBuyer(String buyer) {
            this.buyer.set(buyer);
        }

        public String getDatetime() {
            return datetime.get();
        }

        public SimpleStringProperty datetimeProperty() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime.set(datetime);
        }

        public String getTotalPrice() {
            return totalPrice.get();
        }

        public SimpleStringProperty totalPriceProperty() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice.set(totalPrice);
        }

        public Bills(int billID, String user, String buyer, String datetime, String totalPrice) {
            this.billID = new SimpleIntegerProperty(billID);
            this.user = new SimpleStringProperty(user) ;
            this.buyer = new SimpleStringProperty(buyer);
            this.datetime = new SimpleStringProperty(datetime);
            this.totalPrice = new SimpleStringProperty(totalPrice);
        }
    }
}
