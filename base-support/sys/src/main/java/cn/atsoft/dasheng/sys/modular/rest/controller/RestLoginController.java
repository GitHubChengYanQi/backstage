/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.atsoft.dasheng.sys.modular.rest.controller;

import cn.atsoft.dasheng.sys.modular.rest.model.params.LoginParam;
import cn.atsoft.dasheng.base.auth.service.AuthService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.model.response.SuccessResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * rest方式的登录控制器
 */
@Controller
@RequestMapping("/rest")
@Api(tags = "系统登录")
public class RestLoginController extends BaseController {

    @Resource
    private AuthService authService;

    /**
     * 点击登录执行的动作
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("登录接口")
    public ResponseData restLogin(@RequestBody @Valid LoginParam loginParam) {

        String username = loginParam.getUserName();
        String password = loginParam.getPassword();

        //登录并创建token
        String token = authService.login(username, password);

        return new SuccessResponseData(token);
    }

    /**
     * 退出接口
     *
     * @author fengshuonan
     * @Date 2020/2/16 22:26
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("退出接口")
    public ResponseData logout() {
        authService.logout();
        return new SuccessResponseData();
    }

    @RequestMapping(value = "/refreshToken" , method = RequestMethod.GET)
    @ApiOperation(value = "刷新token")
    @ResponseBody
    public ResponseData refreshToken() {
        return ResponseData.success(authService.refreshToken());
    }
}