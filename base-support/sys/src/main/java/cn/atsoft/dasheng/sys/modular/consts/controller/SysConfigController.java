package cn.atsoft.dasheng.sys.modular.consts.controller;

import cn.atsoft.dasheng.base.pojo.page.LayuiPageInfo;
import cn.atsoft.dasheng.sys.modular.consts.entity.SysConfig;
import cn.atsoft.dasheng.sys.modular.consts.model.params.SysConfigParam;
import cn.atsoft.dasheng.sys.modular.consts.service.SysConfigService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.rest.wrapper.DictTypeWrapper;
import cn.atsoft.dasheng.sys.modular.system.service.DictTypeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 参数配置控制器
 *
 * @author stylefeng
 * @Date 2019-06-20 14:32:21
 */
@Controller
@RequestMapping("/sysConfig")
public class SysConfigController extends BaseController {

    private String PREFIX = "/modular/sysConfig";

    @Autowired
    private SysConfigService sysConfigService;


    /**
     * 跳转到主页面
     *
     * @author stylefeng
     * @Date 2019-06-20
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/sysConfig.html";
    }

    /**
     * 新增页面
     *
     * @author stylefeng
     * @Date 2019-06-20
     */
    @RequestMapping("/add")
    public String add() {
        return PREFIX + "/sysConfig_add.html";
    }

    /**
     * 编辑页面
     *
     * @author stylefeng
     * @Date 2019-06-20
     */
    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/sysConfig_edit.html";
    }

    /**
     * 新增接口
     *
     * @author stylefeng
     * @Date 2019-06-20
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(@RequestBody SysConfigParam sysConfigParam) {
        this.sysConfigService.add(sysConfigParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author stylefeng
     * @Date 2019-06-20
     */
    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(@RequestBody SysConfigParam sysConfigParam) {
        this.sysConfigService.update(sysConfigParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author stylefeng
     * @Date 2019-06-20
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(@RequestBody SysConfigParam sysConfigParam) {
        this.sysConfigService.delete(sysConfigParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author stylefeng
     * @Date 2019-06-20
     */
    @RequestMapping("/detail")
    @ResponseBody
    public ResponseData detail(@RequestBody SysConfigParam sysConfigParam) {
        SysConfig detail = this.sysConfigService.getById(sysConfigParam.getId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author stylefeng
     * @Date 2019-06-20
     */
    @ResponseBody
    @RequestMapping("/list")
    public LayuiPageInfo list(@RequestParam(value = "condition", required = false) String condition) {

        SysConfigParam sysConfigParam = new SysConfigParam();

        if (ToolUtil.isNotEmpty(condition)) {
            sysConfigParam.setCode(condition);
            sysConfigParam.setName(condition);
            sysConfigParam.setRemark(condition);
            sysConfigParam.setValue(condition);
        }

        return this.sysConfigService.findPageBySpec(sysConfigParam);
    }


}


