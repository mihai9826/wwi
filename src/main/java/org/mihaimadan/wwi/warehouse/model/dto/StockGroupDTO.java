package org.mihaimadan.wwi.warehouse.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockGroupDTO {
    private Long stockGroupId;

    private String stockGroupName;

}
