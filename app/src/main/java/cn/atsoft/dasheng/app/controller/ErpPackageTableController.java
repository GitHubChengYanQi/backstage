package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.model.params.BusinessDetailedParam;
import cn.atsoft.dasheng.app.model.params.ErpPackageParam;
import cn.atsoft.dasheng.app.wrapper.ErpPackageTableSelectWrapper;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ErpPackageTable;
import cn.atsoft.dasheng.app.model.params.ErpPackageTableParam;
import cn.atsoft.dasheng.app.model.result.ErpPackageTableResult;
import cn.atsoft.dasheng.app.service.ErpPackageTableService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;


/**
 * 套餐分表控制器
 *
 * @author qr
 * @Date 2021-08-04 11:01:43
 */
@RestController
@RequestMapping("/erpPackageTable")
@Api(tags = "套餐分表")
public class ErpPackageTableController extends BaseController {

    @Autowired
    private ErpPackageTableService erpPackageTableService;

    /**
     * 新增接口
     *
     * @author qr
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ErpPackageTableParam erpPackageTableParam) {
        this.erpPackageTableService.add(erpPackageTableParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author qr
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ErpPackageTableParam erpPackageTableParam) {

//        this.erpPackageTableService.update(erpPackageTableParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author qr
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ErpPackageTableParam erpPackageTableParam)  {
//        this.erpPackageTableService.delete(erpPackageTableParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author qr
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ErpPackageTableParam erpPackageTableParam) {
        ErpPackageTable detail = this.erpPackageTableService.getById(erpPackageTableParam.getId());
        ErpPackageTableResult result = new ErpPackageTableResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author qr
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ErpPackageTableResult> list(@RequestBody(required = false) ErpPackageTableParam erpPackageTableParam) {
        if(ToolUtil.isEmpty(erpPackageTableParam)){
            erpPackageTableParam = new ErpPackageTableParam();
        }
//        return this.erpPackageTableService.findPageBySpec(erpPackageTableParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.erpPackageTableService.findPageBySpec(erpPackageTableParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.erpPackageTableService.findPageBySpec(erpPackageTableParam, dataScope);
        }
    }


  /**
   * 选择列表
   *
   * @author 1
   * @Date 2021-07-14
   */
  @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
  @ApiOperation("Select数据接口")
  public ResponseData listSelect() {
      QueryWrapper<ErpPackageTable> packageTableQueryWrapper = new QueryWrapper<>();
    List<Map<String,Object>> list = this.erpPackageTableService.listMaps(packageTableQueryWrapper);
    ErpPackageTableSelectWrapper factory = new ErpPackageTableSelectWrapper(list);
    List<Map<String,Object>> result = factory.wrap();
    return ResponseData.success(result);
  }
  @RequestMapping(value = "/batchAdd", method = RequestMethod.POST)
  @ApiOperation("批量新增")
  public ResponseData batchAdd(@RequestBody BusinessDetailedParam businessDetailedParam) {
      this.erpPackageTableService.batchAdd(businessDetailedParam);
      return ResponseData.success();
    }

}


