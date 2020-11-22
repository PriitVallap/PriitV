package ee.ut.math.tvt.salessystem.dataobjects;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Sale")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "sale")
    private final List<SoldItem> soldItems;

    @Column(name="date")
    private final Instant saleDate = Instant.now();

    public Sale() {
        this.soldItems = new ArrayList<>();
    }

    public List<SoldItem> getSoldItems() {
        return soldItems;
    }

    public void addSoldItem(SoldItem soldItem) {
        soldItems.add(soldItem);
    }

    public String getSaleDate() {
        return saleDate.toString().substring(0, saleDate.toString().indexOf("T"));
    }

    public String getSaleTime() {
        String dateString = saleDate.toString();
        return dateString.substring(dateString.indexOf("T") + 1, dateString.indexOf("."));
    }

    public double getSaleSum() {
        double sum = 0.0;
        for (SoldItem s : soldItems) {
            sum += s.getSumma();
        }
        return sum;
    }
}
