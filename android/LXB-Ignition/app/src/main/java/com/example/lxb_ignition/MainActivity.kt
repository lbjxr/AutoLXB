package com.example.lxb_ignition

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.os.Bundle
import android.widget.EditText
import android.widget.NumberPicker
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.BackHandler
import androidx.core.content.ContextCompat
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import com.example.lxb_ignition.ui.copy.UserCopy
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lxb_ignition.ui.navigation.TopNavRoutes
import com.example.lxb_ignition.model.AppPackageOption
import com.example.lxb_ignition.model.CoreRuntimeStatus
import com.example.lxb_ignition.model.TaskMapDetail
import com.example.lxb_ignition.model.TaskMapSegmentSnapshot
import com.example.lxb_ignition.model.TaskMapSnapshot
import com.example.lxb_ignition.model.TaskMapStepSnapshot
import com.example.lxb_ignition.model.TaskTemplateSummary
import com.example.lxb_ignition.model.WorkflowSummary
import com.example.lxb_ignition.model.TaskRouteActionSnapshot
import com.example.lxb_ignition.model.TaskRouteRecordSnapshot
import com.example.lxb_ignition.model.TaskSummary
import com.example.lxb_ignition.model.TraceEntry
import com.example.lxb_ignition.model.TraceMetaItem
import com.example.lxb_ignition.model.UnifiedLogEntry
import com.example.lxb_ignition.model.TaskRuntimeUiStatus
import com.example.lxb_ignition.model.WirelessBootstrapStatus
import com.example.lxb_ignition.ui.navigation.TaskNavStack
import com.example.lxb_ignition.ui.navigation.AppNavStack
import com.example.lxb_ignition.ui.theme.LXBIgnitionTheme
import com.example.lxb_ignition.ui.validation.FormField
import com.example.lxb_ignition.ui.validation.validateTemplateForm
import com.example.lxb_ignition.ui.validation.validateWorkflowForm
import com.example.lxb_ignition.ui.theme.AppError
import com.example.lxb_ignition.ui.theme.AppErrorSoft
import com.example.lxb_ignition.ui.theme.AppSuccess
import com.example.lxb_ignition.ui.theme.AppSuccessSoft
import com.example.lxb_ignition.ui.theme.AppWarning
import com.example.lxb_ignition.ui.theme.AppWarningSoft
import com.example.lxb_ignition.ui.components.DeleteConfirmDialog
import com.example.lxb_ignition.ui.components.EmptyStateBox
import com.example.lxb_ignition.ui.components.GlyphBadge
import com.lxb.server.cortex.LlmClient
import com.lxb.server.cortex.LlmConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.emptyFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    companion object {
        private const val ACTION_WIRELESS_DEBUGGING_SETTINGS = "android.settings.WIRELESS_DEBUGGING_SETTINGS"
    }

    // Batch 5: Activity-level failures used to be shown as Toasts, which
    // disappear and leave the user no next step. They are now emitted as
    // one-shot events on a SharedFlow (replay = 0, buffered) so the
    // LXBIgnitionApp SnackbarHost can show them. A MutableStateFlow
    // holding a bare String is deliberately NOT used here: the same
    // failure a second time would not re-emit, because the value is
    // unchanged. tryEmit never suspends the Activity callbacks.
    private val activityNoticeFlow =
        MutableSharedFlow<ActivitySnackbarEvent>(extraBufferCapacity = 64)

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            activityNoticeFlow.tryEmit(
                ActivitySnackbarEvent(
                    messageKey = "Notification permission was denied. Task and runtime status may not be shown; enable notifications in system settings.",
                    action = ActivitySnackbarAction.NONE
                )
            )
        }
    }

    private fun openSystemSettings(action: String): Boolean {
        return runCatching {
            startActivity(
                Intent(action).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
            true
        }.getOrDefault(false)
    }

    private fun openDeveloperOptionsSettings() {
        val ok = openSystemSettings(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
        if (!ok) {
            activityNoticeFlow.tryEmit(
                ActivitySnackbarEvent(
                    messageKey = "Failed to open Developer Options automatically. Please open Developer Options in system settings and try again.",
                    action = ActivitySnackbarAction.OPEN_DEVELOPER_OPTIONS,
                    actionLabelKey = "Retry"
                )
            )
        }
    }

    private fun openWirelessDebuggingSettingsFromUi() {
        val ok = openSystemSettings(ACTION_WIRELESS_DEBUGGING_SETTINGS)
            || openSystemSettings(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
        if (!ok) {
            activityNoticeFlow.tryEmit(
                ActivitySnackbarEvent(
                    messageKey = "Failed to open Wireless debugging settings automatically. Please open Wireless debugging in system settings and try again.",
                    action = ActivitySnackbarAction.OPEN_WIRELESS_DEBUGGING,
                    actionLabelKey = "Retry"
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ensureNotificationPermissionOnLaunch()
        enableEdgeToEdge()
        setContent {
            LXBIgnitionTheme {
                LXBIgnitionApp(
                    onOpenDeveloperOptionsSettings = { openDeveloperOptionsSettings() },
                    onOpenWirelessDebuggingSettings = { openWirelessDebuggingSettingsFromUi() },
                    activitySnackbarEvents = activityNoticeFlow,
                )
            }
        }
    }

    private fun ensureNotificationPermissionOnLaunch() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU) return
        val granted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        if (!granted) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

}

// Batch 1: named in-tab page constants. Previously ControlTab/ConfigTab used raw
// ints (0/1/2/...) and TasksTab used function-local vals; the values and the Int
// state type are unchanged, so behaviour is identical. A NavHost back stack is
// deliberately NOT introduced in this batch (see docs/PRODUCT_SPEC.md).
private const val CONTROL_PAGE_OVERVIEW = 0
private const val CONTROL_PAGE_WIRELESS_GUIDE = 1
private const val CONTROL_PAGE_ROOT_GUIDE = 2
private const val TASKS_PAGE_HOME = 0
private const val TASKS_PAGE_RECENT_RUNS = 1
private const val TASKS_PAGE_ROUTE_EDITOR = 2
private const val TASKS_PAGE_TEMPLATE_LIST = 3
private const val TASKS_PAGE_TEMPLATE_FORM = 4
private const val TASKS_PAGE_WORKFLOW_LIST = 5
private const val TASKS_PAGE_WORKFLOW_FORM = 6
private const val TASKS_PAGE_TEMPLATE_PICKER = 7
private const val CONFIG_PAGE_OVERVIEW = 0
private const val CONFIG_PAGE_DEVICE_CORE = 1
private const val CONFIG_PAGE_LLM = 2
private const val CONFIG_PAGE_UNLOCK_POLICY = 3
private const val CONFIG_PAGE_MAP_SYNC = 4

// Batch 5: one-shot Activity -> Snackbar events. The message is carried
// as an English canonical key and translated through i18n.tr() at
// display time, so it follows the user-chosen uiLang (not the system
// locale). Public visibility is required: LXBIgnitionApp is a public
// function and its parameter type must not be private.
enum class ActivitySnackbarAction {
    NONE,
    OPEN_DEVELOPER_OPTIONS,
    OPEN_WIRELESS_DEBUGGING
}

data class ActivitySnackbarEvent(
    val messageKey: String,
    val action: ActivitySnackbarAction = ActivitySnackbarAction.NONE,
    val actionLabelKey: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LXBIgnitionApp(
    viewModel: MainViewModel = viewModel(),
    onOpenDeveloperOptionsSettings: () -> Unit = {},
    onOpenWirelessDebuggingSettings: () -> Unit = {},
    activitySnackbarEvents: SharedFlow<ActivitySnackbarEvent> = emptyFlow(),
) {
    // Batch 7: top-level tabs are driven by a NavController + NavHost
    // (routes in ui/navigation/TopNavRoutes.kt) instead of the former
    // selectedTab Int state. The in-tab page stacks (Tasks/Control/
    // Config, batches 3-4) are untouched; only the tab switch moves.
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val selectedTab = TopNavRoutes.tabIndexForRoute(currentRoute) ?: 0
    fun navigateToTab(route: String) {
        navController.navigate(route) {
            popUpTo(TopNavRoutes.CONTROL) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val uiLang by viewModel.uiLang.collectAsState()
    val appUpdateResult by viewModel.appUpdateResult.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val i18n = remember(uiLang) { UiI18n(uiLang) }
    val latestI18n by rememberUpdatedState(i18n)

    LaunchedEffect(appUpdateResult) {
        if (appUpdateResult.isNotBlank()) {
            snackbarHostState.showSnackbar(appUpdateResult)
        }
    }

    // Batch 5: Activity-level failures (notification permission, system
    // settings open failures) arrive as one-shot events and are shown
    // on the same SnackbarHost as the update check. latestI18n (via
    // rememberUpdatedState) keeps the translation current without
    // restarting the collection when uiLang changes. The update-check
    // effect above is untouched and keeps its own queue slot.
    LaunchedEffect(activitySnackbarEvents) {
        activitySnackbarEvents.collect { event ->
            val actionLabel = event.actionLabelKey?.let { latestI18n.tr(it) }
            val result = snackbarHostState.showSnackbar(
                message = latestI18n.tr(event.messageKey),
                actionLabel = actionLabel,
                duration = SnackbarDuration.Long
            )
            if (result == SnackbarResult.ActionPerformed) {
                when (event.action) {
                    ActivitySnackbarAction.OPEN_DEVELOPER_OPTIONS ->
                        onOpenDeveloperOptionsSettings()
                    ActivitySnackbarAction.OPEN_WIRELESS_DEBUGGING ->
                        onOpenWirelessDebuggingSettings()
                    ActivitySnackbarAction.NONE -> Unit
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.checkAppUpdateOnLaunch()
    }

    CompositionLocalProvider(LocalUiI18n provides i18n) {
        // Batch 1: Material icons replace the former text glyphs. The
        // NavigationBarItem label already carries the accessible name, so the
        // decorative icon uses contentDescription = null (no double reading).
        // Batch 7: each tab carries its NavHost route. The list order
        // must still agree with TopNavRoutes.tabIndexForRoute, which
        // drives the selected highlight from the current route.
        val tabs = listOf(
            Triple("Control", Icons.Filled.Home, TopNavRoutes.CONTROL),
            Triple("Tasks", Icons.Filled.CheckCircle, TopNavRoutes.TASKS),
            Triple("Config", Icons.Filled.Settings, TopNavRoutes.CONFIG),
            Triple("Logs", Icons.Filled.Menu, TopNavRoutes.LOGS)
        )
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        actionIconContentColor = MaterialTheme.colorScheme.primary
                    ),
                    title = { Text("${tr("AutoLXB")} v${BuildConfig.VERSION_NAME}") },
                    actions = {
                        TextButton(onClick = { viewModel.checkAppUpdateFromGithub() }) {
                            Text(tr("Check update"), fontSize = 12.sp)
                        }
                    }
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp
                ) {
                    tabs.forEachIndexed { index, tab ->
                        val rawLabel = tab.first
                        val label = tr(rawLabel)
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = { navigateToTab(tab.third) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f),
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f)
                            ),
                            icon = { Icon(imageVector = tab.second, contentDescription = null, modifier = Modifier.size(24.dp)) },
                            label = { Text(label) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            // Batch 7: NavHost owns the top-level tab back stack.
            // Tab clicks use popUpTo(CONTROL) + saveState/restoreState,
            // so the stack holds at most the start destination plus the
            // current tab: system Back from a non-home tab returns to
            // Control, and from Control it exits the app as before.
            // In-tab page stacks (Tasks/Control/Config) are untouched.
            NavHost(
                navController = navController,
                startDestination = TopNavRoutes.CONTROL,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(TopNavRoutes.CONTROL) {
                    ControlTab(
                        viewModel = viewModel,
                        onOpenConfig = { navigateToTab(TopNavRoutes.CONFIG) },
                        onOpenDeveloperOptionsSettings = onOpenDeveloperOptionsSettings,
                        onOpenWirelessDebuggingSettings = onOpenWirelessDebuggingSettings
                    )
                }
                composable(TopNavRoutes.TASKS) { TasksTab(viewModel) }
                composable(TopNavRoutes.CONFIG) { ConfigTab(viewModel) }
                composable(TopNavRoutes.LOGS) { LogsTab(viewModel) }
            }
        }
    }
}

// Tab 1: Control

private data class WirelessGuideUiState(
    val headline: String,
    val detail: String,
    val stepIndex: Int,
    val accentColor: Color,
    val ready: Boolean = false,
    val failed: Boolean = false,
    val paired: Boolean = false
)

private fun resolveWirelessGuideUiState(status: WirelessBootstrapStatus): WirelessGuideUiState {
    val detail = status.message.ifBlank { "Idle" }
    return when (status.state.uppercase(Locale.ROOT)) {
        "GUIDE_SETTINGS" -> WirelessGuideUiState(
            headline = "Open Developer Options",
            detail = detail,
            stepIndex = 1,
            accentColor = Color(0xFF1565C0)
        )
        "WAIT_INPUT" -> WirelessGuideUiState(
            headline = "Waiting for pairing code",
            detail = detail,
            stepIndex = 4,
            accentColor = Color(0xFF1565C0)
        )
        "PAIRING" -> WirelessGuideUiState(
            headline = "Pairing phone",
            detail = detail,
            stepIndex = 4,
            accentColor = Color(0xFFEF6C00)
        )
        "PAIRED" -> WirelessGuideUiState(
            headline = "Phone paired successfully",
            detail = detail,
            stepIndex = 4,
            accentColor = Color(0xFF2E7D32),
            paired = true
        )
        "CONNECTING" -> WirelessGuideUiState(
            headline = "Connecting phone",
            detail = detail,
            stepIndex = 4,
            accentColor = Color(0xFFEF6C00)
        )
        "STARTING_CORE" -> WirelessGuideUiState(
            headline = "Starting core service",
            detail = detail,
            stepIndex = 4,
            accentColor = Color(0xFF6A1B9A)
        )
        "RECONNECTING" -> WirelessGuideUiState(
            headline = "Recovering core connection",
            detail = detail,
            stepIndex = 4,
            accentColor = Color(0xFFEF6C00)
        )
        "RUNNING" -> WirelessGuideUiState(
            headline = "Ready to use",
            detail = detail,
            stepIndex = 4,
            accentColor = Color(0xFF2E7D32),
            ready = true
        )
        "FAILED" -> WirelessGuideUiState(
            headline = "Startup failed",
            detail = detail,
            stepIndex = 4,
            accentColor = Color(0xFFC62828),
            failed = true
        )
        "STOPPING" -> WirelessGuideUiState(
            headline = "Stopping core service",
            detail = detail,
            stepIndex = 4,
            accentColor = Color(0xFF616161)
        )
        "STOP_PAIRING_REQUIRED" -> WirelessGuideUiState(
            headline = "Pair and connect first",
            detail = detail,
            stepIndex = 3,
            accentColor = AppWarning
        )
        else -> WirelessGuideUiState(
            headline = "Not started yet",
            detail = detail,
            stepIndex = 1,
            accentColor = Color(0xFF616161)
        )
    }
}

@Composable
fun ControlTab(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onOpenConfig: () -> Unit = {},
    onOpenDeveloperOptionsSettings: () -> Unit = {},
    onOpenWirelessDebuggingSettings: () -> Unit = {}
) {
    val coreRuntime by viewModel.coreRuntimeStatus.collectAsState()
    val wireless by viewModel.wirelessBootstrapStatus.collectAsState()
    val wirelessDebuggingEnabled by viewModel.wirelessDebuggingEnabled.collectAsState()
    val rootAvailable by viewModel.rootAvailable.collectAsState()
    val rootDetail by viewModel.rootDetail.collectAsState()
    var controlNavPages by rememberSaveable { mutableStateOf(listOf(CONTROL_PAGE_OVERVIEW)) }
    // Batch 4: Control back stack, same pattern as Tasks (batch 3). `page`
    // is derived from the stack top; forward moves push, Back pops. Depth
    // is 1 (overview -> one guide), so no popTo/reset helpers are needed.
    val controlNav = AppNavStack(controlNavPages)
    val page = controlNav.current
    fun goToControlPage(target: Int) { controlNavPages = controlNav.push(target).pages }
    fun goBackControlPage() { controlNavPages = controlNav.pop(CONTROL_PAGE_OVERVIEW).pages }
    var showStopPairingDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(wireless.state) {
        if (wireless.state == "STOP_PAIRING_REQUIRED") {
            goToControlPage(CONTROL_PAGE_WIRELESS_GUIDE)
            showStopPairingDialog = true
        }
    }

    if (showStopPairingDialog) {
        AlertDialog(
            onDismissRequest = { showStopPairingDialog = false },
            title = { Text(tr("Pair and connect first")) },
            text = {
                Text(
                    tr("AutoLXB needs Wireless ADB pairing before it can stop this core process. Open Developer Options, enable Wireless debugging, tap \"Pair device with pairing code\", then submit the code from the notification.")
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showStopPairingDialog = false
                        onOpenWirelessDebuggingSettings()
                    }
                ) {
                    Text(tr("Open Developer Options"))
                }
            },
            dismissButton = {
                TextButton(onClick = { showStopPairingDialog = false }) {
                    Text(tr("OK"))
                }
            }
        )
    }

    // Batch 4: system Back pops the Control stack. At the overview this
    // handler is disabled, so Back keeps its old behaviour. The pairing
    // dialog above consumes Back itself when it is open.
    BackHandler(enabled = controlNav.canGoBack(CONTROL_PAGE_OVERVIEW)) {
        controlNavPages = controlNav.pop(CONTROL_PAGE_OVERVIEW).pages
    }

    when (page) {
        CONTROL_PAGE_OVERVIEW -> ControlOverviewPage(
            modifier = modifier,
            coreRuntime = coreRuntime,
            onOpenWirelessGuide = { goToControlPage(CONTROL_PAGE_WIRELESS_GUIDE) },
            onOpenRootGuide = { goToControlPage(CONTROL_PAGE_ROOT_GUIDE) },
            onStop = { viewModel.stopServerProcess() },
            onRefreshState = { viewModel.refreshCoreRuntimeStatusNow() },
            onOpenConfig = onOpenConfig
        )
        CONTROL_PAGE_WIRELESS_GUIDE -> SingleConfigPage(
            title = tr("Wireless ADB startup"),
            modifier = modifier,
            onBack = { goBackControlPage() }
        ) {
            WirelessGuidePage(
                status = wireless,
                wirelessDebuggingEnabled = wirelessDebuggingEnabled,
                onStartGuide = {
                    onOpenDeveloperOptionsSettings()
                    viewModel.startWirelessBootstrapGuide()
                },
                onStartDirect = { viewModel.startServerWithNative() },
                onOpenWirelessDebugging = onOpenWirelessDebuggingSettings,
                onRefreshState = {
                    viewModel.refreshWirelessDebuggingEnabled()
                    viewModel.refreshCoreRuntimeStatusNow()
                }
            )
        }
        CONTROL_PAGE_ROOT_GUIDE -> SingleConfigPage(
            title = tr("Root startup"),
            modifier = modifier,
            onBack = { goBackControlPage() }
        ) {
            RootStartGuidePage(
                rootAvailable = rootAvailable,
                rootDetail = rootDetail,
                coreRuntime = coreRuntime,
                onStartRoot = { viewModel.startServerWithRootDirect() },
                onRefreshRoot = { viewModel.refreshRootAvailability() },
                onRefreshState = { viewModel.refreshCoreRuntimeStatusNow() }
            )
        }
    }
}

@Composable
private fun ControlOverviewPage(
    modifier: Modifier = Modifier,
    coreRuntime: CoreRuntimeStatus,
    onOpenWirelessGuide: () -> Unit,
    onOpenRootGuide: () -> Unit,
    onStop: () -> Unit,
    onRefreshState: () -> Unit,
    onOpenConfig: () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        if (coreRuntime.ready) {
            CompactCoreStatusPanel(
                detail = coreRuntime.detail,
                onStop = onStop,
                onRefreshState = onRefreshState
            )
        } else {
            CompactStartupPanel(
                onOpenWirelessGuide = onOpenWirelessGuide,
                onOpenRootGuide = onOpenRootGuide
            )
        }

        InlineInfoStrip(
            glyph = "⚙",
            accentColor = MaterialTheme.colorScheme.primary,
            title = tr("First-time setup"),
            detail = tr("Before your first task, complete the model configuration once. After that, you can come back here and start core directly."),
            actionLabel = tr("Open model config"),
            onAction = onOpenConfig
        )

        SurfacePanel(
            modifier = Modifier.fillMaxWidth(),
            background = MaterialTheme.colorScheme.surface,
            borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.62f)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                SheetHeader(
                    title = tr("What you can do next"),
                    subtitle = tr("The app is built for direct tasks, timed automation, and notification-triggered automation.")
                )
                FeatureBullet(
                    title = tr("Direct task"),
                    detail = tr("Type a requirement and let the phone complete it for you."),
                    glyph = "⌨"
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.24f))
                FeatureBullet(
                    title = tr("Scheduled workflow"),
                    detail = tr("Run the same workflow later or on a recurring schedule."),
                    glyph = "⏰"
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.24f))
                FeatureBullet(
                    title = tr("Notification workflow"),
                    detail = tr("Listen to one app's notifications and fire a workflow automatically."),
                    glyph = "✉"
                )
            }
        }
    }
}

@Composable
private fun ProductSectionLabel(
    title: String,
    subtitle: String? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, style = MaterialTheme.typography.titleSmall)
        if (!subtitle.isNullOrBlank()) {
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun ProductSectionCard(
    title: String,
    subtitle: String? = null,
    glyph: String = "•",
    content: @Composable () -> Unit
) {
    SurfacePanel(
        modifier = Modifier.fillMaxWidth(),
        background = MaterialTheme.colorScheme.surface,
        borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.62f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                GlyphBadge(
                    glyph = glyph,
                    accentColor = MaterialTheme.colorScheme.primary,
                    size = 36.dp
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ProductSectionLabel(title = title, subtitle = subtitle)
                    content()
                }
            }
        }
    }
}

@Composable
private fun FeatureBullet(title: String, detail: String, glyph: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        GlyphBadge(glyph = glyph, accentColor = MaterialTheme.colorScheme.primary, size = 34.dp)
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(title, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = detail,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun ProductEntryCard(
    title: String,
    description: String,
    meta: String,
    glyph: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SurfacePanel(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        background = MaterialTheme.colorScheme.surface,
        borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.58f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                GlyphBadge(glyph = glyph, accentColor = MaterialTheme.colorScheme.primary)
                StatusTag(text = meta, accentColor = MaterialTheme.colorScheme.primary)
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(title, style = MaterialTheme.typography.titleSmall)
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tr("Tap to open"),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = tr("Open"),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SurfacePanel(
    modifier: Modifier = Modifier,
    background: Color,
    borderColor: Color,
    shape: Shape = RoundedCornerShape(24.dp),
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = background),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        content()
    }
}

@Composable
private fun PageActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
        modifier = modifier.height(48.dp)
    ) {
        Text(text, fontSize = 12.sp)
    }
}

@Composable
private fun PageHeaderBlock(
    title: String,
    subtitle: String,
    glyph: String,
    onBack: (() -> Unit)? = null,
    primaryActionLabel: String? = null,
    onPrimaryAction: (() -> Unit)? = null,
    secondaryActionLabel: String? = null,
    onSecondaryAction: (() -> Unit)? = null
) {
    val hasPrimaryAction = !primaryActionLabel.isNullOrBlank() && onPrimaryAction != null
    val hasSecondaryAction = !secondaryActionLabel.isNullOrBlank() && onSecondaryAction != null
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            if (onBack != null) {
                PageActionButton(text = tr("Back"), onClick = onBack)
            }
            GlyphBadge(
                glyph = glyph,
                accentColor = MaterialTheme.colorScheme.primary,
                size = 34.dp
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f)
                )
            }
        }
        if (hasPrimaryAction || hasSecondaryAction) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (hasPrimaryAction && hasSecondaryAction) Arrangement.spacedBy(8.dp) else Arrangement.End
            ) {
                if (hasPrimaryAction) {
                    PageActionButton(
                        text = primaryActionLabel!!,
                        onClick = onPrimaryAction!!,
                        modifier = if (hasSecondaryAction) Modifier.weight(1f) else Modifier
                    )
                }
                if (hasSecondaryAction) {
                    PageActionButton(
                        text = secondaryActionLabel!!,
                        onClick = onSecondaryAction!!,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryInfoStrip(
    glyph: String,
    title: String,
    primaryMetric: String,
    secondaryMetric: String,
    accentColor: Color = MaterialTheme.colorScheme.primary
) {
    val stripBackground = accentColor.copy(alpha = 0.10f).compositeOver(MaterialTheme.colorScheme.surface)
    SurfacePanel(
        modifier = Modifier.fillMaxWidth(),
        background = stripBackground,
        borderColor = accentColor.copy(alpha = 0.24f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlyphBadge(glyph = glyph, accentColor = accentColor, size = 32.dp)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                Text(title, style = MaterialTheme.typography.labelLarge)
                Text(
                    text = primaryMetric,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.70f)
                )
            }
            StatusTag(text = secondaryMetric, accentColor = accentColor)
        }
    }
}

// Batch 2: dismissible in-page notice banner. Replaces the old Toast for
// portable import/export results: a Toast disappears and cannot be
// re-read, while this banner stays on the Tasks pages until the user
// closes it or the next portable operation replaces it.
@Composable
private fun PortableNoticeBanner(message: String, onDismiss: () -> Unit) {
    SurfacePanel(
        modifier = Modifier.fillMaxWidth(),
        background = MaterialTheme.colorScheme.primaryContainer,
        borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.20f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.heightIn(min = 48.dp)
            ) {
                Text(tr("Close"))
            }
        }
    }
}

