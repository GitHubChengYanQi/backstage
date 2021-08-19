package cn.atsoft.dasheng.api.shop.controller;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.shop.classdifference.entity.ClassDifference;
import cn.atsoft.dasheng.shop.classdifference.model.result.ClassDifferenceResult;
import cn.atsoft.dasheng.shop.classdifference.service.ClassDifferenceService;
import cn.atsoft.dasheng.shop.classdifference.service.impl.ClassDifferenceServiceImpl;
import cn.atsoft.dasheng.shop.classdifferencedetail.entity.ClassDifferenceDetails;
import cn.atsoft.dasheng.shop.classdifferencedetail.model.result.ClassDifferenceDetailsResult;
import cn.atsoft.dasheng.shop.classdifferencedetail.service.ClassDifferenceDetailsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.atsoft.dasheng.shop.classpage.model.params.ClassParam;


import javax.tools.Tool;
import java.util.ArrayList;
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
