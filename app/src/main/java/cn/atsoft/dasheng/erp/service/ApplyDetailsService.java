package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ApplyDetails;
import cn.atsoft.dasheng.erp.model.params.ApplyDetailsParam;
import cn.atsoft.dasheng.erp.model.result.ApplyDetailsResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author song
 * @since 2021-09-15
 */
public interface ApplyDetailsService extends IService<ApplyDetails> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-09-15
     */
    void add(ApplyDetailsParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-09-15
     */
    void delete(ApplyDetailsParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-09-15
     */
    void update(ApplyDetailsParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-09-15
     */
    ApplyDetailsResult findBySpec(ApplyDetailsParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-09-15
     */
    List<ApplyDetailsResult> findListBySpec(ApplyDetailsParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-09-15
     */
     PageInfo<ApplyDetailsResult> findPageBySpec(ApplyDetailsParam param);

}
