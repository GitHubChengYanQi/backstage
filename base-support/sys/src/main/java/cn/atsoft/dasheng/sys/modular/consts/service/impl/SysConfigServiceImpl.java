package cn.atsoft.dasheng.sys.modular.consts.service.impl;

import cn.atsoft.dasheng.sys.core.exception.enums.BizExceptionEnum;
import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.base.pojo.page.LayuiPageFactory;
import cn.atsoft.dasheng.base.pojo.page.LayuiPageInfo;
import cn.atsoft.dasheng.sys.modular.consts.entity.SysConfig;
import cn.atsoft.dasheng.sys.modular.consts.mapper.SysConfigMapper;
import cn.atsoft.dasheng.sys.modular.consts.model.params.SysConfigParam;
import cn.atsoft.dasheng.sys.modular.consts.model.result.SysConfigResult;
import cn.atsoft.dasheng.sys.modular.consts.service.SysConfigService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

import static cn.atsoft.dasheng.base.consts.ConfigConstant.SYSTEM_CONSTANT_PREFIX;

/**
 * <p>
 * 参数配置 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-06-20
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysConfigParam param) {
        SysConfig entity = getEntity(param);

        //判断编码有没有重复
        SysConfig sysConfig = new SysConfig();
        sysConfig.setCode(entity.getCode());
        List<SysConfig> list = this.list(new QueryWrapper<>(sysConfig));
        if (list != null && list.size() > 0) {
            throw new ServiceException(BizExceptionEnum.ALREADY_CONSTANTS_ERROR);
        }

        //如果是字典类型
        if (ToolUtil.isNotEmpty(param.getDictFlag())
                && param.getDictFlag().equalsIgnoreCase("Y")) {
            entity.setValue(param.getDictValue());
        }

        this.save(entity);

        //添加字典context
        ConstantsContext.putConstant(entity.getCode(), entity.getValue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SysConfigParam param) {

        //不能删除系统常量
        SysConfig sysConfig = this.getById(param.getId());
        if (sysConfig != null && sysConfig.getCode().startsWith(SYSTEM_CONSTANT_PREFIX)) {
            throw new ServiceException(BizExceptionEnum.SYSTEM_CONSTANT_ERROR);
        }

        this.removeById(getKey(param));

        //删除字典context
        ConstantsContext.deleteConstant(sysConfig.getCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysConfigParam param) {
        SysConfig oldEntity = getOldEntity(param);
        SysConfig newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);

        //如果是字典类型
        if (ToolUtil.isNotEmpty(param.getDictFlag())
                && param.getDictFlag().equalsIgnoreCase("Y")) {
            newEntity.setValue(param.getDictValue());
        } else {

            //如果是非字典，则标识位置为空
            newEntity.setDictFlag("N");
        }

        this.updateById(newEntity);

        //添加字典context
        ConstantsContext.putConstant(newEntity.getCode(), newEntity.getValue());
    }

    @Override
    public SysConfigResult findBySpec(SysConfigParam param) {
        return null;
    }

    @Override
    public List<SysConfigResult> findListBySpec(SysConfigParam param) {
        return null;
    }

    @Override
    public LayuiPageInfo findPageBySpec(SysConfigParam param) {
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(SysConfigParam param) {
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private SysConfig getOldEntity(SysConfigParam param) {
        return this.getById(getKey(param));
    }

    private SysConfig getEntity(SysConfigParam param) {
        SysConfig entity = new SysConfig();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
