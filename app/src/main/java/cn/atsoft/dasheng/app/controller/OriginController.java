package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.model.result.BatchDeleteRequest;
import cn.atsoft.dasheng.app.wrapper.OriginSelectWrapper;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Origin;
import cn.atsoft.dasheng.app.model.params.OriginParam;
import cn.atsoft.dasheng.app.model.result.OriginResult;
import cn.atsoft.dasheng.app.service.OriginService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;


/**
 * 来源表控制器
 *
 * @author
 * @Date 2021-07-19 17:59:08
 */
@RestController
@RequestMapping("/origin")
@Api(tags = "来源表")
public class OriginController extends BaseController {

    @Autowired
    private OriginService originService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody OriginParam originParam) {
        Long add = this.originService.add(originParam);
        return ResponseData.success(add);
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody OriginParam originParam) {

        this.originService.update(originParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody OriginParam originParam) {
        this.originService.delete(originParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody OriginParam originParam) {
        Origin detail = this.originService.getById(originParam.getOriginId());
        OriginResult result = new OriginResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<OriginResult> list(@RequestBody(required = false) OriginParam originParam) {
        if (ToolUtil.isEmpty(originParam)) {
            originParam = new OriginParam();
        }
//        return this.originService.findPageBySpec(originParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.originService.findPageBySpec(originParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.originService.findPageBySpec(originParam, dataScope);
        }
    }

    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect() {
        QueryWrapper<Origin> originQueryWrapper = new QueryWrapper<>();
        originQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.originService.listMaps(originQueryWrapper);
        OriginSelectWrapper factory = new OriginSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }

    /**
     * 批量删除
     *
     * @author
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData batchDelete(@RequestBody BatchDeleteRequest batchDeleteRequest) {
           this.originService.batchDelete(batchDeleteRequest.getIds());
           return ResponseData.success();
    }
}


