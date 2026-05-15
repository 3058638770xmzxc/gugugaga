
# Jetpack Compose 动画详解

## 1. 什么是 Compose 动画

Jetpack Compose 动画是 Android 开发中用于创建流畅动画效果的 API 集合。它提供了一种声明式的方式来创建动画，使得动画的实现变得更加简洁和直观。Compose 动画系统和传统的 Animation API 有很大不同。传统方式需要写很多代码，还要手动管理动画的生命周期，实现起来比较复杂。Compose 动画采用声明式编程，只需要描述动画应该是什么样子，系统会自动处理细节，而不需要手动控制每一步。一个应用通常包含多个动画效果，从最基础的淡入淡出，到手势驱动的复杂交互，Compose 动画都能很好地支持。本文将通过实际示例，介绍 Compose 动画的核心 API 和使用方法。Compose 动画的主要优势在于代码简洁、逻辑清晰。简单动画可以轻松组合成复杂效果，这种设计让动画实现变得更加直观。与传统方式相比，Compose 动画的代码量明显减少，实现相同效果所需的代码大约能减少 60% 左右。

### 传统方式和Compose 方式的区别

Compose 动画和传统的 Animation API 的主要区别在于编程范式。传统的方式是命令式的，需要手动管理动画的生命周期，而 Compose 是声明式的，只需要描述动画应该是什么样子，系统会自动来处理细节。

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

从代码量上看，Compose 动画相比传统方式能减少大约 60% 的代码。更重要的是，声明式的写法让代码更容易被理解，不需要关心动画的生命周期管理，Compose 可以自动的处理这些细节，这也大大降低了内存泄漏的风险。而且，Compose 动画的组合性很强，可以轻松地将多个动画组合在一起，而这在传统系统中需要大量的代码才能实现。

在实际使用中，经常需要同时动画化多个属性，比如缩放、旋转和位移等。在传统方式中，需要创建多个 Animator 并手动同步它们，而在 Compose 中，只需要几行代码就可以实现：

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


## 2. 创建动画

### 2.1 使用 AnimatedVisibility 创建可见性动画

AnimatedVisibility 是 Compose 中最常用的动画 API 之一，它主要用于控制组件的显示和隐藏。它的 API 设计简洁，只需要几行代码就可以实现出流畅的动画效果。

