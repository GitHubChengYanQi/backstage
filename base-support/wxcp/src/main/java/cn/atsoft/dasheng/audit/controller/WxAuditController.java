package cn.atsoft.dasheng.audit.controller;

import cn.atsoft.dasheng.audit.config.TemplateConfig;
import cn.atsoft.dasheng.audit.model.params.WxCpMediaParam;
import cn.atsoft.dasheng.audit.model.result.WxCpMediaResult;
import cn.atsoft.dasheng.audit.service.RestWxCpService;
import cn.atsoft.dasheng.audit.model.params.WxAuditParam;
import cn.atsoft.dasheng.audit.service.WxAuditService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import me.chanjar.weixin.common.error.WxErrorException;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 控制器
 *
 * @author Captain_Jazz
 * @Date 2023-03-18 09:27:41
 */
@RestController
@RequestMapping("/wxAudit")
@Api(tags = "")
public class WxAuditController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WxAuditService wxAuditService;

    @Autowired
    private RestWxCpService wxCpService;
    @Autowired
    private TemplateConfig templateConfig;

//    /**
//     * 新增接口
//     *
//     * @author Captain_Jazz
//     * @Date 2023-03-18
//     */
//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    @ApiOperation("新增")
//    public ResponseData addItem(@RequestBody WxAuditParam wxAuditParam) {
//        this.wxAuditService.add(wxAuditParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 编辑接口
//     *
//     * @author Captain_Jazz
//     * @Date 2023-03-18
//     */
//    @RequestMapping(value = "/edit", method = RequestMethod.POST)
//    @ApiOperation("编辑")
//    public ResponseData update(@RequestBody WxAuditParam wxAuditParam) {
//
//        this.wxAuditService.update(wxAuditParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 删除接口
//     *
//     * @author Captain_Jazz
//     * @Date 2023-03-18
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody WxAuditParam wxAuditParam)  {
//        this.wxAuditService.delete(wxAuditParam);
//        return ResponseData.success();
//    }

//    /**
//     * 查看详情接口
//     *
//     * @author Captain_Jazz
//     * @Date 2023-03-18
//     */
//    @RequestMapping(value = "/detail", method = RequestMethod.POST)
//    @ApiOperation("详情")
//    public ResponseData<WxAuditResult> detail(@RequestBody WxAuditParam wxAuditParam) {
//        WxAudit detail = this.wxAuditService.getById(wxAuditParam.getSpNo());
//        WxAuditResult result = new WxAuditResult();
//        if (ToolUtil.isNotEmpty(detail)) {
//            ToolUtil.copyProperties(detail, result);
//        }
//
//        return ResponseData.success(result);
//    }
//
//    /**
//     * 查询列表
//     *
//     * @author Captain_Jazz
//     * @Date 2023-03-18
//     */
//    @RequestMapping(value = "/list", method = RequestMethod.POST)
//    @ApiOperation("列表")
//    public PageInfo<WxAuditResult> list(@RequestBody(required = false) WxAuditParam wxAuditParam) {
//        if(ToolUtil.isEmpty(wxAuditParam)){
//            wxAuditParam = new WxAuditParam();
//        }
//        return this.wxAuditService.findPageBySpec(wxAuditParam);
//    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    @RequestMapping(value = "/post", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData post (@RequestBody(required = false) WxAuditParam wxAuditParam) throws WxErrorException, IOException {
        if(ToolUtil.isEmpty(wxAuditParam)){
            wxAuditParam = new WxAuditParam();
        }
        return ResponseData.success(this.wxAuditService.post(wxAuditParam));
    }
    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    @RequestMapping(value = "/getTemplate", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData getTemplate (@RequestBody(required = false) WxAuditParam wxAuditParam) throws WxErrorException {
        if(ToolUtil.isEmpty(wxAuditParam)){
            wxAuditParam = new WxAuditParam();
        }
        return ResponseData.success(this.wxAuditService.getTemplate(templateConfig.getTemplateId()));
    }
    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    @RequestMapping(value = "/getDetail", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData getDetail (@RequestBody(required = false) WxAuditParam wxAuditParam) throws WxErrorException {
        if(ToolUtil.isEmpty(wxAuditParam)){
            wxAuditParam = new WxAuditParam();
        }
        if (ToolUtil.isEmpty(wxAuditParam.getSpNo())) {
            throw new ServiceException(500,"参数错误");
        }
        return ResponseData.success(this.wxAuditService.getDetail(wxAuditParam.getSpNo()));
    }


    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    @RequestMapping(value = "/getMedia", method = RequestMethod.GET)
    @ApiOperation("列表")
    public ResponseData getMedia (@RequestParam String mediaId) throws WxErrorException {
        if(ToolUtil.isEmpty(mediaId)){
            throw new ServiceException(500,"参数错误");
        }
        return ResponseData.success(wxCpService.getWxCpClient().getMediaService().download(mediaId));
    }
    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    @RequestMapping(value = "/getMediaByIds", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData getMedia (@RequestBody WxCpMediaParam param) throws WxErrorException {
        if(ToolUtil.isEmpty(param.getMediaIds()) || param.getMediaIds().size() == 0){
            throw new ServiceException(500,"参数错误");
        }
        List<WxCpMediaResult> resultList = new ArrayList<>();

        for (String mediaId : param.getMediaIds()) {
            resultList.add(new WxCpMediaResult(){{
                setMediaId(mediaId);
                setFile(wxCpService.getWxCpClient().getMediaService().download(mediaId));
            }});
        }

        return ResponseData.success(resultList);
    }



}


