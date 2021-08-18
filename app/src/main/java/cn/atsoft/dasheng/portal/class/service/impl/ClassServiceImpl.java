package cn.atsoft.dasheng.portal.class.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.class.entity.Class;
import cn.atsoft.dasheng.portal.class.mapper.ClassMapper;
import cn.atsoft.dasheng.portal.class.model.params.ClassParam;
import cn.atsoft.dasheng.portal.class.model.result.ClassResult;
import  cn.atsoft.dasheng.portal.class.service.ClassService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 分类导航 服务实现类
 * </p>
 *
 * @author siqiang
 * @since 2021-08-18
 */
@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {

    @Override
    public void add(ClassParam param){
        Class entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ClassParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ClassParam param){
        Class oldEntity = getOldEntity(param);
        Class newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ClassResult findBySpec(ClassParam param){
        return null;
    }

    @Override
    public List<ClassResult> findListBySpec(ClassParam param){
        return null;
    }

    @Override
    public PageInfo<ClassResult> findPageBySpec(ClassParam param){
        Page<ClassResult> pageContext = getPageContext();
        IPage<ClassResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ClassParam param){
        return param.getClassId();
    }

    private Page<ClassResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Class getOldEntity(ClassParam param) {
        return this.getById(getKey(param));
    }

    private Class getEntity(ClassParam param) {
        Class entity = new Class();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
