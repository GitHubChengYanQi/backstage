package cn.atsoft.dasheng.task.service;

import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.pojo.AllBomResult;
import cn.atsoft.dasheng.app.pojo.BomOrder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.task.entity.AsynTask;
import cn.atsoft.dasheng.task.model.params.AsynTaskParam;
import cn.atsoft.dasheng.task.model.result.AsynTaskResult;
import cn.atsoft.dasheng.task.pojo.SkuAnalyse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 等待任务表 服务类
 * </p>
 *
 * @author song
 * @since 2022-04-01
 */
public interface AsynTaskService extends IService<AsynTask> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-04-01
     */
    void add(AsynTaskParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-04-01
     */
    void delete(AsynTaskParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-04-01
     */
    void update(AsynTaskParam param);

    List<AsynTaskResult> BomDetailed();

    List<BomOrder> BomDetailedVersion();

    List<ErpPartsDetail> bomResult(Long skuId, int num);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-04-01
     */
    AsynTaskResult findBySpec(AsynTaskParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-04-01
     */
    List<AsynTaskResult> findListBySpec(AsynTaskParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-04-01
     */
    PageInfo<AsynTaskResult> findPageBySpec(AsynTaskParam param);

    Object spectaculars();

    Object spectacularsVersion();

    Object stockSpectaculars();

    List<SkuAnalyse> skuAnalyses(List<Long> skuIds);
}
