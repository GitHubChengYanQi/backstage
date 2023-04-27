package cn.atsoft.dasheng.miniapp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.miniapp.entity.WxMaConfig;
import cn.atsoft.dasheng.miniapp.model.params.WxMaConfigParam;
import cn.atsoft.dasheng.miniapp.model.result.WxMaConfigResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 微信小程序配置表（对应租户） 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-04-25
 */
public interface WxMaConfigService extends IService<WxMaConfig> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2023-04-25
     */
    void add(WxMaConfigParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2023-04-25
     */
    void delete(WxMaConfigParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2023-04-25
     */
    void update(WxMaConfigParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-04-25
     */
    WxMaConfigResult findBySpec(WxMaConfigParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-04-25
     */
    List<WxMaConfigResult> findListBySpec(WxMaConfigParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-04-25
     */
     PageInfo<WxMaConfigResult> findPageBySpec(WxMaConfigParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-04-25
     */
     PageInfo<WxMaConfigResult> findPageBySpec(WxMaConfigParam param,DataScope dataScope);

    void format(List<WxMaConfigResult> dataList);
}
