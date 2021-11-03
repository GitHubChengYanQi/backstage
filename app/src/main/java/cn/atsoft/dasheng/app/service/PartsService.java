package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.model.params.PartRequest;
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
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-21
     */
    PageInfo<PartsResult> findPageBySpec(PartsParam param);


    List<ErpPartsDetailResult> backDetails(Long skuId,Long partsId);

}
