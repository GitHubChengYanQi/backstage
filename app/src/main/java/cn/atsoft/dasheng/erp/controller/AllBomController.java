package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.pojo.AllBom;
import cn.atsoft.dasheng.app.pojo.AllBomParam;
import cn.atsoft.dasheng.app.pojo.AllBomResult;
import cn.atsoft.dasheng.app.pojo.BomOrder;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
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
import java.util.Stack;

@RestController
@RequestMapping("/mes")
public class AllBomController {
    @Autowired
    private PartsService partsService;

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
                throw new ServiceException(500, skuId.getSkuId() + "没有bom");
            }
            if (!skuId.getFixed()) {
                ids.add(skuId.getSkuId());
            } else {
                noSort.add(skuId);
            }
        }

        /**
         *调用组合方法
         */

        List<List<Long>> lists = skuIdsList(ids);
        List<List<AllBomParam.SkuNumberParam>> allSkus = new ArrayList<>();
        for (List<Long> list : lists) {
            List<AllBomParam.SkuNumberParam> skuNumberParams = new ArrayList<>();
            for (Long aLong : list) {
                for (AllBomParam.SkuNumberParam numberParam : param.getSkuIds()) {
                    if (aLong.equals(numberParam.getSkuId())) {
                        skuNumberParams.addAll(noSort);
                        skuNumberParams.add(numberParam);
                        break;
                    }
                }
            }
            allSkus.add(skuNumberParams);
        }

        /**
         *  调用bom方法
         */

        AllBomResult allBomResult = new AllBomResult();
        List<BomOrder> results = new ArrayList<>();
        List<SkuResult> owes = new ArrayList<>();
        for (List<AllBomParam.SkuNumberParam> skus : allSkus) {
            AllBom allBom = new AllBom();
            allBom.start(skus);
            AllBomResult bom = allBom.getResult();
            results.addAll(bom.getResult());
            owes.addAll(bom.getOwe());
        }
        allBomResult.setResult(results);
        allBomResult.setOwe(owes);

        return ResponseData.success(allBomResult);
    }

    private List<List<Long>> skuIdsList(List<Long> skuIds) {
        List<List<Long>> skuIdsCell = new ArrayList<>();
        this.skuPartsMakeUp(skuIds, skuIds.size(), 0, skuIdsCell);
        return skuIdsCell;
    }

    /**
     * 排列组合sku
     */
    public static Stack<Long> stack = new Stack<Long>();

    private void skuPartsMakeUp(List<Long> skuIds, int count, int now, List<List<Long>> skuIdsCell) {
        if (now == count) {
            List<Long> stacks = new ArrayList<Long>(stack);
            skuIdsCell.add(stacks);

            return;
        }
        for (int i = 0; i < skuIds.size(); i++) {
            if (!stack.contains(skuIds.get(i))) {
                stack.add(skuIds.get(i));
                skuPartsMakeUp(skuIds, count, now + 1, skuIdsCell);
                stack.pop();
            }
        }
    }


}
