package cn.atsoft.dasheng.audit.controller;

import cn.atsoft.dasheng.audit.config.TemplateConfig;
import cn.atsoft.dasheng.audit.service.RestWxCpService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.audit.entity.WxAudit;
import cn.atsoft.dasheng.audit.model.params.WxAuditParam;
import cn.atsoft.dasheng.audit.model.result.WxAuditResult;
import cn.atsoft.dasheng.audit.service.WxAuditService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.message.WxCpXmlOutMessage;
import me.chanjar.weixin.cp.bean.oa.WxCpOaApplyEventRequest;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;


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

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody WxAuditParam wxAuditParam) {
        this.wxAuditService.add(wxAuditParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody WxAuditParam wxAuditParam) {

        this.wxAuditService.update(wxAuditParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody WxAuditParam wxAuditParam)  {
        this.wxAuditService.delete(wxAuditParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<WxAuditResult> detail(@RequestBody WxAuditParam wxAuditParam) {
        WxAudit detail = this.wxAuditService.getById(wxAuditParam.getSpNo());
        WxAuditResult result = new WxAuditResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<WxAuditResult> list(@RequestBody(required = false) WxAuditParam wxAuditParam) {
        if(ToolUtil.isEmpty(wxAuditParam)){
            wxAuditParam = new WxAuditParam();
        }
        return this.wxAuditService.findPageBySpec(wxAuditParam);
    }

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



}


