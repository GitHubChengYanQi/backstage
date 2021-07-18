package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.wrapper.AdressSelectWrapper;
import cn.atsoft.dasheng.app.wrapper.LalSelectWrapper;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Lal;
import cn.atsoft.dasheng.app.model.params.LalParam;
import cn.atsoft.dasheng.app.model.result.LalResult;
import cn.atsoft.dasheng.app.service.LalService;
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
 * 经纬度表控制器
 *
 * @author
 * @Date 2021-07-16 12:55:35
 */
@RestController
@RequestMapping("/lal")
@Api(tags = "经纬度表")
public class LalController extends BaseController {

    @Autowired
    private LalService lalService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-07-16
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody LalParam lalParam) {
        this.lalService.add(lalParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-07-16
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody LalParam lalParam) {

        this.lalService.update(lalParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-07-16
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody LalParam lalParam)  {
        this.lalService.delete(lalParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-07-16
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<LalResult> detail(@RequestBody LalParam lalParam) {
        Lal detail = this.lalService.getById(lalParam.getLalId());
        LalResult result = new LalResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-07-16
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<LalResult> list(@RequestBody(required = false) LalParam lalParam) {
        if(ToolUtil.isEmpty(lalParam)){
            lalParam = new LalParam();
        }
        return this.lalService.findPageBySpec(lalParam);
    }

  @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
  @ApiOperation("Select数据接口")
  public ResponseData<List<Map<String,Object>>> listSelect() {
    List<Map<String,Object>> list = this.lalService.listMaps();
    LalSelectWrapper factory = new LalSelectWrapper(list);
    List<Map<String,Object>> result = factory.wrap();
    return ResponseData.success(result);
  }


}


