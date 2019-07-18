package com.real.name.device.controller;

import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import com.real.name.common.utils.JedisService;
import com.real.name.device.service.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/access")
public class AccessController {

    @Autowired
    private AccessService accessService;

    @Autowired
    private JedisService.JedisStrings jedisStrings;

    /**
     * 获取身份证索引号
     */
    @GetMapping("/getCardIndex")
    public ResultVo getCardIndex(@RequestParam("deviceId") String deviceId) {
        String cardIndex = (String) jedisStrings.get(deviceId);
        if (!StringUtils.hasText(cardIndex)) {
            return ResultVo.failure(ResultError.GET_CARD_INDEX_ERROR);
        }
        return ResultVo.success(cardIndex);
    }

}
