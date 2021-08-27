package cn.atsoft.dasheng.shop.classDifferenceDetail.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.shop.classDifferenceDetail.entity.ClassDifferenceDetails;
import cn.atsoft.dasheng.shop.classDifferenceDetail.mapper.ClassDifferenceDetailsMapper;
import cn.atsoft.dasheng.shop.classDifferenceDetail.model.params.ClassDifferenceDetailsParam;
import cn.atsoft.dasheng.shop.classDifferenceDetail.model.result.ClassDifferenceDetailsResult;
import  cn.atsoft.dasheng.shop.classDifferenceDetail.service.ClassDifferenceDetailsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 分类明细内容 服务实现类
 * </p>
 *
 * @author siqiang
 * @since 2021-08-18
 */
@Service
public class ClassDifferenceDetailsServiceImpl extends ServiceImpl<ClassDifferenceDetailsMapper, ClassDifferenceDetails> implements ClassDifferenceDetailsService {

    @Override
    public void add(ClassDifferenceDetailsParam param){
        ClassDifferenceDetails entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ClassDifferenceDetailsParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ClassDifferenceDetailsParam param){
        ClassDifferenceDetails oldEntity = getOldEntity(param);
        ClassDifferenceDetails newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ClassDifferenceDetailsResult findBySpec(ClassDifferenceDetailsParam param){
        return null;
    }

    @Override
    public List<ClassDifferenceDetailsResult> findListBySpec(ClassDifferenceDetailsParam param){
        return null;
    }

    @Override
    public PageInfo<ClassDifferenceDetailsResult> findPageBySpec(ClassDifferenceDetailsParam param){
        Page<ClassDifferenceDetailsResult> pageContext = getPageContext();
        IPage<ClassDifferenceDetailsResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ClassDifferenceDetailsParam param){
        return param.getDetailId();
    }

    private Page<ClassDifferenceDetailsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ClassDifferenceDetails getOldEntity(ClassDifferenceDetailsParam param) {
        return this.getById(getKey(param));
    }

    private ClassDifferenceDetails getEntity(ClassDifferenceDetailsParam param) {
        ClassDifferenceDetails entity = new ClassDifferenceDetails();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
