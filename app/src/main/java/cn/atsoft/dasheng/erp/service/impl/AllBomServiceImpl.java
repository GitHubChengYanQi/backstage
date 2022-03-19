package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.pojo.AllBom;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.AllBomService;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AllBomServiceImpl implements AllBomService {

    @Override
    public Map<Long, ArrayList<Map<Long, Object>>> getBom(Long skuId, List<Parts> parts, List<ErpPartsDetail> details) {

        Map<Long, ArrayList<Map<Long, Object>>> map = new HashMap<>();
        ArrayList<Map<Long, Object>> childList = new ArrayList<>();

        Map<Long, Object> objectMap = new HashMap<>();
        Parts newParts = new Parts();

        for (Parts part : parts) {
            if (ToolUtil.isNotEmpty(part.getSkuId()) && part.getSkuId().equals(skuId) && part.getStatus() == 99) {
                newParts = part;
            }
        }
        List<Long> children = JSON.parseArray(newParts.getChildren(), Long.class);
        List<Long> childrens = JSON.parseArray(newParts.getChildrens(), Long.class);


        for (ErpPartsDetail detail : details) {
            for (Long child : children) {
                if (child.equals(detail.getSkuId()) && detail.getPartsId().equals(newParts.getPartsId())) {

                    objectMap.put(detail.getSkuId(), detail);
                }
            }
        }
        childList.add(objectMap);   //零下标

        objectMap = new HashMap<>();
        for (ErpPartsDetail detail : details) {
            for (Long aLong : childrens) {
                if (aLong.equals(detail.getSkuId())) {
                    objectMap.put(detail.getSkuId(), detail);
                }
            }
        }
        childList.add(objectMap);  //一下标

        map.put(skuId, childList);
        return map;
    }
}
