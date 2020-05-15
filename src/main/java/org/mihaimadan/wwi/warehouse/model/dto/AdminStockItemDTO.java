package org.mihaimadan.wwi.warehouse.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminStockItemDTO {
    private Long stockItemId;

    private String stockItemName;

    private double unitPrice;

    private double typicalWeightPerUnit;

    private int quantityOnHand;
}
