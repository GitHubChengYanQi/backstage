package cn.atsoft.dasheng.api.shop.controller;

import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.shop.classDifference.model.result.ClassDifferenceResult;
import cn.atsoft.dasheng.shop.classDifference.service.ClassDifferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.atsoft.dasheng.shop.classPage.model.params.ClassParam;


import java.util.List;

@RestController
@RequestMapping("/api")
public class ClassdifController {
    @Autowired
    private ClassDifferenceService service;


    @RequestMapping(value = "/getclassdifference", method = RequestMethod.POST)
    public ResponseData getclassdifference(@RequestBody ClassParam classParam) {
        List<ClassDifferenceResult> getdetalis = service.getdetalis(classParam.getClassId());
        return  ResponseData.success(getdetalis);
    }

}
