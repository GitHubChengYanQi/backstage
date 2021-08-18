package cn.atsoft.dasheng.portal.class.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.class.entity.Class;
import cn.atsoft.dasheng.portal.class.model.params.ClassParam;
import cn.atsoft.dasheng.portal.class.model.result.ClassResult;
import cn.atsoft.dasheng.portal.class.service.ClassService;
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
 * 分类导航控制器
 *
 * @author siqiang
 * @Date 2021-08-18 15:53:56
 */
@RestController
@RequestMapping("/class")
@Api(tags = "分类导航")
public class ClassController extends BaseController {

    @Autowired
    private ClassService classService;

    /**
     * 新增接口
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ClassParam classParam) {
        this.classService.add(classParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ClassParam classParam) {

        this.classService.update(classParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ClassParam classParam)  {
        this.classService.delete(classParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<ClassResult> detail(@RequestBody ClassParam classParam) {
        Class detail = this.classService.getById(classParam.getClassId());
        ClassResult result = new ClassResult();
        ToolUtil.copyProperties(detail, result);

        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ClassResult> list(@RequestBody(required = false) ClassParam classParam) {
        if(ToolUtil.isEmpty(classParam)){
            classParam = new ClassParam();
        }
        return this.classService.findPageBySpec(classParam);
    }




}


