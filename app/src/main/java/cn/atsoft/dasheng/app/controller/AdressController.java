package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.wrapper.AdressSelectWrapper;
import cn.atsoft.dasheng.app.wrapper.ContactsSelectWrapper;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Adress;
import cn.atsoft.dasheng.app.model.params.AdressParam;
import cn.atsoft.dasheng.app.model.result.AdressResult;
import cn.atsoft.dasheng.app.service.AdressService;
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
 * 客户地址表控制器
 *
 * @author 
 * @Date 2021-07-23 10:06:11
 */
@RestController
@RequestMapping("/adress")
@Api(tags = "客户地址表")
public class AdressController extends BaseController {

    @Autowired
    private AdressService adressService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-07-23
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody AdressParam adressParam) {
        adressParam.setCustomerId(clientId);
        Long add = this.adressService.add(adressParam);
        return ResponseData.success(add);
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-07-23
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody AdressParam adressParam) {
        adressParam.setCustomerId(clientId);
        this.adressService.update(adressParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-07-23
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody AdressParam adressParam)  {
        this.adressService.delete(adressParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-07-23
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<AdressResult> detail(@RequestBody AdressParam adressParam) {
        Adress detail = this.adressService.getById(adressParam.getAdressId());
        AdressResult result = new AdressResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-07-23
     */
    Long clientId;
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<AdressResult> list(@RequestBody(required = false) AdressParam adressParam) {
        clientId = adressParam.getCustomerId();
        if(ToolUtil.isEmpty(adressParam)){
            adressParam = new AdressParam();
        }
        return this.adressService.findPageBySpec(adressParam);
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData<List<Map<String,Object>>> listSelect() {
        List<Map<String,Object>> list = this.adressService.listMaps();
     AdressSelectWrapper adressSelectWrapper = new AdressSelectWrapper(list);
        List<Map<String,Object>> result = adressSelectWrapper.wrap();
        return ResponseData.success(result);
    }

}


