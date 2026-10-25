# AutoLXB 产品规范（Material 3 + 常见中文 App 通用规范）

依据：2026-10-25 由维护者确认，以 Material 3 + 常见中文 App 通用规范为准。
范围：只重做 App UI 层，不动 `lxb-core` 执行核心（Route-Then-Act、任务路线、
视觉执行逻辑、API 协议不变），不删除任何现有功能入口。
上游：`wuwei-crg/AutoLXB`，版本 0.7.2，`master` 分支。

## 1. 现状问题（已实测）

- 导航：底部 4 Tab（Control / Tasks / Config / Logs），但 Tasks 内用
  `page = 0~7` 整数硬切 8 个子页，无返回栈，各页 Back 手写回首页，易丢状态。
- 图标与无障碍：全 App 无 `contentDescription`；图标是文字符号
  （⌂、✓、⚙、≡、☰、▣、↳），机型渲染不一致，TalkBack 读不出来。
- 文案体系：`res/values/strings.xml` 仅有 `app_name`；界面 497 处
  `tr("英文原文")` 查中文对照表，查不到就漏英文；用户可见提示漏出
  APP_RESOLVE、SCRIPT_ACT、VISION_ACT、TASK_DECOMPOSE 等内部术语。
- 操作反馈：模板 / 工作流行“删除”一点即删，无二次确认；重要结果多走
  Toast，消失即无从追溯；Control 页把首次引导、核心启动、功能介绍、
  模型配置入口混在一页。

## 2. 导航规范

- 底部导航只放一级入口，不超过 5 个，当前选中态用 Material 3 的选中指示
  + 图标 + 文案三者同时表达，不只靠颜色。
- 二级及以下页面走标准返回栈（NavHost / 系统返回键统一），每页左上角返回，
  返回后恢复上一页滚动位置与表单草稿，不用整数 `page` 手写跳转。
- 一级页面结构固定为：首页（启动与状态）/ 任务 / 配置 / 日志；模板、工作流、
  路线编辑器是任务下的二级页，不再与首页平级混排。
- 首次使用走一条主线：配模型 → 启动核心 → 创建第一个任务；每步只有一个
  主按钮，次要入口收进“更多”，不在首屏堆全部功能。

## 3. 元素与视觉规范

- 图标一律用 Material Symbols / Icons，不用 emoji 与文字符号做功能图标；
  每个可点击元素都有 `contentDescription` 或伴随的可见语义文案，装饰性
  图标标记为无障碍忽略（`contentDescription = null`）。
- 触摸目标不小于 48dp；正文不小于 14sp，次要信息不小于 12sp；颜色只用
  主题色板，不在业务代码里写死十六进制。
- 状态只用三档表达：进行中（进度 + 可取消）、成功（结果 + 下一步）、
  失败（原因 + 重试入口）；不用“Idle / FAILED / ! / ✓”这类裸符号当状态。
- 卡片、列表、表单、弹窗、空状态各只保留一套组件，禁止每页各写一套
  相似样式。

## 4. 文案与提示规范

- 文案全部进 `strings.xml`（中英两套），删除运行时英文原文查表的做法；
  缺翻译时宁可显示中文兜底，不漏英文原文。
- 中文文案规则：动词 + 对象（“新建模板”“删除工作流”），不说
  “模板列表查询失败”这类开发口径；报错格式固定为“发生了什么 + 能怎么办”，
  例如“保存失败，请检查网络后重试”，并给出重试按钮。
- 内部术语一律翻译成用户语言：任务路线（不说 task-map / SCRIPT_ACT）、
  视觉执行（不说 VISION_ACT）、拆解步骤（不说 TASK_DECOMPOSE）；
  技术细节只在日志详情里出现。
- 表单校验在输入时即时提示，不等到点保存才报错；必填项、格式、时间在
  过去等规则在字段下方就近说明。

## 5. 操作反馈与安全规范

