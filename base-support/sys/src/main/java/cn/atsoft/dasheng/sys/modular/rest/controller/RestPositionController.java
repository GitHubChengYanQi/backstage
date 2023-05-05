package cn.atsoft.dasheng.sys.modular.rest.controller;

import cn.atsoft.dasheng.sys.modular.rest.entity.RestPosition;
import cn.atsoft.dasheng.sys.modular.system.model.params.PositionParam;
import cn.atsoft.dasheng.base.enums.CommonStatus;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.sys.modular.rest.service.RestPositionService;
import cn.atsoft.dasheng.sys.modular.rest.wrapper.PositionSelectWrapper;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.RequestEmptyException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.model.response.SuccessResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 职位表控制器
 *
 * @author stylefeng
 * @Date 2019-06-27 2:47
 */
@RestController
@RequestMapping("/rest/position")
public class RestPositionController extends BaseController {

    @Autowired
    private RestPositionService restPositionService;

    /**
     * 新增接口
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    @RequestMapping("/addItem")
    public ResponseData addItem(@RequestBody PositionParam positionParam) {
        return ResponseData.success(this.restPositionService.add(positionParam));
    }

    /**
     * 编辑接口
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    @RequestMapping("/editItem")
    public ResponseData editItem(@RequestBody PositionParam positionParam) {
        this.restPositionService.update(positionParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    @RequestMapping("/delete")
    public ResponseData delete(@RequestBody PositionParam positionParam) {
        this.restPositionService.delete(positionParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    @RequestMapping("/detail")
    public ResponseData detail(@RequestBody PositionParam positionParam) {
        RestPosition detail = this.restPositionService.getById(positionParam.getPositionId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    @RequestMapping("/list")
    public PageInfo list(@RequestParam(value = "condition", required = false) String condition) {

        PositionParam positionParam = new PositionParam();
        if (ToolUtil.isNotEmpty(condition)) {
            positionParam.setCode(condition);
            positionParam.setName(condition);
        }

        return this.restPositionService.findPageBySpec(positionParam);
    }

    /**
     * 修改状态
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    @RequestMapping("/changeStatus")
    public ResponseData changeStatus(@RequestParam("positionId") String positionId,
                                     @RequestParam("status") Boolean status) {

        RestPosition position = this.restPositionService.getById(positionId);
        if (position == null) {
            throw new RequestEmptyException();
        }

        if (status) {
            position.setStatus(CommonStatus.ENABLE.getCode());
        } else {
            position.setStatus(CommonStatus.DISABLE.getCode());
        }

        this.restPositionService.updateById(position);

        return new SuccessResponseData();
    }

    /**
     * 查询所有职位
     */
    @RequestMapping("/listPositions")
    public ResponseData listlistPositionsTypes() {
        List<Map<String, Object>> lists = this.restPositionService.listPositions();
        PositionSelectWrapper factory = new PositionSelectWrapper(lists);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }


}


