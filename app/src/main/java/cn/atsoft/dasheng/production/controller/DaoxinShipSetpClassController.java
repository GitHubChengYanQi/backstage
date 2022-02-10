package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.DaoxinShipSetpClass;
import cn.atsoft.dasheng.production.model.params.DaoxinShipSetpClassParam;
import cn.atsoft.dasheng.production.model.result.DaoxinShipSetpClassResult;
import cn.atsoft.dasheng.production.service.DaoxinShipSetpClassService;
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
 * 工序分类表控制器
 *
 * @author song
 * @Date 2022-02-10 09:16:24
 */
@RestController
@RequestMapping("/daoxinShipSetpClass")
@Api(tags = "工序分类表")
public class DaoxinShipSetpClassController extends BaseController {

    @Autowired
    private DaoxinShipSetpClassService daoxinShipSetpClassService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody DaoxinShipSetpClassParam daoxinShipSetpClassParam) {
        this.daoxinShipSetpClassService.add(daoxinShipSetpClassParam);
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
    public ResponseData update(@RequestBody DaoxinShipSetpClassParam daoxinShipSetpClassParam) {

        this.daoxinShipSetpClassService.update(daoxinShipSetpClassParam);
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
    public ResponseData delete(@RequestBody DaoxinShipSetpClassParam daoxinShipSetpClassParam)  {
        this.daoxinShipSetpClassService.delete(daoxinShipSetpClassParam);
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
    public ResponseData<DaoxinShipSetpClassResult> detail(@RequestBody DaoxinShipSetpClassParam daoxinShipSetpClassParam) {
        DaoxinShipSetpClass detail = this.daoxinShipSetpClassService.getById(daoxinShipSetpClassParam.getShipSetpClassId());
        DaoxinShipSetpClassResult result = new DaoxinShipSetpClassResult();
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
    public PageInfo<DaoxinShipSetpClassResult> list(@RequestBody(required = false) DaoxinShipSetpClassParam daoxinShipSetpClassParam) {
        if(ToolUtil.isEmpty(daoxinShipSetpClassParam)){
            daoxinShipSetpClassParam = new DaoxinShipSetpClassParam();
        }
        return this.daoxinShipSetpClassService.findPageBySpec(daoxinShipSetpClassParam);
    }




}


