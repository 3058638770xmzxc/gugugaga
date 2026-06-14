# Jetpack Compose 动画详解

## 1. 什么是 Compose 动画

Jetpack Compose 动画是 Android 开发中用于创建流畅动画效果的 API 集合。它提供了一种声明式的方式来创建动画，使得动画的实现变得更加简洁和直观。

Compose 动画系统和传统的 Animation API 有很大不同。传统方式需要写很多代码，还要手动管理动画的生命周期，实现起来比较复杂。Compose 动画采用声明式编程，只需要描述动画应该是什么样子，系统会自动处理细节，而不需要手动控制每一步。

一个应用通常包含多个动画效果，从最基础的淡入淡出，到手势驱动的复杂交互，Compose 动画都能很好地支持。本文将通过实际项目示例，介绍 Compose 动画的核心 API 和使用方法。

Compose 动画的主要优势在于代码简洁、逻辑清晰。简单动画可以轻松组合成复杂效果，这种设计让动画实现变得更加直观。与传统方式相比，Compose 动画的代码量明显减少，实现相同效果所需的代码大约能减少 60% 左右。

### 1.1 传统方式 vs Compose 方式

Compose 动画和传统的 Animation API 的主要区别在于编程范式。传统的方式是命令式的，需要手动管理动画的生命周期，而 Compose 是声明式的，只需要描述动画应该是什么样子，系统会自动处理细节。

**传统 Animation API**（命令式）：
```kotlin
// 需要手动管理动画生命周期
val animator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
animator.duration = 300
animator.addListener(object : AnimatorListenerAdapter() {
    override fun onAnimationEnd(animation: Animator) {
        // 处理动画结束
    }
})
animator.start()

// 需要手动取消
animator.cancel()
```

**Compose Animation**（声明式）：
```kotlin
// 声明式，自动管理生命周期
val alpha by animateFloatAsState(
    targetValue = if (visible) 1f else 0f,
    animationSpec = tween(300),
    finishedListener = {
        // 动画完成回调
    }
)

// 自动处理取消和清理
```

从代码量上看，Compose 动画相比传统方式能减少约 60% 的代码。更重要的是，声明式的写法让代码更易理解，不需要关心动画的生命周期管理，Compose 会自动处理这些细节，这也大大降低了内存泄漏的风险。而且，Compose 动画的组合性很强，可以轻松地将多个动画组合在一起，这在传统 View 系统中需要大量代码才能实现。

在项目中，经常需要同时动画化多个属性，比如缩放、旋转和位移。在传统方式中，需要创建多个 Animator 并手动同步它们，而在 Compose 中，只需要几行代码就能实现：

```kotlin
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

// 在 UI 中组合使用
Box(
    modifier = Modifier
        .offset(x = offsetX.dp)
        .scale(scale)
        .rotate(rotation)
) {
    // 内容
}
```

这种组合方式在传统 View 系统中需要大量代码，而在 Compose 中只需要几行。

## 2. Compose 动画 API

### 2.1 可见性动画 - AnimatedVisibility

`AnimatedVisibility` 是 Compose 中最常用的动画 API 之一，主要用于控制组件的显示和隐藏。它的 API 设计简洁，只需要几行代码就能实现流畅的动画效果。

要使用 `AnimatedVisibility`，需要创建一个状态变量来控制组件的可见性，然后在 `AnimatedVisibility` 中指定进入和退出动画。使用方法如下所示：

```kotlin
var visible by remember { mutableStateOf(true) }

AnimatedVisibility(
    visible = visible,
    enter = fadeIn(),
    exit = fadeOut()
) {
    Text("Hello Compose")
}
```

`AnimatedVisibility` 支持将多种进入和退出动画组合使用。常用的动画类型包括：

- **fadeIn/fadeOut**：淡入淡出效果
- **slideIn/slideOut**：滑动进入/退出效果
- **scaleIn/scaleOut**：缩放进入/退出效果
- **expandIn/shrinkOut**：展开/收缩效果

