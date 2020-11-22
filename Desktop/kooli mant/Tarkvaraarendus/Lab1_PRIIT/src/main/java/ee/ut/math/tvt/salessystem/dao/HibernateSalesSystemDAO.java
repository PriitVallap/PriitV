package ee.ut.math.tvt.salessystem.dao;

import ee.ut.math.tvt.salessystem.dataobjects.Sale;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class HibernateSalesSystemDAO implements SalesSystemDAO {

    private final EntityManagerFactory emf;
    private final EntityManager em;

    public HibernateSalesSystemDAO() {
        // if you get ConnectException/JDBCConnectionException then you
        // probably forgot to start the database before starting the application
        emf = Persistence.createEntityManagerFactory("pos");
        em = emf.createEntityManager();
    }

    // TODO implement missing methods

    public void close() {
        em.close();
        emf.close();
    }

    @Override
    public List<StockItem> findStockItems() {
        return em.createQuery("from StockItem", StockItem.class).getResultList();
    }

    @Override
    public List<SoldItem> findSoldItems() {
        return em.createQuery("from SoldItem", SoldItem.class).getResultList();
    }


    @Override
    public StockItem findStockItem(long id) {
        /*
        for (StockItem item : stockItemList) {
            if (item.getId() == id)
                return item;
        }

         */
        return em.find(StockItem.class, id);
    }

    @Override
    public List<Sale> findSales() {
        return em.createQuery("from Sale", Sale.class).getResultList();
    }


    @Override
    public void saveSoldItem(SoldItem item) {
        //soldItemList.add(item);
        em.persist(item);
    }

    @Override
    public void saveSale(Sale sale) {
        em.persist(sale);
    }

    @Override
    public void saveStockItem(StockItem stockItem) {
        //stockItemList.add(stockItem);
        em.persist(stockItem);
    }

    @Override
    public void addExistingStockItem(StockItem stockItem) {
        em.merge(stockItem);
    }

    @Override
    public void beginTransaction() {
        em.getTransaction().begin();
    }

    @Override
    public void rollbackTransaction() {
        em.getTransaction().rollback();
    }

    @Override
    public void commitTransaction() {
        em.getTransaction().commit();
    }
}