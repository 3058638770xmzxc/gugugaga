# Jetpack Compose 动画示例

一个展示 Jetpack Compose 动画功能的完整示例项目，包含多种动画效果的实现和最佳实践。

## 📱 项目简介

本项目是一个基于 Jetpack Compose 的动画示例集合，展示了 Compose 动画系统的各种功能和用法。项目包含了从基础动画到复杂组合动画的完整示例，适合学习和参考。

### 主要特性

- ✨ **声明式动画** - 使用 Compose 的声明式 API 创建流畅动画
- 🎨 **多种动画类型** - 包含可见性动画、内容切换动画、多属性同步动画等
- 🎯 **手势驱动动画** - 支持拖拽、点击等手势交互动画
- 📋 **列表动画** - LazyColumn 列表项动画效果
- 🔄 **状态转换动画** - 复杂状态之间的平滑过渡
- ⚡ **性能优化** - 展示动画性能最佳实践

## 🏗️ 项目结构

```
app/src/main/java/com/example/compose/
├── MainActivity.kt                    # 主 Activity
├── examples/
│   ├── BasicAnimationScreen.kt        # 基础动画示例
│   ├── AdvancedAnimationScreen.kt     # 高级动画示例
│   ├── AnimationExamplesScreen.kt     # 动画集合示例
│   ├── GestureAnimationScreen.kt      # 手势动画示例
│   ├── ListAnimationScreen.kt         # 列表动画示例
│   └── TransitionScreen.kt            # 状态转换动画示例
└── ui/theme/
    ├── Color.kt                       # 颜色定义
    ├── Shape.kt                       # 形状定义
    ├── Theme.kt                       # 主题定义
    └── Type.kt                        # 字体定义
```

## 🚀 技术栈

- **语言**: Kotlin
- **UI 框架**: Jetpack Compose
- **架构模式**: 声明式 UI
- **最低 SDK**: API 24 (Android 7.0)
- **目标 SDK**: API 36
- **Compose 版本**: 2024.02.00 BOM
- **Kotlin 版本**: 1.9.22

## 📦 依赖配置

项目使用 Gradle Version Catalog 管理依赖：

```kotlin
// Compose BOM
implementation(platform(libs.compose.bom))
implementation(libs.compose.ui)
implementation(libs.compose.ui.graphics)
implementation(libs.compose.ui.tooling.preview)
implementation(libs.compose.material3)
implementation(libs.material.icons.extended)
implementation(libs.compose.activity)
implementation(libs.compose.navigation)
```

## 🎯 动画 API 示例

### 1. 可见性动画 - AnimatedVisibility

```kotlin
var visible by remember { mutableStateOf(true) }

AnimatedVisibility(
    visible = visible,
    enter = fadeIn() + slideInVertically(),
    exit = fadeOut() + slideOutVertically()
) {
    Text("Hello Compose")
}
```

### 2. 多属性同步动画 - Transition

```kotlin
enum class BoxState { Small, Medium, Large }

var boxState by remember { mutableStateOf(BoxState.Small) }
val transition = updateTransition(targetState = boxState, label = "box_transition")

val size by transition.animateInt(label = "size") { state ->
    when (state) {
        BoxState.Small -> 80
        BoxState.Medium -> 120
        BoxState.Large -> 160
    }
}

val color by transition.animateColor(label = "color") { state ->
    when (state) {
        BoxState.Small -> Color.Red
        BoxState.Medium -> Color.Blue
        BoxState.Large -> Color.Green
    }
}
```

### 3. 手势驱动动画

```kotlin
var offsetX by remember { mutableFloatStateOf(0f) }

val animatedOffsetX by animateFloatAsState(
    targetValue = offsetX,
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )
)

Box(
    modifier = Modifier
        .offset(x = animatedOffsetX.dp)
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                offsetX += dragAmount.x
            }
        }
)
```

### 4. 列表动画

```kotlin
LazyColumn {
    items(
        items = items,
        key = { it.id }
    ) { item ->
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            ListItemCard(item)
        }
    }
}
```

## 🎨 动画配置

### AnimationSpec 选择指南

| 场景 | 推荐 AnimationSpec | 示例 |
|------|------------------|------|
| 快速反馈 | `tween(100-200ms)` | 按钮点击 |
| 标准动画 | `tween(200-300ms)` | 页面切换 |
| 自然效果 | `spring()` | 拖拽回弹 |
| 复杂路径 | `keyframes` | 多阶段动画 |

### Spring 动画参数

**阻尼比（DampingRatio）**:
- `DampingRatioHighBouncy` - 高弹性
- `DampingRatioMediumBouncy` - 中等弹性（推荐）
- `DampingRatioLowBouncy` - 低弹性
- `DampingRatioNoBouncy` - 无弹性

**刚度（Stiffness）**:
- `StiffnessVeryLow` - 非常慢
- `StiffnessLow` - 慢
- `StiffnessMedium` - 中等（推荐）
- `StiffnessHigh` - 快
- `StiffnessVeryHigh` - 非常快

## 🛠️ 构建和运行

### 前置要求

- Android Studio Hedgehog 或更高版本
- JDK 11 或更高版本
- Android SDK API 36

### 运行步骤

1. 克隆项目到本地
2. 使用 Android Studio 打开项目
3. 同步 Gradle 文件
4. 运行到模拟器或真机

```bash
# 使用命令行构建
./gradlew assembleDebug
```

## 📚 学习资源

- [Jetpack Compose 官方文档](https://developer.android.com/jetpack/compose)
- [Compose 动画文档](https://developer.android.com/jetpack/compose/animation)
- [Material Design 动画指南](https://material.io/design/motion/)

## 📝 博客文章

项目配套博客文章详见 [blog/jetpack-compose-animation.md](blog/jetpack-compose-animation.md)

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

## 📄 许可证

本项目采用 MIT 许可证。详见 [LICENSE](LICENSE) 文件。

## 👨‍💻 关于

这个项目展示了 Jetpack Compose 动画系统的强大功能和灵活性。通过实际的代码示例，帮助开发者更好地理解和使用 Compose 动画 API。

---

**Made with ❤️ using Jetpack Compose**
