package cn.atsoft.dasheng.view.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.view.entity.TableView;
import cn.atsoft.dasheng.view.mapper.TableViewMapper;
import cn.atsoft.dasheng.view.model.params.TableViewParam;
import cn.atsoft.dasheng.view.model.result.TableViewResult;
import cn.atsoft.dasheng.view.service.TableViewService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author
 * @since 2021-11-04
 */
@Service
public class TableViewServiceImpl extends ServiceImpl<TableViewMapper, TableView> implements TableViewService {

    @Override
    public TableView add(TableViewParam param) {

        Integer count = this.query().in("table_key", param.getTableKey()).eq("name", param.getName()).eq("display", 1).count();
        if (count > 0) {
            throw new ServiceException(500, "视图名称重复,请更换");
        }

        TableView entity = getEntity(param);
        this.save(entity);
        return entity;
    }

    @Override
    public void delete(TableViewParam param) {
        param.setDisplay(0);
        this.update(param);
    }

    @Override
    public void update(TableViewParam param) {
        TableView oldEntity = getOldEntity(param);
        TableView newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public TableViewResult findBySpec(TableViewParam param) {
        return null;
    }

    @Override
    public List<TableViewResult> findListBySpec(TableViewParam param) {
        return null;
    }

    @Override
    public PageInfo<TableViewResult> findPageBySpec(TableViewParam param) {
        Page<TableViewResult> pageContext = getPageContext();
        IPage<TableViewResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(TableViewParam param) {
        return param.getTableViewId();
    }

    private Page<TableViewResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private TableView getOldEntity(TableViewParam param) {
        return this.getById(getKey(param));
    }

    private TableView getEntity(TableViewParam param) {
        TableView entity = new TableView();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
