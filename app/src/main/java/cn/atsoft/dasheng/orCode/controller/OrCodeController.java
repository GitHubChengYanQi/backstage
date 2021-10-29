package cn.atsoft.dasheng.orCode.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.orCode.entity.OrCode;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.model.params.OrCodeBindParam;
import cn.atsoft.dasheng.orCode.model.params.OrCodeParam;
import cn.atsoft.dasheng.orCode.model.result.OrCodeResult;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 二维码控制器
 *
 * @author song
 * @Date 2021-10-29 10:23:27
 */
@RestController
@RequestMapping("/orCode")
@Api(tags = "二维码")
public class OrCodeController extends BaseController {

    @Autowired
    private OrCodeService orCodeService;
    @Autowired
    private OrCodeBindService orCodeBindService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody OrCodeParam orCodeParam) {
        this.orCodeService.add(orCodeParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody OrCodeParam orCodeParam) {

        this.orCodeService.update(orCodeParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody OrCodeParam orCodeParam) {
        this.orCodeService.delete(orCodeParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<OrCodeResult> detail(@RequestBody OrCodeParam orCodeParam) {
        OrCode detail = this.orCodeService.getById(orCodeParam.getOrCodeId());
        OrCodeResult result = new OrCodeResult();
        ToolUtil.copyProperties(detail, result);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<OrCodeResult> list(@RequestBody(required = false) OrCodeParam orCodeParam) {
        if (ToolUtil.isEmpty(orCodeParam)) {
            orCodeParam = new OrCodeParam();
        }
        return this.orCodeService.findPageBySpec(orCodeParam);
    }


    /**
     * 返回二维码
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/backCode", method = RequestMethod.GET)
    @ApiOperation("二维码")
    @Transactional
    public ResponseData backCode(@RequestParam String type, Long id) {
        

        OrCodeBind one = orCodeBindService.query().in("source", type).in("form_id", id).one();
        if (ToolUtil.isNotEmpty(one)) {
            return ResponseData.success(one.getOrCodeId());
        }else{
            OrCodeParam orCodeParam = new OrCodeParam();
            orCodeParam.setType(type);
            Long aLong = orCodeService.add(orCodeParam);
            OrCodeBindParam orCodeBindParam = new OrCodeBindParam();
            orCodeBindParam.setSource(type);
            orCodeBindParam.setFormId(id);
            orCodeBindParam.setOrCodeId(aLong);
            orCodeBindService.add(orCodeBindParam);
            return ResponseData.success(aLong);
        }
    }

}


