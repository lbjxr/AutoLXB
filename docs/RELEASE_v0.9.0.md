# v0.9.0 发布说明（beta）

## 这是什么版本

这是改版 fork 的第二个带版本号的版本：`versionCode` 900、
`versionName` "0.9.0"，基线为上游 `wuwei-crg/AutoLXB` master
`894b9ee`（v0.7.2）+ v0.8.0 的两批 + v0.9.0 开发中的五批
（均在 `redesign/material3` 分支上）。`lxb-core` 未动。

改动总览见仓库根 `CHANGELOG.md` v0.9.0 一节，规范见
`docs/PRODUCT_SPEC.md`（§10–§14 为 v0.9.0 各批范围）。

## 验证状态（实话实说）

- 已验证：纯 Kotlin 逻辑用独立 kotlinc + JUnit 跑通——
  `UserCopyTest` 5/5、`FormValidationTest` 7/7、
  `TaskNavStackTest` 8/8、`TopNavRoutesTest` 4/4，合计 24/24；
  文本级 grep 核查通过（顶层 `selectedTab` 赋值残留 0 处、
  navigation-compose 依赖已声明且无重复、文案覆盖缺失 0）。
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

1. 取 `redesign/material3` 分支的最新代码（已含 v0.9.0 五批）。
2. 先跑纯逻辑测试（可选，kotlinc + JUnit 方式即可，重点是
   上面 24 个测试全部通过）。
3. 跑 `./gradlew assembleDebug`，必须通过；若报 navigation
   相关解析失败，先确认版本目录里的
   `navigation-compose:2.8.0` 能从 Maven 仓库取到，不要顺手
   升到 navigation 大版本（它要求更新的 Compose/Kotlin）。
   若报个别 Material 图标不存在，优先替换为同组 core 图标，
   不要为此引入 `material-icons-extended`。
4. 真机走主线并逐项确认：
   - 配模型 → 启动核心 → 快速任务 → 存模板 → 定时工作流；
   - 底部 4 个 Tab 切换正常；从 Control / Tasks 里点“打开配置”
     能跳到 Config；切 Tab 再切回来，各 Tab 内部状态还在
     （例如 Tasks 正停在模板列表、Config 正停在模型页）；
   - 系统返回键：在 Tasks / Config / Logs 按返回回到 Control，
     在 Control 按返回退出应用；各 Tab 内部的返回仍走原来的
     页面栈（例如模板表单返回到模板列表，而不是直接回首页）；
   - 模板/工作流/模型配置删除弹框（名称、不可恢复提示、红色确认、
     取消无动作）；模板/工作流空状态的下一步按钮；表单首次保存
     后的就近错误提示与编辑时实时更新；表单顶部的“有未保存的
     更改”提示只在字段与进入时快照不同时出现、保存后消失；
   - 便携包导入/导出后页顶提示条可关闭；系统设置打开失败时
     弹出 Snackbar 且“重试”真的重新打开系统设置，同一失败
     第二次仍会弹出；通知权限被拒的 Snackbar 无动作按钮；
   - 运行中任务文案不再出现 APP_RESOLVE / SCRIPT_ACT /
     VISION_ACT / TASK_DECOMPOSE / task-map 字样。
5. 上述全部通过后，在 GitHub 上打开 `lbjxr/AutoLXB` → Releases →
   Draft a new release：tag 填 `v0.9.0`（目标分支选
   `release/v0.9.0` 冻结分支），标题写 `v0.9.0 (beta)`，正文贴
   `CHANGELOG.md` 的 v0.9.0 一节并保留“未经过真机验证”这句——
   如果第 4 步已在本机通过，把这句改为“已在本机编译 + 真机
   主线验证通过”，再上传第 3 步打出的 APK，然后 Publish。
6. 若第 3 步或第 4 步任何一项失败：不要发 Release，把报错贴回
   改版分支修，修完重新从第 3 步走。

## 下一步

- 各 Tab 内部页面迁入 NavHost 目的地、其余小组件抽取
  （`PageHeaderBlock` / `SurfacePanel` 等）、全量 `tr()` →
  `stringResource` 迁移，仍列为后续项；`tr()` 迁移的前提是
  解决应用语言（配置页可选）与系统语言不完全等同的问题，
  在此之前只做覆盖补齐，不迁移。
- 草稿持久化不引入（第二批已定案：返回不丢数据，见
  `docs/PRODUCT_SPEC.md` §11）。
