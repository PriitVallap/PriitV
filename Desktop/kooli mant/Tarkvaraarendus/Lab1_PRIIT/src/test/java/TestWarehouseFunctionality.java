import ee.ut.math.tvt.salessystem.dao.InMemorySalesSystemDAO;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import ee.ut.math.tvt.salessystem.logic.ShoppingCart;
import ee.ut.math.tvt.salessystem.logic.Warehouse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestWarehouseFunctionality {

    private ShoppingCart shoppingCart;
    private Warehouse warehouse;
    private StockItem stockItem;
    private StringBuilder calls;
    private InMemorySalesSystemDAO dao;
    private List<StockItem> stockItemList;

    public TestWarehouseFunctionality() {
        SalesSystemDAO dao = new InMemorySalesSystemDAO();
        stockItemList = dao.findStockItems();
        warehouse = new Warehouse(dao);
    }
    @Before
    public void setUp() throws Exception {

        calls = new StringBuilder();
        stockItem = new StockItem(100L, "Test warehouse", " ", 30, 10);
        //dao.saveStockItem(stockItem);
    }

    @Test
    public void testAddingItemBeginsAndCommitsTransaction() {
        SalesSystemDAO mockDAO = mock(InMemorySalesSystemDAO.class);
        Warehouse mockWarehouse = new Warehouse(mockDAO);
        mockWarehouse.addItem(new StockItem(99L, "Test", "test", 50, 10));
        verify(mockDAO, times(1)).beginTransaction();
        verify(mockDAO, times(1)).commitTransaction();
        InOrder inOrder = inOrder(mockDAO);
        inOrder.verify(mockDAO).beginTransaction();
        inOrder.verify(mockDAO).commitTransaction();
    }

    @Test
    public void testAddingNewItem() {
        InMemorySalesSystemDAO dao = new InMemorySalesSystemDAO();
        StockItem stockItem = new StockItem(100L, "Test warehouse", " ", 0, 10);
        dao.saveStockItem(stockItem);

        assertSame( "Test warehouse", dao.findStockItem(100L).getName());
    }

    @Test
    public void testAddingExistingItem() {
        InMemorySalesSystemDAO dao = new InMemorySalesSystemDAO();
        int firstQuantity = dao.findStockItem(1L).getQuantity();
        warehouse.addItem(new StockItem(1L, "Lays chips", "Potato chips", 11.0, 5));
        assertEquals(firstQuantity + 5, stockItem.getQuantity());
    }

    @Test
    public void testAddingExistingItemNegativeQuantity() {
        long id = 1L;
        InMemorySalesSystemDAO dao = new InMemorySalesSystemDAO();
        int quantity = -1;
        StockItem item = dao.findStockItem(id);
        String name = item.getName();
        double price = item.getPrice();
        int startingQuanity = item.getQuantity();
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                warehouse.addItem(new StockItem(id, name, " ", price, quantity)));

        assertEquals(startingQuanity, dao.findStockItem(id).getQuantity());
    }
}
