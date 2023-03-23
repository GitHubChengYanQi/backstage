package cn.atsoft.dasheng.shop.classDifference.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.shop.classDifference.entity.ClassDifference;
import cn.atsoft.dasheng.shop.classDifference.model.params.ClassDifferenceParam;
import cn.atsoft.dasheng.shop.classDifference.model.result.ClassDifferenceResult;
import cn.atsoft.dasheng.shop.classDifference.service.ClassDifferenceService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;

import cn.atsoft.dasheng.shop.classDifference.wrapper.ClassDifferenceSelectWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;


/**
 * 分类明细控制器
 *
 * @author siqiang
 * @Date 2021-08-18 15:57:33
 */
@RestController
@RequestMapping("/classDifference")
@Api(tags = "分类明细")
public class ClassDifferenceController extends BaseController {

    @Autowired
    private ClassDifferenceService classDifferenceService;

    /**
     * 新增接口
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ClassDifferenceParam classDifferenceParam) {
        this.classDifferenceService.add(classDifferenceParam);
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
    public ResponseData update(@RequestBody ClassDifferenceParam classDifferenceParam) {

        this.classDifferenceService.update(classDifferenceParam);
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
    public ResponseData delete(@RequestBody ClassDifferenceParam classDifferenceParam)  {
        this.classDifferenceService.delete(classDifferenceParam);
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
    public ResponseData detail(@RequestBody ClassDifferenceParam classDifferenceParam) {
        ClassDifference detail = this.classDifferenceService.getById(classDifferenceParam.getClassDifferenceId());
        ClassDifferenceResult result = new ClassDifferenceResult();
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
    public PageInfo<ClassDifferenceResult> list(@RequestBody(required = false) ClassDifferenceParam classDifferenceParam) {
        if(ToolUtil.isEmpty(classDifferenceParam)){
            classDifferenceParam = new ClassDifferenceParam();
        }
        return this.classDifferenceService.findPageBySpec(classDifferenceParam);
    }

    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect() {
        QueryWrapper<ClassDifference> queryWrapper = new QueryWrapper();
        queryWrapper.in("display",1);
        List<Map<String, Object>> list = this.classDifferenceService.listMaps(queryWrapper);
        ClassDifferenceSelectWrapper classDifferenceSelectWrapper = new ClassDifferenceSelectWrapper(list);
        List<Map<String, Object>> result = classDifferenceSelectWrapper.wrap();
        return ResponseData.success(result);
    }


}


