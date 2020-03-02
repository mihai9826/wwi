package org.mihaimadan.wwi.warehouse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "StockItemStockGroups", schema = "Warehouse")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockItemStockGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockItemStockGroupId;

    @ManyToOne
    @JoinColumn(name = "StockItemID")
    private StockItem stockItem;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StockGroupID")
    private StockGroup stockGroup;


}
