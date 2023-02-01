package cn.atsoft.dasheng.coderule.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.coderule.entity.RestSerialNumber;
import cn.atsoft.dasheng.coderule.mapper.RestSerialNumberMapper;
import cn.atsoft.dasheng.coderule.model.params.RestSerialNumberParam;
import cn.atsoft.dasheng.coderule.model.result.RestSerialNumberResult;
import cn.atsoft.dasheng.coderule.service.RestSerialNumberService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 流水号 服务实现类
 * </p>
 *
 * @author
 * @since 2021-11-04
 */
@Transactional
@Service
public class RestSerialNumberServiceImpl extends ServiceImpl<RestSerialNumberMapper, RestSerialNumber> implements RestSerialNumberService {
    @Autowired
    private RestSerialNumberService serialNumberService;

    @Override
    public String add(RestSerialNumberParam param) {
        RestSerialNumber entity = getEntity(param);
        RestSerialNumber num = this.getSerial(param);
        if (ToolUtil.isEmpty(num)) {
            entity.setNum(1L);
        } else {
            entity.setNum(num.getNum()+1L);

        }
        entity.setDate(new Date());
        this.save(entity);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        int max = entity.getSerialLength().intValue();

        nf.setMaximumIntegerDigits(max);
        nf.setMinimumIntegerDigits(max);
        Long snum = entity.getNum();
        return nf.format(snum);
    }

    @Override
    public List<String> addBatch(RestSerialNumberParam param) {
        RestSerialNumber entity = getEntity(param);
        RestSerialNumber num = this.getSerial(param);
        Long number = 0L;
        if (ToolUtil.isEmpty(num)) {
            number = 1L;
        } else {
            number = num.getNum() + 1;
        }
        List<RestSerialNumber> serialNumbers = new ArrayList<>();
        if (ToolUtil.isEmpty(param.getCont())){
            param.setCont(0L);
        }
        for (int i = 0; i < param.getCont(); i++) {
            RestSerialNumber serialNumber = new RestSerialNumber();
            ToolUtil.copyProperties(param, serialNumber);
            serialNumber.setNum(number + i);
            serialNumber.setDate(new Date());
            serialNumbers.add(serialNumber);
        }
        serialNumberService.saveBatch(serialNumbers);
        List<String> serialStr = new ArrayList<>();
        for (RestSerialNumber serialNumber : serialNumbers) {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            int max = entity.getSerialLength().intValue();

            nf.setMaximumIntegerDigits(max);
            nf.setMinimumIntegerDigits(max);
            Long snum = serialNumber.getNum();
            serialStr.add(nf.format(snum));
        }
        return serialStr;
    }

    public RestSerialNumber getSerial(RestSerialNumberParam param) {
        QueryWrapper<RestSerialNumber> queryWrapper = new QueryWrapper<>();
        if(ToolUtil.isNotEmpty(param.getDate())){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = simpleDateFormat.format(param.getDate());
            queryWrapper.apply("date_format(create_time,'%Y-%m-%d') = date_format('"+date+"','%Y-%m-%d')").orderByDesc("num");
        }
        if (ToolUtil.isNotEmpty(param.getMd5())) {
            queryWrapper.eq("md5",param.getMd5());
        }
        RestSerialNumber num = this.baseMapper.selectOne(queryWrapper.orderByDesc("num").last("limit 1"));
        return num;
    }

    @Override
    public void delete(RestSerialNumberParam param) {
//        this.removeById(getKey(param));
    }

    @Override
    public void update(RestSerialNumberParam param) {
//        SerialNumber oldEntity = getOldEntity(param);
//        SerialNumber newEntity = getEntity(param);
//        ToolUtil.copyProperties(newEntity, oldEntity);
//        this.updateById(newEntity);
    }

    @Override
    public RestSerialNumberResult findBySpec(RestSerialNumberParam param) {
        return null;
    }

    @Override
    public List<RestSerialNumberResult> findListBySpec(RestSerialNumberParam param) {
        return null;
    }

    @Override
    public PageInfo<RestSerialNumberResult> findPageBySpec(RestSerialNumberParam param) {
        Page<RestSerialNumberResult> pageContext = getPageContext();
        IPage<RestSerialNumberResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(RestSerialNumberParam param) {
        return param.getSerialId();
    }

    private Page<RestSerialNumberResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private RestSerialNumber getOldEntity(RestSerialNumberParam param) {
        return this.getById(getKey(param));
    }

    private RestSerialNumber getEntity(RestSerialNumberParam param) {
        RestSerialNumber entity = new RestSerialNumber();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
