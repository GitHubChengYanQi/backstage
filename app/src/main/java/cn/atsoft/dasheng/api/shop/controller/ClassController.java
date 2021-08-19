package cn.atsoft.dasheng.api.shop.controller;

import cn.atsoft.dasheng.portal.banner.model.response.ResponseData;
import cn.atsoft.dasheng.protal.classPage.entity.DaoxinPortalClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClassController {
    @Autowired
    private cn.atsoft.dasheng.protal.classPage.service.DaoxinPortalClassService service;

    @RequestMapping(value = "/getclass", method = RequestMethod.GET)
    public ResponseData getclass() {
        List<DaoxinPortalClass> list = service.list();
        return ResponseData.success(list);
    }
}
