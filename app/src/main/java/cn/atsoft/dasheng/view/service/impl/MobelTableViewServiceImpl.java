package cn.atsoft.dasheng.view.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Tool;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.controller.LoginController;
import cn.atsoft.dasheng.view.entity.MobelTableView;
import cn.atsoft.dasheng.view.mapper.MobelTableViewMapper;
import cn.atsoft.dasheng.view.model.params.MobelTableViewParam;
import cn.atsoft.dasheng.view.model.result.MobelTableViewResult;
import cn.atsoft.dasheng.view.service.MobelTableViewService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 移动端菜单 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-05-09
 */
@Service
public class MobelTableViewServiceImpl extends ServiceImpl<MobelTableViewMapper, MobelTableView> implements MobelTableViewService {

    @Override
    public void add(MobelTableViewParam param) {
        Long userId = LoginContextHolder.getContext().getUserId();
        List<MobelTableView> views = this.query().eq("user_id", userId).eq("display", 1).eq("type",param.getType()).list();
        if (ToolUtil.isNotEmpty(views)) {
            for (MobelTableView view : views) {
                view.setDisplay(0);
            }
            this.updateBatchById(views);
        }
        MobelTableView entity = getEntity(param);
        if (ToolUtil.isNotEmpty(param.getDetails())) {
            entity.setField(JSON.toJSONString(param.getDetails()));
        }

        Integer count = this.query().in("table_key", param.getTableKey()).eq("display",1).eq("name", param.getName()).eq("user_id", userId).count();
        if (count > 0) {
            throw new ServiceException(500, "视图名称重复,请更换");
        }
        entity.setUserId(userId);

        this.save(entity);


    }

    @Override
    public void delete(MobelTableViewParam param) {
//        this.removeById(getKey(param));
    }

    @Override
    public void update(MobelTableViewParam param) {
        MobelTableView oldEntity = getOldEntity(param);
        MobelTableView newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        newEntity.setDisplay(null);
        this.updateById(newEntity);
    }

    @Override
    public MobelTableViewResult findBySpec(MobelTableViewParam param) {
        return null;
    }

    @Override
    public List<MobelTableViewResult> findListBySpec(MobelTableViewParam param) {
        return null;
    }

    @Override
    public PageInfo<MobelTableViewResult> findPageBySpec(MobelTableViewParam param) {
        Page<MobelTableViewResult> pageContext = getPageContext();
        IPage<MobelTableViewResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(MobelTableViewParam param) {
        return param.getMobelTableViewId();
    }

    private Page<MobelTableViewResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private MobelTableView getOldEntity(MobelTableViewParam param) {
        return this.getById(getKey(param));
    }

    private MobelTableView getEntity(MobelTableViewParam param) {
        MobelTableView entity = new MobelTableView();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
