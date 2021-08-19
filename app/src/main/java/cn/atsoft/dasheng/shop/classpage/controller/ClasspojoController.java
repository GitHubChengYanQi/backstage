package cn.atsoft.dasheng.shop.classpage.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.shop.classpage.entity.Classpojo;
import cn.atsoft.dasheng.shop.classpage.model.params.ClassParam;
import cn.atsoft.dasheng.shop.classpage.model.result.ClassResult;
import cn.atsoft.dasheng.shop.classpage.service.ClassService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 一级分类导航控制器
 *
 * @author 
 * @Date 2021-08-19 11:17:44
 */
@RestController
@RequestMapping("/daoxinPortalClass")
@Api(tags = "一级分类导航")
public class ClasspojoController extends BaseController {

    @Autowired
    private ClassService classService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-08-19
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
     * @author 
     * @Date 2021-08-19
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
     * @author 
     * @Date 2021-08-19
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
     * @author 
     * @Date 2021-08-19
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<ClassResult> detail(@RequestBody ClassParam classParam) {
        Classpojo detail = this.classService.getById(classParam.getClassId());
        ClassResult result = new ClassResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-08-19
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


