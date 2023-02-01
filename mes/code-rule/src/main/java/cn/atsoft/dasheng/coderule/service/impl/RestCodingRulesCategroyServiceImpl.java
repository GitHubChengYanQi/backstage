package cn.atsoft.dasheng.coderule.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.coderule.model.RestCodeRulesCategory;
import cn.atsoft.dasheng.coderule.mapper.RestCodingRulesCategoryMapper;
import cn.atsoft.dasheng.coderule.model.params.RestCodingRulesCategoryParam;
import cn.atsoft.dasheng.coderule.model.result.RestCodingRulesCategoryResult;
import cn.atsoft.dasheng.coderule.service.RestCodingRulesCategoryService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 编码规则分类 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-22
 */
@Service
public class RestCodingRulesCategroyServiceImpl implements RestCodingRulesCategoryService {

    private List<RestCodeRulesCategory> categoryList;

    @Override
    public void add(RestCodeRulesCategory param){
        categoryList.add(param);
    }


}