可以通过 `+` 运算符将多个动画效果组合起来，创造出更丰富的视觉效果。组合动画的用法如下所示：

```kotlin
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
    // 内容
}
```

> **（此处放置gif）**：展示 `AnimatedVisibility` 的淡入淡出、滑动进入退出等动画效果。

### 2.2 多属性同步动画 - Transition

有时候需要同时动画化多个属性，比如卡片展开的时候，高度、圆角、颜色都要一起变化。如果分别对每个属性做动画，可能会出现各个属性的动画进度不一致的问题，看起来不够协调。`updateTransition` 可以解决这个问题，它能保证多个属性同步动画，确保动画的一致性。

要创建 Transition，需要使用 `updateTransition` 函数，并指定目标状态。然后通过 `transition.animateXxx` 方法为每个属性创建动画。使用方法如下所示：

```kotlin
enum class BoxState { Small, Medium, Large }

var boxState by remember { mutableStateOf(BoxState.Small) }

val transition = updateTransition(
    targetState = boxState,
    label = "box_transition"
)

val size by transition.animateInt(
    transitionSpec = {
        when {
            BoxState.Small isTransitioningTo BoxState.Medium ->
                tween(600, easing = FastOutSlowInEasing)
            else -> tween(300)
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
        BoxState.Small -> Color.Red
        BoxState.Medium -> Color.Blue
        BoxState.Large -> Color.Green
    }
}
```

> **（此处放置gif）**：展示 Transition 多属性同步动画效果。

### 2.3 内容切换动画 - AnimatedContent

内容直接切换会显得生硬，`AnimatedContent` 能够在内容切换时创建平滑的过渡效果，适用于数字计数器、页面切换等场景。

`AnimatedContent` 的使用方法如下所示：

```kotlin
var count by remember { mutableStateOf(0) }

AnimatedContent(
    targetState = count,
    transitionSpec = {
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
    Text("$targetCount")
}
```

### 2.4 无限循环动画 - InfiniteTransition

`InfiniteTransition` 用于创建无限循环的动画效果，非常适合加载指示器和装饰性动画。

`InfiniteTransition` 的使用方法如下所示：

```kotlin
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
```

在实际项目中，可以在一个 `InfiniteTransition` 中同时创建多个属性的无限动画，如下所示：

```kotlin
@Composable
fun InfiniteAnimationExample() {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    
    // 旋转动画
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    // 缩放动画
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    // 透明度动画
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    Box(
        modifier = Modifier
            .rotate(rotation)
            .scale(scale)
            .background(
                MaterialTheme.colorScheme.primary.copy(alpha = alpha),
                CircleShape
            )
    ) {
        // 内容
    }
}
```

> **（此处放置gif）**：展示 InfiniteTransition 无限循环动画效果。

### 2.5 动画配置

#### 2.5.1 AnimationSpec 配置

AnimationSpec 定义了动画的行为方式，包括时长、缓动效果等。Compose 提供了多种 AnimationSpec，不同的场景使用不同的 AnimationSpec。

**AnimationSpec 选择指南**：

| 场景 | 推荐 AnimationSpec | 示例 |
|------|------------------|------|
| 快速反馈 | `tween(100-200ms)` | 按钮点击 |
| 标准动画 | `tween(200-300ms)` | 页面切换 |
| 自然效果 | `spring()` | 拖拽回弹 |
| 复杂路径 | `keyframes` | 多阶段动画 |
| 自定义效果 | 自定义 `Easing` | 特殊需求 |

**AnimationSpec 类型说明**：

**tween - 时间插值动画**

`tween` 是最常用的动画规格，用于在指定时间内从起始值过渡到目标值。使用方法如下所示：

```kotlin
tween(
    durationMillis = 300,  // 动画时长（毫秒）
    easing = FastOutSlowInEasing  // 缓动函数
)
```

**spring - 弹簧动画**

`spring` 创建自然的物理效果，适合拖拽回弹等场景。主要参数包括阻尼比和刚度，使用方法如下所示：

