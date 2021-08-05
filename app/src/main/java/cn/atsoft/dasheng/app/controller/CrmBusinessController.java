package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.wrapper.BrandSelectWrapper;
import cn.atsoft.dasheng.app.wrapper.CrmBusinessSelectWrapper;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmBusiness;
import cn.atsoft.dasheng.app.model.params.CrmBusinessParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessResult;
import cn.atsoft.dasheng.app.service.CrmBusinessService;
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
 * 商机表控制器
 *
 * @author
 * @Date 2021-08-03 14:04:51
 */
@RestController
@RequestMapping("/crmBusiness")
@Api(tags = "商机表")
public class CrmBusinessController extends BaseController {

    @Autowired
    private CrmBusinessService crmBusinessService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-08-03
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody CrmBusinessParam crmBusinessParam) {
        Long add = this.crmBusinessService.add(crmBusinessParam);
        return ResponseData.success(add);
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-08-03
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody CrmBusinessParam crmBusinessParam) {

        this.crmBusinessService.update(crmBusinessParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-08-03
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody CrmBusinessParam crmBusinessParam)  {
        this.crmBusinessService.delete(crmBusinessParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-08-03
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<CrmBusinessResult> detail(@RequestBody CrmBusinessParam crmBusinessParam) {

        CrmBusinessResult bySpec = crmBusinessService.findBySpec(crmBusinessParam);
        return ResponseData.success(bySpec);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-08-03
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<CrmBusinessResult> list(@RequestBody(required = false) CrmBusinessParam crmBusinessParam) {
        if(ToolUtil.isEmpty(crmBusinessParam)){
            crmBusinessParam = new CrmBusinessParam();
        }
        return this.crmBusinessService.findPageBySpec(crmBusinessParam);
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
    List<Map<String,Object>> list = this.crmBusinessService.listMaps();
    CrmBusinessSelectWrapper factory = new CrmBusinessSelectWrapper(list);
    List<Map<String,Object>> result = factory.wrap();
    return ResponseData.success(result);
  }

}

