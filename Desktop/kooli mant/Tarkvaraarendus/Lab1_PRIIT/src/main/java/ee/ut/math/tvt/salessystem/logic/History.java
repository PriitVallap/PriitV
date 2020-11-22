package ee.ut.math.tvt.salessystem.logic;

import ee.ut.math.tvt.salessystem.SalesSystemException;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.Sale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class History {

    private final Logger log = LogManager.getLogger(History.class);
    private final List<Sale> sales;
    private final SalesSystemDAO dao;

    public History(SalesSystemDAO dao) {
        sales = dao.findSales();
        this.dao = dao;
    }

    public List<Sale> betweenDates(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new SalesSystemException("Error: end date is supposed to be after start date!");
        } else {
            log.debug("Looking for sales between " + startDate + " and " + endDate);
            ArrayList<Sale> betweenSelected = new ArrayList<>();

            for (Sale sale : sales) {
                LocalDate saleDate = LocalDate.parse(sale.getSaleDate());

                if ((saleDate.isBefore(endDate) || saleDate.isEqual(endDate)) && (saleDate.isAfter(startDate) || saleDate.isEqual(startDate))) {
                    betweenSelected.add(sale);
                }
            }
            log.debug("Found " + betweenSelected.size() + " purchases.");
            return betweenSelected;
        }
    }

    public List<Sale> showLast10() {
        List<Sale> lastTen;

        log.debug("Looking for the last 10 sales.");

        lastTen = sales.subList(sales.size() - Math.min(sales.size(), 10), sales.size());

        log.debug("Found " + lastTen.size() + " purchases.");
        return lastTen;
    }

    public List<Sale> showAll() {
        return dao.findSales();
    }
}
