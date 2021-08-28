package cn.atsoft.dasheng.shop.classPage.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.bannerDifference.entity.BannerDifference;
import cn.atsoft.dasheng.portal.bannerDifference.model.result.BannerDifferenceResult;
import cn.atsoft.dasheng.portal.bannerDifference.service.BannerDifferenceService;
import cn.atsoft.dasheng.shop.classPage.entity.Classpojo;
import cn.atsoft.dasheng.shop.classPage.mapper.ClassMapper;
import cn.atsoft.dasheng.shop.classPage.model.params.ClassParam;
import cn.atsoft.dasheng.shop.classPage.model.result.ClassResult;
import cn.atsoft.dasheng.shop.classPage.service.ClassService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * 一级分类导航 服务实现类
 * </p>
 *
 * @author
 * @since 2021-08-19
 */
@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Classpojo> implements ClassService {
    @Autowired
//    private ClassDifferenceService service;
    private BannerDifferenceService bannerDifferenceService;

    @Override
    public void add(ClassParam param) {
        Classpojo entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ClassParam param) {
        param.setDisplay(0);
        this.update(param);
//        this.removeById(getKey(param));
    }

    @Override
    public void update(ClassParam param) {
        Classpojo oldEntity = getOldEntity(param);
        Classpojo newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ClassResult findBySpec(ClassParam param) {
        return null;
    }

    @Override
    public List<ClassResult> findListBySpec(ClassParam param) {
        return null;
    }

    @Override
    public PageInfo<ClassResult> findPageBySpec(ClassParam param) {
        Page<ClassResult> pageContext = getPageContext();
        IPage<ClassResult> page = this.baseMapper.customPageList(pageContext, param);

        return PageFactory.createPageInfo(page);
    }

    @Override
    public void batchDelete(List<Long> ids) {
        Classpojo classpojo = new Classpojo();
        classpojo.setDisplay(0);
        QueryWrapper<Classpojo> classpojoQueryWrapper = new QueryWrapper<>();
        classpojoQueryWrapper.in("class_id", ids);
        this.update(classpojo, classpojoQueryWrapper);
    }


    private Serializable getKey(ClassParam param) {
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
   public List format (){
        ClassParam classParam = new ClassParam();
       List<ClassResult> classResults = this.baseMapper.customList(classParam);
       List<Long>ids = new ArrayList<>();
       for (ClassResult classResult : classResults) {
           ids.add(classResult.getClassificationId());
       }
       QueryWrapper<BannerDifference> queryWrapper = new QueryWrapper<>();
       queryWrapper.in("classification_id",ids);
       List<BannerDifference> differenceList = bannerDifferenceService.list(queryWrapper);
       for (ClassResult classResult : classResults) {
           for (BannerDifference bannerDifference : differenceList) {
               if ( classResult.getClassificationId()!=null && classResult.getClassificationId().equals(bannerDifference.getClassificationId())) {
                   BannerDifferenceResult bannerDifferenceResult = new BannerDifferenceResult();
                   ToolUtil.copyProperties(bannerDifference,bannerDifferenceResult);
                   classResult.setBannerDifferenceResult(bannerDifferenceResult);
                   break;
               }
           }
       }return  classResults ;

   }

}