- 破坏性操作（删除模板 / 工作流 / 任务路线）必须二次确认，确认框写清
  删的是哪一项、是否可恢复，确认按钮用红色警示样式。
- 可逆操作（保存、刷新、导入）用 Snackbar 反馈并可撤销或查看结果；
  不可逆且重要的结果进日志详情，不只靠 Toast。
- 长耗时操作（启动核心、配对、任务执行）全程显示进度与当前步骤，允许
  取消，取消后明确告知“已在下一个安全点停止”，不出现无响应的假死。
- 空状态必须给下一步：无模板时给“新建模板”按钮，无运行记录时说明
  “先跑一次快速任务”，不只写“No templates yet.”。

## 6. 第一批范围（已落地，见改版总结）

- 规范本文档落入仓库。
- 图标与无障碍：新增 `material-icons-core` 依赖（Compose BOM 2024.09.00
  管理版本）；底部 4 Tab 与 `GlyphBadge` 改用 Material Icon；文字符号
  只保留两类例外——数字步骤徽标（可读文本，不是图标）与“•”装饰圆点；
  `PageActionButton` 与模板行按钮的显式高度由 32dp 改为 48dp。
- 删除二次确认：模板行、工作流行删除先弹 `DeleteConfirmDialog`（写清
  名称、永久删除不可恢复，确认按钮为错误色）；路线编辑器里步骤的移除
  在保存前仍可恢复，因此确认框放在“手动保存路线”动作上，且只在确实
  移除了步骤时弹出。删除本身的调用逻辑未改。
- 用户文案：新增 `ui/copy/UserCopy.kt`（纯函数，无 Android 依赖），在
  任务运行状态卡、应用日志卡、追踪摘要与 `UiMessageLocalizer` 入口处把
  APP_RESOLVE / SCRIPT_ACT / VISION_ACT / TASK_DECOMPOSE / task-map 译为
  中英用户语言；`strings.xml` 只为新增确认框补了英文原文条目，497 处
  `tr()` 的全量迁移留待下一批。
- 页面常量：Control / Tasks / Config 三处的页码裸数字抽为顶层
  `*_PAGE_*` 常量，数值与 Int 状态类型不变，行为不变。

## 7. 第二批范围（已落地，v0.8.0）

- 删除二次确认补齐：模型配置卡内的“删除配置”（`deleteLlmProfile`）
  复用第一批的 `DeleteConfirmDialog`，确认框写清配置名称与“永久删除、
  无法恢复”，取消无动作；删除本身的调用逻辑未改。
- 空状态下一步：`EmptyStateBox` 增加可选的 `actionLabel` + `onAction`
  （默认空，旧调用点不受影响），按钮为 Material 3 规范样式、触摸目标
  不小于 48dp。接线：模板空 →“新建模板”、工作流空 →“新建工作流”、
  工作流步骤空 →“添加模板”（进模板选择页）、近期运行空 →“提交快速
  任务”（回任务首页）。
- 表单即时校验：新增纯函数 `ui/validation/FormValidation.kt`（模板：
  名称、任务描述、目标应用必填；工作流：名称必填、至少一个步骤），
  配 JUnit 测试。UI 上首次点保存后若有错，在对应字段下方就近显示
  中文错误文案（走现有 `tr()` / ZhMap）；此后改动字段时错误实时更新。
  保存本身的 ViewModel 校验未动，表单层只是提前拦截了明显无效的提交。
- 关键结果持久提示：便携包导入/导出结果不再用 Toast（消失即无从追溯），
  改为 Tasks 各页顶部的、可关闭的页内提示条，直到用户关闭或下一次
  便携操作覆盖。未动 ViewModel，也未为此大改 `LXBIgnitionApp` 的
  Scaffold / SnackbarHost 结构。
- 版本：`versionCode` 702 → 800、`versionName` "0.7.2" → "0.8.0"
  （上游编码规则：702 对应 0.7.2）。

## 8. 决策记录（第二批定案，不再反复）

