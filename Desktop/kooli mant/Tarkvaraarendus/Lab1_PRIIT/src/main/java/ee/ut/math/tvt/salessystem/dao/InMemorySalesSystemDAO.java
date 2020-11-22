package ee.ut.math.tvt.salessystem.dao;

import ee.ut.math.tvt.salessystem.dataobjects.Sale;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;

import java.util.ArrayList;
import java.util.List;

public class InMemorySalesSystemDAO implements SalesSystemDAO {

    private final List<StockItem> stockItemList;
    private final List<SoldItem> soldItemList;
    private StringBuilder calls = new StringBuilder();
    private final List<Sale> saleList;


    public InMemorySalesSystemDAO() {
        List<StockItem> items = new ArrayList<StockItem>();
        items.add(new StockItem(1L, "Lays chips", "Potato chips", 11.0, 5));
        items.add(new StockItem(2L, "Chupa-chups", "Sweets", 8.0, 8));
        items.add(new StockItem(3L, "Frankfurters", "Beer sauseges", 15.0, 12));
        items.add(new StockItem(4L, "Free Beer", "Student's delight", 0.0, 100));
        this.stockItemList = items;
        this.soldItemList = new ArrayList<>();
        this.saleList = new ArrayList<>();

    }

    @Override
    public List<StockItem> findStockItems() {
        return stockItemList;
    }

    @Override
    public List<SoldItem> findSoldItems() {
        return soldItemList;
    }

    @Override
    public StockItem findStockItem(long id) {
        for (StockItem item : stockItemList) {
            if (item.getId() == id)
                return item;
        }
        return null;
    }

    @Override
    public void addExistingStockItem(StockItem stockItem) {
        stockItemList.add(stockItem);
    }

    @Override
    public List<Sale> findSales() {
        return saleList;
    }

    @Override
    public void saveSale(Sale sale) {
        saleList.add(sale);
    }


    @Override
    public void saveSoldItem(SoldItem item) {
        soldItemList.add(item);
    }

    @Override
    public void saveStockItem(StockItem stockItem) {
        stockItemList.add(stockItem);
    }

    @Override
    public void beginTransaction() {
        calls.append("dao.begin ");
    }

    @Override
    public void rollbackTransaction() {
    }

    @Override
    public void commitTransaction() {
        calls.append("dao.commit ");
    }
}
