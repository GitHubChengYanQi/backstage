package cn.atsoft.dasheng.storehousePosition.controller;

import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.storehouse.model.params.RestStorehouseParam;
import cn.atsoft.dasheng.storehousePosition.entity.RestStorehousePositions;
import cn.atsoft.dasheng.storehousePosition.model.params.RestStorehousePositionsParam;
import cn.atsoft.dasheng.storehousePosition.service.RestStorehousePositionsService;
import cn.atsoft.dasheng.storehousePosition.service.impl.RestStorehousePositionsServiceImpl;
import cn.atsoft.dasheng.sys.modular.rest.entity.RestDept;
import cn.atsoft.dasheng.sys.modular.rest.model.params.DeptParam;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/storehousePositions/{version}")
@Api(tags = "库位")
@ApiVersion("2.0")
public class RestStorehousePositionsController {
    @Autowired
    private RestStorehousePositionsService storehousePositionsService;

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/sort", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData sort(@RequestBody(required = false) RestStorehousePositionsParam param) {
        //批量更改排序
        if (ToolUtil.isEmpty(param) || ToolUtil.isEmpty(param.getSortList())){
            return ResponseData.error("参数错误");
        }
        //校验sortList中的参数 防止空指针
        for (RestStorehousePositionsParam.Sort restDept : param.getSortList()) {
            if (ToolUtil.isOneEmpty(restDept,restDept.getStorehousePositionsId(),restDept.getSort())){
                return ResponseData.error("参数错误");
            }
        }
        //验证sortParam中的id在数据库中是否存在
        List<Long> ids = param.getSortList().stream().map(RestStorehousePositionsParam.Sort::getStorehousePositionsId).distinct().collect(Collectors.toList());
        List<RestStorehousePositions> restDepts = storehousePositionsService.listByIds(ids);
        if (restDepts.size() != param.getSortList().size()){
            return ResponseData.error("参数错误");
        }
        List<RestStorehousePositions> updateEntity = BeanUtil.copyToList(param.getSortList(), RestStorehousePositions.class);

        storehousePositionsService.updateBatchById(updateEntity);

        return ResponseData.success();

    }
}
