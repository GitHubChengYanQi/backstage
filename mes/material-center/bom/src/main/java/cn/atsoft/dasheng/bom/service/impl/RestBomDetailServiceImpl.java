package cn.atsoft.dasheng.bom.service.impl;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.bom.entity.RestBomDetail;
import cn.atsoft.dasheng.bom.mapper.RestBomDetailMapper;
import cn.atsoft.dasheng.bom.model.params.RestBomDetailParam;
import cn.atsoft.dasheng.bom.model.params.RestBomParam;
import cn.atsoft.dasheng.bom.model.result.RestBomDetailResult;
import cn.atsoft.dasheng.bom.service.RestBomDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestBomDetailServiceImpl extends ServiceImpl<RestBomDetailMapper, RestBomDetail> implements RestBomDetailService {


    @Override
    public void add(RestBomDetailParam param) {

    }

    @Override
    public void delete(RestBomDetailParam param) {

    }

    @Override
    public void update(RestBomDetailParam param) {

    }

    @Override
    public RestBomDetailResult findBySpec(RestBomDetailParam param) {
        return null;
    }

    @Override
    public List<RestBomDetailResult> findListBySpec(RestBomDetailParam param) {
        return null;
    }

    @Override
    public PageInfo<RestBomDetailResult> findPageBySpec(RestBomDetailParam param) {
        return null;
    }
}
