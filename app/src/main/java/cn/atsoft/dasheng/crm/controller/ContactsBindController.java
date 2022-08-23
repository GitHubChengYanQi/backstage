package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.ContactsBind;
import cn.atsoft.dasheng.crm.model.params.ContactsBindParam;
import cn.atsoft.dasheng.crm.model.result.ContactsBindResult;
import cn.atsoft.dasheng.crm.service.ContactsBindService;
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
 * 联系人绑定表控制器
 *
 * @author song
 * @Date 2021-09-22 11:59:36
 */
@RestController
@RequestMapping("/contactsBind")
@Api(tags = "联系人绑定表")
public class ContactsBindController extends BaseController {

    @Autowired
    private ContactsBindService contactsBindService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-09-22
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ContactsBindParam contactsBindParam) {
        this.contactsBindService.add(contactsBindParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-09-22
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ContactsBindParam contactsBindParam) {

        this.contactsBindService.update(contactsBindParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-09-22
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ContactsBindParam contactsBindParam)  {
        this.contactsBindService.delete(contactsBindParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-09-22
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ContactsBindParam contactsBindParam) {
        ContactsBind detail = this.contactsBindService.getById(contactsBindParam.getContactsBindId());
        ContactsBindResult result = new ContactsBindResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-09-22
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ContactsBindResult> list(@RequestBody(required = false) ContactsBindParam contactsBindParam) {
        if(ToolUtil.isEmpty(contactsBindParam)){
            contactsBindParam = new ContactsBindParam();
        }
        return this.contactsBindService.findPageBySpec(contactsBindParam);
    }




}


