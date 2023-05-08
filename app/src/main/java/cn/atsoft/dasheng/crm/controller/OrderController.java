package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.crm.entity.Order;
import cn.atsoft.dasheng.crm.model.params.OrderParam;
import cn.atsoft.dasheng.crm.model.result.OrderResult;
import cn.atsoft.dasheng.crm.service.OrderService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.CodingRules;
import cn.atsoft.dasheng.erp.service.CodingRulesService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.wedrive.file.model.params.WedriveFileParam;
import cn.atsoft.dasheng.wedrive.file.service.WedriveFileService;
import cn.atsoft.dasheng.wedrive.space.model.enums.TypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import java.io.IOException;


/**
 * 订单表控制器
 *
 * @author song
 * @Date 2022-02-23 09:48:33
 */
@RestController
@RequestMapping("/order")
@Api(tags = "订单表")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CodingRulesService codingRulesService;
    @Autowired
    private WedriveFileService wedriveFileService;


    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody @Valid OrderParam orderParam) {

        if (ToolUtil.isEmpty(orderParam.getCoding())) {
            CodingRules codingRules = codingRulesService.query().eq("module", "11").eq("state", 1).one();
            if (ToolUtil.isNotEmpty(codingRules)) {
                String coding = codingRulesService.backCoding(codingRules.getCodingRulesId());
                orderParam.setCoding(coding);
            } else {

                orderParam.setCoding(codingRulesService.genSerial());
            }
        }

        Order order = this.orderService.add(orderParam);
        return ResponseData.success(order);
    }

    /**
     * 订单生成合同
     * @param orderParam
     * @return
     */
    @RequestMapping(value = "/updateContract", method = RequestMethod.POST)
    @ApiOperation("生成合同")
    public ResponseData updateContract(@RequestBody OrderParam orderParam) {
        this.orderService.updateContract(orderParam.getOrderId(), orderParam.getContractParam());
        return ResponseData.success();
    }


    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody OrderParam orderParam) {
        OrderResult detail = this.orderService.getDetail(orderParam.getOrderId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    //TODO @Permission
    public PageInfo<OrderResult> list(@RequestBody(required = false) OrderParam orderParam) {
        if (ToolUtil.isEmpty(orderParam)) {
            orderParam = new OrderParam();
        }
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.orderService.findPageBySpec(orderParam,null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
            return  this.orderService.findPageBySpec(orderParam,dataScope);
        }
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/noPageList", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData noPageList(@RequestBody(required = false) OrderParam orderParam) {
        if (ToolUtil.isEmpty(orderParam)) {
            orderParam = new OrderParam();
        }
        if (LoginContextHolder.getContext().isAdmin()) {
            return ResponseData.success(this.orderService.findListBySpec(orderParam,null));
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
            return  ResponseData.success(this.orderService.findListBySpec(orderParam,dataScope));
        }
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/pendingProductionPlan", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData pendingProductionPlan(@RequestBody(required = false) OrderParam orderParam) {
        if (ToolUtil.isEmpty(orderParam)) {
            orderParam = new OrderParam();
        }
        return ResponseData.success(this.orderService.pendingProductionPlan(orderParam));
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/doneOrder", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData doneOrder(@RequestBody(required = false) OrderParam orderParam) {
        if (ToolUtil.isEmpty(orderParam)) {
            orderParam = new OrderParam();
        }
        if (ToolUtil.isEmpty(orderParam.getOrderId())){
            throw new ServiceException(500,"参数错误");
        }
        orderService.update(new Order(){{
            setStatus(99);
        }},new QueryWrapper<Order>().eq("order_id",orderParam.getOrderId()));
        return ResponseData.success();
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/pendingProductionPlanByContracts", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData pendingProductionPlanByContracts(@RequestBody(required = false) OrderParam orderParam) {
        if (ToolUtil.isEmpty(orderParam)) {
            orderParam = new OrderParam();
        }
        return ResponseData.success(this.orderService.pendingProductionPlanByContracts(orderParam));
    }
    /**
     * 企业微信微盘上传
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/wxUpload", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData wxUpload(@RequestBody(required = false) OrderParam orderParam)  {
        if (ToolUtil.isEmpty(orderParam)) {
            orderParam = new OrderParam();
        }
        return ResponseData.success(this.orderService.wxUpload(orderParam));
    }

    /**
     * 企业微信微盘上传
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/createWeDirvSpace", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData createWeDirvSpace(@RequestBody(required = false) OrderParam orderParam){
        if (ToolUtil.isEmpty(orderParam)) {
            orderParam = new OrderParam();
        }
        return ResponseData.success(this.orderService.createWeDirvSpace(orderParam));
    }

    /**
     * 绑定附件
     *
     * @author jazz
     * @Date 2023-03-29
     */
    @RequestMapping(value = "/addFile", method = RequestMethod.POST)
    @ApiOperation("列表")
    @Transactional
    public ResponseData addFile(@RequestBody(required = false) OrderParam orderParam) throws IOException, WxErrorException {
        if (ToolUtil.isEmpty(orderParam)) {
            orderParam = new OrderParam();
        }
        if (ToolUtil.isEmpty(orderParam.getOrderId()) || ToolUtil.isEmpty(orderParam.getMediaIds())) {
            throw new ServiceException(500,"参数错误");
        }
        orderParam.setFileId( StringUtils.join(orderParam.getMediaIds(),","));
        this.orderService.update(orderParam);
        OrderParam finalOrderParam = orderParam;
        wedriveFileService.add(new WedriveFileParam(){{
            setSpaceType(TypeEnum.order);
            setMediaIds(finalOrderParam.getMediaIds());
            setOrderId(finalOrderParam.getOrderId());
        }});
        return ResponseData.success();
    }
    /**
     * 绑定附件
     *
     * @author jazz
     * @Date 2023-03-29
     */
    @RequestMapping(value = "/getAllFile", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData getAllFile(@RequestBody(required = false) OrderParam orderParam){
        if (ToolUtil.isEmpty(orderParam)) {
            orderParam = new OrderParam();
        }
        if (ToolUtil.isEmpty(orderParam.getOrderId())) {
            throw new ServiceException(500,"参数错误");
        }

        return ResponseData.success(this.orderService.getAllFile(orderParam));
    }

}



