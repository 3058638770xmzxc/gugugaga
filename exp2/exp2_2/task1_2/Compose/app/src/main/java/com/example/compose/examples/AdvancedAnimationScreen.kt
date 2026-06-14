package com.example.compose.examples

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import kotlin.math.pow
import kotlinx.coroutines.delay

@Composable
fun AdvancedAnimationScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "高级动画示例",
            style = MaterialTheme.typography.headlineMedium
        )
        
        CustomAnimationSpecExample()
        InfiniteAnimationExample()
        ComplexCombinedAnimationExample()
        AnimationPerformanceExample()
    }
}

// 自定义缓动函数
class CustomEasing : Easing {
    override fun transform(fraction: Float): Float {
        // 自定义弹性效果
        return if (fraction < 0.5) {
            fraction * 2 * fraction * 2
        } else {
            1 - (2 * (1 - fraction)).toDouble().pow(2.0).toFloat()
        }
    }
}

@Composable
fun CustomAnimationSpecExample() {
    var isAnimating by remember { mutableStateOf(false) }
    
    // 使用自定义 AnimationSpec
    val customSpec = remember {
        tween<Float>(
            durationMillis = 2000,
            easing = CustomEasing()
        )
    }
    
    val springSpec = remember {
        spring<Float>(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessVeryLow
        )
    }
    
    val keyframesSpec = remember {
        keyframes {
            durationMillis = 2000
            0f at 0
            0.5f at 500
            1f at 1000
            0.8f at 1500
            1f at 2000
        }
    }
    
    val animatedValue1 by animateFloatAsState(
        targetValue = if (isAnimating) 200f else 0f,
        animationSpec = customSpec,
        label = "custom"
    )
    
    val animatedValue2 by animateFloatAsState(
        targetValue = if (isAnimating) 200f else 0f,
        animationSpec = springSpec,
        label = "spring"
    )
    
    val animatedValue3 by animateFloatAsState(
        targetValue = if (isAnimating) 200f else 0f,
        animationSpec = keyframesSpec,
        label = "keyframes"
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "1. 自定义 AnimationSpec",
                style = MaterialTheme.typography.titleMedium
            )
            
            Button(onClick = { isAnimating = !isAnimating }) {
                Text(if (isAnimating) "重置" else "开始动画")
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("自定义缓动", style = MaterialTheme.typography.labelSmall)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(
                                MaterialTheme.colorScheme.surface,
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Box(
                            modifier = Modifier
                                .offset(x = animatedValue1.dp)
                                .size(40.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    CircleShape
                                )
                        )
                    }
                }
                
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("弹性动画", style = MaterialTheme.typography.labelSmall)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(
                                MaterialTheme.colorScheme.surface,
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Box(
                            modifier = Modifier
                                .offset(x = animatedValue2.dp)
                                .size(40.dp)
                                .background(
                                    MaterialTheme.colorScheme.secondary,
                                    CircleShape
                                )
                        )
                    }
                }
                
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("关键帧", style = MaterialTheme.typography.labelSmall)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(
                                MaterialTheme.colorScheme.surface,
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Box(
                            modifier = Modifier
                                .offset(x = animatedValue3.dp)
                                .size(40.dp)
                                .background(
                                    MaterialTheme.colorScheme.tertiary,
                                    CircleShape
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfiniteAnimationExample() {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
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
                text = "2. InfiniteTransition - 无限循环动画",
                style = MaterialTheme.typography.titleMedium
            )
            
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .rotate(rotation)
                    .scale(scale)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = alpha),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "无限动画",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            Text(
                text = "旋转、缩放和透明度同时无限循环",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ComplexCombinedAnimationExample() {
    var isExpanded by remember { mutableStateOf(false) }
    
    // 创建多个相关的动画值
    val transition = updateTransition(
        targetState = isExpanded,
        label = "complex_transition"
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
        if (state) 300.dp else 100.dp
    }
    
    val cornerRadius by transition.animateDp(
        transitionSpec = { tween(600) },
        label = "cornerRadius"
    ) { state ->
        if (state) 24.dp else 8.dp
    }
    
    val rotation by transition.animateFloat(
        transitionSpec = { tween(600) },
        label = "rotation"
    ) { state ->
        if (state) 180f else 0f
    }
    
    // 使用 LaunchedEffect 创建延迟动画
    var showContent by remember { mutableStateOf(false) }
    
    LaunchedEffect(isExpanded) {
        if (isExpanded) {
            delay(300) // 等待展开动画开始
            showContent = true
        } else {
            showContent = false
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
                text = "3. 复杂组合动画 - 多阶段动画",
                style = MaterialTheme.typography.titleMedium
            )
            
            Button(onClick = { isExpanded = !isExpanded }) {
                Text(if (isExpanded) "收起" else "展开")
            }
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        RoundedCornerShape(cornerRadius)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.rotate(rotation),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    
                    AnimatedVisibility(
                        visible = showContent,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut() + slideOutVertically()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "复杂动画示例",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "这个示例展示了如何组合多个动画：",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "• Transition 动画",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "• AnimatedVisibility",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "• LaunchedEffect 延迟",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimationPerformanceExample() {
    var itemCount by remember { mutableIntStateOf(5) }
    
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
                text = "4. 动画性能优化示例",
                style = MaterialTheme.typography.titleMedium
            )
            
            Text(
                text = "使用 remember 缓存动画规格，避免重复创建",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { itemCount = (itemCount - 1).coerceAtLeast(1) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("减少")
                }
                Button(
                    onClick = { itemCount = (itemCount + 1).coerceAtMost(20) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("增加")
                }
            }
            
            // 使用 remember 缓存动画规格
            val animationSpec = remember {
                spring<Float>(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            }
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(itemCount) { index ->
                    var isExpanded by remember { mutableStateOf(false) }
                    
                    val scale by animateFloatAsState(
                        targetValue = if (isExpanded) 1.1f else 1f,
                        animationSpec = animationSpec, // 重用缓存的规格
                        label = "scale_$index"
                    )
                    
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .scale(scale),
                        onClick = { isExpanded = !isExpanded },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            text = "项目 ${index + 1}",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

