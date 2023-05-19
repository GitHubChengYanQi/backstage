package cn.atsoft.dasheng.sys.modular.system.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.sys.modular.system.entity.Dept;
import cn.atsoft.dasheng.sys.modular.system.entity.DeptBind;
import cn.atsoft.dasheng.sys.modular.system.mapper.DeptBindMapper;
import cn.atsoft.dasheng.sys.modular.system.model.params.DeptBindParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.DeptBindResult;
import cn.atsoft.dasheng.sys.modular.system.service.DeptBindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.modular.system.service.DeptService;
import cn.hutool.core.bean.BeanUtil;
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
 * 各租户内 部门绑定表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-04
 */
@Service
public class DeptBindServiceImpl extends ServiceImpl<DeptBindMapper, DeptBind> implements DeptBindService {

    @Autowired
    private DeptService deptService;

    @Override
    public void add(DeptBindParam param) {
        DeptBind entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(DeptBindParam param) {
        //this.removeById(getKey(param));
        DeptBind entity = this.getOldEntity(param);
//        entity.setDisplay(0);
        this.updateById(entity);
    }

    @Override
    public void update(DeptBindParam param) {
        DeptBind oldEntity = getOldEntity(param);
        DeptBind newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public DeptBindResult findBySpec(DeptBindParam param) {
        return null;
    }

    @Override
    public List<DeptBindResult> findListBySpec(DeptBindParam param) {
        return null;
    }

    @Override
    public PageInfo<DeptBindResult> findPageBySpec(DeptBindParam param) {
        Page<DeptBindResult> pageContext = getPageContext();
        IPage<DeptBindResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public PageInfo<DeptBindResult> findPageBySpec(DeptBindParam param, DataScope dataScope) {
        Page<DeptBindResult> pageContext = getPageContext();
        IPage<DeptBindResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        return PageFactory.createPageInfo(page);
    }

    /**
     * 序列化List<DeptBindResult>
     *
     * @param dataList
     */
    @Override
    public void format(List<DeptBindResult> dataList) {
        //取出deptIds集合
        List<Long> deptIds = dataList.stream().map(DeptBindResult::getDeptId).distinct().collect(Collectors.toList());
        //根据deptIds查询deptResult
        List<Dept> deptResults = deptIds.size() == 0 ? new ArrayList<>() : deptService.listByIds(deptIds);
        deptResults.add(new Dept(){{
            setDeptId(0L);
                        }});
        //匹配数据
        dataList.forEach(deptBindResult -> {
            deptResults.forEach(deptResult -> {
                if (deptBindResult.getDeptId().equals(deptResult.getDeptId())) {
                    Map<String, Object> stringObjectMap = BeanUtil.beanToMap(deptResult);
                    stringObjectMap.put("mainDept", deptBindResult.getMainDept());
                    deptBindResult.setDept(stringObjectMap);
                }
            });
        });


    }

    private Serializable getKey(DeptBindParam param) {
        return param.getDeptBindId();
    }

    private Page<DeptBindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private DeptBind getOldEntity(DeptBindParam param) {
        return this.getById(getKey(param));
    }

    private DeptBind getEntity(DeptBindParam param) {
        DeptBind entity = new DeptBind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
