package cn.atsoft.dasheng.sys.modular.system.controller;

import cn.atsoft.dasheng.base.pojo.node.ZTreeNode;
import cn.atsoft.dasheng.base.pojo.page.LayuiPageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.modular.system.entity.Dict;
import cn.atsoft.dasheng.sys.modular.system.entity.DictType;
import cn.atsoft.dasheng.sys.modular.system.model.params.DictParam;
import cn.atsoft.dasheng.sys.modular.system.model.params.DictTypeParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.DictResult;
import cn.atsoft.dasheng.sys.modular.system.service.DictService;
import cn.atsoft.dasheng.sys.modular.system.service.DictTypeService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.model.exception.RequestEmptyException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.model.response.SuccessResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 基础字典控制器
 *
 * @author stylefeng
 * @Date 2019-03-13 13:53:53
 */
@Controller
@RequestMapping("/dict")
public class DictController extends BaseController {

    private String PREFIX = "/modular/system/dict";

    @Autowired
    private DictService dictService;

    @Autowired
    private DictTypeService dictTypeService;

    /**
     * 跳转到主页面
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @RequestMapping("")
    public String index(@RequestParam("dictTypeId") Long dictTypeId, Model model) {
        model.addAttribute("dictTypeId", dictTypeId);

        //获取type的名称
        DictType dictType = dictTypeService.getById(dictTypeId);
        if (dictType == null) {
            throw new RequestEmptyException();
        }
        model.addAttribute("dictTypeName", dictType.getName());

        return PREFIX + "/dict.html";
    }

    /**
     * 新增页面
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @RequestMapping("/add")
    public String add(@RequestParam("dictTypeId") Long dictTypeId, Model model) {
        model.addAttribute("dictTypeId", dictTypeId);

        //获取type的名称
        DictType dictType = dictTypeService.getById(dictTypeId);
        if (dictType == null) {
            throw new RequestEmptyException();
        }

        model.addAttribute("dictTypeName", dictType.getName());
        return PREFIX + "/dict_add.html";
    }

    /**
     * 编辑页面
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @RequestMapping("/edit")
    public String edit(@RequestParam("dictId") Long dictId, Model model) {

        //获取type的id
        Dict dict = dictService.getById(dictId);
        if (dict == null) {
            throw new RequestEmptyException();
        }

        //获取type的名称
        DictType dictType = dictTypeService.getById(dict.getDictTypeId());
        if (dictType == null) {
            throw new RequestEmptyException();
        }

        model.addAttribute("dictTypeId", dict.getDictTypeId());
        model.addAttribute("dictTypeName", dictType.getName());

        return PREFIX + "/dict_edit.html";
    }

    /**
     * 新增接口
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(DictParam dictParam) {
        this.dictService.add(dictParam);
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
    public ResponseData editItem(DictParam dictParam) {
        this.dictService.update(dictParam);
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
    public ResponseData delete(DictParam dictParam) {
        this.dictService.delete(dictParam);
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
    public ResponseData detail(DictParam dictParam) {
        DictResult dictResult = this.dictService.dictDetail(dictParam.getDictId());
        return ResponseData.success(dictResult);
    }

    /**
     * 查询列表
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @ResponseBody
    @RequestMapping("/list")
    public LayuiPageInfo list(DictParam dictParam) {
        return this.dictService.findPageBySpec(dictParam);
    }

    /**
     * 获取某个字典类型下的所有字典
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @ResponseBody
    @RequestMapping("/listDicts")
    public ResponseData listDicts(@RequestParam("dictTypeId") Long dictTypeId) {
        List<Dict> dicts = this.dictService.listDicts(dictTypeId);
        return new SuccessResponseData(dicts);
    }

    /**
     * 获取某个字典类型下的所有字典
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @ResponseBody
    @RequestMapping("/listDictsByCode")
    public ResponseData listDictsByCode(@RequestParam("dictTypeCode") String dictTypeCode) {
        List<Dict> dicts = this.dictService.listDictsByCode(dictTypeCode);
        return new SuccessResponseData(dicts);
    }

    /**
     * 获取某个类型下字典树的列表，ztree格式
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:56 PM
     */
    @RequestMapping(value = "/ztree")
    @ResponseBody
    public List<ZTreeNode> ztree(@RequestParam("dictTypeId") Long dictTypeId, @RequestParam(value = "dictId", required = false) Long dictId) {
        return this.dictService.dictTreeList(dictTypeId, dictId);
    }

    /**
     *
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @ResponseBody
    @RequestMapping("/queryBrandAndView")
    public ResponseData queryBrandAndView(DictTypeParam dictTypeParam) {
        Dict brand = this.dictService.query().eq("code", "brand").one();
        Dict view = this.dictService.query().eq("code", "view").one();
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

}


