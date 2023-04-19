package cn.atsoft.dasheng.bom.controller;


import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.bom.entity.RestBom;
import cn.atsoft.dasheng.bom.model.params.RestBomDetailParam;
import cn.atsoft.dasheng.bom.model.params.RestBomParam;
import cn.atsoft.dasheng.bom.service.RestBomService;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.sku.entity.RestSku;
import cn.atsoft.dasheng.goods.sku.service.RestSkuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.bean.BeanUtil;
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
@RequestMapping("/bom/{version}")
@ApiVersion("2.0")
@Api(tags = "bom管理")
public class RestBomController {

    @Autowired
    private RestBomService bomService;

    @Autowired
    private RestSkuService restSkuService;


    /**
     * 新增接口
     */

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RestBomParam bomParam) {


//
//
//        for (RestBom restBom : bomList) {
//            for (RestBomDetailParam bomDetailParam : restBomDetailParams) {
//
//                if(restBom.getBomId().equals(bomDetailParam.getVersionBomId())){
//
//                }
//            }
//        }


//        for (RestBom restBom : bomList) {
//            for (RestSku restSku : skuList) {
//                if(restBom.getSkuId().equals(restSku.getSkuId()) && restBom.getBomId().equals()){
//                    i++;
//                }
//            }
//        }
//        if(bomList.size() != i){
//            throw new ServiceException(500, "物料详情错误Bom版本选择错误");
//        }

        this.bomService.add(bomParam);
        return ResponseData.success();
    }
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo list(@RequestBody RestBomParam bomParam) {

        return this.bomService.findPageBySpec(bomParam);
    }
    @RequestMapping(value = "/getByBomId", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData getByBomId(@RequestBody RestBomParam bomParam) {
        return ResponseData.success(this.bomService.getByBomId(bomParam.getBomId(),bomParam.getNumber()));
    }
}
