package cn.atsoft.dasheng.view.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.view.entity.MobelTableView;
import cn.atsoft.dasheng.view.model.params.MobelTableViewParam;
import cn.atsoft.dasheng.view.model.result.MobelTableViewResult;
import cn.atsoft.dasheng.view.service.MobelTableViewService;
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
 * 移动端菜单控制器
 *
 * @author Captain_Jazz
 * @Date 2022-05-09 09:25:40
 */
@RestController
@RequestMapping("/mobelTableView")
@Api(tags = "移动端菜单")
public class MobelTableViewController extends BaseController {

    @Autowired
    private MobelTableViewService mobelTableViewService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-05-09
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody MobelTableViewParam mobelTableViewParam) {
        this.mobelTableViewService.add(mobelTableViewParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-05-09
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody MobelTableViewParam mobelTableViewParam) {

        this.mobelTableViewService.update(mobelTableViewParam);
        return ResponseData.success();
    }

//    /**
//     * 删除接口
//     *
//     * @author Captain_Jazz
//     * @Date 2022-05-09
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody MobelTableViewParam mobelTableViewParam)  {
//        this.mobelTableViewService.delete(mobelTableViewParam);
//        return ResponseData.success();
//    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-05-09
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<MobelTableViewResult> detail(@RequestBody MobelTableViewParam mobelTableViewParam) {
        MobelTableView detail = this.mobelTableViewService.getById(mobelTableViewParam.getMobelTableViewId());
        MobelTableViewResult result = new MobelTableViewResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-05-09
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<MobelTableViewResult> list(@RequestBody(required = false) MobelTableViewParam mobelTableViewParam) {
        if(ToolUtil.isEmpty(mobelTableViewParam)){
            mobelTableViewParam = new MobelTableViewParam();
        }
        return this.mobelTableViewService.findPageBySpec(mobelTableViewParam);
    }




}


