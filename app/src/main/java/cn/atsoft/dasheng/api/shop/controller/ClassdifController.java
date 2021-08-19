package cn.atsoft.dasheng.api.shop.controller;

import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.shop.classdifference.entity.ClassDifference;
import cn.atsoft.dasheng.shop.classdifference.service.ClassDifferenceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api")
public class ClassdifController {
    @Autowired
    private ClassDifferenceService service;

    @RequestMapping(value = "/getclassdifference", method = RequestMethod.POST)
    public ResponseData getclassdifference(@RequestBody cn.atsoft.dasheng.shop.classpage.model.params.ClassParam classParam) {
        QueryWrapper<ClassDifference> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("class_id", classParam.getClassId()).orderByAsc("sort").in("display",1);
        List<ClassDifference> list = service.list(queryWrapper);
        return ResponseData.success(list);
    }
}
