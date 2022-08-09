package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Message;
import cn.atsoft.dasheng.app.model.params.MessageParam;
import cn.atsoft.dasheng.app.model.result.MessageResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 消息提醒 服务类
 * </p>
 *
 * @author 
 * @since 2021-08-03
 */
public interface MessageService extends IService<Message> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-08-03
     */
    void add(MessageParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-08-03
     */
    void delete(MessageParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-08-03
     */
    void update(MessageParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-08-03
     */
    MessageResult findBySpec(MessageParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-08-03
     */
    List<MessageResult> findListBySpec(MessageParam param);

    PageInfo<MessageResult> view(MessageParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-08-03
     */
     PageInfo<MessageResult> findPageBySpec(MessageParam param, DataScope dataScope );

    Integer getViewCount();
}
