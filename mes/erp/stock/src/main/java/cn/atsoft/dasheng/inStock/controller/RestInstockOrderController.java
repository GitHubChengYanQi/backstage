package cn.atsoft.dasheng.inStock.controller;

import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.SpringContextHolder;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.inStock.model.params.RestInstockOrderParam;
import cn.atsoft.dasheng.inStock.model.params.RestOrderDetailParam;
import cn.atsoft.dasheng.inStock.model.params.RestOrderParam;
import cn.atsoft.dasheng.inStock.service.RestInstockOrderService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.model.result.RestOrderDetailResult;
import cn.atsoft.dasheng.service.IErpBase;
import cn.atsoft.dasheng.storehousePositionBind.model.result.StorehousePositionsBindResult;
import cn.atsoft.dasheng.storehousePositionBind.service.RestStorehousePositionsBindService;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 入库单控制器
 *
 * @author song
 * @Date 2021-10-06 09:43:44
 */
@RestController
@RequestMapping("/instockOrder/{version}")
@ApiVersion("2.0")
@Api(tags = "入库单")
public class RestInstockOrderController extends BaseController {
    @Autowired
    private RestInstockOrderService instockOrderService;
    @Autowired
    private RestStorehousePositionsBindService positionsBindService;
    /**
     * 订单查询接口
     *
     * @author Captain_Jazz
     * @Date 2023-02-28
     */
    @RequestMapping(value = "/showOrderList", method = RequestMethod.POST)
    @ApiOperation("订单查询接口")
    public PageInfo showOrderList(@RequestBody  RestOrderParam param){
         if (ToolUtil.isEmpty(param.getKeywords())){
            return new PageInfo();
        }
        IErpBase restOrderService = SpringContextHolder.getBean("RestOrderService");
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(param);
        try{
          return  restOrderService.getOrderList(stringObjectMap);
        }catch (Exception e){
            return new PageInfo();
        }
    }
    /**
     * 根据订单入库
     *
     * @author Captain_Jazz
     * @Date 2023-02-28
     */
    @RequestMapping(value = "/autoInStock", method = RequestMethod.POST)
    @ApiOperation("根据订单入库")
    public ResponseData showOrderList(@RequestBody RestInstockOrderParam param){

        return  ResponseData.success(instockOrderService.autoInStock(param));
    }

    /**
     * 订单详情查询接口
     *
     * @author Captain_Jazz
     * @Date 2023-02-28
     */
    @RequestMapping(value = "/showOrderDetailList", method = RequestMethod.POST)
    @ApiOperation("订单详情查询接口")
    public ResponseData showOrderDetailList(@RequestBody  RestOrderDetailParam param){
        IErpBase restOrderService = SpringContextHolder.getBean("RestOrderDetailService");
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(param);
        try{
            List<RestOrderDetailResult> orderDetailList = restOrderService.getOrderDetailList(stringObjectMap);
            List<Long> skuIds = orderDetailList.stream().map(RestOrderDetailResult::getSkuId).distinct().collect(Collectors.toList());
            List<StorehousePositionsBindResult> storehousePositionsBindResults = positionsBindService.resultBySkuIds(skuIds);
            for (RestOrderDetailResult restOrderDetailResult : orderDetailList) {
                List<Map<String,Object>> bindList = new ArrayList<>();
                for (StorehousePositionsBindResult positionBind : storehousePositionsBindResults) {
                    if (restOrderDetailResult.getSkuId().equals(positionBind.getSkuId())){
                        bindList.add(BeanUtil.beanToMap( positionBind.getStorehousePositionsResult()));
                    }
                }
                restOrderDetailResult.setBindPositions(bindList);
            }



            return  ResponseData.success(orderDetailList);
        }catch (Exception e){
            return ResponseData.success();
        }
    }
//    /**
//     * 订单详情查询接口
//     *
//     * @author Captain_Jazz
//     * @Date 2023-02-28
//     */
//    @RequestMapping(value = "/autoInstock", method = RequestMethod.POST)
//    @ApiOperation("订单详情查询接口")
//    public ResponseData autoInstock(RestOrderDetailParam param){
//        return ResponseData.success();
//    }

    /**
     * 订单详情查询接口
     *
     * @author Captain_Jazz
     * @Date 2023-02-28
     */
    @RequestMapping(value = "/countByBomId", method = RequestMethod.POST)
    @ApiOperation("订单详情查询接口")
    public ResponseData countByBomId(RestInstockOrderParam param){

        return ResponseData.success();
    }



}


