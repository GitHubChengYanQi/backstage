package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.InquirtyComment;
import cn.atsoft.dasheng.purchase.mapper.InquirtyCommentMapper;
import cn.atsoft.dasheng.purchase.model.params.InquirtyCommentParam;
import cn.atsoft.dasheng.purchase.model.result.InquirtyCommentResult;
import cn.atsoft.dasheng.purchase.service.InquirtyCommentService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-23
 */
@Service
public class InquirtyCommentServiceImpl extends ServiceImpl<InquirtyCommentMapper, InquirtyComment> implements InquirtyCommentService {

    @Autowired
    private UserService userService;

    @Override
    public void add(InquirtyCommentParam param) {
        InquirtyComment entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(InquirtyCommentParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(InquirtyCommentParam param) {
        InquirtyComment oldEntity = getOldEntity(param);
        InquirtyComment newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InquirtyCommentResult findBySpec(InquirtyCommentParam param) {
        return null;
    }

    @Override
    public List<InquirtyCommentResult> findListBySpec(InquirtyCommentParam param) {
        return null;
    }

    @Override
    public PageInfo<InquirtyCommentResult> findPageBySpec(InquirtyCommentParam param) {
        Page<InquirtyCommentResult> pageContext = getPageContext();
        IPage<InquirtyCommentResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(InquirtyCommentParam param) {
        return param.getCommentId();
    }

    private Page<InquirtyCommentResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InquirtyComment getOldEntity(InquirtyCommentParam param) {
        return this.getById(getKey(param));
    }

    private InquirtyComment getEntity(InquirtyCommentParam param) {
        InquirtyComment entity = new InquirtyComment();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public List<InquirtyCommentResult> getComment(Long id) {
        List<InquirtyComment> comments = this.query().eq("form_id", id).list();
        List<Long> userIds = new ArrayList<>();
        List<InquirtyCommentResult> commentResults = new ArrayList<>();


        for (InquirtyComment comment : comments) {
            userIds.add(comment.getCreateUser());
            InquirtyCommentResult commentResult = new InquirtyCommentResult();
            ToolUtil.copyProperties(comment, commentResult);
            commentResults.add(commentResult);
        }

        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);

        for (InquirtyCommentResult commentResult : commentResults) {
            for (User user : userList) {
                if (user.getUserId().equals(commentResult.getCreateUser())) {
                    commentResult.setUser(user);
                    break;
                }
            }
        }

        return commentResults;
    }

}
