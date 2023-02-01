package cn.atsoft.dasheng.codeRule.controller;

import cn.atsoft.dasheng.codeRule.service.RestCodeRuleCategoryService;
import cn.atsoft.dasheng.codeRule.wrapper.RestCodeRuleCategorySelectWrapper;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.model.response.ResponseData;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;


/**
 * 编码规则分类控制器
 *
 * @author song
 * @Date 2021-10-22 17:20:05
 */
@RestController
@RequestMapping("/codingRulesCategory/{version}")
@ApiVersion("2.0")
@Api(tags = "编码规则分类 管理")
public class RestCodeRuleCategoryController extends BaseController {

    @Autowired
    private RestCodeRuleCategoryService codingRulesClassificationService;

    /**
     * 下拉接口
     *
     * @return
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    public ResponseData listSelect() {
        return ResponseData.success(this.codingRulesClassificationService.get());
    }


}


