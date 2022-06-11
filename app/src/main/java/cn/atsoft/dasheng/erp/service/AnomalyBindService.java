package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AnomalyBind;
import cn.atsoft.dasheng.erp.model.params.AnomalyBindParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyBindResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 异常生成的实物 绑定 服务类
 * </p>
 *
 * @author song
 * @since 2022-05-28
 */
public interface AnomalyBindService extends IService<AnomalyBind> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-05-28
     */
    void add(AnomalyBindParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-05-28
     */
    void delete(AnomalyBindParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-05-28
     */
    void update(AnomalyBindParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-05-28
     */
    AnomalyBindResult findBySpec(AnomalyBindParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-05-28
     */
    List<AnomalyBindResult> findListBySpec(AnomalyBindParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-05-28
     */
     PageInfo<AnomalyBindResult> findPageBySpec(AnomalyBindParam param);

}
