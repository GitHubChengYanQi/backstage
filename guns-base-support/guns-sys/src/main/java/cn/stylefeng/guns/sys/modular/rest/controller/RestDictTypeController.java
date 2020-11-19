package cn.stylefeng.guns.sys.modular.rest.controller;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.stylefeng.guns.base.pojo.page.PageInfo;
import cn.stylefeng.guns.sys.modular.rest.entity.RestDictType;
import cn.stylefeng.guns.sys.modular.rest.service.RestDictTypeService;
import cn.stylefeng.guns.sys.modular.rest.wrapper.DictTypeWrapper;
import cn.stylefeng.guns.sys.modular.system.model.params.DictTypeParam;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.model.response.SuccessResponseData;
import cn.stylefeng.guns.sys.modular.system.warpper.UserWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 字典类型表控制器
 */
@RestController
@RequestMapping("/rest/dictType")
public class RestDictTypeController extends BaseController {

    @Autowired
    private RestDictTypeService restDictTypeService;

    /**
     * 新增接口
     */
    @RequestMapping("/addItem")
    public ResponseData addItem(@RequestBody DictTypeParam dictTypeParam) {
        this.restDictTypeService.add(dictTypeParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     */
    @RequestMapping("/editItem")
    public ResponseData editItem(@RequestBody DictTypeParam dictTypeParam) {
        this.restDictTypeService.update(dictTypeParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     */
    @RequestMapping("/delete")
    public ResponseData delete(@RequestBody DictTypeParam dictTypeParam) {
        this.restDictTypeService.delete(dictTypeParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     */
    @RequestMapping("/detail")
    public ResponseData detail(@RequestParam Long dictTypeId) {
        RestDictType detail = this.restDictTypeService.getById(dictTypeId);
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     */
    @RequestMapping("/list")
    public PageInfo list(@RequestBody DictTypeParam dictTypeParam) {
        return this.restDictTypeService.findPageBySpec(dictTypeParam);
    }

    /**
     * 查询所有字典
     */
    @RequestMapping("/listTypes")
    public ResponseData listTypes(@RequestBody(required = false) DictTypeParam dictTypeParam) {

        QueryWrapper<RestDictType> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.select("dict_type_id", "code", "name");

        List<RestDictType> list = this.restDictTypeService.list(objectQueryWrapper);
        return new SuccessResponseData(list);
    }

    @RequestMapping("/select")
    public ResponseData listSelect(@RequestBody(required = false) DictTypeParam dictTypeParam) {

        // QueryWrapper<Map<String, Object>> objectQueryWrapper = new QueryWrapper<>();
        // objectQueryWrapper.select("dict_type_id", "code", "name");

        String name = "";
        if(ToolUtil.isNotEmpty(dictTypeParam)){
            name = dictTypeParam.getName();
        }
        List<Map<String, Object>> list = this.restDictTypeService.selectList(name);

        List wrapped = new DictTypeWrapper(list).wrap();
        return new SuccessResponseData(wrapped);
    }

}


