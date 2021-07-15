package com.example.demo.controller;

import com.example.demo.bean.request.VpImmUsePointReq;
import com.example.demo.bean.request.VpInqPointReq;
import com.example.demo.bean.response.VpImmUsePointRes;
import com.example.demo.bean.response.VpInqPointRes;
import com.example.demo.service.CallBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/vp")
public class VPController {

    @Autowired
    private CallBackService callBackService;

    @Value("${vp.inqPoint.code}")
    private String inqPointCode;

    @Value("${vp.immUsePoint.code}")
    private String immUsePointCode;

    @PostMapping("/inqPoint")
    public VpInqPointRes inqPoint(
            @Valid @RequestBody VpInqPointReq vpInqPointReq
    ) {
        List<VpInqPointRes.Pnt> pnt = new LinkedList<>();
        pnt.add(
                VpInqPointRes.Pnt.builder()
                        .crdc_cd("0100")
                        .crdc_nm("비씨카드")
                        .crdc_rsp_cd(inqPointCode)
                        .crdc_rsp_msg(getMessage(inqPointCode))
                        .curr_pnt("12345")
                        .cvt_rt("1.0")
                        .avle_pnt("12345")
                        .build()
        );
        return VpInqPointRes.builder()
                .rsp_cd(inqPointCode)
                .rsp_msg(getMessage(inqPointCode))
                .etc_val("")
                .pnt(pnt)
                .build();
    }

    @PostMapping("/immUsePoint")
    public VpImmUsePointRes immUsePoint(
            @Valid @RequestBody VpImmUsePointReq vpImmUsePointReq
    ) {
        List<VpImmUsePointRes.Pnt> pnt = new LinkedList<>();
        pnt.add(
                VpImmUsePointRes.Pnt.builder()
                        .crdc_cd(vpImmUsePointReq.getPnt().get(0).getCrdc_cd())
                        .crdc_nm(vpImmUsePointReq.getPnt().get(0).getCrdc_nm())
                        .crdc_rsp_cd(immUsePointCode)
                        .crdc_rsp_msg(getMessage(immUsePointCode))
                        .curr_pnt("11345")
                        .cvt_rt("1.0")
                        .req_pnt("1000")
                        .build()
        );
        return VpImmUsePointRes.builder()
                .rsp_cd(immUsePointCode)
                .rsp_msg(getMessage(immUsePointCode))
                .rsp_dtm("20200414100000")
                .etc_val("")
                .pnt(pnt)
                .build();
    }

    private String getMessage(String pointCode){
        String message = "Error.";

        switch (pointCode) {
            case "0000":
                message = "성공";
                break;
            case "0001":
                message = "필수 파라미터 오류";
                break;
            case "0002":
                message = "유효하지 않은 데이터";
                break;
            case "0003":
                message = "미등록 가맹점ID";
                break;
            case "0004":
                message = "미등록 가맹점IDX";
                break;
            case "0005":
                message = "필수 약관 미동의 고객";
                break;
            case "0006":
                message = "거래번호 중복";
                break;
            case "0007":
                message = "예약 거래번호 없음";
                break;
            case "0008":
                message = "취소거래번호 없음";
                break;
            case "0009":
                message = "거래시간 초과";
                break;
            case "0010":
                message = "포인트한도 초과";
                break;
            case "0011":
                message = "사용 완료된 거래번호";
                break;
            case "0012":
                message = "카드사 오류";
                break;
            case "0013":
                message = "원천사 없음";
                break;
            case "0014":
                message = "총포인트 오류";
                break;
            case "0015":
                message = "요청건수 오류";
                break;
            case "0016":
                message = "암복호화 오류";
                break;
            case "0017":
                message = "미제공 원천사";
                break;
            case "0099":
                message = "결제진행취소";
                break;
        }
        return message;
    }
}
