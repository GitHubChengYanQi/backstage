package cn.atsoft.dasheng.portal.remind.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.remind.entity.Remind;
import cn.atsoft.dasheng.portal.remind.mapper.RemindMapper;
import cn.atsoft.dasheng.portal.remind.model.params.RemindParam;
import cn.atsoft.dasheng.portal.remind.model.result.RemindResult;
import  cn.atsoft.dasheng.portal.remind.service.RemindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 提醒表 服务实现类
 * </p>
 *
 * @author cheng
 * @since 2021-08-26
 */
@Service
public class RemindServiceImpl extends ServiceImpl<RemindMapper, Remind> implements RemindService {

    @Override
    public void add(RemindParam param){
        Remind entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(RemindParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(RemindParam param){
        Remind oldEntity = getOldEntity(param);
        Remind newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RemindResult findBySpec(RemindParam param){
        return null;
    }

    @Override
    public List<RemindResult> findListBySpec(RemindParam param){
        return null;
    }

    @Override
    public PageInfo<RemindResult> findPageBySpec(RemindParam param){
        Page<RemindResult> pageContext = getPageContext();
        IPage<RemindResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(RemindParam param){
        return param.getRemindId();
    }

    private Page<RemindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Remind getOldEntity(RemindParam param) {
        return this.getById(getKey(param));
    }

    private Remind getEntity(RemindParam param) {
        Remind entity = new Remind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
