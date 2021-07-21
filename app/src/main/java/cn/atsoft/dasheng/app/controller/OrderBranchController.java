package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.wrapper.OrderBranchSelectWrapper;
import cn.atsoft.dasheng.app.wrapper.OrderSelectWrapper;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.OrderBranch;
import cn.atsoft.dasheng.app.model.params.OrderBranchParam;
import cn.atsoft.dasheng.app.model.result.OrderBranchResult;
import cn.atsoft.dasheng.app.service.OrderBranchService;
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
 * 订单分表控制器
 *
 * @author ta
 * @Date 2021-07-20 16:22:28
 */
@RestController
@RequestMapping("/orderBranch")
@Api(tags = "订单分表")
public class OrderBranchController extends BaseController {

    @Autowired
    private OrderBranchService orderBranchService;

    /**
     * 新增接口
     *
     * @author ta
     * @Date 2021-07-20
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody OrderBranchParam orderBranchParam) {
        this.orderBranchService.add(orderBranchParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author ta
     * @Date 2021-07-20
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody OrderBranchParam orderBranchParam) {

        this.orderBranchService.update(orderBranchParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author ta
     * @Date 2021-07-20
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody OrderBranchParam orderBranchParam)  {
        this.orderBranchService.delete(orderBranchParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author ta
     * @Date 2021-07-20
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<OrderBranchResult> detail(@RequestBody OrderBranchParam orderBranchParam) {
        OrderBranch detail = this.orderBranchService.getById(orderBranchParam.getId());
        OrderBranchResult result = new OrderBranchResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author ta
     * @Date 2021-07-20
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<OrderBranchResult> list(@RequestBody(required = false) OrderBranchParam orderBranchParam) {
        if(ToolUtil.isEmpty(orderBranchParam)){
            orderBranchParam = new OrderBranchParam();
        }
        return this.orderBranchService.findPageBySpec(orderBranchParam);
    }



  /**
   * 选择列表
   *
   * @author ta
   * @Date 2021-07-19
   */
  @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
  @ApiOperation("Select数据接口")
  public ResponseData<List<Map<String,Object>>> listSelect() {
    List<Map<String,Object>> list = this.orderBranchService.listMaps();
    OrderBranchSelectWrapper factory = new OrderBranchSelectWrapper(list);
    List<Map<String,Object>> result = factory.wrap();
    return ResponseData.success(result);
  }
}


