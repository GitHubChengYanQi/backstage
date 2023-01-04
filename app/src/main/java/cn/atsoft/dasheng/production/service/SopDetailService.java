package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.SopDetail;
import cn.atsoft.dasheng.production.model.params.SopDetailParam;
import cn.atsoft.dasheng.production.model.result.SopDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * sop 详情 服务类
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
public interface SopDetailService extends IService<SopDetail> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-02-10
     */
    void add(SopDetailParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-02-10
     */
    void delete(SopDetailParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-02-10
     */
    void update(SopDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-02-10
     */
    SopDetailResult findBySpec(SopDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-02-10
     */
    List<SopDetailResult> findListBySpec(SopDetailParam param);

    List<SopDetailResult> getResultBySopId(Long sopId);

    List<SopDetail> getSopdetailIdbyResult(Long sopId);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-02-10
     */
     PageInfo<SopDetailResult> findPageBySpec(SopDetailParam param);

}
