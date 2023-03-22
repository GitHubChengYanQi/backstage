package cn.atsoft.dasheng.goods.brand.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.brand.entity.RestSkuBrandBind;
import cn.atsoft.dasheng.goods.brand.model.params.RestSkuBrandBindParam;
import cn.atsoft.dasheng.goods.brand.model.result.RestSkuBrandBindResult;
import cn.atsoft.dasheng.goods.brand.service.RestSkuBrandBindService;
import cn.atsoft.dasheng.model.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * 控制器
 *
 * @author Captain_Jazz
 * @Date 2022-01-18 10:55:42
 */
@RestController
@RequestMapping("/skuBrandBind/{version}")
@ApiVersion("2.0")
@Api(tags = "品牌绑定管理")
public class RestSkuBrandBindController extends BaseController {

    @Autowired
    private RestSkuBrandBindService skuBrandBindService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RestSkuBrandBindParam skuBrandBindParam) {
        this.skuBrandBindService.add(skuBrandBindParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody RestSkuBrandBindParam skuBrandBindParam) {

        this.skuBrandBindService.update(skuBrandBindParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody RestSkuBrandBindParam skuBrandBindParam)  {
        this.skuBrandBindService.delete(skuBrandBindParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody RestSkuBrandBindParam skuBrandBindParam) {
        RestSkuBrandBind detail = this.skuBrandBindService.getById(skuBrandBindParam.getSkuBrandBind());
        RestSkuBrandBindResult result = new RestSkuBrandBindResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<RestSkuBrandBindResult> list(@RequestBody(required = false) RestSkuBrandBindParam skuBrandBindParam) {
        if(ToolUtil.isEmpty(skuBrandBindParam)){
            skuBrandBindParam = new RestSkuBrandBindParam();
        }
        return this.skuBrandBindService.findPageBySpec(skuBrandBindParam);
    }




}


