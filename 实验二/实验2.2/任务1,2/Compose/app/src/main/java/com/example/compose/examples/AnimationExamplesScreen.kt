package com.example.compose.examples

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimationExamplesScreen() {
    var selectedScreen by remember { mutableStateOf<AnimationScreen?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Jetpack Compose 动画示例") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        if (selectedScreen == null) {
            AnimationMenuScreen(
                modifier = Modifier.padding(paddingValues),
                onScreenSelected = { selectedScreen = it }
            )
        } else {
            selectedScreen?.let { screen ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = { selectedScreen = null }) {
                            Text("← 返回")
                        }
                        Text(
                            text = screen.title,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.width(80.dp))
                    }
                    screen.content()
                }
            }
        }
    }
}

@Composable
fun AnimationMenuScreen(
    modifier: Modifier = Modifier,
    onScreenSelected: (AnimationScreen) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "选择动画示例",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        AnimationScreen.entries.forEach { screen ->
            Card(
                onClick = { onScreenSelected(screen) },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = screen.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = screen.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

enum class AnimationScreen(
    val title: String,
    val description: String,
    val content: @Composable () -> Unit
) {
    BASIC(
        title = "基础动画",
        description = "AnimatedVisibility、AnimatedValue、Crossfade 等基础动画 API",
        content = { BasicAnimationScreen() }
    ),
    TRANSITION(
        title = "Transition 动画",
        description = "多状态切换和组合动画",
        content = { TransitionScreen() }
    ),
    LIST(
        title = "列表动画",
        description = "LazyColumn 项动画和列表操作动画",
        content = { ListAnimationScreen() }
    ),
    GESTURE(
        title = "手势动画",
        description = "拖拽、滑动、缩放手势驱动的动画",
        content = { GestureAnimationScreen() }
    ),
    ADVANCED(
        title = "高级动画",
        description = "自定义 AnimationSpec、无限动画、复杂组合",
        content = { AdvancedAnimationScreen() }
    )
}

