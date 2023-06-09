package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.app.model.request.InStockByBom;
import cn.atsoft.dasheng.app.model.result.ErpPartsDetailResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.model.params.PartsParam;
import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.transaction.Transactional;
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
    Parts add(PartsParam partsParam);


    Parts newAdd(PartsParam partsParam);

    void startAnalyse();

    void updateAdd(PartsParam partsParam);

    List<PartsResult> getParent(Long skuId);

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
    PageInfo oldFindPageBySpec(PartsParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-21
     */
    PageInfo findPageBySpec(PartsParam param, DataScope dataScope);

    /**
     *
     * 查询分页数据，通过skuId找到boms
     */
    PageInfo findPageBySkuId(Long skuId);


    List<Long> getSkuIdsByBom(Long skuId);

    PartsResult getBOM(Long partId);

    List<ErpPartsDetailResult> recursiveDetails(Long partId);

    List<ErpPartsDetailResult> backDetails(Long skuId, Long partsId, String type);

    List<ErpPartsDetailResult> oldBackDetails(Long skuId, Long partsId);

    List<PartsResult> getTreeParts(Long partId);

    List<PartsResult> getdetails(List<Long> partIds);

    void format(List<PartsResult> data);

    List<InStockByBom> getByBomId(Long bomId, Integer number);



    List<PartsResult> listByBomId(Long bomId, Long cartId);
}
