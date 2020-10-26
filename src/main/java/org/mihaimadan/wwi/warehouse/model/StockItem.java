package org.mihaimadan.wwi.warehouse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mihaimadan.wwi.users.model.Favorites;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "StockItems", schema = "Warehouse")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockItemId;

    @NotNull
    private String stockItemName;

    @NotNull
    private int leadTimeDays;

    @NotNull
    private int quantityPerOuter;

    @NotNull
    private boolean isChillerStock;

    @NotNull
    private double taxRate;

    @NotNull
    private double unitPrice;

    @NotNull
    private double recommendedRetailPrice;

    @NotNull
    private double typicalWeightPerUnit;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "stockItem", orphanRemoval = true)
    private StockItemHoldings stockItemHoldings;

    @JsonIgnore
    @OneToMany(mappedBy = "stockItem")
    private List<StockItemStockGroup> stockGroups = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "stockItem")
    private List<Favorites> usersFavorites = new ArrayList<>();

}
