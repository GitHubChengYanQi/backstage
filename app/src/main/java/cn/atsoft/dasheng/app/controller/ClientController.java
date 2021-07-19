package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Client;
import cn.atsoft.dasheng.app.model.params.ClientParam;
import cn.atsoft.dasheng.app.model.result.ClientResult;
import cn.atsoft.dasheng.app.service.ClientService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import cn.atsoft.dasheng.app.wrapper.ClientSelectWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 客户管理表控制器
 *
 * @author ta
 * @Date 2021-07-19 11:26:02
 */
@RestController
@RequestMapping("/client")
@Api(tags = "客户管理表")
public class ClientController extends BaseController {

    @Autowired
    private ClientService clientService;

    /**
     * 新增接口
     *
     * @author ta
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ClientParam clientParam) {
        this.clientService.add(clientParam);
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
    public ResponseData update(@RequestBody ClientParam clientParam) {

        this.clientService.update(clientParam);
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
    public ResponseData delete(@RequestBody ClientParam clientParam)  {
        this.clientService.delete(clientParam);
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
    public ResponseData<ClientResult> detail(@RequestBody ClientParam clientParam) {
        Client detail = this.clientService.getById(clientParam.getClientId());
        ClientResult result = new ClientResult();
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
    public PageInfo<ClientResult> list(@RequestBody(required = false) ClientParam clientParam) {
        if(ToolUtil.isEmpty(clientParam)){
            clientParam = new ClientParam();
        }
        return this.clientService.findPageBySpec(clientParam);
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
        List<Map<String,Object>> list = this.clientService.listMaps();
        ClientSelectWrapper factory = new ClientSelectWrapper(list);
        List<Map<String,Object>> result = factory.wrap();
        return ResponseData.success(result);
    }



}


