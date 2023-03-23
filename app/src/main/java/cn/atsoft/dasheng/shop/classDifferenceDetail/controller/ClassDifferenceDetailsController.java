package cn.atsoft.dasheng.shop.classDifferenceDetail.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.shop.classDifferenceDetail.entity.ClassDifferenceDetails;
import cn.atsoft.dasheng.shop.classDifferenceDetail.model.params.ClassDifferenceDetailsParam;
import cn.atsoft.dasheng.shop.classDifferenceDetail.model.result.ClassDifferenceDetailsResult;
import cn.atsoft.dasheng.shop.classDifferenceDetail.service.ClassDifferenceDetailsService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 分类明细内容控制器
 *
 * @author siqiang
 * @Date 2021-08-18 15:57:52
 */
@RestController
@RequestMapping("/classDifferenceDetails")
@Api(tags = "分类明细内容")
public class ClassDifferenceDetailsController extends BaseController {

    @Autowired
    private ClassDifferenceDetailsService classDifferenceDetailsService;

    /**
     * 新增接口
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ClassDifferenceDetailsParam classDifferenceDetailsParam) {
        this.classDifferenceDetailsService.add(classDifferenceDetailsParam);
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
    public ResponseData update(@RequestBody ClassDifferenceDetailsParam classDifferenceDetailsParam) {

        this.classDifferenceDetailsService.update(classDifferenceDetailsParam);
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
    public ResponseData delete(@RequestBody ClassDifferenceDetailsParam classDifferenceDetailsParam)  {
        this.classDifferenceDetailsService.delete(classDifferenceDetailsParam);
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
    public ResponseData detail(@RequestBody ClassDifferenceDetailsParam classDifferenceDetailsParam) {
        ClassDifferenceDetails detail = this.classDifferenceDetailsService.getById(classDifferenceDetailsParam.getDetailId());
        ClassDifferenceDetailsResult result = new ClassDifferenceDetailsResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }


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
    public PageInfo<ClassDifferenceDetailsResult> list(@RequestBody(required = false) ClassDifferenceDetailsParam classDifferenceDetailsParam) {
        if(ToolUtil.isEmpty(classDifferenceDetailsParam)){
            classDifferenceDetailsParam = new ClassDifferenceDetailsParam();
        }
        return this.classDifferenceDetailsService.findPageBySpec(classDifferenceDetailsParam);
    }




}