```kotlin
spring(
    dampingRatio = Spring.DampingRatioMediumBouncy,  // 阻尼比
    stiffness = Spring.StiffnessMedium  // 刚度
)
```

**keyframes - 关键帧动画**

`keyframes` 用于创建多阶段动画，可以定义动画过程中的多个关键点。使用方法如下所示：

```kotlin
keyframes {
    durationMillis = 1000
    0f at 0      // 0ms 时值为 0f
    0.5f at 300  // 300ms 时值为 0.5f
    1f at 1000   // 1000ms 时值为 1f
}
```

#### 2.5.2 Easing（插值器）配置

Easing 定义了动画的缓动效果，影响动画的速度曲线。Compose 提供了多种内置插值器：

- **LinearEasing**：线性变化，速度恒定
- **FastOutSlowInEasing**：快速开始，缓慢结束（最常用，最自然）
- **FastOutLinearInEasing**：快速开始，线性结束
- **LinearOutSlowInEasing**：线性开始，缓慢结束

`FastOutSlowInEasing` 的效果最为自然，适用于大多数场景。在代码中，如果未指定 easing，`tween` 默认使用 `FastOutSlowInEasing`。

#### 2.5.3 Spring 动画参数配置

Spring 动画使用物理模型创建自然的动画效果，主要参数包括：

**阻尼比（DampingRatio）**：
- `DampingRatioHighBouncy`：高弹性，有明显回弹效果
- `DampingRatioMediumBouncy`：中等弹性，通用场景（推荐）
- `DampingRatioLowBouncy`：低弹性，自然效果
- `DampingRatioNoBouncy`：无弹性，快速响应

**刚度（Stiffness）**：
- `StiffnessVeryLow`：非常慢，适合大范围移动
- `StiffnessLow`：慢，适合拖拽回弹
- `StiffnessMedium`：中等，通用场景（推荐）
- `StiffnessHigh`：快，适合快速反馈
- `StiffnessVeryHigh`：非常快，适合即时响应

在拖拽手势动画中，通常使用 `DampingRatioMediumBouncy` 和 `StiffnessLow` 来创建自然的回弹效果。

Compose 动画支持多种组合方式，可以将不同的动画效果组合起来，创造出更丰富的视觉效果。

**使用 `+` 运算符组合动画**：

在 `AnimatedVisibility` 中，可以用 `+` 运算符把多个动画效果组合起来，比如同时应用滑动、淡入、缩放：

```kotlin
AnimatedVisibility(
    visible = visible,
    enter = slideInVertically() + fadeIn() + scaleIn(),  // 同时应用滑动、淡入、缩放
    exit = slideOutVertically() + fadeOut() + scaleOut()
) {
    // 内容
}
```

**使用 `togetherWith` 组合进入和退出动画**：

在 `AnimatedContent` 中，可以用 `togetherWith` 同时定义进入和退出动画：

```kotlin
AnimatedContent(
    targetState = count,
    transitionSpec = {
        slideInHorizontally() + fadeIn() togetherWith
        slideOutHorizontally() + fadeOut()
    }
) { targetCount ->
    Text("$targetCount")
}
```

**组合多个动画 API**：

可以把不同的动画 API 组合起来用，比如在复杂组合动画中同时用 `Transition`、`AnimatedVisibility` 和 `LaunchedEffect`：

```kotlin
// Transition 用于同步动画多个属性
val transition = updateTransition(targetState = isExpanded)
val height by transition.animateDp { /* ... */ }
val cornerRadius by transition.animateDp { /* ... */ }

// AnimatedVisibility 控制内容显示
AnimatedVisibility(visible = showContent) {
    // 内容
}

// LaunchedEffect 用于延迟触发
LaunchedEffect(isExpanded) {
    delay(300)
    showContent = true
}
```

### 3.2 动画的生命周期管理

Compose 动画自动管理生命周期，当组件被移除或状态发生变化时，动画会自动取消和清理，无需手动管理。这种机制大大降低了内存泄漏的风险。

## 4. 实际应用场景

### 4.1 列表项动画

