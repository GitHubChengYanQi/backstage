package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.app.entity.Contacts;
import cn.atsoft.dasheng.app.wrapper.ContactsSelectWrapper;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.Sop;
import cn.atsoft.dasheng.production.model.params.SopParam;
import cn.atsoft.dasheng.production.model.result.SopResult;
import cn.atsoft.dasheng.production.pojo.ImgParam;
import cn.atsoft.dasheng.production.service.SopService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.production.wrapper.SopSelectWrapper;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * sop主表控制器
 *
 * @author song
 * @Date 2022-02-10 09:21:35
 */
@RestController
@RequestMapping("/sop")
@Api(tags = "sop主表")
public class SopController extends BaseController {

    @Autowired
    private SopService sopService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody SopParam sopParam) {
        this.sopService.add(sopParam);
        return ResponseData.success();
    }


    @RequestMapping(value = "/getImgUrls", method = RequestMethod.POST)
    public ResponseData getImgUrls(@Valid @RequestBody ImgParam imgParam) {
        List<String> imgUrls = this.sopService.getImgUrls(imgParam.getImgs());
        return ResponseData.success(imgUrls);
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody SopParam sopParam) {
        this.sopService.update(sopParam);
        return ResponseData.success();
    }


    /**
     * 添加工序
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/addShip", method = RequestMethod.POST)
    @ApiOperation("添加工序")
    public ResponseData addShip(@RequestBody SopParam sopParam) {
        this.sopService.addShip(sopParam.getSopId(), sopParam.getShipSetpId());
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody SopParam sopParam) {
        SopResult result = this.sopService.detail(sopParam.getSopId());
        return ResponseData.success(result);
    }

    /**
     * 删除接口
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody SopParam sopParam) {
        sopService.sopdeatilIdsBysopId(sopParam);
        return ResponseData.success();
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<SopResult> list(@RequestBody(required = false) SopParam sopParam) {
        if (ToolUtil.isEmpty(sopParam)) {
            sopParam = new SopParam();
        }
        return this.sopService.findPageBySpec(sopParam);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData listSelect(@RequestBody(required = false) SopParam sopParam) {

        QueryWrapper<Sop> contactsQueryWrapper = new QueryWrapper<>();
        contactsQueryWrapper.in("display", 1);

        Sop sop = null;

        if (ToolUtil.isNotEmpty(sopParam)) {
            if (ToolUtil.isNotEmpty(sopParam.getSopId())) {
                sop = this.sopService.getById(sopParam.getSopId());
            }
            contactsQueryWrapper.isNull("ship_setp_id");
        }

        List<Map<String, Object>> list = this.sopService.listMaps(contactsQueryWrapper);
        SopSelectWrapper contactsSelectWrapper = new SopSelectWrapper(list);
        List<Map<String, Object>> result = contactsSelectWrapper.wrap();

        if (ToolUtil.isNotEmpty(sop)) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", sop.getName());
            map.put("value", sop.getSopId());
            result.add(map);
        }

        return ResponseData.success(result);
    }


}


