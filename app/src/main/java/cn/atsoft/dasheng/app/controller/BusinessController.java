package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.wrapper.AdressSelectWrapper;
import cn.atsoft.dasheng.app.wrapper.BusinessSelectWrapper;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Business;
import cn.atsoft.dasheng.app.model.params.BusinessParam;
import cn.atsoft.dasheng.app.model.result.BusinessResult;
import cn.atsoft.dasheng.app.service.BusinessService;
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
 * 商机表控制器
 *
 * @author cheng
 * @Date 2021-07-19 15:13:58
 */
@RestController
@RequestMapping("/business")
@Api(tags = "商机表")
public class BusinessController extends BaseController {

    @Autowired
    private BusinessService businessService;

    /**
     * 新增接口
     *
     * @author cheng
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody BusinessParam businessParam) {
        Long add = this.businessService.add(businessParam);
        return ResponseData.success(add);
    }

    /**
     * 编辑接口
     *
     * @author cheng
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody BusinessParam businessParam) {

        this.businessService.update(businessParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author cheng
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody BusinessParam businessParam)  {
        this.businessService.delete(businessParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author cheng
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<BusinessResult> detail(@RequestBody BusinessParam businessParam) {
        Business detail = this.businessService.getById(businessParam.getBusinessId());
        BusinessResult result = new BusinessResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author cheng
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<BusinessResult> list(@RequestBody(required = false) BusinessParam businessParam) {
        if(ToolUtil.isEmpty(businessParam)){
            businessParam = new BusinessParam();
        }
        return this.businessService.findPageBySpec(businessParam);
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData<List<Map<String,Object>>> listSelect() {
        List<Map<String, Object>> list = this.businessService.listMaps();
        BusinessSelectWrapper businessSelectWrapper = new BusinessSelectWrapper(list);
        List<Map<String, Object>> result = businessSelectWrapper.wrap();
        return ResponseData.success(result);
    }

    }


