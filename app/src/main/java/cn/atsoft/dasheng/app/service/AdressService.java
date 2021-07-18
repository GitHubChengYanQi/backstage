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
 * @author 
 * @since 2021-07-16
 */
public interface AdressService extends IService<Adress> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-07-16
     */
    void add(AdressParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-07-16
     */
    void delete(AdressParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-07-16
     */
    void update(AdressParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-07-16
     */
    AdressResult findBySpec(AdressParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-07-16
     */
    List<AdressResult> findListBySpec(AdressParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-07-16
     */
     PageInfo<AdressResult> findPageBySpec(AdressParam param);

}