@Composable
private fun StatusTag(text: String, accentColor: Color) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = accentColor.copy(alpha = 0.14f).compositeOver(MaterialTheme.colorScheme.surface)
        ),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.24f))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
            color = accentColor,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun PrimaryActionTile(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    glyph: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    val tileBackground = accentColor.copy(alpha = 0.08f).compositeOver(MaterialTheme.colorScheme.surface)
    SurfacePanel(
        modifier = modifier.clickable(onClick = onClick),
        background = tileBackground,
        borderColor = accentColor.copy(alpha = 0.28f),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .height(132.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            GlyphBadge(glyph = glyph, accentColor = accentColor)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(title, style = MaterialTheme.typography.titleSmall)
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f)
                )
            }
        }
    }
}

@Composable
private fun InlineInfoStrip(
    glyph: String,
    accentColor: Color,
    title: String,
    detail: String,
    actionLabel: String,
    onAction: () -> Unit
) {
    val stripBackground = accentColor.copy(alpha = 0.10f).compositeOver(MaterialTheme.colorScheme.surface)
    SurfacePanel(
        modifier = Modifier.fillMaxWidth(),
        background = stripBackground,
        borderColor = accentColor.copy(alpha = 0.24f),
        shape = RoundedCornerShape(26.dp)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlyphBadge(glyph = glyph, accentColor = accentColor, size = 40.dp)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(title, style = MaterialTheme.typography.titleSmall)
                Text(
                    text = detail,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
                )
            }
            OutlinedButton(
                onClick = onAction,
                border = BorderStroke(1.dp, accentColor.copy(alpha = 0.28f))
            ) {
                Text(actionLabel)
            }
        }
    }
}

@Composable
private fun CompactCoreStatusPanel(
    detail: String,
    onStop: () -> Unit,
    onRefreshState: () -> Unit
) {
    SurfacePanel(
        modifier = Modifier.fillMaxWidth(),
        background = MaterialTheme.colorScheme.surface,
        borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.52f),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlyphBadge(glyph = "✓", accentColor = AppSuccess, size = 34.dp)
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(tr("AutoLXB Core is running"), style = MaterialTheme.typography.titleSmall)
                        Text(
                            text = tr("Core is ready"),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f)
                        )
                    }
                }
                StatusTag(text = tr("Core Connected"), accentColor = AppSuccess)
            }
            Text(
                text = detail,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.76f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onStop,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = AppError)
                ) {
                    Text(tr("Stop Core"))
                }
                OutlinedButton(
                    onClick = onRefreshState,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(tr("Refresh status"))
                }
            }
        }
    }
}

@Composable
private fun CompactStartupPanel(
    onOpenWirelessGuide: () -> Unit,
    onOpenRootGuide: () -> Unit
) {
    SurfacePanel(
        modifier = Modifier.fillMaxWidth(),
        background = MaterialTheme.colorScheme.surface,
        borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.52f),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlyphBadge(glyph = "⚡", accentColor = MaterialTheme.colorScheme.primary, size = 34.dp)
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(tr("Start AutoLXB Core in one tap"), style = MaterialTheme.typography.titleSmall)
                    Text(
                        text = tr("Choose the startup method that matches your phone. Root is the shortest path, and Wireless ADB is the easiest path for most phones."),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PrimaryActionTile(
                    modifier = Modifier.weight(1f),
                    title = tr("ADB start"),
                    subtitle = tr("Recommended for most phones"),
                    glyph = "⌁",
                    accentColor = MaterialTheme.colorScheme.primary,
                    onClick = onOpenWirelessGuide
                )
                PrimaryActionTile(
                    modifier = Modifier.weight(1f),
                    title = tr("Root start"),
                    subtitle = tr("For rooted phones"),
                    glyph = "#",
                    accentColor = MaterialTheme.colorScheme.tertiary,
                    onClick = onOpenRootGuide
                )
            }
            Text(
                text = tr("Detailed steps stay inside each startup page."),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
            )
        }
    }
}

@Composable
private fun CompactGuideStatusPanel(
    eyebrow: String,
    title: String,
    detail: String,
    accentColor: Color,
    glyph: String,
    statusText: String
) {
    SurfacePanel(
        modifier = Modifier.fillMaxWidth(),
        background = MaterialTheme.colorScheme.surface,
        borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.52f),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlyphBadge(glyph = glyph, accentColor = accentColor, size = 34.dp)
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(title, style = MaterialTheme.typography.titleSmall)
                        Text(
                            text = eyebrow,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f)
                        )
                    }
                }
                StatusTag(text = statusText, accentColor = accentColor)
            }
            Text(
                text = detail,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.76f)
            )
        }
    }
}

@Composable
private fun SheetHeader(
    title: String,
    subtitle: String? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, style = MaterialTheme.typography.titleSmall)
        if (!subtitle.isNullOrBlank()) {
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.70f)
            )
        }
    }
}

@Composable
private fun WirelessGuidePage(
    status: WirelessBootstrapStatus,
    wirelessDebuggingEnabled: Boolean,
    onStartGuide: () -> Unit,
    onStartDirect: () -> Unit,
    onOpenWirelessDebugging: () -> Unit,
    onRefreshState: () -> Unit
) {
    val ui = resolveWirelessGuideUiState(status)
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        CompactGuideStatusPanel(
            eyebrow = tr("Recommended for most phones"),
            title = tr("Wireless ADB startup"),
            detail = ui.detail,
            accentColor = ui.accentColor,
            glyph = "⌁",
            statusText = tr(ui.headline)
        )

        SurfacePanel(
            modifier = Modifier.fillMaxWidth(),
            background = MaterialTheme.colorScheme.surface,
            borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.58f)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                SheetHeader(
                    title = tr("Follow these steps"),
                    subtitle = tr("Stay on this page while you complete the steps on your phone.")
                )
                GuideStepRow(
                    number = 1,
                    title = tr("Turn on Developer Options"),
                    detail = tr("If your phone has not enabled Developer Options yet, search your phone model first."),
                    active = ui.stepIndex == 1 && !ui.ready,
                    done = ui.stepIndex > 1 || ui.ready
                )
                GuideStepRow(
                    number = 2,
                    title = tr("Turn on Wireless debugging"),
                    detail = tr("Open Developer Options, find Wireless debugging, and switch it on."),
                    active = ui.stepIndex == 2 && !ui.ready,
                    done = ui.stepIndex > 2 || ui.ready
                )
                GuideStepRow(
                    number = 3,
                    title = tr("Open the pairing-code page"),
                    detail = tr("Tap \"Pair device with pairing code\" and keep that page open."),
                    active = ui.stepIndex == 3 && !ui.ready,
                    done = ui.stepIndex > 3 || ui.ready
                )
                GuideStepRow(
                    number = 4,
                    title = tr("Enter the pairing code in the notification"),
                    detail = tr("After the notification appears, enter the 6-digit pairing code there and wait for startup to finish."),
                    active = ui.stepIndex == 4 && !ui.ready && !ui.paired,
                    done = ui.ready || ui.paired
                )
            }
        }

        SurfacePanel(
            modifier = Modifier.fillMaxWidth(),
            background = MaterialTheme.colorScheme.primaryContainer,
            borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SheetHeader(
                    title = tr("Actions"),
                    subtitle = tr("Use the first button for first-time setup. If you already paired before, you can try starting directly.")
                )
                Button(onClick = onStartGuide, modifier = Modifier.fillMaxWidth()) {
                    Text(tr("Open Developer Options (Start Guide)"))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onStartDirect,
                        modifier = Modifier.weight(1f),
                        enabled = wirelessDebuggingEnabled
                    ) {
                        Text(tr("I already paired before, start directly"))
                    }
                    OutlinedButton(
                        onClick = onRefreshState,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(tr("Refresh connection state"))
                    }
                }
                if (!wirelessDebuggingEnabled) {
                    Text(
                        tr("Please make sure Wireless debugging is turned on first."),
                        color = AppWarning,
                        fontSize = 12.sp
                    )
                    OutlinedButton(onClick = onOpenWirelessDebugging, modifier = Modifier.fillMaxWidth()) {
                        Text(tr("Open Wireless debugging settings"))
                    }
                }
            }
        }

        SurfacePanel(
            modifier = Modifier.fillMaxWidth(),
            background = MaterialTheme.colorScheme.surface,
            borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.52f)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SheetHeader(
                    title = tr("Need help?"),
                    subtitle = tr("These are the most common ROM-specific requirements before startup can succeed.")
                )
                Text(
                    tr("- USB debugging must be enabled, otherwise process keepalive may fail."),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f),
                    fontSize = 12.sp
                )
                Text(
                    tr("- MIUI / HyperOS: enable \"USB debugging (Security settings)\". This is separate from \"USB debugging\"."),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f),
                    fontSize = 12.sp
                )
                Text(
                    tr("- ColorOS (OPPO / OnePlus): disable \"Permission monitoring\" in Developer options."),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f),
                    fontSize = 12.sp
                )
                Text(
                    tr("- Flyme: disable \"Flyme payment protection\" in Developer options."),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f),
                    fontSize = 12.sp
                )
                Text(
                    tr("Reference: ROM-specific notes above are adapted from Shizuku docs."),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f),
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun GuideStepRow(
    number: Int,
    title: String,
    detail: String,
    active: Boolean,
    done: Boolean
) {
    val accent = when {
        done -> AppSuccess
        active -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.48f)
    }
    val background = when {
        done -> AppSuccessSoft
        active -> MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    SurfacePanel(
        modifier = Modifier.fillMaxWidth(),
        background = background,
        borderColor = accent.copy(alpha = 0.18f),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            GlyphBadge(glyph = number.toString(), accentColor = accent, size = 34.dp)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(title, color = accent, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                    if (done) {
                        StatusTag(text = tr("Done"), accentColor = AppSuccess)
                    } else if (active) {
                        StatusTag(text = tr("Current status"), accentColor = MaterialTheme.colorScheme.primary)
                    }
                }
                Text(
                    detail,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun RootStartGuidePage(
    rootAvailable: Boolean,
    rootDetail: String,
    coreRuntime: CoreRuntimeStatus,
    onStartRoot: () -> Unit,
    onRefreshRoot: () -> Unit,
    onRefreshState: () -> Unit
) {
    val accent = if (rootAvailable) AppSuccess else AppWarning
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        CompactGuideStatusPanel(
            eyebrow = tr("For rooted phones"),
            title = tr("Root startup"),
            detail = rootDetail,
            accentColor = accent,
            glyph = "#",
            statusText = if (rootAvailable) tr("Root available") else tr("Root unavailable")
        )

        SurfacePanel(
            modifier = Modifier.fillMaxWidth(),
            background = if (coreRuntime.ready) AppSuccessSoft else AppWarningSoft,
            borderColor = (if (coreRuntime.ready) AppSuccess else AppWarning).copy(alpha = 0.20f)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlyphBadge(
                    glyph = if (coreRuntime.ready) "✓" else "!",
                    accentColor = if (coreRuntime.ready) AppSuccess else AppWarning,
                    size = 32.dp
                )
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "${tr("State")}: ${if (coreRuntime.ready) tr("Core Connected") else tr("Core Disconnected")}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = if (coreRuntime.ready) tr("AutoLXB Core is running") else tr("Get started"),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.70f)
                    )
                }
            }
        }

        SurfacePanel(
            modifier = Modifier.fillMaxWidth(),
            background = MaterialTheme.colorScheme.surface,
            borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.58f)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SheetHeader(
                    title = tr("Before you start"),
                    subtitle = tr("Root startup is shorter, but it still depends on root permission being granted in time.")
                )
                Text(
                    tr("Requires root permission grant in superuser manager."),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f),
                    fontSize = 12.sp
                )
                Text(
                    tr("No Wireless ADB guide is needed on rooted phones."),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f),
                    fontSize = 12.sp
                )
                Text(
                    tr("If startup fails, check whether the root permission popup was denied or timed out."),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f),
                    fontSize = 12.sp
                )
            }
        }

        SurfacePanel(
            modifier = Modifier.fillMaxWidth(),
            background = if (rootAvailable) AppSuccessSoft else AppWarningSoft,
            borderColor = accent.copy(alpha = 0.20f)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SheetHeader(
                    title = tr("Actions"),
                    subtitle = tr("Use root startup only after you confirm root is available on this phone.")
                )
                Button(
                    onClick = onStartRoot,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = rootAvailable
                ) {
                    Text(tr("Start via Root"))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(onClick = onRefreshRoot, modifier = Modifier.weight(1f)) {
                        Text(tr("Check Root"))
                    }
                    OutlinedButton(onClick = onRefreshState, modifier = Modifier.weight(1f)) {
                        Text(tr("Refresh connection state"))
                    }
                }
            }
        }
    }
}

@Composable
fun TaskSessionCard(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val requirement by viewModel.requirement.collectAsState()

    SurfacePanel(
        modifier = modifier.fillMaxWidth(),
        background = MaterialTheme.colorScheme.surface,
        borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.62f),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            SheetHeader(
                title = tr("Quick task"),
                subtitle = tr("Type one task and run it directly on this page.")
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = requirement,
                    onValueChange = { viewModel.requirement.value = it },
                    label = { Text(tr("Describe what you want to do")) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )
                Button(
                    onClick = { viewModel.runRequirementOnDevice() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                ) {
                    Text(tr("Run"))
                }
            }
        }
    }
}

@Composable
fun TaskRuntimeStatusCard(
    status: TaskRuntimeUiStatus,
    modifier: Modifier = Modifier,
    onStop: (() -> Unit)? = null
) {
    val accentColor = if (status.running) AppSuccess else MaterialTheme.colorScheme.primary
    val background = if (status.running) AppSuccessSoft else MaterialTheme.colorScheme.surfaceVariant
    SurfacePanel(
        modifier = modifier.fillMaxWidth(),
        background = background,
        borderColor = accentColor.copy(alpha = 0.18f),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlyphBadge(
                        glyph = if (status.running) "▶" else "○",
                        accentColor = accentColor,
                        size = 34.dp
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(tr("Task Runtime"), style = MaterialTheme.typography.titleSmall)
                        Text(
                            text = if (status.running) tr("A task is currently running on the phone.") else tr("No task is running right now."),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
                        )
                    }
                }
                StatusTag(text = if (status.running) tr("RUNNING") else tr("IDLE"), accentColor = accentColor)
            }
            Text(
                text = "${tr("Current task")}: ${if (status.taskId.isNotEmpty()) status.taskId.take(8) + "..." else "-"}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f)
            )
            Text(
                text = "${tr("Phase")}: ${tr(ut(status.phase))}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f)
            )
            Text(
                text = if (status.running) ut(status.detail) else tr("No task is running."),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f)
            )
            if (status.running && onStop != null) {
                OutlinedButton(
                    onClick = onStop,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(tr("Stop"))
                }
            }
        }
    }
}

