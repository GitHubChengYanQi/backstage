package cn.atsoft.dasheng.serial.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.serial.entity.SerialNumber;
import cn.atsoft.dasheng.serial.model.params.SerialNumberParam;
import cn.atsoft.dasheng.serial.model.result.SerialNumberResult;
import cn.atsoft.dasheng.serial.service.SerialNumberService;
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
 * 流水号控制器
 *
 * @author 
 * @Date 2021-11-04 18:59:26
 */
@RestController
@RequestMapping("/serialNumber")
@Api(tags = "流水号")
public class SerialNumberController extends BaseController {

    @Autowired
    private SerialNumberService serialNumberService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-11-04
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody SerialNumberParam serialNumberParam) {

        return ResponseData.success(this.serialNumberService.add(serialNumberParam));
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-11-04
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody SerialNumberParam serialNumberParam) {

        this.serialNumberService.update(serialNumberParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-11-04
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody SerialNumberParam serialNumberParam)  {
        this.serialNumberService.delete(serialNumberParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-11-04
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody SerialNumberParam serialNumberParam) {
        SerialNumber detail = this.serialNumberService.getById(serialNumberParam.getSerialId());
        SerialNumberResult result = new SerialNumberResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

        return ResponseData.success(result);

    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-11-04
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<SerialNumberResult> list(@RequestBody(required = false) SerialNumberParam serialNumberParam) {
        if(ToolUtil.isEmpty(serialNumberParam)){
            serialNumberParam = new SerialNumberParam();
        }
        return this.serialNumberService.findPageBySpec(serialNumberParam);
    }




}


