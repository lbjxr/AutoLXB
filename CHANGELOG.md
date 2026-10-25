# Changelog

本仓库是 `wuwei-crg/AutoLXB` 的改版 fork，改版只动 App UI 层，
`lxb-core` 执行核心（Route-Then-Act、任务路线、视觉执行、API 协议）
在以下版本中均未改动。

## v0.9.0（开发中，beta）

> **验证状态（请先读）：本版未经过 `./gradlew` 编译与真机验证。**
> 改版机器的 Gradle daemon 协议在沙箱里连不上（连空项目也会失败），
> 因此编译与真机验证只能在维护者本机做。合并与发正式版前，必须在
> 本机跑通 `./gradlew assembleDebug` 并走一遍“配模型 → 启动核心 →
> 快速任务 → 存模板 → 定时工作流”主线，重点验证返回栈（见下）。

### 第一批（本版新增）

- Tasks 返回栈：新增可测试的纯逻辑类 `ui/navigation/TaskNavStack.kt`
  （压栈/弹栈/回首页/按页回退/重复压入保护），TasksTab 内全部
  `page = …` 直接赋值改为压栈/弹栈/按页回退，顶部返回与 Android
  系统返回键（BackHandler）统一走弹栈；栈底（任务首页）时不拦截，
  系统返回键保持原有行为。行为上除“返回回到上一页而非直接首页”
  外无其他变化；完整的 NavHost 重构仍未做（见“明确未做”）。
- 文案覆盖补齐：新增 `docs/check_tr_coverage.py`，抽取
  MainActivity.kt 全部 `tr("…")` 字面量并与 ZhMap 逐一对照，补齐
  之前中文用户会看到英文原文的 16 项（13 项字面量 + 触发器类型
  `None`/`Schedule` + 任务状态 `PENDING`），补齐后字面量与已登记
  的动态键缺失数均为 0。已有翻译一律未动；`tr()`/ZhMap 机制本身
  未动，没有迁移到 `stringResource`（应用语言可在配置页改，与系统
  语言不完全等同，迁移会破坏语言选择）。
- 版本：`versionCode` 800 → 900，`versionName` "0.8.0" → "0.9.0"。

### 第二批（本版新增）

- Control 与 Config 返回栈：复用第一批的 `TaskNavStack` 纯逻辑类
  （新增通用别名 `AppNavStack`，Tasks 代码未动）；Control（总览 →
  无线引导 / Root 引导）与 Config（总览 → 设备核心 / 模型 / 解锁
  策略 / 地图同步）的 `page` 整数改为压栈/弹栈，顶部返回与系统
  返回键统一走弹栈，总览时不拦截。
- 表单未保存决策：返回**不弹**二次确认框——表单字段存在
  ViewModel 的 StateFlow 里，返回列表不会清空它们（只有“新建”
  重置、打开另一项加载时才会覆盖），弹框会误导用户以为返回会
  丢数据。改为纯展示的未保存提示：进入表单时在本地拍字段快照，
  与当前字段不同时在表单顶部显示“有未保存的更改”，并说明返回
  后仍可继续编辑、点保存后生效；不改保存逻辑，不引入草稿持久化。
- 错误文案扩展：`UserCopy` 新增 9 条高频失败映射（服务未运行、
  核心服务未运行、模型规划调用失败、无线启动失败、任务提交失败、
  配置同步失败、视觉输出解析失败、视觉动作输出无效、重复无效
  动作），格式为“发生了什么 + 能怎么办”，全部取自代码里真实
  出现的文案，配 JUnit 测试。
- 无障碍审计：MainActivity 全部 4 处 `Icon` 逐一核查后 0 处需要改
  （底部 Tab、两处“打开”右箭头、`GlyphBadge` 均伴随可见文字，
  `contentDescription = null` 是正确的装饰性用法）；审计标准与
  清单见 `docs/PRODUCT_SPEC.md` §11。
- 版本保持 0.9.0 / versionCode 900 不变，本批不发版。

### 第三批（本版新增）

- 全 App 最后一批 Toast 消除：MainActivity 类里仅剩的 3 处
  Toast（通知权限被拒、打开开发者选项失败、打开无线调试设置
  失败）改为走 `LXBIgnitionApp` 已有的 SnackbarHost，事件经
  `MutableSharedFlow` 一次性传递（不用裸字符串 StateFlow，避免
  同一失败第二次不重播）；两条系统设置失败带“重试”动作按钮，
  点动作重新打开对应系统设置，通知权限那条不带动作、文案写清
  到系统设置里手动开启。文案走 `tr()` / ZhMap，格式为“发生了
  什么 + 能怎么办”。打开系统设置本身的逻辑与 ViewModel 未动。
