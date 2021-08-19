package cn.atsoft.dasheng.api.shop.controller;

import cn.atsoft.dasheng.portal.banner.model.response.ResponseData;
import cn.atsoft.dasheng.shop.classdifference.entity.ClassDifference;
import cn.atsoft.dasheng.shop.classdifference.service.ClassDifferenceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/api")
public class ClassdifController {
    @Autowired
    private ClassDifferenceService service;

    @RequestMapping(value = "/getclassdifference", method = RequestMethod.GET)
    public ResponseData getclassdifference(@RequestParam("classId") Long classId) {
        QueryWrapper<ClassDifference> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("class_id", classId).orderByAsc("sort");
        List<ClassDifference> list = service.list(queryWrapper);
        return ResponseData.success(list);
    }
}
