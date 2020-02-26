package org.mihaimadan.wwi.warehouse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "StockItemHoldings", schema = "Warehouse")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockItemHoldings {
    @Id
    private Long stockItemId;

    @NotNull
    private int quantityOnHand;

    @NotNull
    private String binLocation;

    @NotNull
    private double lastCostPrice;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "StockItemID")
    private StockItem stockItem;

}
