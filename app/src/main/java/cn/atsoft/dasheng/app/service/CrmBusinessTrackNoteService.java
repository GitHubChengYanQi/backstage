package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmBusinessTrackNote;
import cn.atsoft.dasheng.app.model.params.CrmBusinessTrackNoteParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessTrackNoteResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商机跟踪备注 服务类
 * </p>
 *
 * @author 
 * @since 2021-08-04
 */
public interface CrmBusinessTrackNoteService extends IService<CrmBusinessTrackNote> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-08-04
     */
    void add(CrmBusinessTrackNoteParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-08-04
     */
    void delete(CrmBusinessTrackNoteParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-08-04
     */
    void update(CrmBusinessTrackNoteParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-08-04
     */
    CrmBusinessTrackNoteResult findBySpec(CrmBusinessTrackNoteParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-08-04
     */
    List<CrmBusinessTrackNoteResult> findListBySpec(CrmBusinessTrackNoteParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-08-04
     */
     PageInfo<CrmBusinessTrackNoteResult> findPageBySpec(CrmBusinessTrackNoteParam param);

}
