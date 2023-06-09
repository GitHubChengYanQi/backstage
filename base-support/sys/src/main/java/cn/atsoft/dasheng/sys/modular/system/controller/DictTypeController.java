package cn.atsoft.dasheng.sys.modular.system.controller;

import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.pojo.page.LayuiPageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.modular.rest.wrapper.DictTypeWrapper;
import cn.atsoft.dasheng.sys.modular.system.entity.Dict;
import cn.atsoft.dasheng.sys.modular.system.entity.DictType;
import cn.atsoft.dasheng.sys.modular.system.model.params.DictTypeParam;
import cn.atsoft.dasheng.sys.modular.system.service.DictTypeService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.model.response.SuccessResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 字典类型表控制器
 *
 * @author stylefeng
 * @Date 2019-03-13 13:53:54
 */
@Controller
@RequestMapping("/dictType")
public class DictTypeController extends BaseController {

    private String PREFIX = "/modular/system/dictType";

    @Autowired
    private DictTypeService dictTypeService;

    /**
     * 跳转到主页面
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/dictType.html";
    }

    /**
     * 新增页面
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @RequestMapping("/add")
    public String add() {
        return PREFIX + "/dictType_add.html";
    }

    /**
     * 编辑页面
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/dictType_edit.html";
    }

    /**
     * 新增接口
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(DictTypeParam dictTypeParam) {
        this.dictTypeService.add(dictTypeParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(DictTypeParam dictTypeParam) {
        this.dictTypeService.update(dictTypeParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(DictTypeParam dictTypeParam) {
        this.dictTypeService.delete(dictTypeParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @RequestMapping("/detail")
    @ResponseBody
    public ResponseData detail(DictTypeParam dictTypeParam) {
        DictType detail = this.dictTypeService.getById(dictTypeParam.getDictTypeId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @ResponseBody
    @RequestMapping("/list")
    public LayuiPageInfo list(DictTypeParam dictTypeParam) {
        return this.dictTypeService.findPageBySpec(dictTypeParam);
    }


    /**
     * 查询所有字典
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @ResponseBody
    @RequestMapping("/listTypes")
    public ResponseData listTypes() {

        QueryWrapper<DictType> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.select("dict_type_id", "code", "name");

        List<DictType> list = this.dictTypeService.list(objectQueryWrapper);
        return new SuccessResponseData(list);
    }

    /**
     *
     *
     * @author 查询字典
     * @Date 2019-03-13
     */
    @ResponseBody
    @RequestMapping("/queryBrandAndView")
    public ResponseData queryBrandAndView(DictTypeParam dictTypeParam) {
        DictType brand = this.dictTypeService.query().eq("code", "BRAND").one();
        DictType view = this.dictTypeService.query().eq("code", "VIEW").one();
        Map<String,Boolean> map = new HashMap<>();
        if (ToolUtil.isNotEmpty(brand) && brand.getStatus().equals("ENABLE")){
            map.put("brand",true);
        }else {
            map.put("brand",false);
        }
        if (ToolUtil.isNotEmpty(view) && view.getStatus().equals("ENABLE")) {
            map.put("view",true);
        }else {
            map.put("view",false);
        }
        return ResponseData.success(map);
    }
    /**
     *c
     *
     * @author 查询字典
     * @Date 2019-03-13
     */
    @ResponseBody
    @RequestMapping("/updateStatus")
    public ResponseData updateStatus(DictTypeParam dictTypeParam) {
        if (dictTypeParam.getStatus().equals("ENABLE") || dictTypeParam.getStatus().equals("DISABLE")){
            DictType entity = this.dictTypeService.getById(dictTypeParam.getDictTypeId());
            entity.setStatus(dictTypeParam.getStatus());
            this.dictTypeService.updateById(entity);
        }
        return ResponseData.success();
    }
}


