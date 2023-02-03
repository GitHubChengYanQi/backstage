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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        updateChildren(entity.getCategoryId());
        return entity.getCategoryId();
    }
    public List<Long> getChildrens(Long id) {

        List<RestClass> restClassList = this.query().like("pid", id).eq("display", 1).list();

        List<Long> childrenList = new ArrayList<>();
        for (RestClass restClass : restClassList) {
            childrenList.add(restClass.getCategoryId());
            childrenList.addAll(getChildrens(restClass.getCategoryId()));
        }
        return childrenList;
    }


    /**
     * 更新包含它的
     */
    public void updateChildren(Long id) {
        RestClass restClass = this.getById(id);
        if(ToolUtil.isEmpty(restClass)) return;

        List<RestClass> restClassList = this.query().like("pid", id).eq("display", 1).list();

        List<Long> children = new ArrayList<>();
        children.addAll(restClassList.stream().map(RestClass::getCategoryId).collect(Collectors.toList()));

        List<Long> childrens = new ArrayList<Long>(){{
            add(restClass.getCategoryId());
        }};
        childrens.addAll(getChildrens(id));

        restClass.setChildren(StringUtils.join(children,","));
        restClass.setChildrens(StringUtils.join(childrens,","));
        this.updateById(restClass);
        updateChildren(restClass.getPid());
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
