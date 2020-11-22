package ee.ut.math.tvt.salessystem.ui.controllers;

import ee.ut.math.tvt.salessystem.SalesSystemException;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.Sale;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.logic.History;
import ee.ut.math.tvt.salessystem.logic.ShoppingCart;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Encapsulates everything that has to do with the history tab (the tab
 * labelled "History" in the menu).
 */

public class HistoryController implements Initializable {
    public SalesSystemDAO dao;
    public ShoppingCart shoppingCart;
    public List<SoldItem> soldItems;
    private final List<Sale> sales;
    private final History history;

    @FXML
    private Button showBetweenDates;
    @FXML
    private Button showLast10;
    @FXML
    private Button showAll;
    @FXML
    private TableView<SoldItem> warehouseTableView;
    @FXML
    private TableView<Sale> historyView;
    @FXML
    private TableColumn<Sale, String> date;
    @FXML
    private TableColumn<Sale, String> time;
    @FXML
    private TableColumn<Sale, String> sum;
    @FXML
    private Button refreshButton;
    @FXML
    private TableView<SoldItem> HistoryTableView;
    @FXML
    private TableColumn<SoldItem, String> contentId;
    @FXML
    private TableColumn<SoldItem, String> contentName;
    @FXML
    private TableColumn<SoldItem, String> contentPrice;
    @FXML
    private TableColumn<SoldItem, String> contentQuantity;
    @FXML
    private TableColumn<SoldItem, String> contentSum;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;

    private static final Logger log = LogManager.getLogger(HistoryController.class);


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*
        log.info("History Controller is initialized.");
        warehouseTableView.setItems(FXCollections.observableList(soldItems));
        endDate.setValue(LocalDate.now());
        startDate.setValue(LocalDate.now().minusWeeks(2));
        refreshHistory();*/
        historyView.setItems(FXCollections.observableList(dao.findSales()));
        date.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getSaleDate()));
        time.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getSaleTime()));
        sum.setCellValueFactory(p -> new SimpleStringProperty(String.valueOf(p.getValue().getSaleSum())));

        contentId.setCellValueFactory(p -> new SimpleStringProperty(String.valueOf(p.getValue().getId())));
        contentName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getName()));
        contentPrice.setCellValueFactory(p -> new SimpleStringProperty(String.valueOf(p.getValue().getPrice())));
        contentQuantity.setCellValueFactory(p -> new SimpleStringProperty(String.valueOf(p.getValue().getQuantity())));
        contentSum.setCellValueFactory(p -> new SimpleStringProperty(String.valueOf(p.getValue().getSumma())));
        HistoryTableView.setItems(FXCollections.observableList(soldItems));

        historyView.getSelectionModel().selectedItemProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue != null) {
                log.debug("Clicked on " + newPropertyValue.getSaleDate() + " in salelist");
                HistoryTableView.setItems(FXCollections.observableList(newPropertyValue.getSoldItems()));
                HistoryTableView.refresh();
            }
        });
        refreshHistory();
        log.debug("HistoryController initialized");
    }

    public HistoryController(SalesSystemDAO dao) {
        this.dao = dao;
        //this.soldItems = new ArrayList<>();
        sales = dao.findSales();
        soldItems = new ArrayList<>();
        this.history = new History(dao);
    }

    public void refreshHistory() {
        log.debug("Refreshed stock items.");
        historyView.refresh();

        //items need to be set again every time, or the rows will not be clickable.
        historyView.setItems(FXCollections.observableList(dao.findSales()));
    }

    @FXML
    protected void showBetweenDatesButtonClicked() {
        log.debug("betweenDates button clicked");

        LocalDate startDateValue = startDate.getValue();
        LocalDate endDateValue = endDate.getValue();

        if (startDateValue != null && endDateValue != null) {
            try {
                historyView.setItems(FXCollections.observableList(history.betweenDates(startDateValue, endDateValue)));
            } catch (SalesSystemException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                log.debug(e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error: incorrect date values.\nMake sure both fields are filled.");
            log.debug("Incorrect or incomplete dates submitted.");
        }
        HistoryTableView.setItems(null);
    }

    @FXML
    protected void showLast10ButtonClicked() {
        log.debug("showLast10 button clicked");
        historyView.setItems(FXCollections.observableList((history.showLast10())));
        HistoryTableView.setItems(null);
    }

    @FXML
    protected void showAllButtonClicked() {
        log.debug("showAll button clicked");
        refreshHistory();
        HistoryTableView.setItems(null);
    }
}