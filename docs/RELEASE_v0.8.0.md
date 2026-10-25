# v0.8.0 发布说明（beta）

## 这是什么版本

这是改版 fork 的第一个带版本号的版本：`versionCode` 800、
`versionName` "0.8.0"，基线为上游 `wuwei-crg/AutoLXB` master
`894b9ee`（v0.7.2）+ 改版第一批（`redesign/material3` 提交
094ca44）+ 第二批（本补丁）。`lxb-core` 未动。

改动总览见仓库根 `CHANGELOG.md` v0.8.0 一节，规范见
`docs/PRODUCT_SPEC.md`。

## 验证状态（实话实说）

- 已验证：纯 Kotlin 逻辑用独立 kotlinc + JUnit 跑通——第一批
  `UserCopyTest` 4/4，第二批 `FormValidationTest` 7/7；文本级
  grep 核查通过（删除调用点、空状态按钮、表单错误文案、
  versionCode/versionName，见第二批总结）。
- **未验证：没有跑过 `./gradlew` 编译，没有真机验证。**
  原因：改版机器的 Gradle daemon 协议在沙箱里连不上，连一个空
  项目也会报“收不到 daemon 的消息”；这不是项目代码的问题，
  但结果就是编译背书只能由维护者本机补上。
- 因此本版定性为 **beta**：可以先在测试机上装、走主线，但不要
  直接当作正式版发给他人使用。

## 在本机打 APK（维护者操作）

以下步骤只能在维护者本机（daemon 正常的机器）做，改版机器
做不了，连接器也没有创建 GitHub Release 的工具，所以 Release
也只能由维护者在 GitHub 网页上建：

1. 取 `redesign/material3` 分支的最新代码（已含第一批 + 第二批）。
2. 先跑纯逻辑测试（可选，kotlinc + JUnit 方式即可，重点是
   `UserCopyTest` 4/4、`FormValidationTest` 7/7）。
3. 跑 `./gradlew assembleDebug`，必须通过；若报个别 Material
   图标不存在，优先替换为同组 core 图标，不要为此引入
   `material-icons-extended`。
4. 真机走主线并逐项确认：
   - 配模型 → 启动核心 → 快速任务 → 存模板 → 定时工作流；
   - 模板/工作流/模型配置删除弹框（名称、不可恢复提示、红色确认、
     取消无动作）；
   - 模板/工作流空状态的下一步按钮、表单首次保存后的就近错误提示
     与编辑时实时更新；
   - 便携包导入/导出后页顶提示条可关闭、且不会被 Toast 盖掉；
   - 运行中任务文案不再出现 APP_RESOLVE / SCRIPT_ACT / VISION_ACT /
     TASK_DECOMPOSE / task-map 字样。
5. 上述全部通过后，在 GitHub 上打开 `lbjxr/AutoLXB` → Releases →
   Draft a new release：tag 填 `v0.8.0`（目标分支选
   `redesign/material3` 或合并后的目标分支），标题写
   `v0.8.0 (beta)`，正文贴 `CHANGELOG.md` 的 v0.8.0 一节并保留
   “未经过真机验证”这句——如果第 4 步已在本机通过，把这句改为
   “已在本机编译 + 真机主线验证通过”，再上传第 3 步打出的 APK，
   然后 Publish。
6. 若第 3 步或第 4 步任何一项失败：不要发 Release，把报错贴回
   改版分支修，修完重新从第 3 步走。

## 下一步

v0.9.0 计划：全量 `tr()` → `strings.xml` 迁移、NavHost 返回栈重构，
前置 gate 与上面第 3、4 步相同（编译 + 真机主线通过），gate 不过不开工。
