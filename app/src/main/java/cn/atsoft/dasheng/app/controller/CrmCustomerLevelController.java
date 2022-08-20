package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.wrapper.CrmCustomerLevelSelectWrapper;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmCustomerLevel;
import cn.atsoft.dasheng.app.model.params.CrmCustomerLevelParam;
import cn.atsoft.dasheng.app.model.result.CrmCustomerLevelResult;
import cn.atsoft.dasheng.app.service.CrmCustomerLevelService;
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
 * 客户级别表控制器
 *
 * @author 
 * @Date 2021-07-30 13:00:02
 */
@RestController
@RequestMapping("/crmCustomerLevel")
@Api(tags = "客户级别表")
public class CrmCustomerLevelController extends BaseController {

    @Autowired
    private CrmCustomerLevelService crmCustomerLevelService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-07-30
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody CrmCustomerLevelParam crmCustomerLevelParam) {
        this.crmCustomerLevelService.add(crmCustomerLevelParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-07-30
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody CrmCustomerLevelParam crmCustomerLevelParam) {

        this.crmCustomerLevelService.update(crmCustomerLevelParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-07-30
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody CrmCustomerLevelParam crmCustomerLevelParam)  {
        this.crmCustomerLevelService.delete(crmCustomerLevelParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-07-30
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody CrmCustomerLevelParam crmCustomerLevelParam) {
        CrmCustomerLevel detail = this.crmCustomerLevelService.getById(crmCustomerLevelParam.getCustomerLevelId());
        CrmCustomerLevelResult result = new CrmCustomerLevelResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-07-30
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<CrmCustomerLevelResult> list(@RequestBody(required = false) CrmCustomerLevelParam crmCustomerLevelParam) {
        if(ToolUtil.isEmpty(crmCustomerLevelParam)){
            crmCustomerLevelParam = new CrmCustomerLevelParam();
        }
//        return this.crmCustomerLevelService.findPageBySpec(crmCustomerLevelParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.crmCustomerLevelService.findPageBySpec(crmCustomerLevelParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.crmCustomerLevelService.findPageBySpec(crmCustomerLevelParam, dataScope);
        }
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    public ResponseData listSelect() {
        QueryWrapper<CrmCustomerLevel> levelQueryWrapper = new QueryWrapper<>();
        levelQueryWrapper.in("display",1);
        levelQueryWrapper.orderByDesc("rank");
        List<Map<String, Object>> list = this.crmCustomerLevelService.listMaps(levelQueryWrapper);
        CrmCustomerLevelSelectWrapper crmCustomerLevelSelectWrapper = new CrmCustomerLevelSelectWrapper(list);
        List<Map<String, Object>> result = crmCustomerLevelSelectWrapper.wrap();
        return ResponseData.success(result);
    }

}


