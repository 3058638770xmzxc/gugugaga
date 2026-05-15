package com.example.compose.examples

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown

@Composable
fun TransitionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Transition 动画示例",
            style = MaterialTheme.typography.headlineMedium
        )
        
        BasicTransitionExample()
        MultiStateTransitionExample()
        AnimatedContentExample()
        ComplexTransitionExample()
    }
}

enum class BoxState {
    Small, Medium, Large
}

@Composable
fun BasicTransitionExample() {
    var boxState by remember { mutableStateOf(BoxState.Small) }
    
    // 创建 Transition 来同时动画化多个属性
    val transition = updateTransition(
        targetState = boxState,
        label = "box_transition"
    )
    
    val size by transition.animateInt(
        transitionSpec = {
            when {
                BoxState.Small isTransitioningTo BoxState.Medium ->
                    tween(600, easing = FastOutSlowInEasing)
                BoxState.Medium isTransitioningTo BoxState.Large ->
                    spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                else ->
                    tween(300)
            }
        },
        label = "size"
    ) { state ->
        when (state) {
            BoxState.Small -> 80
            BoxState.Medium -> 120
            BoxState.Large -> 160
        }
    }
    
    val color by transition.animateColor(
        transitionSpec = { tween(600) },
        label = "color"
    ) { state ->
        when (state) {
            BoxState.Small -> MaterialTheme.colorScheme.primary
            BoxState.Medium -> MaterialTheme.colorScheme.secondary
            BoxState.Large -> MaterialTheme.colorScheme.tertiary
        }
    }
    
    val cornerRadius by transition.animateInt(
        transitionSpec = { tween(600) },
        label = "cornerRadius"
    ) { state ->
        when (state) {
            BoxState.Small -> 8
            BoxState.Medium -> 16
            BoxState.Large -> 32
        }
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "1. 基础 Transition - 多属性同步动画",
                style = MaterialTheme.typography.titleMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BoxState.entries.forEach { state ->
                    Button(
                        onClick = { boxState = state },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (boxState == state)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(state.name)
                    }
                }
            }
            
            Box(
                modifier = Modifier
                    .size(size.dp)
                    .clip(RoundedCornerShape(cornerRadius.dp))
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = boxState.name,
                    color = when (boxState) {
                        BoxState.Small -> MaterialTheme.colorScheme.onPrimary
                        BoxState.Medium -> MaterialTheme.colorScheme.onSecondary
                        BoxState.Large -> MaterialTheme.colorScheme.onTertiary
                    }
                )
            }
        }
    }
}

enum class LoadingState {
    Idle, Loading, Success, Error
}

@Composable
fun MultiStateTransitionExample() {
    var loadingState by remember { mutableStateOf(LoadingState.Idle) }
    
    val transition = updateTransition(
        targetState = loadingState,
        label = "loading_transition"
    )
    
    val backgroundColor by transition.animateColor(
        transitionSpec = { tween(500) },
        label = "backgroundColor"
    ) { state ->
        when (state) {
            LoadingState.Idle -> MaterialTheme.colorScheme.surfaceVariant
            LoadingState.Loading -> MaterialTheme.colorScheme.primary
            LoadingState.Success -> MaterialTheme.colorScheme.tertiary
            LoadingState.Error -> MaterialTheme.colorScheme.error
        }
    }
    
    val progress by transition.animateFloat(
        transitionSpec = {
            when {
                LoadingState.Idle isTransitioningTo LoadingState.Loading ->
                    tween(1000, easing = LinearEasing)
                LoadingState.Loading isTransitioningTo LoadingState.Success ->
                    spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                else -> tween(300)
            }
        },
        label = "progress"
    ) { state ->
        when (state) {
            LoadingState.Idle -> 0f
            LoadingState.Loading -> 0.5f
            LoadingState.Success -> 1f
            LoadingState.Error -> 0f
        }
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "2. 多状态 Transition - 加载状态动画",
                style = MaterialTheme.typography.titleMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LoadingState.entries.forEach { state ->
                    Button(
                        onClick = { loadingState = state },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (loadingState == state)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            text = state.name.take(4),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(backgroundColor, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = loadingState.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = when (loadingState) {
                            LoadingState.Idle -> MaterialTheme.colorScheme.onSurfaceVariant
                            LoadingState.Loading -> MaterialTheme.colorScheme.onPrimary
                            LoadingState.Success -> MaterialTheme.colorScheme.onTertiary
                            LoadingState.Error -> MaterialTheme.colorScheme.onError
                        }
                    )
                    if (loadingState == LoadingState.Loading) {
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedContentExample() {
    var count by remember { mutableIntStateOf(0) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "3. AnimatedContent - 内容切换动画",
                style = MaterialTheme.typography.titleMedium
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(onClick = { count-- }) {
                    Text(
                        text = "−",
                        style = MaterialTheme.typography.displayMedium
                    )
                }
                
                AnimatedContent(
                    targetState = count,
                    transitionSpec = {
                        // 定义进入和退出动画
                        if (targetState > initialState) {
                            // 增加：从右侧滑入
                            slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(300)
                            ) + fadeIn() togetherWith
                            slideOutHorizontally(
                                targetOffsetX = { -it },
                                animationSpec = tween(300)
                            ) + fadeOut()
                        } else {
                            // 减少：从左侧滑入
                            slideInHorizontally(
                                initialOffsetX = { -it },
                                animationSpec = tween(300)
                            ) + fadeIn() togetherWith
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(300)
                            ) + fadeOut()
                        }
                    },
                    label = "count_animation"
                ) { targetCount ->
                    Text(
                        text = "$targetCount",
                        style = MaterialTheme.typography.displayMedium
                    )
                }
                
                IconButton(onClick = { count++ }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "增加"
                    )
                }
            }
        }
    }
}

enum class CardState {
    Collapsed, Expanded
}

@Composable
fun ComplexTransitionExample() {
    var cardState by remember { mutableStateOf(CardState.Collapsed) }
    
    val transition = updateTransition(
        targetState = cardState,
        label = "card_transition"
    )
    
    val height by transition.animateDp(
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        },
        label = "height"
    ) { state ->
        when (state) {
            CardState.Collapsed -> 100.dp
            CardState.Expanded -> 300.dp
        }
    }
    
    val padding by transition.animateDp(
        transitionSpec = { tween(600) },
        label = "padding"
    ) { state ->
        when (state) {
            CardState.Collapsed -> 16.dp
            CardState.Expanded -> 32.dp
        }
    }
    
    val cornerRadius by transition.animateDp(
        transitionSpec = { tween(600) },
        label = "cornerRadius"
    ) { state ->
        when (state) {
            CardState.Collapsed -> 8.dp
            CardState.Expanded -> 24.dp
        }
    }
    
    val rotation by transition.animateFloat(
        transitionSpec = { tween(600) },
        label = "rotation"
    ) { state ->
        when (state) {
            CardState.Collapsed -> 0f
            CardState.Expanded -> 180f
        }
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "4. 复杂 Transition - 卡片展开/收起",
                style = MaterialTheme.typography.titleMedium
            )
            
            Button(
                onClick = { cardState = if (cardState == CardState.Collapsed) CardState.Expanded else CardState.Collapsed }
            ) {
                Text(if (cardState == CardState.Collapsed) "展开" else "收起")
            }
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .clip(RoundedCornerShape(cornerRadius))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.rotate(rotation)
                    )
                    Text(
                        text = if (cardState == CardState.Collapsed) "点击展开" else "已展开",
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (cardState == CardState.Expanded) {
                        Text(
                            text = "这是一个复杂的 Transition 示例，展示了多个属性同时动画化的效果。",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

