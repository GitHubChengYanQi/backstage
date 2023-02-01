package cn.atsoft.dasheng.coderule.controller;

import cn.atsoft.dasheng.coderule.service.RestCodingRulesCategoryService;
import cn.atsoft.dasheng.coderule.wrapper.RestCodingRulesCategorySelectWrapper;
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
public class RestCodingRulesCategoryController extends BaseController {

    @Autowired
    private RestCodingRulesCategoryService codingRulesClassificationService;

    /**
     * 下拉接口
     *
     * @return
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    public ResponseData listSelect() {
        List<Map<String, Object>> list = this.codingRulesClassificationService.listMaps();
        RestCodingRulesCategorySelectWrapper codingRulesClassificationSelectWrapper = new RestCodingRulesCategorySelectWrapper(list);
        List<Map<String, Object>> result = codingRulesClassificationSelectWrapper.wrap();
        return ResponseData.success(result);
    }


}