// Batch 6: language infrastructure moved to ui/i18n/UiI18n.kt.
// Thin wrappers only, so existing call sites are unchanged. The class,
// the composition local and the translation table now live in the new file.
private typealias UiI18n = com.example.lxb_ignition.ui.i18n.UiI18n

private val LocalUiI18n = com.example.lxb_ignition.ui.i18n.LocalUiI18n

@Composable
private fun tr(text: String): String = com.example.lxb_ignition.ui.i18n.tr(text)

@Composable
private fun ut(text: String): String = com.example.lxb_ignition.ui.i18n.ut(text)

private fun llmRequestTypeLabel(requestType: String): String {
    return when (LlmConfig.normalizeRequestType(requestType)) {
        LlmConfig.REQUEST_TYPE_GEMINI_GENERATE_CONTENT -> "Gemini generateContent"
        LlmConfig.REQUEST_TYPE_ANTHROPIC_MESSAGES -> "Anthropic Messages"
        else -> "OpenAI Chat Completions"
    }
}

private fun llmBaseUrlExample(requestType: String): String {
    return when (LlmConfig.normalizeRequestType(requestType)) {
        LlmConfig.REQUEST_TYPE_GEMINI_GENERATE_CONTENT -> "https://generativelanguage.googleapis.com/v1beta"
        LlmConfig.REQUEST_TYPE_ANTHROPIC_MESSAGES -> "https://api.anthropic.com/v1"
        else -> "https://api.openai.com/v1"
    }
}

private data class RouteEditorTarget(
    val title: String,
    val source: String,
    val routeId: String,
    val packageName: String = "",
    val userTask: String = "",
    val userPlaybook: String = "",
    val mode: String = ""
)

private data class TemplateFormSnapshot(
    val name: String,
    val description: String,
    val packageName: String,
    val playbook: String,
    val decomposeEnabled: Boolean,
    val taskMapMode: String
)

private data class WorkflowFormSnapshot(
    val name: String,
    val playbook: String,
    val stepTemplateIds: List<String>,
    val triggerType: String,
    val runAtMs: String,
    val repeatMode: String,
    val repeatWeekdays: Int,
    val triggerPackage: String,
    val titlePattern: String,
    val bodyPattern: String,
    val cooldownSeconds: String,
    val activeTimeStart: String,
    val activeTimeEnd: String,
    val llmConditionEnabled: Boolean,
    val llmCondition: String
)

// Tab 2: Tasks

@Composable
fun TasksTab(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val tasks by viewModel.taskList.collectAsState()
    val taskMapDetail by viewModel.taskMapDetail.collectAsState()
    val taskMapDetailLoading by viewModel.taskMapDetailLoading.collectAsState()
    val taskMapSaving by viewModel.taskMapSaving.collectAsState()
    val templates by viewModel.templateList.collectAsState()
    val workflows by viewModel.workflowList.collectAsState()
    val installedApps by viewModel.installedAppList.collectAsState()
    val taskRuntime by viewModel.taskRuntimeUiStatus.collectAsState()
    val templateName by viewModel.templateName.collectAsState()
    val templateDescription by viewModel.templateDescription.collectAsState()
    val templatePackage by viewModel.templatePackage.collectAsState()
    val templatePlaybook by viewModel.templatePlaybook.collectAsState()
    val templateDecomposeEnabled by viewModel.templateDecomposeEnabled.collectAsState()
    val templateTaskMapMode by viewModel.templateTaskMapMode.collectAsState()
    val workflowName by viewModel.workflowName.collectAsState()
    val workflowPlaybook by viewModel.workflowPlaybook.collectAsState()
    val workflowStepTemplateIds by viewModel.workflowStepTemplateIds.collectAsState()
    val workflowTriggerType by viewModel.workflowTriggerType.collectAsState()
    val workflowRunAtMs by viewModel.workflowRunAtMs.collectAsState()
    val workflowRepeatMode by viewModel.workflowRepeatMode.collectAsState()
    val workflowRepeatWeekdays by viewModel.workflowRepeatWeekdays.collectAsState()
    val workflowTriggerPackage by viewModel.workflowTriggerPackage.collectAsState()
    val workflowNotifyTitlePattern by viewModel.workflowNotifyTitlePattern.collectAsState()
    val workflowNotifyBodyPattern by viewModel.workflowNotifyBodyPattern.collectAsState()
    val workflowNotifyCooldownSeconds by viewModel.workflowNotifyCooldownSeconds.collectAsState()
    val workflowNotifyActiveTimeStart by viewModel.workflowNotifyActiveTimeStart.collectAsState()
    val workflowNotifyActiveTimeEnd by viewModel.workflowNotifyActiveTimeEnd.collectAsState()
    val workflowNotifyLlmConditionEnabled by viewModel.workflowNotifyLlmConditionEnabled.collectAsState()
    val workflowNotifyLlmCondition by viewModel.workflowNotifyLlmCondition.collectAsState()
    var taskNavPages by rememberSaveable { mutableStateOf(listOf(TASKS_PAGE_HOME)) }
    // Batch 3: the Tasks back stack. `page` below is derived from the
    // stack top; forward moves push, Back pops, Save pops back to (or
    // resets to) its list. Form contents live in the ViewModel, so
    // popping does not discard them; unsaved-form draft saving and a
    // leave-without-saving confirm are explicitly next-batch decisions.
    val taskNav = TaskNavStack(taskNavPages)
    val page = taskNav.current
    fun goToTasksPage(target: Int) { taskNavPages = taskNav.push(target).pages }
    fun goBackTasksPage() { taskNavPages = taskNav.pop(TASKS_PAGE_HOME).pages }
    fun goBackToTasksPage(target: Int) { taskNavPages = taskNav.popToOrReset(target, TASKS_PAGE_HOME).pages }
    var selectedTask by remember { mutableStateOf<TaskSummary?>(null) }
    var routeEditorTarget by remember { mutableStateOf<RouteEditorTarget?>(null) }
    var portableNotice by remember { mutableStateOf<String?>(null) }
    var templateAttemptedSave by remember { mutableStateOf(false) }
    var workflowAttemptedSave by remember { mutableStateOf(false) }
    // Batch 4: unsaved-changes snapshots. Form fields live in ViewModel
    // StateFlows, so leaving a form does NOT discard them (verified
    // premise, see PRODUCT_SPEC.md section 11) -- hence no leave-confirm
    // dialog. The snapshot is taken on entering a form page and compared
    // with the live fields; it is NOT retaken when returning from the
    // route editor (template) or from the template picker / template form
    // (workflow), so edits made before those detours stay flagged.
    var templateFormSnapshot by remember { mutableStateOf<TemplateFormSnapshot?>(null) }
    var workflowFormSnapshot by remember { mutableStateOf<WorkflowFormSnapshot?>(null) }
    var tasksPreviousPage by remember { mutableStateOf(TASKS_PAGE_HOME) }
    LaunchedEffect(page) {
        if (page == TASKS_PAGE_TEMPLATE_FORM && tasksPreviousPage != TASKS_PAGE_ROUTE_EDITOR) {
            templateFormSnapshot = TemplateFormSnapshot(templateName, templateDescription, templatePackage, templatePlaybook, templateDecomposeEnabled, templateTaskMapMode)
        }
        if (page == TASKS_PAGE_WORKFLOW_FORM && tasksPreviousPage != TASKS_PAGE_TEMPLATE_PICKER && tasksPreviousPage != TASKS_PAGE_TEMPLATE_FORM) {
            workflowFormSnapshot = WorkflowFormSnapshot(workflowName, workflowPlaybook, workflowStepTemplateIds, workflowTriggerType, workflowRunAtMs, workflowRepeatMode, workflowRepeatWeekdays, workflowTriggerPackage, workflowNotifyTitlePattern, workflowNotifyBodyPattern, workflowNotifyCooldownSeconds, workflowNotifyActiveTimeStart, workflowNotifyActiveTimeEnd, workflowNotifyLlmConditionEnabled, workflowNotifyLlmCondition)
        }
        tasksPreviousPage = page
    }
    val portableImportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            viewModel.importPortableBundleFromUri(
                uri = uri
            ) { importedType, _ ->
                when (importedType) {
                    "workflow" -> goToTasksPage(TASKS_PAGE_WORKFLOW_FORM)
                    "template" -> goToTasksPage(TASKS_PAGE_TEMPLATE_FORM)
                }
                viewModel.refreshTemplateListOnDevice(silent = true)
                viewModel.refreshWorkflowListOnDevice(silent = true)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.refreshInstalledAppSnapshotOnDevice()
        viewModel.refreshTemplateListOnDevice(silent = true)
        viewModel.refreshWorkflowListOnDevice(silent = true)
        viewModel.refreshTaskListOnDevice()
    }

    LaunchedEffect(viewModel) {
        viewModel.portableOperationNotice.collect { message ->
            portableNotice = message
        }
    }

    LaunchedEffect(selectedTask?.taskId, selectedTask?.routeId) {
        val task = selectedTask
        if (task == null) {
            viewModel.clearTaskMapDetail()
        } else {
            viewModel.loadTaskMapDetail(task)
        }
    }

    LaunchedEffect(page) {
        if (page != TASKS_PAGE_HOME && page != TASKS_PAGE_RECENT_RUNS) return@LaunchedEffect
        while (true) {
            viewModel.refreshTaskListOnDevice(silent = true)
            delay(3000)
        }
    }

    // Batch 3: system Back pops the Tasks stack. At the Tasks home page
    // this handler is disabled, so Back keeps its old behaviour (the
    // system handles it, i.e. leaving the app). Dialogs shown above
    // TasksTab (task detail, pickers, delete confirms) consume Back
    // themselves before this handler runs.
    BackHandler(enabled = taskNav.canGoBack(TASKS_PAGE_HOME)) {
        taskNavPages = taskNav.pop(TASKS_PAGE_HOME).pages
    }

    when (page) {
        TASKS_PAGE_HOME -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                PageHeaderBlock(
                    title = tr("Tasks"),
                    subtitle = tr("Run a task now, manage automation, or review recent results."),
                    glyph = "☰",
                    primaryActionLabel = tr("Refresh All"),
                    onPrimaryAction = {
                        viewModel.refreshInstalledAppSnapshotOnDevice()
                        viewModel.refreshTemplateListOnDevice()
                        viewModel.refreshWorkflowListOnDevice()
                        viewModel.refreshTaskListOnDevice()
                    }
                )

                if (portableNotice != null) {
                    PortableNoticeBanner(message = portableNotice!!, onDismiss = { portableNotice = null })
                }
                TaskRuntimeStatusCard(
                    status = taskRuntime,
                    onStop = { viewModel.cancelCurrentTaskOnDevice() }
                )

                TaskSessionCard(viewModel = viewModel)
                CompactEntryRow(title = tr("Templates"), meta = "${templates.size} ${tr("items")}", onClick = { goToTasksPage(TASKS_PAGE_TEMPLATE_LIST) })
                CompactEntryRow(title = tr("Workflows"), meta = "${workflows.size} ${tr("items")}", onClick = { goToTasksPage(TASKS_PAGE_WORKFLOW_LIST) })
                CompactEntryRow(title = tr("Recent Runs"), meta = "${tasks.size} ${tr("items")}", onClick = { goToTasksPage(TASKS_PAGE_RECENT_RUNS) })
                OutlinedButton(
                    onClick = { portableImportLauncher.launch(arrayOf("application/json", "text/plain")) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(tr("Import Portable Bundle"))
                }
            }
        }

        TASKS_PAGE_TEMPLATE_LIST -> {
            val listState = rememberLazyListState()
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                PageHeaderBlock(
                    title = tr("Templates"),
                    subtitle = tr("Author reusable task templates and their primary routes."),
                    glyph = "▣",
                    onBack = { goBackTasksPage() },
                    primaryActionLabel = tr("New"),
                    onPrimaryAction = {
                        viewModel.resetTemplateForm()
                        templateAttemptedSave = false
                        goToTasksPage(TASKS_PAGE_TEMPLATE_FORM)
                    },
                    secondaryActionLabel = tr("Refresh"),
                    onSecondaryAction = { viewModel.refreshTemplateListOnDevice() }
                )
                if (portableNotice != null) {
                    PortableNoticeBanner(message = portableNotice!!, onDismiss = { portableNotice = null })
                }
                SurfacePanel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    background = MaterialTheme.colorScheme.surface,
                    borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.58f)
                ) {
                    if (templates.isEmpty()) {
                        EmptyStateBox(
                            title = tr("No templates yet."),
                            detail = tr("Create a template before composing workflows."),
                            actionLabel = tr("New template"),
                            onAction = {
                                viewModel.resetTemplateForm()
                                templateAttemptedSave = false
                                goToTasksPage(TASKS_PAGE_TEMPLATE_FORM)
                            }
                        )
                    } else {
                        LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                            itemsIndexed(templates, key = { _, it -> it.templateId }) { index, template ->
                                TemplateRow(
                                    template = template,
                                    onRun = { viewModel.runTemplateOnDevice(template.templateId) },
                                    onEdit = {
                                        viewModel.loadTemplateForm(template)
                                        templateAttemptedSave = false
                                        goToTasksPage(TASKS_PAGE_TEMPLATE_FORM)
                                    },
                                    onRoute = {
                                        routeEditorTarget = RouteEditorTarget(
                                            title = template.name.ifBlank { template.description },
                                            source = "template",
                                            routeId = template.templateId,
                                            packageName = template.packageName,
                                            userTask = template.description,
                                            userPlaybook = template.userPlaybook,
                                            mode = template.taskMapMode
                                        )
                                        goToTasksPage(TASKS_PAGE_ROUTE_EDITOR)
                                    },
                                    onDelete = { viewModel.deleteTemplateOnDevice(template.templateId) }
                                )
                                if (index < templates.lastIndex) {
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.18f))
                                }
                            }
                        }
                    }
                }
            }
        }

        TASKS_PAGE_TEMPLATE_FORM -> {
            var showTemplateAppPicker by rememberSaveable { mutableStateOf(false) }
            // Batch 2: errors appear only after the first Save tap; after that
            // they are recomputed from the live field values on every edit.
            val templateErrors = if (templateAttemptedSave) validateTemplateForm(templateName, templateDescription, templatePackage) else emptyList()
            val templateNameError = templateErrors.firstOrNull { it.field == FormField.TEMPLATE_NAME }?.message
            // Batch 4: display-only unsaved-changes indicator (see above).
            val templateHasUnsavedChanges = templateFormSnapshot?.let {
                it != TemplateFormSnapshot(templateName, templateDescription, templatePackage, templatePlaybook, templateDecomposeEnabled, templateTaskMapMode)
            } ?: false
            val templateDescriptionError = templateErrors.firstOrNull { it.field == FormField.TASK_DESCRIPTION }?.message
            val templatePackageError = templateErrors.firstOrNull { it.field == FormField.TARGET_APP }?.message
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PageHeaderBlock(
                    title = tr("Template"),
                    subtitle = tr("Define one reusable task unit."),
                    glyph = "▣",
                    onBack = {
                        templateAttemptedSave = false
                        goBackTasksPage()
                    },
                    primaryActionLabel = tr("Save"),
                    onPrimaryAction = {
                        templateAttemptedSave = true
                        if (validateTemplateForm(templateName, templateDescription, templatePackage).isEmpty()) {
                            viewModel.saveTemplateOnDevice()
                            templateAttemptedSave = false
                            goBackToTasksPage(TASKS_PAGE_TEMPLATE_LIST)
                        }
                    },
                    secondaryActionLabel = tr("Export Template"),
                    onSecondaryAction = {
                        viewModel.exportPortableTemplateOnDevice(viewModel.templateId.value)
                    }
                )
                if (portableNotice != null) {
                    PortableNoticeBanner(message = portableNotice!!, onDismiss = { portableNotice = null })
                }
                if (templateHasUnsavedChanges) {
                    Text(
                        text = tr("Unsaved changes. Your edits are kept in this form until you save."),
                        fontSize = 12.sp,
                        color = AppWarning
                    )
                }
                OutlinedTextField(
                    value = templateName,
                    onValueChange = { viewModel.templateName.value = it },
                    label = { Text(tr("Name")) },
                    isError = templateNameError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (templateNameError != null) {
                    Text(
                        text = tr(templateNameError),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                OutlinedTextField(
                    value = templateDescription,
                    onValueChange = { viewModel.templateDescription.value = it },
                    label = { Text(tr("Task description")) },
                    isError = templateDescriptionError != null,
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                if (templateDescriptionError != null) {
                    Text(
                        text = tr(templateDescriptionError),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                PackageSelectField(
                    title = tr("Target app"),
                    selectedPackage = templatePackage,
                    error = templatePackageError,
                    options = installedApps,
                    onOpen = {
                        if (installedApps.isEmpty()) {
                            viewModel.refreshInstalledAppSnapshotOnDevice()
                        }
                        showTemplateAppPicker = true
                    },
                    onClear = { viewModel.templatePackage.value = "" }
                )
                OutlinedTextField(
                    value = templatePlaybook,
                    onValueChange = { viewModel.templatePlaybook.value = it },
                    label = { Text(tr("Playbook")) },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                PreferenceSwitchRow(
                    title = tr("Allow model decomposition"),
                    detail = tr("Enable the legacy TASK_DECOMPOSE stage for this template."),
                    checked = templateDecomposeEnabled,
                    onCheckedChange = { viewModel.templateDecomposeEnabled.value = it }
                )
                PreferenceSwitchRow(
                    title = tr("Task route mode"),
                    detail = tr("Prefer the saved route for this template before normal execution."),
                    checked = templateTaskMapMode != MainViewModel.TASK_MAP_MODE_OFF,
                    onCheckedChange = {
                        viewModel.templateTaskMapMode.value = if (it) {
                            MainViewModel.TASK_MAP_MODE_MANUAL
                        } else {
                            MainViewModel.TASK_MAP_MODE_OFF
                        }
                    }
                )
                OutlinedButton(
                    onClick = {
                        routeEditorTarget = RouteEditorTarget(
                            title = templateName.ifBlank { templateDescription },
                            source = "template",
                            routeId = viewModel.templateId.value.trim(),
                            packageName = templatePackage,
                            userTask = templateDescription,
                            userPlaybook = templatePlaybook,
                            mode = templateTaskMapMode
                        )
                        goToTasksPage(TASKS_PAGE_ROUTE_EDITOR)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(tr("Open task route editor"))
                }
                if (showTemplateAppPicker) {
                    PackagePickerDialog(
                        options = installedApps,
                        onDismiss = { showTemplateAppPicker = false },
                        onSelect = {
                            viewModel.templatePackage.value = it.packageName
                            showTemplateAppPicker = false
                        }
                    )
                }
            }
        }

        TASKS_PAGE_WORKFLOW_LIST -> {
            val listState = rememberLazyListState()
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                PageHeaderBlock(
                    title = tr("Workflows"),
                    subtitle = tr("Sequence templates and attach one optional trigger."),
                    glyph = "↳",
                    onBack = { goBackTasksPage() },
                    primaryActionLabel = tr("New"),
                    onPrimaryAction = {
                        viewModel.resetWorkflowForm()
                        workflowAttemptedSave = false
                        goToTasksPage(TASKS_PAGE_WORKFLOW_FORM)
                    },
                    secondaryActionLabel = tr("Refresh"),
                    onSecondaryAction = { viewModel.refreshWorkflowListOnDevice() }
                )
                if (portableNotice != null) {
                    PortableNoticeBanner(message = portableNotice!!, onDismiss = { portableNotice = null })
                }
                SurfacePanel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    background = MaterialTheme.colorScheme.surface,
                    borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.58f)
                ) {
                    if (workflows.isEmpty()) {
                        EmptyStateBox(
                            title = tr("No workflows yet."),
                            detail = tr("Create a workflow from one or more templates."),
                            actionLabel = tr("New workflow"),
                            onAction = {
                                viewModel.resetWorkflowForm()
                                workflowAttemptedSave = false
                                goToTasksPage(TASKS_PAGE_WORKFLOW_FORM)
                            }
                        )
                    } else {
                        LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                            itemsIndexed(workflows, key = { _, it -> it.workflowId }) { index, workflow ->
                                WorkflowRow(
                                    workflow = workflow,
                                    onRun = { viewModel.runWorkflowOnDevice(workflow.workflowId) },
                                    onEdit = {
                                        viewModel.loadWorkflowForm(workflow)
                                        workflowAttemptedSave = false
                                        goToTasksPage(TASKS_PAGE_WORKFLOW_FORM)
                                    },
                                    onToggleEnabled = { checked -> viewModel.toggleWorkflowTriggerEnabledOnDevice(workflow, checked) },
                                    onDelete = { viewModel.deleteWorkflowOnDevice(workflow.workflowId) }
                                )
                                if (index < workflows.lastIndex) {
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.18f))
                                }
                            }
                        }
                    }
                }
            }
        }

        TASKS_PAGE_WORKFLOW_FORM -> {
            // Batch 2: same first-save-then-live rule as the template form.
            val workflowErrors = if (workflowAttemptedSave) validateWorkflowForm(workflowName, workflowStepTemplateIds.size) else emptyList()
            val workflowNameError = workflowErrors.firstOrNull { it.field == FormField.WORKFLOW_NAME }?.message
            // Batch 4: display-only unsaved-changes indicator (see above).
            val workflowHasUnsavedChanges = workflowFormSnapshot?.let {
                it != WorkflowFormSnapshot(workflowName, workflowPlaybook, workflowStepTemplateIds, workflowTriggerType, workflowRunAtMs, workflowRepeatMode, workflowRepeatWeekdays, workflowTriggerPackage, workflowNotifyTitlePattern, workflowNotifyBodyPattern, workflowNotifyCooldownSeconds, workflowNotifyActiveTimeStart, workflowNotifyActiveTimeEnd, workflowNotifyLlmConditionEnabled, workflowNotifyLlmCondition)
            } ?: false
            val workflowStepsError = workflowErrors.firstOrNull { it.field == FormField.WORKFLOW_STEPS }?.message
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PageHeaderBlock(
                    title = tr("Workflow"),
                    subtitle = tr("Select a template and optional trigger."),
                    glyph = "↳",
                    onBack = {
                        workflowAttemptedSave = false
                        goBackTasksPage()
                    },
                    primaryActionLabel = tr("Save"),
                    onPrimaryAction = {
                        workflowAttemptedSave = true
                        if (validateWorkflowForm(workflowName, workflowStepTemplateIds.size).isEmpty()) {
                            viewModel.saveWorkflowOnDevice()
                            workflowAttemptedSave = false
                            goBackToTasksPage(TASKS_PAGE_WORKFLOW_LIST)
                        }
                    },
                    secondaryActionLabel = tr("Export Workflow"),
                    onSecondaryAction = {
                        viewModel.exportPortableWorkflowOnDevice(viewModel.workflowId.value)
                    }
                )
                if (portableNotice != null) {
                    PortableNoticeBanner(message = portableNotice!!, onDismiss = { portableNotice = null })
                }
                if (workflowHasUnsavedChanges) {
                    Text(
                        text = tr("Unsaved changes. Your edits are kept in this form until you save."),
                        fontSize = 12.sp,
                        color = AppWarning
                    )
                }
                OutlinedTextField(
                    value = workflowName,
                    onValueChange = { viewModel.workflowName.value = it },
                    label = { Text(tr("Name")) },
                    isError = workflowNameError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (workflowNameError != null) {
                    Text(
                        text = tr(workflowNameError),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                OutlinedTextField(
                    value = workflowPlaybook,
                    onValueChange = { viewModel.workflowPlaybook.value = it },
                    label = { Text(tr("Workflow playbook (optional)")) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 8
                )
                SheetHeader(
                    title = tr("Steps"),
                    subtitle = tr("Add templates and arrange the order they should run.")
                )
                if (workflowStepsError != null) {
                    Text(
                        text = tr(workflowStepsError),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                if (workflowStepTemplateIds.isEmpty()) {
                    EmptyStateBox(
                        title = tr("No steps yet."),
                        detail = tr("Add a template to start building this workflow."),
                        actionLabel = tr("Add template"),
                        onAction = { goToTasksPage(TASKS_PAGE_TEMPLATE_PICKER) }
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        workflowStepTemplateIds.forEachIndexed { index, templateId ->
                            val template = templates.firstOrNull { it.templateId == templateId }
                            WorkflowStepEditorRow(
                                index = index,
                                templateName = template?.name.orEmpty().ifBlank { template?.description.orEmpty() },
                                packageName = template?.packageName.orEmpty(),
                                onOpen = {
                                    template?.let {
                                        viewModel.loadTemplateForm(it)
                                        templateAttemptedSave = false
                                        goToTasksPage(TASKS_PAGE_TEMPLATE_FORM)
                                    }
                                },
                                onMoveUp = { viewModel.moveWorkflowStepUp(index) },
                                onMoveDown = { viewModel.moveWorkflowStepDown(index) },
                                onDelete = { viewModel.removeWorkflowStepAt(index) }
                            )
                        }
                    }
                }
                OutlinedButton(
                    onClick = { goToTasksPage(TASKS_PAGE_TEMPLATE_PICKER) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(tr("Add step"))
                }
                SheetHeader(
                    title = tr("Trigger"),
                    subtitle = tr("Choose whether this workflow runs manually only, on a schedule, or from notifications.")
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("none", "schedule", "notification").forEach { type ->
                        FilterChip(
                            selected = workflowTriggerType == type,
                            onClick = { viewModel.workflowTriggerType.value = type },
                            label = { Text(tr(type.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })) }
                        )
                    }
                }
                if (workflowTriggerType == "schedule") {
                    ScheduleTriggerEditor(
                        runAtMs = workflowRunAtMs,
                        repeatMode = workflowRepeatMode,
                        repeatWeekdays = workflowRepeatWeekdays,
                        onRunAtChange = { viewModel.workflowRunAtMs.value = it },
                        onRepeatModeChange = { viewModel.workflowRepeatMode.value = it },
                        onRepeatWeekdaysChange = { viewModel.workflowRepeatWeekdays.value = it }
                    )
                } else if (workflowTriggerType == "notification") {
                    NotificationTriggerEditor(
                        installedApps = installedApps,
                        triggerPackage = workflowTriggerPackage,
                        titlePattern = workflowNotifyTitlePattern,
                        bodyPattern = workflowNotifyBodyPattern,
                        cooldownSeconds = workflowNotifyCooldownSeconds,
                        activeTimeStart = workflowNotifyActiveTimeStart,
                        activeTimeEnd = workflowNotifyActiveTimeEnd,
                        llmConditionEnabled = workflowNotifyLlmConditionEnabled,
                        llmCondition = workflowNotifyLlmCondition,
                        onRefreshApps = { viewModel.refreshInstalledAppSnapshotOnDevice() },
                        onTriggerPackageChange = { viewModel.workflowTriggerPackage.value = it },
                        onTitlePatternChange = { viewModel.workflowNotifyTitlePattern.value = it },
                        onBodyPatternChange = { viewModel.workflowNotifyBodyPattern.value = it },
                        onCooldownSecondsChange = { viewModel.workflowNotifyCooldownSeconds.value = it },
                        onActiveTimeStartChange = { viewModel.workflowNotifyActiveTimeStart.value = it },
                        onActiveTimeEndChange = { viewModel.workflowNotifyActiveTimeEnd.value = it },
                        onLlmConditionEnabledChange = { viewModel.workflowNotifyLlmConditionEnabled.value = it },
                        onLlmConditionChange = { viewModel.workflowNotifyLlmCondition.value = it }
                    )
                }
            }
        }

        TASKS_PAGE_TEMPLATE_PICKER -> {
            val listState = rememberLazyListState()
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                PageHeaderBlock(
                    title = tr("Select template"),
                    subtitle = tr("Choose an existing template to add to this workflow."),
                    glyph = "▣",
                    onBack = { goBackTasksPage() }
                )
                SurfacePanel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    background = MaterialTheme.colorScheme.surface,
                    borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.58f)
                ) {
                    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                        itemsIndexed(templates, key = { _, it -> it.templateId }) { index, template ->
                            CompactEntryRow(
                                title = template.name.ifBlank { template.description },
                                meta = template.packageName.ifBlank { tr("No app") },
                                onClick = {
                                    viewModel.addWorkflowStepTemplate(template.templateId)
                                    goBackTasksPage()
                                }
                            )
                            if (index < templates.lastIndex) {
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.18f))
                            }
                        }
                    }
                }
            }
        }

        TASKS_PAGE_RECENT_RUNS -> {
            val taskListState = rememberLazyListState()
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                PageHeaderBlock(
                    title = tr("Recent Runs"),
                    subtitle = tr("Review what just ran, what failed, and what route data was captured."),
                    glyph = "☰",
                    onBack = { goBackTasksPage() },
                    primaryActionLabel = tr("Refresh"),
                    onPrimaryAction = { viewModel.refreshTaskListOnDevice() }
                )
                if (portableNotice != null) {
                    PortableNoticeBanner(message = portableNotice!!, onDismiss = { portableNotice = null })
                }

                SummaryInfoStrip(
                    glyph = "☰",
                    title = tr("Run history"),
                    primaryMetric = "${tr("items")}: ${tasks.size}",
                    secondaryMetric = "${tr("FAILED")}: ${tasks.count { it.state == "FAILED" }}",
                    accentColor = if (tasks.any { it.state == "FAILED" }) AppWarning else MaterialTheme.colorScheme.primary
                )

                SurfacePanel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    background = MaterialTheme.colorScheme.surface,
                    borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.58f)
                ) {
                    if (tasks.isEmpty()) {
                        EmptyStateBox(
                            title = tr("No runs yet."),
                            detail = tr("Submit a quick task, or wait for a workflow trigger to fire."),
                            actionLabel = tr("Submit a quick task"),
                            onAction = { goBackToTasksPage(TASKS_PAGE_HOME) }
                        )
                    } else {
                        LazyColumn(
                            state = taskListState,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            itemsIndexed(tasks, key = { _, it -> it.taskId }) { index, task ->
                                TaskRow(task = task, onClick = { selectedTask = task })
                                if (index < tasks.lastIndex) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(horizontal = 18.dp),
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.18f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        TASKS_PAGE_ROUTE_EDITOR -> {
            val target = routeEditorTarget
            LaunchedEffect(target?.source, target?.routeId, target?.packageName, target?.userTask, target?.userPlaybook, target?.mode) {
                if (target != null) {
                    viewModel.clearTaskMapDetail()
                    viewModel.loadTaskMapDetailByQuery(
                        routeId = target.routeId,
                        packageName = target.packageName,
                        userTask = target.userTask,
                        userPlaybook = target.userPlaybook,
                        mode = target.mode
                    )
                }
            }
            if (target == null) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(onClick = { goBackToTasksPage(TASKS_PAGE_HOME) }) {
                        Text(tr("Back"))
                    }
                    Text(
                        text = tr("No task route data yet."),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            } else {
                TaskRouteEditorPage(
                    modifier = modifier,
                    routeDetail = taskMapDetail,
                    loading = taskMapDetailLoading,
                    saving = taskMapSaving,
                    onBack = { goBackTasksPage() },
                    onRefresh = {
                        viewModel.loadTaskMapDetailByQuery(
                            routeId = target.routeId,
                            packageName = target.packageName,
                            userTask = target.userTask,
                            userPlaybook = target.userPlaybook,
                            mode = target.mode
                        )
                    },
                    onSaveManualTaskMap = { deleteIds, finishAfterReplay ->
                        val key = taskMapDetail?.routeId.orEmpty().ifBlank { target.routeId }
                        viewModel.saveManualTaskMapByKey(
                            routeId = key,
                            deleteActionIds = deleteIds,
                            finishAfterReplay = finishAfterReplay
                        ) {
                            viewModel.loadTaskMapDetailByQuery(
                                routeId = target.routeId,
                                packageName = target.packageName,
                                userTask = target.userTask,
                                userPlaybook = target.userPlaybook,
                                mode = target.mode
                            )
                        }
                    }
                )
            }
        }
    }

    val detail = selectedTask
    if (detail != null) {
        TaskDetailDialog(
            task = detail,
            routeDetail = taskMapDetail,
            loading = taskMapDetailLoading,
            onDismiss = { selectedTask = null },
            onRefresh = { viewModel.loadTaskMapDetail(detail) }
        )
    }
}

@Composable
private fun TaskDetailDialog(
    task: TaskSummary,
    routeDetail: TaskMapDetail?,
    loading: Boolean,
    onDismiss: () -> Unit,
    onRefresh: () -> Unit
) {
    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(tr("Task Details")) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 560.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TaskSummarySection(task = task)
                if (loading) {
                    Text(
                        text = tr("Loading task route details..."),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
                    )
                }
                val effectiveDetail = routeDetail
                if (effectiveDetail != null) {
                    TaskMapMetaSection(detail = effectiveDetail)
                    SavedTaskMapSection(taskMap = effectiveDetail.taskMap)
                    TaskRouteRecordSection(
                        title = tr("Latest Success Trace"),
                        emptyText = tr("No latest success trace yet."),
                        record = effectiveDetail.latestSuccessRecord,
                        editable = false,
                        saving = false,
                        selectedDeleteIds = emptySet(),
                        onToggleDelete = {},
                        onSaveManual = {}
                    )
                    if (effectiveDetail.latestAttemptRecord != null) {
                        TaskRouteRecordSection(
                            title = tr("Latest Attempt Trace"),
                            emptyText = tr("No latest attempt trace yet."),
                            record = effectiveDetail.latestAttemptRecord,
                            editable = false,
                            saving = false,
                            selectedDeleteIds = emptySet(),
                            onToggleDelete = {},
                            onSaveManual = {}
                        )
                    }
                }
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onRefresh) {
                    Text(tr("Refresh detail"))
                }
                OutlinedButton(onClick = onDismiss) {
                    Text(tr("Close"))
                }
            }
        }
    )
}