要使用 AnimatedVisibility，需要创建一个状态变量来控制组件的可见性，然后在 AnimatedVisibility`中指定进入和退出动画。使用方法如下所示：

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

AnimatedVisibility可以将多种进入和退出动画组合使用。常用的动画类型包括：

fadeIn/fadeOut，slideIn/slideOut，scaleIn/scaleOut以及expandIn/shrinkOut，分别对应了淡入淡出效果，滑动进入/退出效果，缩放进入/退出效果以及展开/收缩效果。

可以通过 + 运算符将多个动画效果组合起来，创造出更丰富的视觉效果。组合动画的用法：

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

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/87a016d65e1f4d47a842b4bb3bad3adb.gif#pic_center)


### 2.2 使用 Transition 创建多属性组合动画

有时候需要同时动画化多个属性，比如卡片展开的时候，高度、颜色等多个属性都要一起变化。如果分别对每个属性做动画，那将可能会出现各个属性的动画运行的速度不一致的问题，看起来不够协调。updateTransition 可以用来解决这个问题，它能够保证多个属性同步动画，来保证动画的一致性。

要创建 Transition，需要使用 updateTransition 函数，并且指定目标状态。然后通过 transition.animateXxx 方法为每个属性创建动画。使用方法：

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

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/300cffa19ae74856a7b70a17fe47b22e.gif#pic_center)


### 2.3 使用 AnimatedContent 创建内容切换动画

内容的直接切换会显得十分生硬，AnimatedContent 能够在内容切换时创建平滑的过渡效果，可以适用于数字计数器、页面切换等场景。

AnimatedContent的使用方法：

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
![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/f70664659d744ae19a0e6a0e1eee3864.gif#pic_center)

### 2.4 组合多个动画

Compose 提供了多种组合动画的方式，可以把不同的动画效果组合起来，创造出更丰富的视觉体验。

在 AnimatedVisibility 中，可以用 + 运算符把多个动画效果组合起来，比如同时应用滑动、淡入、缩放：

```kotlin
AnimatedVisibility(
    visible = visible,
    enter = slideInVertically() + fadeIn() + scaleIn(),  // 同时应用滑动、淡入、缩放
    exit = slideOutVertically() + fadeOut() + scaleOut()
) {
    // 内容
}
```

在 AnimatedContent 中，可以用 togetherWith 同时定义进入和退出动画：

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

还可以把不同的动画 API 组合起来使用，比如在复杂组合动画中同时使用 Transition、AnimatedVisibility 和 LaunchedEffect：

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
![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/e7edb3e79e924bb8a3316ffe5587121e.gif#pic_center)

### 2.5 使用 InfiniteTransition 创建无限循环动画

InfiniteTransition用于创建无限循环的动画效果，非常适合加载指示器和装饰性动画。

InfiniteTransition 的使用方法：

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

在实际使用中，可以在一个 InfiniteTransition 中同时创建多个属性的无限动画：

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
![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/4fc24ff54f294ae39623944edcfb3765.gif#pic_center)


## 3. 配置动画

### 3.1 AnimationSpec 配置

AnimationSpec 定义了动画的行为方式，包括时长、缓动效果等。Compose 提供了多种AnimationSpec，不同的场景使用不同的 AnimationSpec。

#### 3.1.1 AnimationSpec 类型说明

**tween - 时间插值动画**

tween 是最常用的动画规格，用于在指定时间内从起始值到目标值。使用方法：

```kotlin
tween(
    durationMillis = 300,  // 动画时长（毫秒）
    easing = FastOutSlowInEasing  // 缓动函数
)
```

**spring - 弹簧动画**

spring 创建自然的物理效果，适合拖拽回弹等场景。主要的参数包括了阻尼比和刚度，使用方法：

```kotlin
spring(
    dampingRatio = Spring.DampingRatioMediumBouncy,  // 阻尼比
    stiffness = Spring.StiffnessMedium  // 刚度
)
```


### 3.2 Easing（插值器）配置

Easing 定义了动画的缓动效果，影响动画的速度曲线。Compose 提供了多种内置插值器：

LinearEasing、FastOutSlowInEasing、FastOutLinearInEasing以及LinearOutSlowInEasing，分别对应了线性变化，速度恒定，快速开始，缓慢结束，快速开始，线性结束以及线性开始，缓慢结束。

FastOutSlowInEasing 的效果最自然，适用于绝大多数的场景。在代码中，如果没有指定 easing，tween 会默认使用 FastOutSlowInEasing。

### 3.3 Spring 动画参数配置

Spring 动画使用物理模型创建自然的动画效果，主要参数包括：

**阻尼比（DampingRatio）**：
	DampingRatioHighBouncy，DampingRatioMediumBouncy，DampingRatioLowBouncy以及DampingRatioNoBouncy，分别对应高弹性，有明显回弹效果，中等弹性，通用场景，低弹性，自然效果以及无弹性，快速响应。


**刚度（Stiffness）**：
StiffnessVeryLow，StiffnessLow，StiffnessMedium，StiffnessHigh以及StiffnessVeryHigh，分别对应非常慢，适合大范围移动，慢，适合拖拽回弹，中等，通用场景，快，适合快速反馈以及非常快，适合即时响应。

在拖拽手势动画中，通常使用 DampingRatioMediumBouncy 和 StiffnessLow 来创建自然的回弹效果。

## 4. 动画的核心机制

### 4.1 状态驱动的动画

Compose 动画是基于状态驱动，当元件的状态发生变化时，动画会自动计算并执行。这样的方式让动画的实现变得简单，只需要更新状态，动画就会自动的进行处理。

### 4.2 动画的组合机制

Compose 动画支持多种组合方式，可以将不同的动画效果组合起来，创造出更丰富的视觉效果。组合方式包括：

- **使用 + 运算符**：在 AnimatedVisibility 中组合多个进入/退出动画
- **使用 togetherWith**：在 AnimatedContent 中同时定义进入和退出动画
- **组合多个 API**：将 Transition、AnimatedVisibility、LaunchedEffect 等组合使用

### 4.3 动画的生命周期管理

Compose 动画会自动的管理生命周期，当组件被移除或者状态发生变化时，动画会自动的取消和清理，不需要手动的对动画进行管理。这种方式大大降低了内存泄漏的风险。

## 5. 实际应用场景

### 5.1 列表项动画

在 LazyColumn 中为列表项添加动画效果是常见的需求。以下是实际的实现：

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

必须设置 key 参数，确保 Compose 正确识别列表项，这是列表动画的关键。使用 spring 动画创建自然的进入效果，使用 tween 动画创建快速的退出效果。

#### 5.1.1 列表项展开收起动画

列表项展开收起是常见的交互，可以使用 AnimatedVisibility和 Transition 实现。实现方法如下：

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

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/9d273b5b6a7f4c2d9ff5098252280dbb.gif#pic_center)


### 5.2 手势驱动的动画

手势驱动的动画是 Compose 动画的强大应用场景。通过手势检测，可以创建丰富的交互式动画效果，显著提升用户体验。以下是实现示例：

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

手势动画的实现相对简单，detectTapGestures 和 detectDragGestures 提供了便捷的手势检测 API，比手动处理事件更方便。animateFloatAsState 可以创建回弹效果，配合 spring 动画可以获得自然的物理效果。animateColorAsState 用于颜色过渡动画，使用简单。手势直接更新状态，动画会自动处理，这种设计使得手势驱动的动画实现变得直观。

![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/8899c5068472476ab826315f8a285744.gif#pic_center)![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/859e9894330a49e793d35cdf1c592e7a.gif#pic_center)



### 5.3 复杂状态转换动画

对于复杂的状态转换，Transition 提供了便捷的处理方式，每个状态之间的转换都可以配置不同的动画。实现方法如下所示：

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
![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/a58c8607b5ff4f3eb54d141b9462542a.gif#pic_center)

## 6. 性能优化与最佳实践

在动画实现过程中，性能优化是一个重要的考虑因素。接下来我介绍一些常用的性能优化的技巧。

### 6.1 使用 remember 缓存动画规格

在动画实现中，使用 remember 缓存 AnimationSpec 非常重要。如果每次重组都重新创建 AnimationSpec，会导致性能问题。正确的做法如下所示：

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
![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/13c79e91bf4c45abaa84d0f3db40bafa.gif#pic_center)

### 6.2 避免不必要的重组

频繁重组会影响性能。使用 derivedStateOf 可以减少重组次数，仅在状态实际变化时才触发重组。使用方法如下所示：

```kotlin
// 错误：每次 count 变化都会重组
val isEven = count % 2 == 0

