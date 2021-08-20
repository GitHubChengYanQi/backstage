package cn.atsoft.dasheng.shop.classdifference.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.shop.classdifference.entity.ClassDifference;
import cn.atsoft.dasheng.shop.classdifference.mapper.ClassDifferenceMapper;
import cn.atsoft.dasheng.shop.classdifference.model.params.ClassDifferenceParam;
import cn.atsoft.dasheng.shop.classdifference.model.result.ClassDifferenceResult;
import cn.atsoft.dasheng.shop.classdifference.service.ClassDifferenceService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.shop.classdifferencedetail.entity.ClassDifferenceDetails;
import cn.atsoft.dasheng.shop.classdifferencedetail.model.result.ClassDifferenceDetailsResult;
import cn.atsoft.dasheng.shop.classdifferencedetail.service.ClassDifferenceDetailsService;
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
 * 分类明细 服务实现类
 * </p>
 *
 * @author siqiang
 * @since 2021-08-18
 */
@Service
public class ClassDifferenceServiceImpl extends ServiceImpl<ClassDifferenceMapper, ClassDifference> implements ClassDifferenceService {
    @Autowired
    private ClassDifferenceDetailsService service;

    @Override
    public void add(ClassDifferenceParam param) {
        ClassDifference entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ClassDifferenceParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(ClassDifferenceParam param) {
        ClassDifference oldEntity = getOldEntity(param);
        ClassDifference newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ClassDifferenceResult findBySpec(ClassDifferenceParam param) {
        return null;
    }

    @Override
    public List<ClassDifferenceResult> findListBySpec(ClassDifferenceParam param) {
        return null;
    }

    @Override
    public PageInfo<ClassDifferenceResult> findPageBySpec(ClassDifferenceParam param) {
        Page<ClassDifferenceResult> pageContext = getPageContext();
        IPage<ClassDifferenceResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public List<ClassDifferenceResult> getByIds(List<Long> ids) {
        QueryWrapper<ClassDifference> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("class_id",ids);
        List<ClassDifference> list = this.list(queryWrapper);
        List<ClassDifferenceResult> results = new ArrayList<>();
        for (ClassDifference classDifference : list) {
            for (Long id : ids) {
                if (classDifference.getClassId().equals(id)){
                    ClassDifferenceResult classDifferenceResult = new ClassDifferenceResult();
                    ToolUtil.copyProperties(classDifference,classDifferenceResult);
                    results.add(classDifferenceResult);
                }
            }

        }
        this.getdeta(results);
        return results;
    }

    @Override
    public List<ClassDifferenceResult> getdetalis(Long ids) {
        List<Long> dIds = new ArrayList<>();
        ClassDifferenceParam classDifferenceParam = new ClassDifferenceParam();
        classDifferenceParam.setClassId(ids);
        List<ClassDifferenceResult> list = this.baseMapper.customList(classDifferenceParam);
        for (ClassDifferenceResult classDifference : list) {
            Long classDifferenceId = classDifference.getClassDifferenceId();
            dIds.add(classDifferenceId);
        }
        QueryWrapper<ClassDifferenceDetails> detailsQueryWrapper = new QueryWrapper<>();
        detailsQueryWrapper.in("class_difference_id",dIds);
        List<ClassDifferenceDetails> details = dIds.size() == 0 ? new ArrayList<>() : service.list(detailsQueryWrapper);
        for (ClassDifferenceResult classDifference : list) {
            List<ClassDifferenceDetailsResult> results = new ArrayList();
            for (ClassDifferenceDetails detail : details) {
                if (detail.getClassDifferenceId().equals(classDifference.getClassDifferenceId())) {
                    ClassDifferenceDetailsResult classDifferenceDetailsResult = new ClassDifferenceDetailsResult();
                    ToolUtil.copyProperties(detail, classDifferenceDetailsResult);
                    results.add(classDifferenceDetailsResult);
                }

            }

            classDifference.setList(results);
        }



        return list;
    }


    public void getdeta(List<ClassDifferenceResult> data ) {
        List<Long> ids = new ArrayList<>();
        for (ClassDifferenceResult datum : data) {
            ids.add(datum.getClassDifferenceId());
        }
        QueryWrapper<ClassDifferenceDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("class_difference_id", ids);
        List<ClassDifferenceDetails> detailsList = service.list(queryWrapper);
        for (ClassDifferenceResult datum : data) {

            List<ClassDifferenceDetailsResult> list = new ArrayList<>();

            for (ClassDifferenceDetails classDifferenceDetails : detailsList) {
                if (datum.getClassDifferenceId().equals(classDifferenceDetails.getClassDifferenceId())) {
                    ClassDifferenceDetailsResult classDifferenceResult = new ClassDifferenceDetailsResult();
                    ToolUtil.copyProperties(classDifferenceDetails,classDifferenceResult);
                    list.add(classDifferenceResult);
                }
            }   datum.setList(list);
        }

    }

    private Serializable getKey(ClassDifferenceParam param) {
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
