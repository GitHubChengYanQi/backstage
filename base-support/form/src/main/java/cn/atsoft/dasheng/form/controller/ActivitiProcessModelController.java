package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessResult;
import cn.atsoft.dasheng.form.pojo.ProcessParam;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 流程主表控制器
 *
 * @author Sing
 * @Date 2021-11-10 16:53:55
 */
@RestController
@RequestMapping("/activitiProcess/model")
@Api(tags = "流程主表")
public class ActivitiProcessModelController extends BaseController {

    @Autowired
    private ActivitiProcessService activitiProcessService;

    /**
     * 新增接口
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/getByModel", method = RequestMethod.GET)
    @ApiOperation("新增")
    public ResponseData getByModel() {
         return ResponseData.success();
    }
//    public ResponseData addItem(@RequestParam  activitiProcessParam) {
//
//        this.activitiProcessService.add(activitiProcessParam);
//        return ResponseData.success();
//    }


}