@Composable
private fun TaskRouteEditorPage(
    modifier: Modifier = Modifier,
    routeDetail: TaskMapDetail?,
    loading: Boolean,
    saving: Boolean,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onSaveManualTaskMap: (List<String>, Boolean) -> Unit
) {
    val scrollState = rememberScrollState()
    val editableRecord = routeDetail?.latestAttemptRecord ?: routeDetail?.latestSuccessRecord
    var deleteActionIds by remember(editableRecord?.createdAtMs, editableRecord?.actions?.size) {
        mutableStateOf(setOf<String>())
    }
    var finishAfterReplay by remember(routeDetail?.taskMap?.createdAtMs, routeDetail?.taskMap?.finishAfterReplay) {
        mutableStateOf(routeDetail?.taskMap?.finishAfterReplay ?: false)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        PageHeaderBlock(
            title = tr("Route Editor"),
            subtitle = tr("Review the latest captured path, delete noisy actions, and save only the useful route."),
            glyph = "⇄",
            onBack = onBack,
            primaryActionLabel = tr("Refresh"),
            onPrimaryAction = onRefresh
        )


        if (loading) {
            Text(
                text = tr("Loading task route details..."),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
            )
        }
        if (!loading && routeDetail == null) {
            Text(
                text = tr("No task route data yet."),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
            )
        }
        if (routeDetail != null) {
            SavedTaskMapSection(taskMap = routeDetail.taskMap)
            TaskRouteRecordSection(
                title = tr("Current Route"),
                emptyText = tr("No captured trace yet."),
                record = editableRecord,
                editable = true,
                saving = saving,
                selectedDeleteIds = deleteActionIds,
                finishAfterReplay = finishAfterReplay,
                onFinishAfterReplayChange = { finishAfterReplay = it },
                onToggleDelete = { actionId ->
                    deleteActionIds = if (deleteActionIds.contains(actionId)) {
                        deleteActionIds - actionId
                    } else {
                        deleteActionIds + actionId
                    }
                },
                onSaveManual = { onSaveManualTaskMap(deleteActionIds.toList(), finishAfterReplay) }
            )
        }
    }
}

@Composable
private fun TaskSummarySection(task: TaskSummary) {
    Card {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = if (task.taskSummary.isNotBlank()) task.taskSummary else tr("No task summary available yet."),
                style = MaterialTheme.typography.bodyMedium
            )
            DetailTextLine(tr("Task ID"), task.taskId)
            DetailTextLine(
                tr("State"),
                "${tr(task.state)}" + if (task.finalState.isNotBlank()) " / ${tr(task.finalState)}" else ""
            )
            if (task.reason.isNotBlank()) {
                Text(
                    text = "${tr("Reason")}=${task.reason}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.error
                )
            }
            if (task.userTask.isNotBlank()) DetailTextLine(tr("Task"), task.userTask)
            if (task.packageName.isNotBlank()) DetailTextLine(tr("Package name"), task.packageName)
            if (task.targetPage.isNotBlank()) DetailTextLine(tr("Target page"), task.targetPage)
            if (task.source.isNotBlank()) DetailTextLine(tr("Source"), task.source)
            if (task.taskMapMode.isNotBlank()) DetailTextLine(tr("Task route mode"), formatTaskRouteMode(task.taskMapMode))
            DetailTextLine(tr("Saved task route"), if (task.hasTaskMap) tr("Yes") else tr("No"))
            if (task.scheduleId.isNotBlank()) DetailTextLine(tr("Schedule ID"), task.scheduleId)
            DetailTextLine(tr("Memory applied"), if (task.memoryApplied) tr("Yes") else tr("No"))
            DetailTextLine(tr("Record enabled"), if (task.recordEnabled) tr("Yes") else tr("No"))
            if (task.recordFile.isNotBlank()) DetailTextLine(tr("Record file"), task.recordFile)
            if (task.createdAt > 0L) DetailTextLine(tr("Created at"), formatTsFull(task.createdAt))
            if (task.finishedAt > 0L) DetailTextLine(tr("Finished at"), formatTsFull(task.finishedAt))
        }
    }
}

@Composable
private fun TaskMapMetaSection(detail: TaskMapDetail) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(tr("Task route detail"), style = MaterialTheme.typography.labelLarge)
            if (detail.routeId.isNotBlank()) DetailTextLine(tr("Route ID"), detail.routeId)
            if (detail.mode.isNotBlank()) DetailTextLine(tr("Task route mode"), formatTaskRouteMode(detail.mode))
            if (detail.source.isNotBlank()) DetailTextLine(tr("Source"), detail.source)
            if (detail.sourceId.isNotBlank()) DetailTextLine(tr("Source ID"), detail.sourceId)
            if (detail.packageName.isNotBlank()) DetailTextLine(tr("Package name"), detail.packageName)
            if (detail.userTask.isNotBlank()) DetailTextLine(tr("Task"), detail.userTask)
            DetailTextLine(tr("Saved task route"), if (detail.hasMap) tr("Yes") else tr("No"))
            DetailTextLine(tr("Finish after replay"), if (detail.taskMap?.finishAfterReplay == true) tr("Yes") else tr("No"))
            DetailTextLine(tr("Latest success trace"), if (detail.hasLatestSuccessRecord) tr("Yes") else tr("No"))
            DetailTextLine(tr("Latest attempt trace"), if (detail.hasLatestAttemptRecord) tr("Yes") else tr("No"))
        }
    }
}

