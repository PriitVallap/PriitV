package ee.ut.math.tvt.salessystem.logic;

import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Warehouse {

    private final Logger log = LogManager.getLogger(Warehouse.class);
    private final SalesSystemDAO dao;

    public Warehouse(SalesSystemDAO dao) {
        this.dao = dao;
    }


    /**
     * Add new SoldItem to table.
     */
    public void addItem(StockItem item) {

        long barcode = item.getId();
        String name = item.getName();
        double price = item.getPrice();
        int quantity = item.getQuantity();

        boolean itemAlreadyExists = false;
        int replaceableID = 0;
        List<StockItem> newItemList = dao.findStockItems();
        for (int i = 0; i < newItemList.size(); i++) {
            log.debug("All items are checked.");
            StockItem current = newItemList.get(i);
            long currentID = current.getId();
            if (currentID == barcode) {
                itemAlreadyExists = true;
                replaceableID = (int) currentID - 1;
            }
        }
        if (!itemAlreadyExists) {
            newItemList.add(item);
        } else {
            if (barcode <= 0 || quantity <= 0 || price < 0) {
                throw new IllegalArgumentException("Error :\nquantity and bar code values must be greater than 0.\nPrice must be greater than or equal to 0.");
            }
            if (newItemList.get(replaceableID).getPrice() != price) {
                throw new IllegalArgumentException("Error : Prices dont match");
            }
            int newQuantity = newItemList.get(replaceableID).getQuantity() + quantity;
            if (newQuantity >= 0) {
                newItemList.get(replaceableID).setQuantity(newQuantity);
            } else {
                throw new IllegalArgumentException("Error : A wrong quantity was entered");
            }

        }
        log.debug("Finally, a new item is added to the warehouse.");
        addToDao(item);
    }

    public void addToDao(StockItem newItem) {
        dao.beginTransaction();
        log.debug("Transaction began.");
        try {
            dao.saveStockItem(newItem);
            dao.commitTransaction();
            log.debug("Transaction committed");
        } catch (Exception e) {
            dao.rollbackTransaction();
            log.error("rollbackTransaction");
            throw e;
        }
    }


}
