package com.example.lxb_ignition.ui.validation

/**
 * Batch 2 form validation: pure functions, no Android dependencies, so they
 * can be unit-tested with plain kotlinc + JUnit.
 *
 * The [FormError.message] values are the canonical English keys used by the
 * existing tr()/ZhMap translation system in MainActivity.kt. The UI passes
 * them through tr() at display time; this file intentionally does NOT use
 * stringResource, because the app's uiLang can be changed in Config and is
 * not necessarily the system locale.
 *
 * Validation is deliberately form-level only: it never saves, never calls
 * the device, and never changes the ViewModel's own save-time checks. The
 * ViewModel still enforces its own rules when a save actually runs.
 */
enum class FormField {
    TEMPLATE_NAME,
    TASK_DESCRIPTION,
    TARGET_APP,
    WORKFLOW_NAME,
    WORKFLOW_STEPS
}

data class FormError(
    val field: FormField,
    val message: String
)

fun validateTemplateForm(
    name: String,
    description: String,
    packageName: String
): List<FormError> {
    val errors = mutableListOf<FormError>()
    if (name.isBlank()) {
        errors += FormError(FormField.TEMPLATE_NAME, "Template name is required.")
    }
    if (description.isBlank()) {
        errors += FormError(FormField.TASK_DESCRIPTION, "Task description is required.")
    }
    if (packageName.isBlank()) {
        errors += FormError(FormField.TARGET_APP, "Target app is required.")
    }
    return errors
}

fun validateWorkflowForm(
    name: String,
    stepCount: Int
): List<FormError> {
    val errors = mutableListOf<FormError>()
    if (name.isBlank()) {
        errors += FormError(FormField.WORKFLOW_NAME, "Workflow name is required.")
    }
    if (stepCount <= 0) {
        errors += FormError(FormField.WORKFLOW_STEPS, "Add at least one step.")
    }
    return errors
}