- 工作流表单内步骤移除**不加确认框**：这是未保存的表单内操作，删后
  可重新添加模板恢复，不是破坏性操作；对可逆操作弹框只会造成弹框疲劳。
- 字符串系统**不动**：新增文案仍走现有 `tr()` / ZhMap，不迁入
  `stringResource`。原因是应用内 `uiLang` 可在配置里改，与系统语言
  不完全等同；第一批 `strings.xml` 里的 5 条英文条目只是英文原文存档，
  中文仍在 ZhMap，全量迁移是 v0.9.0 的事，不要在本批“纠正”它。
- 导入提示不用 Snackbar：为接 SnackbarHost 而大改顶层 Scaffold 结构
  风险高于收益；页内可关闭提示条已满足“结果可追溯”这一条规范。

## 9. v0.9.0 路线与前置 gate

以下两项明确放在 v0.9.0，**本批不做**，理由是这两项没有编译器兜底时
强上会把一个可发布的版本置于风险中：

- 全量 `tr()` → `strings.xml` 迁移（含 `values-zh` 两套文案）。
- 全量 NavHost 返回栈重构（替代 Tasks 内整数 `page` 手写跳转）。

前置 gate（缺一不可）：在 daemon 正常的机器上 `./gradlew assembleDebug`
通过；真机走“配模型 → 启动核心 → 快速任务 → 存模板 → 定时工作流”
主线通过。只有 gate 通过后，才在 v0.9.0 里动这两项。

## 10. v0.9.0 第一批范围（已落地，开发中 beta）

- Tasks 返回栈（可验证方案，非完整 NavHost）：新增纯逻辑
  `ui/navigation/TaskNavStack.kt`（压栈、弹栈、回首页、按页回退、
  重复压入同一页的保护）配 JUnit 测试；TasksTab 内全部
  `page = …` 直接赋值改为压栈/弹栈/按页回退，`PageHeaderBlock`
  的返回与系统返回键（BackHandler）统一走弹栈，栈底（任务首页）
  时不拦截、交还系统。例外与取舍：模板表单从工作流步骤打开时，
  返回按栈回到工作流表单（旧行为是硬编码回模板列表，这是栈语义
  本身带来的改进）；路线编辑器的旧“非模板来源回首页”分支已被
  弹栈取代（当前代码中编辑器只从模板入口打开，该分支不可达）；
  模板/工作流表单保存成功后按页回退到列表（列表不在栈上时重置
  为首页 → 列表，保存后返回不再回到刚保存的表单）。
- 文案覆盖补齐（不是迁移）：`docs/check_tr_coverage.py` 抽取
  MainActivity.kt 全部 `tr("…")` 字面量并与 ZhMap 对照，补齐 16 项
  （13 项字面量 + 触发器类型 `None`/`Schedule` + 任务状态
  `PENDING`），补齐后缺失数为 0。`tr()`/ZhMap 机制本身未动，已有
  翻译未动；不迁移到 `stringResource`，因为应用语言 `uiLang` 可在
  配置页改，与系统语言不完全等同，迁移会破坏用户的语言选择。
- 版本：`versionCode` 800 → 900、`versionName` "0.8.0" → "0.9.0"。
- 本批有意未做：表单未保存内容的草稿保存与“未保存离开”二次确认
  （返回时表单内容仍按原有行为处理，是否加是下一批决策点）；
  `DeleteConfirmDialog` / `EmptyStateBox` 抽到 `ui/components/`（两者
  依赖 MainActivity 私有的 `tr()` / `LocalUiI18n` / `GlyphBadge`，
  抽取必须改签名或复制语言 plumbing，待有编译 gate 的批次再做）；
  完整 NavHost 重构（仍列为后续项，见 §9）。

## 11. v0.9.0 第二批范围（已落地，开发中 beta）

