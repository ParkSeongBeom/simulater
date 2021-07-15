package com.example.demo.bean.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class VpImmUsePointRes {
    private String rsp_cd;
    private String rsp_msg;
    private String etc_val;
    private String rsp_dtm;
    private List<Pnt> pnt;

    @Data
    @Builder
    public static class Pnt {
        private String crdc_cd;
        private String crdc_nm;
        private String crdc_rsp_cd;
        private String crdc_rsp_msg;
        private String curr_pnt;
        private String cvt_rt;
        private String req_pnt;
    }
}
