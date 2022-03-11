package cn.atsoft.dasheng.app.pojo;

import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.app.service.ErpPartsDetailService;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.erp.service.AllBomService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Service
public class AllBom {
    @Autowired
    private AllBomService allBomService;
    @Autowired
    private PartsService partsService;
    @Autowired
    private ErpPartsDetailService detailService;

    private Map<Long, ArrayList<Map<Long, Object>>> Bom;

    /**
     * 1=>[
     * {2=>{},3=>{},4=>{}},
     * {5=>{},6=>{},7=>{}}
     * ]
     */
    public void getBom(Long skuId) {

        List<Parts> parts = partsService.list();
        List<ErpPartsDetail> partsDetails = detailService.list();
        Bom = allBomService.getBom(skuId, parts, partsDetails);
    }
}
