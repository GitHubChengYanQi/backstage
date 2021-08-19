package cn.atsoft.dasheng.api.shop.controller;

import cn.atsoft.dasheng.portal.banner.model.response.ResponseData;
import cn.atsoft.dasheng.shop.classdifferencedetail.entity.ClassDifferenceDetails;
import cn.atsoft.dasheng.shop.classdifferencedetail.service.ClassDifferenceDetailsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClassDifdetailsController {
    @Autowired
    private ClassDifferenceDetailsService service;

    @RequestMapping(value = "/getdetails", method = RequestMethod.GET)
    public ResponseData getdetails(@RequestParam("classId") Long classId) {
        QueryWrapper<ClassDifferenceDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("class_difference_id", classId);
        List<ClassDifferenceDetails> list = service.list(queryWrapper);
        return ResponseData.success(list);
    }
}
