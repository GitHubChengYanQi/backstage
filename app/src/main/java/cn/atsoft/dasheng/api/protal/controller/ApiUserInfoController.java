package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.api.uc.model.params.OpenUserInfoParam;
import cn.atsoft.dasheng.api.uc.model.result.OpenUserInfoResult;
import cn.atsoft.dasheng.api.uc.service.OpenUserInfoService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.rest.service.RestUserService;
import cn.atsoft.dasheng.sys.modular.rest.wrapper.RestUserSelectWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class ApiUserInfoController {
    @Autowired
    private OpenUserInfoService openUserInfoService;
    @Autowired
    private RestUserService restUserService;


    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    public PageInfo<OpenUserInfoResult> list(@RequestBody(required = false) OpenUserInfoParam openUserInfoParam) {
        if(ToolUtil.isEmpty(openUserInfoParam)){
            openUserInfoParam = new OpenUserInfoParam();
        }
        return this.openUserInfoService.findPageBySpec(openUserInfoParam);
    }

    @RequestMapping(value = "/getUser", method = RequestMethod.POST)
    public ResponseData listSelect() {
        List<Map<String, Object>> list = this.restUserService.listMaps();
        RestUserSelectWrapper restUserSelectWrapper = new RestUserSelectWrapper(list);
        List<Map<String, Object>> result = restUserSelectWrapper.wrap();
        return ResponseData.success(result);
    }


}
