package cn.atsoft.dasheng.view.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;

import cn.atsoft.dasheng.view.model.params.ViewStockDetailsParam;
import cn.atsoft.dasheng.view.model.result.ViewStockDetailsResult;
import cn.atsoft.dasheng.view.service.ViewStockDetailsService;
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
 * VIEW控制器
 *
 * @author
 * @Date 2022-01-27 09:21:24
 */
@RestController
@RequestMapping("/viewStockDetails")
@Api(tags = "VIEW")
public class ViewStockDetailsController extends BaseController {

    @Autowired
    private ViewStockDetailsService viewStockDetailsService;


    /**
     * 查询列表
     *
     * @author
     * @Date 2022-01-27
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ViewStockDetailsResult> list(@RequestBody ViewStockDetailsParam viewStockDetailsParam) {
        if (ToolUtil.isEmpty(viewStockDetailsParam)) {
            viewStockDetailsParam = new ViewStockDetailsParam();
        }
        return this.viewStockDetailsService.findListBySpec(viewStockDetailsParam);
    }


}


