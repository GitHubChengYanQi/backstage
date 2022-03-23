package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.app.pojo.AllBom;
import cn.atsoft.dasheng.app.pojo.AllBomParam;
import cn.atsoft.dasheng.app.pojo.AllBomResult;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mes")
public class AllBomController {


    @RequestMapping(value = "/analysis", method = RequestMethod.POST)
    public ResponseData getBoms(@RequestBody AllBomParam param) {

        AllBom allBom = new AllBom();
        allBom.start(param.getSkuIds());
        AllBomResult allBomResult = allBom.getResult();
        return ResponseData.success(allBomResult);
    }

}
