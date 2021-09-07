package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.BusinessCompetition;
import cn.atsoft.dasheng.crm.model.params.BusinessCompetitionParam;
import cn.atsoft.dasheng.crm.model.params.CompetitorParam;
import cn.atsoft.dasheng.crm.model.result.BusinessCompetitionResult;
import cn.atsoft.dasheng.crm.model.result.CompetitorResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商机 竞争对手 绑定 服务类
 * </p>
 *
 * @author
 * @since 2021-09-07
 */
public interface BusinessCompetitionService extends IService<BusinessCompetition> {

    /**
     * 新增
     *
     * @author
     * @Date 2021-09-07
     */
    void add(BusinessCompetitionParam param);

    /**
     * 删除
     *
     * @author
     * @Date 2021-09-07
     */
    void delete(BusinessCompetitionParam param);

    /**
     * 更新
     *
     * @author
     * @Date 2021-09-07
     */
    void update(BusinessCompetitionParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author
     * @Date 2021-09-07
     */
    BusinessCompetitionResult findBySpec(BusinessCompetitionParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author
     * @Date 2021-09-07
     */
    List<BusinessCompetitionResult> findListBySpec(BusinessCompetitionParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author
     * @Date 2021-09-07
     */
    PageInfo<BusinessCompetitionResult> findPageBySpec(BusinessCompetitionParam param);

    List<CompetitorResult> findComptitor(BusinessCompetitionParam param);

}
