package com.example.demo.bean.request;

import lombok.Data;

import java.util.List;

@Data
public class VpImmUsePointReq {
    private String mch_id;
    private String mch_idx;
    private String mch_nm;
    private String sbj_clss;
    private String ci;
    private String pnt_tot;
    private String pnt_cnt;
    private List<Pnt> pnt;
    private String etc_val;

    @Data
    public static class Pnt {
        private String crdc_cd;
        private String crdc_nm;
        private String req_pnt;
    }
}
