package cn.atsoft.dasheng.shop.classpage.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.shop.classpage.entity.Classpojo;
import cn.atsoft.dasheng.shop.classpage.mapper.ClassMapper;
import cn.atsoft.dasheng.shop.classpage.model.params.ClassParam;
import cn.atsoft.dasheng.shop.classpage.model.result.ClassResult;
import  cn.atsoft.dasheng.shop.classpage.service.ClassService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 一级分类导航 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-08-19
 */
@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Classpojo> implements ClassService {

    @Override
    public void add(ClassParam param){
        Classpojo entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ClassParam param){
        param.setDisplay(0);
        this.update(param);
//        this.removeById(getKey(param));
    }

    @Override
    public void update(ClassParam param){
        Classpojo oldEntity = getOldEntity(param);
        Classpojo newEntity = getEntity(param);
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

    private Classpojo getOldEntity(ClassParam param) {
        return this.getById(getKey(param));
    }

    private Classpojo getEntity(ClassParam param) {
        Classpojo entity = new Classpojo();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
