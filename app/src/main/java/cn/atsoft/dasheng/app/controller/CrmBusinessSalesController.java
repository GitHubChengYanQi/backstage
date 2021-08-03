package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.wrapper.CrmBusinessSalesProcessSelectWrapper;
import cn.atsoft.dasheng.app.wrapper.CustomerSelectWrapper;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmBusinessSales;
import cn.atsoft.dasheng.app.model.params.CrmBusinessSalesParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessSalesResult;
import cn.atsoft.dasheng.app.service.CrmBusinessSalesService;
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
 * 销售控制器
 *
 * @author 
 * @Date 2021-08-02 15:47:16
 */
@RestController
@RequestMapping("/crmBusinessSales")
@Api(tags = "销售")
public class CrmBusinessSalesController extends BaseController {

    @Autowired
    private CrmBusinessSalesService crmBusinessSalesService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-08-02
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody CrmBusinessSalesParam crmBusinessSalesParam) {
        this.crmBusinessSalesService.add(crmBusinessSalesParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-08-02
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody CrmBusinessSalesParam crmBusinessSalesParam) {

        this.crmBusinessSalesService.update(crmBusinessSalesParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-08-02
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody CrmBusinessSalesParam crmBusinessSalesParam)  {
        this.crmBusinessSalesService.delete(crmBusinessSalesParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-08-02
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<CrmBusinessSalesResult> detail(@RequestBody CrmBusinessSalesParam crmBusinessSalesParam) {
        CrmBusinessSales detail = this.crmBusinessSalesService.getById(crmBusinessSalesParam.getSalesId());
        CrmBusinessSalesResult result = new CrmBusinessSalesResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-08-02
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<CrmBusinessSalesResult> list(@RequestBody(required = false) CrmBusinessSalesParam crmBusinessSalesParam) {
        if(ToolUtil.isEmpty(crmBusinessSalesParam)){
            crmBusinessSalesParam = new CrmBusinessSalesParam();
        }
        int size = crmBusinessSalesService.findPageBySpec(crmBusinessSalesParam).getData().size();
        for (int i = 0; i < size; i++) {
            Long salesId = crmBusinessSalesService.findPageBySpec(crmBusinessSalesParam).getData().get(i).getSalesId();
            System.err .println(salesId+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        }
        return this.crmBusinessSalesService.findPageBySpec(crmBusinessSalesParam);
    }

    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData<List<Map<String, Object>>> listSelect() {
        List<Map<String, Object>> list = this.crmBusinessSalesService.listMaps();
        CrmBusinessSalesProcessSelectWrapper crmBusinessSalesProcessSelectWrapper =new CrmBusinessSalesProcessSelectWrapper(list);
        List<Map<String, Object>> result = crmBusinessSalesProcessSelectWrapper.wrap();
        return ResponseData.success(result);
    }



}


