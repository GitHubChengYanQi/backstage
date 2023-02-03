package cn.atsoft.dasheng.goods.classz.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.classz.entity.RestAttribute;
import cn.atsoft.dasheng.goods.classz.entity.RestAttributeValues;
import cn.atsoft.dasheng.goods.classz.entity.RestClass;
import cn.atsoft.dasheng.goods.classz.mapper.RestClassMapper;
import cn.atsoft.dasheng.goods.classz.model.params.RestAttributeParam;
import cn.atsoft.dasheng.goods.classz.model.params.RestAttributeValuesParam;
import cn.atsoft.dasheng.goods.classz.model.params.RestClassParam;
import cn.atsoft.dasheng.goods.classz.model.result.RestClassResult;
import cn.atsoft.dasheng.goods.classz.service.RestAttributeService;
import cn.atsoft.dasheng.goods.classz.service.RestAttributeValuesService;
import cn.atsoft.dasheng.goods.classz.service.RestClassService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物品分类表 服务实现类
 * </p>
 *
 * @author jazz
 * @since 2021-10-18
 */
@Service
public class RestClassServiceImpl extends ServiceImpl<RestClassMapper, RestClass> implements RestClassService {

    @Override
    public Long add(RestClassParam param) {
        RestClass entity = this.getOne(new QueryWrapper<RestClass>() {{
            eq("category_name", param.getName());
            eq("display", 1);
        }});
        if (ToolUtil.isEmpty(entity)){
            entity = getEntity(param);
            this.save(entity);
        }
        // 更新当前节点，及下级
        RestClass category = new RestClass();
        Map<String, List<Long>> childrenMap = getChildrens(entity.getPid());
        category.setChildrens(JSON.toJSONString(childrenMap.get("childrens")));
        category.setChildren(JSON.toJSONString(childrenMap.get("children")));
        QueryWrapper<RestClass> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("category_id", entity.getPid());
        this.update(category, QueryWrapper);

        updateChildren(entity.getPid());

        return entity.getCategoryId();
    }

    /**
     * 递归
     */
    public Map<String, List<Long>> getChildrens(Long id) {

        List<Long> childrensSkuIds = new ArrayList<>();
        Map<String, List<Long>> result = new HashMap<String, List<Long>>() {
            {
                put("children", new ArrayList<>());
                put("childrens", new ArrayList<>());
            }
        };

        List<Long> skuIds = new ArrayList<>();
        RestClass category = this.query().eq("category_id", id).eq("display", 1).one();
        if (ToolUtil.isNotEmpty(category)) {
            List<RestClass> details = this.query().eq("pid", category.getCategoryId()).eq("display", 1).list();
            for (RestClass detail : details) {
                skuIds.add(detail.getCategoryId());
                childrensSkuIds.add(detail.getCategoryId());
                Map<String, List<Long>> childrenMap = this.getChildrens(detail.getCategoryId());
                childrensSkuIds.addAll(childrenMap.get("childrens"));
            }
            result.put("children", skuIds);
            result.put("childrens", childrensSkuIds);
        }
        return result;
    }

    /**
     * 更新包含它的
     */
    public void updateChildren(Long id) {
        List<RestClass> categories = this.query().like("children", id).eq("display", 1).list();
        for (RestClass category : categories) {
            Map<String, List<Long>> childrenMap = getChildrens(id);
            JSONArray childrensjsonArray = JSONUtil.parseArray(category.getChildrens());
            List<Long> longs = JSONUtil.toList(childrensjsonArray, Long.class);
            List<Long> list = childrenMap.get("childrens");
            for (Long aLong : list) {
                longs.add(aLong);
            }
            category.setChildrens(JSON.toJSONString(longs));
            // update
            QueryWrapper<RestClass> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("category_id", category.getCategoryId());
            this.update(category, queryWrapper);
            updateChildren(category.getCategoryId());
        }
    }

    @Override
    @Transactional
    public void delete(RestClassParam param) {
        RestClass category = new RestClass();
        category.setDisplay(0);
        QueryWrapper<RestClass> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.in("category_id", param.getClassId());
        this.update(category, categoryQueryWrapper);
    }

