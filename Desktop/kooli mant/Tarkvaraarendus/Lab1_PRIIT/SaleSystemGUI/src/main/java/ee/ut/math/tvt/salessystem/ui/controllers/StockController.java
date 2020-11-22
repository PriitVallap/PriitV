package ee.ut.math.tvt.salessystem.ui.controllers;

import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import ee.ut.math.tvt.salessystem.logic.Warehouse;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class StockController implements Initializable {

    private static final Logger log = LogManager.getLogger(StockController.class);

    private final SalesSystemDAO dao;
    private final Warehouse warehouse;

    @FXML
    private Button addProductButton;
    @FXML
    private TableView<StockItem> warehouseTableView;
    @FXML
    private TextField barCodeFieldWH;
    @FXML
    private TextField nameFieldWH;
    @FXML
    private TextField priceFieldWH;
    @FXML
    private TextField quantityFieldWH;

    public StockController(SalesSystemDAO dao) {
        this.dao = dao;
        this.warehouse = new Warehouse(dao);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("This initializes the Stock Controller class.");
        refreshStockItems();

        this.barCodeFieldWH.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!newPropertyValue) {
                    fillInputsBySelectedStockItem();
                }
            }
        });
        refreshStockItems();

        warehouseTableView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent keyEvent) {
                final StockItem selectedItem = warehouseTableView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    //                                                 For macOS
                    if (keyEvent.getCode().equals(KeyCode.DELETE) || keyEvent.getCode().equals(KeyCode.BACK_SPACE)){
                        log.info("Deleted "+selectedItem.getName()+" from warehouse");
                        deleteRow(selectedItem);
                        refreshStockItems();
                    }
                }
            }
        });
        refreshStockItems();
    }

    private void deleteRow(StockItem selected){
        Alert deleteRow = createChoiceMessage("Delete row?", "Are you sure you want to delete the selected row?", "Yes", "No");
        Optional<ButtonType> barCodeChoice = deleteRow.showAndWait();
        if(barCodeChoice.get().getText().equals("Yes")){
            List<StockItem> itemList = dao.findStockItems();
            itemList.remove(selected);
        }else if(barCodeChoice.get().getText().equals("No")) {
            return;
        }
    }

    private void fillInputsBySelectedStockItem() {
        log.debug("This checks for already existing products.");
        StockItem stockItem = getStockItemByBarcodeWH();
        if (stockItem != null) {
            nameFieldWH.setText(stockItem.getName());
            priceFieldWH.setText(String.valueOf(stockItem.getPrice()));
        } else {
            refreshStockItems();
        }
    }

    private StockItem getStockItemByBarcodeWH() {
        try {
            long code = Long.parseLong(barCodeFieldWH.getText());
            StockItem stockitem = dao.findStockItem(code);
            quantityFieldWH.setText("0");
            log.debug("This should return the stock item by barcode.");
            return stockitem;

        } catch (NumberFormatException e) {
            log.error("NumberFormatException");
            return null;
        }
    }

    @FXML
    public void refreshButtonClicked() {
        log.debug("This refreshes the list");
        refreshStockItems();
    }
    public Alert createErrorMessage(String title, String content){
        log.error("Error!");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert;
    }

    public Alert createChoiceMessage(String title, String content, String buttonOne, String buttonTwo){
        log.debug("This creates a choice message.");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        ButtonType buttonTypeOne = new ButtonType(buttonOne);
        ButtonType buttonTypeTwo = new ButtonType(buttonTwo);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);
        return alert;
    }



    @FXML
    public void addProductButtonClicked() {
        // Add item to the warehouse
        if(barCodeFieldWH.getText().equals("") || quantityFieldWH.getText().equals("") || nameFieldWH.getText().equals("")  || priceFieldWH.getText().equals("")){
            Alert emptyField = createErrorMessage("A field was not filled", "Products can not be added to the warehouse with empty fields. Please check all the fields and try again.");
            log.error("Barcode field was not filled.");
            emptyField.showAndWait();
            return;
        }
        log.debug("This adds items");
        long barcode = Long.parseLong(barCodeFieldWH.getText());
        String name = nameFieldWH.getText();
        String desc = "Description";
        double price = Double.parseDouble(priceFieldWH.getText());
        int quantity = Integer.parseInt(quantityFieldWH.getText());


        StockItem newItem = new StockItem(barcode, name, desc, price, quantity);
        //this.warehouse.addItem(newItem);

        //warehouse.addItem(newItem);

        boolean itemAlreadyExists = false;
        int replaceableID = 0;
        List<StockItem> newItemList = dao.findStockItems();
        for (int i = 0; i < newItemList.size(); i++) {
            log.debug("All items are checked.");
            StockItem current = newItemList.get(i);
            long currentID = current.getId();
            if(currentID == barcode){
                itemAlreadyExists = true;
                replaceableID = (int) currentID-1;
            }
        }

        if(!itemAlreadyExists) {
            if(quantity==0){
                Alert wrongQuantity = createErrorMessage("Quantity can not be 0!", "Quantity can not be 0!");
                wrongQuantity.showAndWait();
                return;
            }
            log.debug("This item didn't exist previously");
            Alert newBarcode = createChoiceMessage("Item with this barcode does not exist!", "Would you like to a new product to the warehouse?: " +
                    "\nBarcode: "+barcode+"\nName: "+ name +"\nQuantity: " +quantity+"\nPrice: "+price, "Yes", "No");
            Optional<ButtonType> barCodeChoice = newBarcode.showAndWait();
            if(barCodeChoice.get().getText().equals("Yes")){
                this.warehouse.addItem(newItem);
                warehouse.addItem(newItem);
            }else if(barCodeChoice.get().getText().equals("No")){
                return;
            }else{
                return;
            }
        }else{
            double oldprice = newItemList.get(replaceableID).getPrice();
            double newprice = price;

            String oldname = newItemList.get(replaceableID).getName();
            String newname = name;
            if(newItemList.get(replaceableID).getPrice() != price){
                Alert wrongPrice = createChoiceMessage("The prices do not match!", "The prices do not match. Which of the prices would you like to use?", String.valueOf(oldprice), String.valueOf(newprice));
                log.error("A wrong price was entered");
                Optional<ButtonType> priceChoice = wrongPrice.showAndWait();
                if(priceChoice.get().getText().equals(String.valueOf(newprice))){
                    newItemList.get(replaceableID).setPrice(newprice);
                }else if(priceChoice.get().getText().equals(String.valueOf(oldprice))){

                }else{
                    return;
                }
            }

            if(!newItemList.get(replaceableID).getName().equals(name)){
                //log.debug("This asks for the correct name in case a weird one is inserted.");

                Alert wrongName = createChoiceMessage("The names do not match!", "The names do not match. Which of the names would you like to use?", oldname, newname);
                log.error("A wrong name was entered.");
                Optional<ButtonType> nameChoice = wrongName.showAndWait();
                if(nameChoice.get().getText().equals(newname)){
                    newItemList.get(replaceableID).setName(newname);
                }else if(nameChoice.get().getText().equals(oldname)){

                }else{
                    return;
                }
            }
            int newQuantity = newItemList.get(replaceableID).getQuantity() + quantity;
            if(newQuantity >= 0) {
                newItemList.get(replaceableID).setQuantity(newQuantity);
            }else{
                Alert quantityError = createErrorMessage("Incorrect quantity after process!", "The quantity of the product cannot be smaller than 0! Please check the quantity field!");
                quantityError.showAndWait();
                log.error("A wrong quantity was entered.");
                return;
            }
        }
        log.debug("Finally, a new item is added to the warehouse.");
        //warehouseTableView.setItems(FXCollections.observableList(newItemList));


        warehouseTableView.refresh();
        refreshStockItems();
    }

    private void refreshStockItems() {
        log.debug("This refreshes stock items.");
        warehouseTableView.setItems(FXCollections.observableList(dao.findStockItems()));
        warehouseTableView.refresh();
    }
}