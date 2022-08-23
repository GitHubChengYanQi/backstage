package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.wrapper.PhoneSelectWrapper;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Phone;
import cn.atsoft.dasheng.app.model.params.PhoneParam;
import cn.atsoft.dasheng.app.model.result.PhoneResult;
import cn.atsoft.dasheng.app.service.PhoneService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


/**
 * 控制器
 *
 * @author cheng
 * @Date 2021-08-12 08:47:13
 */
@RestController
@RequestMapping("/phone")
@Api(tags = "")
public class PhoneController extends BaseController {

    @Autowired
    private PhoneService phoneService;

    /**
     * 新增接口
     *
     * @author cheng
     * @Date 2021-08-12
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody @Valid  PhoneParam phoneParam) {
        Phone phone = this.phoneService.add(phoneParam);
        return ResponseData.success(phone);
    }

    /**
     * 编辑接口
     *
     * @author cheng
     * @Date 2021-08-12
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody PhoneParam phoneParam) {

        this.phoneService.update(phoneParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author cheng
     * @Date 2021-08-12
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody PhoneParam phoneParam)  {
        this.phoneService.delete(phoneParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author cheng
     * @Date 2021-08-12
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody PhoneParam phoneParam) {
        Phone detail = this.phoneService.getById(phoneParam.getPhoneId());
        PhoneResult result = new PhoneResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author cheng
     * @Date 2021-08-12
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<PhoneResult> list(@RequestBody(required = false) PhoneParam phoneParam) {
        if(ToolUtil.isEmpty(phoneParam)){
            phoneParam = new PhoneParam();
        }
//        return this.phoneService.findPageBySpec(phoneParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.phoneService.findPageBySpec(phoneParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.phoneService.findPageBySpec(phoneParam, dataScope);
        }
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)

    public ResponseData listSelect() {
        QueryWrapper<Phone> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.in("display",1);
        List<Map<String, Object>> list = this.phoneService.listMaps(QueryWrapper);
        PhoneSelectWrapper phoneWrapper = new PhoneSelectWrapper(list);
        List<Map<String, Object>> result = phoneWrapper.wrap();
        return ResponseData.success(result);
    }


}


