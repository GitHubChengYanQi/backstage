package cn.atsoft.dasheng.drafts.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.drafts.entity.Drafts;
import cn.atsoft.dasheng.drafts.model.params.DraftsParam;
import cn.atsoft.dasheng.drafts.model.result.DraftsResult;
import cn.atsoft.dasheng.drafts.service.DraftsService;
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
 * 草稿箱控制器
 *
 * @author Captain_Jazz
 * @Date 2022-04-21 13:25:01
 */
@RestController
@RequestMapping("/drafts")
@Api(tags = "草稿箱")
public class DraftsController extends BaseController {

    @Autowired
    private DraftsService draftsService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-04-21
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody DraftsParam draftsParam) {
        this.draftsService.add(draftsParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-04-21
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody DraftsParam draftsParam) {

        this.draftsService.update(draftsParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2022-04-21
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody DraftsParam draftsParam)  {
        this.draftsService.delete(draftsParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-04-21
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody DraftsParam draftsParam) {
        Drafts detail = this.draftsService.getById(draftsParam.getDraftsId());
        DraftsResult result = new DraftsResult();
        ToolUtil.copyProperties(detail, result);
        draftsService.format(new ArrayList<DraftsResult>(){{
            add(result);
        }});
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-04-21
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<DraftsResult> list(@RequestBody(required = false) DraftsParam draftsParam) {
        if(ToolUtil.isEmpty(draftsParam)){
            draftsParam = new DraftsParam();
        }
        return this.draftsService.findPageBySpec(draftsParam);
    }




}


