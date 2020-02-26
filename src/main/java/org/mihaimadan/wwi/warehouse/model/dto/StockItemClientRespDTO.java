package org.mihaimadan.wwi.warehouse.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockItemClientRespDTO {
    private Long stockItemId;
    private String stockItemName;
    private double unitPrice;
    private boolean soldOut;
}
