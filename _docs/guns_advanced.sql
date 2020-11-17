/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50728
 Source Host           : localhost:3306
 Source Schema         : guns_advanced

 Target Server Type    : MySQL
 Target Server Version : 50728
 File Encoding         : 65001

 Date: 17/11/2020 17:30:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for database_info
-- ----------------------------
DROP TABLE IF EXISTS `database_info`;
CREATE TABLE `database_info`  (
  `db_id` bigint(20) NOT NULL COMMENT '主键id',
  `db_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据库名称（英文名称）',
  `jdbc_driver` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'jdbc的驱动类型',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据库连接的账号',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据库连接密码',
  `jdbc_url` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'jdbc的url',
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注，摘要',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`db_id`) USING BTREE,
  UNIQUE INDEX `NAME_UNIQUE`(`db_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据库信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of database_info
-- ----------------------------
INSERT INTO `database_info` VALUES (1328595938939367426, 'master', 'com.mysql.cj.jdbc.Driver', 'root', '123qweasd', 'jdbc:mysql://127.0.0.1:3306/guns_advanced?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=CTT', '主数据源，项目启动数据源！', '2020-11-17 15:08:50');

-- ----------------------------
-- Table structure for excel_export_deploy
-- ----------------------------
DROP TABLE IF EXISTS `excel_export_deploy`;
CREATE TABLE `excel_export_deploy`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'excel导出配置名称',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件名称',
  `nid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一标识',
  `template` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模版路径',
  `data_source` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据源',
  `status` tinyint(3) NULL DEFAULT NULL COMMENT '0开启1关闭',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `E_E_D_NID`(`nid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'excel导出配置' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of excel_export_deploy
-- ----------------------------
INSERT INTO `excel_export_deploy` VALUES (1, '测试导出excel', '#{name}导出文件', 'test', '/uploadFiles/excelExportTemplate/1240266064118456322.xlsx', 'import cn.stylefeng.roses.core.data.SqlExe;\n\nimport javax.servlet.http.HttpServletRequest;\nimport java.util.HashMap;\nimport java.util.List;\nimport java.util.Map;\n\npublic class Engine {\n    public Map<String, Object> run(HttpServletRequest request) {\n        Map<String, Object> map = new HashMap<String, Object>();\n        // 通过request 可获取前台传来的参数\n        // 通过 GroovyTool.findManyRowData(sql, dataSource) 可获得对应的数据源\n        // 可像正常的使用java一样调用java类、方法以及Spring的bean\n        List<Map<String, Object>> list = SqlExe.selectList(\"SELECT * FROM `sys_dict`\", null);\n        map.put(\"name\", \"系统字典属性内容\");\n        map.put(\"list\", list);\n        System.out.println(\"123123123\");\n        return map;\n    }\n}', 0);

-- ----------------------------
-- Table structure for oauth_user_info
-- ----------------------------
DROP TABLE IF EXISTS `oauth_user_info`;
CREATE TABLE `oauth_user_info`  (
  `oauth_id` bigint(20) NOT NULL COMMENT '主键id',
  `user_id` bigint(20) NOT NULL COMMENT '用户主键id',
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `blog` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户网址',
  `company` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所在公司',
  `location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '位置',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户备注（各平台中的用户个人介绍）',
  `gender` int(11) NULL DEFAULT NULL COMMENT '性别，1-男，0-女',
  `source` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户来源',
  `token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户授权的token',
  `uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三方平台的用户唯一di',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建用户',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_user` bigint(20) NULL DEFAULT NULL COMMENT '更新用户',
  PRIMARY KEY (`oauth_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '第三方用户信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '属性编码标识',
  `dict_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否是字典中的值',
  `dict_type_id` bigint(20) NULL DEFAULT NULL COMMENT '字典类型的编码',
  `value` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '属性值，如果是字典中的类型，则为dict的code',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_user` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '参数配置' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1143324237579165697, '验证码开关', 'GUNS_KAPTCHA_OPEN', 'Y', 1106120265689055233, 'DISABLE', '是否开启验证码', '2019-06-24 12:46:43', 1, '2019-06-25 09:04:42', 1);
INSERT INTO `sys_config` VALUES (1143386834613694465, '阿里云短信的keyId', 'GUNS_SMS_ACCESSKEY_ID', 'N', NULL, 'xxxkey', '阿里云短信的密钥key', '2019-06-25 13:13:59', 1, '2019-06-25 13:19:21', 1);
INSERT INTO `sys_config` VALUES (1143386953933254657, '阿里云短信的secret', 'GUNS_SMS_ACCESSKEY_SECRET', 'N', NULL, 'xxxsecret', '阿里云短信的secret', '2019-06-25 13:14:28', 1, NULL, NULL);
INSERT INTO `sys_config` VALUES (1143387023449649154, '阿里云短信的签名', 'GUNS_SMS_SIGN_NAME', 'N', NULL, 'xxxsign', '阿里云短信的签名', '2019-06-25 13:14:44', 1, NULL, NULL);
INSERT INTO `sys_config` VALUES (1143387131109044225, '阿里云短信登录的模板号', 'GUNS_SMS_LOGIN_TEMPLATE_CODE', 'N', NULL, 'SMS_XXXXXX', '阿里云短信登录的模板号', '2019-06-25 13:15:10', 1, NULL, NULL);
INSERT INTO `sys_config` VALUES (1143387225019510785, '验证码短信失效时间', 'GUNS_SMS_INVALIDATE_MINUTES', 'N', NULL, '2', '验证码短信失效时间', '2019-06-25 13:15:32', 1, NULL, NULL);
INSERT INTO `sys_config` VALUES (1143468689664876546, '管理系统名称', 'GUNS_SYSTEM_NAME', 'N', NULL, 'Guns快速开发平台', '管理系统名称', '2019-06-25 18:39:15', 1, NULL, NULL);
INSERT INTO `sys_config` VALUES (1143468867767607297, '默认系统密码', 'GUNS_DEFAULT_PASSWORD', 'N', NULL, '111111', '默认系统密码', '2019-06-25 18:39:57', 1, NULL, NULL);
INSERT INTO `sys_config` VALUES (1143469008025133058, 'OAuth2登录用户的账号标识', 'GUNS_OAUTH2_PREFIX', 'N', NULL, 'oauth2', 'OAuth2登录用户的账号标识', '2019-06-25 18:40:31', 1, NULL, NULL);
INSERT INTO `sys_config` VALUES (1145207130463191041, '顶部导航条是否开启', 'GUNS_DEFAULT_ADVERT', 'Y', 1106120265689055233, 'DISABLE', '顶部Guns广告是否开启', '2019-06-30 13:47:11', 1, '2020-01-01 17:22:52', 1);
INSERT INTO `sys_config` VALUES (1145915627211370498, 'Guns发布的编号', 'GUNS_SYSTEM_RELEASE_VERSION', 'N', NULL, '20200318', '用于防止浏览器缓存相关的js和css', '2019-07-02 12:42:30', 1, NULL, NULL);
INSERT INTO `sys_config` VALUES (1145915627211370499, '文件上传路径', 'GUNS_FILE_UPLOAD_PATH', 'N', NULL, 'D:/tmp/', '文件上传默认目录', '2019-08-30 09:10:40', 1, NULL, NULL);
INSERT INTO `sys_config` VALUES (1145915627211370500, 'BPMN文件上传路径', 'GUNS_BPMN_FILE_UPLOAD_PATH', 'N', NULL, 'D:/tmp/', '工作流文件上传默认目录', '2019-08-30 09:10:40', 1, NULL, NULL);
INSERT INTO `sys_config` VALUES (1145915627211370501, '获取系统地密钥过期时间', 'GUNS_JWT_SECRET_EXPIRE', 'N', NULL, '86400', '获取系统地密钥过期时间（单位：秒），默认1天', '2019-10-16 23:02:39', 1, NULL, NULL);
INSERT INTO `sys_config` VALUES (1145915627211370502, '获取token的header标识', 'GUNS_TOKEN_HEADER_NAME', 'N', NULL, 'Authorization', '获取token的header标识', '2019-10-16 23:02:39', 1, NULL, NULL);
INSERT INTO `sys_config` VALUES (1145915627211370503, '获取租户是否开启的标识', 'GUNS_TENANT_OPEN', 'Y', 1106120265689055233, 'DISABLE', '获取租户是否开启的标识，默认是关的', '2019-10-16 23:02:39', 1, NULL, NULL);

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint(20) NOT NULL COMMENT '主键id',
  `pid` bigint(20) NULL DEFAULT 0 COMMENT '父部门id',
  `pids` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '父级ids',
  `simple_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '简称',
  `full_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '全称',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `version` int(11) NULL DEFAULT NULL COMMENT '版本（乐观锁保留字段）',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (24, 0, '[0],', '总公司', '总公司', '', NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES (25, 24, '[0],[24],', '开发部', '开发部', '', NULL, 2, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES (26, 24, '[0],[24],', '运营部', '运营部', '', NULL, 3, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES (27, 24, '[0],[24],', '战略部', '战略部', '', NULL, 4, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `dict_id` bigint(20) NOT NULL COMMENT '字典id',
  `dict_type_id` bigint(20) NOT NULL COMMENT '所属字典类型的id',
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典编码',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典名称',
  `parent_id` bigint(20) NOT NULL COMMENT '上级代码id',
  `parent_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所有上级id',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'ENABLE' COMMENT '状态（字典）',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `description` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典的描述',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`dict_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '基础字典' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1106120532442595330, 1106120208097067009, 'M', '男', 0, '[0]', 'ENABLE', NULL, '', '2019-03-14 17:11:00', NULL, 1, NULL);
INSERT INTO `sys_dict` VALUES (1106120574163337218, 1106120208097067009, 'F', '女', 0, '[0]', 'ENABLE', NULL, '', '2019-03-14 17:11:10', NULL, 1, NULL);
INSERT INTO `sys_dict` VALUES (1106120645697191938, 1106120265689055233, 'ENABLE', '启用', 0, '[0]', 'ENABLE', NULL, '', '2019-03-14 17:11:27', NULL, 1, NULL);
INSERT INTO `sys_dict` VALUES (1106120699468169217, 1106120265689055233, 'DISABLE', '禁用', 0, '[0]', 'ENABLE', NULL, '', '2019-03-14 17:11:40', NULL, 1, NULL);
INSERT INTO `sys_dict` VALUES (1106120784318939137, 1106120322450571266, 'ENABLE', '启用', 0, '[0]', 'ENABLE', NULL, '', '2019-03-14 17:12:00', NULL, 1, NULL);
INSERT INTO `sys_dict` VALUES (1106120825993543682, 1106120322450571266, 'FREEZE', '冻结', 0, '[0]', 'ENABLE', 1, '', '2019-03-14 17:12:10', '2019-03-16 10:56:36', 1, 1);
INSERT INTO `sys_dict` VALUES (1106120875872206849, 1106120322450571266, 'DELETED', '已删除', 0, '[0]', 'ENABLE', -1221, '', '2019-03-14 17:12:22', '2019-03-16 10:56:53', 1, 1);
INSERT INTO `sys_dict` VALUES (1106120935070613505, 1106120388036902914, 'Y', '删除', 0, '[0]', 'ENABLE', 23333, '', '2019-03-14 17:12:36', '2019-03-16 10:58:53', 1, 1);
INSERT INTO `sys_dict` VALUES (1106120968910258177, 1106120388036902914, 'N', '未删除', 0, '[0]', 'ENABLE', 1212211221, '', '2019-03-14 17:12:44', '2019-03-16 10:59:03', 1, 1);
INSERT INTO `sys_dict` VALUES (1149218674746355713, 1149217131989069826, 'BASE_SYSTEM', '基础功能', 0, '[0]', 'ENABLE', 1, '系统管理平台', '2019-07-11 15:27:38', '2020-01-01 17:14:45', 1, 1);
INSERT INTO `sys_dict` VALUES (1160533174626959361, 1160532704105742337, '00101', '办公审批', 0, '[0]', 'ENABLE', 10, '', '2019-08-11 20:47:25', NULL, 1, NULL);
INSERT INTO `sys_dict` VALUES (1160533264645111810, 1160532704105742337, '00102', '行政审批', 0, '[0]', 'ENABLE', 20, '', '2019-08-11 20:47:47', NULL, 1, NULL);
INSERT INTO `sys_dict` VALUES (1160533377727741954, 1160532775455047681, 'KEY_LEAVE', '请假流程标识', 0, '[0]', 'ENABLE', 10, '', '2019-08-11 20:48:14', NULL, 1, NULL);
INSERT INTO `sys_dict` VALUES (1160533455343337474, 1160532775455047681, 'KEY_FINANCE', '财务流程标识', 0, '[0]', 'ENABLE', 20, '', '2019-08-11 20:48:32', NULL, 1, NULL);
INSERT INTO `sys_dict` VALUES (1160533574843252737, 1160532886713155585, '00401', '事假', 0, '[0]', 'ENABLE', 10, '', '2019-08-11 20:49:01', NULL, 1, NULL);
INSERT INTO `sys_dict` VALUES (1160533625615302658, 1160532886713155585, '00402', '婚假', 0, '[0]', 'ENABLE', 20, '', '2019-08-11 20:49:13', NULL, 1, NULL);
INSERT INTO `sys_dict` VALUES (1160533707215486977, 1160532886713155585, '00403', '产假', 0, '[0]', 'ENABLE', 30, '', '2019-08-11 20:49:32', NULL, 1, NULL);
INSERT INTO `sys_dict` VALUES (1160533765403066370, 1160532886713155585, '00404', '病假', 0, '[0]', 'ENABLE', 40, '', '2019-08-11 20:49:46', NULL, 1, NULL);
INSERT INTO `sys_dict` VALUES (1160533863834992641, 1160532886713155585, '00405', '公假', 0, '[0]', 'ENABLE', 50, '', '2019-08-11 20:50:09', NULL, 1, NULL);
INSERT INTO `sys_dict` VALUES (1160533945309347841, 1160532886713155585, '00406', '年假', 0, '[0]', 'ENABLE', 60, '', '2019-08-11 20:50:29', NULL, 1, NULL);
INSERT INTO `sys_dict` VALUES (1160534007389241346, 1160532886713155585, '00407', '其他', 0, '[0]', 'ENABLE', 70, '', '2019-08-11 20:50:44', NULL, 1, NULL);
INSERT INTO `sys_dict` VALUES (1212300736972668929, 1149217131989069826, 'ENT_FUNC', '企业功能', 0, '[0]', 'ENABLE', 20, '企业功能', '2020-01-01 17:13:14', NULL, 1, NULL);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_type_id` bigint(20) NOT NULL COMMENT '字典类型id',
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典类型编码',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典类型名称',
  `description` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典描述',
  `system_flag` char(1) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '是否是系统字典，Y-是，N-否',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'ENABLE' COMMENT '状态(字典)',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`dict_type_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字典类型表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1106120208097067009, 'SEX', '性别', '', 'Y', 'ENABLE', 4, '2019-03-14 17:09:43', 1, NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (1106120265689055233, 'STATUS', '状态', '', 'Y', 'ENABLE', 3, '2019-03-14 17:09:57', 1, NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (1106120322450571266, 'ACCOUNT_STATUS', '账号状态', '', 'Y', 'ENABLE', 40, '2019-03-14 17:10:10', 1, '2019-08-11 20:46:38', 1);
INSERT INTO `sys_dict_type` VALUES (1106120388036902914, 'DEL_FLAG', '是否删除', '', 'Y', 'ENABLE', 2, '2019-03-14 17:10:26', 1, '2019-03-27 16:26:31', 1);
INSERT INTO `sys_dict_type` VALUES (1149217131989069826, 'SYSTEM_TYPE', '系统分类', '系统所有分类的维护', 'Y', 'ENABLE', 50, '2019-07-11 15:21:30', 1, '2019-08-11 20:46:47', 1);
INSERT INTO `sys_dict_type` VALUES (1160532704105742337, 'FLOW_CATEGARY', '工作流分类', '工作流分类', 'Y', 'ENABLE', 60, '2019-08-11 20:45:33', 1, NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (1160532775455047681, 'FLOW_KEY', '工作流标识', '工作流标识', 'Y', 'ENABLE', 70, '2019-08-11 20:45:50', 1, NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (1160532886713155585, 'LEAVE_TYPE', '请假类型', '请假类型', 'Y', 'ENABLE', 80, '2019-08-11 20:46:17', 1, '2019-08-11 20:46:23', 1);

-- ----------------------------
-- Table structure for sys_file_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_file_info`;
CREATE TABLE `sys_file_info`  (
  `file_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `file_bucket` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件仓库（oss仓库）',
  `file_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件名称',
  `file_suffix` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件后缀',
  `file_size_kb` bigint(20) NULL DEFAULT NULL COMMENT '文件大小kb',
  `final_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件唯一标识id',
  `file_path` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存储路径',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建用户',
  `update_user` bigint(20) NULL DEFAULT NULL COMMENT '修改用户',
  PRIMARY KEY (`file_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文件信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_file_info
-- ----------------------------
INSERT INTO `sys_file_info` VALUES ('1167385745179131905', NULL, '请假流程.bpmn20.xml', 'xml', 6, '1167385745179131905.xml', '/Users/stylefeng/tmp/gunsTempFiles/1167385745179131905.xml', '2019-08-30 18:37:05', NULL, 1, NULL);

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`  (
  `login_log_id` bigint(20) NOT NULL COMMENT '主键',
  `log_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志名称',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '管理员id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `succeed` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否执行成功',
  `message` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '具体消息',
  `ip_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录ip',
  PRIMARY KEY (`login_log_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '登录记录' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------
INSERT INTO `sys_login_log` VALUES (1240625083123281921, '登录日志', 1, '2020-03-19 21:04:04', '成功', NULL, '0:0:0:0:0:0:0:1');
INSERT INTO `sys_login_log` VALUES (1327256448223625217, '登录日志', 1, '2020-11-13 22:26:11', '成功', NULL, '127.0.0.1');
INSERT INTO `sys_login_log` VALUES (1327260421953372161, '登录日志', 1, '2020-11-13 22:41:59', '成功', NULL, '127.0.0.1');
INSERT INTO `sys_login_log` VALUES (1327262994978480129, '登录日志', 1, '2020-11-13 22:52:12', '成功', NULL, '127.0.0.1');
INSERT INTO `sys_login_log` VALUES (1327266400057040897, '登录日志', 1, '2020-11-13 23:05:44', '成功', NULL, '127.0.0.1');
INSERT INTO `sys_login_log` VALUES (1327266919521611777, '登录日志', 1, '2020-11-13 23:07:48', '成功', NULL, '127.0.0.1');
INSERT INTO `sys_login_log` VALUES (1327267393951899650, '登录日志', 1, '2020-11-13 23:09:41', '成功', NULL, '127.0.0.1');
INSERT INTO `sys_login_log` VALUES (1327270731791671298, '登录日志', 1, '2020-11-13 23:22:57', '成功', NULL, '127.0.0.1');
INSERT INTO `sys_login_log` VALUES (1327429050409844738, '登录日志', 1, '2020-11-14 09:52:03', '成功', NULL, '127.0.0.1');
INSERT INTO `sys_login_log` VALUES (1327430095529713666, '登录日志', 1, '2020-11-14 09:56:12', '成功', NULL, '127.0.0.1');
INSERT INTO `sys_login_log` VALUES (1327431011796414465, '登录日志', 1, '2020-11-14 09:59:50', '成功', NULL, '127.0.0.1');
INSERT INTO `sys_login_log` VALUES (1327433102879256577, '登录日志', 1, '2020-11-14 10:08:09', '成功', NULL, '127.0.0.1');
INSERT INTO `sys_login_log` VALUES (1327447373151391745, '登录日志', 1, '2020-11-14 11:04:51', '成功', NULL, '127.0.0.1');
INSERT INTO `sys_login_log` VALUES (1327449897740701698, '登录日志', 1, '2020-11-14 11:14:53', '成功', NULL, '127.0.0.1');
INSERT INTO `sys_login_log` VALUES (1327450567545905153, '登录日志', 1, '2020-11-14 11:17:33', '成功', NULL, '127.0.0.1');
INSERT INTO `sys_login_log` VALUES (1327453123345055745, '登录日志', 1, '2020-11-14 11:27:42', '成功', NULL, '127.0.0.1');
INSERT INTO `sys_login_log` VALUES (1327453461267513346, '登录日志', 1, '2020-11-14 11:29:03', '成功', NULL, '127.0.0.1');
INSERT INTO `sys_login_log` VALUES (1328143047770271745, '登录日志', 1, '2020-11-16 09:09:13', '成功', NULL, '192.168.5.253');
INSERT INTO `sys_login_log` VALUES (1328214747765657602, '登录日志', 1, '2020-11-16 13:54:08', '成功', NULL, '192.168.5.253');
INSERT INTO `sys_login_log` VALUES (1328215345479131138, '登录日志', 1, '2020-11-16 13:56:30', '成功', NULL, '192.168.5.253');
INSERT INTO `sys_login_log` VALUES (1328215992253378561, '登录日志', 1, '2020-11-16 13:59:04', '成功', NULL, '192.168.5.253');
INSERT INTO `sys_login_log` VALUES (1328216716378968066, '登录日志', 1, '2020-11-16 14:01:57', '成功', NULL, '192.168.5.253');
INSERT INTO `sys_login_log` VALUES (1328238444467466242, '登录日志', 1, '2020-11-16 15:28:17', '成功', NULL, '192.168.5.253');
INSERT INTO `sys_login_log` VALUES (1328238842968399873, '登录日志', 1, '2020-11-16 15:29:52', '成功', NULL, '192.168.5.253');
INSERT INTO `sys_login_log` VALUES (1328247502192893954, '登录日志', 1, '2020-11-16 16:04:17', '成功', NULL, '192.168.5.253');
INSERT INTO `sys_login_log` VALUES (1328248662832201730, '登录日志', 1, '2020-11-16 16:08:54', '成功', NULL, '192.168.5.253');
INSERT INTO `sys_login_log` VALUES (1328500062933819393, '登录日志', 1, '2020-11-17 08:47:52', '成功', NULL, '192.168.5.253');
INSERT INTO `sys_login_log` VALUES (1328506394978037762, '登录日志', 1, '2020-11-17 09:13:02', '成功', NULL, '192.168.5.253');
INSERT INTO `sys_login_log` VALUES (1328537931232587778, '登录日志', 1, '2020-11-17 11:18:21', '成功', NULL, '192.168.5.253');
INSERT INTO `sys_login_log` VALUES (1328591211937660929, '登录日志', 1, '2020-11-17 14:50:04', '成功', NULL, '192.168.5.253');
INSERT INTO `sys_login_log` VALUES (1328595972820955138, '登录日志', 1, '2020-11-17 15:08:59', '成功', NULL, '192.168.5.253');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(20) NOT NULL COMMENT '主键id',
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单编号',
  `pid` bigint(20) NULL DEFAULT 0 COMMENT '菜单父编号',
  `pcode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单父编号',
  `pids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前菜单的所有父菜单编号',
  `pcodes` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前菜单的所有父菜单编号',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'url地址',
  `sort` int(65) NULL DEFAULT NULL COMMENT '菜单排序号',
  `levels` int(65) NULL DEFAULT NULL COMMENT '菜单层级',
  `menu_flag` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否是菜单(字典)',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'ENABLE' COMMENT '菜单状态(字典)',
  `new_page_flag` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否打开新页面的标识(字典)',
  `open_flag` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否打开(字典)',
  `system_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统分类(字典)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (105, 'system', 0, '0', '0,', '[0],', '系统管理', 'SettingOutlined', '#', 20, 1, 'Y', '', 'ENABLE', NULL, '1', 'BASE_SYSTEM', NULL, '2020-11-14 10:04:16', NULL, 1);
INSERT INTO `sys_menu` VALUES (106, 'mgr', 105, 'system', '0,105,', '[0],[105],', '用户管理', 'UserOutlined', '/BASE_SYSTEM/mgr', 10, 2, 'Y', '', 'ENABLE', NULL, '0', 'BASE_SYSTEM', NULL, '2020-11-14 11:52:09', NULL, 1);
INSERT INTO `sys_menu` VALUES (107, 'mgr_add', 106, 'mgr', '0,105,106,', '[0],[105],[106],', '添加用户', '', '/mgr/add', 1, 3, 'N', '', 'ENABLE', NULL, '0', 'BASE_SYSTEM', NULL, '2020-11-14 10:00:27', NULL, 1);
INSERT INTO `sys_menu` VALUES (108, 'mgr_edit', 106, 'mgr', '0,105,106,', '[0],[105],[106],', '修改用户', '', '/mgr/edit', 2, 3, 'N', '', 'ENABLE', NULL, '0', 'BASE_SYSTEM', NULL, '2020-11-14 10:00:35', NULL, 1);
INSERT INTO `sys_menu` VALUES (109, 'mgr_delete', 106, 'mgr', '0,105,106,', '[0],[105],[106],', '删除用户', '', '/mgr/delete', 3, 3, 'N', '', 'ENABLE', NULL, '0', 'BASE_SYSTEM', NULL, '2020-11-14 10:00:43', NULL, 1);
INSERT INTO `sys_menu` VALUES (110, 'mgr_reset', 106, 'mgr', '0,105,106,', '[0],[105],[106],', '重置密码', '', '/mgr/reset', 4, 3, 'N', '', 'ENABLE', NULL, '0', 'BASE_SYSTEM', NULL, '2020-11-14 10:02:11', NULL, 1);
INSERT INTO `sys_menu` VALUES (111, 'mgr_freeze', 106, 'mgr', '0,105,106,', '[0],[105],[106],', '冻结用户', '', '/mgr/freeze', 5, 3, 'N', '', 'ENABLE', NULL, '0', 'BASE_SYSTEM', NULL, '2020-11-14 10:02:17', NULL, 1);
INSERT INTO `sys_menu` VALUES (112, 'mgr_unfreeze', 106, 'mgr', '0,105,106,', '[0],[105],[106],', '解除冻结用户', '', '/mgr/unfreeze', 6, 3, 'N', '', 'ENABLE', NULL, '0', 'BASE_SYSTEM', NULL, '2020-11-14 10:02:23', NULL, 1);
INSERT INTO `sys_menu` VALUES (113, 'mgr_setRole', 106, 'mgr', '0,105,106,', '[0],[105],[106],', '分配角色', '', '/mgr/setRole', 7, 3, 'N', '', 'ENABLE', NULL, '0', 'BASE_SYSTEM', NULL, '2020-11-14 10:05:07', NULL, 1);
INSERT INTO `sys_menu` VALUES (114, 'role', 105, 'system', '0,105,', '[0],[105],', '角色管理', 'SkinOutlined', '/BASE_SYSTEM/role', 20, 2, 'Y', '', 'ENABLE', NULL, '0', 'BASE_SYSTEM', NULL, '2020-11-16 09:52:02', NULL, 1);
INSERT INTO `sys_menu` VALUES (115, 'role_add', 114, 'role', '0,105,114,', '[0],[105],[114],', '添加角色', '', '/role/add', 1, 3, 'N', '', 'ENABLE', NULL, '0', 'BASE_SYSTEM', NULL, '2020-11-14 10:05:28', NULL, 1);
INSERT INTO `sys_menu` VALUES (116, 'role_edit', 114, 'role', '0,105,114,', '[0],[105],[114],', '修改角色', '', '/role/edit', 2, 3, 'N', '', 'ENABLE', NULL, '0', 'BASE_SYSTEM', NULL, '2020-11-14 10:05:35', NULL, 1);
INSERT INTO `sys_menu` VALUES (117, 'role_remove', 114, 'role', '0,105,114,', '[0],[105],[114],', '删除角色', '', '/role/remove', 3, 3, 'N', '', 'ENABLE', NULL, '0', 'BASE_SYSTEM', NULL, '2020-11-14 10:05:40', NULL, 1);
INSERT INTO `sys_menu` VALUES (118, 'role_setAuthority', 114, 'role', '0,105,114,', '[0],[105],[114],', '配置权限', '', '/role/setAuthority', 4, 3, 'N', '', 'ENABLE', NULL, '0', 'BASE_SYSTEM', NULL, '2020-11-14 10:05:47', NULL, 1);
INSERT INTO `sys_menu` VALUES (119, 'menu', 105, 'system', '0,105,', '[0],[105],', '菜单管理', 'InsertRowBelowOutlined', '/BASE_SYSTEM/menu', 50, 2, 'Y', '', 'ENABLE', NULL, '0', 'BASE_SYSTEM', NULL, '2020-11-14 11:54:04', NULL, 1);
INSERT INTO `sys_menu` VALUES (120, 'menu_add', 119, 'menu', '0119,', '[0],[system],[119],', '添加菜单', '', '/menu/add', 1, 3, 'N', '', 'ENABLE', NULL, '0', 'BASE_SYSTEM', NULL, '2020-11-14 10:06:15', NULL, 1);
INSERT INTO `sys_menu` VALUES (121, 'menu_edit', 119, 'menu', '0119,', '[0],[system],[119],', '修改菜单', '', '/menu/edit', 2, 3, 'N', '', 'ENABLE', NULL, '0', 'BASE_SYSTEM', NULL, '2020-11-14 10:06:20', NULL, 1);
INSERT INTO `sys_menu` VALUES (122, 'menu_remove', 119, 'menu', '0119,', '[0],[system],[119],', '删除菜单', '', '/menu/remove', 3, 3, 'N', '', 'ENABLE', NULL, '0', 'BASE_SYSTEM', NULL, '2020-11-14 10:06:25', NULL, 1);
INSERT INTO `sys_menu` VALUES (128, 'log', 105, 'system', '0', '[0],[system],', '业务日志', '', '/log', 70, 2, 'Y', NULL, 'ENABLE', NULL, '0', 'BASE_SYSTEM', NULL, '2020-11-14 10:04:16', NULL, 1);
INSERT INTO `sys_menu` VALUES (130, 'druid', 105, 'system', '0', '[0],[system],', '监控管理', '', '/druid', 80, 2, 'Y', NULL, 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:04:16', NULL, 1);
INSERT INTO `sys_menu` VALUES (131, 'dept', 105, 'system', '0,105,', '[0],[105],', '部门管理', 'PhoneOutlined', '/BASE_SYSTEM/dept', 30, 2, 'Y', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-16 10:37:57', NULL, 1);
INSERT INTO `sys_menu` VALUES (132, 'dict', 105, 'system', '0,105,', '[0],[105],', '字典管理', 'WindowsOutlined', '/BASE_SYSTEM/dictType', 50, 2, 'Y', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-17 09:00:17', NULL, 1);
INSERT INTO `sys_menu` VALUES (133, 'loginLog', 105, 'system', '0', '[0],[system],', '登录日志', '', '/loginLog', 60, 2, 'Y', NULL, 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:04:16', NULL, 1);
INSERT INTO `sys_menu` VALUES (134, 'log_clean', 128, 'log', '0128,', '[0],[system],[128],', '清空日志', '', '/log/delLog', 3, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:06:41', NULL, 1);
INSERT INTO `sys_menu` VALUES (135, 'dept_add', 131, 'dept', '0131,', '[0],[system],[131],', '添加部门', '', '/dept/add', 1, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:10:14', NULL, 1);
INSERT INTO `sys_menu` VALUES (136, 'dept_update', 131, 'dept', '0131,', '[0],[system],[131],', '修改部门', '', '/dept/update', 1, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:10:22', NULL, 1);
INSERT INTO `sys_menu` VALUES (137, 'dept_delete', 131, 'dept', '0131,', '[0],[system],[131],', '删除部门', '', '/dept/delete', 1, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:11:00', NULL, 1);
INSERT INTO `sys_menu` VALUES (138, 'dict_add', 132, 'dict', '0132,', '[0],[system],[132],', '添加字典', '', '/dictType/addItem', 1, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:11:08', NULL, 1);
INSERT INTO `sys_menu` VALUES (139, 'dict_update', 132, 'dict', '0132,', '[0],[system],[132],', '修改字典', '', '/dictType/editItem', 1, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:11:15', NULL, 1);
INSERT INTO `sys_menu` VALUES (140, 'dict_delete', 132, 'dict', '0132,', '[0],[system],[132],', '删除字典', '', '/dictType/delete', 1, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:11:23', NULL, 1);
INSERT INTO `sys_menu` VALUES (141, 'notice', 105, 'system', '0', '[0],[system],', '通知管理', '', '/notice', 90, 2, 'Y', NULL, 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:04:16', NULL, 1);
INSERT INTO `sys_menu` VALUES (142, 'notice_add', 141, 'notice', '0141,', '[0],[system],[141],', '添加通知', '', '/notice/add', 1, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:11:32', NULL, 1);
INSERT INTO `sys_menu` VALUES (143, 'notice_update', 141, 'notice', '0141,', '[0],[system],[141],', '修改通知', '', '/notice/update', 2, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:11:40', NULL, 1);
INSERT INTO `sys_menu` VALUES (144, 'notice_delete', 141, 'notice', '0141,', '[0],[system],[141],', '删除通知', '', '/notice/delete', 3, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:11:59', NULL, 1);
INSERT INTO `sys_menu` VALUES (145, 'sys_message', 172, 'dashboard', '0,172,', '[0],[172],', '消息通知', 'BellOutlined', '/system/notice', 30, 2, 'Y', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 11:51:36', NULL, 1);
INSERT INTO `sys_menu` VALUES (149, 'api_mgr', 171, 'dev_tools', '0,171,', '[0],[171],', '接口文档', 'fa-leaf', '/swagger-ui.html', 30, 2, 'Y', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 11:43:21', NULL, 1);
INSERT INTO `sys_menu` VALUES (150, 'to_menu_edit', 119, 'menu', '0119,', '[0],[system],[119],', '菜单编辑跳转', '', '/menu/menu_edit', 4, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:12:11', NULL, 1);
INSERT INTO `sys_menu` VALUES (151, 'menu_list', 119, 'menu', '0119,', '[0],[system],[119],', '菜单列表', '', '/menu/list', 5, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:12:18', NULL, 1);
INSERT INTO `sys_menu` VALUES (152, 'to_dept_update', 131, 'dept', '0131,', '[0],[system],[131],', '修改部门跳转', '', '/dept/dept_update', 4, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:12:29', NULL, 1);
INSERT INTO `sys_menu` VALUES (153, 'dept_list', 131, 'dept', '0131,', '[0],[system],[131],', '部门列表', '', '/dept/list', 5, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:12:36', NULL, 1);
INSERT INTO `sys_menu` VALUES (154, 'dept_detail', 131, 'dept', '0131,', '[0],[system],[131],', '部门详情', '', '/dept/detail', 6, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:12:42', NULL, 1);
INSERT INTO `sys_menu` VALUES (155, 'to_dict_edit', 119, 'menu', '0119,', '[0],[system],[119],', '修改菜单跳转', '', '/dict/dict_edit', 4, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:13:11', NULL, 1);
INSERT INTO `sys_menu` VALUES (156, 'dict_list', 132, 'dict', '0132,', '[0],[system],[132],', '字典列表', '', '/dict/list', 5, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:13:17', NULL, 1);
INSERT INTO `sys_menu` VALUES (157, 'dict_detail', 132, 'dict', '0132,', '[0],[system],[132],', '字典详情', '', '/dict/detail', 6, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:13:25', NULL, 1);
INSERT INTO `sys_menu` VALUES (158, 'log_list', 128, 'log', '0128,', '[0],[system],[128],', '日志列表', '', '/log/list', 2, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:13:30', NULL, 1);
INSERT INTO `sys_menu` VALUES (159, 'log_detail', 128, 'log', '0128,', '[0],[system],[128],', '日志详情', '', '/log/detail', 3, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:13:35', NULL, 1);
INSERT INTO `sys_menu` VALUES (160, 'del_login_log', 133, 'loginLog', '0133,', '[0],[system],[133],', '清空登录日志', '', '/loginLog/delLoginLog', 1, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:13:41', NULL, 1);
INSERT INTO `sys_menu` VALUES (161, 'login_log_list', 133, 'loginLog', '0133,', '[0],[system],[133],', '登录日志列表', '', '/loginLog/list', 2, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:13:47', NULL, 1);
INSERT INTO `sys_menu` VALUES (162, 'to_role_edit', 114, 'role', '0,105,114,', '[0],[105],[114],', '修改角色跳转', '', '/role/role_edit', 5, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:13:55', NULL, 1);
INSERT INTO `sys_menu` VALUES (163, 'to_role_assign', 114, 'role', '0,105,114,', '[0],[105],[114],', '角色分配跳转', '', '/role/role_assign', 6, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:14:00', NULL, 1);
INSERT INTO `sys_menu` VALUES (164, 'role_list', 114, 'role', '0,105,114,', '[0],[105],[114],', '角色列表', '', '/role/list', 7, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:14:05', NULL, 1);
INSERT INTO `sys_menu` VALUES (165, 'to_assign_role', 114, 'role', '0,105,114,', '[0],[105],[114],', '分配角色跳转', '', '/mgr/role_assign', 8, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:14:10', NULL, 1);
INSERT INTO `sys_menu` VALUES (166, 'to_user_edit', 106, 'mgr', '0,105,106,', '[0],[105],[106],', '编辑用户跳转', '', '/mgr/user_edit', 9, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:14:14', NULL, 1);
INSERT INTO `sys_menu` VALUES (167, 'mgr_list', 106, 'mgr', '0,105,106,', '[0],[105],[106],', '用户列表', '', '/mgr/list', 10, 3, 'N', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 10:14:19', NULL, 1);
INSERT INTO `sys_menu` VALUES (171, 'dev_tools', 0, '0', '0,', '[0],', '开发管理', 'layui-icon layui-icon-code-circle', '#', 30, 1, 'Y', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 11:43:15', NULL, 1);
INSERT INTO `sys_menu` VALUES (172, 'dashboard', 0, '0', '0,', '[0],', '主控面板', 'layui-icon layui-icon-home', '#', 10, 1, 'Y', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', NULL, '2020-11-14 11:29:51', NULL, 1);
INSERT INTO `sys_menu` VALUES (1110777136265838594, 'demos_show', 105, '0', NULL, '[0],', '高级组件', 'layui-icon-diamond', '#', 60, 1, 'Y', NULL, 'ENABLE', NULL, NULL, 'ENT_FUNC', '2019-03-27 13:34:41', '2020-01-01 18:23:50', 1, 1);
INSERT INTO `sys_menu` VALUES (1110777366856089602, 'excel_import', 1212299504967204865, 'EXCEL_PROCESS', '01212299504967204865,', '[0],[1212299504967204865],', 'excel导入', 'layui-icon-rate-solid', '/excel/import', 10, 2, 'Y', '', 'ENABLE', NULL, NULL, 'ENT_FUNC', '2019-03-27 13:35:36', '2020-11-14 11:30:17', 1, 1);
INSERT INTO `sys_menu` VALUES (1110777491464667137, 'excel_export', 1212299504967204865, 'EXCEL_PROCESS', '01212299504967204865,', '[0],[1212299504967204865],', 'excel导出', 'layui-icon-rate-solid', '/excel/export', 20, 2, 'Y', '', 'ENABLE', NULL, NULL, 'ENT_FUNC', '2019-03-27 13:36:06', '2020-11-14 11:30:24', 1, 1);
INSERT INTO `sys_menu` VALUES (1110787391943098370, 'advanced_form', 1110777136265838594, 'demos_show', '01110777136265838594,', '[0],[1110777136265838594],', '高级表单', '', '/egForm', 30, 2, 'Y', '', 'ENABLE', NULL, NULL, 'ENT_FUNC', '2019-03-27 14:15:26', '2020-11-14 11:30:37', 1, 1);
INSERT INTO `sys_menu` VALUES (1110839216310329346, 'pdf_view', 1110777136265838594, 'demos_show', '01110777136265838594,', '[0],[1110777136265838594],', '文档预览', '', '/loadPdfFile', 40, 2, 'Y', '', 'ENABLE', NULL, NULL, 'ENT_FUNC', '2019-03-27 17:41:22', '2020-11-14 11:30:43', 1, 1);
INSERT INTO `sys_menu` VALUES (1111545968697860098, 'console', 172, 'dashboard', '0,172,', '[0],[172],', '项目介绍', 'PicCenterOutlined', '/system/console', 10, 2, 'Y', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', '2019-03-29 16:29:45', '2020-11-14 11:51:22', 1, 1);
INSERT INTO `sys_menu` VALUES (1111546189892870145, 'console2', 105, '0', NULL, '[0],', '统计报表', '', '/system/console2', 20, 1, 'Y', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', '2019-03-29 16:30:38', '2020-11-13 23:11:11', 1, 1);
INSERT INTO `sys_menu` VALUES (1127085735660421122, 'code_generate', 171, 'dev_tools', '0,171,', '[0],[171],', '代码生成', '', '/gen', 20, 2, 'Y', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', '2019-05-11 13:39:14', '2020-11-14 11:43:28', 1, 1);
INSERT INTO `sys_menu` VALUES (1139827152854716418, 'data_source', 105, '0', NULL, '[0],', '数据容器', 'layui-icon-template-1', '/databaseInfo', 40, 1, 'Y', NULL, 'ENABLE', NULL, NULL, 'ENT_FUNC', '2019-06-15 17:29:05', '2020-01-01 18:23:17', 1, 1);
INSERT INTO `sys_menu` VALUES (1142957882422112257, 'SYS_CONFIG', 171, 'dev_tools', '0,171,', '[0],[171],', '参数配置', 'fa-star', '/sysConfig', 30, 2, 'Y', '', 'ENABLE', '', '', 'BASE_SYSTEM', '2019-06-24 08:49:28', '2020-11-14 11:43:35', 1, 1);
INSERT INTO `sys_menu` VALUES (1142957882422112258, 'SYS_CONFIG_ADD', 1142957882422112257, 'SYS_CONFIG', '0,171,1142957882422112257,', '[0],[171],[1142957882422112257],', '参数配置添加', 'fa-star', '', 999, 3, 'N', '', 'ENABLE', '', '', 'BASE_SYSTEM', '2019-06-24 08:49:28', '2020-11-14 11:43:46', 1, 1);
INSERT INTO `sys_menu` VALUES (1142957882422112259, 'SYS_CONFIG_EDIT', 1142957882422112257, 'SYS_CONFIG', '0,171,1142957882422112257,', '[0],[171],[1142957882422112257],', '参数配置修改', 'fa-star', '', 999, 3, 'N', '', 'ENABLE', '', '', 'BASE_SYSTEM', '2019-06-24 08:49:28', '2020-11-14 11:43:51', 1, 1);
INSERT INTO `sys_menu` VALUES (1142957882426306562, 'SYS_CONFIG_DELETE', 1142957882422112257, 'SYS_CONFIG', '0,171,1142957882422112257,', '[0],[171],[1142957882422112257],', '参数配置删除', 'fa-star', '', 999, 3, 'N', '', 'ENABLE', '', '', 'BASE_SYSTEM', '2019-06-24 08:49:28', '2020-11-14 11:43:56', 1, 1);
INSERT INTO `sys_menu` VALUES (1144441072852684801, 'SYS_POSITION', 105, 'system', '0,105,', '[0],[105],', '职位管理', 'ReadOutlined', '/BASE_SYSTEM/position', 35, 2, 'Y', '', 'ENABLE', '', '', 'BASE_SYSTEM', '2019-06-28 11:03:09', '2020-11-16 10:38:38', 1, 1);
INSERT INTO `sys_menu` VALUES (1144441072852684802, 'SYS_POSITION_ADD', 1144441072852684801, 'SYS_POSITION', '01144441072852684801,', '[0],[system],[1144441072852684801],', '职位表添加', 'fa-star', '', 999, 3, 'N', '', 'ENABLE', '', '', 'BASE_SYSTEM', '2019-06-28 11:03:09', '2020-11-14 11:32:11', 1, 1);
INSERT INTO `sys_menu` VALUES (1144441072852684803, 'SYS_POSITION_EDIT', 1144441072852684801, 'SYS_POSITION', '01144441072852684801,', '[0],[system],[1144441072852684801],', '职位表修改', 'fa-star', '', 999, 3, 'N', '', 'ENABLE', '', '', 'BASE_SYSTEM', '2019-06-28 11:03:09', '2020-11-14 11:32:19', 1, 1);
INSERT INTO `sys_menu` VALUES (1144441072852684804, 'SYS_POSITION_DELETE', 1144441072852684801, 'SYS_POSITION', '01144441072852684801,', '[0],[system],[1144441072852684801],', '职位表删除', 'fa-star', '', 999, 3, 'N', '', 'ENABLE', '', '', 'BASE_SYSTEM', '2019-06-28 11:03:09', '2020-11-14 11:32:28', 1, 1);
INSERT INTO `sys_menu` VALUES (1149955324929765378, 'system_info', 172, 'dashboard', '0,172,', '[0],[172],', '系统监控', 'layui-icon-star-fill', '/system/systemInfo', 40, 2, 'Y', '', 'ENABLE', NULL, NULL, 'BASE_SYSTEM', '2019-07-13 16:14:49', '2020-11-14 11:42:18', 1, 1);
INSERT INTO `sys_menu` VALUES (1212299504967204865, 'EXCEL_PROCESS', 105, '0', NULL, '[0],', 'excel处理', 'layui-icon-template', '#', 10, 1, 'Y', NULL, 'ENABLE', NULL, NULL, 'ENT_FUNC', '2020-01-01 17:08:20', '2020-01-01 18:22:18', 1, 1);
INSERT INTO `sys_menu` VALUES (1212299802154614786, 'EXCEL_PROCESS_EXPORT', 1212299504967204865, 'EXCEL_PROCESS', '01212299504967204865,', '[0],[1212299504967204865],', '模板配置', 'layui-icon-template', '/excelExportDeploy', 5, 2, 'Y', '', 'ENABLE', NULL, NULL, 'ENT_FUNC', '2020-01-01 17:09:31', '2020-11-14 11:32:39', 1, 1);

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` bigint(20) NOT NULL COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '内容',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通知表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (6, '欢迎', 'hi，Guns旗舰版发布了！', '2017-01-11 08:53:20', 1, '2018-12-28 23:24:48', 1);
INSERT INTO `sys_notice` VALUES (8, '你好', '你好，世界！', '2017-05-10 19:28:57', 1, '2018-12-28 23:28:26', 1);

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`  (
  `operation_log_id` bigint(20) NOT NULL COMMENT '主键',
  `log_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志类型(字典)',
  `log_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志名称',
  `user_id` bigint(65) NULL DEFAULT NULL COMMENT '用户id',
  `class_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类名称',
  `method` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '方法名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `succeed` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否成功(字典)',
  `message` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '备注',
  PRIMARY KEY (`operation_log_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '操作日志' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_operation_log
-- ----------------------------
INSERT INTO `sys_operation_log` VALUES (1240625204678406146, '业务日志', '配置权限', 1, 'cn.stylefeng.guns.sys.modular.system.controller.RoleController', 'setAuthority', '2020-03-19 21:04:33', '成功', '角色名称=超级管理员,资源名称=系统管理,用户管理,添加用户,修改用户,删除用户,重置密码,冻结用户,解除冻结用户,分配角色,分配角色跳转,编辑用户跳转,用户列表,角色管理,添加角色,修改角色,删除角色,配置权限,修改角色跳转,角色分配跳转,角色列表,菜单管理,添加菜单,修改菜单,删除菜单,菜单编辑跳转,菜单列表,业务日志,清空日志,日志列表,日志详情,监控管理,部门管理,添加部门,修改部门,删除部门,修改部门跳转,部门列表,部门详情,字典管理,添加字典,修改字典,删除字典,修改菜单跳转,字典列表,字典详情,登录日志,清空登录日志,登录日志列表,通知管理,添加通知,修改通知,删除通知,职位管理,职位表添加,职位表修改,职位表删除,开发管理,接口文档,代码生成,参数配置,参数配置添加,参数配置修改,参数配置删除,主控面板,消息通知,项目介绍,统计报表,系统监控,高级组件,高级表单,文档预览,数据容器,excel处理,excel导入,excel导出,模板配置');
INSERT INTO `sys_operation_log` VALUES (1240625232142708737, '业务日志', '删除菜单', 1, 'cn.stylefeng.guns.sys.modular.system.controller.MenuController', 'remove', '2020-03-19 21:04:40', '成功', '菜单名称=在线办公');
INSERT INTO `sys_operation_log` VALUES (1240625246839549953, '业务日志', '删除菜单', 1, 'cn.stylefeng.guns.sys.modular.system.controller.MenuController', 'remove', '2020-03-19 21:04:43', '成功', '菜单名称=国际化');
INSERT INTO `sys_operation_log` VALUES (1240625258407440385, '业务日志', '删除菜单', 1, 'cn.stylefeng.guns.sys.modular.system.controller.MenuController', 'remove', '2020-03-19 21:04:46', '成功', '菜单名称=租户管理');
INSERT INTO `sys_operation_log` VALUES (1327256678981648386, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-13 22:27:06', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327256861551312898, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-13 22:27:50', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327259317085618177, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-13 22:37:35', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327259876484136962, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-13 22:39:49', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327260275333087233, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-13 22:41:24', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327260477435625474, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-13 22:42:12', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327260564349992961, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.system.controller.MenuController', 'edit', '2020-11-13 22:42:33', '成功', '菜单名称=菜单管理;;;');
INSERT INTO `sys_operation_log` VALUES (1327266927926996993, '异常日志', '', 1, NULL, NULL, '2020-11-13 23:07:50', '失败', 'java.lang.NullPointerException\r\n	at cn.atsoft.dasheng.core.treebuild.DefaultCascaderBuildFactory.getSubChildsLevelOne(DefaultCascaderBuildFactory.java:79)\r\n	at cn.atsoft.dasheng.core.treebuild.DefaultCascaderBuildFactory.buildChildNodes(DefaultCascaderBuildFactory.java:53)\r\n	at cn.atsoft.dasheng.core.treebuild.DefaultCascaderBuildFactory.executeBuilding(DefaultCascaderBuildFactory.java:105)\r\n	at cn.atsoft.dasheng.core.treebuild.abst.AbstractTreeBuildFactory.doTreeBuild(AbstractTreeBuildFactory.java:40)\r\n	at cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController.menuTreeList(RestMenuController.java:217)\r\n	at cn.stylefeng.guns.sys.modular.rest.controller.RestMenuControllerTTFastClassBySpringCGLIBTTeba945df.invoke(<generated>)\r\n	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)\r\n	at org.springframework.aop.framework.CglibAopProxyTCglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:749)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\r\n	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:93)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\r\n	at org.springframework.aop.framework.CglibAopProxyTDynamicAdvisedInterceptor.intercept(CglibAopProxy.java:688)\r\n	at cn.stylefeng.guns.sys.modular.rest.controller.RestMenuControllerTTEnhancerBySpringCGLIBTTaadf1c99.menuTreeList(<generated>)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:190)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:138)\r\n	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:104)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:892)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:797)\r\n	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\r\n	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1039)\r\n	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:942)\r\n	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1005)\r\n	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:897)\r\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:634)\r\n	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:882)\r\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:741)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:103)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at cn.atsoft.dasheng.core.xss.XssFilter.doFilter(XssFilter.java:30)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at com.alibaba.druid.support.http.WebStatFilter.doFilter(WebStatFilter.java:124)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:320)\r\n	at org.springframework.security.web.access.intercept.FilterSecurityInterceptor.invoke(FilterSecurityInterceptor.java:127)\r\n	at org.springframework.security.web.access.intercept.FilterSecurityInterceptor.doFilter(FilterSecurityInterceptor.java:91)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.access.ExceptionTranslationFilter.doFilter(ExceptionTranslationFilter.java:119)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.session.SessionManagementFilter.doFilter(SessionManagementFilter.java:137)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.authentication.AnonymousAuthenticationFilter.doFilter(AnonymousAuthenticationFilter.java:111)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter.doFilter(SecurityContextHolderAwareRequestFilter.java:170)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.savedrequest.RequestCacheAwareFilter.doFilter(RequestCacheAwareFilter.java:63)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at cn.stylefeng.guns.sys.core.auth.filter.JwtAuthorizationTokenFilter.doFilterInternal(JwtAuthorizationTokenFilter.java:78)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.web.filter.CorsFilter.doFilterInternal(CorsFilter.java:96)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.header.HeaderWriterFilter.doFilterInternal(HeaderWriterFilter.java:74)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.context.SecurityContextPersistenceFilter.doFilter(SecurityContextPersistenceFilter.java:105)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter.doFilterInternal(WebAsyncManagerIntegrationFilter.java:56)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.FilterChainProxy.doFilterInternal(FilterChainProxy.java:215)\r\n	at org.springframework.security.web.FilterChainProxy.doFilter(FilterChainProxy.java:178)\r\n	at org.springframework.web.filter.DelegatingFilterProxy.invokeDelegate(DelegatingFilterProxy.java:357)\r\n	at org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:270)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:99)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:92)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.HiddenHttpMethodFilter.doFilterInternal(HiddenHttpMethodFilter.java:93)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:200)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:202)\r\n	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:96)\r\n	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:490)\r\n	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:139)\r\n	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)\r\n	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)\r\n	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343)\r\n	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:408)\r\n	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:66)\r\n	at org.apache.coyote.AbstractProtocolTConnectionHandler.process(AbstractProtocol.java:853)\r\n	at org.apache.tomcat.util.net.NioEndpointTSocketProcessor.doRun(NioEndpoint.java:1587)\r\n	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)\r\n	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)\r\n	at java.util.concurrent.ThreadPoolExecutorTWorker.run(ThreadPoolExecutor.java:624)\r\n	at org.apache.tomcat.util.threads.TaskThreadTWrappingRunnable.run(TaskThread.java:61)\r\n	at java.lang.Thread.run(Thread.java:748)\r\n');
INSERT INTO `sys_operation_log` VALUES (1327267543998930945, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-13 23:10:17', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327267771888050178, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-13 23:11:11', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327268003149389825, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-13 23:12:06', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327268232519098369, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-13 23:13:01', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327268589051715586, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-13 23:14:26', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327268757272666113, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-13 23:15:06', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327269819853447169, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-13 23:19:19', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327270503948623873, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-13 23:22:02', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327270793884147714, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-13 23:23:11', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327270919264477185, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-13 23:23:41', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327421591037353985, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 09:22:24', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327421595583979521, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 09:22:25', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327423301877178370, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 09:29:12', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327423739624103938, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 09:30:57', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327423877985804290, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 09:31:30', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327424166935601153, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 09:32:38', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327424353288527874, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 09:33:23', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327424396116566018, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 09:33:33', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327427714666668034, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 09:46:44', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327428143869796354, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 09:48:27', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327429116616933377, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 09:52:18', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327429543827767297, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 09:54:00', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327429631430000642, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 09:54:21', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327430123111456770, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 09:56:18', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327430170066690050, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 09:56:30', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327430496127688705, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 09:57:47', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327431075700830210, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:00:06', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327431119770382337, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:00:16', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327431164154507265, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:00:27', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327431197650219009, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:00:35', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327431231837990913, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:00:43', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327431277052588033, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:00:54', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327431323617751041, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:01:05', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327431374393995265, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:01:17', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327431548264673281, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:01:58', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327431571958296578, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:02:04', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327431600093687810, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:02:11', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327431627499270145, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:02:17', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327431650484056065, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:02:23', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327431706025029633, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:02:36', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327432127124762625, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:04:16', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327432341915070465, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:05:07', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327432395593773058, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:05:20', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327432428540030978, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:05:28', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327432455916253186, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:05:35', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327432478309642242, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:05:40', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327432507518775298, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:05:47', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327432625919782914, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:06:15', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327432646090190850, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:06:20', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327432667116236801, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:06:25', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327432735776993281, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:06:41', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327433626647162882, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:10:14', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327433659744415746, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:10:22', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327433820084269057, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:11:00', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327433855429668865, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:11:08', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327433884450058242, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:11:15', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327433916679090177, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:11:23', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327433955325407234, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:11:32', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327433987562827778, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:11:40', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327434069712465922, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:11:59', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327434120484515841, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:12:12', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327434146434674690, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:12:18', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327434194438483970, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:12:29', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327434222355771393, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:12:36', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327434248515645441, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:12:42', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327434368581791746, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:13:11', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327434396117397506, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:13:17', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327434427264299009, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:13:25', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327434449984843778, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:13:30', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327434471585509378, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:13:35', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327434494922616833, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:13:41', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327434522328199170, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:13:47', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327434553689010177, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:13:55', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327434577118392322, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:14:00', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327434595325865985, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:14:05', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327434617299824641, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:14:10', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327434634509053954, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:14:14', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327434655702872065, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 10:14:19', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327449942640726017, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:15:04', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327453662447304706, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:29:51', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327453771314659330, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:30:17', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327453801220046849, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:30:24', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327453855129436161, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:30:37', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327453883805892610, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:30:43', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327454132360347650, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:31:43', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327454160978083842, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:31:50', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327454180800364545, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:31:54', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327454251738628098, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:32:11', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327454285288865794, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:32:19', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327454322018385921, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:32:28', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327454367111348226, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:32:39', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327454652873474049, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:33:47', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327456467442630657, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:40:59', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327456631574134785, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:41:39', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327456702269128706, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:41:55', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327456777967927298, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:42:13', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327456797844733953, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:42:18', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327456824172380162, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:42:24', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327456860532801537, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:42:33', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327456881814700034, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:42:38', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327456946079825922, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:42:54', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327457001008431105, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:43:07', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327457034193764354, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:43:15', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327457061880365057, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:43:21', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327457089650851841, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:43:28', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327457118893539330, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:43:35', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327457165454508033, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:43:46', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327457187403300865, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:43:51', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327457207766646786, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:43:56', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327459079713882114, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:51:22', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327459136173408258, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:51:36', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327459274455416833, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:52:09', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327459383834476546, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:52:35', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1327459757953810434, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-14 11:54:04', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1328153823251820545, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-16 09:52:02', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1328165135461675009, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-16 10:36:59', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1328165195964510210, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-16 10:37:14', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1328165377741451265, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-16 10:37:57', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1328165549854715905, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-16 10:38:38', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1328165628573413378, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-16 10:38:57', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1328165691047571457, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-16 10:39:12', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1328214779734642690, '异常日志', '', 1, NULL, NULL, '2020-11-16 13:54:15', '失败', 'org.springframework.jdbc.UncategorizedSQLException: \r\n### Error querying database.  Cause: java.sql.SQLException: sql injection violation, syntax error: TODO pos 96, line 2, column 34, token CASE : select role_id AS id,role_id AS \"value\", pid as \"parentId\",\n		name as name,name as \"label\" (case when (pid = 0 or pid is null) then \'true\'\n		else \'false\' end) as \"open\" from sys_role\r\n### The error may exist in file [D:\\Project\\Java\\guns-vip\\guns-base-support\\guns-sys\\target\\classes\\cn\\stylefeng\\guns\\sys\\modular\\rest\\mapper\\mapping\\RestRoleMapper.xml]\r\n### The error may involve cn.stylefeng.guns.sys.modular.rest.mapper.RestRoleMapper.roleTreeList\r\n### The error occurred while executing a query\r\n### SQL: select role_id AS id,role_id AS \"value\", pid as \"parentId\",   name as name,name as \"label\" (case when (pid = 0 or pid is null) then \'true\'   else \'false\' end) as \"open\" from sys_role\r\n### Cause: java.sql.SQLException: sql injection violation, syntax error: TODO pos 96, line 2, column 34, token CASE : select role_id AS id,role_id AS \"value\", pid as \"parentId\",\n		name as name,name as \"label\" (case when (pid = 0 or pid is null) then \'true\'\n		else \'false\' end) as \"open\" from sys_role\n; uncategorized SQLException; SQL state [null]; error code [0]; sql injection violation, syntax error: TODO pos 96, line 2, column 34, token CASE : select role_id AS id,role_id AS \"value\", pid as \"parentId\",\n		name as name,name as \"label\" (case when (pid = 0 or pid is null) then \'true\'\n		else \'false\' end) as \"open\" from sys_role; nested exception is java.sql.SQLException: sql injection violation, syntax error: TODO pos 96, line 2, column 34, token CASE : select role_id AS id,role_id AS \"value\", pid as \"parentId\",\n		name as name,name as \"label\" (case when (pid = 0 or pid is null) then \'true\'\n		else \'false\' end) as \"open\" from sys_role\r\n	at org.springframework.jdbc.support.AbstractFallbackSQLExceptionTranslator.translate(AbstractFallbackSQLExceptionTranslator.java:89)\r\n	at org.springframework.jdbc.support.AbstractFallbackSQLExceptionTranslator.translate(AbstractFallbackSQLExceptionTranslator.java:81)\r\n	at org.springframework.jdbc.support.AbstractFallbackSQLExceptionTranslator.translate(AbstractFallbackSQLExceptionTranslator.java:81)\r\n	at org.mybatis.spring.MyBatisExceptionTranslator.translateExceptionIfPossible(MyBatisExceptionTranslator.java:88)\r\n	at cn.atsoft.dasheng.core.mutidatasource.mybatis.OptionalSqlSessionTemplateTSqlSessionInterceptor.invoke(OptionalSqlSessionTemplate.java:348)\r\n	at com.sun.proxy.TProxy96.selectList(Unknown Source)\r\n	at cn.atsoft.dasheng.core.mutidatasource.mybatis.OptionalSqlSessionTemplate.selectList(OptionalSqlSessionTemplate.java:155)\r\n	at com.baomidou.mybatisplus.core.override.MybatisMapperMethod.executeForMany(MybatisMapperMethod.java:177)\r\n	at com.baomidou.mybatisplus.core.override.MybatisMapperMethod.execute(MybatisMapperMethod.java:78)\r\n	at com.baomidou.mybatisplus.core.override.MybatisMapperProxy.invoke(MybatisMapperProxy.java:96)\r\n	at com.sun.proxy.TProxy142.roleTreeList(Unknown Source)\r\n	at cn.stylefeng.guns.sys.modular.rest.service.RestRoleService.roleTreeList(RestRoleService.java:167)\r\n	at cn.stylefeng.guns.sys.modular.rest.service.RestRoleServiceTTFastClassBySpringCGLIBTTcb8788f0.invoke(<generated>)\r\n	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)\r\n	at org.springframework.aop.framework.CglibAopProxyTDynamicAdvisedInterceptor.intercept(CglibAopProxy.java:684)\r\n	at cn.stylefeng.guns.sys.modular.rest.service.RestRoleServiceTTEnhancerBySpringCGLIBTT96c0ae23.roleTreeList(<generated>)\r\n	at cn.stylefeng.guns.sys.modular.rest.controller.RestRoleController.roleTreeList(RestRoleController.java:137)\r\n	at cn.stylefeng.guns.sys.modular.rest.controller.RestRoleControllerTTFastClassBySpringCGLIBTT486e0036.invoke(<generated>)\r\n	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)\r\n	at org.springframework.aop.framework.CglibAopProxyTCglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:749)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\r\n	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:93)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\r\n	at org.springframework.aop.framework.CglibAopProxyTDynamicAdvisedInterceptor.intercept(CglibAopProxy.java:688)\r\n	at cn.stylefeng.guns.sys.modular.rest.controller.RestRoleControllerTTEnhancerBySpringCGLIBTT3accd3dc.roleTreeList(<generated>)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:190)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:138)\r\n	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:104)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:892)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:797)\r\n	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\r\n	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1039)\r\n	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:942)\r\n	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1005)\r\n	at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:908)\r\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:660)\r\n	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:882)\r\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:741)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:103)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at cn.atsoft.dasheng.core.xss.XssFilter.doFilter(XssFilter.java:30)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at com.alibaba.druid.support.http.WebStatFilter.doFilter(WebStatFilter.java:124)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:320)\r\n	at org.springframework.security.web.access.intercept.FilterSecurityInterceptor.invoke(FilterSecurityInterceptor.java:127)\r\n	at org.springframework.security.web.access.intercept.FilterSecurityInterceptor.doFilter(FilterSecurityInterceptor.java:91)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.access.ExceptionTranslationFilter.doFilter(ExceptionTranslationFilter.java:119)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.session.SessionManagementFilter.doFilter(SessionManagementFilter.java:137)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.authentication.AnonymousAuthenticationFilter.doFilter(AnonymousAuthenticationFilter.java:111)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter.doFilter(SecurityContextHolderAwareRequestFilter.java:170)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.savedrequest.RequestCacheAwareFilter.doFilter(RequestCacheAwareFilter.java:63)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at cn.stylefeng.guns.sys.core.auth.filter.JwtAuthorizationTokenFilter.doFilterInternal(JwtAuthorizationTokenFilter.java:78)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.web.filter.CorsFilter.doFilterInternal(CorsFilter.java:96)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.header.HeaderWriterFilter.doFilterInternal(HeaderWriterFilter.java:74)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.context.SecurityContextPersistenceFilter.doFilter(SecurityContextPersistenceFilter.java:105)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter.doFilterInternal(WebAsyncManagerIntegrationFilter.java:56)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.FilterChainProxy.doFilterInternal(FilterChainProxy.java:215)\r\n	at org.springframework.security.web.FilterChainProxy.doFilter(FilterChainProxy.java:178)\r\n	at org.springframework.web.filter.DelegatingFilterProxy.invokeDelegate(DelegatingFilterProxy.java:357)\r\n	at org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:270)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:99)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:92)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.HiddenHttpMethodFilter.doFilterInternal(HiddenHttpMethodFilter.java:93)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:200)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:202)\r\n	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:96)\r\n	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:490)\r\n	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:139)\r\n	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)\r\n	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)\r\n	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343)\r\n	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:408)\r\n	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:66)\r\n	at org.apache.coyote.AbstractProtocolTConnectionHandler.process(AbstractProtocol.java:853)\r\n	at org.apache.tomcat.util.net.NioEndpointTSocketProcessor.doRun(NioEndpoint.java:1587)\r\n	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)\r\n	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)\r\n	at java.util.concurrent.ThreadPoolExecutorTWorker.run(ThreadPoolExecutor.java:624)\r\n	at org.apache.tomcat.util.threads.TaskThreadTWrappingRunnable.run(TaskThread.java:61)\r\n	at java.lang.Thread.run(Thread.java:748)\r\nCaused by: java.sql.SQLException: sql injection violation, syntax error: TODO pos 96, line 2, column 34, token CASE : select role_id AS id,role_id AS \"value\", pid as \"parentId\",\n		name as name,name as \"label\" (case when (pid = 0 or pid is null) then \'true\'\n		else \'false\' end) as \"open\" from sys_role\r\n	at com.alibaba.druid.wall.WallFilter.checkInternal(WallFilter.java:806)\r\n	at com.alibaba.druid.wall.WallFilter.connection_prepareStatement(WallFilter.java:259)\r\n	at com.alibaba.druid.filter.FilterChainImpl.connection_prepareStatement(FilterChainImpl.java:568)\r\n	at com.alibaba.druid.proxy.jdbc.ConnectionProxyImpl.prepareStatement(ConnectionProxyImpl.java:341)\r\n	at com.alibaba.druid.pool.DruidPooledConnection.prepareStatement(DruidPooledConnection.java:350)\r\n	at sun.reflect.GeneratedMethodAccessor83.invoke(Unknown Source)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at com.atomikos.jdbc.AtomikosConnectionProxy.invoke(AtomikosConnectionProxy.java:141)\r\n	at com.sun.proxy.TProxy182.prepareStatement(Unknown Source)\r\n	at sun.reflect.GeneratedMethodAccessor83.invoke(Unknown Source)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.apache.ibatis.logging.jdbc.ConnectionLogger.invoke(ConnectionLogger.java:55)\r\n	at com.sun.proxy.TProxy178.prepareStatement(Unknown Source)\r\n	at org.apache.ibatis.executor.statement.PreparedStatementHandler.instantiateStatement(PreparedStatementHandler.java:86)\r\n	at org.apache.ibatis.executor.statement.BaseStatementHandler.prepare(BaseStatementHandler.java:88)\r\n	at org.apache.ibatis.executor.statement.RoutingStatementHandler.prepare(RoutingStatementHandler.java:59)\r\n	at sun.reflect.GeneratedMethodAccessor82.invoke(Unknown Source)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.apache.ibatis.plugin.Invocation.proceed(Invocation.java:49)\r\n	at com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor.intercept(PaginationInterceptor.java:186)\r\n	at org.apache.ibatis.plugin.Plugin.invoke(Plugin.java:61)\r\n	at com.sun.proxy.TProxy177.prepare(Unknown Source)\r\n	at sun.reflect.GeneratedMethodAccessor82.invoke(Unknown Source)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.apache.ibatis.plugin.Invocation.proceed(Invocation.java:49)\r\n	at cn.atsoft.dasheng.core.datascope.DataScopeInterceptor.intercept(DataScopeInterceptor.java:52)\r\n	at org.apache.ibatis.plugin.Plugin.invoke(Plugin.java:61)\r\n	at com.sun.proxy.TProxy177.prepare(Unknown Source)\r\n	at com.baomidou.mybatisplus.core.executor.MybatisSimpleExecutor.prepareStatement(MybatisSimpleExecutor.java:92)\r\n	at com.baomidou.mybatisplus.core.executor.MybatisSimpleExecutor.doQuery(MybatisSimpleExecutor.java:66)\r\n	at org.apache.ibatis.executor.BaseExecutor.queryFromDatabase(BaseExecutor.java:324)\r\n	at org.apache.ibatis.executor.BaseExecutor.query(BaseExecutor.java:156)\r\n	at com.baomidou.mybatisplus.core.executor.MybatisCachingExecutor.query(MybatisCachingExecutor.java:155)\r\n	at com.baomidou.mybatisplus.core.executor.MybatisCachingExecutor.query(MybatisCachingExecutor.java:90)\r\n	at sun.reflect.GeneratedMethodAccessor110.invoke(Unknown Source)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.apache.ibatis.plugin.Plugin.invoke(Plugin.java:63)\r\n	at com.sun.proxy.TProxy176.query(Unknown Source)\r\n	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:147)\r\n	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:140)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at cn.atsoft.dasheng.core.mutidatasource.mybatis.OptionalSqlSessionTemplateTSqlSessionInterceptor.invoke(OptionalSqlSessionTemplate.java:335)\r\n	... 117 more\r\nCaused by: com.alibaba.druid.sql.parser.ParserException: TODO pos 96, line 2, column 34, token CASE\r\n	at com.alibaba.druid.sql.parser.SQLStatementParser.parseStatementList(SQLStatementParser.java:512)\r\n	at com.alibaba.druid.sql.parser.SQLStatementParser.parseStatementList(SQLStatementParser.java:182)\r\n	at com.alibaba.druid.wall.WallProvider.checkInternal(WallProvider.java:624)\r\n	at com.alibaba.druid.wall.WallProvider.check(WallProvider.java:578)\r\n	at com.alibaba.druid.wall.WallFilter.checkInternal(WallFilter.java:793)\r\n	... 166 more\r\n');
INSERT INTO `sys_operation_log` VALUES (1328215372142321666, '异常日志', '', 1, NULL, NULL, '2020-11-16 13:56:36', '失败', 'org.springframework.jdbc.UncategorizedSQLException: \r\n### Error querying database.  Cause: java.sql.SQLException: sql injection violation, syntax error: ERROR. pos 77, line 2, column 17, token COMMA : select role_id AS id,role_id AS \"value\", pid as \"parentId\",\n		name as name, ,name as \"label\", (case when (pid = 0 or pid is null) then \'true\'\n		else \'false\' end) as \"open\" from sys_role\r\n### The error may exist in file [D:\\Project\\Java\\guns-vip\\guns-base-support\\guns-sys\\target\\classes\\cn\\stylefeng\\guns\\sys\\modular\\rest\\mapper\\mapping\\RestRoleMapper.xml]\r\n### The error may involve cn.stylefeng.guns.sys.modular.rest.mapper.RestRoleMapper.roleTreeList\r\n### The error occurred while executing a query\r\n### SQL: select role_id AS id,role_id AS \"value\", pid as \"parentId\",   name as name, ,name as \"label\", (case when (pid = 0 or pid is null) then \'true\'   else \'false\' end) as \"open\" from sys_role\r\n### Cause: java.sql.SQLException: sql injection violation, syntax error: ERROR. pos 77, line 2, column 17, token COMMA : select role_id AS id,role_id AS \"value\", pid as \"parentId\",\n		name as name, ,name as \"label\", (case when (pid = 0 or pid is null) then \'true\'\n		else \'false\' end) as \"open\" from sys_role\n; uncategorized SQLException; SQL state [null]; error code [0]; sql injection violation, syntax error: ERROR. pos 77, line 2, column 17, token COMMA : select role_id AS id,role_id AS \"value\", pid as \"parentId\",\n		name as name, ,name as \"label\", (case when (pid = 0 or pid is null) then \'true\'\n		else \'false\' end) as \"open\" from sys_role; nested exception is java.sql.SQLException: sql injection violation, syntax error: ERROR. pos 77, line 2, column 17, token COMMA : select role_id AS id,role_id AS \"value\", pid as \"parentId\",\n		name as name, ,name as \"label\", (case when (pid = 0 or pid is null) then \'true\'\n		else \'false\' end) as \"open\" from sys_role\r\n	at org.springframework.jdbc.support.AbstractFallbackSQLExceptionTranslator.translate(AbstractFallbackSQLExceptionTranslator.java:89)\r\n	at org.springframework.jdbc.support.AbstractFallbackSQLExceptionTranslator.translate(AbstractFallbackSQLExceptionTranslator.java:81)\r\n	at org.springframework.jdbc.support.AbstractFallbackSQLExceptionTranslator.translate(AbstractFallbackSQLExceptionTranslator.java:81)\r\n	at org.mybatis.spring.MyBatisExceptionTranslator.translateExceptionIfPossible(MyBatisExceptionTranslator.java:88)\r\n	at cn.atsoft.dasheng.core.mutidatasource.mybatis.OptionalSqlSessionTemplateTSqlSessionInterceptor.invoke(OptionalSqlSessionTemplate.java:348)\r\n	at com.sun.proxy.TProxy96.selectList(Unknown Source)\r\n	at cn.atsoft.dasheng.core.mutidatasource.mybatis.OptionalSqlSessionTemplate.selectList(OptionalSqlSessionTemplate.java:155)\r\n	at com.baomidou.mybatisplus.core.override.MybatisMapperMethod.executeForMany(MybatisMapperMethod.java:177)\r\n	at com.baomidou.mybatisplus.core.override.MybatisMapperMethod.execute(MybatisMapperMethod.java:78)\r\n	at com.baomidou.mybatisplus.core.override.MybatisMapperProxy.invoke(MybatisMapperProxy.java:96)\r\n	at com.sun.proxy.TProxy142.roleTreeList(Unknown Source)\r\n	at cn.stylefeng.guns.sys.modular.rest.service.RestRoleService.roleTreeList(RestRoleService.java:167)\r\n	at cn.stylefeng.guns.sys.modular.rest.service.RestRoleServiceTTFastClassBySpringCGLIBTTcb8788f0.invoke(<generated>)\r\n	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)\r\n	at org.springframework.aop.framework.CglibAopProxyTDynamicAdvisedInterceptor.intercept(CglibAopProxy.java:684)\r\n	at cn.stylefeng.guns.sys.modular.rest.service.RestRoleServiceTTEnhancerBySpringCGLIBTT913c63d1.roleTreeList(<generated>)\r\n	at cn.stylefeng.guns.sys.modular.rest.controller.RestRoleController.roleTreeList(RestRoleController.java:137)\r\n	at cn.stylefeng.guns.sys.modular.rest.controller.RestRoleControllerTTFastClassBySpringCGLIBTT486e0036.invoke(<generated>)\r\n	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)\r\n	at org.springframework.aop.framework.CglibAopProxyTCglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:749)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\r\n	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:93)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\r\n	at org.springframework.aop.framework.CglibAopProxyTDynamicAdvisedInterceptor.intercept(CglibAopProxy.java:688)\r\n	at cn.stylefeng.guns.sys.modular.rest.controller.RestRoleControllerTTEnhancerBySpringCGLIBTTdccb1272.roleTreeList(<generated>)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:190)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:138)\r\n	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:104)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:892)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:797)\r\n	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\r\n	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1039)\r\n	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:942)\r\n	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1005)\r\n	at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:908)\r\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:660)\r\n	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:882)\r\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:741)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:103)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at cn.atsoft.dasheng.core.xss.XssFilter.doFilter(XssFilter.java:30)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at com.alibaba.druid.support.http.WebStatFilter.doFilter(WebStatFilter.java:124)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:320)\r\n	at org.springframework.security.web.access.intercept.FilterSecurityInterceptor.invoke(FilterSecurityInterceptor.java:127)\r\n	at org.springframework.security.web.access.intercept.FilterSecurityInterceptor.doFilter(FilterSecurityInterceptor.java:91)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.access.ExceptionTranslationFilter.doFilter(ExceptionTranslationFilter.java:119)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.session.SessionManagementFilter.doFilter(SessionManagementFilter.java:137)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.authentication.AnonymousAuthenticationFilter.doFilter(AnonymousAuthenticationFilter.java:111)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter.doFilter(SecurityContextHolderAwareRequestFilter.java:170)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.savedrequest.RequestCacheAwareFilter.doFilter(RequestCacheAwareFilter.java:63)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at cn.stylefeng.guns.sys.core.auth.filter.JwtAuthorizationTokenFilter.doFilterInternal(JwtAuthorizationTokenFilter.java:78)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.web.filter.CorsFilter.doFilterInternal(CorsFilter.java:96)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.header.HeaderWriterFilter.doFilterInternal(HeaderWriterFilter.java:74)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.context.SecurityContextPersistenceFilter.doFilter(SecurityContextPersistenceFilter.java:105)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter.doFilterInternal(WebAsyncManagerIntegrationFilter.java:56)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\r\n	at org.springframework.security.web.FilterChainProxyTVirtualFilterChain.doFilter(FilterChainProxy.java:334)\r\n	at org.springframework.security.web.FilterChainProxy.doFilterInternal(FilterChainProxy.java:215)\r\n	at org.springframework.security.web.FilterChainProxy.doFilter(FilterChainProxy.java:178)\r\n	at org.springframework.web.filter.DelegatingFilterProxy.invokeDelegate(DelegatingFilterProxy.java:357)\r\n	at org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:270)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:99)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:92)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.HiddenHttpMethodFilter.doFilterInternal(HiddenHttpMethodFilter.java:93)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:200)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:202)\r\n	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:96)\r\n	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:490)\r\n	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:139)\r\n	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)\r\n	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)\r\n	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343)\r\n	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:408)\r\n	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:66)\r\n	at org.apache.coyote.AbstractProtocolTConnectionHandler.process(AbstractProtocol.java:853)\r\n	at org.apache.tomcat.util.net.NioEndpointTSocketProcessor.doRun(NioEndpoint.java:1587)\r\n	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)\r\n	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)\r\n	at java.util.concurrent.ThreadPoolExecutorTWorker.run(ThreadPoolExecutor.java:624)\r\n	at org.apache.tomcat.util.threads.TaskThreadTWrappingRunnable.run(TaskThread.java:61)\r\n	at java.lang.Thread.run(Thread.java:748)\r\nCaused by: java.sql.SQLException: sql injection violation, syntax error: ERROR. pos 77, line 2, column 17, token COMMA : select role_id AS id,role_id AS \"value\", pid as \"parentId\",\n		name as name, ,name as \"label\", (case when (pid = 0 or pid is null) then \'true\'\n		else \'false\' end) as \"open\" from sys_role\r\n	at com.alibaba.druid.wall.WallFilter.checkInternal(WallFilter.java:806)\r\n	at com.alibaba.druid.wall.WallFilter.connection_prepareStatement(WallFilter.java:259)\r\n	at com.alibaba.druid.filter.FilterChainImpl.connection_prepareStatement(FilterChainImpl.java:568)\r\n	at com.alibaba.druid.proxy.jdbc.ConnectionProxyImpl.prepareStatement(ConnectionProxyImpl.java:341)\r\n	at com.alibaba.druid.pool.DruidPooledConnection.prepareStatement(DruidPooledConnection.java:350)\r\n	at sun.reflect.GeneratedMethodAccessor83.invoke(Unknown Source)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at com.atomikos.jdbc.AtomikosConnectionProxy.invoke(AtomikosConnectionProxy.java:141)\r\n	at com.sun.proxy.TProxy182.prepareStatement(Unknown Source)\r\n	at sun.reflect.GeneratedMethodAccessor83.invoke(Unknown Source)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.apache.ibatis.logging.jdbc.ConnectionLogger.invoke(ConnectionLogger.java:55)\r\n	at com.sun.proxy.TProxy178.prepareStatement(Unknown Source)\r\n	at org.apache.ibatis.executor.statement.PreparedStatementHandler.instantiateStatement(PreparedStatementHandler.java:86)\r\n	at org.apache.ibatis.executor.statement.BaseStatementHandler.prepare(BaseStatementHandler.java:88)\r\n	at org.apache.ibatis.executor.statement.RoutingStatementHandler.prepare(RoutingStatementHandler.java:59)\r\n	at sun.reflect.GeneratedMethodAccessor82.invoke(Unknown Source)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.apache.ibatis.plugin.Invocation.proceed(Invocation.java:49)\r\n	at com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor.intercept(PaginationInterceptor.java:186)\r\n	at org.apache.ibatis.plugin.Plugin.invoke(Plugin.java:61)\r\n	at com.sun.proxy.TProxy177.prepare(Unknown Source)\r\n	at sun.reflect.GeneratedMethodAccessor82.invoke(Unknown Source)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.apache.ibatis.plugin.Invocation.proceed(Invocation.java:49)\r\n	at cn.atsoft.dasheng.core.datascope.DataScopeInterceptor.intercept(DataScopeInterceptor.java:52)\r\n	at org.apache.ibatis.plugin.Plugin.invoke(Plugin.java:61)\r\n	at com.sun.proxy.TProxy177.prepare(Unknown Source)\r\n	at com.baomidou.mybatisplus.core.executor.MybatisSimpleExecutor.prepareStatement(MybatisSimpleExecutor.java:92)\r\n	at com.baomidou.mybatisplus.core.executor.MybatisSimpleExecutor.doQuery(MybatisSimpleExecutor.java:66)\r\n	at org.apache.ibatis.executor.BaseExecutor.queryFromDatabase(BaseExecutor.java:324)\r\n	at org.apache.ibatis.executor.BaseExecutor.query(BaseExecutor.java:156)\r\n	at com.baomidou.mybatisplus.core.executor.MybatisCachingExecutor.query(MybatisCachingExecutor.java:155)\r\n	at com.baomidou.mybatisplus.core.executor.MybatisCachingExecutor.query(MybatisCachingExecutor.java:90)\r\n	at sun.reflect.GeneratedMethodAccessor109.invoke(Unknown Source)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.apache.ibatis.plugin.Plugin.invoke(Plugin.java:63)\r\n	at com.sun.proxy.TProxy176.query(Unknown Source)\r\n	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:147)\r\n	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:140)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at cn.atsoft.dasheng.core.mutidatasource.mybatis.OptionalSqlSessionTemplateTSqlSessionInterceptor.invoke(OptionalSqlSessionTemplate.java:335)\r\n	... 117 more\r\nCaused by: com.alibaba.druid.sql.parser.ParserException: ERROR. pos 77, line 2, column 17, token COMMA\r\n	at com.alibaba.druid.sql.parser.SQLExprParser.primary(SQLExprParser.java:882)\r\n	at com.alibaba.druid.sql.dialect.mysql.parser.MySqlExprParser.primary(MySqlExprParser.java:198)\r\n	at com.alibaba.druid.sql.parser.SQLExprParser.expr(SQLExprParser.java:157)\r\n	at com.alibaba.druid.sql.parser.SQLExprParser.parseSelectItem(SQLExprParser.java:3485)\r\n	at com.alibaba.druid.sql.parser.SQLSelectParser.parseSelectList(SQLSelectParser.java:937)\r\n	at com.alibaba.druid.sql.dialect.mysql.parser.MySqlSelectParser.query(MySqlSelectParser.java:181)\r\n	at com.alibaba.druid.sql.parser.SQLSelectParser.query(SQLSelectParser.java:362)\r\n	at com.alibaba.druid.sql.parser.SQLSelectParser.select(SQLSelectParser.java:61)\r\n	at com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser.parseSelect(MySqlStatementParser.java:285)\r\n	at com.alibaba.druid.sql.parser.SQLStatementParser.parseStatementList(SQLStatementParser.java:248)\r\n	at com.alibaba.druid.sql.parser.SQLStatementParser.parseStatementList(SQLStatementParser.java:182)\r\n	at com.alibaba.druid.wall.WallProvider.checkInternal(WallProvider.java:624)\r\n	at com.alibaba.druid.wall.WallProvider.check(WallProvider.java:578)\r\n	at com.alibaba.druid.wall.WallFilter.checkInternal(WallFilter.java:793)\r\n	... 166 more\r\n');
INSERT INTO `sys_operation_log` VALUES (1328246781439500290, '异常日志', '', 1, NULL, NULL, '2020-11-16 16:01:25', '失败', 'RequestEmptyException()\r\n');
INSERT INTO `sys_operation_log` VALUES (1328247611978801153, '业务日志', '修改角色', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestRoleController', 'edit', '2020-11-16 16:04:43', '成功', '角色名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1328247974802874369, '业务日志', '修改角色', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestRoleController', 'edit', '2020-11-16 16:06:10', '成功', '角色名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1328248451607158785, '业务日志', '修改角色', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestRoleController', 'edit', '2020-11-16 16:08:03', '成功', '角色名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1328248934073647106, '业务日志', '修改角色', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestRoleController', 'edit', '2020-11-16 16:09:58', '成功', '角色名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1328249042924224513, '业务日志', '修改角色', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestRoleController', 'edit', '2020-11-16 16:10:24', '成功', '角色名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1328249065187590145, '业务日志', '修改角色', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestRoleController', 'edit', '2020-11-16 16:10:29', '成功', '角色名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1328253617907449857, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-16 16:28:35', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1328253691123220482, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-16 16:28:52', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1328502820311212034, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-17 08:58:49', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1328503124800905218, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-17 09:00:02', '成功', '菜单名称=null;;;');
INSERT INTO `sys_operation_log` VALUES (1328503185974829058, '业务日志', '修改菜单', 1, 'cn.stylefeng.guns.sys.modular.rest.controller.RestMenuController', 'edit', '2020-11-17 09:00:17', '成功', '菜单名称=null;;;');

-- ----------------------------
-- Table structure for sys_position
-- ----------------------------
DROP TABLE IF EXISTS `sys_position`;
CREATE TABLE `sys_position`  (
  `position_id` bigint(20) NOT NULL COMMENT '主键id',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '职位名称',
  `code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '职位编码',
  `sort` int(11) NOT NULL COMMENT '顺序',
  `status` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'ENABLE' COMMENT '状态(字典)',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  PRIMARY KEY (`position_id`) USING BTREE,
  UNIQUE INDEX `CODE_UNI`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '职位表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_position
-- ----------------------------
INSERT INTO `sys_position` VALUES (1, '董事长', 'President', 1, 'ENABLE', NULL, '2019-06-27 18:14:43', 1, NULL, NULL);
INSERT INTO `sys_position` VALUES (2, '总经理', 'GM', 2, 'ENABLE', NULL, '2019-06-27 18:14:43', 1, NULL, NULL);

-- ----------------------------
-- Table structure for sys_relation
-- ----------------------------
DROP TABLE IF EXISTS `sys_relation`;
CREATE TABLE `sys_relation`  (
  `relation_id` bigint(20) NOT NULL COMMENT '主键',
  `menu_id` bigint(20) NULL DEFAULT NULL COMMENT '菜单id',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`relation_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_relation
-- ----------------------------
INSERT INTO `sys_relation` VALUES (1184839164297945090, 105, 5);
INSERT INTO `sys_relation` VALUES (1184839164306333697, 132, 5);
INSERT INTO `sys_relation` VALUES (1184839164310528002, 138, 5);
INSERT INTO `sys_relation` VALUES (1184839164318916609, 139, 5);
INSERT INTO `sys_relation` VALUES (1184839164323110913, 140, 5);
INSERT INTO `sys_relation` VALUES (1184839164327305218, 155, 5);
INSERT INTO `sys_relation` VALUES (1184839164335693826, 156, 5);
INSERT INTO `sys_relation` VALUES (1184839164339888130, 157, 5);
INSERT INTO `sys_relation` VALUES (1184839164344082434, 141, 5);
INSERT INTO `sys_relation` VALUES (1184839164348276737, 142, 5);
INSERT INTO `sys_relation` VALUES (1184839164360859649, 143, 5);
INSERT INTO `sys_relation` VALUES (1184839164365053953, 144, 5);
INSERT INTO `sys_relation` VALUES (1184839164373442561, 171, 5);
INSERT INTO `sys_relation` VALUES (1184839164377636866, 149, 5);
INSERT INTO `sys_relation` VALUES (1184839164381831170, 1127085735660421122, 5);
INSERT INTO `sys_relation` VALUES (1184839164390219778, 172, 5);
INSERT INTO `sys_relation` VALUES (1184839164394414081, 145, 5);
INSERT INTO `sys_relation` VALUES (1184839164398608386, 1111545968697860098, 5);
INSERT INTO `sys_relation` VALUES (1184839164402802690, 1111546189892870145, 5);
INSERT INTO `sys_relation` VALUES (1240625203508195330, 105, 1);
INSERT INTO `sys_relation` VALUES (1240625203520778242, 106, 1);
INSERT INTO `sys_relation` VALUES (1240625203529166850, 107, 1);
INSERT INTO `sys_relation` VALUES (1240625203537555457, 108, 1);
INSERT INTO `sys_relation` VALUES (1240625203545944066, 109, 1);
INSERT INTO `sys_relation` VALUES (1240625203554332673, 110, 1);
INSERT INTO `sys_relation` VALUES (1240625203554332674, 111, 1);
INSERT INTO `sys_relation` VALUES (1240625203566915585, 112, 1);
INSERT INTO `sys_relation` VALUES (1240625203571109889, 113, 1);
INSERT INTO `sys_relation` VALUES (1240625203579498498, 165, 1);
INSERT INTO `sys_relation` VALUES (1240625203583692802, 166, 1);
INSERT INTO `sys_relation` VALUES (1240625203592081410, 167, 1);
INSERT INTO `sys_relation` VALUES (1240625203604664322, 114, 1);
INSERT INTO `sys_relation` VALUES (1240625203613052930, 115, 1);
INSERT INTO `sys_relation` VALUES (1240625203617247233, 116, 1);
INSERT INTO `sys_relation` VALUES (1240625203625635841, 117, 1);
INSERT INTO `sys_relation` VALUES (1240625203634024449, 118, 1);
INSERT INTO `sys_relation` VALUES (1240625203638218753, 162, 1);
INSERT INTO `sys_relation` VALUES (1240625203646607362, 163, 1);
INSERT INTO `sys_relation` VALUES (1240625203654995969, 164, 1);
INSERT INTO `sys_relation` VALUES (1240625203659190273, 119, 1);
INSERT INTO `sys_relation` VALUES (1240625203663384578, 120, 1);
INSERT INTO `sys_relation` VALUES (1240625203671773185, 121, 1);
INSERT INTO `sys_relation` VALUES (1240625203675967489, 122, 1);
INSERT INTO `sys_relation` VALUES (1240625203680161794, 150, 1);
INSERT INTO `sys_relation` VALUES (1240625203688550401, 151, 1);
INSERT INTO `sys_relation` VALUES (1240625203696939009, 128, 1);
INSERT INTO `sys_relation` VALUES (1240625203696939010, 134, 1);
INSERT INTO `sys_relation` VALUES (1240625203705327617, 158, 1);
INSERT INTO `sys_relation` VALUES (1240625203713716226, 159, 1);
INSERT INTO `sys_relation` VALUES (1240625203717910530, 130, 1);
INSERT INTO `sys_relation` VALUES (1240625203726299137, 131, 1);
INSERT INTO `sys_relation` VALUES (1240625203726299138, 135, 1);
INSERT INTO `sys_relation` VALUES (1240625203738882049, 136, 1);
INSERT INTO `sys_relation` VALUES (1240625203738882050, 137, 1);
INSERT INTO `sys_relation` VALUES (1240625203751464962, 152, 1);
INSERT INTO `sys_relation` VALUES (1240625203755659265, 153, 1);
INSERT INTO `sys_relation` VALUES (1240625203764047874, 154, 1);
INSERT INTO `sys_relation` VALUES (1240625203764047875, 132, 1);
INSERT INTO `sys_relation` VALUES (1240625203772436482, 138, 1);
INSERT INTO `sys_relation` VALUES (1240625203780825090, 139, 1);
INSERT INTO `sys_relation` VALUES (1240625203785019394, 140, 1);
INSERT INTO `sys_relation` VALUES (1240625203793408001, 155, 1);
INSERT INTO `sys_relation` VALUES (1240625203797602306, 156, 1);
INSERT INTO `sys_relation` VALUES (1240625203797602307, 157, 1);
INSERT INTO `sys_relation` VALUES (1240625203805990914, 133, 1);
INSERT INTO `sys_relation` VALUES (1240625203814379522, 160, 1);
INSERT INTO `sys_relation` VALUES (1240625203818573826, 161, 1);
INSERT INTO `sys_relation` VALUES (1240625203818573827, 141, 1);
INSERT INTO `sys_relation` VALUES (1240625203831156737, 142, 1);
INSERT INTO `sys_relation` VALUES (1240625203835351042, 143, 1);
INSERT INTO `sys_relation` VALUES (1240625203839545346, 144, 1);
INSERT INTO `sys_relation` VALUES (1240625203847933953, 1144441072852684801, 1);
INSERT INTO `sys_relation` VALUES (1240625203847933954, 1144441072852684802, 1);
INSERT INTO `sys_relation` VALUES (1240625203852128258, 1144441072852684803, 1);
INSERT INTO `sys_relation` VALUES (1240625203860516865, 1144441072852684804, 1);
INSERT INTO `sys_relation` VALUES (1240625203860516866, 171, 1);
INSERT INTO `sys_relation` VALUES (1240625203868905474, 149, 1);
INSERT INTO `sys_relation` VALUES (1240625203877294081, 1127085735660421122, 1);
INSERT INTO `sys_relation` VALUES (1240625203877294082, 1142957882422112257, 1);
INSERT INTO `sys_relation` VALUES (1240625203885682689, 1142957882422112258, 1);
INSERT INTO `sys_relation` VALUES (1240625203889876993, 1142957882422112259, 1);
INSERT INTO `sys_relation` VALUES (1240625203898265602, 1142957882426306562, 1);
INSERT INTO `sys_relation` VALUES (1240625203902459905, 172, 1);
INSERT INTO `sys_relation` VALUES (1240625203902459906, 145, 1);
INSERT INTO `sys_relation` VALUES (1240625203915042817, 1111545968697860098, 1);
INSERT INTO `sys_relation` VALUES (1240625203919237122, 1111546189892870145, 1);
INSERT INTO `sys_relation` VALUES (1240625203923431426, 1149955324929765378, 1);
INSERT INTO `sys_relation` VALUES (1240625203931820034, 1110777136265838594, 1);
INSERT INTO `sys_relation` VALUES (1240625203931820035, 1110787391943098370, 1);
INSERT INTO `sys_relation` VALUES (1240625203940208641, 1110839216310329346, 1);
INSERT INTO `sys_relation` VALUES (1240625203948597250, 1139827152854716418, 1);
INSERT INTO `sys_relation` VALUES (1240625203952791554, 1212299504967204865, 1);
INSERT INTO `sys_relation` VALUES (1240625203952791555, 1110777366856089602, 1);
INSERT INTO `sys_relation` VALUES (1240625203965374466, 1110777491464667137, 1);
INSERT INTO `sys_relation` VALUES (1240625203965374467, 1212299802154614786, 1);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(20) NOT NULL COMMENT '主键id',
  `pid` bigint(20) NULL DEFAULT NULL COMMENT '父角色id',
  `pids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提示',
  `sort` int(11) NULL DEFAULT NULL COMMENT '序号',
  `version` int(11) NULL DEFAULT NULL COMMENT '乐观锁',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建用户',
  `update_user` bigint(20) NULL DEFAULT NULL COMMENT '修改用户',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 0, '0,', '超级管理员', 'administrator', 1, 1, NULL, '2020-11-16 16:10:24', NULL, 1);
INSERT INTO `sys_role` VALUES (5, 1, '0,1,', '第三方登录', 'oauth_role', 2, NULL, NULL, '2020-11-16 16:10:29', NULL, 1);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(20) NOT NULL COMMENT '主键id',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `account` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'md5密码盐',
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名字',
  `birthday` datetime(0) NULL DEFAULT NULL COMMENT '生日',
  `sex` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别(字典)',
  `email` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电子邮件',
  `phone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `role_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色id(多个逗号隔开)',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门id(多个逗号隔开)',
  `status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态(字典)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_user` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `version` int(11) NULL DEFAULT NULL COMMENT '乐观锁',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '管理员表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '1124606971782160385', 'admin', '17db03c22596b7609c7c9704f16663e0', 'abcdef', 'stylefeng', '2018-11-16 00:00:00', 'M', 'sn93@qq.com', '18200000000', '1', 25, 'ENABLE', '2016-01-29 08:49:53', NULL, '2019-06-28 14:38:19', 24, 25);

-- ----------------------------
-- Table structure for sys_user_pos
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_pos`;
CREATE TABLE `sys_user_pos`  (
  `user_pos_id` bigint(20) NOT NULL COMMENT '主键id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `pos_id` bigint(20) NOT NULL COMMENT '职位id',
  PRIMARY KEY (`user_pos_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户职位关联表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_user_pos
-- ----------------------------
INSERT INTO `sys_user_pos` VALUES (1144495219551617025, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
