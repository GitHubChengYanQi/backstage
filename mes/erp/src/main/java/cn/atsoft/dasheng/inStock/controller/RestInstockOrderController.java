package cn.atsoft.dasheng.inStock.controller;

import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.inStock.entity.RestInstockOrder;
import cn.atsoft.dasheng.inStock.model.params.RestInstockOrderParam;
import cn.atsoft.dasheng.inStock.model.result.RestInstockOrderResult;
import cn.atsoft.dasheng.inStock.service.RestInstockOrderService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * 入库单控制器
 *
 * @author song
 * @Date 2021-10-06 09:43:44
 */
@RestController
@RequestMapping("/instockOrder/{version}")
@ApiVersion("2.0")
@Api(tags = "入库单")
public class RestInstockOrderController extends BaseController {
    @Autowired
    private RestInstockOrderService instockOrderService;

    public ResponseData showOrderList(){
        return ResponseData.success();
    }



}


