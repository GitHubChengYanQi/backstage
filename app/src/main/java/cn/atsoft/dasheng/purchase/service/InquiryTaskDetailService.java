package cn.atsoft.dasheng.purchase.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.InquiryTaskDetail;
import cn.atsoft.dasheng.purchase.model.params.InquiryTaskDetailParam;
import cn.atsoft.dasheng.purchase.model.result.InquiryTaskDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 询价任务详情 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-23
 */
public interface InquiryTaskDetailService extends IService<InquiryTaskDetail> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    void add(InquiryTaskDetailParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    void delete(InquiryTaskDetailParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    void update(InquiryTaskDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    InquiryTaskDetailResult findBySpec(InquiryTaskDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    List<InquiryTaskDetailResult> findListBySpec(InquiryTaskDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
     PageInfo<InquiryTaskDetailResult> findPageBySpec(InquiryTaskDetailParam param);

     List<InquiryTaskDetailResult> getDetails (Long taskId);

    List<InquiryTaskDetailResult> getDetailByInquiryId(Long inQuiruId);

    List<Long> getSku(Long taskId);
}
