package cn.atsoft.dasheng.api.shop.controller;

import cn.atsoft.dasheng.model.response.ResponseData;

import cn.atsoft.dasheng.shop.classpage.entity.Classpojo;
import cn.atsoft.dasheng.shop.classpage.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClassController {
    @Autowired
    private ClassService service;

    @RequestMapping(value = "/getclass", method = RequestMethod.GET)
    public ResponseData getclass() {
        List<Classpojo> list = service.list();
        return ResponseData.success(list);
    }
}
