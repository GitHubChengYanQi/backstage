package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InstockHandle;
import cn.atsoft.dasheng.erp.model.params.InstockHandleParam;
import cn.atsoft.dasheng.erp.model.result.InstockHandleResult;
import cn.atsoft.dasheng.erp.service.InstockHandleService;
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
 * 入库操作结果控制器
 *
 * @author song
 * @Date 2022-07-08 14:27:08
 */
@RestController
@RequestMapping("/instockHandle")
@Api(tags = "入库操作结果")
public class InstockHandleController extends BaseController {

    @Autowired
    private InstockHandleService instockHandleService;

//    /**
//     * 新增接口
//     *
//     * @author song
//     * @Date 2022-07-08
//     */
//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    @ApiOperation("新增")
//    public ResponseData addItem(@RequestBody InstockHandleParam instockHandleParam) {
//        this.instockHandleService.add(instockHandleParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 编辑接口
//     *
//     * @author song
//     * @Date 2022-07-08
//     */
//    @RequestMapping(value = "/edit", method = RequestMethod.POST)
//    @ApiOperation("编辑")
//    public ResponseData update(@RequestBody InstockHandleParam instockHandleParam) {
//
//        this.instockHandleService.update(instockHandleParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 删除接口
//     *
//     * @author song
//     * @Date 2022-07-08
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody InstockHandleParam instockHandleParam)  {
//        this.instockHandleService.delete(instockHandleParam);
//        return ResponseData.success();
//    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-07-08
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<InstockHandleResult> detail(@RequestBody InstockHandleParam instockHandleParam) {
        InstockHandle detail = this.instockHandleService.getById(instockHandleParam.getInstockHandleId());
        InstockHandleResult result = new InstockHandleResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-07-08
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<InstockHandleResult> list(@RequestBody(required = false) InstockHandleParam instockHandleParam) {
        if(ToolUtil.isEmpty(instockHandleParam)){
            instockHandleParam = new InstockHandleParam();
        }
        return this.instockHandleService.findPageBySpec(instockHandleParam);
    }

}


