package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Inkind;
import cn.atsoft.dasheng.erp.model.params.InkindParam;
import cn.atsoft.dasheng.erp.model.result.InkindResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 实物表 服务类
 * </p>
 *
 * @author song
 * @since 2021-11-01
 */
public interface InkindService extends IService<Inkind> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-11-01
     */
    Long add(InkindParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-11-01
     */
    void delete(InkindParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-11-01
     */
    void update(InkindParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-11-01
     */
    InkindResult findBySpec(InkindParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-11-01
     */
    List<InkindResult> findListBySpec(InkindParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-11-01
     */
    PageInfo<InkindResult> findPageBySpec(InkindParam param);


    void updateAnomalyInKind(List<Long> inKindIds);

    InkindResult backInKindgetById(Long id);


    List<InkindResult> getInKinds(List<Long> inKindIds);


    List<InkindResult> details(InkindParam param);

    InkindResult inkindDetail(InkindParam param);

    InkindResult getInkindResult(Long id);
}
