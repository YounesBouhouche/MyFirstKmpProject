package org.example.project.presentation.routes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import myfirstkmpproject.composeapp.generated.resources.Res
import myfirstkmpproject.composeapp.generated.resources.battery_level
import myfirstkmpproject.composeapp.generated.resources.compose_multiplatform
import org.example.project.presentation.MyViewModel
import org.example.project.util.BatteryManager
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(
    viewModel: MyViewModel,
    batteryManager: BatteryManager
) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(Res.drawable.compose_multiplatform),
            contentDescription = "Compose Multiplatform Logo",
            modifier = Modifier.size(200.dp)
        )
        Text(text = viewModel.getHelloWorld())
        Text(text = stringResource(
            Res.string.battery_level,
            batteryManager.getBatteryLevel()
        ))
    }
}