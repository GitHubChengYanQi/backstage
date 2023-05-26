package cn.atsoft.dasheng.storehouse.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.category.model.result.RestCategoryResult;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.storehouse.entity.RestStorehouse;
import cn.atsoft.dasheng.storehouse.entity.StorehouseSpuClassBind;
import cn.atsoft.dasheng.storehouse.mapper.RestStorehouseMapper;
import cn.atsoft.dasheng.storehouse.model.params.RestStorehouseParam;
import cn.atsoft.dasheng.storehouse.model.result.RestStorehouseResult;
import cn.atsoft.dasheng.storehouse.model.result.StorehouseSpuClassBindResult;
import cn.atsoft.dasheng.storehouse.service.RestStorehouseService;
import cn.atsoft.dasheng.storehouse.service.StorehouseSpuClassBindService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 地点表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-07-15
 */
@Service
public class RestStorehouseServiceImpl extends ServiceImpl<RestStorehouseMapper, RestStorehouse> implements RestStorehouseService {

    @Autowired
    private StorehouseSpuClassBindService storehouseSpuClassBindService;

    @Override
    @Transactional
    public Long add(RestStorehouseParam param) {
        RestStorehouse entity = getEntity(param);
        this.save(entity);
        //更新children
        // 查找父仓库
        RestStorehouse parentWarehouse = this.lambdaQuery()
                .eq(RestStorehouse::getStorehouseId, param.getPid())
                .one();
        if (parentWarehouse != null) {
            // 更新父仓库的 children 字段
            parentWarehouse.setChildren(parentWarehouse.getChildren() + "," + entity.getStorehouseId());
            this.updateById(parentWarehouse);
        }
        // 查找所有祖先仓库
        List<RestStorehouse> allAncestorWarehouses = this.lambdaQuery()
                .like(RestStorehouse::getChildrens, entity.getStorehouseId().toString())
                .list();
        // 遍历所有祖先仓库
        for (RestStorehouse ancestorWarehouse : allAncestorWarehouses) {
            // 更新祖先仓库的 childrens 字段
            ancestorWarehouse.setChildrens(ancestorWarehouse.getChildrens() + "," + entity.getStorehouseId());
        }
        // 批量更新所有祖先仓库的信息
        if(!allAncestorWarehouses.isEmpty()){
            this.updateBatchById(allAncestorWarehouses);
        }
        return entity.getStorehouseId();
    }

    @Override
    public void delete(RestStorehouseParam param) {
        param.setDisplay(0);
        this.updateById(getEntity(param));
    }

    @Override
    public void update(RestStorehouseParam param) {
        RestStorehouse oldEntity = getOldEntity(param);
        RestStorehouse newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }
    @Override
    @Transactional
    public void updateRestStorehouse(RestStorehouseParam restStorehouse) {
        RestStorehouse oldRestStorehouse = this.getById(restStorehouse.getStorehouseId());
        if (oldRestStorehouse == null) {
            throw new ServiceException(500,"仓库未找到");
        }
        RestStorehouse newEntity = getEntity(restStorehouse);

        ToolUtil.copyProperties(newEntity, oldRestStorehouse);
        this.updateById(oldRestStorehouse);
        // 如果新旧父仓库的 ID 不同
        if (!oldRestStorehouse.getPid().equals(restStorehouse.getPid())) {
            // 获取旧的父仓库信息
            RestStorehouse oldParentRestStorehouse = this.lambdaQuery()
                    .eq(RestStorehouse::getStorehouseId, oldRestStorehouse.getPid())
                    .one();
            if (oldParentRestStorehouse != null) {
                // 如果旧的父仓库存在，从旧的父仓库的 children 字段中移除当前仓库的 ID
                List<String> oldChildrenIds = Arrays.stream(oldParentRestStorehouse.getChildren().split(","))
                        .filter(id -> !id.equals(String.valueOf(oldRestStorehouse.getStorehouseId()))) 
                        .collect(Collectors.toList());
                oldParentRestStorehouse.setChildren(String.join(",", oldChildrenIds));
                this.updateById(oldParentRestStorehouse);
            }
            // 获取新的父仓库信息
            RestStorehouse newParentRestStorehouse = this.lambdaQuery()
                    .eq(RestStorehouse::getStorehouseId, restStorehouse.getPid())
                    .one();
            if (newParentRestStorehouse != null) {
                // 如果新的父仓库存在，将当前仓库的 ID 添加到新的父仓库的 children 字段中
                newParentRestStorehouse.setChildren(newParentRestStorehouse.getChildren() + "," + restStorehouse.getStorehouseId());
                this.updateById(newParentRestStorehouse);
            }

            // 获取所有的老祖先仓库
            List<RestStorehouse> allOldAncestorRestStorehouses = this.lambdaQuery()
                    .like(RestStorehouse::getChildrens, oldRestStorehouse.getStorehouseId().toString())
                    .list();
            for (RestStorehouse ancestorRestStorehouse : allOldAncestorRestStorehouses) {
                List<String> oldChildrensIds = Arrays.stream(ancestorRestStorehouse.getChildrens().split(","))
                        .filter(id -> !id.equals(String.valueOf(oldRestStorehouse.getStorehouseId())))
                        .collect(Collectors.toList());
                ancestorRestStorehouse.setChildrens(String.join(",", oldChildrensIds));
                this.updateById(ancestorRestStorehouse);
            }

            List<RestStorehouse> allNewAncestorRestStorehouses = this.lambdaQuery()
                    .like(RestStorehouse::getChildrens, restStorehouse.getStorehouseId().toString())
                    .list();
            for (RestStorehouse ancestorRestStorehouse : allNewAncestorRestStorehouses) {
                ancestorRestStorehouse.setChildrens(ancestorRestStorehouse.getChildrens() + "," + restStorehouse.getStorehouseId());
                this.updateById(ancestorRestStorehouse);
            }
        }
    }
    @Override
    public RestStorehouseResult findBySpec(RestStorehouseParam param) {
        return null;
    }

