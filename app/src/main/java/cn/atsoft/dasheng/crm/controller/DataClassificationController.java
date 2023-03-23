package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.app.model.result.BatchDeleteRequest;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Competitor;
import cn.atsoft.dasheng.crm.entity.DataClassification;
import cn.atsoft.dasheng.crm.model.params.DataClassificationParam;
import cn.atsoft.dasheng.crm.model.result.DataClassificationResult;
import cn.atsoft.dasheng.crm.service.DataClassificationService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.wrapper.CompetitorSelectWrapper;
import cn.atsoft.dasheng.crm.wrapper.DataClassificationSelectWrapper;
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
 * 资料分类表控制器
 *
 * @author 
 * @Date 2021-09-13 12:51:21
 */
@RestController
@RequestMapping("/dataClassification")
@Api(tags = "资料分类表")
public class DataClassificationController extends BaseController {

    @Autowired
    private DataClassificationService dataClassificationService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-09-13
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody DataClassificationParam dataClassificationParam) {
        this.dataClassificationService.add(dataClassificationParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-09-13
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody DataClassificationParam dataClassificationParam) {

        this.dataClassificationService.update(dataClassificationParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-09-13
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody DataClassificationParam dataClassificationParam)  {
        this.dataClassificationService.delete(dataClassificationParam);
        return ResponseData.success();
    }

    /**
     * 批量删除接口
     * @param batchDeleteRequest
     * @return
     */
    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    @ApiOperation("批量删除")
    public ResponseData batchDelete(@RequestBody (required = false) BatchDeleteRequest batchDeleteRequest) {
        this.dataClassificationService.batchDelete(batchDeleteRequest.getIds());
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-09-13
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody DataClassificationParam dataClassificationParam) {
        DataClassification detail = this.dataClassificationService.getById(dataClassificationParam.getDataClassificationId());
        DataClassificationResult result = new DataClassificationResult();
if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-09-13
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<DataClassificationResult> list(@RequestBody(required = false) DataClassificationParam dataClassificationParam) {
        if(ToolUtil.isEmpty(dataClassificationParam)){
            dataClassificationParam = new DataClassificationParam();
        }
        return this.dataClassificationService.findPageBySpec(dataClassificationParam);
    }

    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect() {
        QueryWrapper<DataClassification> dataClassificationQueryWrapper = new QueryWrapper<>();
        dataClassificationQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.dataClassificationService.listMaps(dataClassificationQueryWrapper);
        DataClassificationSelectWrapper classificationSelectWrapper = new DataClassificationSelectWrapper(list);
        List<Map<String, Object>> result = classificationSelectWrapper.wrap();
        return ResponseData.success(result);
    }




}


