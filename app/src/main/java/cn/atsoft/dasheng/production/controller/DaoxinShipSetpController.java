package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.DaoxinShipSetp;
import cn.atsoft.dasheng.production.model.params.DaoxinShipSetpParam;
import cn.atsoft.dasheng.production.model.result.DaoxinShipSetpResult;
import cn.atsoft.dasheng.production.service.DaoxinShipSetpService;
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
 * 工序表控制器
 *
 * @author song
 * @Date 2022-02-10 09:16:24
 */
@RestController
@RequestMapping("/daoxinShipSetp")
@Api(tags = "工序表")
public class DaoxinShipSetpController extends BaseController {

    @Autowired
    private DaoxinShipSetpService daoxinShipSetpService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody DaoxinShipSetpParam daoxinShipSetpParam) {
        this.daoxinShipSetpService.add(daoxinShipSetpParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody DaoxinShipSetpParam daoxinShipSetpParam) {

        this.daoxinShipSetpService.update(daoxinShipSetpParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody DaoxinShipSetpParam daoxinShipSetpParam)  {
        this.daoxinShipSetpService.delete(daoxinShipSetpParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<DaoxinShipSetpResult> detail(@RequestBody DaoxinShipSetpParam daoxinShipSetpParam) {
        DaoxinShipSetp detail = this.daoxinShipSetpService.getById(daoxinShipSetpParam.getShipSetpId());
        DaoxinShipSetpResult result = new DaoxinShipSetpResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<DaoxinShipSetpResult> list(@RequestBody(required = false) DaoxinShipSetpParam daoxinShipSetpParam) {
        if(ToolUtil.isEmpty(daoxinShipSetpParam)){
            daoxinShipSetpParam = new DaoxinShipSetpParam();
        }
        return this.daoxinShipSetpService.findPageBySpec(daoxinShipSetpParam);
    }




}


