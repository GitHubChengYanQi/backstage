package cn.atsoft.dasheng.protal.classPage.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.protal.classPage.entity.DaoxinPortalClass;
import cn.atsoft.dasheng.protal.classPage.model.params.DaoxinPortalClassParam;
import cn.atsoft.dasheng.protal.classPage.model.result.DaoxinPortalClassResult;
import cn.atsoft.dasheng.protal.classPage.service.DaoxinPortalClassService;
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
 * 分类导航控制器
 *
 * @author siqiang
 * @Date 2021-08-18 16:13:41
 */
@RestController
@RequestMapping("/daoxinPortalClass")
@Api(tags = "分类导航")
public class DaoxinPortalClassController extends BaseController {

    @Autowired
    private DaoxinPortalClassService daoxinPortalClassService;

    /**
     * 新增接口
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody DaoxinPortalClassParam daoxinPortalClassParam) {
        this.daoxinPortalClassService.add(daoxinPortalClassParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody DaoxinPortalClassParam daoxinPortalClassParam) {

        this.daoxinPortalClassService.update(daoxinPortalClassParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody DaoxinPortalClassParam daoxinPortalClassParam)  {
        this.daoxinPortalClassService.delete(daoxinPortalClassParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<DaoxinPortalClassResult> detail(@RequestBody DaoxinPortalClassParam daoxinPortalClassParam) {
        DaoxinPortalClass detail = this.daoxinPortalClassService.getById(daoxinPortalClassParam.getClassId());
        DaoxinPortalClassResult result = new DaoxinPortalClassResult();
        ToolUtil.copyProperties(detail, result);

        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<DaoxinPortalClassResult> list(@RequestBody(required = false) DaoxinPortalClassParam daoxinPortalClassParam) {
        if(ToolUtil.isEmpty(daoxinPortalClassParam)){
            daoxinPortalClassParam = new DaoxinPortalClassParam();
        }
        return this.daoxinPortalClassService.findPageBySpec(daoxinPortalClassParam);
    }




}