在 `LazyColumn` 中为列表项添加动画效果是常见的需求。以下是实际项目中的完整实现：

#### 基础列表动画

```kotlin
@Composable
fun AnimatedListExample() {
    var items by remember {
        mutableStateOf(
            (1..10).map { index ->
                ListItem(
                    id = index,
                    title = "项目 $index",
                    description = "这是第 $index 个列表项"
                )
            }
        )
    }
    
    LazyColumn(
        modifier = Modifier.height(300.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = items,
            key = { it.id }  // 关键：必须设置 key
        ) { item ->
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = tween(300)
                ) + fadeOut()
            ) {
                ListItemCard(item = item)
            }
        }
    }
}
```

**关键点**：必须设置 `key` 参数，确保 Compose 正确识别列表项，这是列表动画的关键。使用 `spring` 动画创建自然的进入效果，使用 `tween` 动画创建快速的退出效果。

#### 4.1.1 列表项展开收起动画

列表项展开收起是常见的交互需求，可以使用 `AnimatedVisibility` 配合 `Transition` 实现。实现方法如下所示：

```kotlin
@Composable
fun ExpandableListItem(item: ListItem) {
    var isExpanded by remember { mutableStateOf(false) }
    
    val transition = updateTransition(
        targetState = isExpanded,
        label = "expand_transition"
    )
    
    val height by transition.animateDp(
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        },
        label = "height"
    ) { state ->
        if (state) 200.dp else 80.dp
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        onClick = { isExpanded = !isExpanded }
    ) {
        Column {
            Text(text = item.title)
            
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Text(text = item.description)
            }
        }
    }
}
```

> **（此处放置gif）**：展示列表项展开收起动画效果。

### 4.2 手势驱动的动画

手势驱动的动画是 Compose 动画的强大应用场景。结合手势检测，可以创建丰富的交互式动画效果，显著提升用户体验。以下是项目中的实现示例：

#### 拖拽手势动画

```kotlin
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
    
    Box(
        modifier = Modifier
            .offset(x = animatedOffsetX.dp, y = animatedOffsetY.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
    ) {
        // 内容
    }
}
```

#### 点击手势动画

```kotlin
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
    
    Box(
        modifier = Modifier
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
            }
    ) {
        // 内容
    }
}
```

手势动画的实现相对简单，`detectTapGestures` 和 `detectDragGestures` 提供了便捷的手势检测 API，比手动处理事件更方便。`animateFloatAsState` 可以创建回弹效果，配合 `spring` 动画可以获得自然的物理效果。`animateColorAsState` 用于颜色过渡动画，使用简单。手势直接更新状态，动画会自动处理，这种设计使得手势驱动的动画实现变得直观。

> **（此处放置gif）**：展示手势驱动动画效果，包括拖拽、点击、滑动等交互。

### 4.3 复杂状态转换动画

对于复杂的状态转换，`Transition` 提供了便捷的处理方式。例如加载状态，从空闲到加载中，再到成功或失败，每个状态之间的转换都可以配置不同的动画。实现方法如下所示：

```kotlin
enum class LoadingState { Idle, Loading, Success, Error }

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
        LoadingState.Idle -> Color.Gray
        LoadingState.Loading -> Color.Blue
        LoadingState.Success -> Color.Green
        LoadingState.Error -> Color.Red
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
```

## 5. 性能优化与最佳实践

在动画实现过程中，性能优化是一个重要的考虑因素。以下介绍一些常用的性能优化技巧。

### 5.1 使用 remember 缓存动画规格

在动画实现中，使用 `remember` 缓存 `AnimationSpec` 非常重要。如果每次重组都重新创建 `AnimationSpec`，会导致性能问题。正确的做法如下所示：

```kotlin
// 错误：每次重组都创建新的 AnimationSpec
val scale by animateFloatAsState(
    targetValue = if (expanded) 1.5f else 1f,
    animationSpec = spring() // 每次重组都创建新对象
)

// 正确：使用 remember 缓存
val animationSpec = remember {
    spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )
}

val scale by animateFloatAsState(
    targetValue = if (expanded) 1.5f else 1f,
    animationSpec = animationSpec
)
```

