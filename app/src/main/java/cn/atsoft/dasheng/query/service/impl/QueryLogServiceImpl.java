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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
        // 判断重复
        QueryWrapper<QueryLog> queryLogQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(param.getFormType()) && ToolUtil.isNotEmpty(param.getRecord())) {
            queryLogQueryWrapper.eq("record", param.getRecord());
            queryLogQueryWrapper.eq("form_type", param.getFormType());
            queryLogQueryWrapper.last("limit 1");
            queryLogQueryWrapper.eq("display", 1);
            QueryLog queryLog = this.getOne(queryLogQueryWrapper);
            if (ToolUtil.isNotEmpty(queryLog)) {
                queryLog.setDisplay(0);
                this.updateById(queryLog);
            }
            QueryLog entity = getEntity(param);
            this.save(entity);
        }


    }

    @Override
    public void deleteBatch(QueryLogParam param) {
        QueryLog log = new QueryLog();
        log.setDisplay(0);
        QueryWrapper<QueryLog> logQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(param.getFormType())) {
            logQueryWrapper.eq("form_type", param.getFormType());
        }
        Long userId = LoginContextHolder.getContext().getUserId();
        if (ToolUtil.isNotEmpty(userId)) {
            logQueryWrapper.eq("create_user", userId);
        }
        this.update(log, logQueryWrapper);
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
