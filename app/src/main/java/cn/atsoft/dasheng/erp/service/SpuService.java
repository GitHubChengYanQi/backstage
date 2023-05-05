package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.app.model.params.Attribute;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.erp.entity.Spu;
import cn.atsoft.dasheng.erp.model.params.SpuParam;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author
 * @since 2021-10-18
 */
public interface SpuService extends IService<Spu> {

    /**
     * 新增
     *
     * @author
     * @Date 2021-10-18
     */
    Long add(SpuParam param);

    /**
     * 删除
     *
     * @author
     * @Date 2021-10-18
     */
    void delete(SpuParam param);

    /**
     * 更新
     *
     * @author
     * @Date 2021-10-18
     */
    void update(SpuParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author
     * @Date 2021-10-18
     */
    SpuResult findBySpec(SpuParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author
     * @Date 2021-10-18
     */
    List<SpuResult> findListBySpec(SpuParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author
     * @Date 2021-10-18
     */
    PageInfo<SpuResult> findPageBySpec(SpuParam param, DataScope dataScope);

    ResponseData detail (SpuParam spuParam);


    void skuFormat(List<SkuResult> skuResults);
}
