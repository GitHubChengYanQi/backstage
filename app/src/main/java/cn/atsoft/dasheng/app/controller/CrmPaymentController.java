package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.wrapper.BrandSelectWrapper;
import cn.atsoft.dasheng.app.wrapper.CrmPaymentSelectWrapper;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmPayment;
import cn.atsoft.dasheng.app.model.params.CrmPaymentParam;
import cn.atsoft.dasheng.app.model.result.CrmPaymentResult;
import cn.atsoft.dasheng.app.service.CrmPaymentService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 付款信息表控制器
 *
 * @author ta
 * @Date 2021-07-26 13:48:54
 */
@RestController
@RequestMapping("/crmPayment")
@Api(tags = "付款信息表")
public class CrmPaymentController extends BaseController {

    @Autowired
    private CrmPaymentService crmPaymentService;

    /**
     * 新增接口
     *
     * @author ta
     * @Date 2021-07-26
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody CrmPaymentParam crmPaymentParam) {
        this.crmPaymentService.add(crmPaymentParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author ta
     * @Date 2021-07-26
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody CrmPaymentParam crmPaymentParam) {

        this.crmPaymentService.update(crmPaymentParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author ta
     * @Date 2021-07-26
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody CrmPaymentParam crmPaymentParam)  {
        this.crmPaymentService.delete(crmPaymentParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author ta
     * @Date 2021-07-26
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<CrmPaymentResult> detail(@RequestBody CrmPaymentParam crmPaymentParam) {
        CrmPayment detail = this.crmPaymentService.getById(crmPaymentParam.getPaymentId());
        CrmPaymentResult result = new CrmPaymentResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author ta
     * @Date 2021-07-26
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<CrmPaymentResult> list(@RequestBody(required = false) CrmPaymentParam crmPaymentParam) {
        if(ToolUtil.isEmpty(crmPaymentParam)){
            crmPaymentParam = new CrmPaymentParam();
        }
        return this.crmPaymentService.findPageBySpec(crmPaymentParam);
    }



  /**
   * 选择列表
   *
   * @author 1
   * @Date 2021-07-14
   */
  @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
  @ApiOperation("Select数据接口")
  public ResponseData<List<Map<String,Object>>> listSelect() {
    List<Map<String,Object>> list = this.crmPaymentService.listMaps();
    CrmPaymentSelectWrapper factory = new CrmPaymentSelectWrapper(list);
    List<Map<String,Object>> result = factory.wrap();
    return ResponseData.success(result);
  }
}


