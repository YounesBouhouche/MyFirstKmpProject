package org.example.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import myfirstkmpproject.composeapp.generated.resources.Res
import myfirstkmpproject.composeapp.generated.resources.battery_level
import myfirstkmpproject.composeapp.generated.resources.compose_multiplatform
import org.example.project.dependencies.MyViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(batteryManager: BatteryManager) {
    val viewModel = koinViewModel<MyViewModel>()
    MaterialTheme {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(Res.drawable.compose_multiplatform),
                contentDescription = "Compose Multiplatform Logo"
            )
            Text(text = viewModel.getHelloWorld())
            Text(text = stringResource(
                Res.string.battery_level,
                batteryManager.getBatteryLevel()
            ))
        }
    }
}