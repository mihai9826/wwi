package org.mihaimadan.wwi.warehouse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "StockGroups", schema = "Warehouse")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockGroupId;

    @NotNull
    private String stockGroupName;

    @OneToMany(mappedBy = "stockGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockItemStockGroups> stockItems = new ArrayList<>();
}
