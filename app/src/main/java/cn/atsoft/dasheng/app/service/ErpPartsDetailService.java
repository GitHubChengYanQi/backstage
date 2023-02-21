package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.model.params.ErpPartsDetailParam;
import cn.atsoft.dasheng.app.model.result.ErpPartsDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 清单详情 服务类
 * </p>
 *
 * @author cheng
 * @since 2021-10-26
 */
public interface ErpPartsDetailService extends IService<ErpPartsDetail> {

    /**
     * 新增
     *
     * @author cheng
     * @Date 2021-10-26
     */
    void add(ErpPartsDetailParam param);

    /**
     * 删除
     *
     * @author cheng
     * @Date 2021-10-26
     */
    void delete(ErpPartsDetailParam param);

    /**
     * 更新
     *
     * @author cheng
     * @Date 2021-10-26
     */
    void update(ErpPartsDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author cheng
     * @Date 2021-10-26
     */
    ErpPartsDetailResult findBySpec(ErpPartsDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author cheng
     * @Date 2021-10-26
     */
    List<ErpPartsDetailResult> findListBySpec(ErpPartsDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author cheng
     * @Date 2021-10-26
     */
     PageInfo findPageBySpec(ErpPartsDetailParam param);

    List<ErpPartsDetailResult> bomList(ErpPartsDetailParam param);

    List<ErpPartsDetailResult> bomListVersion(ErpPartsDetailParam param);

    List<ErpPartsDetailResult> recursiveDetails(Long skuId, ErpPartsDetailResult result);

    List<ErpPartsDetailResult> getDetails(List<Long> partIds);

    List<ErpPartsDetail> listByPartIds(List<Long> ids);

    void format(List<ErpPartsDetailResult> data);
}
