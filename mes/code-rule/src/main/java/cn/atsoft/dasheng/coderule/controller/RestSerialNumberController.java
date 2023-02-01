package cn.atsoft.dasheng.coderule.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.coderule.entity.RestSerialNumber;
import cn.atsoft.dasheng.coderule.model.params.RestSerialNumberParam;
import cn.atsoft.dasheng.coderule.model.result.RestSerialNumberResult;
import cn.atsoft.dasheng.coderule.service.RestSerialNumberService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * 流水号控制器
 *
 * @author 
 * @Date 2021-11-04 18:59:26
 */
@RestController
@RequestMapping("/serialNumber/{version}")
@ApiVersion("2.0")
@Api(tags = "流水号 管理")
public class RestSerialNumberController extends BaseController {

    @Autowired
    private RestSerialNumberService serialNumberService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-11-04
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RestSerialNumberParam serialNumberParam) {

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
    public ResponseData update(@RequestBody RestSerialNumberParam serialNumberParam) {

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
    public ResponseData delete(@RequestBody RestSerialNumberParam serialNumberParam)  {
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
    public ResponseData detail(@RequestBody RestSerialNumberParam serialNumberParam) {
        RestSerialNumber detail = this.serialNumberService.getById(serialNumberParam.getSerialId());
        RestSerialNumberResult result = new RestSerialNumberResult();
        ToolUtil.copyProperties(detail, result);

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
    public PageInfo<RestSerialNumberResult> list(@RequestBody(required = false) RestSerialNumberParam serialNumberParam) {
        if(ToolUtil.isEmpty(serialNumberParam)){
            serialNumberParam = new RestSerialNumberParam();
        }
        return this.serialNumberService.findPageBySpec(serialNumberParam);
    }




}