    @Override
    @Transactional
    public void update(RestClassParam param) {
        //如果设为顶级 修改所有当前节点的父级
        if (param.getPid() == 0) {
            List<RestClass> categories = this.query().like("childrens", param.getClassId()).list();
            for (RestClass category : categories) {
                JSONArray jsonArray = JSONUtil.parseArray(category.getChildrens());
                JSONArray childrenJson = JSONUtil.parseArray(category.getChildren());
                List<Long> oldchildrenList = JSONUtil.toList(childrenJson, Long.class);
                List<Long> newChildrenList = new ArrayList<>();
                List<Long> longs = JSONUtil.toList(jsonArray, Long.class);
                longs.remove(param.getClassId());
                for (Long aLong : oldchildrenList) {
                    if (!aLong.equals(param.getClassId())) {
                        newChildrenList.add(aLong);
                    }
                }
                category.setChildren(JSON.toJSONString(newChildrenList));
                category.setChildrens(JSON.toJSONString(longs));
                this.update(category, new QueryWrapper<RestClass>().in("category_id", category.getCategoryId()));
            }

        }
        //防止循环添加
        if (ToolUtil.isNotEmpty(param.getPid())) {
            List<RestClass> categories = this.query().in("category_id", param.getClassId()).eq("display", 1).list();
            for (RestClass category : categories) {
                JSONArray jsonArray = JSONUtil.parseArray(category.getChildrens());
                List<Long> longs = JSONUtil.toList(jsonArray, Long.class);
                for (Long aLong : longs) {
                    if (param.getPid().equals(aLong)) {
                        throw new ServiceException(500, "请勿循环添加");
                    }
                }
            }
        }

        RestClass category = this.getById(param.getClassId());
        if (!category.getCategoryName().equals(param.getName())) {
            Integer count = this.query().in("display", 1).in("category_name", param.getClassId()).count();
            if (count > 0) {
                throw new ServiceException(500, "名字已重复");
            }
        }

        // 更新当前节点，及下级
        RestClass newCategory = new RestClass();
        Map<String, List<Long>> childrenMap = getChildrens(param.getPid());
        List<Long> childrens = childrenMap.get("childrens");
        childrens.add(param.getClassId());
        newCategory.setChildrens(JSON.toJSONString(childrens));
        List<Long> children = childrenMap.get("children");
        children.add(param.getClassId());
        newCategory.setChildren(JSON.toJSONString(children));
        QueryWrapper<RestClass> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("category_id", param.getPid());
        this.update(newCategory, QueryWrapper);

        updateChildren(param.getPid());

        RestClass oldEntity = getOldEntity(param);
        RestClass newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RestClassResult findBySpec(RestClassParam param) {
        return null;
    }

    @Override
    public List<RestClassResult> findListBySpec(RestClassParam param) {
        return null;
    }

    @Override
    public PageInfo<RestClassResult> findPageBySpec(RestClassParam param) {
        Page<RestClassResult> pageContext = getPageContext();
        IPage<RestClassResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(RestClassParam param) {
        return param.getClassId();
    }

    private Page<RestClassResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private RestClass getOldEntity(RestClassParam param) {
        return this.getById(getKey(param));
    }

    private RestClass getEntity(RestClassParam param) {
        RestClass entity = new RestClass();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<RestClassResult> data) {
        List<Long> pids = new ArrayList<>();
        for (RestClassResult datum : data) {
            pids.add(datum.getPid());
        }
        if (ToolUtil.isNotEmpty(pids)) {
            List<RestClass> categories = pids.size() == 0 ? new ArrayList<>() :
                    this.lambdaQuery().in(RestClass::getCategoryId, pids).in(RestClass::getDisplay, 1).list();
            for (RestClassResult datum : data) {
                if (ToolUtil.isNotEmpty(categories)) {
                    for (RestClass category : categories) {
                        if (datum.getPid() != null && datum.getPid().equals(category.getCategoryId())) {
                            RestClassResult categoryResult = new RestClassResult();
                            ToolUtil.copyProperties(category, categoryResult);
                            datum.setPidCategoryResult(categoryResult);
                            break;
                        }
                    }
                }
            }
        }
    }


}
