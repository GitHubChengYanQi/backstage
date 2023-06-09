package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.pojo.*;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mes")
public class AllBomController {
    @Autowired
    private PartsService partsService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private AsyncMethod asyncMethod;

    @RequestMapping(value = "/analysis", method = RequestMethod.POST)
    public ResponseData getBoms(@RequestBody @Valid AllBomParam param) {

        /**
         * 判断是否有bom
         */
        List<Long> ids = new ArrayList<>();
        List<AllBomParam.SkuNumberParam> noSort = new ArrayList<>();
        for (AllBomParam.SkuNumberParam skuId : param.getSkuIds()) {
            Parts one = partsService.query().eq("sku_id", skuId.getSkuId())
                    .eq("status", 99).eq("type", 1).one();
            if (ToolUtil.isEmpty(one)) {
                List<SkuResult> skuResults = skuService.formatSkuResult(new ArrayList<Long>() {{
                    add(skuId.getSkuId());
                }});
                throw new ServiceException(500, skuResults.get(0).getSpuResult().getName() + skuResults.get(0).getSkuName() + "没有bom");
            }
            if (!skuId.getFixed()) {
                ids.add(skuId.getSkuId());
            } else {
                noSort.add(skuId);
            }
        }


        /**
         * 调用 异步
         */
        asyncMethod.task(ids, noSort, param, "物料分析");

        return ResponseData.success("ok");
    }


}