    @Override
    public List<RestStorehouseResult> findListBySpec(RestStorehouseParam param) {
        return null;
    }

    @Override
    public PageInfo<RestStorehouseResult> findPageBySpec(RestStorehouseParam param, DataScope dataScope) {
        Page<RestStorehouseResult> pageContext = getPageContext();
        IPage<RestStorehouseResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public RestStorehouseResult getDetail(Long Id) {
        RestStorehouse storehouse = this.getById(Id);
        if (ToolUtil.isEmpty(storehouse)) {
            return null;
        }
        RestStorehouseResult storehouseResult = new RestStorehouseResult();
        ToolUtil.copyProperties(storehouse, storehouseResult);
        return storehouseResult;
    }



    private Serializable getKey(RestStorehouseParam param) {
        return param.getStorehouseId();
    }

    private Page<RestStorehouseResult> getPageContext() {
        List<String> fields = new ArrayList<String>() {{
            add("itemId");
            add("brandId");
            add("name");
            add("capacity");

        }};
        return PageFactory.defaultPage(fields);
    }

    private RestStorehouse getOldEntity(RestStorehouseParam param) {
        return this.getById(getKey(param));
    }

    private RestStorehouse getEntity(RestStorehouseParam param) {
        RestStorehouse entity = new RestStorehouse();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public List<RestStorehouseResult> getDetails(List<Long> ids) {
        if (ToolUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        List<RestStorehouse> storehouses = this.listByIds(ids);
        return BeanUtil.copyToList(storehouses, RestStorehouseResult.class, new CopyOptions());
    }
    @Override
    @Transactional
    public void sort(List<RestStorehouseParam.Sort> param){


        //校验sortList中的参数 防止空指针
        for (RestStorehouseParam.Sort restDept : param) {
            if (ToolUtil.isOneEmpty(restDept,restDept.getStorehouseId(),restDept.getSort())){
                throw new ServiceException(500,"参数错误");
            }
        }
        //验证sortParam中的id在数据库中是否存在
        List<Long> storehouseIds = param.stream().map(RestStorehouseParam.Sort::getStorehouseId).collect(Collectors.toList());
        List<RestStorehouse> storehouse = this.listByIds(storehouseIds);
        if (storehouse.size() != param.size()){
            throw new ServiceException(500,"参数错误");
        }
        List<RestStorehouse> updateEntity = BeanUtil.copyToList(param, RestStorehouse.class);

        this.updateBatchById(updateEntity);

    }

    private RestStorehouseResult buildTree(RestStorehouseResult parent, List<RestStorehouseResult> restStorehouses) {
        for (RestStorehouseResult restStorehouse : restStorehouses) {
            if (restStorehouse.getPid().equals(parent.getStorehouseId())) {
                if (parent.getChildrenList() == null) {
                    parent.setChildrenList(new ArrayList<>());
                }
                parent.getChildrenList().add(buildTree(restStorehouse, restStorehouses));
            }
        }
        return parent;
    }
    @Override
    public List<RestStorehouseResult> getRestStorehouseTree() {
        List<RestStorehouse> restStorehouses = this.list(new QueryWrapper<RestStorehouse>(){{
            eq("tenant_id", LoginContextHolder.getContext().getTenantId());
            eq("display",1);
        }});
        List<RestStorehouseResult> restStorehouseResults = BeanUtil.copyToList(restStorehouses, RestStorehouseResult.class);
        restStorehouseResults.sort(Comparator.comparing(RestStorehouseResult::getSort).reversed());
        List<Long> storehouseIds = restStorehouseResults.stream().map(RestStorehouseResult::getStorehouseId).collect(Collectors.toList());

        List<StorehouseSpuClassBind> storehouseSpuClassBinds = storehouseSpuClassBindService.lambdaQuery().in(StorehouseSpuClassBind::getStorehouseId, storehouseIds).eq(StorehouseSpuClassBind::getDisplay, 1).list();
        List<StorehouseSpuClassBindResult> storehouseSpuClassBindResults = BeanUtil.copyToList(storehouseSpuClassBinds, StorehouseSpuClassBindResult.class);
        storehouseSpuClassBindService.format(storehouseSpuClassBindResults);

        for (RestStorehouseResult restStorehouseResult : restStorehouseResults) {
            List<RestCategoryResult> categoryResults = new ArrayList<>();
            for (StorehouseSpuClassBindResult storehouseSpuClassBindResult : storehouseSpuClassBindResults) {
                if (restStorehouseResult.getStorehouseId().equals(storehouseSpuClassBindResult.getStorehouseId())) {
                    categoryResults.add(storehouseSpuClassBindResult.getCategoryResult());
                }
            }
            restStorehouseResult.setSpuClassResults(categoryResults);
        }

        // 获取所有仓库
        List<RestStorehouseResult> roots = restStorehouseResults.stream()
                .filter(restStorehouse -> restStorehouse.getPid() == null || restStorehouse.getPid() == 0) // 找到所有根节点
                .collect(Collectors.toList());
        for (RestStorehouseResult root : roots) {
            buildTree(root, restStorehouseResults);
        }
        return roots;
    }
}
