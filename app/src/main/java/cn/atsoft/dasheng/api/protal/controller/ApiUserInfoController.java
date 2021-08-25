package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.api.uc.model.params.OpenUserInfoParam;
import cn.atsoft.dasheng.api.uc.model.result.OpenUserInfoResult;
import cn.atsoft.dasheng.api.uc.service.OpenUserInfoService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class ApiUserInfoController {
    @Autowired
    private OpenUserInfoService openUserInfoService;

    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    public PageInfo<OpenUserInfoResult> list(@RequestBody(required = false) OpenUserInfoParam openUserInfoParam) {
        if(ToolUtil.isEmpty(openUserInfoParam)){
            openUserInfoParam = new OpenUserInfoParam();
        }
        return this.openUserInfoService.findPageBySpec(openUserInfoParam);
    }
}
