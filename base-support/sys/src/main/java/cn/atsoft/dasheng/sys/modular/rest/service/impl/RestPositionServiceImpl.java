package cn.atsoft.dasheng.sys.modular.rest.service.impl;

import cn.atsoft.dasheng.sys.modular.rest.entity.RestPosition;
import cn.atsoft.dasheng.sys.modular.rest.entity.RestUserPos;
import cn.atsoft.dasheng.sys.modular.rest.mapper.RestPositionMapper;
import cn.atsoft.dasheng.sys.modular.system.model.params.PositionParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.PositionResult;
import cn.atsoft.dasheng.base.pojo.page.LayuiPageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.sys.modular.rest.service.RestPositionService;
import cn.atsoft.dasheng.sys.modular.rest.service.RestUserPosService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 职位表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-06-27
 */
@Service
public class RestPositionServiceImpl extends ServiceImpl<RestPositionMapper, RestPosition> implements RestPositionService {

    @Resource
    private RestUserPosService restUserPosService;

    @Override
    public Long add(PositionParam param) {
        RestPosition entity = getEntity(param);
        this.save(entity);
        return entity.getPositionId();
    }

    @Override
    public void delete(PositionParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(PositionParam param) {
        RestPosition oldEntity = getOldEntity(param);
        RestPosition newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PositionResult findBySpec(PositionParam param) {
        return null;
    }

    @Override
    public List<PositionResult> findListBySpec(PositionParam param) {
        return null;
    }

    @Override
    public PageInfo findPageBySpec(PositionParam param) {
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public PageInfo listPositions(Long userId) {

        //找出所有职位
        List<Map<String, Object>> list = this.baseMapper.getAllPositionMap();

        //用户id为空，则直接返回
        if (userId == null) {

        } else {

            //查询用户的职位id列表
            List<RestUserPos> userPosList = this.restUserPosService.list(new QueryWrapper<RestUserPos>().eq("user_id", userId));

            if (userPosList != null && userPosList.size() > 0) {
                for (RestUserPos userPos : userPosList) {
                    for (Map<String, Object> positionMap : list) {
                        if (userPos.getPosId().equals(positionMap.get("positionId"))) {
                            positionMap.put("selected", true);
                        }
                    }
                }
            }
        }

        PageInfo pageInfo = new PageInfo();
        pageInfo.setData(list);
        return pageInfo;

    }
    @Override
    public List<Map<String, Object>> listPositions(){
        return this.baseMapper.getAllPositionMap();
    }

    private Serializable getKey(PositionParam param) {
        return param.getPositionId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private RestPosition getOldEntity(PositionParam param) {
        return this.getById(getKey(param));
    }

    private RestPosition getEntity(PositionParam param) {
        RestPosition entity = new RestPosition();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
