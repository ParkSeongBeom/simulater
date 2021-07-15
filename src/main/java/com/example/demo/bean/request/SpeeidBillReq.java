package com.example.demo.bean.request;

import lombok.Data;

@Data
public class SpeeidBillReq {
    private String apikey;
    private String member;
    private String merchant;
    private String customer;
    private Bill bill;

    @Data
    public static class Bill {
        private String bill_id;
        private String product_nm;
        private String price;
        private String hash;
        private String bill_type;
        private String bill_desc;
        private String request_pay_type;
        private String callbackURL;
    }
}
