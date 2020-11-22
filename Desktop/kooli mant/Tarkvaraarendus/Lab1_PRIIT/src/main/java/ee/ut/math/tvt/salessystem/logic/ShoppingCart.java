package ee.ut.math.tvt.salessystem.logic;

import ee.ut.math.tvt.salessystem.SalesSystemException;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.Sale;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

public class ShoppingCart {

    private final Logger log = LogManager.getLogger(ShoppingCart.class);

    private final SalesSystemDAO dao;
    private final List<SoldItem> items = new ArrayList<>();

    public ShoppingCart(SalesSystemDAO dao) {
        this.dao = dao;
    }

    /**
     * Add new SoldItem to table.
     */
    public void addItem(SoldItem item) {
        if (item.getQuantity() <= 0) {
            log.error("Attempted to add an item with a quantity 0 or lower to cart.");
            throw new SalesSystemException("Quantity must be greater than 0.");
        }
        if (items.size() == 0) {
            items.add(item);
        } else {
            boolean contains = false;
            int index = 0;
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getName().equals(item.getName())) {
                    contains = true;
                    index = i;
                    break;
                }
            }
            if(contains) {
                int newQuantity = items.get(index).getQuantity() + item.getQuantity();
                StockItem stockItem = new StockItem();
                stockItem.setQuantity(newQuantity);
                if (newQuantity > 0) {
                    items.get(index).setQuantity(newQuantity);
                }
            }else{
                items.add(item);
            }
        }
        log.debug("Added " + item.getName() + " quantity of " + item.getQuantity());
    }

    public void addToCartFromConsole(String[] c) {
        try {
            long idx = Long.parseLong(c[1]);
            int amount = Integer.parseInt(c[2]);
            StockItem item = dao.findStockItem(idx);
            if (item != null) {
                items.add(new SoldItem(item, Math.min(amount, item.getQuantity())));
            } else {
                System.out.println("no stock item with id " + idx);
            }
        } catch (SalesSystemException | NoSuchElementException e) {
            log.error(e.getMessage(), e);
        }
    }

    public List<SoldItem> getAll() {
        return items;
    }

    public void cancelCurrentPurchase() {
        items.clear();
    }

    public void submitCurrentPurchase() {
        // TODO decrease quantities of the warehouse stock

        // note the use of transactions. InMemorySalesSystemDAO ignores transactions
        // but when you start using hibernate in lab5, then it will become relevant.
        // what is a transaction? https://stackoverflow.com/q/974596
        dao.beginTransaction();
        try {
            Sale sale = new Sale();
            for (SoldItem item : items) {
                StockItem stockitem = dao.findStockItem(item.getId());
                stockitem.setQuantity(stockitem.getQuantity() - item.getQuantity());
                sale.addSoldItem(item);
            }
            if (!sale.getSoldItems().isEmpty()) {
                dao.saveSale(sale);
            }
            dao.commitTransaction();
            items.clear();
        } catch (Exception e) {
            dao.rollbackTransaction();
            throw e;
        }
    }
}
