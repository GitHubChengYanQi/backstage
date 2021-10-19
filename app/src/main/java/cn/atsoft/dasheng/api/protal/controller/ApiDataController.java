package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.binding.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.binding.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.model.params.DataParam;
import cn.atsoft.dasheng.crm.model.result.DataResult;
import cn.atsoft.dasheng.crm.service.DataService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.uc.jwt.UcJwtPayLoad;
import cn.atsoft.dasheng.uc.utils.UserUtils;
import cn.atsoft.dasheng.userInfo.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiDataController {
    @Autowired
    private DataService dataService;


    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-09-11
     */
    @RequestMapping(value = "/listData", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<DataResult> list(@RequestBody(required = false) DataParam dataParam) {
        UserUtils.getUserId();

        if (ToolUtil.isEmpty(dataParam)) {
            dataParam = new DataParam();
        }
        return this.dataService.findPageBySpec(null, dataParam);
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-09-11
     */
    @RequestMapping(value = "/detailData", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<DataResult> detail(@RequestBody DataParam dataParam) {
        UserUtils.getUserId();
        DataResult detail = dataService.detail(dataParam);
        return ResponseData.success(detail);
    }
}

