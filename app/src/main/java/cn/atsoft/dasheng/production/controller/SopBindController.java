package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.SopBind;
import cn.atsoft.dasheng.production.model.params.SopBindParam;
import cn.atsoft.dasheng.production.model.result.SopBindResult;
import cn.atsoft.dasheng.production.service.SopBindService;
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
 * sop绑定工序控制器
 *
 * @author song
 * @Date 2022-02-17 10:50:23
 */
@RestController
@RequestMapping("/sopBind")
@Api(tags = "sop绑定工序")
public class SopBindController extends BaseController {


}


