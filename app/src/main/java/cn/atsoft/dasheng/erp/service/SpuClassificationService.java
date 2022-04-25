package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.SpuClassification;
import cn.atsoft.dasheng.erp.model.params.SpuClassificationParam;
import cn.atsoft.dasheng.erp.model.result.SpuClassificationResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * SPU分类 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-25
 */
public interface SpuClassificationService extends IService<SpuClassification> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-25
     */
    Long add(SpuClassificationParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-25
     */
    void delete(SpuClassificationParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-25
     */
    void update(SpuClassificationParam param);

    List<String> getClassName();

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-25
     */
    SpuClassificationResult findBySpec(SpuClassificationParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-25
     */
    List<SpuClassificationResult> findListBySpec(SpuClassificationParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-25
     */
     PageInfo<SpuClassificationResult> findPageBySpec(SpuClassificationParam param);

}
