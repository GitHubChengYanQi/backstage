package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.model.result.ContactsRequest;
import cn.atsoft.dasheng.app.wrapper.ContactsSelectWrapper;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Contacts;
import cn.atsoft.dasheng.app.model.params.ContactsParam;
import cn.atsoft.dasheng.app.model.result.ContactsResult;
import cn.atsoft.dasheng.app.service.ContactsService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 联系人表控制器
 *
 * @author
 * @Date 2021-07-23 10:06:12
 */
@RestController
@RequestMapping("/contacts")
@Api(tags = "联系人表")
public class ContactsController extends BaseController {

    @Autowired
    private ContactsService contactsService;


    /**
     * 新增接口
     *
     * @author
     * @Date 2021-07-23
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    @Permission
    public ResponseData addItem(@RequestBody @Valid ContactsParam contactsParam) {

        Contacts contacts = contactsService.add(contactsParam);
        return ResponseData.success(contacts);
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-07-23
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    @Permission
    public ResponseData update(@RequestBody ContactsParam contactsParam) {

        this.contactsService.update(contactsParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-07-23
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    @Permission
    public ResponseData delete(@RequestBody ContactsParam contactsParam) {
        this.contactsService.delete(contactsParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-07-23
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    @Permission
    public ResponseData detail(@RequestBody ContactsParam contactsParam) {
        Contacts detail = this.contactsService.getById(contactsParam.getContactsId());
        ContactsResult result = new ContactsResult();
        List<ContactsResult> results = new ArrayList<>();
        ToolUtil.copyProperties(detail, result);
        results.add(result);
        this.contactsService.format(results);
        for (ContactsResult res : results) {
            return ResponseData.success(res);
        }
//        result.setValue(parentValue);
        return ResponseData.success();
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-07-23
     */

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    @Permission
    public PageInfo list(@RequestBody(required = false) ContactsParam contactsParam) {
        if (ToolUtil.isEmpty(contactsParam)) {
            contactsParam = new ContactsParam();
        }
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.contactsService.findPageBySpec(null, contactsParam);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.contactsService.findPageBySpec(dataScope, contactsParam);
        }
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    @Permission
    public ResponseData listSelect() {
        QueryWrapper<Contacts> contactsQueryWrapper = new QueryWrapper<>();
        contactsQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.contactsService.listMaps(contactsQueryWrapper);
        ContactsSelectWrapper contactsSelectWrapper = new ContactsSelectWrapper(list);
        List<Map<String, Object>> result = contactsSelectWrapper.wrap();
        return ResponseData.success(result);

    }

    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    @ApiOperation("批量删除")
    @Permission
    public ResponseData batchDelete(@RequestBody ContactsRequest contactsRequest) {
        contactsService.batchDelete(contactsRequest.getContactsId());
        return ResponseData.success();
    }

}
