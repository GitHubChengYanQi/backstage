package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.RestFormStyle;
import cn.atsoft.dasheng.form.mapper.RestFormStyleMapper;
import cn.atsoft.dasheng.form.model.params.RestFormStyleParam;
import cn.atsoft.dasheng.form.model.result.RestFormStyleResult;
import cn.atsoft.dasheng.form.service.RestFormStyleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 表单风格 服务实现类
 * </p>
 *
 * @author
 * @since 2022-09-23
 */
@Service
public class RestFormStyleServiceImpl extends ServiceImpl<RestFormStyleMapper, RestFormStyle> implements RestFormStyleService {

    @Override
    public void add(RestFormStyleParam param) {
        RestFormStyle entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(RestFormStyleParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(RestFormStyleParam param) {

        RestFormStyle detail = this.lambdaQuery().eq(RestFormStyle::getFormType, param.getFormType()).eq(RestFormStyle::getDisplay, 1).orderByDesc(RestFormStyle::getCreateTime).last(" limit 1").one();

        if (ToolUtil.isEmpty(detail)) {
            this.add(param);
            return;
        }
        RestFormStyleParam formStyleParam = new RestFormStyleParam();
        ToolUtil.copyProperties(detail, formStyleParam);
        RestFormStyle newEntity = getEntity(param);
        newEntity.setStyleId(detail.getStyleId());
        this.updateById(newEntity);
    }

    @Override
    public RestFormStyleResult findBySpec(RestFormStyleParam param) {
        return null;
    }

    @Override
    public List<RestFormStyleResult> findListBySpec(RestFormStyleParam param) {
        return null;
    }

    @Override
    public PageInfo<RestFormStyleResult> findPageBySpec(RestFormStyleParam param) {
        Page<RestFormStyleResult> pageContext = getPageContext();
        IPage<RestFormStyleResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(RestFormStyleParam param) {
        return param.getStyleId();
    }

    private Page<RestFormStyleResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private RestFormStyle getOldEntity(RestFormStyleParam param) {
        return this.getById(getKey(param));
    }

    private RestFormStyle getEntity(RestFormStyleParam param) {
        RestFormStyle entity = new RestFormStyle();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
