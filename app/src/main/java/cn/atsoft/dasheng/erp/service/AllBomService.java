package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.pojo.AllBom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface AllBomService {

    Map<Long, ArrayList<Map<Long, Object>>> getBom(Long skuId, List<Parts> parts, List<ErpPartsDetail> details);
}
