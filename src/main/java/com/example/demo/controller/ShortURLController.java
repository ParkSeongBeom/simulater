package com.example.demo.controller;

import com.example.demo.service.CallBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class ShortURLController {

    @Autowired
    private CallBackService callBackService;

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/index.html");
        return modelAndView;
    }

    @RequestMapping("/test/{actionId}")
    public ModelAndView shortURL(
            @PathVariable("actionId") String actionId
    ) {
        ModelAndView modelAndView = callBackService.callback(actionId);
        modelAndView.setViewName("/speed.html");
        return modelAndView;
    }
}
