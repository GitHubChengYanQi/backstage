package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.CompetitorQuote;
import cn.atsoft.dasheng.crm.model.params.CompetitorQuoteParam;
import cn.atsoft.dasheng.crm.model.result.CompetitorQuoteResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 竞争对手报价 服务类
 * </p>
 *
 * @author 
 * @since 2021-09-07
 */
public interface CompetitorQuoteService extends IService<CompetitorQuote> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-09-07
     */
    void add(CompetitorQuoteParam param);

    void addTrack (CompetitorQuoteParam param);
    /**
     * 删除
     *
     * @author 
     * @Date 2021-09-07
     */
    void delete(CompetitorQuoteParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-09-07
     */
    void update(CompetitorQuoteParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-09-07
     */
    CompetitorQuoteResult findBySpec(CompetitorQuoteParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-09-07
     */
    List<CompetitorQuoteResult> findListBySpec(CompetitorQuoteParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-09-07
     */
     PageInfo<CompetitorQuoteResult> findPageBySpec(CompetitorQuoteParam param);





}
