package cn.atsoft.dasheng.miniapp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.miniapp.entity.WxMaMessage;
import cn.atsoft.dasheng.miniapp.model.params.WxMaMessageParam;
import cn.atsoft.dasheng.miniapp.model.result.WxMaMessageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 小程序订阅消息 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-11
 */
public interface WxMaMessageService extends IService<WxMaMessage> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2023-05-11
     */
    void add(WxMaMessageParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2023-05-11
     */
    void delete(WxMaMessageParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2023-05-11
     */
    void update(WxMaMessageParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-11
     */
    WxMaMessageResult findBySpec(WxMaMessageParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-11
     */
    List<WxMaMessageResult> findListBySpec(WxMaMessageParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-11
     */
     PageInfo<WxMaMessageResult> findPageBySpec(WxMaMessageParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-11
     */
     PageInfo<WxMaMessageResult> findPageBySpec(WxMaMessageParam param,DataScope dataScope);

}