@Composable
private fun SavedTaskMapSection(taskMap: TaskMapSnapshot?) {
    var selectedStep by remember(taskMap?.createdAtMs, taskMap?.segments?.size) {
        mutableStateOf<TaskMapStepSnapshot?>(null)
    }
    SurfacePanel(
        modifier = Modifier.fillMaxWidth(),
        background = MaterialTheme.colorScheme.surface,
        borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.52f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            SheetHeader(
                title = tr("Saved Route"),
                subtitle = tr("Open any step to inspect locator, expected result, and fallback details.")
            )
            if (taskMap == null || taskMap.segments.isEmpty()) {
                Text(
                    text = tr("No saved route yet."),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
                )
            } else {
                taskMap.segments.forEachIndexed { index, segment ->
                    SavedRouteSegmentBlock(
                        index = index,
                        segment = segment,
                        onOpenStep = { selectedStep = it }
                    )
                }
            }
        }
    }
    selectedStep?.let { step ->
        TaskMapStepDetailDialog(
            step = step,
            onDismiss = { selectedStep = null }
        )
    }
}

@Composable
private fun SavedRouteSegmentBlock(
    index: Int,
    segment: TaskMapSegmentSnapshot,
    onOpenStep: (TaskMapStepSnapshot) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = "${tr("Segment")} ${index + 1}",
                    style = MaterialTheme.typography.labelLarge
                )
                if (segment.subTaskDescription.isNotBlank()) {
                    Text(
                        text = segment.subTaskDescription,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.76f)
                    )
                }
            }
            StatusTag(
                text = "${segment.steps.size} ${tr("Step")}",
                accentColor = MaterialTheme.colorScheme.primary
            )
        }
        segment.steps.forEachIndexed { stepIndex, step ->
            SavedRouteStepRow(
                index = stepIndex,
                step = step,
                onClick = { onOpenStep(step) }
            )
        }
        if (segment.successCriteria.isNotBlank()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = tr("Expected"),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = segment.successCriteria,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
                )
            }
        }
        if (segment.inputs.isNotEmpty() || segment.outputs.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (segment.inputs.isNotEmpty()) {
                    CompactMetaPill(
                        modifier = Modifier.weight(1f),
                        label = tr("Input"),
                        value = segment.inputs.joinToString(" / "),
                        accentColor = MaterialTheme.colorScheme.secondary
                    )
                }
                if (segment.outputs.isNotEmpty()) {
                    CompactMetaPill(
                        modifier = Modifier.weight(1f),
                        label = tr("Output"),
                        value = segment.outputs.joinToString(" / "),
                        accentColor = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.18f))
    }
}

@Composable
private fun SavedRouteStepRow(
    index: Int,
    step: TaskMapStepSnapshot,
    onClick: () -> Unit
) {
    val summary = buildString {
        append(step.op.ifBlank { tr("Step") })
        if (step.args.isNotEmpty()) {
            append(": ")
            append(step.args.joinToString(" "))
        }
    }
    SurfacePanel(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        background = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.38f).compositeOver(MaterialTheme.colorScheme.surface),
        borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            StatusTag(
                text = "${index + 1}",
                accentColor = MaterialTheme.colorScheme.primary
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = summary,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.86f)
                )
                val hint = when {
                    step.portableKind.isNotBlank() && step.adaptationStatus.isNotBlank() ->
                        buildString {
                            append(step.portableKind)
                            append(" / ")
                            append(step.adaptationStatus)
                            if (step.adaptationError.isNotBlank()) {
                                append(" / ")
                                append(step.adaptationError)
                            }
                        }
                    step.semanticNote.isNotBlank() -> step.semanticNote
                    step.expected.isNotBlank() -> step.expected
                    else -> tr("Tap to view locator and fallback details.")
                }
                Text(
                    text = hint,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = tr("Details"),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun CompactMetaPill(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    accentColor: Color
) {
    SurfacePanel(
        modifier = modifier,
        background = accentColor.copy(alpha = 0.08f).compositeOver(MaterialTheme.colorScheme.surface),
        borderColor = accentColor.copy(alpha = 0.16f),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = label,
                fontSize = 10.sp,
                color = accentColor
            )
            Text(
                text = value,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.76f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun TaskMapStepDetailDialog(
    step: TaskMapStepSnapshot,
    onDismiss: () -> Unit
) {
    val scrollState = rememberScrollState()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(tr("Route Step")) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 560.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (step.stepId.isNotBlank()) DetailTextLine(tr("Step ID"), step.stepId)
                if (step.sourceActionId.isNotBlank()) DetailTextLine(tr("Source action ID"), step.sourceActionId)
                if (step.op.isNotBlank()) DetailTextLine(tr("Operation"), step.op)
                if (step.args.isNotEmpty()) DetailTextLine(tr("Arguments"), step.args.joinToString(" "))
                if (step.portableKind.isNotBlank()) DetailTextLine(tr("Portable kind"), step.portableKind)
                if (step.adaptationStatus.isNotBlank()) DetailTextLine(tr("Adaptation status"), step.adaptationStatus)
                if (step.adaptationError.isNotBlank()) DetailTextLine(tr("Adaptation error"), step.adaptationError)
                if (step.materializedFromStepId.isNotBlank()) DetailTextLine(tr("Materialized from"), step.materializedFromStepId)
                if (step.materializedAtMs > 0L) DetailTextLine(tr("Materialized at"), formatTsFull(step.materializedAtMs))
                if (step.semanticNote.isNotBlank()) DetailTextLine(tr("Page semantics"), step.semanticNote)
                if (step.expected.isNotBlank()) DetailTextLine(tr("Expected"), step.expected)
                if (step.tapPoint.isNotBlank()) DetailTextLine(tr("Tap point"), step.tapPoint)
                if (step.fallbackPoint.isNotBlank()) DetailTextLine(tr("Fallback point"), step.fallbackPoint)
                if (step.locatorFields.isNotEmpty()) {
                    TraceDetailSection(title = tr("Locator"), items = step.locatorFields)
                }
                if (step.containerProbeFields.isNotEmpty()) {
                    TraceDetailSection(title = tr("Container probe"), items = step.containerProbeFields)
                }
                if (step.semanticDescriptorFields.isNotEmpty()) {
                    TraceDetailSection(title = tr("Semantic descriptor"), items = step.semanticDescriptorFields)
                }
                if (step.swipeFields.isNotEmpty()) {
                    TraceDetailSection(title = tr("Swipe"), items = step.swipeFields)
                }
            }
        },
        confirmButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(tr("Close"))
            }
        }
    )
}

@Composable
private fun RemovedTaskRouteSection(
    actions: List<TaskRouteActionSnapshot>,
    onRestore: (String) -> Unit,
    onOpenDetail: (TaskRouteActionSnapshot) -> Unit
) {
    if (actions.isEmpty()) return
    SurfacePanel(
        modifier = Modifier.fillMaxWidth(),
        background = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.28f).compositeOver(MaterialTheme.colorScheme.surface),
        borderColor = MaterialTheme.colorScheme.error.copy(alpha = 0.14f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SheetHeader(
                title = tr("Removed from Route"),
                subtitle = tr("Removed actions will not be saved into the final route.")
            )
            actions.forEachIndexed { index, action ->
                TaskRouteActionCard(
                    index = index,
                    action = action,
                    editable = true,
                    removed = true,
                    onToggleRouteState = { onRestore(action.actionId) },
                    onOpenDetail = { onOpenDetail(action) }
                )
            }
        }
    }
}

@Composable
private fun TaskRouteRecordSection(
    title: String,
    emptyText: String,
    record: TaskRouteRecordSnapshot?,
    editable: Boolean,
    saving: Boolean,
    selectedDeleteIds: Set<String>,
    finishAfterReplay: Boolean = false,
    onFinishAfterReplayChange: (Boolean) -> Unit = {},
    onToggleDelete: (String) -> Unit,
    onSaveManual: () -> Unit
) {
    var selectedAction by remember(record?.createdAtMs, record?.actions?.size) {
        mutableStateOf<TaskRouteActionSnapshot?>(null)
    }
    val actions = record?.actions.orEmpty()
    val keptActions = if (editable) {
        actions.filterNot { selectedDeleteIds.contains(it.actionId) }
    } else {
        actions
    }
    val removedActions = if (editable) {
        actions.filter { selectedDeleteIds.contains(it.actionId) }
    } else {
        emptyList()
    }
    // Batch 1: removing steps is staged (and restorable) until Save is tapped,
    // so the confirm dialog belongs on the Save action, not on each toggle.
    var showRemoveStepsConfirm by rememberSaveable { mutableStateOf(false) }
    if (showRemoveStepsConfirm) {
        DeleteConfirmDialog(
            itemName = "${selectedDeleteIds.size} ${tr("steps to remove")}",
            title = tr("Remove steps from route"),
            warning = tr("Saving will remove the selected steps from this route."),
            onConfirm = onSaveManual,
            onDismiss = { showRemoveStepsConfirm = false }
        )
    }
    SurfacePanel(
        modifier = Modifier.fillMaxWidth(),
        background = MaterialTheme.colorScheme.surface,
        borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.52f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SheetHeader(
                title = title,
                subtitle = tr("Tap a card to view full details.")
            )
            if (record == null) {
                Text(
                    text = emptyText,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
                )
                return@Column
            }
            if (editable) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { if (selectedDeleteIds.isEmpty()) onSaveManual() else showRemoveStepsConfirm = true },
                        enabled = !saving && keptActions.isNotEmpty()
                    ) {
                        Text(if (saving) tr("Saving...") else tr("Save manual route"))
                    }
                }
                SurfacePanel(
                    modifier = Modifier.fillMaxWidth(),
                    background = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.24f)
                        .compositeOver(MaterialTheme.colorScheme.surface),
                    borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(tr("Finish after replay"), style = MaterialTheme.typography.titleSmall)
                            Text(
                                text = tr("This option is saved only when you tap Save manual route."),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        Switch(
                            checked = finishAfterReplay,
                            onCheckedChange = onFinishAfterReplayChange
                        )
                    }
                }
            }
            if (keptActions.isEmpty()) {
                Text(
                    if (editable) tr("No actions left in route.") else tr("No trace actions yet."),
                    fontSize = 12.sp
                )
            } else {
                keptActions.forEachIndexed { index, action ->
                    TaskRouteActionCard(
                        index = index,
                        action = action,
                        editable = editable,
                        removed = false,
                        onToggleRouteState = { onToggleDelete(action.actionId) },
                        onOpenDetail = { selectedAction = action }
                    )
                }
            }
        }
    }
    if (editable) {
        RemovedTaskRouteSection(
            actions = removedActions,
            onRestore = onToggleDelete,
            onOpenDetail = { selectedAction = it }
        )
    }
    selectedAction?.let { action ->
        TaskRouteActionDetailDialog(
            action = action,
            onDismiss = { selectedAction = null }
        )
    }
}

@Composable
private fun TaskRouteActionCard(
    index: Int,
    action: TaskRouteActionSnapshot,
    editable: Boolean,
    removed: Boolean,
    onToggleRouteState: () -> Unit,
    onOpenDetail: () -> Unit
) {
    val summary = when {
        action.createdPageSemantics.isNotBlank() -> action.createdPageSemantics
        action.rawCommand.isNotBlank() -> action.rawCommand
        action.args.isNotEmpty() -> action.args.joinToString(" ")
        else -> action.op
    }
    val baseAccentColor = if (removed) {
        MaterialTheme.colorScheme.error
    } else if (action.execError.isNotBlank()) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.primary
    }
    val background = if (removed) {
        MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.28f).compositeOver(MaterialTheme.colorScheme.surface)
    } else {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.32f).compositeOver(MaterialTheme.colorScheme.surface)
    }
    val border = if (removed) {
        MaterialTheme.colorScheme.error.copy(alpha = 0.14f)
    } else {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    }
    val hint = when {
        action.execError.isNotBlank() -> action.execError
        action.execResult.isNotBlank() -> action.execResult
        else -> summary
    }
    SurfacePanel(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenDetail() },
        background = background,
        borderColor = border,
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            StatusTag(
                text = "${index + 1}",
                accentColor = baseAccentColor
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = summary.take(160),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.86f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${tr("Operation")}=${action.op.ifBlank { "-" }}  •  ${tr("Turn")}=${action.turn}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f)
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = tr("Details"),
                            fontSize = 11.sp,
                            color = baseAccentColor
                        )
                        if (editable) {
                            TextButton(
                                onClick = onToggleRouteState,
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 0.dp, vertical = 0.dp)
                            ) {
                                Text(
                                    if (removed) tr("Restore to route") else tr("Move out of route"),
                                    fontSize = 11.sp,
                                    color = if (removed) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.error
                                    }
                                )
                            }
                        }
                    }
                }
                Text(
                    text = hint.take(180),
                    fontSize = 11.sp,
                    color = if (action.execError.isNotBlank()) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f)
                    },
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun TaskRouteActionDetailDialog(
    action: TaskRouteActionSnapshot,
    onDismiss: () -> Unit
) {
    val scrollState = rememberScrollState()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(tr("Action")) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 560.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (action.actionId.isNotBlank()) DetailTextLine(tr("Action ID"), action.actionId)
                if (action.subTaskId.isNotBlank()) DetailTextLine(tr("Sub-task ID"), action.subTaskId)
                DetailTextLine(tr("Turn"), action.turn.toString())
                if (action.op.isNotBlank()) DetailTextLine(tr("Operation"), action.op)
                if (action.args.isNotEmpty()) DetailTextLine(tr("Arguments"), action.args.joinToString(" "))
                if (action.rawCommand.isNotBlank()) DetailTextLine(tr("Command"), action.rawCommand)
                if (action.createdPageSemantics.isNotBlank()) DetailTextLine(tr("Page semantics"), action.createdPageSemantics)
                if (action.execResult.isNotBlank()) DetailTextLine(tr("Execution result"), action.execResult)
                if (action.execError.isNotBlank()) DetailTextLine(tr("Execution error"), action.execError)
                if (action.locatorFields.isNotEmpty()) {
                    TraceDetailSection(title = tr("Locator"), items = action.locatorFields)
                }
                if (action.visionFields.isNotEmpty()) {
                    TraceDetailSection(title = tr("Vision fields"), items = action.visionFields)
                }
            }
        },
        confirmButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(tr("Close"))
            }
        }
    )
}

@Composable
private fun DetailTextLine(label: String, value: String) {
    if (value.isBlank()) return
    Text("$label=$value", fontSize = 11.sp)
}

@Composable
fun TemplateRow(
    template: TaskTemplateSummary,
    onRun: () -> Unit,
    onEdit: () -> Unit,
    onRoute: () -> Unit,
    onDelete: () -> Unit
) {
    val scheme = MaterialTheme.colorScheme
    var showDeleteConfirm by rememberSaveable { mutableStateOf(false) }
    if (showDeleteConfirm) {
        DeleteConfirmDialog(
            itemName = template.name.ifBlank { template.description },
            title = tr("Delete template"),
            warning = tr("This item will be permanently deleted and cannot be restored."),
            onConfirm = onDelete,
            onDismiss = { showDeleteConfirm = false }
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEdit)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = template.name.ifBlank { template.description },
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = template.description,
            fontSize = 11.sp,
            color = scheme.onSurface.copy(alpha = 0.72f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = buildString {
                append(if (template.packageName.isBlank()) tr("No app") else template.packageName)
                append("  •  ")
                append(if (template.routeId.isBlank()) tr("No route") else tr("Route ready"))
            },
            fontSize = 10.sp,
            color = scheme.onSurface.copy(alpha = 0.58f)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onRun,
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                modifier = Modifier.height(48.dp)
            ) { Text(tr("Trigger now"), fontSize = 13.sp, maxLines = 1) }
            TextButton(
                onClick = onRoute,
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                modifier = Modifier.height(48.dp)
            ) { Text(tr("Route"), fontSize = 13.sp, maxLines = 1) }
            TextButton(
                onClick = { showDeleteConfirm = true },
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                modifier = Modifier.height(48.dp)
            ) { Text(tr("Delete"), fontSize = 13.sp, maxLines = 1, color = scheme.error) }
        }
    }
}

@Composable
fun WorkflowRow(
    workflow: WorkflowSummary,
    onRun: () -> Unit,
    onEdit: () -> Unit,
    onToggleEnabled: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    val scheme = MaterialTheme.colorScheme
    val listToggleColors = SwitchDefaults.colors(
        checkedThumbColor = scheme.surface,
        checkedTrackColor = AppSuccess,
        checkedBorderColor = AppSuccess,
        uncheckedThumbColor = scheme.surface,
        uncheckedTrackColor = scheme.primary.copy(alpha = 0.22f).compositeOver(scheme.surface),
        uncheckedBorderColor = scheme.primary.copy(alpha = 0.45f)
    )
    var showDeleteConfirm by rememberSaveable { mutableStateOf(false) }
    if (showDeleteConfirm) {
        DeleteConfirmDialog(
            itemName = workflow.name,
            title = tr("Delete workflow"),
            warning = tr("This item will be permanently deleted and cannot be restored."),
            onConfirm = onDelete,
            onDismiss = { showDeleteConfirm = false }
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEdit)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = workflow.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = workflow.triggerSummary.ifBlank {
                    when (workflow.triggerType) {
                        "none" -> tr("No trigger")
                        "schedule" -> tr("Scheduled")
                        "notification" -> tr("Notification")
                        else -> workflow.triggerType
                    }
                },
                fontSize = 11.sp,
                color = scheme.onSurface.copy(alpha = 0.72f)
            )
        }
        Switch(
            checked = workflow.triggerEnabled,
            onCheckedChange = onToggleEnabled,
            enabled = workflow.triggerType != "none",
            colors = listToggleColors
        )
        TextButton(onClick = onRun) { Text(tr("Run")) }
        TextButton(onClick = { showDeleteConfirm = true }) { Text(tr("Delete")) }
    }
}

@Composable
fun CompactEntryRow(title: String, meta: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(meta, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f), maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Text(tr("Open"), fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun WorkflowStepEditorRow(
    index: Int,
    templateName: String,
    packageName: String,
    onOpen: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpen)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text("${index + 1}. $templateName", style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(packageName.ifBlank { tr("No app") }, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f))
        }
        TextButton(onClick = onMoveUp, enabled = index > 0) { Text(tr("Up")) }
        TextButton(onClick = onMoveDown) { Text(tr("Down")) }
        TextButton(onClick = onDelete) { Text(tr("Delete")) }
    }
}

