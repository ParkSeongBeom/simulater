package com.example.demo.service;

import com.example.demo.bean.request.SpeeidBillReq;
import com.example.demo.bean.request.SpeeidCallbackReq;
import com.example.demo.bean.response.SpeeidCallbackRes;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class CallBackService {

    @Value("${speeid.bill.appr_state}")
    private String appr_state;

    private Map<String, SpeeidBillReq> speeidBillReqMap = new HashMap<>();

    public void setCallbackReq(String actionId, SpeeidBillReq speeidBillReq){
        speeidBillReqMap.put(actionId,speeidBillReq);
    }

    public ModelAndView callback(String actionId){
        if(actionId==null || speeidBillReqMap.get(actionId)==null){
            log.error("No such data in speeidBillReqMap - actionId : {}",actionId);
        }else{
            try{
                SpeeidBillReq speeidBillReq = speeidBillReqMap.get(actionId);

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                SpeeidCallbackReq speeidCallbackReq = SpeeidCallbackReq.builder()
                        .apikey(speeidBillReq.getApikey())
                        .bill_id(speeidBillReq.getBill().getBill_id())
                        .appr_price(speeidBillReq.getBill().getPrice())
                        .appr_num("test")
                        .appr_state(appr_state)
                        .build();
                HttpEntity<SpeeidCallbackReq> entity = new HttpEntity<>(speeidCallbackReq, headers);

                ResponseEntity<String> responseEntity = restTemplate.exchange(
                        speeidBillReq.getBill().getCallbackURL()
                        , HttpMethod.POST
                        , entity
//                        , new ParameterizedTypeReference<>() {}
                        , String.class
                );

                SpeeidCallbackRes speeidCallbackRes = new ObjectMapper().readValue(responseEntity.getBody(),SpeeidCallbackRes.class);
                log.info(speeidCallbackRes.toString());
            }catch(Exception e){
                e.printStackTrace();
            }
            speeidBillReqMap.remove(actionId);
        }
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }
}
