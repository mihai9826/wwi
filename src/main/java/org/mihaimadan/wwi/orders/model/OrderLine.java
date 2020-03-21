package org.mihaimadan.wwi.orders.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mihaimadan.wwi.warehouse.model.StockItem;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "OrderLines", schema = "Sales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderLineId;

    @OneToOne
    @JoinColumn(name = "StockItemID")
    private StockItem stockItem;

    @NotNull
    private int quantity;

    @NotNull
    private double totalUnitPrice;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OrderID")
    private Order theOrder;

}
