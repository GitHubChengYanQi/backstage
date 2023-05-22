package cn.atsoft.dasheng.bom.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.bom.entity.RestBom;
import cn.atsoft.dasheng.bom.model.params.RestBomDetailParam;
import cn.atsoft.dasheng.bom.model.params.RestBomParam;
import cn.atsoft.dasheng.bom.model.result.RestBomResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RestBomService extends IService<RestBom> {
    /**
     * 新增
     */
    void add(RestBomParam bomParam);


    void cycleJudge(Long skuId, List<RestBom> bomList);

    /**
     * 删除
     */
    void delete(RestBomParam bomParam);

    /**
     * 更新
     */
    void update(RestBomParam bomParam);

    /**
     * 查询单条数据，Specification模式
     *
     */
    RestBomResult findBySpec(RestBomParam bomParam);

    /**
     * 查询列表，Specification模式
     *
     */
    List<RestBomResult> findListBySpec(RestBomParam bomParam);

    /**
     * 查询分页数据，Specification模式
     *
     */
    PageInfo<RestBomResult> findPageBySpec(RestBomParam bomParam, DataScope dataScope);

    void format(List<RestBomResult> dataList);

    Integer countBySkuIdAndVersion(Long skuId, String version);

    List<RestBom> getBySkuIds(List<Long> skuIds);

    List<RestBom> getBySkuAndVersion(List<RestBomDetailParam> params);

    List<RestBomResult> getByBomId(Long bomId, Integer number);
}
