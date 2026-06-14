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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp

@Composable
fun BasicAnimationScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "基础动画示例",
            style = MaterialTheme.typography.headlineMedium
        )
        
        AnimatedVisibilityExample()
        AnimatedValueExample()
        CrossfadeExample()
        CombinedAnimationExample()
    }
}

@Composable
fun AnimatedVisibilityExample() {
    var visible by remember { mutableStateOf(true) }
    
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
                text = "1. AnimatedVisibility - 可见性动画",
                style = MaterialTheme.typography.titleMedium
            )
            
            Button(onClick = { visible = !visible }) {
                Text(if (visible) "隐藏" else "显示")
            }
            
            // 基础淡入淡出
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "淡入淡出动画",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            
            // 缩放动画
            AnimatedVisibility(
                visible = visible,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(
                            MaterialTheme.colorScheme.secondary,
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "缩放 + 淡入淡出",
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
            
            // 滑动动画
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(500)
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = tween(500)
                ) + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(
                            MaterialTheme.colorScheme.tertiary,
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "滑动 + 淡入淡出",
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedValueExample() {
    var isExpanded by remember { mutableStateOf(false) }
    
    // 使用 animateFloatAsState 创建动画值
    val scale by animateFloatAsState(
        targetValue = if (isExpanded) 1.5f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(600),
        label = "rotation"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (isExpanded) 0.5f else 1f,
        animationSpec = tween(300),
        label = "alpha"
    )
    
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
                text = "2. AnimatedValue - 数值动画",
                style = MaterialTheme.typography.titleMedium
            )
            
            Button(onClick = { isExpanded = !isExpanded }) {
                Text(if (isExpanded) "缩小" else "放大")
            }
            
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .scale(scale)
                    .rotate(rotation)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = alpha),
                        RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "动画",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            
            // 使用 animateIntAsState
            var count by remember { mutableIntStateOf(0) }
            val animatedCount by animateIntAsState(
                targetValue = count,
                animationSpec = tween(500),
                label = "count"
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { count-- }) {
                    Text("-")
                }
                Text(
                    text = "$animatedCount",
                    style = MaterialTheme.typography.headlineMedium
                )
                Button(onClick = { count++ }) {
                    Text("+")
                }
            }
        }
    }
}

@Composable
fun CrossfadeExample() {
    var currentPage by remember { mutableIntStateOf(0) }
    val pages = listOf("页面 1", "页面 2", "页面 3")
    
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
                text = "3. Crossfade - 内容切换动画",
                style = MaterialTheme.typography.titleMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                pages.forEachIndexed { index, _ ->
                    Button(
                        onClick = { currentPage = index },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (currentPage == index)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text("${index + 1}")
                    }
                }
            }
            
            Crossfade(
                targetState = currentPage,
                animationSpec = tween(500),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            when (page) {
                                0 -> MaterialTheme.colorScheme.primary
                                1 -> MaterialTheme.colorScheme.secondary
                                else -> MaterialTheme.colorScheme.tertiary
                            },
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = pages[page],
                        style = MaterialTheme.typography.headlineMedium,
                        color = when (page) {
                            0 -> MaterialTheme.colorScheme.onPrimary
                            1 -> MaterialTheme.colorScheme.onSecondary
                            else -> MaterialTheme.colorScheme.onTertiary
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CombinedAnimationExample() {
    var isAnimating by remember { mutableStateOf(false) }
    
    // 组合多个动画值
    val scale by animateFloatAsState(
        targetValue = if (isAnimating) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )
    
    val rotation by animateFloatAsState(
        targetValue = if (isAnimating) 360f else 0f,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "rotation"
    )
    
    val offsetX by animateFloatAsState(
        targetValue = if (isAnimating) 100f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "offsetX"
    )
    
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
                text = "4. 组合动画 - 多个动画值同时运行",
                style = MaterialTheme.typography.titleMedium
            )
            
            Button(onClick = { isAnimating = !isAnimating }) {
                Text(if (isAnimating) "重置" else "开始动画")
            }
            
            Box(
                modifier = Modifier
                    .offset(x = offsetX.dp)
                    .size(100.dp)
                    .scale(scale)
                    .rotate(rotation)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "组合",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

