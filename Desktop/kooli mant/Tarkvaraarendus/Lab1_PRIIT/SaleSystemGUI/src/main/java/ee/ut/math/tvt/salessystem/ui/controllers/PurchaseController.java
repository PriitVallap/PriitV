package ee.ut.math.tvt.salessystem.ui.controllers;

import ee.ut.math.tvt.salessystem.SalesSystemException;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import ee.ut.math.tvt.salessystem.logic.ShoppingCart;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

/**
 * Encapsulates everything that has to do with the purchase tab (the tab
 * labelled "Point-of-sale" in the menu). Consists of the purchase menu,
 * current purchase dialog and shopping cart table.
 */
public class PurchaseController implements Initializable {

    private static final Logger log = LogManager.getLogger(PurchaseController.class);

    private final SalesSystemDAO dao;
    private final ShoppingCart shoppingCart;

    @FXML
    private Button newPurchase;
    @FXML
    private Button submitPurchase;
    @FXML
    private Button cancelPurchase;
    @FXML
    private TextField barCodeField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private Button addItemButton;
    @FXML
    private TableView<SoldItem> purchaseTableView;

    public PurchaseController(SalesSystemDAO dao, ShoppingCart shoppingCart) {
        this.dao = dao;
        this.shoppingCart = shoppingCart;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Purchase Controller class is initialized.");
        cancelPurchase.setDisable(true);
        submitPurchase.setDisable(true);
        purchaseTableView.setItems(FXCollections.observableList(shoppingCart.getAll()));
        disableProductField(true);

        this.barCodeField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!newPropertyValue) {
                    fillInputsBySelectedStockItem();
                }
            }
        });

        purchaseTableView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent keyEvent) {
                final SoldItem soldItem = purchaseTableView.getSelectionModel().getSelectedItem();
                if (soldItem != null) {
                    //                                                 For macOS
                    if (keyEvent.getCode().equals(KeyCode.DELETE) || keyEvent.getCode().equals(KeyCode.BACK_SPACE)){
                        log.info("Deleted "+soldItem.getName()+" from shopping cart");
                        deleteRow(soldItem);
                        purchaseTableView.refresh();
                    }
                }
            }
        });

    }

    private void deleteRow(SoldItem selected){
        Alert deleteRow = createChoiceMessage("Delete row?", "Are you sure you want to delete the selected row?", "Yes", "No");
        Optional<ButtonType> barCodeChoice = deleteRow.showAndWait();
        if(barCodeChoice.get().getText().equals("Yes")){
            ObservableList<SoldItem> itemList = purchaseTableView.getItems();
            itemList.remove(selected);
        }else if(barCodeChoice.get().getText().equals("No")) {
            return;
        }
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

    /** Event handler for the <code>new purchase</code> event. */
    @FXML
    protected void newPurchaseButtonClicked() {
        log.info("New sale process started");
        try {
            enableInputs();
        } catch (SalesSystemException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Event handler for the <code>cancel purchase</code> event.
     */
    @FXML
    protected void cancelPurchaseButtonClicked() {
        try {
            shoppingCart.cancelCurrentPurchase();
            disableInputs();
            purchaseTableView.refresh();
            log.debug("Sale cancelled");
        } catch (SalesSystemException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Event handler for the <code>submit purchase</code> event.
     */
    @FXML
    protected void submitPurchaseButtonClicked() {
        try {
            log.debug("Contents of the current basket:\n" + shoppingCart.getAll());
            shoppingCart.submitCurrentPurchase();
            disableInputs();
            purchaseTableView.refresh();
            log.info("Sale complete");
        } catch (SalesSystemException e) {
            log.error(e.getMessage(), e);
        }
    }

    // switch UI to the state that allows to proceed with the purchase
    private void enableInputs() {
        log.debug("Inputs are enabled.");
        resetProductField();
        disableProductField(false);
        cancelPurchase.setDisable(false);
        submitPurchase.setDisable(false);
        newPurchase.setDisable(true);
    }

    // switch UI to the state that allows to initiate new purchase
    private void disableInputs() {
        log.debug("Inputs are disabled.");
        resetProductField();
        cancelPurchase.setDisable(true);
        submitPurchase.setDisable(true);
        newPurchase.setDisable(false);
        disableProductField(true);
    }

    public Alert createErrorMessage(String title, String content){
        log.error("Error!");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert;
    }

    private void fillInputsBySelectedStockItem() {
        log.debug("Inputs are filled by selected stock item.");
        StockItem stockItem = getStockItemByBarcode();
        if (stockItem != null) {
            nameField.setText(stockItem.getName());
            priceField.setText(String.valueOf(stockItem.getPrice()));
        } else {
            resetProductField();
            log.debug("Product field was reset");
        }
    }

    // Search the warehouse for a StockItem with the bar code entered
    // to the barCode textfield.
    private StockItem getStockItemByBarcode() {
        try {
            long code = Long.parseLong(barCodeField.getText());
            log.debug("Stock Item was retrieved by barcode");
            return dao.findStockItem(code);
        } catch (NumberFormatException e) {
            log.error("NumberFormatException was thrown when getting stock item by barcode");
            return null;
        }

    }

    /**
     * Add new item to the cart.
     */
    @FXML
    public void addItemEventHandler() {
        // add chosen item to the shopping cart.
        StockItem stockItem = getStockItemByBarcode();
        if (stockItem != null) {
            int quantity;
            try {
                quantity = Integer.parseInt(quantityField.getText());
            } catch (NumberFormatException e) {
                log.error("NumberFormatException was thrown when adding chosen item to shopping cart");
                quantity = 1;
            }
            int currentQuantity = stockItem.getQuantity();
            if(currentQuantity <quantity){
                Alert negativeStock = createErrorMessage("Not enough product in stock", "Only "+currentQuantity+" units in stock!");
                negativeStock.showAndWait();
                return;
            }
            if(quantity > 0) {
                shoppingCart.addItem(new SoldItem(stockItem, quantity));
                log.debug("New item was added to shopping cart.");
                purchaseTableView.refresh();
            }else{
                Alert negativeQuantity = createErrorMessage("Negative quantity!", "Quantity can not be lower than 1!");
                negativeQuantity.showAndWait();
                return;
            }
        }
    }

    /**
     * Sets whether or not the product component is enabled.
     */
    private void disableProductField(boolean disable) {
        log.debug("Product field is disabled");
        this.addItemButton.setDisable(disable);
        this.barCodeField.setDisable(disable);
        this.quantityField.setDisable(disable);
        this.nameField.setDisable(disable);
        this.priceField.setDisable(disable);
    }

    /**
     * Reset dialog fields.
     */
    private void resetProductField() {
        log.debug("Product field is reset");
        barCodeField.setText("");
        quantityField.setText("1");
        nameField.setText("");
        priceField.setText("");
    }
}