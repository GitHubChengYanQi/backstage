package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InstockList;
import cn.atsoft.dasheng.erp.model.params.InkindParam;
import cn.atsoft.dasheng.erp.model.params.InstockListParam;
import cn.atsoft.dasheng.erp.model.result.InstockListResult;
import cn.atsoft.dasheng.erp.service.InstockListService;
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
 * 入库清单控制器
 *
 * @author song
 * @Date 2021-10-06 16:24:36
 */
@RestController
@RequestMapping("/instockList")
@Api(tags = "入库清单")
public class InstockListController extends BaseController {

    @Autowired
    private InstockListService instockListService;

//    /**
//     * 新增接口
//     *
//     * @author song
//     * @Date 2021-10-06
//     */
//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    @ApiOperation("新增")
//    public ResponseData addItem(@RequestBody InstockListParam instockListParam) {
//       this.instockListService.add(instockListParam);
//        return ResponseData.success();
//    }

//    /**
//     * 编辑接口
//     *
//     * @author song
//     * @Date 2021-10-06
//     */
//    @RequestMapping(value = "/edit", method = RequestMethod.POST)
//    @BussinessLog(value = "修改入库清单", key = "name", dict = InstockListParam.class)
//    @ApiOperation("编辑")
//    public ResponseData update(@RequestBody InstockListParam instockListParam) {
//
////        InstockList entity = new InstockList();
////        entity.setStatus(instockListParam.getStatus());
////        entity.setInstockListId(instockListParam.getInstockListId());
////        this.instockListService.updateById(entity);
//
//        return ResponseData.success();
//    }

//    /**
//     * 删除接口
//     *
//     * @author song
//     * @Date 2021-10-06
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @BussinessLog(value = "删除入库清单", key = "name", dict = InstockListParam.class)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody InstockListParam instockListParam) {
//        this.instockListService.delete(instockListParam);
//        return ResponseData.success();
//    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-10-06
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<InstockListResult> detail(@RequestBody InstockListParam instockListParam) {
        InstockList detail = this.instockListService.getById(instockListParam.getInstockListId());
        InstockListResult result = new InstockListResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-10-06
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<InstockListResult> list(@RequestBody(required = false) InstockListParam instockListParam) {
        if (ToolUtil.isEmpty(instockListParam)) {
            instockListParam = new InstockListParam();
        }
        return this.instockListService.findPageBySpec(instockListParam);
    }


}