### 5.2 避免不必要的重组

频繁重组会影响性能。使用 `derivedStateOf` 可以减少重组次数，仅在状态实际变化时才触发重组。使用方法如下所示：

```kotlin
// 错误：每次 count 变化都会重组
val isEven = count % 2 == 0

// 正确：只在 isEven 实际变化时重组
val isEven by remember {
    derivedStateOf { count % 2 == 0 }
}
```

### 5.3 合理使用 LaunchedEffect

异步操作和副作用应该放在 `LaunchedEffect` 里，不要在 Composable 中直接执行。使用方法如下所示：

```kotlin
LaunchedEffect(isExpanded) {
    if (isExpanded) {
        delay(300) // 等待展开动画开始
        showContent = true
    } else {
        showContent = false
    }
}
```

### 5.4 动画性能监控

当绘制性能出现问题时，可以使用 `Modifier.drawWithCache` 和 `Modifier.drawWithContent` 进行优化。这些优化通常用于复杂的动画场景，使用方法如下所示：

```kotlin
Box(
    modifier = Modifier
        .size(100.dp)
        .drawWithCache {
            // 缓存绘制内容
        }
) {
    // 内容
}
```

### 6.5 使用类型化状态 API

Compose 提供了类型化的状态 API，性能更好且更安全。推荐使用类型化状态 API，使用方法如下所示：

```kotlin
// 旧方式：使用通用 mutableStateOf
var count by remember { mutableStateOf(0) }
var offsetX by remember { mutableStateOf(0f) }

// 新方式：使用类型化状态
var count by remember { mutableIntStateOf(0) }
var offsetX by remember { mutableFloatStateOf(0f) }
```

类型化状态的优势包括：更好的性能（避免装箱拆箱）、类型安全（编译期即可发现错误）、代码更清晰。

### 6.6 避免在动画中执行耗时操作

在动画计算中进行复杂操作会影响性能。应该预先计算好再传给动画函数，使用方法如下所示：

```kotlin
// 错误：在动画计算中进行复杂操作
val scale by animateFloatAsState(
    targetValue = if (expanded) {
        // 复杂计算
        calculateComplexValue()
    } else 1f
)

// 正确：预先计算
val targetScale = remember(expanded) {
    if (expanded) calculateComplexValue() else 1f
}
val scale by animateFloatAsState(targetValue = targetScale)
```

### 6.7 常见陷阱和解决方案

在动画开发过程中，会遇到一些常见问题，以下是解决方案：

#### 6.7.1 动画值未正确更新

**问题**：直接修改动画值不会触发动画，需要通过状态更新来触发。

**解决方案**：在事件处理中更新状态，而不是直接修改动画值。正确做法如下所示：

```kotlin
// 错误：直接修改动画值
var offset by remember { mutableStateOf(0f) }
val animatedOffset by animateFloatAsState(targetValue = offset)
offset = 100f // 不会触发动画

// 正确：通过状态更新
var offset by remember { mutableStateOf(0f) }
val animatedOffset by animateFloatAsState(targetValue = offset)
// 在事件处理中更新
Button(onClick = { offset = 100f }) { Text("移动") }
```

#### 6.7.2 忘记设置 key

**问题**：列表动画中，必须设置 `key` 参数，否则 Compose 无法正确识别列表项。

**解决方案**：在 `items` 函数中设置 `key` 参数，确保每个列表项都有唯一标识。正确做法如下所示：

```kotlin
// 错误：没有 key，Compose 无法正确识别项
items(items) { item ->
    AnimatedVisibility(visible = true) {
        ItemCard(item)
    }
}

// 正确：使用 key
items(
    items = items,
    key = { it.id }
) { item ->
    AnimatedVisibility(visible = true) {
        ItemCard(item)
    }
}
```

#### 6.7.3 动画规格配置不当

**问题**：动画时长过长会影响用户体验。

