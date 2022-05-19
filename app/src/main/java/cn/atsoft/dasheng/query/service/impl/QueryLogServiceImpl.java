package cn.atsoft.dasheng.query.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.query.entity.QueryLog;
import cn.atsoft.dasheng.query.mapper.QueryLogMapper;
import cn.atsoft.dasheng.query.model.params.QueryLogParam;
import cn.atsoft.dasheng.query.model.result.QueryLogResult;
import cn.atsoft.dasheng.query.service.QueryLogService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 搜索查询记录 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-05-19
 */
@Service
public class QueryLogServiceImpl extends ServiceImpl<QueryLogMapper, QueryLog> implements QueryLogService {

    @Override
    public void add(QueryLogParam param) {
        QueryLog entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(QueryLogParam param) {
        QueryLog entity = getEntity(param);
        entity.setDisplay(0);
        this.updateById(entity);
    }

    @Override
    public void update(QueryLogParam param) {
        QueryLog oldEntity = getOldEntity(param);
        QueryLog newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public QueryLogResult findBySpec(QueryLogParam param) {
        return null;
    }

    @Override
    public List<QueryLogResult> findListBySpec(QueryLogParam param) {
        return null;
    }

    @Override
    public PageInfo<QueryLogResult> findPageBySpec(QueryLogParam param) {
        Long userId = LoginContextHolder.getContext().getUserId();
        param.setCreateUser(userId);
        Page<QueryLogResult> pageContext = getPageContext();
        IPage<QueryLogResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(QueryLogParam param) {
        return param.getQueryLogId();
    }

    private Page<QueryLogResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private QueryLog getOldEntity(QueryLogParam param) {
        return this.getById(getKey(param));
    }

    private QueryLog getEntity(QueryLogParam param) {
        QueryLog entity = new QueryLog();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
