package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.BomDetail;
import cn.atsoft.dasheng.erp.mapper.BomDetailMapper;
import cn.atsoft.dasheng.erp.model.params.BomDetailParam;
import cn.atsoft.dasheng.erp.model.result.BomDetailResult;
import cn.atsoft.dasheng.erp.service.BomDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BomDetailServiceImpl extends ServiceImpl<BomDetailMapper,BomDetail> implements BomDetailService {


    @Override
    public void add(BomDetailParam param) {

    }

    @Override
    public void delete(BomDetailParam param) {

    }

    @Override
    public void update(BomDetailParam param) {

    }

    @Override
    public BomDetailResult findBySpec(BomDetailParam param) {
        return null;
    }

    @Override
    public List<BomDetailResult> findListBySpec(BomDetailParam param) {
        return null;
    }

    @Override
    public PageInfo<BomDetailResult> findPageBySpec(BomDetailParam param) {
        return null;
    }
}