// 正确：只在 isEven 实际变化时重组
val isEven by remember {
    derivedStateOf { count % 2 == 0 }
}
```

### 6.3 合理使用 LaunchedEffect

异步操作和副作用应该放在 LaunchedEffect 里，不要在 Composable 中直接执行。使用方法如下所示：

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

### 6.4 动画性能监控

当绘制性能出现问题时，可以使用 Modifier.drawWithCache 和 Modifier.drawWithContent 进行优化。这些优化通常用于复杂的动画场景，使用方法如下所示：

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



### 6.5 避免在动画中执行耗时操作

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

### 6.6 常见问题和解决方案

在动画开发过程中，会遇到一些常见问题，以下是解决方案：

#### 6.6.1 动画值未正确更新

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

#### 6.6.2 忘记设置 key

**问题**：列表动画中，必须设置 key 参数，否则 Compose 无法正确识别列表项。

**解决方案**：在 items 函数中设置 key 参数，确保每个列表项都有唯一标识。正确做法如下所示：

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

#### 6.6.3 动画规格配置不当

**问题**：动画时长过长会影响用户体验。

**解决方案**：合理设置动画时长。建议时长：快速反馈 100-200ms，标准动画 200-300ms，复杂动画 300-500ms。正确做法如下所示：

```kotlin
// 错误：动画时间过长，影响用户体验
animationSpec = tween(5000)

// 正确：合理的动画时长
animationSpec = tween(300) // 或使用 spring
```

#### 在动画计算中进行复杂操作

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

### 7.1 复杂组合动画 - 多阶段动画

多个动画组合可以创建复杂的效果。以下示例展示了如何将 Transition、AnimatedVisibility 和 LaunchedEffect 组合使用：

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

该示例展示了多种动画方式的组合：Transition 用于同步动画多个属性，AnimatedVisibility 控制内容显示，LaunchedEffect 用于延迟触发动画。组合使用可获得良好的效果。
![在这里插入图片描述](https://i-blog.csdnimg.cn/direct/d21c5bdf3b9f424f913afeee980fb312.gif#pic_center)

## 8. 项目配置

### 8.1 依赖配置

项目使用 Gradle Version Catalog 管理依赖，主要配置如下：

**gradle/libs.versions.toml**：
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

**app/build.gradle.kts**：
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

Jetpack Compose 动画系统通过声明式编程范式，提供了简洁高效的动画 API。AnimatedVisibility 用于控制组件的显示隐藏，AnimatedContent 实现内容切换动画，updateTransition 支持多属性同步动画，InfiniteTransition 创建无限循环动画。通过 AnimationSpec 可以配置动画的时长、缓动效果等参数，tween、spring、`keyframes 分别适用于不同的场景。Compose 动画的核心优势在于代码简洁、组合性强，可以将简单的动画效果组合成复杂的视觉效果，同时自动管理动画生命周期，降低内存泄漏风险。在实际开发中，合理使用 remember 缓存、类型化状态 API 等优化技巧，可以进一步提升动画性能。Compose 技术对 Android 开发产生了深远影响。声明式 UI 编程范式显著提升了开发效率，减少了大量样板代码，使开发者能够更专注于业务逻辑。Compose 的组件化设计理念促进了代码复用和维护性，动画系统作为其中的重要组成部分，让创建流畅的用户体验变得更加简单。Compose 与 Kotlin 的深度集成，充分利用了语言特性，提供了类型安全的 API。随着 Compose 的不断成熟和推广，它正在成为 Android 现代 UI 开发的主流方案，为 Android 应用开发带来了新的可能性和更高的开发标准。

---