**解决方案**：合理设置动画时长。建议时长：快速反馈 100-200ms，标准动画 200-300ms，复杂动画 300-500ms。正确做法如下所示：

```kotlin
// 错误：动画时间过长，影响用户体验
animationSpec = tween(5000)

// 正确：合理的动画时长
animationSpec = tween(300) // 或使用 spring
```

#### 6.7.4 在动画计算中进行复杂操作

**问题**：在动画计算中进行复杂操作会影响性能。

**解决方案**：预先计算好再传给动画函数，使用 `remember` 缓存结果。正确做法如下所示：

```kotlin
// 错误：每次重组都执行复杂计算
val scale by animateFloatAsState(
    targetValue = if (expanded) {
        // 复杂计算，影响性能
        items.sumOf { it.value }.toFloat() / items.size
    } else 1f
)

// 正确：预先计算，使用 remember 缓存
val targetScale = remember(expanded, items) {
    if (expanded) {
        items.sumOf { it.value }.toFloat() / items.size
    } else 1f
}
val scale by animateFloatAsState(targetValue = targetScale)
```

## 7. 高级主题

### 7.1 复杂组合动画示例

以下是一个完整的复杂组合动画示例，展示了如何将 `Transition`、`AnimatedVisibility` 和 `LaunchedEffect` 组合使用，实现多阶段动画效果：

```kotlin
@Composable
fun ComplexCombinedAnimationExample() {
    var isExpanded by remember { mutableStateOf(false) }
    
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
    
    Box(
        modifier = Modifier
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            modifier = Modifier.rotate(rotation)
        )
        
        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            // 展开后的内容
        }
    }
}
```

该示例展示了多种动画方式的组合：`Transition` 用于同步动画多个属性，`AnimatedVisibility` 控制内容显示，`LaunchedEffect` 用于延迟触发动画。组合使用可获得良好的效果。

## 8. 项目配置

### 8.1 依赖配置

项目使用 Gradle Version Catalog 管理依赖，主要配置如下：

**`gradle/libs.versions.toml`**：
```toml
[versions]
compose-bom = "2024.02.00"
kotlin = "1.9.22"

[libraries]
compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "compose-bom" }
compose-ui = { group = "androidx.compose.ui", name = "ui" }
compose-material3 = { group = "androidx.compose.material3", name = "material3" }
material-icons-extended = { group = "androidx.compose.material", name = "material-icons-extended", version = "1.6.1" }
```

**`app/build.gradle.kts`**：
```kotlin
dependencies {
    // Compose BOM
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.material.icons.extended)
    // ...
}
```

### 8.2 编译器配置

编译器配置如下所示：

```kotlin
android {
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}
```

## 总结

Jetpack Compose 动画系统通过声明式编程范式，提供了简洁高效的动画 API。`AnimatedVisibility` 用于控制组件的显示隐藏，`AnimatedContent` 实现内容切换动画，`updateTransition` 支持多属性同步动画，`InfiniteTransition` 创建无限循环动画。通过 `AnimationSpec` 可以配置动画的时长、缓动效果等参数，`tween`、`spring`、`keyframes` 分别适用于不同的场景。Compose 动画的核心优势在于代码简洁、组合性强，可以将简单的动画效果组合成复杂的视觉效果，同时自动管理动画生命周期，降低内存泄漏风险。在实际开发中，合理使用 `remember` 缓存、类型化状态 API 等优化技巧，可以进一步提升动画性能。

Compose 技术对 Android 开发产生了深远影响。首先，声明式 UI 编程范式显著提升了开发效率，减少了大量样板代码，使开发者能够更专注于业务逻辑。其次，Compose 的组件化设计理念促进了代码复用和维护性，动画系统作为其中的重要组成部分，让创建流畅的用户体验变得更加简单。此外，Compose 与 Kotlin 的深度集成，充分利用了语言特性，提供了类型安全的 API。随着 Compose 的不断成熟和推广，它正在成为 Android 现代 UI 开发的主流方案，为 Android 应用开发带来了新的可能性和更高的开发标准。

---


