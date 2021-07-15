package com.example.demo.bean.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpeeidBillRes {
    private String code;
    private String msg;
    private String apikey;
    private String member;
    private String merchant;
    private String bill_id;
    private String hash;
    private String customer;
    private String shortURL;
}