@Composable
fun ScheduleTriggerEditor(
    runAtMs: String,
    repeatMode: String,
    repeatWeekdays: Int,
    onRunAtChange: (String) -> Unit,
    onRepeatModeChange: (String) -> Unit,
    onRepeatWeekdaysChange: (Int) -> Unit
) {
    val context = LocalContext.current
    val selectedRunAt = runAtMs.toLongOrNull()?.takeIf { it > 0L } ?: (System.currentTimeMillis() + 5 * 60_000L)
    var showTimeWheel by rememberSaveable { mutableStateOf(false) }
    Text(
        text = "${tr("Run time")}: ${formatTsFull(selectedRunAt)}",
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
    )
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton(
            onClick = {
                val cal = Calendar.getInstance().apply { timeInMillis = selectedRunAt }
                DatePickerDialog(
                    context,
                    { _, y, m, d ->
                        val next = Calendar.getInstance().apply {
                            timeInMillis = selectedRunAt
                            set(Calendar.YEAR, y)
                            set(Calendar.MONTH, m)
                            set(Calendar.DAY_OF_MONTH, d)
                        }
                        onRunAtChange(next.timeInMillis.toString())
                    },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
            modifier = Modifier.weight(1f)
        ) { Text(tr("Pick Date")) }
        OutlinedButton(onClick = { showTimeWheel = true }, modifier = Modifier.weight(1f)) { Text(tr("Pick Time")) }
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(MainViewModel.REPEAT_ONCE, MainViewModel.REPEAT_DAILY, MainViewModel.REPEAT_WEEKLY).forEach { mode ->
            RepeatModeButton(
                text = tr(mode.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }),
                selected = repeatMode == mode,
                onClick = { onRepeatModeChange(mode) },
                modifier = Modifier.weight(1f)
            )
        }
    }
    if (repeatMode == MainViewModel.REPEAT_WEEKLY) {
        val labels = listOf(tr("Mon"), tr("Tue"), tr("Wed"), tr("Thu"), tr("Fri"), tr("Sat"), tr("Sun"))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            for (i in 0..6) {
                WeekdayButton(
                    text = labels[i],
                    selected = ((repeatWeekdays shr i) and 1) == 1,
                    onClick = { onRepeatWeekdaysChange(repeatWeekdays xor (1 shl i)) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
    if (showTimeWheel) {
        val cal = Calendar.getInstance().apply { timeInMillis = selectedRunAt }
        WheelTimePickerDialog(
            initialHour = cal.get(Calendar.HOUR_OF_DAY),
            initialMinute = cal.get(Calendar.MINUTE),
            onDismiss = { showTimeWheel = false },
            onConfirm = { hour, minute ->
                val next = Calendar.getInstance().apply {
                    timeInMillis = selectedRunAt
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                onRunAtChange(next.timeInMillis.toString())
                showTimeWheel = false
            }
        )
    }
}

@Composable
fun NotificationTriggerEditor(
    installedApps: List<AppPackageOption>,
    triggerPackage: String,
    titlePattern: String,
    bodyPattern: String,
    cooldownSeconds: String,
    activeTimeStart: String,
    activeTimeEnd: String,
    llmConditionEnabled: Boolean,
    llmCondition: String,
    onRefreshApps: () -> Unit,
    onTriggerPackageChange: (String) -> Unit,
    onTitlePatternChange: (String) -> Unit,
    onBodyPatternChange: (String) -> Unit,
    onCooldownSecondsChange: (String) -> Unit,
    onActiveTimeStartChange: (String) -> Unit,
    onActiveTimeEndChange: (String) -> Unit,
    onLlmConditionEnabledChange: (Boolean) -> Unit,
    onLlmConditionChange: (String) -> Unit
) {
    var showAppPicker by rememberSaveable { mutableStateOf(false) }
    SheetHeader(title = tr("Trigger conditions"), subtitle = tr("Choose which notifications should start this workflow."))
    PackageSelectField(
        title = tr("Listen app"),
        selectedPackage = triggerPackage,
        options = installedApps,
        onOpen = {
            if (installedApps.isEmpty()) {
                onRefreshApps()
            }
            showAppPicker = true
        },
        onClear = { onTriggerPackageChange("") }
    )
    OutlinedTextField(value = titlePattern, onValueChange = onTitlePatternChange, label = { Text(tr("Title pattern")) }, modifier = Modifier.fillMaxWidth())
    OutlinedTextField(value = bodyPattern, onValueChange = onBodyPatternChange, label = { Text(tr("Body pattern")) }, modifier = Modifier.fillMaxWidth())
    OutlinedTextField(value = cooldownSeconds, onValueChange = onCooldownSecondsChange, label = { Text(tr("Cooldown seconds")) }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value = activeTimeStart, onValueChange = onActiveTimeStartChange, label = { Text(tr("Start time")) }, modifier = Modifier.weight(1f))
        OutlinedTextField(value = activeTimeEnd, onValueChange = onActiveTimeEndChange, label = { Text(tr("End time")) }, modifier = Modifier.weight(1f))
    }
    PreferenceSwitchRow(
        title = tr("Use LLM condition"),
        detail = tr("Add an extra model check after the text match."),
        checked = llmConditionEnabled,
        onCheckedChange = onLlmConditionEnabledChange
    )
    if (llmConditionEnabled) {
        OutlinedTextField(value = llmCondition, onValueChange = onLlmConditionChange, label = { Text(tr("LLM condition")) }, modifier = Modifier.fillMaxWidth(), minLines = 2)
    }
    if (showAppPicker) {
        PackagePickerDialog(
            options = installedApps,
            onDismiss = { showAppPicker = false },
            onSelect = {
                onTriggerPackageChange(it.packageName)
                showAppPicker = false
            }
        )
    }
}

@Composable
private fun PackageSelectField(
    title: String,
    selectedPackage: String,
    options: List<AppPackageOption>,
    onOpen: () -> Unit,
    onClear: () -> Unit,
    error: String? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = title,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onOpen,
                modifier = Modifier.weight(1f)
            ) {
                val display = if (selectedPackage.isBlank()) {
                    tr("Select App")
                } else {
                    formatPackageDisplay(selectedPackage, options)
                }
                Text(display, maxLines = 1)
            }
            OutlinedButton(
                onClick = onClear,
                enabled = selectedPackage.isNotBlank()
            ) {
                Text(tr("Clear"), fontSize = 12.sp)
            }
        }
        if (!error.isNullOrBlank()) {
            Text(
                text = tr(error),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun PackagePickerDialog(
    options: List<AppPackageOption>,
    onDismiss: () -> Unit,
    onSelect: (AppPackageOption) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }
    val filtered = remember(options, query) {
        val key = query.trim().lowercase(Locale.getDefault())
        if (key.isEmpty()) {
            options
        } else {
            options.filter { item ->
                item.packageName.lowercase(Locale.getDefault()).contains(key) ||
                        item.label.lowercase(Locale.getDefault()).contains(key)
            }
        }
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(tr("Select App")) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text(tr("Search apps")) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                if (filtered.isEmpty()) {
                    Text(
                        text = tr("No app found."),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 320.dp)
                    ) {
                        itemsIndexed(filtered, key = { _, it -> it.packageName }) { index, item ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelect(item) }
                                    .padding(vertical = 8.dp)
                            ) {
                                Text(
                                    text = formatPackageDisplay(item.packageName, options),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            if (index < filtered.lastIndex) {
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(tr("Close"))
            }
        }
    )
}

@Composable
fun TaskRow(task: TaskSummary, onClick: () -> Unit) {
    val scheme = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        val accentColor = when (task.state) {
            "FAILED" -> scheme.error
            "RUNNING" -> scheme.primary
            "COMPLETED" -> AppSuccess
            else -> scheme.onSurface.copy(alpha = 0.55f)
        }
        GlyphBadge(
            glyph = when (task.state) {
                "FAILED" -> "!"
                "RUNNING" -> "▶"
                "COMPLETED" -> "✓"
                else -> "•"
            },
            accentColor = accentColor
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = if (task.userTask.isNotEmpty()) task.userTask else tr("(no task description)"),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (task.state == "FAILED") scheme.error else scheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                StatusTag(
                    text = tr(task.state),
                    accentColor = accentColor
                )
            }
            val stateParts = mutableListOf(tr(task.state))
            if (task.finalState.isNotEmpty()) {
                stateParts += tr(task.finalState)
            }
            if (task.packageName.isNotEmpty()) {
                stateParts += task.packageName
            }
            if (task.source.isNotEmpty()) {
                stateParts += task.source
            }
            if (task.memoryApplied) {
                stateParts += tr("Memory applied")
            }
            Text(
                text = stateParts.joinToString(" / "),
                fontSize = 11.sp,
                color = scheme.onSurface.copy(alpha = 0.75f)
            )
            if (task.reason.isNotEmpty()) {
                Text(
                    text = "${tr("Reason")}: ${task.reason}",
                    fontSize = 10.sp,
                    color = scheme.error
                )
            }
        }
    }
}

@Composable
private fun WheelTimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    var hour by rememberSaveable { mutableIntStateOf(initialHour.coerceIn(0, 23)) }
    var minute by rememberSaveable { mutableIntStateOf(initialMinute.coerceIn(0, 59)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(tr("Select Time")) },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(tr("Hour"), fontSize = 12.sp)
                    AndroidView(
                        factory = { ctx ->
                            NumberPicker(ctx).apply {
                                minValue = 0
                                maxValue = 23
                                wrapSelectorWheel = true
                                descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                                setFormatter { String.format(Locale.getDefault(), "%02d", it) }
                                styleWheelNumberPicker(this)
                            }
                        },
                        update = { picker ->
                            styleWheelNumberPicker(picker)
                            if (picker.value != hour) {
                                picker.value = hour
                            }
                            picker.setOnValueChangedListener { _, _, newVal ->
                                hour = newVal
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(tr("Minute"), fontSize = 12.sp)
                    AndroidView(
                        factory = { ctx ->
                            NumberPicker(ctx).apply {
                                minValue = 0
                                maxValue = 59
                                wrapSelectorWheel = true
                                descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                                setFormatter { String.format(Locale.getDefault(), "%02d", it) }
                                styleWheelNumberPicker(this)
                            }
                        },
                        update = { picker ->
                            styleWheelNumberPicker(picker)
                            if (picker.value != minute) {
                                picker.value = minute
                            }
                            picker.setOnValueChangedListener { _, _, newVal ->
                                minute = newVal
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(hour, minute) }) {
                Text(tr("OK"))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(tr("Cancel"))
            }
        }
    )
}

private fun styleWheelNumberPicker(picker: NumberPicker) {
    runCatching {
        picker.setBackgroundColor(android.graphics.Color.WHITE)
        for (i in 0 until picker.childCount) {
            val child = picker.getChildAt(i)
            if (child is EditText) {
                child.setTextColor(android.graphics.Color.BLACK)
                child.textSize = 20f
            }
        }
    }
}

@Composable
private fun RepeatModeButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (selected) {
        Button(onClick = onClick, modifier = modifier) {
            Text(text)
        }
    } else {
        OutlinedButton(onClick = onClick, modifier = modifier) {
            Text(text)
        }
    }
}

@Composable
private fun WeekdayButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (selected) {
        Button(onClick = onClick, modifier = modifier) {
            Text(text, fontSize = 11.sp)
        }
    } else {
        OutlinedButton(onClick = onClick, modifier = modifier) {
            Text(text, fontSize = 11.sp)
        }
    }
}

private fun formatTsFull(ms: Long): String {
    if (ms <= 0L) return "-"
    return runCatching {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        sdf.format(Date(ms))
    }.getOrElse { "-" }
}

private fun formatPackageDisplay(packageName: String, options: List<AppPackageOption>): String {
    if (packageName.isBlank()) return ""
    val row = options.firstOrNull { it.packageName == packageName }
    val label = row?.label?.trim().orEmpty()
    return if (label.isNotEmpty()) {
        "$label ($packageName)"
    } else {
        packageName
    }
}

@Composable
private fun formatTaskRouteMode(modeRaw: String): String {
    return when (modeRaw.trim().lowercase(Locale.getDefault())) {
        MainViewModel.TASK_MAP_MODE_MANUAL, MainViewModel.TASK_MAP_MODE_AI -> tr("On")
        else -> tr("Off")
    }
}

@Composable
private fun formatWeekdayMask(mask: Int): String {
    if ((mask and 0x7F) == 0) return "-"
    val labels = listOf(tr("Mon"), tr("Tue"), tr("Wed"), tr("Thu"), tr("Fri"), tr("Sat"), tr("Sun"))
    val selected = mutableListOf<String>()
    for (i in 0..6) {
        if (((mask shr i) and 1) == 1) {
            selected.add(labels[i])
        }
    }
    return if (selected.isEmpty()) "-" else selected.joinToString(", ")
}

@Composable
private fun formatRepeat(modeRaw: String, weekdays: Int): String {
    return when (modeRaw.lowercase(Locale.getDefault())) {
        MainViewModel.REPEAT_DAILY -> tr("Daily")
        MainViewModel.REPEAT_WEEKLY -> "${tr("Weekly")}(${formatWeekdayMask(weekdays)})"
        else -> tr("Once")
    }
}

@Composable
fun ProcessRuntimeCard(status: CoreRuntimeStatus) {
    val (bgColor, label) = if (status.ready) {
        Color(0xFF4CAF50) to tr("Core Connected")
    } else {
        Color(0xFF9E9E9E) to tr("Core Disconnected")
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(label, color = Color.White, style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(2.dp))
            Text(status.detail, color = Color.White.copy(alpha = 0.92f), fontSize = 12.sp)
        }
    }
}

@Composable
fun UnifiedStopCard(
    onStop: () -> Unit,
    onRefreshState: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(tr("Unified stop"), style = MaterialTheme.typography.titleSmall)
            Text(
                tr("Stop core process regardless of start mode."),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                fontSize = 12.sp
            )
            OutlinedButton(
                onClick = onStop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 8.dp,
                    vertical = 4.dp
                )
            ) {
                Text(tr("Stop Core Process"), fontSize = 12.sp)
            }
            OutlinedButton(
                onClick = onRefreshState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 8.dp,
                    vertical = 4.dp
                )
            ) {
                Text(tr("Refresh Core State"), fontSize = 12.sp)
            }
        }
    }
}

// Tab 3: Config

@Composable
fun ConfigTab(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    // Batch 4: Config back stack, same pattern as Tasks (batch 3) and
    // Control (this batch). Depth is 1 (overview -> one sub-page).
    var configNavPages by rememberSaveable { mutableStateOf(listOf(CONFIG_PAGE_OVERVIEW)) }
    val configNav = AppNavStack(configNavPages)
    val page = configNav.current
    fun goToConfigPage(target: Int) { configNavPages = configNav.push(target).pages }
    fun goBackConfigPage() { configNavPages = configNav.pop(CONFIG_PAGE_OVERVIEW).pages }

    // Batch 4: system Back pops the Config stack. At the overview this
    // handler is disabled, so Back keeps its old behaviour. Dialogs inside
    // the sub-pages (e.g. the LLM delete confirm) consume Back themselves.
    BackHandler(enabled = configNav.canGoBack(CONFIG_PAGE_OVERVIEW)) {
        configNavPages = configNav.pop(CONFIG_PAGE_OVERVIEW).pages
    }

    when (page) {
        CONFIG_PAGE_OVERVIEW -> ConfigOverviewPage(
            modifier = modifier,
            viewModel = viewModel,
            onOpenDeviceCore = { goToConfigPage(CONFIG_PAGE_DEVICE_CORE) },
            onOpenLlm = { goToConfigPage(CONFIG_PAGE_LLM) },
            onOpenUnlockPolicy = { goToConfigPage(CONFIG_PAGE_UNLOCK_POLICY) },
            onOpenMapSync = { goToConfigPage(CONFIG_PAGE_MAP_SYNC) }
        )
        CONFIG_PAGE_DEVICE_CORE -> SingleConfigPage(
            title = tr("Control mode config"),
            subtitle = tr("Choose how touch, typing, and task-time phone behavior should work on this device."),
            glyph = "⌁",
            modifier = modifier,
            onBack = { goBackConfigPage() }
        ) {
            LxbCoreConfigCard(viewModel)
        }
        CONFIG_PAGE_LLM -> SingleConfigPage(
            title = tr("LLM config (device-side)"),
            subtitle = tr("Set the model endpoint used for device-side planning and visual understanding."),
            glyph = "✦",
            modifier = modifier,
            onBack = { goBackConfigPage() }
        ) {
            LlmConfigCard(viewModel)
        }
        CONFIG_PAGE_UNLOCK_POLICY -> SingleConfigPage(
            title = tr("Unlock & lock policy"),
            subtitle = tr("Control how the phone is unlocked before a task and locked again afterward."),
            glyph = "🔐",
            modifier = modifier,
            onBack = { goBackConfigPage() }
        ) {
            UnlockPolicyConfigCard(viewModel)
        }
        CONFIG_PAGE_MAP_SYNC -> SingleConfigPage(
            title = tr("Map sync & source"),
            subtitle = tr("Manage route assets, active map lane, and pull-by-identifier actions."),
            glyph = "➜",
            modifier = modifier,
            onBack = { goBackConfigPage() }
        ) {
            MapSyncConfigCard(viewModel)
        }
    }
}

// Logs

private data class TracePrependAnchor(
    val count: Int,
    val index: Int,
    val offset: Int,
    val oldestSeq: Long
)

