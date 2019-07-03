package com.real.name.httptest;

import com.real.name.common.result.ResultVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class TestController {


    /**
     * get方法测试
     * @return
     */
    @GetMapping("testGet")
    public ResultVo testGet(String name, String password, Integer gender) {
        System.out.println(name);
        System.out.println(password);
        System.out.println(gender);
        return ResultVo.success();
    }

    @PostMapping("testPost")
    public ResultVo testPost(@RequestParam("name") String name, @RequestParam("password") String password, @RequestParam("gender") Integer gender) {
        System.out.println(name);
        System.out.println(password);
        System.out.println(gender);
        String data = "name: " + name + "password: " + password + "gender: " + gender;
        return ResultVo.success(data);
    }

}
