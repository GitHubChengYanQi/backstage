package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.app.entity.Adress;
import cn.atsoft.dasheng.app.model.params.AdressParam;
import cn.atsoft.dasheng.app.wrapper.AdressSelectWrapper;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Announcements;
import cn.atsoft.dasheng.erp.model.params.AnnouncementsParam;
import cn.atsoft.dasheng.erp.model.result.AnnouncementsResult;
import cn.atsoft.dasheng.erp.service.AnnouncementsService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.wrapper.AnnouncementsSelectWrapper;
import cn.atsoft.dasheng.model.response.ResponseData;
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
 * 注意事项控制器
 *
 * @author song
 * @Date 2022-05-27 13:45:48
 */
@RestController
@RequestMapping("/announcements")
@Api(tags = "注意事项")
public class AnnouncementsController extends BaseController {

    @Autowired
    private AnnouncementsService announcementsService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-05-27
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody AnnouncementsParam announcementsParam) {
        this.announcementsService.add(announcementsParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2022-05-27
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody AnnouncementsParam announcementsParam) {

        this.announcementsService.update(announcementsParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2022-05-27
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody AnnouncementsParam announcementsParam) {
        this.announcementsService.delete(announcementsParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-05-27
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<AnnouncementsResult> detail(@RequestBody AnnouncementsParam announcementsParam) {
        Announcements detail = this.announcementsService.getById(announcementsParam.getNoticeId());
        AnnouncementsResult result = new AnnouncementsResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-05-27
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<AnnouncementsResult> list(@RequestBody(required = false) AnnouncementsParam announcementsParam) {
        if (ToolUtil.isEmpty(announcementsParam)) {
            announcementsParam = new AnnouncementsParam();
        }
        return this.announcementsService.findPageBySpec(announcementsParam);
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData<List<Map<String, Object>>> listSelect(@RequestBody(required = false) AdressParam adressParam) {
        List<Map<String, Object>> list = this.announcementsService.listMaps();
        AnnouncementsSelectWrapper selectWrapper = new AnnouncementsSelectWrapper(list);
        List<Map<String, Object>> result = selectWrapper.wrap();
        return ResponseData.success(result);
    }

}


