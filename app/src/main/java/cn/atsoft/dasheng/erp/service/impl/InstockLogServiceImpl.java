package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InstockList;
import cn.atsoft.dasheng.erp.entity.InstockLog;
import cn.atsoft.dasheng.erp.mapper.InstockLogMapper;
import cn.atsoft.dasheng.erp.model.params.InstockLogParam;
import cn.atsoft.dasheng.erp.model.result.InstockLogDetailResult;
import cn.atsoft.dasheng.erp.model.result.InstockLogResult;
import cn.atsoft.dasheng.erp.service.InstockLogDetailService;
import  cn.atsoft.dasheng.erp.service.InstockLogService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.modular.rest.service.RestUserService;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 入库记录表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-04-14
 */
@Service
public class InstockLogServiceImpl extends ServiceImpl<InstockLogMapper, InstockLog> implements InstockLogService {

    @Autowired
    private InstockLogDetailService instockLogDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestUserService restUserService;

    @Override
    public void add(InstockLogParam param){
        InstockLog entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(InstockLogParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(InstockLogParam param){
        InstockLog oldEntity = getOldEntity(param);
        InstockLog newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InstockLogResult findBySpec(InstockLogParam param){
        return null;
    }

    @Override
    public List<InstockLogResult> findListBySpec(InstockLogParam param){
        return null;
    }

    @Override
    public PageInfo<InstockLogResult> findPageBySpec(InstockLogParam param){
        Page<InstockLogResult> pageContext = getPageContext();
        IPage<InstockLogResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(InstockLogParam param){
        return param.getInstockLogId();
    }

    private Page<InstockLogResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InstockLog getOldEntity(InstockLogParam param) {
        return this.getById(getKey(param));
    }

    private InstockLog getEntity(InstockLogParam param) {
        InstockLog entity = new InstockLog();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
    @Override
    public List<InstockLogResult> listByInstockOrderId(Long id){
        List<InstockLog> instockLogList = this.query().eq("instock_order_id", id).list();
        List<InstockLogResult> results = new ArrayList<>();
        for (InstockLog instockLog : instockLogList) {
            InstockLogResult result = new InstockLogResult();
            ToolUtil.copyProperties(instockLog,result);
            results.add(result);
        }
        this.format(results);
        return  results;
    }
    private void format(List<InstockLogResult> results){
        List<Long> logIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        for (InstockLogResult result : results) {
            logIds.add(result.getInstockLogId());
            userIds.add(result.getCreateUser());
        }
        List<InstockLogDetailResult> instockLogDetailResults =logIds.size() == 0 ? new ArrayList<>() : instockLogDetailService.resultsByLogIds(logIds);
        userIds = userIds.stream().distinct().collect(Collectors.toList());
        List<Map<String, Object>> userResults = new ArrayList<>();
        for (Long userId : userIds) {
            Map<String, Object> userInfo = restUserService.getUserInfo(userId);
            userResults.add(userInfo);
        }

        for (InstockLogResult result : results) {
            for (Map<String, Object> userResult : userResults) {
                if (result.getCreateUser().equals(userResult.get("userId"))){
                    result.setCreateUserResult(userResult);
                    break;
                }
            }
            List<InstockLogDetailResult> detailResults = new ArrayList<>();
            for (InstockLogDetailResult instockLogDetailResult : instockLogDetailResults) {
                if (instockLogDetailResult.getInstockLogId().equals(result.getInstockLogId())){
                    detailResults.add(instockLogDetailResult);
                }
            }
            result.setDetailResults(detailResults);
        }

    }



}
