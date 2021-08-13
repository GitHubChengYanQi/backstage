package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
import cn.atsoft.dasheng.app.model.params.StockParam;
import cn.atsoft.dasheng.app.model.result.StockResult;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.app.service.StockService;
import cn.atsoft.dasheng.app.wrapper.InstockSelectWrapper;
import cn.atsoft.dasheng.app.wrapper.ItemsSelectWrapper;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Instock;
import cn.atsoft.dasheng.app.model.params.InstockParam;
import cn.atsoft.dasheng.app.model.result.InstockResult;
import cn.atsoft.dasheng.app.service.InstockService;
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
import java.util.logging.Handler;


/**
 * 入库表控制器
 *
 * @author song
 * @Date 2021-07-17 10:46:08
 */
@RestController
@RequestMapping("/instock")
@Api(tags = "入库表")
public class InstockController extends BaseController {

    @Autowired
    private InstockService instockService;
    @Autowired
    private StockService stockService;
    @Autowired
    private StockDetailsService stockDetailsService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-07-17
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody InstockParam instockParam) {

        Long add = this.instockService.add(instockParam);
        return ResponseData.success(add);
    }




    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-07-17
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody InstockParam instockParam) {

        this.instockService.update(instockParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-07-17
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody InstockParam instockParam)  {
        this.instockService.delete(instockParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-07-17
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<InstockResult> detail(@RequestBody InstockParam instockParam) {
        Instock detail = this.instockService.getById(instockParam.getInstockId());
        InstockResult result = new InstockResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-07-17
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<InstockResult> list(@RequestBody(required = false) InstockParam instockParam) {
        if(ToolUtil.isEmpty(instockParam)){
            instockParam = new InstockParam();
        }
        return this.instockService.findPageBySpec(instockParam);
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)

    public ResponseData<List<Map<String, Object>>> listSelect() {
        List<Map<String, Object>> list = this.instockService.listMaps();

        InstockSelectWrapper instockSelectWrapper = new InstockSelectWrapper(list);
        List<Map<String, Object>> result = instockSelectWrapper.wrap();
        return ResponseData.success(result);
    }

}


