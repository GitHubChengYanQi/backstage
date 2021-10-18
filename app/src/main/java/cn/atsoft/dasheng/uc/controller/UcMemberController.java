package cn.atsoft.dasheng.uc.controller;


import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.uc.entity.UcMember;
import cn.atsoft.dasheng.uc.model.params.UcMemberParam;
import cn.atsoft.dasheng.uc.model.result.UcMemberResult;
import cn.atsoft.dasheng.uc.service.UcMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * member
 *
 * @author cheng
 * @Date 2021-10-18 09:22:54
 */
@RestController
@RequestMapping("/ucMember")
@Api(tags = "资料")

public class UcMemberController extends BaseController {

    @Autowired
    private UcMemberService ucMemberService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-09-11
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody UcMemberParam ucMemberParam) {
        this.ucMemberService.add(ucMemberParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-09-11
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody UcMemberParam ucMemberParam) {

        this.ucMemberService.update(ucMemberParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-09-11
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody UcMemberParam ucMemberParam) {
        this.ucMemberService.delete(ucMemberParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-09-11
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<UcMemberResult> detail(@RequestBody UcMemberParam ucMemberParam) {
        UcMember byId = this.ucMemberService.getById(ucMemberParam.getMemberId());
        UcMemberResult result = new UcMemberResult();
        ToolUtil.copyProperties(byId, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-09-11
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<UcMemberResult> list(@RequestBody(required = false) UcMemberParam ucMemberParam) {
        if (ToolUtil.isEmpty(ucMemberParam)) {
            ucMemberParam = new UcMemberParam();
        }
        return this.ucMemberService.findPageBySpec(ucMemberParam);

    }


//    /**
//     * 批量删除
//     *
//     * @author song
//     * @Date 2021-09-11
//     */
//    @Permission
//    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
//    @ApiOperation("详情")
//    public ResponseData<DataResult> batchDelete(@RequestBody DataRequest dataRequest) {
//        dataService.batchDelete(dataRequest.getIds());
//        return ResponseData.success();
//    }

}
