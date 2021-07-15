package com.example.demo.bean.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpeeidCallbackReq {
    private String apikey;
    private String bill_id;
    private String appr_pay_type;
    private String appr_dt;
    private String appr_price;
    private String appr_issuer;
    private String appr_issuer_cd;
    private String appr_issuer_num;
    private String appr_acquirer_cd;
    private String appr_acquirer_nm;
    private String appr_num;
    private String appr_monthly;
    private String appr_state;
    private String cms_tcp_res_code;
    private String appr_cash_num;
}
