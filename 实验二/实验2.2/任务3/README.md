# AICompose

## 项目简介

本项目是一个基于 Android Jetpack Compose 开发的现代化 Android 应用程序，展示了 Compose UI 框架的使用。

## 功能特性

- 🎨 **Jetpack Compose UI**：声明式 UI 框架
- 🎭 **Material Design 3**：遵循最新的 Material You 设计规范
- 🌙 **动态主题**：支持动态配色和主题定制
- 📱 **响应式布局**：适配不同屏幕尺寸

## 项目结构

```
AIcompose/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/aicompose/
│   │   │   │   ├── MainActivity.kt          # 主活动
│   │   │   │   └── ui/theme/                # 主题相关
│   │   │   │       ├── Color.kt             # 颜色定义
│   │   │   │       ├── Theme.kt             # 主题配置
│   │   │   │       └── Type.kt              # 字体排版
│   │   │   ├── res/
│   │   │   │   ├── drawable/                # 可绘制资源
│   │   │   │   ├── mipmap-*/                # 应用图标
│   │   │   │   └── values/                  # 字符串、颜色、主题
│   │   │   └── AndroidManifest.xml
│   │   ├── androidTest/                     # 仪器测试
│   │   └── test/                            # 单元测试
│   └── build.gradle.kts
├── gradle/
│   ├── libs.versions.toml                   # 版本目录
│   └── wrapper/
├── build.gradle.kts
└── settings.gradle.kts
```

## 技术栈

- **语言**: Kotlin
- **最低 SDK 版本**: Android 8.0 (API 26)
- **目标 SDK 版本**: Android 14 (API 34)
- **UI 框架**: Jetpack Compose
- **设计语言**: Material Design 3
- **构建工具**: Gradle Kotlin DSL
- **依赖管理**: Version Catalog (libs.versions.toml)

## 主要依赖

- `androidx.compose.ui` - Compose UI 核心
- `androidx.compose.material3` - Material 3 组件
- `androidx.compose.ui.tooling.preview` - Compose 预览工具
- `androidx.activity:activity-compose` - Activity Compose 集成
- `androidx.lifecycle:lifecycle-runtime-ktx` - 生命周期管理

## 运行环境要求

- Android Studio Hedgehog 或更高版本
- JDK 17+
- Android 设备或模拟器（API 26+）

## 构建与运行

1. 克隆或下载项目到本地
2. 使用 Android Studio 打开项目
3. 等待 Gradle 同步完成
4. 连接 Android 设备或启动模拟器
5. 点击运行按钮

## 使用说明

启动应用后即可看到基于 Jetpack Compose 构建的用户界面，体验 Material Design 3 的现代化设计风格。

## 截图展示

### 🎨 应用主页面

![AICompose 主页面](../images/Y1.png)

*图 1: AICompose 应用主页面，展示 Jetpack Compose 和 Material Design 3 的用户界面*

## 实验信息

- **实验编号**: 实验 2.2 - 任务 3
- **实验名称**: AICompose - Jetpack Compose 应用开发
- **开发环境**: Android Studio
- **开发语言**: Kotlin
- **UI 框架**: Jetpack Compose

## Jetpack Compose 优势

1. **声明式编程**：通过简单的函数调用描述 UI
2. **更少的代码**：相比传统 View 系统，代码量更少
3. **强大的工具支持**：Android Studio 提供实时预览
4. **与现有代码兼容**：可以与现有的 View 系统互操作
5. **响应式编程**：基于 Compose 的状态管理，UI 自动更新

## 核心概念

### Composable 函数
```kotlin
@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}
```

### 状态管理
使用 `remember` 和 `mutableStateOf` 管理 UI 状态

### Material Theme
通过 `MaterialTheme` 统一应用的颜色、形状和排版

## 注意事项

1. Jetpack Compose 需要 Android Studio 较新版本支持
2. 部分旧款设备可能存在兼容性问题
3. 建议使用真机测试以获得最佳体验

## 学习资源

- [Jetpack Compose 官方文档](https://developer.android.com/jetpack/compose)
- [Compose 路径学习](https://developer.android.com/courses/pathways/compose)
- [Material Design 3 指南](https://m3.material.io/)

## 许可证

本项目仅用于学习和实验目的。
