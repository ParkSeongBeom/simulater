package com.example.demo.bean.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString
@Getter
@Setter
public class SpeeidCallbackRes {
    private String apikey;
    private String bill_id;
    private String member;
    private String merchant;
    private String code;
    private String msg;
}
