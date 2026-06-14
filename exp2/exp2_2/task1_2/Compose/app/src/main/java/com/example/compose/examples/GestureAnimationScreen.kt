package com.example.compose.examples

import androidx.compose.animation.core.*
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun GestureAnimationScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "手势动画示例",
            style = MaterialTheme.typography.headlineMedium
        )
        
        DragGestureExample()
        TapGestureExample()
        SwipeGestureExample()
        PinchGestureExample()
    }
}

@Composable
fun DragGestureExample() {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    
    // 使用 animateFloatAsState 创建回弹动画
    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "offsetX"
    )
    
    val animatedOffsetY by animateFloatAsState(
        targetValue = offsetY,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "offsetY"
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
                text = "1. 拖拽手势动画 - 拖拽后回弹",
                style = MaterialTheme.typography.titleMedium
            )
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(
                        MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .offset(x = animatedOffsetX.dp, y = animatedOffsetY.dp)
                        .size(80.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            CircleShape
                        )
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                offsetX += dragAmount.x
                                offsetY += dragAmount.y
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "拖拽我",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            
            Button(
                onClick = {
                    offsetX = 0f
                    offsetY = 0f
                }
            ) {
                Text("重置位置")
            }
        }
    }
}

@Composable
fun TapGestureExample() {
    var isPressed by remember { mutableStateOf(false) }
    var tapCount by remember { mutableIntStateOf(0) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "scale"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed)
            MaterialTheme.colorScheme.secondary
        else
            MaterialTheme.colorScheme.primary,
        animationSpec = tween(200),
        label = "backgroundColor"
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
                text = "2. 点击手势动画 - 按下反馈",
                style = MaterialTheme.typography.titleMedium
            )
            
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .scale(scale)
                    .background(color = backgroundColor, shape = CircleShape)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                isPressed = true
                                tryAwaitRelease()
                                isPressed = false
                            },
                            onTap = {
                                tapCount++
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "点击我",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "点击次数: $tapCount",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun SwipeGestureExample() {
    var swipeDirection by remember { mutableStateOf<SwipeDirection?>(null) }
    var swipeCount by remember { mutableIntStateOf(0) }
    
    val offsetX by animateFloatAsState(
        targetValue = when (swipeDirection) {
            SwipeDirection.Left -> -200f
            SwipeDirection.Right -> 200f
            else -> 0f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "swipeOffsetX",
        finishedListener = {
            // 动画完成后重置
            if (swipeDirection != null) {
                swipeDirection = null
            }
        }
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (swipeDirection != null) 0.5f else 1f,
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
                text = "3. 滑动手势动画 - 左右滑动",
                style = MaterialTheme.typography.titleMedium
            )
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .offset(x = offsetX.dp)
                        .size(120.dp)
                        .background(
                            MaterialTheme.colorScheme.tertiary.copy(alpha = alpha),
                            RoundedCornerShape(16.dp)
                        )
                        .pointerInput(Unit) {
                            var currentDragX = 0f
                            detectDragGestures(
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    currentDragX += dragAmount.x
                                    // 实时更新位置
                                    if (kotlin.math.abs(currentDragX) < 250) {
                                        swipeDirection = if (currentDragX > 0) {
                                            SwipeDirection.Right
                                        } else {
                                            SwipeDirection.Left
                                        }
                                    }
                                },
                                onDragEnd = {
                                    val dragDistance = currentDragX
                                    if (kotlin.math.abs(dragDistance) > 100) {
                                        swipeDirection = if (dragDistance > 0) {
                                            SwipeDirection.Right
                                        } else {
                                            SwipeDirection.Left
                                        }
                                        swipeCount++
                                    } else {
                                        swipeDirection = null
                                    }
                                    currentDragX = 0f
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "滑动我",
                        color = MaterialTheme.colorScheme.onTertiary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            
            Text(
                text = "滑动次数: $swipeCount",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

enum class SwipeDirection {
    Left, Right
}

@Composable
fun PinchGestureExample() {
    var scale by remember { mutableFloatStateOf(1f) }
    var rotation by remember { mutableFloatStateOf(0f) }
    
    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "pinchScale"
    )
    
    val animatedRotation by animateFloatAsState(
        targetValue = rotation,
        animationSpec = tween(300),
        label = "pinchRotation"
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
                text = "4. 缩放和旋转手势动画",
                style = MaterialTheme.typography.titleMedium
            )
            
            // 简化的缩放控制（实际应用中需要使用 detectTransformGestures）
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { scale = (scale - 0.1f).coerceAtLeast(0.5f) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("缩小")
                }
                Button(
                    onClick = { scale = (scale + 0.1f).coerceAtMost(2f) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("放大")
                }
                Button(
                    onClick = { rotation += 45f },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("旋转")
                }
            }
            
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(
                        MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .scale(animatedScale)
                        .rotate(animatedRotation)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "缩放: ${String.format(java.util.Locale.getDefault(), "%.1f", animatedScale)}x",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "旋转: ${animatedRotation.toInt()}°",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            
            Button(
                onClick = {
                    scale = 1f
                    rotation = 0f
                }
            ) {
                Text("重置")
            }
        }
    }
}

