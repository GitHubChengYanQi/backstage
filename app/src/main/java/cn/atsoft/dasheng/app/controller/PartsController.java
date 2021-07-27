package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.entity.Items;
import cn.atsoft.dasheng.app.model.params.ItemsParam;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.app.service.impl.PartsServiceImpl;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.model.params.PartsParam;
import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import cn.atsoft.dasheng.app.wrapper.PartsSelectWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 清单控制器
 *
 * @author 1
 * @Date 2021-07-14 14:56:44
 */
@RestController
@RequestMapping("/parts")
@Api(tags = "清单")
public class PartsController extends BaseController {

    @Autowired
    private PartsService partsService;

    /**
     * 新增接口
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody PartsParam partsParam) {
        this.partsService.add(partsParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody PartsParam partsParam) {

        this.partsService.update(partsParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody PartsParam partsParam) {
        this.partsService.delete(partsParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<PartsResult> detail(@RequestBody PartsParam partsParam) {
        Parts detail = this.partsService.getById(partsParam.getPartsId());
        PartsResult result = new PartsResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<PartsResult> list(@RequestBody(required = false) PartsParam partsParam) {
        if (ToolUtil.isEmpty(partsParam)) {
            partsParam = new PartsParam();
        }
        List<PartsResult> data = partsService.findPageBySpec(partsParam).getData();

        int size = partsService.findPageBySpec(partsParam).getData().size();
        for (int i = 0; i < size; i++) {
            Long partsId = partsService.findPageBySpec(partsParam).getData().get(i).getPartsId();
            String iname = partsService.findPageBySpec(partsParam).getData().get(i).getName();
            System.err.println(partsId+"-----------------------------------------------------------------------------------------------------------------");
            System.err.println(partsService.findPageBySpec(partsParam));
            System.err.println("物品名称：+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"+iname);
        }

        return this.partsService.findPageBySpec(partsParam);
}

    /**
     * 选择列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData<List<Map<String, Object>>> listSelect() {
        List<Map<String, Object>> list = this.partsService.listMaps();

        PartsSelectWrapper factory = new PartsSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }


}


