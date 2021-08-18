package cn.atsoft.dasheng.portal.navigation.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.navigation.entity.Navigation;
import cn.atsoft.dasheng.portal.navigation.mapper.NavigationMapper;
import cn.atsoft.dasheng.portal.navigation.model.params.NavigationParam;
import cn.atsoft.dasheng.portal.navigation.model.result.NavigationResult;
import cn.atsoft.dasheng.portal.navigation.service.NavigationService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.navigationdifference.entity.NavigationDifference;
import cn.atsoft.dasheng.portal.navigationdifference.model.result.NavigationDifferenceResult;
import cn.atsoft.dasheng.portal.navigationdifference.service.NavigationDifferenceService;
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
 * 导航表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-08-18
 */
@Service
public class NavigationServiceImpl extends ServiceImpl<NavigationMapper, Navigation> implements NavigationService {
    @Autowired
    private NavigationDifferenceService navigationDifferenceService;

    @Override
    public void add(NavigationParam param) {
        Navigation entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(NavigationParam param) {
        param.setDisplay(0);
        this.update(param);
//        this.removeById(getKey(param));
    }

    @Override
    public void update(NavigationParam param) {
        Navigation oldEntity = getOldEntity(param);
        Navigation newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public NavigationResult findBySpec(NavigationParam param) {
        return null;
    }

    @Override
    public List<NavigationResult> findListBySpec(NavigationParam param) {
        return null;
    }

    @Override
    public PageInfo<NavigationResult> findPageBySpec(NavigationParam param) {
        Page<NavigationResult> pageContext = getPageContext();
        IPage<NavigationResult> page = this.baseMapper.customPageList(pageContext, param);
        List<Long> Ids = new ArrayList<>();
        for (NavigationResult record : page.getRecords()) {
            Ids.add(record.getDifference());
        }
        QueryWrapper<NavigationDifference> differenceQueryWrapper = new QueryWrapper<>();
        differenceQueryWrapper.in("classification_id", Ids);
        List<NavigationDifference> list = Ids.size() == 0 ? new ArrayList<>() : navigationDifferenceService.list(differenceQueryWrapper);
        for (NavigationResult record : page.getRecords()) {
            for (NavigationDifference navigationDifference : list) {
                if (record.getDifference().equals(navigationDifference.getClassificationId())) {
                    NavigationDifferenceResult differenceResult = new NavigationDifferenceResult();
                    ToolUtil.copyProperties(navigationDifference, differenceResult);
                    record.setDifferenceResult(differenceResult);
                    break;
                }
            }
        }
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(NavigationParam param) {
        return param.getNavigationId();
    }

    private Page<NavigationResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Navigation getOldEntity(NavigationParam param) {
        return this.getById(getKey(param));
    }

    private Navigation getEntity(NavigationParam param) {
        Navigation entity = new Navigation();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
