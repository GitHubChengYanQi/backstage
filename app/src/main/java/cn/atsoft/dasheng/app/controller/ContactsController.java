package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.entity.Contacts;
import cn.atsoft.dasheng.app.model.params.ContactsParam;
import cn.atsoft.dasheng.app.model.result.ContactsResult;
import cn.atsoft.dasheng.app.service.ContactsService;
import cn.atsoft.dasheng.app.wrapper.ContactsSelectWrapper;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 联系人表控制器
 *
 * @author ta
 * @Date 2021-07-19 11:26:02
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
     * @author ta
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ContactsParam contactsParam) {
        this.contactsService.add(contactsParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author ta
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ContactsParam contactsParam) {

        this.contactsService.update(contactsParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author ta
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ContactsParam contactsParam)  {
        this.contactsService.delete(contactsParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author ta
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<ContactsResult> detail(@RequestBody ContactsParam contactsParam) {
        Contacts detail = this.contactsService.getById(contactsParam.getId());
        ContactsResult result = new ContactsResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author ta
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ContactsResult> list(@RequestBody(required = false) ContactsParam contactsParam) {
      System.err.println(contactsParam);
        if(ToolUtil.isEmpty(contactsParam)){
            contactsParam = new ContactsParam();
        }
        return this.contactsService.findPageBySpec(contactsParam);
    }

  /**
   * 选择列表
   *
   * @author ta
   * @Date 2021-07-19
   */
  @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
  @ApiOperation("Select数据接口")
  public ResponseData<List<Map<String,Object>>> listSelect() {
    List<Map<String,Object>> list = this.contactsService.listMaps();
    ContactsSelectWrapper factory = new ContactsSelectWrapper(list);
    List<Map<String,Object>> result = factory.wrap();
    return ResponseData.success(result);
  }


}

