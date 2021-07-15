package com.example.demo.bean.request;

import lombok.Data;

@Data
public class VpInqPointReq {
    private String mch_id;
    private String mch_idx;
    private String mch_nm;
    private String customer;
    private String ci;
    private String etc_val;
}
