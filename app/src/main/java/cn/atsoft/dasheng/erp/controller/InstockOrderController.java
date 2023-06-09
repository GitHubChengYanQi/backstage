package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.action.Enum.InStockActionEnum;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.erp.entity.InstockOrder;
import cn.atsoft.dasheng.erp.model.params.InstockListParam;
import cn.atsoft.dasheng.erp.model.params.InstockOrderParam;
import cn.atsoft.dasheng.erp.model.request.InstockParams;
import cn.atsoft.dasheng.erp.model.result.InstockOrderResult;
import cn.atsoft.dasheng.erp.pojo.FreeInStockParam;
import cn.atsoft.dasheng.erp.pojo.InStockByOrderParam;
import cn.atsoft.dasheng.erp.service.InstockOrderService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.purchase.pojo.ThemeAndOrigin;
import cn.atsoft.dasheng.purchase.service.GetOrigin;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 入库单控制器
 *
 * @author song
 * @Date 2021-10-06 09:43:44
 */
@RestController
@RequestMapping("/instockOrder")
@Api(tags = "入库单")
public class InstockOrderController extends BaseController {
    @Autowired
    private InstockOrderService instockOrderService;
    @Autowired
    private GetOrigin getOrigin;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-06
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    @Permission
    public ResponseData addItem(@RequestBody InstockOrderParam instockOrderParam) {
        InstockOrder instockOrder = this.instockOrderService.add(instockOrderParam);
        return ResponseData.success(instockOrder);
    }


    @RequestMapping(value = "/judgeLoginUser", method = RequestMethod.GET)
    @ApiOperation("新增")
    public ResponseData judgeLoginUser() {
        boolean b = this.instockOrderService.judgeLoginUser();
        return ResponseData.success(b);
    }

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-06
     */
    @RequestMapping(value = "/checkNumberTrue", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData checkNumberTrue(@RequestBody @Valid InstockOrderParam instockOrderParam) {
        this.instockOrderService.checkNumberTrue(instockOrderParam.getInstockOrderId(), instockOrderParam.getState(), instockOrderParam.getActionType());
        return ResponseData.success();
    }

    @RequestMapping(value = "/document", method = RequestMethod.GET)
    public ResponseData document(@RequestParam("id") Long id, String type) {
        if (ToolUtil.isEmpty(id) || ToolUtil.isEmpty(type)) {
            throw new ServiceException(500, "缺少参数");
        }
        Object document = this.instockOrderService.document(id, type);
        return ResponseData.success(document);
    }

    /**
     * 通过质检创建入库单
     *
     * @author song
     * @Date 2021-10-06
     */
    @RequestMapping(value = "/addByQuality", method = RequestMethod.POST)
    public ResponseData addByQuality(@RequestBody InstockParams instockParams) {
        this.instockOrderService.addByQuality(instockParams);
        return ResponseData.success();
    }


    @RequestMapping(value = "/freeInStockByPositions", method = RequestMethod.POST)
    @ApiOperation("通过库位自由入库")
    public ResponseData freeInStockByPositions(@RequestBody FreeInStockParam freeInStockParam) {
        this.instockOrderService.freeInStockByPositions(freeInStockParam);
        return ResponseData.success();
    }

    /**
     * 自由入库
     *
     * @author song
     * @Date 2021-10-06
     */
    @RequestMapping(value = "/freeInstock", method = RequestMethod.POST)
    @ApiOperation("自由入库")
    @Permission
    public ResponseData freeInstock(@Valid @RequestBody FreeInStockParam freeInStockParam) {
        this.instockOrderService.freeInstock(freeInStockParam);
        return ResponseData.success();
    }

    /**
     * 通过入库单入库
     *
     * @author song
     * @Date 2021-10-06
     */
    @RequestMapping(value = "/inStockByOrder", method = RequestMethod.POST)
    @Permission
    public ResponseData inStockByOrder(@Valid @RequestBody InstockOrderParam param) {
        List<Long> inkindIds = this.instockOrderService.inStock(param);
        return ResponseData.success(inkindIds);
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-10-06
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody InstockOrderParam instockOrderParam) {
        InstockOrder detail = this.instockOrderService.getById(instockOrderParam.getInstockOrderId());
        InstockOrderResult result = new InstockOrderResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

        instockOrderService.formatDetail(result);
        if (ToolUtil.isNotEmpty(result.getOrigin())) {
            result.setThemeAndOrigin(getOrigin.getOrigin(JSON.parseObject(result.getOrigin(), ThemeAndOrigin.class)));
        }

        instockOrderService.formatResult(result);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-10-06
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    @Permission
    public PageInfo list(@RequestBody(required = false) InstockOrderParam instockOrderParam) {
        if (ToolUtil.isEmpty(instockOrderParam)) {
            instockOrderParam = new InstockOrderParam();
        }

        if (LoginContextHolder.getContext().isAdmin()) {
            return this.instockOrderService.findPageBySpec(instockOrderParam,null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
            return  this.instockOrderService.findPageBySpec(instockOrderParam,dataScope);
        }
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-10-06
     */
    @RequestMapping(value = "/noPageList", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData noPageList(@RequestBody(required = false) InstockOrderParam instockOrderParam) {
        if (ToolUtil.isEmpty(instockOrderParam)) {
            instockOrderParam = new InstockOrderParam();
        }
        return ResponseData.success(this.instockOrderService.findListBySpec(instockOrderParam));
    }


}


