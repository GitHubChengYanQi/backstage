package cn.atsoft.dasheng.dynamic.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.dynamic.model.result.DynamicResult;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.dynamic.entity.Dynamic;
import cn.atsoft.dasheng.dynamic.mapper.DynamicMapper;
import cn.atsoft.dasheng.dynamic.model.params.DynamicParam;
import cn.atsoft.dasheng.dynamic.service.DynamicService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-08-10
 */
@Service
public class DynamicServiceImpl extends ServiceImpl<DynamicMapper, Dynamic> implements DynamicService {
    @Autowired
    private UserService userService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private ActivitiProcessTaskService taskService;


    @Override
    public void add(DynamicParam param) {
        Dynamic entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(DynamicParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(DynamicParam param) {
        Dynamic oldEntity = getOldEntity(param);
        Dynamic newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public DynamicResult findBySpec(DynamicParam param) {
        return null;
    }

    @Override
    public List<DynamicResult> findListBySpec(DynamicParam param) {
        return null;
    }


    @Override
    public PageInfo<DynamicResult> findPageBySpec(DynamicParam param) {
        Page<DynamicResult> pageContext = getPageContext();
        IPage<DynamicResult> page = this.baseMapper.customPageList(pageContext, param);

        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private void format(List<DynamicResult> data) {
        List<Long> userIds = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        List<Long> taskIds = new ArrayList<>();
        for (DynamicResult datum : data) {
            userIds.add(datum.getUserId());
            if (ToolUtil.isNotEmpty(datum.getSkuId())) {
                skuIds.add(datum.getSkuId());
            }
            if (ToolUtil.isNotEmpty(datum.getTaskId())) {
                taskIds.add(datum.getTaskId());
            }
        }
        List<SkuSimpleResult> skuResult = skuService.simpleFormatSkuResult(skuIds);

        List<ActivitiProcessTaskResult> taskResults = taskService.resultsByIds(taskIds);


        List<UserResult> userResultsByIds = userService.getUserResultsByIds(userIds);
        for (DynamicResult datum : data) {
            for (UserResult userResultsById : userResultsByIds) {
                if (userResultsById.getUserId().equals(datum.getUserId())) {
                    datum.setUserResult(userResultsById);
                    break;
                }
            }
            for (SkuSimpleResult skuSimpleResult : skuResult) {
                if (ToolUtil.isNotEmpty(datum.getSkuId()) && datum.getSkuId().equals(skuSimpleResult.getSkuId())) {
                    datum.setSkuResult(skuSimpleResult);
                    break;
                }

            }
            for (ActivitiProcessTaskResult taskResult : taskResults) {
                if (ToolUtil.isNotEmpty(datum.getTaskId()) && datum.getTaskId().equals(taskResult.getProcessTaskId())) {
                    datum.setTaskResult(taskResult);
                    break;
                }
            }
        }


    }

    private Serializable getKey(DynamicParam param) {
        return param.getDynamicId();
    }

    private Page<DynamicResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Dynamic getOldEntity(DynamicParam param) {
        return this.getById(getKey(param));
    }

    private Dynamic getEntity(DynamicParam param) {
        Dynamic entity = new Dynamic();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
