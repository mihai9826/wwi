package org.mihaimadan.wwi.orders.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum {
    PENDING("PENDING"),
    PROCESSING("PROCESSING"),
    DISPATCHED("DISPATCHED");

    private String value;

}
