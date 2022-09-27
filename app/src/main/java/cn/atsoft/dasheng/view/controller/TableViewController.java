package cn.atsoft.dasheng.view.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Data;
import cn.atsoft.dasheng.crm.wrapper.DataSelectWrapper;
import cn.atsoft.dasheng.view.entity.TableView;
import cn.atsoft.dasheng.view.model.params.TableViewParam;
import cn.atsoft.dasheng.view.model.result.TableViewResult;
import cn.atsoft.dasheng.view.service.TableViewService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.view.wrapper.TableViewSelectWrapper;
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
 * 控制器
 *
 * @author
 * @Date 2021-11-04 16:02:10
 */
@RestController
@RequestMapping("/tableView")
@Api(tags = "")
public class TableViewController extends BaseController {

    @Autowired
    private TableViewService tableViewService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-11-04
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody TableViewParam tableViewParam) {
        TableView tableView = this.tableViewService.add(tableViewParam);
        return ResponseData.success(tableView);
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-11-04
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody TableViewParam tableViewParam) {

        this.tableViewService.update(tableViewParam);
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
    public ResponseData delete(@RequestBody TableViewParam tableViewParam) {
        this.tableViewService.delete(tableViewParam);
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
    public ResponseData detail(@RequestBody TableViewParam tableViewParam) {
        TableView detail = this.tableViewService.getById(tableViewParam.getTableViewId());

        if (ToolUtil.isEmpty(detail)) {
            return null;
        }

        TableViewResult result = new TableViewResult();
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
    public PageInfo<TableViewResult> list(@RequestBody(required = false) TableViewParam tableViewParam) {
        if (ToolUtil.isEmpty(tableViewParam)) {
            tableViewParam = new TableViewParam();
        }
        return this.tableViewService.findPageBySpec(tableViewParam);
    }

    /**
     * 选择列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect(@RequestBody(required = false) TableViewParam tableViewParam) {
        QueryWrapper<TableView> tableViewQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(tableViewParam.getTableKey())) {
            tableViewQueryWrapper.in("table_key", tableViewParam.getTableKey());
        }
        Long userId = LoginContextHolder.getContext().getUserId();
        tableViewQueryWrapper.in("create_user", userId);
        tableViewQueryWrapper.eq("display", 1);
        List<Map<String, Object>> list = this.tableViewService.listMaps(tableViewQueryWrapper);
        TableViewSelectWrapper tableViewSelectWrapper = new TableViewSelectWrapper(list);
        List<Map<String, Object>> result = tableViewSelectWrapper.wrap();
        return ResponseData.success(result);
    }


}


