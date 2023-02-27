package cn.atsoft.dasheng.purchase.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.purchase.entity.RestOrder;
import cn.atsoft.dasheng.purchase.model.params.RestOrderParam;
import cn.atsoft.dasheng.purchase.model.result.RestOrderResult;
import cn.atsoft.dasheng.purchase.model.result.RestOrderSimpleResult;
import cn.atsoft.dasheng.purchase.service.RestOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * 订单表控制器
 *
 * @author song
 * @Date 2022-02-23 09:48:33
 */
@RestController
@RequestMapping("/order/{version}")
@ApiVersion("2.0")
@Api(tags = "订单表")
public class RestOrderController extends BaseController {

    @Autowired
    private RestOrderService orderService;


    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody @Valid RestOrderParam orderParam) {

//        if (ToolUtil.isEmpty(orderParam.getCoding())) {
//            CodingRules codingRules = codingRulesService.query().eq("module", "11").eq("state", 1).one();
//            if (ToolUtil.isNotEmpty(codingRules)) {
//                String coding = codingRulesService.backCoding(codingRules.getCodingRulesId());
//                orderParam.setCoding(coding);
//            } else {
//                throw new ServiceException(500, "请配置采购单据自动生成编码规则");
//            }
//        }

        RestOrder order = this.orderService.add(orderParam);
        return ResponseData.success(order);
    }






    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<RestOrderResult> list(@RequestBody(required = false) RestOrderParam orderParam) {
        if (ToolUtil.isEmpty(orderParam)) {
            orderParam = new RestOrderParam();
        }
        return this.orderService.findPageBySpec(orderParam);
    }


    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/simpleList", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo simpleList(@RequestBody(required = false) RestOrderParam orderParam) {
        if (ToolUtil.isEmpty(orderParam)) {
            orderParam = new RestOrderParam();
        }
        PageInfo<RestOrderSimpleResult> pageBySpec = this.orderService.simpleFindPageBySpec(orderParam);
        return pageBySpec;
    }



}


