package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.app.service.StockService;
import cn.atsoft.dasheng.app.wrapper.InstockSelectWrapper;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Instock;
import cn.atsoft.dasheng.app.model.params.InstockParam;
import cn.atsoft.dasheng.app.model.result.InstockResult;
import cn.atsoft.dasheng.app.service.InstockService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.model.request.InstockRequest;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.model.params.OrCodeBindParam;
import cn.atsoft.dasheng.orCode.model.params.OrCodeParam;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 入库表控制器
 *
 * @author song
 * @Date 2021-07-17 10:46:08
 */
@RestController
@RequestMapping("/instock")
@Api(tags = "入库表")
public class InstockController extends BaseController {

    @Autowired
    private InstockService instockService;
    @Autowired
    private StockService stockService;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private OrCodeBindService orCodeBindService;
    @Autowired
    private OrCodeService orCodeService;
    @Autowired
    private SkuService skuService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-07-17
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    @Permission
    public ResponseData addItem(@RequestBody InstockParam instockParam) {

        Long add = this.instockService.add(instockParam);
        return ResponseData.success(add);
    }


    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-07-17
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    @Permission
    public ResponseData update(@RequestBody InstockParam instockParam) {

        this.instockService.update(instockParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-07-17
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    @Permission
    public ResponseData delete(@RequestBody InstockParam instockParam) {
        this.instockService.delete(instockParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-07-17
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    @Permission
    public ResponseData<InstockResult> detail(@RequestBody InstockParam instockParam) {
        Instock detail = this.instockService.getById(instockParam.getInstockId());
        InstockResult result = new InstockResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-07-17
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    @Permission
    public PageInfo<InstockResult> list(@RequestBody(required = false) InstockParam instockParam) {
        if (ToolUtil.isEmpty(instockParam)) {
            instockParam = new InstockParam();
        }
//        return this.instockService.findPageBySpec(instockParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.instockService.findPageBySpec(instockParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.instockService.findPageBySpec(instockParam, dataScope);
        }
    }


    @Permission
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)

    public ResponseData<List<Map<String, Object>>> listSelect() {
        QueryWrapper<Instock> instockQueryWrapper = new QueryWrapper<>();
        instockQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.instockService.listMaps(instockQueryWrapper);
        InstockSelectWrapper instockSelectWrapper = new InstockSelectWrapper(list);
        List<Map<String, Object>> result = instockSelectWrapper.wrap();
        return ResponseData.success(result);
    }


    /**
     * 手机端入库
     *
     * @author song
     * @Date 2021-07-17
     */
    @Transactional
    @RequestMapping(value = "/apiInstock", method = RequestMethod.POST)
    public ResponseData apiInstock(@RequestBody InstockRequest instockRequest) {
        if (!instockRequest.getType().equals("sku")) {
            throw new ServiceException(500, "请确定商品类型");
        }

        List<Sku> skus = skuService.query().in("sku_id", instockRequest.getIds()).in("display", 1).list();
        if (skus.size() != instockRequest.getIds().size()) {
            throw new ServiceException(500, "你的数据不合法");
        }

        //入库 产品绑定二维码-----------------------------------------------------------------------------------------------
        List<OrCodeBind> orCodeBinds = new ArrayList<>();
        List<Long> codes = new ArrayList<>();
        for (Long id : instockRequest.getIds()) {
            OrCodeParam orCodeParam = new OrCodeParam();
            orCodeParam.setType(instockRequest.getType());
            Long aLong = orCodeService.add(orCodeParam);
            codes.add(aLong);
            OrCodeBind orCodeBind = new OrCodeBind();
            orCodeBind.setOrCodeId(aLong);
            orCodeBind.setSource(instockRequest.getType());
            orCodeBind.setFormId(id);
            orCodeBinds.add(orCodeBind);
        }



        orCodeBindService.saveBatch(orCodeBinds);
        return ResponseData.success(codes);
    }



}


