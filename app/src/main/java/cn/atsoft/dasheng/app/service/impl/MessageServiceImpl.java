package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Message;
import cn.atsoft.dasheng.app.mapper.MessageMapper;
import cn.atsoft.dasheng.app.model.params.MessageParam;
import cn.atsoft.dasheng.app.model.result.MessageResult;
import cn.atsoft.dasheng.app.service.MessageService;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 消息提醒 服务实现类
 * </p>
 *
 * @author
 * @since 2021-08-03
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Override
    public void add(MessageParam param) {
        Message entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(MessageParam param) {
        param.setDisplay(0);
        this.updateById(getEntity(param));
    }

    @Override
    public void update(MessageParam param) {
        Message oldEntity = getOldEntity(param);
        Message newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public MessageResult findBySpec(MessageParam param) {
        return null;
    }

    @Override
    public List<MessageResult> findListBySpec(MessageParam param) {
        return null;
    }

    @Override
    public PageInfo<MessageResult> findPageBySpec(MessageParam param, DataScope dataScope) {
        if (ToolUtil.isNotEmpty(param.getCreateTime())) {
            DateTime date = DateUtil.date(param.getCreateTime());
            param.setCreateTime(date);
        }
        Page<MessageResult> pageContext = getPageContext();
        IPage<MessageResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(MessageParam param) {
        return param.getMessageId();
    }

    private Page<MessageResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Message getOldEntity(MessageParam param) {
        return this.getById(getKey(param));
    }

    private Message getEntity(MessageParam param) {
        Message entity = new Message();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
