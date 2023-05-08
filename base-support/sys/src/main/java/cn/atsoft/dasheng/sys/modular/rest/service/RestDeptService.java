package cn.atsoft.dasheng.sys.modular.rest.service;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.sys.core.exception.enums.BizExceptionEnum;
import cn.atsoft.dasheng.sys.modular.rest.entity.RestDept;
import cn.atsoft.dasheng.sys.modular.rest.mapper.RestDeptMapper;
import cn.atsoft.dasheng.base.pojo.node.TreeNode;
import cn.atsoft.dasheng.base.pojo.node.TreeviewNode;
import cn.atsoft.dasheng.base.pojo.page.LayuiPageFactory;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.DeptBind;
import cn.atsoft.dasheng.sys.modular.system.service.DeptBindService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
@Service
public class RestDeptService extends ServiceImpl<RestDeptMapper, RestDept> {

    @Resource
    private RestDeptMapper restDeptMapper;

    @Autowired
    private DeptBindService deptBindService;
    /**
     * 新增部门
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:00 PM
     */
    @Transactional(rollbackFor = Exception.class)
    public Long addDept(RestDept dept) {

        if (ToolUtil.isOneEmpty(dept, dept.getSimpleName(), dept.getFullName(), dept.getPid(), dept.getDescription())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        //完善pids,根据pid拿到pid的pids
        this.deptSetPids(dept);

        this.save(dept);
        return dept.getDeptId();
    }

    /**
     * 修改部门
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:00 PM
     */
    @Transactional(rollbackFor = Exception.class)
    public void editDept(RestDept dept) {

        if (ToolUtil.isOneEmpty(dept, dept.getDeptId(), dept.getSimpleName(), dept.getFullName(), dept.getPid(), dept.getDescription())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        //完善pids,根据pid拿到pid的pids
        this.deptSetPids(dept);

        this.updateById(dept);
    }

    /**
     * 删除部门
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:16 PM
     */
    @Transactional
    public void deleteDept(Long deptId) {
        RestDept dept = restDeptMapper.selectById(deptId);

        //根据like查询删除所有级联的部门
        List<RestDept> subDepts = restDeptMapper.likePids(dept.getDeptId());
        Integer count = deptBindService.lambdaQuery().eq(DeptBind::getDeptId, deptId).count();
        if (subDepts.size()>0){
            throw new ServiceException(500,"请先删除子部门");
        }
        if (count>0){
            throw new ServiceException(500,"此部门下还有人员，不能删除");
        }
//        for (RestDept temp : subDepts) {
//            this.removeById(temp.getDeptId());
//        }

        this.removeById(dept.getDeptId());
    }

    /**
     * 获取ztree的节点列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:16 PM
     */
    public List<TreeNode> tree() {
        return this.baseMapper.tree(LoginContextHolder.getContext().getTenantId());
    }

    /**
     * 获取ztree的节点列表

     */
    public List<TreeviewNode> treeviewNodes() {
        return this.baseMapper.treeviewNodes();
    }

    /**
     * 获取所有部门列表
     */
    public Page<Map<String, Object>> list(String condition, Long deptId) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.list(page, condition, deptId,LoginContextHolder.getContext().getTenantId());
    }

    /**
     * 设置部门的父级ids
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:58 PM
     */
    private void deptSetPids(RestDept dept) {
        if (ToolUtil.isEmpty(dept.getPid()) || dept.getPid().equals(0L)) {
            dept.setPid(0L);
            dept.setPids("[0],");
        } else {
            Long pid = dept.getPid();
            RestDept temp = this.getById(pid);
            String pids = temp.getPids();
            dept.setPid(pid);
            dept.setPids(pids + "[" + pid + "],");
        }
    }
}
