package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.wrapper.BrandSelectWrapper;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.ContractClass;
import cn.atsoft.dasheng.crm.model.params.ContractClassParam;
import cn.atsoft.dasheng.crm.model.result.ContractClassResult;
import cn.atsoft.dasheng.crm.service.ContractClassService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.wrapper.ContractClassSelectWrapper;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 合同分类控制器
 *
 * @author song
 * @Date 2021-12-09 14:11:38
 */
@RestController
@RequestMapping("/contractClass")
@Api(tags = "合同分类")
public class ContractClassController extends BaseController {

    @Autowired
    private ContractClassService contractClassService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-12-09
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ContractClassParam contractClassParam) {
        this.contractClassService.add(contractClassParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-12-09
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ContractClassParam contractClassParam) {

        this.contractClassService.update(contractClassParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-12-09
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ContractClassParam contractClassParam)  {
        this.contractClassService.delete(contractClassParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-12-09
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ContractClassParam contractClassParam) {
        ContractClass detail = this.contractClassService.getById(contractClassParam.getContractClassId());
        ContractClassResult result = new ContractClassResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-12-09
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ContractClassResult> list(@RequestBody(required = false) ContractClassParam contractClassParam) {
        if(ToolUtil.isEmpty(contractClassParam)){
            contractClassParam = new ContractClassParam();
        }
        return this.contractClassService.findPageBySpec(contractClassParam);
    }

    /**
     * 选择列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect() {
        QueryWrapper<ContractClass> contractClassQueryWrapper = new QueryWrapper<>();
        contractClassQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.contractClassService.listMaps(contractClassQueryWrapper);
        ContractClassSelectWrapper factory = new ContractClassSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }





}


