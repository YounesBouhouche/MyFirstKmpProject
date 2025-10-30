package org.example.project.presentation.util

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExpressiveButton(
    size: Dp,
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = colors,
        shapes = ButtonDefaults.shapesFor(size),
        contentPadding = ButtonDefaults.contentPaddingFor(size),
        modifier = modifier
    ) {
        icon?.let {
            Icon(icon, null, modifier = Modifier.size(ButtonDefaults.iconSizeFor(size)))
            Spacer(Modifier.width(ButtonDefaults.iconSpacingFor(size)))
        }
        Text(text, style = ButtonDefaults.textStyleFor(size))
    }
}