- 版本保持 0.9.0 / versionCode 900 不变，本批不发版。

### 第四批（本版新增，本批只做小组件抽取）

- 语言基础设施先行移动：MainActivity.kt 里私有的 `UiI18n` 类、
  `LocalUiI18n`、顶层 `tr()` / `ut()` 与 `ZhMap` 对照表原样移到
  新文件 `ui/i18n/UiI18n.kt`（公开）；MainActivity 只留同名薄
  wrapper（`tr()` / `ut()` 委托，`UiI18n` 为类型别名、
  `LocalUiI18n` 为同名转发值），现有调用点未改。语言机制未变，
  没有迁移到 `stringResource`。
- 小组件抽取：`DeleteConfirmDialog` 与 `EmptyStateBox` 抽到
  `ui/components/UiComponents.kt`，签名与行为不变；`GlyphBadge`
  因 `EmptyStateBox` 依赖它而一并移过去（连同它独占的
  `glyphToIcon`），没有复制两份。调用点只改导入/引用。
  `PageHeaderBlock`、`SurfacePanel` 等其他组件本批不抽。
- 移动核对：`ZhMap` 条目行数移动前后一致（660 行、659 个唯一键，
  1 个重复键原样保留）；已有翻译一字未动；覆盖脚本已同步改读
  新文件，复核缺失仍为 0；MainActivity 里旧定义残留 0 处；
  调用点数量一致（`DeleteConfirmDialog` 4 处、`EmptyStateBox`
  4 处、`GlyphBadge` 18 处）；删了移动后仅剩的 3 个未用导入。
- 版本保持 0.9.0 / versionCode 900 不变，本批不发版。

### 第五批（本版新增，本批只做顶层 NavHost + 发版文档）

- 顶层 4 个 Tab 接 NavHost：`LXBIgnitionApp` 里驱动底部栏的
  `selectedTab` Int 状态，换成 `NavController` + `NavHost`，4 条
  路由为 control / tasks / config / logs（路由常量与 tab↔route
  纯映射抽到 `ui/navigation/TopNavRoutes.kt`，配 JUnit 测试）。
  底部栏选中态从 `currentBackStackEntryAsState` 取；点 Tab 时用
  标准底部导航写法（`popUpTo(CONTROL)` + `saveState` /
  `launchSingleTop` / `restoreState`）；Control 与 Tasks 里“打开
  配置”的入口改为导航到 config 路由，不再写 `selectedTab = 2`。
  各 Tab 内部的页面返回栈（第三、四批）一律未动。
- 行为变化（有意的，不是回归）：系统返回键在非首页 Tab 时，
  现在按 NavHost 返回栈回到 Control（Tab 切换时先回到起点再
  入栈，栈里最多只有起点 + 当前 Tab）；从 Control 按返回仍
  直接退出应用，与以前一致。切 Tab 后各 Tab 内部状态由
  `saveState` / `restoreState` 保留。
- 依赖：新增 `androidx.navigation:navigation-compose:2.8.0`
  （版本目录统一管理）。选型：2.8.0 是 2024 年 9 月同期版本，
  与 Compose BOM 2024.09.00（Compose 1.7）与 Kotlin 2.0.21
  同代；更新的 navigation 大版本要求更新的 Compose/Kotlin，
  不在本批引入。
- 发版文档：新增 `docs/RELEASE_v0.9.0.md`（照 v0.8.0 那份的
  格式：本机打 APK + GitHub 网页建 Release 的步骤、未验证声明）。
- 版本保持 0.9.0 / versionCode 900 不变；本批之后由维护者把
  `release/v0.9.0` 分支从本批提交上冻结。

### 第六批（编译修复；此前三批代码未经编译，本批把手动编译管线发现的 3 处错误修进仓库）

- `MainActivity.kt`：`LXBIgnitionApp` 的
  `activitySnackbarEvents` 参数默认值由 `emptyFlow()` 改为
  `MutableSharedFlow()`——`emptyFlow()` 返回 `Flow`，不能赋给
  `SharedFlow` 参数，是类型错误；同时删掉因此无用的
  `emptyFlow` 导入。行为不变（默认仍不产生任何事件，唯一
  调用点本来就传入 Activity 自有的事件流）。
- `ui/i18n/UiI18n.kt`：`"Semantic descriptor" to "语义描述"`
  行尾补逗号（此前缺逗号，是语法错误）。
- `ui/components/UiComponents.kt`：`glyphToIcon` 里 "⏰" 的
  映射由 `Icons.Filled.Schedule` 改为 `Icons.Filled.Alarm`——
  Schedule 只在 material-icons-extended 里，不在已声明的
  material-icons-core 里；Alarm 在 core 里，且与 ⏰（闹钟）
  的语义相符，不引入 extended 依赖（可省约 35MB 包体）。
