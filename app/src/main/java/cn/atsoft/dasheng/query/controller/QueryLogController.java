package cn.atsoft.dasheng.query.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.query.model.params.QueryLogParam;
import cn.atsoft.dasheng.query.model.result.QueryLogResult;
import cn.atsoft.dasheng.query.service.QueryLogService;
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
 * 搜索查询记录控制器
 *
 * @author song
 * @Date 2022-05-19 13:48:22
 */
@RestController
@RequestMapping("/queryLog")
@Api(tags = "搜索查询记录")
public class QueryLogController extends BaseController {

    @Autowired
    private QueryLogService queryLogService;

//    /**
//     * 新增接口
//     *
//     * @author song
//     * @Date 2022-05-19
//     */
//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    @ApiOperation("新增")
//    public ResponseData addItem(@RequestBody QueryLogParam queryLogParam) {
//        this.queryLogService.add(queryLogParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 编辑接口
//     *
//     * @author song
//     * @Date 2022-05-19
//     */
//    @RequestMapping(value = "/edit", method = RequestMethod.POST)
//    @ApiOperation("编辑")
//    public ResponseData update(@RequestBody QueryLogParam queryLogParam) {
//
//        this.queryLogService.update(queryLogParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 删除接口
//     *
//     * @author song
//     * @Date 2022-05-19
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody QueryLogParam queryLogParam)  {
//        this.queryLogService.delete(queryLogParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 查看详情接口
//     *
//     * @author song
//     * @Date 2022-05-19
//     */
//    @RequestMapping(value = "/detail", method = RequestMethod.POST)
//    @ApiOperation("详情")
//    public ResponseData<QueryLogResult> detail(@RequestBody QueryLogParam queryLogParam) {
//        QueryLog detail = this.queryLogService.getById(queryLogParam.getQueryLogId());
//        QueryLogResult result = new QueryLogResult();
//        ToolUtil.copyProperties(detail, result);
//
//        return ResponseData.success(result);
//    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-05-19
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<QueryLogResult> list(@RequestBody(required = false) QueryLogParam queryLogParam) {
        if(ToolUtil.isEmpty(queryLogParam)){
            queryLogParam = new QueryLogParam();
        }
        return this.queryLogService.findPageBySpec(queryLogParam);
    }




}


