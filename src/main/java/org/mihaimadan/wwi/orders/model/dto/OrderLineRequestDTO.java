package org.mihaimadan.wwi.orders.model.dto;

import lombok.Data;
import org.mihaimadan.wwi.warehouse.model.dto.StockItemClientRespDTO;

@Data
public class OrderLineRequestDTO {
    private StockItemClientRespDTO stockItem;
    private int quantity;
    private double totalUnitPrice;
}