- 版本保持 0.9.0 / versionCode 900 不变；本批之后用手动
  编译管线（aapt2 + kotlinc + d8 + apksigner）从仓库干净
  源码重打 debug APK 验证。

### 明确未做（本批有意不做，留待后续决策/验证）

- 顶层 NavHost 已于第五批落地（只覆盖底部 4 个 Tab；各 Tab
  内部仍是第三、四批的状态栈，不是 NavHost 目的地，见
  `docs/PRODUCT_SPEC.md` §14）。
- 表单未保存内容的草稿保存：第二批已定案——返回不弹二次确认
  框（字段在 ViewModel 中，返回不丢弃），只做纯展示的未保存
  提示；草稿持久化仍不引入，见 `docs/PRODUCT_SPEC.md` §11。
- `DeleteConfirmDialog` / `EmptyStateBox` 抽到 `ui/components/`：
  已于第四批用“先移语言基础设施、再抽组件”的两步法完成，
  见上；`PageHeaderBlock` / `SurfacePanel` 等其他组件仍未抽。
- 全量 `tr()` → `strings.xml` 迁移：按规范与语言选择的约束，本批
  只做覆盖补齐，不迁移（见 PRODUCT_SPEC.md §10）。

## v0.8.0（beta）

> **验证状态（请先读）：本版未经过 `./gradlew` 编译与真机验证。**
> 改版机器的 Gradle daemon 协议在沙箱里连不上（连空项目也会失败），
> 因此编译与真机验证只能在维护者本机做。合并与发正式版前，必须在
> 本机跑通 `./gradlew assembleDebug` 并走一遍“配模型 → 启动核心 →
> 快速任务 → 存模板 → 定时工作流”主线。见 `docs/RELEASE_v0.8.0.md`。

### 第一批（已随 `redesign/material3` 提交 094ca44 落地）

- 规范落库：`docs/PRODUCT_SPEC.md`（Material 3 + 常见中文 App 通用规范）。
- 图标与无障碍：新增 `material-icons-core` 依赖；底部 4 Tab 与各页
  文字符号（⌂/✓/⚙/≡/☰/▣/↳）换成 Material Icon 并补
  `contentDescription`；`PageActionButton` 与模板行按钮高度由 32dp 改为 48dp。
- 删除二次确认：模板行、工作流行删除先弹 `DeleteConfirmDialog`
  （写清名称、永久删除不可恢复，确认按钮为错误色）；路线编辑器步骤
  移除的确认放在“手动保存路线”动作上，且只在确实移除了步骤时弹出。
- 用户文案：新增纯函数 `ui/copy/UserCopy.kt`，把 APP_RESOLVE /
  SCRIPT_ACT / VISION_ACT / TASK_DECOMPOSE / task-map 译为中英用户语言，
  接线于任务运行状态卡、应用日志卡、追踪摘要与 `UiMessageLocalizer` 入口。
- 页面常量：Control / Tasks / Config 三处的页码裸数字抽为顶层
  `*_PAGE_*` 常量，数值与行为不变。

### 第二批（本版新增）

- 删除二次确认补齐：模型配置卡内的“删除配置”复用
  `DeleteConfirmDialog`，写清配置名称与“永久删除、无法恢复”，
  取消无动作；删除逻辑本身未改。
- 空状态下一步：`EmptyStateBox` 增加可选的下一步按钮——模板空
  →“新建模板”、工作流空 →“新建工作流”、工作流步骤空 →“添加模板”、
  近期运行空 →“提交快速任务”。
- 表单即时校验：新增纯函数 `ui/validation/FormValidation.kt` 及
  JUnit 测试；模板（名称、任务描述、目标应用必填）与工作流
  （名称必填、至少一个步骤）在首次点保存后就近显示中文错误，
  此后编辑字段时错误实时更新。
- 关键结果持久提示：便携包导入/导出结果由 Toast 改为 Tasks 各页顶部
  的、可关闭的页内提示条，直到用户关闭或下一次便携操作覆盖。
- 版本：`versionCode` 702 → 800，`versionName` "0.7.2" → "0.8.0"。

### 明确未做（留待 v0.9.0，以编译 + 真机验证通过为 gate）

- 全量 `tr()` → `strings.xml` 迁移（含 `values-zh`）。
- 全量 NavHost 返回栈重构。
- 工作流表单内步骤移除不加确认框（决策：未保存的表单内操作，
  删后可重新添加模板恢复，不属破坏性操作；见 PRODUCT_SPEC.md §8）。
