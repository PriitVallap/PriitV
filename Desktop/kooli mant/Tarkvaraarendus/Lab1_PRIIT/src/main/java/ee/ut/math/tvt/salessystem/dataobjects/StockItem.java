package ee.ut.math.tvt.salessystem.dataobjects;


import javax.persistence.*;

/**
 * Stock item.
 */
@Entity
@Table(name="StockItem")
public class StockItem {

    @Id
    @Column(name = "stockitem_id")
    private Long barcode;

    @Column(name="name")
    private String name;

    @Column(name="price")
    private double price;

    @Column(name="description")
    private String description;

    @Column(name="quantity")
    private int quantity;

    public StockItem() {
    }

    public Long getBarcode() {
        return this.barcode;
    }

    public StockItem(Long id, String name, String desc, double price, int quantity) {
        this.barcode = id;
        this.name = name;
        this.description = desc;
        this.price = price;
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getId() {
        return barcode;
    }

    public void setId(Long id) {
        this.barcode = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("StockItem{id=%d, name='%s'}", barcode, name);
    }
}
