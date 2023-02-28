package cn.atsoft.dasheng.inStock.controller;

import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.SpringContextHolder;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.inStock.model.params.RestOrderDetailParam;
import cn.atsoft.dasheng.inStock.model.params.RestOrderParam;
import cn.atsoft.dasheng.inStock.service.RestInstockOrderService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.service.IErpBase;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;


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
    /**
     * 订单查询接口
     *
     * @author Captain_Jazz
     * @Date 2023-02-28
     */
    @RequestMapping(value = "/showOrderList", method = RequestMethod.POST)
    @ApiOperation("订单查询接口")
    public PageInfo showOrderList(RestOrderParam param){
        IErpBase restOrderService = SpringContextHolder.getBean("RestOrderService");
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(param);
        try{
          return  restOrderService.getOrderList(stringObjectMap);
        }catch (Exception e){
            return new PageInfo();
        }
    }

    /**
     * 订单详情查询接口
     *
     * @author Captain_Jazz
     * @Date 2023-02-28
     */
    @RequestMapping(value = "/showOrderDetailList", method = RequestMethod.POST)
    @ApiOperation("订单详情查询接口")
    public PageInfo showOrderDetailList(RestOrderDetailParam param){
        IErpBase restOrderService = SpringContextHolder.getBean("RestOrderDetailService");
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(param);
        try{
          return  restOrderService.getOrderDetailList(stringObjectMap);
        }catch (Exception e){
            return new PageInfo()   ;
        }
    }



}


