package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.Bom;
import cn.atsoft.dasheng.erp.model.params.BomDetailParam;
import cn.atsoft.dasheng.erp.model.params.BomParam;
import cn.atsoft.dasheng.erp.model.result.BomResult;
import cn.atsoft.dasheng.erp.model.result.SkuListResult;
import cn.atsoft.dasheng.erp.service.BomService;
import cn.atsoft.dasheng.erp.service.SkuListService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bom")
@Api("bom主表")
public class BomsController {

    @Autowired
    private BomService bomService;

    @Autowired
    private SkuListService skuListService;

    /**
     * 新增接口
     */

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody BomParam bomParam) {
        if (ToolUtil.isEmpty(bomParam.getVersion())) {
            throw new ServiceException(500, "版本号不能为空");
        }

        Integer count = this.bomService.countBySkuIdAndVersion(bomParam.getSkuId(), bomParam.getVersion());
        if (count > 0) {
            throw new ServiceException(500, "版本号不能重复");
        }

        if (ToolUtil.isNotEmpty(bomParam.getBomDetailParam()) && ToolUtil.isNotEmpty(bomParam.getSkuId())) {
            for (BomDetailParam bomDetailParam : bomParam.getBomDetailParam()) {
                if (bomDetailParam.getSkuId().equals(bomParam.getSkuId())) {
                    throw new ServiceException(500, "选择物料重复,请重新选择");
                }
            }
        }



//       //当 BomDetailParam 不为空时
//       if (ToolUtil.isNotEmpty(bomParam.getBomDetailParam())) {
//           //循环遍历 bomParam.getBomDetailParam()
//           for (BomDetailParam param : bomParam.getBomDetailParam()) {
//               if (ToolUtil.isEmpty(param.getVersionBomId())) {
//                   throw new ServiceException(500,"请设置版本号对应的BomId");
//               }
//           }
//       }

        this.bomService.add(bomParam);
        return ResponseData.success();
    }
}
