package cn.atsoft.dasheng.shop.classdifference.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.shop.classdifference.entity.ClassDifference;
import cn.atsoft.dasheng.shop.classdifference.mapper.ClassDifferenceMapper;
import cn.atsoft.dasheng.shop.classdifference.model.params.ClassDifferenceParam;
import cn.atsoft.dasheng.shop.classdifference.model.result.ClassDifferenceResult;
import  cn.atsoft.dasheng.shop.classdifference.service.ClassDifferenceService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 分类明细 服务实现类
 * </p>
 *
 * @author siqiang
 * @since 2021-08-18
 */
@Service
public class ClassDifferenceServiceImpl extends ServiceImpl<ClassDifferenceMapper, ClassDifference> implements ClassDifferenceService {

    @Override
    public void add(ClassDifferenceParam param){
        ClassDifference entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ClassDifferenceParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ClassDifferenceParam param){
        ClassDifference oldEntity = getOldEntity(param);
        ClassDifference newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ClassDifferenceResult findBySpec(ClassDifferenceParam param){
        return null;
    }

    @Override
    public List<ClassDifferenceResult> findListBySpec(ClassDifferenceParam param){
        return null;
    }

    @Override
    public PageInfo<ClassDifferenceResult> findPageBySpec(ClassDifferenceParam param){
        Page<ClassDifferenceResult> pageContext = getPageContext();
        IPage<ClassDifferenceResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ClassDifferenceParam param){
        return param.getClassDifferenceId();
    }

    private Page<ClassDifferenceResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ClassDifference getOldEntity(ClassDifferenceParam param) {
        return this.getById(getKey(param));
    }

    private ClassDifference getEntity(ClassDifferenceParam param) {
        ClassDifference entity = new ClassDifference();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