- Control 与 Config 返回栈：复用第一批的纯逻辑栈类
  `ui/navigation/TaskNavStack.kt`（新增通用别名 `AppNavStack`，Tasks
  的 `TaskNavStack` 名称与已验证代码一律未动）。ControlTab
  （总览 → 无线引导 / Root 引导）与 ConfigTab（总览 → 设备核心 /
  模型 / 解锁策略 / 地图同步）的 `page` 整数改为压栈/弹栈，顶部
  返回与系统返回键（BackHandler）统一走弹栈，总览时不拦截。
  两处都是深度 1，没有链式跳转，不做过度设计；行为上除“返回
  回到上一页”外无其他变化。
- 表单未保存决策（本批定案，不再反复）：**返回不弹二次确认框**。
  核实的关键事实是模板/工作流表单字段存在 ViewModel 的
  StateFlow 里（`templateName` 等），返回列表本身不会清空它们，
  只有“新建”时 `reset*Form()`、打开另一项时 `load*Form()` 才会
  覆盖。因此“返回即丢弃”这个前提不成立，弹框只会误导用户
  以为返回会丢数据。最小实现是纯展示的未保存提示：进入表单页
  时在 TasksTab 本地状态里拍一张字段快照（不改 ViewModel），
  快照与当前字段不同时在表单顶部显示“有未保存的更改。返回后
  仍可继续编辑，点保存后才会生效。”；保存成功后离开表单，下
  次进入重新快照，提示自然消失。边界：从列表打开另一项会覆盖
  字段并重新快照（提示只针对当前表单页内的改动）；从路线编辑
  器返回模板表单、从模板选择器/模板表单返回工作流表单时不重新
  快照，之前做的改动仍被标记。**不引入草稿持久化**，进程被杀
  后 ViewModel 字段本身也不保留，这一点不在本批承诺范围内。
- 错误文案扩展：`UserCopy` 新增 9 条高频失败映射，格式固定为
  “发生了什么 + 能怎么办”，全部取自 MainViewModel /
  TraceEventMapper 里真实出现的文案（服务未运行、核心服务未运行、
  模型规划调用失败、无线启动失败、任务提交失败、配置同步失败、
  视觉输出解析失败、视觉动作输出无效、重复无效动作），配 JUnit
  测试；没有臆测不存在的错误码。已有失败翻译的裸“失败：详情”
  形式在本批被带建议的版本取代（UserCopy 在 UiMessageLocalizer
  表之前先行）。
- 无障碍审计：MainActivity.kt 里全部 `Icon` 用法共 4 处，逐一核查
  后结论是 **0 处需要改**：底部 Tab 的 Icon 伴随可见文字标签；
  两处“打开”行的右箭头伴随可见“打开”文字；`GlyphBadge` 的
  Icon 伴随所在卡片的标题/描述文字（包括可点击卡片，TalkBack
  读的是文字内容，徽标只是装饰）。区分标准：图标本身就是唯一
  可操作元素时才必须给 `contentDescription`；图标只是文字的装饰
  性陪衬时 `contentDescription = null` 才是正确的（避免重复朗读）。
  本批审计后数量不变：`contentDescription = null` 4 处，有描述 0 处。
- 版本保持 0.9.0 / versionCode 900 不变，本批不发版。

## 12. v0.9.0 第三批范围（已落地，开发中 beta；后续迭代之一）

- 全 App 最后一批 Toast：此前 MainActivity 类里仅剩的 3 处
  Toast（通知权限被拒、打开开发者选项失败、打开无线调试设置
  失败）全部消除，`Toast.makeText` 在 MainActivity.kt 里为 0 处，
  其他源码文件里也没有 Toast 调用点。本批范围只做这一件事，
  不扩散到其他反馈通道。
- 反馈通道：Activity 内以 `MutableSharedFlow`（replay = 0、
  带缓冲）承载一次性事件 `ActivitySnackbarEvent`，传给
  `LXBIgnitionApp` 已有的 SnackbarHost 展示；更新检查的
  Snackbar 机制未动，两者共用同一个 SnackbarHostState 排队。
  刻意不用裸字符串的 `MutableStateFlow`：同一失败第二次发生时
  值不变就不会重播，Snackbar 也就不会再弹。
