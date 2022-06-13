package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Announcements;
import cn.atsoft.dasheng.erp.model.params.AnnouncementsParam;
import cn.atsoft.dasheng.erp.model.result.AnnouncementsResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 注意事项 服务类
 * </p>
 *
 * @author song
 * @since 2022-05-27
 */
public interface AnnouncementsService extends IService<Announcements> {

    String toJson(List<Long> ids);

    /**
     * 新增
     *
     * @author song
     * @Date 2022-05-27
     */
    Announcements add(AnnouncementsParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-05-27
     */
    void delete(AnnouncementsParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-05-27
     */
    void update(AnnouncementsParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-05-27
     */
    AnnouncementsResult findBySpec(AnnouncementsParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-05-27
     */
    List<AnnouncementsResult> findListBySpec(AnnouncementsParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-05-27
     */
     PageInfo<AnnouncementsResult> findPageBySpec(AnnouncementsParam param);

}
