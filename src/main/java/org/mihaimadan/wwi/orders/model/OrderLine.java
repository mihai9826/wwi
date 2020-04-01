package org.mihaimadan.wwi.orders.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.mihaimadan.wwi.warehouse.model.StockItem;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "OrderLines", schema = "Sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderLine {

    @Id
    @SequenceGenerator(name = "idGenerator", sequenceName = "Sequences.OrderLineID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "idGenerator")
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
