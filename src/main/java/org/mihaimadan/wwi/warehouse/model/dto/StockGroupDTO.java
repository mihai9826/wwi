package org.mihaimadan.wwi.warehouse.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mihaimadan.wwi.warehouse.model.StockItemStockGroups;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockGroupDTO {
    private Long stockGroupId;

    private String stockGroupName;

    private List<StockItemStockGroups> stockItems;
}
