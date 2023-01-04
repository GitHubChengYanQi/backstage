package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.Sop;
import cn.atsoft.dasheng.production.model.params.SopParam;
import cn.atsoft.dasheng.production.model.result.SopResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * sop 主表 服务类
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
public interface SopService extends IService<Sop> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-02-10
     */
    Long add(SopParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-02-10
     */
    void delete(SopParam param);

    void sopdeatilIdsBysopId(SopParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-02-10
     */
    void update(SopParam param);

    SopResult detail(Long sopId);

    List<String> getImgUrls(List<Long> ids);

    void addShip(Long sopId, Long shipId);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-02-10
     */
    SopResult findBySpec(SopParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-02-10
     */
    List<SopResult> findListBySpec(SopParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-02-10
     */
     PageInfo<SopResult> findPageBySpec(SopParam param);

    void format(List<SopResult> data);
}
