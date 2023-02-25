package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Bom;
import cn.atsoft.dasheng.erp.model.params.BomParam;
import cn.atsoft.dasheng.erp.model.result.BomResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BomService extends IService<Bom> {
    /**
     * 新增
     */
    void add(BomParam bomParam);


    /**
     * 删除
     */
    void delete(BomParam bomParam);

    /**
     * 更新
     */
    void update(BomParam bomParam);

    /**
     * 查询单条数据，Specification模式
     *
     */
    BomResult findBySpec(BomParam bomParam);

    /**
     * 查询列表，Specification模式
     *
     */
    List<BomResult> findListBySpec(BomParam bomParam);

    /**
     * 查询分页数据，Specification模式
     *
     */
    PageInfo<BomResult> findPageBySpec(BomParam bomParam);

    Integer countBySkuIdAndVersion(Long skuId, String version);

}
