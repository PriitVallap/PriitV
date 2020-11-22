package ee.ut.math.tvt.salessystem.dataobjects;


import javax.persistence.*;

import java.time.Instant;

/**
 * Already bought StockItem. SoldItem duplicates name and price for preserving history.
 */
@Entity
@Table(name = "SoldItem")
public class SoldItem {

    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    //@Transient
    private Sale sale;

    @OneToOne
    @JoinColumn(name = "stockitem_id")
    private StockItem stockItem;

    @Column(name="name")
    private String name;

    @Column(name="quantity")
    private Integer quantity;

    @Column(name = "price")
    private double price;

    @Column(name = "summa")
    private double summa;

    @Column(name = "saleDate")
    private final Instant saleDate;

    public SoldItem(StockItem stockItem, int quantity) {
        this.stockItem = stockItem;
        this.id = stockItem.getId();
        this.name = stockItem.getName();
        this.price = stockItem.getPrice();
        this.quantity = quantity;
        this.summa = quantity * stockItem.getPrice();
        this.saleDate = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public double getSumma() {
        return price * ((double) quantity);
    }

    public StockItem getStockItem() {
        return stockItem;
    }

    public void setStockItem(StockItem stockItem) {
        this.stockItem = stockItem;
    }

    @Override
    public String toString() {
        return String.format("SoldItem{id=%d, name='%s'}", id, name);
    }
}
