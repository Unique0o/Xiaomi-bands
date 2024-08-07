package com.example.logifitappp.ui.components.forms

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.OutlinedTextField as MaterialOutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.PasswordVisibilityToggleText
import com.example.logifitappp.ui.components.Text

@Composable
fun OutlinedTextField(
    modifier: Modifier = Modifier,
    asPassword: Boolean = false,
    error: String? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOption: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    leadingIcon: ImageVector? = null,
    onValueChange: (TextFieldValue) -> Unit,
    placeholder: String,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    value: TextFieldValue
) {
    var showPassword by rememberSaveable {
        mutableStateOf(false)
    }

    val hasError = !error.isNullOrEmpty()

    MaterialOutlinedTextField(
        colors = TextFieldDefaults.colors().copy(
            cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
            errorContainerColor = Color.Transparent,
            errorIndicatorColor = MaterialTheme.colorScheme.error,
            errorLabelColor = MaterialTheme.colorScheme.error,
            errorLeadingIconColor = MaterialTheme.colorScheme.error,
            errorSupportingTextColor = MaterialTheme.colorScheme.error,
            focusedContainerColor = Color.Transparent,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
            unfocusedLabelColor = MaterialTheme.colorScheme.surfaceTint,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.surfaceTint
        ),
        isError = hasError,
        keyboardActions = keyboardActions,
        keyboardOptions = if (asPassword) KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ) else keyboardOption,
        label = {
            Text(
                text = placeholder,
                typography = MaterialTheme.typography.bodyMedium
            )
        },
        leadingIcon = if (leadingIcon != null) ({
            Icon(
                imageVector = leadingIcon,
                modifier = Modifier.size(16.dp),
                contentDescription = null
            )
        }) else null,
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        onValueChange = onValueChange,
        readOnly = readOnly,
        supportingText = {
            if (hasError) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = error!!,
                    textAlign = TextAlign.Right,
                    typography = MaterialTheme.typography.labelSmall
                )
            }
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        trailingIcon = if (asPassword) ({
            PasswordVisibilityToggleText(showPassword = showPassword) {
                showPassword = !showPassword
            }
        }) else trailingIcon,
        value = value,
        visualTransformation = if (asPassword && !showPassword) PasswordVisualTransformation() else VisualTransformation.None
    )
}