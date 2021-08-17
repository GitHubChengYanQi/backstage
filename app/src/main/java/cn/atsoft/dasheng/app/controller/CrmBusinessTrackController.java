package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmBusinessTrack;
import cn.atsoft.dasheng.app.model.params.CrmBusinessTrackParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessTrackResult;
import cn.atsoft.dasheng.app.service.CrmBusinessTrackService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 商机跟踪表控制器
 *
 * @author 
 * @Date 2021-08-04 08:30:07
 */
@RestController
@RequestMapping("/crmBusinessTrack")
@Api(tags = "商机跟踪表")
public class CrmBusinessTrackController extends BaseController {

    @Autowired
    private CrmBusinessTrackService crmBusinessTrackService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody CrmBusinessTrackParam crmBusinessTrackParam) {
        this.crmBusinessTrackService.add(crmBusinessTrackParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody CrmBusinessTrackParam crmBusinessTrackParam) {

        this.crmBusinessTrackService.update(crmBusinessTrackParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody CrmBusinessTrackParam crmBusinessTrackParam)  {
        this.crmBusinessTrackService.delete(crmBusinessTrackParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<CrmBusinessTrackResult> detail(@RequestBody CrmBusinessTrackParam crmBusinessTrackParam) {
        CrmBusinessTrack detail = this.crmBusinessTrackService.getById(crmBusinessTrackParam.getTrackId());
        CrmBusinessTrackResult result = new CrmBusinessTrackResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<CrmBusinessTrackResult> list(@RequestBody(required = false) CrmBusinessTrackParam crmBusinessTrackParam) {
        if(ToolUtil.isEmpty(crmBusinessTrackParam)){
            crmBusinessTrackParam = new CrmBusinessTrackParam();
        }
        return this.crmBusinessTrackService.findPageBySpec(crmBusinessTrackParam);
    }




}


