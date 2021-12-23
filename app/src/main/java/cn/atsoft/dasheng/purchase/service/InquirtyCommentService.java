package cn.atsoft.dasheng.purchase.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.InquirtyComment;
import cn.atsoft.dasheng.purchase.model.params.InquirtyCommentParam;
import cn.atsoft.dasheng.purchase.model.result.InquirtyCommentResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-23
 */
public interface InquirtyCommentService extends IService<InquirtyComment> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    void add(InquirtyCommentParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    void delete(InquirtyCommentParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    void update(InquirtyCommentParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    InquirtyCommentResult findBySpec(InquirtyCommentParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    List<InquirtyCommentResult> findListBySpec(InquirtyCommentParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
     PageInfo<InquirtyCommentResult> findPageBySpec(InquirtyCommentParam param);

    List<InquirtyCommentResult> getComment(Long id);
}
