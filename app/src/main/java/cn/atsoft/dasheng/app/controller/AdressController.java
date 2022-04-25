package cn.atsoft.dasheng.app.controller;


import cn.atsoft.dasheng.app.wrapper.AdressSelectWrapper;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Adress;
import cn.atsoft.dasheng.app.model.params.AdressParam;
import cn.atsoft.dasheng.app.model.result.AdressResult;
import cn.atsoft.dasheng.app.service.AdressService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @Permission
    public ResponseData addItem(@RequestBody AdressParam adressParam) {
        Adress adress = this.adressService.add(adressParam);
        return ResponseData.success(adress);
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-07-23
     */
    @Permission
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
    @Permission
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody AdressParam adressParam) {
        this.adressService.delete(adressParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-07-23
     */
    @Permission
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")

    public ResponseData<AdressResult> detail(@RequestBody AdressParam adressParam) {
        if (LoginContextHolder.getContext().isAdmin()) {
            PageInfo<AdressResult> adress = adressService.findPageBySpec(adressParam, null);
            return ResponseData.success(adress.getData().get(0));
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            PageInfo<AdressResult> adress = adressService.findPageBySpec(adressParam, dataScope);
            return ResponseData.success(adress.getData().get(0));
        }
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
    @Permission
    public PageInfo<AdressResult> list(@RequestBody(required = false) AdressParam adressParam) {
        if (ToolUtil.isEmpty(adressParam)) {
            adressParam = new AdressParam();
        }
        clientId = adressParam.getCustomerId();
        if (LoginContextHolder.getContext().isAdmin()) {
            PageInfo<AdressResult> adress = adressService.findPageBySpec(adressParam, null);
//            return ResponseData.success(adress.getData().get(0));
            return this.adressService.findPageBySpec(adressParam, null);

        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            PageInfo<AdressResult> adress = adressService.findPageBySpec(adressParam, dataScope);
            return this.adressService.findPageBySpec(adressParam, null);
        }
//        return this.adressService.findPageBySpec(null,adressParam);
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    @Permission
    public ResponseData<List<Map<String, Object>>> listSelect(@RequestBody(required = false) AdressParam adressParam) {
        QueryWrapper<Adress> adressQueryWrapper = new QueryWrapper<>();
        adressQueryWrapper.eq("display", 1);
        if (ToolUtil.isNotEmpty(adressParam) && ToolUtil.isNotEmpty(adressParam.getCustomerId())){
            adressQueryWrapper.eq("customer_id", adressParam.getCustomerId());
        }
        List<Map<String, Object>> list = this.adressService.listMaps(adressQueryWrapper);
        AdressSelectWrapper adressSelectWrapper = new AdressSelectWrapper(list);
        List<Map<String, Object>> result = adressSelectWrapper.wrap();
        return ResponseData.success(result);
    }


//    @RequestMapping(value = "/test", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData test(@RequestBody AdressParam adressParam) {
//        List<Long> ids = new ArrayList<>();
//        ids.add(1434831489561059329L);
//        ids.add(1434828867206701057L);
//        ids.add(1434828867303170049L);
//        ids.add(1434831489561059329l);
//        List<AdressResult> adressResults = this.adressService.listQuery(ids);
//        return ResponseData.success(adressResults);
//    }
}


