package cn.atsoft.dasheng.bom.controller;

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

        if (ToolUtil.isEmpty(bomParam.getVersion())) {
            throw new ServiceException(500, "版本号不能为空");
        }

        if (ToolUtil.isEmpty(bomParam.getSkuId())) {
            throw new ServiceException(500, "请选择物料");
        }

        RestSku restSkus =  restSkuService.getById(bomParam.getSkuId());
        if (ToolUtil.isEmpty(restSkus)) {
            throw new ServiceException(500,"该物料不存在");
        }

        Integer count = this.bomService.countBySkuIdAndVersion(bomParam.getSkuId(), bomParam.getVersion());
        if (count > 0) {
            throw new ServiceException(500, "版本号不能重复");
        }

        List<Long> skuIds = new ArrayList<>();
        List<RestBomDetailParam> restBomDetailParams = bomParam.getBomDetailParam();
        for (RestBomDetailParam bomDetailParam : restBomDetailParams) {
            skuIds.add(bomDetailParam.getSkuId());
        }

        List<RestSku> skuList = restSkuService.getByIds(skuIds);

        if (skuList.size() != restBomDetailParams.size()){
            throw new ServiceException(500, "物料详情错误");
        }

//        int i=0;
        List<RestBom> bomList = bomService.getBySkuIds(skuIds);
        //拦截循环添加
        this.bomService.cycleJudge(bomParam.getSkuId(), bomList);

        for (RestBomDetailParam bomDetailParam : restBomDetailParams) {
            //如果传入versionBomId 但是versionBomId 并没有匹配到正确数据
            if (bomList.stream().anyMatch(e->e.getSkuId().equals(bomDetailParam.getSkuId())) && (ToolUtil.isNotEmpty(bomDetailParam.getVersionBomId()) &&  bomList.stream().noneMatch(e->e.getBomId().equals(bomDetailParam.getVersionBomId())))) {
                throw new ServiceException(500,"子件bom版本信息错误");
            }
            //如果没传入 versionBomId 但是子件物料存在bom 请指定bom版本
            if (bomList.stream().anyMatch(e->e.getSkuId().equals(bomDetailParam.getSkuId())) && ToolUtil.isEmpty(bomDetailParam.getVersionBomId())) {
                throw new ServiceException(500,"请指定子件bom版本");
            }
        }
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
}
