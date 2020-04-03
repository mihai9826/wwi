package org.mihaimadan.wwi.orders.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum StatusEnum {
    PENDING("PENDING"),
    PROCESSING("PROCESSING"),
    DISPATCHED("DISPATCHED");

    private String value;

}
