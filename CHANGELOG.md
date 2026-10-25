# Changelog

本仓库是 `wuwei-crg/AutoLXB` 的改版 fork，改版只动 App UI 层，
`lxb-core` 执行核心（Route-Then-Act、任务路线、视觉执行、API 协议）
在以下版本中均未改动。

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
