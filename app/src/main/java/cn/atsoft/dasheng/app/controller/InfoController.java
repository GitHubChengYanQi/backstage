package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Info;
import cn.atsoft.dasheng.app.model.params.InfoParam;
import cn.atsoft.dasheng.app.service.InfoService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 数据库信息表控制器
 *
 * @author sing
 * @Date 2020-12-07 17:16:39
 */
@RestController
@RequestMapping("/info")
@Api(tags = "数据库信息表")
public class InfoController extends BaseController {

    @Autowired
    private InfoService infoService;

    /**
     * 新增接口
     *
     * @author sing
     * @Date 2020-12-07
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody InfoParam infoParam) {
        this.infoService.add(infoParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author sing
     * @Date 2020-12-07
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody InfoParam infoParam) {
        this.infoService.update(infoParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author sing
     * @Date 2020-12-07
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody InfoParam infoParam)  {
        this.infoService.delete(infoParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author sing
     * @Date 2020-12-07
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody InfoParam infoParam) {
        Info detail = this.infoService.getById(infoParam.getDbId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author sing
     * @Date 2020-12-07
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo list(@RequestBody(required = false) InfoParam infoParam) {
        return this.infoService.findPageBySpec(infoParam);
    }

}


