package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Adress;
import cn.atsoft.dasheng.app.model.params.AdressParam;
import cn.atsoft.dasheng.app.model.result.AdressResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 客户地址表 服务类
 * </p>
 *
 * @author ta
 * @since 2021-07-19
 */
public interface AdressService extends IService<Adress> {

    /**
     * 新增
     *
     * @author ta
     * @Date 2021-07-19
     */
    void add(AdressParam param);

    /**
     * 删除
     *
     * @author ta
     * @Date 2021-07-19
     */
    void delete(AdressParam param);

    /**
     * 更新
     *
     * @author ta
     * @Date 2021-07-19
     */
    void update(AdressParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author ta
     * @Date 2021-07-19
     */
    AdressResult findBySpec(AdressParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author ta
     * @Date 2021-07-19
     */
    List<AdressResult> findListBySpec(AdressParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author ta
     * @Date 2021-07-19
     */
     PageInfo<AdressResult> findPageBySpec(AdressParam param);

}