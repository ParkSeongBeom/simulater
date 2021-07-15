package com.example.demo.controller;

import com.example.demo.bean.request.SpeeidBillReq;
import com.example.demo.bean.response.SpeeidBillRes;
import com.example.demo.service.CallBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/if")
public class SpeeidController {

    @Autowired
    private CallBackService callBackService;

    @Value("${speeid.shortUrl.ip}")
    private String shortUrlIp;

    @Value("${speeid.shortUrl.port}")
    private String shortUrlPort;

    @Value("${speeid.bill.code}")
    private String billCode;

    @PostMapping("/bill/url")
    public SpeeidBillRes bill(
            @Valid @RequestBody SpeeidBillReq speeidBillReq
    ) {
        String actionId = random();
        callBackService.setCallbackReq(actionId,speeidBillReq);
        String message = "Error.";

        switch (billCode) {
            case "9999": {
                message = "특정되지 않은 실패. (입력값 오류)";
                break;
            }
            case "9800": {
                message = "이미 사용된 Bill ID";
                break;
            }
            case "9980": {
                message = "Bill ID에 해당하는 청구서 없음";
                break;
            }
            case "9870": {
                message = "동기화 대상 없음 (Call Back URL오류)";
                break;
            }
            case "5000": {
                message = "시스템 오류";
                break;
            }
            case "1000": {
                message = "개시된 매장이 없습니다";
                break;
            }
            case "1003": {
                message = "DB 에 정보 없음. (APIKey, Member 등)";
                break;
            }
            case "1010": {
                message = "사용자 또는 매장정보를 확인할 수 없습니다.";
                break;
            }
            case "1201": {
                message = "잔여 발송톡이 없음. (충전필요)";
                break;
            }
            case "1205": {
                message = "청구금액은 100원 이상이어야 함.";
                break;
            }
            case "1500": {
                message = "Member 가 존재하지 않습니다.";
                break;
            }
            case "1700": {
                message = "기 가입된 매장입니다.";
                break;
            }
        }

        return SpeeidBillRes.builder()
                .code(billCode)
                .msg(message)
                .apikey(speeidBillReq.getApikey())
                .member(speeidBillReq.getMember())
                .merchant(speeidBillReq.getMerchant())
                .customer(speeidBillReq.getCustomer())
                .bill_id(speeidBillReq.getBill().getBill_id())
                .hash(speeidBillReq.getBill().getHash())
                .shortURL("http://"+shortUrlIp+":"+shortUrlPort+"/test/"+actionId)
                .build();
    }

    private String random(){
        StringBuffer temp = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0:
                    // a-z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    // A-Z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    // 0-9
                    temp.append((rnd.nextInt(10)));
                    break;
            }
        }
        return temp.toString();
    }
}
