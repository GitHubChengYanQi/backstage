package cn.atsoft.dasheng.general.controller;

import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.general.model.params.GeneralParam;
import cn.atsoft.dasheng.general.model.result.BomListResult;
import cn.atsoft.dasheng.general.model.result.ClassListResult;
import cn.atsoft.dasheng.general.model.result.GeneralResult;
import cn.atsoft.dasheng.general.service.impl.GeneralServiceImpl;
import cn.atsoft.dasheng.model.response.ResponseData;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/general")
public class GeneralController extends BaseController {
    @Autowired
    private GeneralServiceImpl generalService;


    /**
     * 查询通过模糊查询spuClassName 找到对应的数据
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("查询")
    public ResponseData spuClassListbyspuClassName(@RequestBody GeneralParam generalParam){
        GeneralResult generalResult = new GeneralResult();
        if (ToolUtil.isEmpty(generalParam.getKeyWord())){
            return ResponseData.success(generalResult);
        }

        List<ClassListResult> classList = this.generalService.listByspuClassName(generalParam.getKeyWord());
        generalResult.setClassListResults(classList);

        List<BomListResult> bomList = this.generalService.listBySkuName(generalParam.getKeyWord());
        generalResult.setBomListResults(bomList);

        return ResponseData.success(generalResult);
    }
}
