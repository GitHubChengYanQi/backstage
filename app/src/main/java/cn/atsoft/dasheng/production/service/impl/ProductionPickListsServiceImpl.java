package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionPickLists;
import cn.atsoft.dasheng.production.entity.ProductionTask;
import cn.atsoft.dasheng.production.entity.ProductionTaskDetail;
import cn.atsoft.dasheng.production.mapper.ProductionPickListsMapper;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsParam;
import cn.atsoft.dasheng.production.model.request.SavePickListsObject;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsResult;
import cn.atsoft.dasheng.production.model.result.ProductionTaskResult;
import  cn.atsoft.dasheng.production.service.ProductionPickListsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.production.service.ProductionTaskService;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
 * 领料单 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-25
 */
@Service
public class ProductionPickListsServiceImpl extends ServiceImpl<ProductionPickListsMapper, ProductionPickLists> implements ProductionPickListsService {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductionTaskService productionTaskService;


    @Override
    public void add(ProductionPickListsParam param){
        ProductionPickLists entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProductionPickListsParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionPickListsParam param){
        ProductionPickLists oldEntity = getOldEntity(param);
        ProductionPickLists newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionPickListsResult findBySpec(ProductionPickListsParam param){
        return null;
    }

    @Override
    public List<ProductionPickListsResult> findListBySpec(ProductionPickListsParam param){
        return null;
    }

    @Override
    public PageInfo<ProductionPickListsResult> findPageBySpec(ProductionPickListsParam param){
        Page<ProductionPickListsResult> pageContext = getPageContext();
        IPage<ProductionPickListsResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }
    private void format(List<ProductionPickListsResult> results){
        List<Long> productionTaskIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        for (ProductionPickListsResult result : results) {
            userIds.add(result.getUserId());
            userIds.add(result.getCreateUser());
            productionTaskIds.add(result.getSourceId());
        }

        List<UserResult> userResultsByIds = userService.getUserResultsByIds(userIds);

        List<ProductionTask> productionTasks = productionTaskIds.size() == 0 ? new ArrayList<>() : productionTaskService.listByIds(productionTaskIds);
        List<ProductionTaskResult> productionTaskResults = new ArrayList<>();
        for (ProductionTask productionTask : productionTasks) {
            ProductionTaskResult productionTaskResult = new ProductionTaskResult();
            ToolUtil.copyProperties(productionTask,productionTaskResult);
            productionTaskResults.add(productionTaskResult);
        }
        for (ProductionPickListsResult result : results) {
            for (ProductionTaskResult productionTaskResult : productionTaskResults) {
                if (result.getSource().equals("productionTask") && result.getSourceId().equals(productionTaskResult.getProductionTaskId())){
                    result.setProductionTaskResult(productionTaskResult);
                }
            }

            for (UserResult userResultsById : userResultsByIds) {
                if (result.getUserId().equals(userResultsById.getUserId())){
                    result.setUserResult(userResultsById);
                }
                if (result.getCreateUser().equals(userResultsById.getUserId())){
                    result.setUserResult(userResultsById);
                }
            }
        }
    }

    @Override
    public String addByProductionTask(Object param) {
        SavePickListsObject savePickListsObject = JSON.parseObject(param.toString(), SavePickListsObject.class);
        Long taskId = savePickListsObject.getProductionTask().getProductionTaskId();

        ProductionPickLists productionPickLists = new ProductionPickLists();
        productionPickLists.setStatus(97);
        productionPickLists.setSourceId(taskId);
        productionPickLists.setSource("productionTask");
        productionPickLists.setUserId(savePickListsObject.getProductionTask().getUserId());
        this.save(productionPickLists);

        return null;
    }

    private Serializable getKey(ProductionPickListsParam param){
        return param.getPickListsId();
    }

    private Page<ProductionPickListsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionPickLists getOldEntity(ProductionPickListsParam param) {
        return this.getById(getKey(param));
    }

    private ProductionPickLists getEntity(ProductionPickListsParam param) {
        ProductionPickLists entity = new ProductionPickLists();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
