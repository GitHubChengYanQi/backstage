package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.app.model.result.ErpPartsDetailResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.model.params.PartsParam;
import cn.atsoft.dasheng.app.model.result.PartsResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 清单 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-21
 */
public interface PartsService extends IService<Parts> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-21
     */
    void add(PartsParam partsParam);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-21
     */
    void delete(PartsParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-21
     */
    void update(PartsParam param);

    void release(Long id);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-21
     */
    PartsResult findBySpec(PartsParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-21
     */
    List<PartsResult> findListBySpec(PartsParam param);

    /**
     * 旧查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-21
     */
    PageInfo<PartsResult> oldFindPageBySpec(PartsParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-21
     */
    PageInfo<PartsResult> findPageBySpec(PartsParam param);


    PartsResult getBOM(Long partId, String type);

    List<ErpPartsDetailResult> backDetails(Long skuId, Long partsId, String type);

    List<ErpPartsDetailResult> oldBackDetails(Long skuId, Long partsId);

    List<PartsResult> getTreeParts(Long partId);
}