- 文案与动作：3 条文案全部走现有 `tr()` / ZhMap（新增对照条目，
  覆盖脚本的 DYNAMIC_KEYS 已登记，因为它们在展示时经
  `i18n.tr()` 翻译、不以 `tr("...")` 字面量出现），格式为
  “发生了什么 + 能怎么办”。“打开开发者选项失败”与“打开无线
  调试设置失败”带 Snackbar 动作按钮“重试”，点动作重新执行
  对应的打开系统设置操作（失败会再次以 Snackbar 报告，不会
  静默）。通知权限那条不带动作：用户需到系统设置里手动开启，
  文案里写清去向，不给一个点下去没用的按钮。
- 打开系统设置本身的逻辑未改（`openSystemSettings` 仍是
  runCatching + `ACTION_APPLICATION_DEVELOPMENT_SETTINGS` /
  无线调试回退），只改失败反馈通道；ViewModel 未动。
- 版本保持 0.9.0 / versionCode 900 不变，本批不发版。

## 13. v0.9.0 第四批范围（已落地，开发中 beta；本批只做小组件抽取）

- 两步抽取法（解决前几批推迟的原因）：第一步先移语言基础设施，
  把 MainActivity.kt 里私有的 `UiI18n` 类、`LocalUiI18n`、
  顶层 `tr()` / `ut()` 与 `ZhMap` 对照表原样移到新文件
  `ui/i18n/UiI18n.kt`（公开）；MainActivity 里只留同名薄 wrapper
  （`tr()` / `ut()` 委托给新文件，`UiI18n` 为类型别名、
  `LocalUiI18n` 为同名转发值），现有调用点一字未改。第二步再把
  两个已稳定的小组件抽到 `ui/components/UiComponents.kt`：
  `DeleteConfirmDialog` 与 `EmptyStateBox`，签名与行为不变；
  `GlyphBadge` 因 `EmptyStateBox` 依赖它而一并移过去（连同它独占
  的 `glyphToIcon`），没有复制两份。调用点只改导入/引用，不改
  传参语义。`PageHeaderBlock`、`SurfacePanel` 等其他组件本批不抽。
- 移动核对：`ZhMap` 条目行数移动前后一致（660 行、659 个唯一键，
  其中 1 个重复键原样保留，未做去重）；已有翻译一字未动；
  `docs/check_tr_coverage.py` 已同步改读新文件里的表并扫描
  新组件文件里的 `tr()` 字面量，复核缺失仍为 0；MainActivity 里
  旧定义残留 0 处；调用点数量移动前后一致（`DeleteConfirmDialog`
  4 处调用、`EmptyStateBox` 4 处调用、`GlyphBadge` 18 处调用，
  均指向新位置）；删除了移动后 MainActivity 里仅剩的 3 个未用
  导入（`staticCompositionLocalOf`、`CircleShape`、`ImageVector`）。
- 语言机制未变：抽取只是换了文件位置，`tr()` / ZhMap 机制、
  `uiLang` 用户语言选择、中文兜底规则全部不变；没有迁移到
  `stringResource`。
- 行尾：MainActivity.kt 混用 CRLF/LF 且带 BOM，编辑按行保留
  原行尾，未做全文件转换；两个新文件统一用 LF。
- 版本保持 0.9.0 / versionCode 900 不变，本批不发版。

## 14. v0.9.0 第五批范围（已落地，开发中 beta；本批只做顶层 NavHost）

- 顶层导航：`LXBIgnitionApp` 的底部 4 个 Tab 由 NavHost 驱动，
  路由为 control（起点）/ tasks / config / logs，路由常量与
  tab↔route 纯映射在 `ui/navigation/TopNavRoutes.kt`（配 JUnit
  测试）。单个 Scaffold + 底部栏的壳不变；Tab 的选中态从
  NavController 的当前返回栈项取，不再由 `selectedTab` Int
  状态驱动；“打开配置”入口改为导航到 config 路由。
