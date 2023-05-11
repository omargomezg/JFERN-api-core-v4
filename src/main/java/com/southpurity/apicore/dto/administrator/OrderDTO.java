package com.southpurity.apicore.dto.administrator;

import lombok.Data;

@Data
public class OrderDTO {
    private Integer lockNumber;
    private Integer padlockKey;
    private String place;

}
