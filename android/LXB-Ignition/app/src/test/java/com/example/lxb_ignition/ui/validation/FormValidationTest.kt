package com.example.lxb_ignition.ui.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FormValidationTest {

    @Test
    fun template_allBlank_reportsAllThreeFieldsInOrder() {
        val errors = validateTemplateForm("", "   ", "\t")
        assertEquals(
            listOf(FormField.TEMPLATE_NAME, FormField.TASK_DESCRIPTION, FormField.TARGET_APP),
            errors.map { it.field }
        )
        assertEquals("Template name is required.", errors[0].message)
        assertEquals("Task description is required.", errors[1].message)
        assertEquals("Target app is required.", errors[2].message)
    }

    @Test
    fun template_whitespaceOnlyName_isBlank() {
        val errors = validateTemplateForm("   ", "do the check-in", "com.example.app")
        assertEquals(listOf(FormField.TEMPLATE_NAME), errors.map { it.field })
    }

    @Test
    fun template_valid_returnsEmpty() {
        assertTrue(
            validateTemplateForm("Check-in", "open the app and check in", "com.example.app").isEmpty()
        )
    }

    @Test
    fun workflow_blankNameAndNoSteps_reportsBoth() {
        val errors = validateWorkflowForm("", 0)
        assertEquals(
            listOf(FormField.WORKFLOW_NAME, FormField.WORKFLOW_STEPS),
            errors.map { it.field }
        )
        assertEquals("Workflow name is required.", errors[0].message)
        assertEquals("Add at least one step.", errors[1].message)
    }

    @Test
    fun workflow_nameOkButNoSteps_reportsOnlySteps() {
        val errors = validateWorkflowForm("Morning", 0)
        assertEquals(listOf(FormField.WORKFLOW_STEPS), errors.map { it.field })
    }

    @Test
    fun workflow_negativeStepCount_isTreatedAsEmpty() {
        assertEquals(1, validateWorkflowForm("Morning", -1).size)
    }

    @Test
    fun workflow_valid_returnsEmpty() {
        assertTrue(validateWorkflowForm("Morning", 2).isEmpty())
    }
}
