package cn.atsoft.dasheng.drafts.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.drafts.entity.Drafts;
import cn.atsoft.dasheng.drafts.model.params.DraftsParam;
import cn.atsoft.dasheng.drafts.model.result.DraftsResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 草稿箱 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-04-21
 */
public interface DraftsService extends IService<Drafts> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-04-21
     */
    void add(DraftsParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-04-21
     */
    void delete(DraftsParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-04-21
     */
    void update(DraftsParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-04-21
     */
    DraftsResult findBySpec(DraftsParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-04-21
     */
    List<DraftsResult> findListBySpec(DraftsParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-04-21
     */
     PageInfo<DraftsResult> findPageBySpec(DraftsParam param);

    void format(List<DraftsResult> param);
}
