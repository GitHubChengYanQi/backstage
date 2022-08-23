package cn.atsoft.dasheng.sys.modular.system.controller;

import cn.atsoft.dasheng.base.enums.CommonStatus;
import cn.atsoft.dasheng.base.pojo.page.LayuiPageInfo;
import cn.atsoft.dasheng.sys.modular.rest.wrapper.PositionSelectWrapper;
import cn.atsoft.dasheng.sys.modular.system.entity.Position;
import cn.atsoft.dasheng.sys.modular.system.model.params.PositionParam;
import cn.atsoft.dasheng.sys.modular.system.service.PositionService;
import cn.atsoft.dasheng.sys.modular.system.service.UserPosService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.RequestEmptyException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.model.response.SuccessResponseData;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 职位表控制器
 *
 * @author stylefeng
 * @Date 2019-06-27 21:33:47
 */
@RestController
@RequestMapping("/position")
public class PositionController extends BaseController {

    private String PREFIX = "/modular/system/position";

    @Autowired
    private PositionService positionService;

    @Autowired
    private UserPosService userPosService;

    /**
     * 跳转到主页面
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/position.html";
    }

    /**
     * 新增页面
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    @RequestMapping("/add")
    public String add() {
        return PREFIX + "/position_add.html";
    }

    /**
     * 编辑页面
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/position_edit.html";
    }

    /**
     * 新增接口
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    @RequestMapping(value = "/addItem", method = RequestMethod.POST)
    public ResponseData addItem(@RequestBody PositionParam positionParam) {
        this.positionService.add(positionParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    @RequestMapping(value = "/editItem", method = RequestMethod.POST)
    public ResponseData editItem(@RequestBody PositionParam positionParam) {
        this.positionService.update(positionParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData delete(@RequestBody PositionParam positionParam) {
        this.positionService.delete(positionParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    @RequestMapping(value = "/detail",method = RequestMethod.POST)
    @ResponseBody
    public ResponseData detail(PositionParam positionParam) {
        Position detail = this.positionService.getById(positionParam.getPositionId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    @ResponseBody
    @RequestMapping("/list")
    public LayuiPageInfo list(@RequestParam(value = "condition", required = false) String condition) {

        PositionParam positionParam = new PositionParam();
        if (ToolUtil.isNotEmpty(condition)) {
            positionParam.setCode(condition);
            positionParam.setName(condition);
        }

        return this.positionService.findPageBySpec(positionParam);
    }

    /**
     * 修改状态
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    @ResponseBody
    @RequestMapping("/changeStatus")
    public ResponseData changeStatus(@RequestParam("positionId") String positionId,
                                     @RequestParam("status") Boolean status) {

        Position position = this.positionService.getById(positionId);
        if (position == null) {
            throw new RequestEmptyException();
        }

        if (status) {
            position.setStatus(CommonStatus.ENABLE.getCode());
        } else {
            position.setStatus(CommonStatus.DISABLE.getCode());
        }

        this.positionService.updateById(position);

        return new SuccessResponseData();
    }

    /**
     * 查询所有职位
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @ResponseBody
    @RequestMapping("/listPositions")
    public LayuiPageInfo listlistPositionsTypes(@RequestParam(value = "userId", required = false) Long userId) {
        return this.positionService.listPositions(userId);
    }

    /**
     * 选择列表
     *
     * @author jazz
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect() {
        List<Map<String, Object>> list = this.positionService.listMaps();
        PositionSelectWrapper factory = new PositionSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }


}


