package cn.atsoft.dasheng.sys.modular.rest.controller;

import cn.atsoft.dasheng.sys.modular.rest.entity.RestDict;
import cn.atsoft.dasheng.sys.modular.system.model.params.DictParam;
import cn.atsoft.dasheng.sys.modular.system.model.params.DictTypeParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.DictResult;
import cn.atsoft.dasheng.base.pojo.node.ZTreeNode;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.sys.modular.rest.service.RestDictService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.model.response.SuccessResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 基础字典控制器
 *
 * @author stylefeng
 * @Date 2019-03-13 13:53:53
 */
@RestController
@RequestMapping("/rest/dict")
public class RestDictController extends BaseController {

    @Autowired
    private RestDictService restDictService;

    /**
     * 新增接口
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @RequestMapping("/addItem")
    public ResponseData addItem(@RequestBody DictParam dictParam) {
        this.restDictService.add(dictParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @RequestMapping("/editItem")
    public ResponseData editItem(@RequestBody DictParam dictParam) {
        this.restDictService.update(dictParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @RequestMapping("/delete")
    public ResponseData delete(@RequestBody DictParam dictParam) {
        this.restDictService.delete(dictParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @RequestMapping("/detail")
    public ResponseData detail(@RequestParam Long dictId) {
        DictResult dictResult = this.restDictService.dictDetail(dictId);
        return ResponseData.success(dictResult);
    }

    /**
     * 查询列表
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @RequestMapping("/list")
    public PageInfo list(@RequestBody DictParam dictParam) {
        return this.restDictService.findPageBySpec(dictParam);
    }

    /**
     * 获取某个字典类型下的所有字典
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @RequestMapping("/listDicts")
    public ResponseData listDicts(@RequestParam("dictTypeId") Long dictTypeId) {
        List<RestDict> dicts = this.restDictService.listDicts(dictTypeId);
        return new SuccessResponseData(dicts);
    }

    /**
     * 获取某个字典类型下的所有字典
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @RequestMapping("/listDictsByCode")
    public ResponseData listDictsByCode(@RequestBody DictTypeParam dictTypeParam) {
        String dictTypeCode = dictTypeParam.getDictTypeCode();
        List<RestDict> dicts = this.restDictService.listDictsByCode(dictTypeCode);
        return new SuccessResponseData(dicts);
    }

    @RequestMapping("/radioDictsByCode")
    public ResponseData radioDictsByCode(@RequestBody DictTypeParam dictTypeParam) {
        String dictTypeCode = dictTypeParam.getDictTypeCode();
        List<RestDict> dicts = this.restDictService.radioDictsByCode(dictTypeCode);
        return new SuccessResponseData(dicts);
    }

    /**
     * 获取某个类型下字典树的列表，ztree格式
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:56 PM
     */
    @RequestMapping(value = "/ztree")
    public List<ZTreeNode> ztree(@RequestParam("dictTypeId") Long dictTypeId,
                                 @RequestParam(value = "dictId", required = false) Long dictId) {
        return this.restDictService.dictTreeList(dictTypeId, dictId);
    }

}


