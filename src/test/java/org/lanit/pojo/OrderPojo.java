package org.lanit.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class OrderPojo {

    private Long id;
    private int petId;
    private int quantity;
    private Date shipDate;
    private String status;
    private boolean complete;

}
