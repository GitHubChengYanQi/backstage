/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.atsoft.dasheng.sys.modular.rest.controller;

import cn.atsoft.dasheng.core.treebuild.DefaultTreeBuildFactory;
import cn.atsoft.dasheng.sys.core.constant.dictmap.DeptDict;
import cn.atsoft.dasheng.sys.core.constant.factory.ConstantFactory;
import cn.atsoft.dasheng.sys.modular.rest.entity.RestDept;
import cn.atsoft.dasheng.sys.modular.rest.model.params.DeptParam;
import cn.atsoft.dasheng.sys.modular.system.model.DeptDto;
import cn.atsoft.dasheng.sys.modular.system.warpper.DeptWrapper;
import cn.hutool.core.bean.BeanUtil;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.node.TreeNode;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.sys.modular.rest.service.RestDeptService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 部门控制器
 *
 * @author fengshuonan
 * @Date 2017年2月17日20:27:22
 */
@RestController
@RequestMapping("/rest/dept")
public class RestDeptController extends BaseController {

    @Autowired
    private RestDeptService restDeptService;

    /**
     * 获取部门的tree列表，ztree格式
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:56 PM
     */
    @RequestMapping(value = "/tree")
    public ResponseData tree() {
        List<TreeNode> tree = this.restDeptService.tree();
        tree.add(TreeNode.createParent());
        //构建树
        DefaultTreeBuildFactory<TreeNode> factory = new DefaultTreeBuildFactory<>();
        factory.setRootParentId("-1");
        List<TreeNode> results = factory.doTreeBuild(tree);
        return ResponseData.success(results);
    }

    /**
     * 新增部门
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @BussinessLog(value = "添加部门", key = "simpleName", dict = DeptDict.class)
    @RequestMapping(value = "/add")
    public ResponseData add(@RequestBody RestDept restDept) {
        return ResponseData.success(this.restDeptService.addDept(restDept));
    }

    /**
     * 获取所有部门列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @RequestMapping(value = "/list")
    public PageInfo list(@RequestBody(required = false) DeptParam deptParam) {
        String condition = deptParam.getCondition();
        Long deptId = deptParam.getDeptId();
        Page<Map<String, Object>> list = this.restDeptService.list(condition, deptId);
        Page<Map<String, Object>> wrap = new DeptWrapper(list).wrap();

        return PageFactory.createPageInfo(wrap);
    }

    /**
     * 部门详情
     */
    @RequestMapping(value = "/detail")
    public ResponseData detail(@RequestBody RestDept restDept) {
        Long deptId = restDept.getDeptId();
        RestDept dept = restDeptService.getById(deptId);
        DeptDto deptDto = new DeptDto();
        BeanUtil.copyProperties(dept, deptDto);
        deptDto.setPName(ConstantFactory.me().getDeptName(deptDto.getPid()));
        return ResponseData.success(deptDto);
    }

    /**
     * 修改部门
     */
    @BussinessLog(value = "修改部门", key = "simpleName", dict = DeptDict.class)
    @RequestMapping(value = "/update")
    public ResponseData update(@RequestBody RestDept restDept) {
        restDeptService.editDept(restDept);
        return SUCCESS_TIP;
    }

    /**
     * 删除部门
     */
    @BussinessLog(value = "删除部门", key = "deptId", dict = DeptDict.class)
    @RequestMapping(value = "/delete")
    public ResponseData delete(@RequestBody RestDept restDept) {
        Long deptId = restDept.getDeptId();
        restDeptService.deleteDept(deptId);
        return SUCCESS_TIP;
    }

}
