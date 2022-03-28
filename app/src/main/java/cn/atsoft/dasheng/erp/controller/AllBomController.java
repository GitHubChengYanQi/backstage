package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.app.pojo.AllBom;
import cn.atsoft.dasheng.app.pojo.AllBomParam;
import cn.atsoft.dasheng.app.pojo.AllBomResult;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@RestController
@RequestMapping("/mes")
public class AllBomController {


    @RequestMapping(value = "/analysis", method = RequestMethod.POST)
    public ResponseData getBoms(@RequestBody AllBomParam param) {

        List<AllBomResult> bomResults = new ArrayList<>();
        List<Long> ids = new ArrayList<>();

        for (AllBomParam.SkuNumberParam skuId : param.getSkuIds()) {
            ids.add(skuId.getSkuId());
        }


        List<List<Long>> lists = skuIdsList(ids);
        List<List<AllBomParam.SkuNumberParam>> allSkus = new ArrayList<>();


        for (List<Long> list : lists) {
            List<AllBomParam.SkuNumberParam> skuNumberParams = new ArrayList<>();
            for (Long aLong : list) {
                for (AllBomParam.SkuNumberParam numberParam : param.getSkuIds()) {
                    if (aLong.equals(numberParam.getSkuId())) {
                        skuNumberParams.add(numberParam);
                        break;
                    }
                }
            }
            allSkus.add(skuNumberParams);
        }

        AllBomResult allBomResult = new AllBomResult();
        for (List<AllBomParam.SkuNumberParam> skus : allSkus) {
            AllBom allBom = new AllBom();
            allBom.start(skus);
            AllBomResult bom = allBom.getResult();
            allBomResult.setResult(bom.getResult());
            allBomResult.setOwe(bom.getOwe());
            bomResults.add(allBomResult);
        }


        return ResponseData.success(bomResults);
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
