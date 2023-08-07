package com.anji.captcha.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.xxw.shop.module.common.response.ServerResponseEntity;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ua/captcha")
public class CaptchaController {

    @Resource
    private CaptchaService captchaService;

    @PostMapping({"/get"})
    public ServerResponseEntity<ResponseModel> get(@RequestBody CaptchaVO captchaVO) {
        return ServerResponseEntity.success(captchaService.get(captchaVO));
    }

    @PostMapping({"/check"})
    public ServerResponseEntity<ResponseModel> check(@RequestBody CaptchaVO captchaVO) {
        return ServerResponseEntity.success(captchaService.check(captchaVO));
    }

}