package cn.atsoft.dasheng.sys.modular.rest.service;

import cn.atsoft.dasheng.sys.modular.rest.entity.RestNotice;
import cn.atsoft.dasheng.sys.modular.rest.mapper.RestNoticeMapper;
import cn.atsoft.dasheng.base.pojo.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 通知表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
@Service
public class RestNoticeService extends ServiceImpl<RestNoticeMapper, RestNotice> {

    /**
     * 获取通知列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:05 PM
     */
    public Page<Map<String, Object>> list(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.list(page, condition);
    }
}