- Tab 切换语义：标准底部导航写法，`popUpTo(CONTROL)` +
  `saveState` / `launchSingleTop` / `restoreState`。返回栈里
  最多只有起点 + 当前 Tab，因此系统返回键在非首页 Tab 时
  回到 Control，从 Control 按返回则退出应用（与以前一致）。
  切 Tab 后各 Tab 内部状态由 save/restore 保留。
- 边界：各 Tab 内部的页面仍是第三、四批的状态栈（Tasks 8 页、
  Control 3 页、Config 5 页），不是 NavHost 目的地；把它们
  也迁入 NavHost 是后续项，本批不做，避免在没有编译兜底
  时一次性替换全部导航。
- 依赖：`androidx.navigation:navigation-compose:2.8.0`，与
  Compose BOM 2024.09.00 同代；版本目录统一管理。
- 版本保持 0.9.0 / versionCode 900 不变；本批之后
  `release/v0.9.0` 分支从本批提交上冻结，见
  `docs/RELEASE_v0.9.0.md`。

## 15. v0.9.0 第六批范围（编译修复；手动编译管线发现的错误）

- 背景：此前各批代码从未经编译；手动编译管线（aapt2 +
  kotlinc + d8 + apksigner，不走 Gradle）首次完整编译时
  发现以下 3 处错误，本批只修这 3 处，不改行为、不加功能。
- `activitySnackbarEvents` 默认值：`emptyFlow()` 改为
  `MutableSharedFlow()`（类型错误，见 CHANGELOG 第六批）。
- ZhMap 缺逗号：`"Semantic descriptor"` 条目行尾补逗号。
- 图标归属：`glyphToIcon` 的 "⏰" 由 `Icons.Filled.Schedule`
  改为 `Icons.Filled.Alarm`。原则：只用 material-icons-core
  里的图标；需要 extended 才有的图标时，优先换成 core 里
  语义相近的图标，不默认引入 extended 依赖。
- 版本保持 0.9.0 / versionCode 900 不变；本批之后从仓库
  干净源码重打 debug APK，作为本版第一份与源码完全对得
  上的安装包。

## 16. v0.9.0 第七批范围（图标修正；纠正第六批的错误修法）

- 教训：改图标用法时必须同步改导入；声称某个图标“在
  core 里”必须以解包核对为准，不能凭印象。第六批把
  Schedule 换成 Alarm 时漏了 2 行 Schedule 导入，且
  Alarm 本身也不在 core 1.7.0 里（实测 49 个 filled
  图标，无 Alarm、无 Schedule），导致干净重打仍失败。
- 本批：删掉 2 行残留的 Schedule 导入；"⏰" 映射到
  Notifications（铃铛）。core 图标的选用以解包后的
  classes.jar 清单为准；仍不引入 extended 依赖。
- 版本保持 0.9.0 / versionCode 900 不变；本批之后重打
  干净 APK。

## 17. v0.9.0 第八批范围（依赖声明修正）

- 原则：引入一个库的新版时，必须同步核对它编译时所
  用的依赖版本，不能只看它自己 POM 里声明的版本。
  navigation-compose 2.8.0 的 POM 写的是
  lifecycle-viewmodel-compose 2.6.2，但它的字节码调用
  了 2.6.2 里没有的 KClass 重载——POM 声明与编译实际
  不一致时，以字节码 + javap 实测为准。
- 本批：`app/build.gradle.kts:291` 的
  lifecycle-viewmodel-compose 由 2.6.1 提到 2.8.3，
  并附注释。Compose BOM 不管理 lifecycle 制品，这个
  pin 是抬起全家的唯一约束。
- 版本保持 0.9.0 / versionCode 900 不变；本批不改界面
  与行为，不必为本批重打 APK（clean3 已是 2.8.3 对齐
  的产物）。