@Composable
fun LogsTab(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val appLogs by viewModel.appLogEntries.collectAsState()
    val traceLines by viewModel.traceLines.collectAsState()
    val traceHasMoreBefore by viewModel.traceHasMoreBefore.collectAsState()
    val traceLoadingOlder by viewModel.traceLoadingOlder.collectAsState()
    val logExportUiState by viewModel.logExportUiState.collectAsState()
    val appListState = rememberLazyListState()
    val coreListState = rememberLazyListState()
    var selectedLogPage by rememberSaveable { mutableIntStateOf(0) }
    var selectedAppLog by remember { mutableStateOf<UnifiedLogEntry?>(null) }
    var selectedTrace by remember { mutableStateOf<TraceEntry?>(null) }
    var initialScrollDone by rememberSaveable { mutableStateOf(false) }
    var prependAnchor by remember { mutableStateOf<TracePrependAnchor?>(null) }

    LaunchedEffect(Unit) {
        viewModel.refreshTraceTailOnDevice(silent = true, limit = 80)
    }

    LaunchedEffect(Unit) {
        while (true) {
            viewModel.pollNewerTraceOnDevice(silent = true, limit = 80)
            delay(2000L)
        }
    }

    LaunchedEffect(appLogs.size, selectedLogPage) {
        if (selectedLogPage == 0 && appLogs.isNotEmpty()) {
            appListState.animateScrollToItem(appLogs.lastIndex)
        }
    }

    LaunchedEffect(traceLines.size) {
        if (!initialScrollDone && traceLines.isNotEmpty()) {
            coreListState.scrollToItem(traceLines.lastIndex)
            initialScrollDone = true
            return@LaunchedEffect
        }
        val layout = coreListState.layoutInfo
        val lastVisible = layout.visibleItemsInfo.lastOrNull()?.index ?: -1
        val isNearBottom = lastVisible >= traceLines.lastIndex - 2
        if (isNearBottom && traceLines.isNotEmpty()) {
            coreListState.animateScrollToItem(traceLines.lastIndex)
        }
    }

    LaunchedEffect(traceLines.size, traceLines.firstOrNull()?.seq, traceLoadingOlder) {
        val anchor = prependAnchor ?: return@LaunchedEffect
        val newOldestSeq = traceLines.firstOrNull()?.seq ?: 0L
        if (newOldestSeq < anchor.oldestSeq && traceLines.size > anchor.count) {
            val delta = traceLines.size - anchor.count
            coreListState.scrollToItem((anchor.index + delta).coerceAtLeast(0), anchor.offset)
            prependAnchor = null
        } else if (!traceLoadingOlder) {
            prependAnchor = null
        }
    }

    LaunchedEffect(coreListState.firstVisibleItemIndex, traceLines.size, traceHasMoreBefore, traceLoadingOlder) {
        if (traceLines.isEmpty()) return@LaunchedEffect
        if (selectedLogPage == 1 && traceHasMoreBefore && !traceLoadingOlder && coreListState.firstVisibleItemIndex <= 6) {
            prependAnchor = TracePrependAnchor(
                count = traceLines.size,
                index = coreListState.firstVisibleItemIndex,
                offset = coreListState.firstVisibleItemScrollOffset,
                oldestSeq = traceLines.firstOrNull()?.seq ?: 0L
            )
            viewModel.loadOlderTraceOnDevice(silent = true, limit = 80)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        PageHeaderBlock(
            title = tr("Logs"),
            subtitle = tr("Inspect app startup logs and live core traces, then export both together for debugging."),
            glyph = "≡",
            primaryActionLabel = tr("Export"),
            onPrimaryAction = { viewModel.exportAllLogsToDevice() },
            secondaryActionLabel = tr("Refresh"),
            onSecondaryAction = {
                viewModel.refreshAppLogs()
                viewModel.refreshTraceTailOnDevice(limit = 80)
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (selectedLogPage == 0) {
                Button(onClick = { selectedLogPage = 0 }, modifier = Modifier.weight(1f)) {
                    Text(tr("App"))
                }
            } else {
                OutlinedButton(onClick = { selectedLogPage = 0 }, modifier = Modifier.weight(1f)) {
                    Text(tr("App"))
                }
            }
            if (selectedLogPage == 1) {
                Button(onClick = { selectedLogPage = 1 }, modifier = Modifier.weight(1f)) {
                    Text(tr("Core"))
                }
            } else {
                OutlinedButton(onClick = { selectedLogPage = 1 }, modifier = Modifier.weight(1f)) {
                    Text(tr("Core"))
                }
            }
        }
        LogPanel(
            selectedPage = selectedLogPage,
            appLogs = appLogs,
            traceLines = traceLines,
            appListState = appListState,
            coreListState = coreListState,
            showLoadingOlder = traceLoadingOlder || (traceHasMoreBefore && coreListState.firstVisibleItemIndex <= 6),
            logExportUiState = logExportUiState,
            onOpenAppLog = { selectedAppLog = it },
            onOpenTrace = { selectedTrace = it },
            modifier = Modifier.fillMaxSize()
        )
    }

    val appDetail = selectedAppLog
    if (appDetail != null) {
        UnifiedLogDetailDialog(
            title = tr("Log Details"),
            entry = appDetail,
            onDismiss = { selectedAppLog = null }
        )
    }

    val detail = selectedTrace
    if (detail != null) {
        AlertDialog(
            onDismissRequest = { selectedTrace = null },
            title = { Text(tr("Trace Details")) },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SurfacePanel(
                        modifier = Modifier.fillMaxWidth(),
                        background = MaterialTheme.colorScheme.surfaceVariant,
                        borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.18f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(detail.event, style = MaterialTheme.typography.titleSmall)
                            if (detail.summary.isNotBlank()) {
                                Text(detail.summary, fontSize = 12.sp)
                            }
                            if (detail.timestamp.isNotBlank()) {
                                Text("${tr("Timestamp")}: ${detail.timestamp}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            if (detail.taskId.isNotBlank()) {
                                Text("${tr("Task ID")}: ${detail.taskId}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                    if (detail.meta.isNotEmpty()) {
                        TraceDetailSection(
                            title = tr("Key Fields"),
                            items = detail.meta
                        )
                    }
                    if (detail.fields.isNotEmpty()) {
                        TraceDetailSection(
                            title = tr("All Fields"),
                            items = detail.fields
                        )
                    } else if (detail.detail.isNotBlank()) {
                        SurfacePanel(
                            modifier = Modifier.fillMaxWidth(),
                            background = MaterialTheme.colorScheme.surface,
                            borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.18f),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                detail.detail,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    } else {
                        Text(tr("No trace details."), fontSize = 11.sp)
                    }
                    SurfacePanel(
                        modifier = Modifier.fillMaxWidth(),
                        background = MaterialTheme.colorScheme.surfaceVariant,
                        borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.18f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(tr("Raw Trace"), style = MaterialTheme.typography.labelMedium)
                            Text(detail.rawLine, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            },
            confirmButton = {
                OutlinedButton(onClick = { selectedTrace = null }) {
                    Text(tr("Close"))
                }
            }
        )
    }
}

@Composable
fun LogPanel(
    selectedPage: Int,
    appLogs: List<UnifiedLogEntry>,
    traceLines: List<TraceEntry>,
    appListState: androidx.compose.foundation.lazy.LazyListState,
    coreListState: androidx.compose.foundation.lazy.LazyListState,
    showLoadingOlder: Boolean,
    logExportUiState: MainViewModel.LogExportUiState,
    onOpenAppLog: (UnifiedLogEntry) -> Unit,
    onOpenTrace: (TraceEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when {
            logExportUiState.exporting -> {
                StatusNotice(
                    text = tr("Exporting logs..."),
                    accentColor = MaterialTheme.colorScheme.primary
                )
            }

            logExportUiState.status == "success" -> {
                StatusNotice(
                    text = "${tr("Logs exported")}: ${tr("Saved to")} ${logExportUiState.savedPath}",
                    accentColor = AppSuccess
                )
            }

            logExportUiState.status == "empty" -> {
                StatusNotice(
                    text = tr("No logs to export."),
                    accentColor = MaterialTheme.colorScheme.secondary
                )
            }

            logExportUiState.status == "failure" -> {
                StatusNotice(
                    text = "${tr("Log export failed")}: ${logExportUiState.error}",
                    accentColor = AppError
                )
            }
        }
        SurfacePanel(
            modifier = Modifier.fillMaxSize(),
            background = MaterialTheme.colorScheme.surface,
            borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.52f),
            shape = RoundedCornerShape(24.dp)
        ) {
            LazyColumn(
                state = if (selectedPage == 0) appListState else coreListState,
                modifier = Modifier.fillMaxSize()
            ) {
                item(key = "log_hint") {
                    Text(
                        text = if (selectedPage == 0) {
                            tr("Latest app logs stay at the bottom.")
                        } else {
                            tr("Latest core traces stay at the bottom. Scroll upward to load older ones.")
                        },
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f),
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    )
                }
                if (selectedPage == 0 && appLogs.isEmpty()) {
                    item(key = "app_empty") {
                        Text(
                            text = tr("No app logs yet."),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
                if (selectedPage == 1 && showLoadingOlder) {
                    item(key = "loading_older") {
                        Text(
                            text = tr("Load older traces..."),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 2.dp)
                        )
                    }
                }
                if (selectedPage == 0) {
                    items(appLogs, key = { "app-${it.seq}-${it.timestamp}" }) { entry ->
                        AppLogCard(entry = entry, onClick = { onOpenAppLog(entry) })
                    }
                } else {
                    items(traceLines, key = { if (it.seq > 0L) it.seq else it.rawLine.hashCode().toLong() }) { entry ->
                        TraceCard(entry = entry, onClick = { onOpenTrace(entry) })
                    }
                }
                item(key = "log_footer_space") {
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}

@Composable
private fun AppLogCard(entry: UnifiedLogEntry, onClick: () -> Unit) {
    val scheme = MaterialTheme.colorScheme
    val accent = logLevelAccent(entry.level)
    SurfacePanel(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 4.dp),
        background = if (entry.level == "error") AppErrorSoft else scheme.surface,
        borderColor = accent.copy(alpha = 0.18f),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlyphBadge(
                        glyph = when (entry.level) {
                            "error" -> "!"
                            "warn" -> "!"
                            else -> "•"
                        },
                        accentColor = accent,
                        size = 28.dp
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                        Text(
                            text = entry.logger,
                            style = MaterialTheme.typography.bodyMedium,
                            color = scheme.onSurface
                        )
                        Text(
                            text = entry.level.uppercase(Locale.US),
                            fontSize = 10.sp,
                            color = accent
                        )
                    }
                }
                if (entry.timestamp.isNotBlank()) {
                    StatusTag(
                        text = entry.timestamp.takeLast(12),
                        accentColor = accent
                    )
                }
            }
            Text(
                text = ut(entry.message),
                fontSize = 11.sp,
                color = scheme.onSurface.copy(alpha = 0.92f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun logLevelAccent(level: String): Color {
    return when (level.lowercase(Locale.US)) {
        "error" -> AppError
        "warn" -> AppWarning
        else -> MaterialTheme.colorScheme.primary
    }
}

@Composable
private fun UnifiedLogDetailDialog(
    title: String,
    entry: UnifiedLogEntry,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SurfacePanel(
                    modifier = Modifier.fillMaxWidth(),
                    background = MaterialTheme.colorScheme.surfaceVariant,
                    borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.18f),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(entry.logger, style = MaterialTheme.typography.titleSmall)
                        Text(entry.message, fontSize = 12.sp)
                        if (entry.timestamp.isNotBlank()) {
                            Text("${tr("Timestamp")}: ${entry.timestamp}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text("${tr("Level")}: ${entry.level}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                TraceDetailSection(
                    title = tr("Fields"),
                    items = entry.attrs.map { (key, value) -> TraceMetaItem(label = key, value = value) }
                )
            }
        },
        confirmButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(tr("Close"))
            }
        }
    )
}

@Composable
private fun TraceCard(entry: TraceEntry, onClick: () -> Unit) {
    val scheme = MaterialTheme.colorScheme
    val accent = when {
        entry.isError -> scheme.error
        entry.event.startsWith("notify_") -> scheme.secondary
        entry.event == "fsm_state_enter" -> scheme.primary
        else -> scheme.onSurface.copy(alpha = 0.42f)
    }
    SurfacePanel(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 4.dp),
        background = if (entry.isError) AppErrorSoft else scheme.surface,
        borderColor = accent.copy(alpha = 0.18f),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlyphBadge(
                        glyph = when {
                            entry.isError -> "!"
                            entry.event.startsWith("notify_") -> "✉"
                            entry.event == "fsm_state_enter" -> "↺"
                            else -> "•"
                        },
                        accentColor = accent,
                        size = 28.dp
                    )
                    Text(
                        text = entry.logger.ifBlank { entry.event },
                        style = MaterialTheme.typography.bodyMedium,
                        color = scheme.onSurface
                    )
                }
                if (entry.timestamp.isNotBlank()) {
                    StatusTag(
                        text = entry.timestamp.takeLast(12),
                        accentColor = accent
                    )
                }
            }
            if (entry.summary.isNotBlank()) {
                Text(
                    text = "${entry.event}: ${ut(entry.summary)}",
                    fontSize = 11.sp,
                    color = scheme.onSurface.copy(alpha = 0.92f),
                    maxLines = 2
                )
            }
            if (entry.taskId.isNotBlank()) {
                Text(
                    text = "${tr("Task")}=${entry.taskId.take(8)}...",
                    fontSize = 10.sp,
                    color = scheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun TraceDetailSection(title: String, items: List<TraceMetaItem>) {
    if (items.isEmpty()) return
    SurfacePanel(
        modifier = Modifier.fillMaxWidth(),
        background = MaterialTheme.colorScheme.surface,
        borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.52f),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(title, style = MaterialTheme.typography.labelLarge)
            items.forEach { item ->
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = item.value,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

// Config overview + sub-pages

@Composable
fun ConfigOverviewPage(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onOpenDeviceCore: () -> Unit,
    onOpenLlm: () -> Unit,
    onOpenUnlockPolicy: () -> Unit,
    onOpenMapSync: () -> Unit
) {
    val uiLang by viewModel.uiLang.collectAsState()
    val coreRuntime by viewModel.coreRuntimeStatus.collectAsState()
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        PageHeaderBlock(
            title = tr("Config center"),
            subtitle = tr("Choose a category to configure. Each section opens a dedicated settings page."),
            glyph = "⚙"
        )

        SummaryInfoStrip(
            glyph = if (coreRuntime.ready) "✓" else "!",
            title = tr("Current setup"),
            primaryMetric = "${tr("State")}: ${if (coreRuntime.ready) tr("Core Connected") else tr("Core Disconnected")}",
            secondaryMetric = if (uiLang == "zh") tr("Chinese") else tr("English"),
            accentColor = if (coreRuntime.ready) AppSuccess else AppWarning
        )

        ProductSectionCard(
            title = tr("Recommended first"),
            subtitle = tr("Most people only need model and control settings before starting tasks."),
            glyph = "★"
        ) {
            ConfigEntryCard(
                title = tr("LLM config (device-side)"),
                description = tr("Base URL, API key and model for device-side LLM/VLM calls."),
                onClick = onOpenLlm
            )
            ConfigEntryCard(
                title = tr("Control mode config"),
                description = tr("If taps, swipes, or typing are not behaving well on your phone, adjust them here."),
                onClick = onOpenDeviceCore,
                badgeText = if (coreRuntime.ready) tr("Ready") else tr("Needs sync")
            )
        }

        ProductSectionCard(
            title = tr("Device behavior"),
            subtitle = tr("Language, unlock, and other phone-side behavior live here."),
            glyph = "☑"
        ) {
            SurfacePanel(
                modifier = Modifier.fillMaxWidth(),
                background = MaterialTheme.colorScheme.surfaceVariant,
                borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.20f),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SheetHeader(
                        title = tr("Language"),
                        subtitle = tr("Language for app UI text.")
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ModeChoiceButton(
                            text = tr("English"),
                            selected = uiLang == "en",
                            onClick = { viewModel.setUiLang("en") },
                            modifier = Modifier.weight(1f)
                        )
                        ModeChoiceButton(
                            text = tr("Chinese"),
                            selected = uiLang == "zh",
                            onClick = { viewModel.setUiLang("zh") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            ConfigEntryCard(
                title = tr("Unlock & lock policy"),
                description = tr("Auto unlock before route, auto lock after task, and lockscreen credentials."),
                onClick = onOpenUnlockPolicy
            )
        }

        ProductSectionCard(
            title = tr("Map routing"),
            subtitle = tr("Route asset source, sync, and identifier pull controls."),
            glyph = "➜"
        ) {
            ConfigEntryCard(
                title = tr("Map sync & source"),
                description = tr("Sync stable maps, pull map by identifier, and choose runtime source lane."),
                onClick = onOpenMapSync
            )
        }
    }
}

@Composable
fun ConfigEntryCard(
    title: String,
    description: String,
    onClick: () -> Unit,
    badgeText: String? = null
) {
    SurfacePanel(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        background = MaterialTheme.colorScheme.surfaceVariant,
        borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.20f),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                if (!badgeText.isNullOrBlank()) {
                    StatusTag(text = badgeText, accentColor = MaterialTheme.colorScheme.primary)
                }
            }
            Text(
                text = description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = tr("Open"),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun SingleConfigPage(
    title: String,
    subtitle: String? = null,
    glyph: String = "⚙",
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (!subtitle.isNullOrBlank()) {
            PageHeaderBlock(
                title = title,
                subtitle = subtitle,
                glyph = glyph,
                onBack = onBack
            )
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PageActionButton(text = tr("Back"), onClick = onBack)
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .weight(1f),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        content()
    }
}

@Composable
private fun SettingsSectionCard(
    title: String,
    subtitle: String? = null,
    glyph: String = "•",
    content: @Composable () -> Unit
) {
    SurfacePanel(
        modifier = Modifier.fillMaxWidth(),
        background = MaterialTheme.colorScheme.surface,
        borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.58f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                GlyphBadge(
                    glyph = glyph,
                    accentColor = MaterialTheme.colorScheme.primary,
                    size = 34.dp
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(title, style = MaterialTheme.typography.titleSmall)
                    if (!subtitle.isNullOrBlank()) {
                        Text(
                            text = subtitle,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
                        )
                    }
                }
            }
            content()
        }
    }
}

@Composable
private fun PreferenceSwitchRow(
    title: String,
    detail: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(title, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = detail,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun StatusNotice(
    text: String,
    accentColor: Color
) {
    SurfacePanel(
        modifier = Modifier.fillMaxWidth(),
        background = accentColor.copy(alpha = 0.09f).compositeOver(MaterialTheme.colorScheme.surface),
        borderColor = accentColor.copy(alpha = 0.22f),
        shape = RoundedCornerShape(18.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            fontSize = 11.sp,
            color = accentColor
        )
    }
}

@Composable
fun LxbCoreConfigCard(viewModel: MainViewModel) {
    val lxbPort by viewModel.lxbPort.collectAsState()
    val touchMode by viewModel.touchMode.collectAsState()
    val taskDndMode by viewModel.taskDndMode.collectAsState()
    val maxTaskSteps by viewModel.maxTaskSteps.collectAsState()
    val adbKeyboardUiState by viewModel.adbKeyboardUiState.collectAsState()
    val coreConfigResult by viewModel.coreConfigResult.collectAsState()
    val coreRuntime by viewModel.coreRuntimeStatus.collectAsState()
    val maxTaskStepsUnlimited = maxTaskSteps.trim().toIntOrNull()?.let { it <= 0 } == true

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SummaryInfoStrip(
            glyph = if (coreRuntime.ready) "✓" else "!",
            title = tr("Device control"),
            primaryMetric = "${tr("State")}: ${if (coreRuntime.ready) tr("Core Connected") else tr("Core Disconnected")}",
            secondaryMetric = if (touchMode == MainViewModel.TOUCH_MODE_SHELL) tr("Shell") else tr("UIAutomator"),
            accentColor = if (coreRuntime.ready) AppSuccess else AppWarning
        )

        SettingsSectionCard(
            title = tr("Connection"),
            subtitle = tr("Device-side core connection settings."),
            glyph = "⌁"
        ) {
            OutlinedTextField(
                value = lxbPort,
                onValueChange = { viewModel.lxbPort.value = it },
                label = { Text(tr("TCP port")) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                supportingText = {
                    Text(
                        tr("TCP port listened by lxb-core on device (default 12345)"),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }
            )
        }

        SettingsSectionCard(
            title = tr("Touch input mode"),
            subtitle = tr("Choose the injection strategy used for taps and swipes."),
            glyph = "☞"
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ModeChoiceButton(
                    text = tr("Shell"),
                    selected = touchMode == MainViewModel.TOUCH_MODE_SHELL,
                    onClick = { viewModel.setTouchMode(MainViewModel.TOUCH_MODE_SHELL) },
                    modifier = Modifier.weight(1f)
                )
                ModeChoiceButton(
                    text = tr("UIAutomator"),
                    selected = touchMode == MainViewModel.TOUCH_MODE_UIAUTOMATION,
                    onClick = { viewModel.setTouchMode(MainViewModel.TOUCH_MODE_UIAUTOMATION) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        SettingsSectionCard(
            title = tr("Text input mode"),
            subtitle = tr("ADB Keyboard is recommended because it gives the most reliable input experience."),
            glyph = "⌨"
        ) {
            Text(
                tr("Recommended: install ADB Keyboard. If detected, core will switch to it only while typing, then restore your original keyboard automatically."),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                fontSize = 12.sp
            )
            Text(
                tr("If ADB Keyboard is not detected, fallback input will still work, but some apps may fail to accept Chinese text."),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                fontSize = 12.sp
            )
            StatusNotice(
                text = buildString {
                    append(tr(if (adbKeyboardUiState.installed) "Detected" else "Not detected"))
                    if (adbKeyboardUiState.installed && adbKeyboardUiState.label.isNotBlank()) {
                        append(": ")
                        append(adbKeyboardUiState.label)
                    }
                },
                accentColor = if (adbKeyboardUiState.installed) AppSuccess else AppWarning
            )
            if (adbKeyboardUiState.currentImeId.isNotBlank()) {
                Text(
                    text = "${tr("Current keyboard")}: ${adbKeyboardUiState.currentImeId}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                )
            }
            Text(
                text = tr(
                    if (adbKeyboardUiState.installed) {
                        "Auto-switch input channel is ready."
                    } else {
                        "Core will use fallback text input for now."
                    }
                ),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
            )
            OutlinedButton(
                onClick = { viewModel.refreshAdbKeyboardStatus() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(tr("Check ADB Keyboard"))
            }
            if (adbKeyboardUiState.checked && !adbKeyboardUiState.installed) {
                OutlinedButton(
                    onClick = { viewModel.openAdbKeyboardReleasePage() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(tr("Download ADB Keyboard"))
                }
            }
        }

        SettingsSectionCard(
            title = tr("Task-time Do Not Disturb"),
            subtitle = tr("Choose what happens to DND automatically when a task begins."),
            glyph = "☾"
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ModeChoiceButton(
                    text = tr("Keep current DND state"),
                    selected = taskDndMode == MainViewModel.TASK_DND_MODE_SKIP,
                    onClick = { viewModel.setTaskDndMode(MainViewModel.TASK_DND_MODE_SKIP) }
                )
                ModeChoiceButton(
                    text = tr("Set DND to OFF (allow notifications)"),
                    selected = taskDndMode == MainViewModel.TASK_DND_MODE_OFF,
                    onClick = { viewModel.setTaskDndMode(MainViewModel.TASK_DND_MODE_OFF) }
                )
                ModeChoiceButton(
                    text = tr("Set DND to NONE (silence all)"),
                    selected = taskDndMode == MainViewModel.TASK_DND_MODE_NONE,
                    onClick = { viewModel.setTaskDndMode(MainViewModel.TASK_DND_MODE_NONE) }
                )
            }
        }

        SettingsSectionCard(
            title = tr("Task step limit"),
            subtitle = tr("Limits state-machine/vision turns per sub-task. Set 0 for unlimited."),
            glyph = "∞"
        ) {
            PreferenceSwitchRow(
                title = tr("No step limit"),
                detail = tr("Allow tasks to run without stopping on a step count. Cancellation still works."),
                checked = maxTaskStepsUnlimited,
                onCheckedChange = { viewModel.setMaxTaskStepsUnlimited(it) }
            )
            OutlinedTextField(
                value = if (maxTaskStepsUnlimited) tr("Unlimited") else maxTaskSteps,
                onValueChange = { viewModel.setMaxTaskSteps(it) },
                enabled = !maxTaskStepsUnlimited,
                label = { Text(tr("Maximum steps")) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                supportingText = {
                    Text(tr("0 means unlimited; default is 100."))
                }
            )
        }

        SettingsSectionCard(
            title = tr("Apply changes"),
            subtitle = tr("Push the current control configuration to core now, or only save it locally."),
            glyph = "✓"
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { viewModel.applyTouchModeToCore() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(tr("Apply to core"))
                }
                OutlinedButton(
                    onClick = { viewModel.saveConfig() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(tr("Save all config only"))
                }
            }
            if (coreConfigResult.isNotEmpty()) {
                StatusNotice(
                    text = coreConfigResult,
                    accentColor = if (coreConfigResult.contains("ok", ignoreCase = true) || coreConfigResult.contains("success", ignoreCase = true)) AppSuccess else MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
private fun ModeChoiceButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    if (selected) {
        Button(
            onClick = onClick,
            enabled = false,
            modifier = modifier
        ) {
            Text(text)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier
        ) {
            Text(text)
        }
    }
}

@Composable
fun LlmConfigCard(viewModel: MainViewModel) {
    val llmBaseUrl by viewModel.llmBaseUrl.collectAsState()
    val llmApiKey by viewModel.llmApiKey.collectAsState()
    val llmModel by viewModel.llmModel.collectAsState()
    val llmRequestType by viewModel.llmRequestType.collectAsState()
    val llmRoutingMode by viewModel.llmRoutingMode.collectAsState()
    val llmUnifiedProviderId by viewModel.llmUnifiedProviderId.collectAsState()
    val llmSemanticLocatorProviderId by viewModel.llmSemanticLocatorProviderId.collectAsState()
    val llmVisionActProviderId by viewModel.llmVisionActProviderId.collectAsState()
    val llmTestResult by viewModel.llmTestResult.collectAsState()
    val llmUnifiedRouteResult by viewModel.llmUnifiedRouteResult.collectAsState()
    val llmSemanticLocatorRouteResult by viewModel.llmSemanticLocatorRouteResult.collectAsState()
    val llmVisionActRouteResult by viewModel.llmVisionActRouteResult.collectAsState()
    val llmProfileDraftName by viewModel.llmProfileDraftName.collectAsState()
    val llmProfiles by viewModel.llmProfiles.collectAsState()
    val activeLlmProfileId by viewModel.activeLlmProfileId.collectAsState()
    var profilePendingDelete by remember { mutableStateOf<MainViewModel.LlmProfile?>(null) }
    val llmProfileResult by viewModel.llmProfileResult.collectAsState()
    val resolvedEndpoint = remember(llmBaseUrl, llmRequestType, llmModel) {
        runCatching { LlmClient.buildEndpointUrl(llmBaseUrl, llmRequestType, llmModel) }.getOrNull().orEmpty()
    }
    val requestTypes = remember {
        listOf(
            LlmConfig.REQUEST_TYPE_OPENAI_CHAT_COMPLETIONS,
            LlmConfig.REQUEST_TYPE_GEMINI_GENERATE_CONTENT,
            LlmConfig.REQUEST_TYPE_ANTHROPIC_MESSAGES
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SummaryInfoStrip(
            glyph = "✦",
            title = tr("Model routing"),
            primaryMetric = if (llmRoutingMode == MainViewModel.LLM_ROUTING_MODE_SPLIT) tr("Separate routes") else tr("Unified route"),
            secondaryMetric = if (activeLlmProfileId.isNotBlank()) tr("Saved config") else tr("Draft only"),
            accentColor = MaterialTheme.colorScheme.primary
        )

        SettingsSectionCard(
            title = tr("SCRIPT_ACTION routes"),
            subtitle = tr("Choose whether semantic locator and vision act share one provider or use separate providers."),
            glyph = "⇄"
        ) {
            Text(
                text = tr("Routing mode"),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = llmRoutingMode != MainViewModel.LLM_ROUTING_MODE_SPLIT,
                    onClick = { viewModel.setLlmRoutingMode(MainViewModel.LLM_ROUTING_MODE_UNIFIED) },
                    label = { Text(tr("Unified route")) }
                )
                FilterChip(
                    selected = llmRoutingMode == MainViewModel.LLM_ROUTING_MODE_SPLIT,
                    onClick = { viewModel.setLlmRoutingMode(MainViewModel.LLM_ROUTING_MODE_SPLIT) },
                    label = { Text(tr("Separate routes")) }
                )
            }
            if (llmRoutingMode == MainViewModel.LLM_ROUTING_MODE_SPLIT) {
                LlmRouteProviderPanel(
                    title = tr("Semantic locator"),
                    description = tr("Provider used when SCRIPT_ACT needs semantic visual target location."),
                    profiles = llmProfiles,
                    selectedProviderId = llmSemanticLocatorProviderId,
                    result = llmSemanticLocatorRouteResult,
                    onProviderChange = { viewModel.setLlmRouteProvider(MainViewModel.LLM_ROUTE_SEMANTIC_LOCATOR, it) },
                    onSave = { viewModel.saveLlmRoute(MainViewModel.LLM_ROUTE_SEMANTIC_LOCATOR) },
                    onSync = { viewModel.syncLlmRouteConfig(MainViewModel.LLM_ROUTE_SEMANTIC_LOCATOR) },
                    onTest = { viewModel.testLlmRouteAndSyncConfig(MainViewModel.LLM_ROUTE_SEMANTIC_LOCATOR) }
                )
                LlmRouteProviderPanel(
                    title = tr("Vision act"),
                    description = tr("Provider used by the VISION_ACT planner."),
                    profiles = llmProfiles,
                    selectedProviderId = llmVisionActProviderId,
                    result = llmVisionActRouteResult,
                    onProviderChange = { viewModel.setLlmRouteProvider(MainViewModel.LLM_ROUTE_VISION_ACT, it) },
                    onSave = { viewModel.saveLlmRoute(MainViewModel.LLM_ROUTE_VISION_ACT) },
                    onSync = { viewModel.syncLlmRouteConfig(MainViewModel.LLM_ROUTE_VISION_ACT) },
                    onTest = { viewModel.testLlmRouteAndSyncConfig(MainViewModel.LLM_ROUTE_VISION_ACT) }
                )
            } else {
                LlmRouteProviderPanel(
                    title = tr("Unified route"),
                    description = tr("One provider is used for both semantic locator and vision act."),
                    profiles = llmProfiles,
                    selectedProviderId = llmUnifiedProviderId,
                    result = if (llmUnifiedRouteResult.isNotBlank()) llmUnifiedRouteResult else llmTestResult,
                    allowDraftFallback = true,
                    onProviderChange = { viewModel.setLlmRouteProvider(MainViewModel.LLM_ROUTE_UNIFIED, it) },
                    onSave = { viewModel.saveLlmRoute(MainViewModel.LLM_ROUTE_UNIFIED) },
                    onSync = { viewModel.syncLlmRouteConfig(MainViewModel.LLM_ROUTE_UNIFIED) },
                    onTest = { viewModel.testLlmRouteAndSyncConfig(MainViewModel.LLM_ROUTE_UNIFIED) }
                )
            }
        }

        SettingsSectionCard(
            title = tr("Provider editor"),
            subtitle = tr("Base endpoint, API key, and model used for device-side requests."),
            glyph = "🌐"
        ) {
            Text(
                text = tr("Request Type"),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f)
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                requestTypes.forEach { type ->
                    FilterChip(
                        selected = LlmConfig.normalizeRequestType(llmRequestType) == type,
                        onClick = { viewModel.llmRequestType.value = type },
                        label = { Text(tr(llmRequestTypeLabel(type))) }
                    )
                }
            }
            OutlinedTextField(
                value = llmBaseUrl,
                onValueChange = { viewModel.llmBaseUrl.value = it },
                label = { Text(tr("API Base URL")) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                supportingText = {
                    Text(
                        tr("Base URL example") + ": " + llmBaseUrlExample(llmRequestType),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }
            )
            StatusNotice(
                text = "${tr("Resolved request URL")}: " +
                    if (resolvedEndpoint.isNotBlank()) resolvedEndpoint else tr("Input API Base URL to preview request endpoint."),
                accentColor = MaterialTheme.colorScheme.secondary
            )
            OutlinedTextField(
                value = llmApiKey,
                onValueChange = { viewModel.llmApiKey.value = it },
                label = { Text(tr("API Key")) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = llmModel,
                onValueChange = { viewModel.llmModel.value = it },
                label = { Text(tr("Model")) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                supportingText = {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(tr("e.g. gpt-4o-mini, qwen-plus"))
                        Text(tr("Please use a model with image recognition capability."))
                    }
                }
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { viewModel.testLlmAndSyncConfig() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(tr("Test LLM & sync to device"))
                }
                OutlinedButton(
                    onClick = { viewModel.saveConfig() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(tr("Save only"))
                }
            }
            if (llmTestResult.isNotEmpty()) {
                StatusNotice(
                    text = llmTestResult,
                    accentColor = if (llmTestResult.startsWith("LLM OK")) AppSuccess else AppWarning
                )
            }
        }

        SettingsSectionCard(
            title = tr("Saved local configs"),
            subtitle = tr("Save reusable model presets locally and switch between them quickly."),
            glyph = "☁"
        ) {
            OutlinedTextField(
                value = llmProfileDraftName,
                onValueChange = { viewModel.llmProfileDraftName.value = it },
                label = { Text(tr("Config name")) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { viewModel.saveCurrentLlmAsNewProfile() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(tr("Save as new"))
                }
                OutlinedButton(
                    onClick = { viewModel.updateSelectedLlmProfile() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(tr("Update selected"))
                }
            }
            if (llmProfileResult.isNotEmpty()) {
                StatusNotice(
                    text = llmProfileResult,
                    accentColor = MaterialTheme.colorScheme.secondary
                )
            }
            if (llmProfiles.isEmpty()) {
                Text(
                    text = tr("No saved LLM configs yet."),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    llmProfiles.forEach { profile ->
                        SurfacePanel(
                            modifier = Modifier.fillMaxWidth(),
                            background = if (profile.id == activeLlmProfileId) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                            borderColor = if (profile.id == activeLlmProfileId) MaterialTheme.colorScheme.primary.copy(alpha = 0.20f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.18f),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = profile.name,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    if (profile.id == activeLlmProfileId) {
                                        StatusTag(
                                            text = tr("Selected config"),
                                            accentColor = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                                if (profile.model.isNotBlank()) {
                                    Text(
                                        text = "${profile.model} · ${tr(llmRequestTypeLabel(profile.requestType))}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f)
                                    )
                                }
                                if (profile.apiBaseUrl.isNotBlank()) {
                                    Text(
                                        text = profile.apiBaseUrl,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedButton(
                                        onClick = { viewModel.applyLlmProfile(profile.id) },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(tr("Use this config"))
                                    }
                                    OutlinedButton(
                                        onClick = { profilePendingDelete = profile },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(tr("Delete"))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        val pendingDelete = profilePendingDelete
        if (pendingDelete != null) {
            DeleteConfirmDialog(
                itemName = pendingDelete.name,
                title = tr("Delete configuration"),
                warning = tr("This configuration will be permanently deleted and cannot be recovered."),
                onConfirm = { viewModel.deleteLlmProfile(pendingDelete.id) },
                onDismiss = { profilePendingDelete = null }
            )
        }
    }
}

@Composable
private fun LlmRouteProviderPanel(
    title: String,
    description: String,
    profiles: List<MainViewModel.LlmProfile>,
    selectedProviderId: String,
    result: String,
    allowDraftFallback: Boolean = false,
    onProviderChange: (String) -> Unit,
    onSave: () -> Unit,
    onSync: () -> Unit,
    onTest: () -> Unit
) {
    val selected = profiles.firstOrNull { it.id == selectedProviderId }
    SurfacePanel(
        modifier = Modifier.fillMaxWidth(),
        background = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.52f),
        borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.18f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(title, style = MaterialTheme.typography.titleSmall)
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
                )
            }
            if (profiles.isEmpty()) {
                StatusNotice(
                    text = if (allowDraftFallback) {
                        tr("No saved provider yet. This route will use the draft provider above.")
                    } else {
                        tr("No saved provider yet. Save a provider first.")
                    },
                    accentColor = AppWarning
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    profiles.forEach { profile ->
                        FilterChip(
                            selected = profile.id == selectedProviderId,
                            onClick = { onProviderChange(profile.id) },
                            label = {
                                Text(
                                    text = profile.name,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        )
                    }
                }
            }
            StatusNotice(
                text = when {
                    selected != null -> "${tr("Selected provider")}: ${selected.name} · ${selected.model}"
                    allowDraftFallback -> tr("No provider selected. This route will use the draft provider above.")
                    else -> tr("No provider selected.")
                },
                accentColor = if (selected != null || allowDraftFallback) MaterialTheme.colorScheme.secondary else AppWarning
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onTest,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(tr("Test and sync"))
                }
                OutlinedButton(
                    onClick = onSync,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(tr("Sync route"))
                }
            }
            OutlinedButton(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(tr("Save route"))
            }
            if (result.isNotBlank()) {
                StatusNotice(
                    text = result,
                    accentColor = if (result.startsWith("LLM OK")) AppSuccess else MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
fun UnlockPolicyConfigCard(viewModel: MainViewModel) {
    val autoUnlockBeforeRoute by viewModel.autoUnlockBeforeRoute.collectAsState()
    val autoLockAfterTask by viewModel.autoLockAfterTask.collectAsState()
    val unlockPin by viewModel.unlockPin.collectAsState()
    val llmTestResult by viewModel.llmTestResult.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SummaryInfoStrip(
            glyph = "🔐",
            title = tr("Unlock policy"),
            primaryMetric = if (autoUnlockBeforeRoute) tr("Unlock before route is ON") else tr("Unlock before route is OFF"),
            secondaryMetric = if (autoLockAfterTask) tr("Auto lock ON") else tr("Auto lock OFF"),
            accentColor = MaterialTheme.colorScheme.primary
        )

        SettingsSectionCard(
            title = tr("Behavior"),
            subtitle = tr("These switches control whether the phone unlocks before a task and locks after it."),
            glyph = "☑"
        ) {
            PreferenceSwitchRow(
                title = tr("Auto unlock before route"),
                detail = tr("Check screen state and unlock before app launch/routing."),
                checked = autoUnlockBeforeRoute,
                onCheckedChange = { viewModel.autoUnlockBeforeRoute.value = it }
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.18f))
            PreferenceSwitchRow(
                title = tr("Auto lock after task"),
                detail = tr("Lock screen when task ends if the FSM unlocked it."),
                checked = autoLockAfterTask,
                onCheckedChange = { viewModel.autoLockAfterTask.value = it }
            )
        }

        SettingsSectionCard(
            title = tr("Lockscreen credentials"),
            subtitle = tr("Only needed when swipe unlock alone is not enough."),
            glyph = "•"
        ) {
            OutlinedTextField(
                value = unlockPin,
                onValueChange = { viewModel.unlockPin.value = it },
                label = { Text(tr("Unlock PIN / password")) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                supportingText = {
                    Text(tr("Used only when swipe unlock is not enough."))
                }
            )
        }

        SettingsSectionCard(
            title = tr("Apply changes"),
            subtitle = tr("Sync the latest unlock settings to device now, or only save them locally."),
            glyph = "✓"
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { viewModel.syncDeviceConfigOnly() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(tr("Sync to device"))
                }
                OutlinedButton(
                    onClick = { viewModel.saveConfig() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(tr("Save only"))
                }
            }
            if (llmTestResult.isNotEmpty()) {
                StatusNotice(
                    text = llmTestResult,
                    accentColor = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
fun MapSyncConfigCard(viewModel: MainViewModel) {
    val mapRepoRawBaseUrl by viewModel.mapRepoRawBaseUrl.collectAsState()
    val useMap by viewModel.useMap.collectAsState()
    val mapSource by viewModel.mapSource.collectAsState()
    val mapTargetPackage by viewModel.mapTargetPackage.collectAsState()
    val mapTargetId by viewModel.mapTargetId.collectAsState()
    val mapSyncResult by viewModel.mapSyncResult.collectAsState()
    val installedApps by viewModel.installedAppList.collectAsState()
    var showMapPackagePicker by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.refreshInstalledAppSnapshotOnDevice()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SummaryInfoStrip(
            glyph = "➜",
            title = tr("Map routing"),
            primaryMetric = if (useMap) tr("Use map routing is ON") else tr("Use map routing is OFF"),
            secondaryMetric = tr(
                when (mapSource) {
                    "candidate" -> "Candidate"
                    "burn" -> "Burn"
                    else -> "Stable"
                }
            ),
            accentColor = if (useMap) MaterialTheme.colorScheme.primary else AppWarning
        )

        SettingsSectionCard(
            title = tr("Source & repository"),
            subtitle = tr("Choose where maps come from and which runtime lane stays active."),
            glyph = "☁"
        ) {
            OutlinedTextField(
                value = mapRepoRawBaseUrl,
                onValueChange = { viewModel.mapRepoRawBaseUrl.value = it },
                label = { Text(tr("Map repo raw base URL")) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                supportingText = {
                    Text(tr("e.g. https://raw.githubusercontent.com/wuwei-crg/LXB-MapRepo/main"))
                }
            )
            PreferenceSwitchRow(
                title = tr("Use map routing"),
                detail = if (useMap) {
                    tr("ON: route with map when available.")
                } else {
                    tr("OFF: force no-map mode (launch then vision-only).")
                },
                checked = useMap,
                onCheckedChange = { viewModel.setUseMap(it) }
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(tr("Map source"), style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = tr("Choose which lane is applied to runtime routing map."),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ModeChoiceButton(
                        text = tr("Stable"),
                        selected = mapSource == "stable",
                        onClick = { viewModel.setMapSource("stable") },
                        modifier = Modifier.weight(1f)
                    )
                    ModeChoiceButton(
                        text = tr("Candidate"),
                        selected = mapSource == "candidate",
                        onClick = { viewModel.setMapSource("candidate") },
                        modifier = Modifier.weight(1f)
                    )
                    ModeChoiceButton(
                        text = tr("Burn"),
                        selected = mapSource == "burn",
                        onClick = { viewModel.setMapSource("burn") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        SettingsSectionCard(
            title = tr("Identifier pull"),
            subtitle = tr("Pick the app and map identifier used for pull-by-ID and active-map checks."),
            glyph = "⌕"
        ) {
            PackageSelectField(
                title = tr("Package (select from local snapshot)"),
                selectedPackage = mapTargetPackage,
                options = installedApps,
                onOpen = {
                    if (installedApps.isEmpty()) {
                        viewModel.refreshInstalledAppSnapshotOnDevice()
                    }
                    showMapPackagePicker = true
                },
                onClear = { viewModel.mapTargetPackage.value = "" }
            )
            Text(
                text = tr("Used for pull-by-identifier and active status."),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
            )
            OutlinedTextField(
                value = mapTargetId,
                onValueChange = { viewModel.mapTargetId.value = it },
                label = { Text(tr("Map ID")) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                supportingText = {
                    Text(tr("Required for stable/candidate pull by identifier."))
                }
            )
        }

        SettingsSectionCard(
            title = tr("Actions"),
            subtitle = tr("Sync or pull route assets, inspect the active map, and save the current settings."),
            glyph = "✓"
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { viewModel.syncStableMapsNow() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(tr("Sync Stable All"))
                }
                OutlinedButton(
                    onClick = { viewModel.pullStableByIdentifierNow() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(tr("Pull Stable by ID"))
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { viewModel.pullCandidateByIdentifierNow() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(tr("Pull Candidate by ID"))
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { viewModel.checkActiveMapStatus() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(tr("Show active"))
                }
                OutlinedButton(
                    onClick = { viewModel.saveConfig() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(tr("Save only"))
                }
            }
            if (mapSyncResult.isNotEmpty()) {
                StatusNotice(
                    text = mapSyncResult,
                    accentColor = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
    if (showMapPackagePicker) {
        PackagePickerDialog(
            options = installedApps,
            onDismiss = { showMapPackagePicker = false },
            onSelect = { item ->
                viewModel.mapTargetPackage.value = item.packageName
                showMapPackagePicker = false
            }
        )
    }
}
