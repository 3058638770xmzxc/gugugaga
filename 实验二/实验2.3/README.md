# CameraX 相机应用

## 项目简介

本项目是一个基于 Android Jetpack CameraX API 开发的相机应用程序，实现了拍照和录像功能。

## 功能特性

- 📸 **拍照功能**：支持实时预览和拍照捕获
- 🎥 **录像功能**：支持视频录制
- 📁 **媒体存储**：照片和视频自动保存到设备存储
- 🎨 **Material Design UI**：遵循 Material Design 设计规范的用户界面

## 项目结构

```
CameraX/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/camerax/
│   │   │   │   ├── MainActivity.kt          # 主活动
│   │   │   │   └── ui/theme/                # 主题相关
│   │   │   │       ├── Color.kt
│   │   │   │       ├── Theme.kt
│   │   │   │       └── Type.kt
│   │   │   ├── res/
│   │   │   │   ├── layout/                  # 布局文件
│   │   │   │   ├── drawable/                # 可绘制资源
│   │   │   │   ├── mipmap-*/                # 应用图标
│   │   │   │   └── values/                  # 字符串、颜色、主题
│   │   │   └── AndroidManifest.xml
│   │   ├── androidTest/                     # 仪器测试
│   │   └── test/                            # 单元测试
│   └── build.gradle.kts
├── gradle/
└── build.gradle.kts
```

## 技术栈

- **语言**: Kotlin
- **最低 SDK 版本**: Android 8.0 (API 26)
- **目标 SDK 版本**: Android 14 (API 34)
- **UI 框架**: View 系统 (XML 布局)
- **相机 API**: Jetpack CameraX
- **构建工具**: Gradle Kotlin DSL

## 主要依赖

- `androidx.camera:camera-core`
- `androidx.camera:camera-camera2`
- `androidx.camera:camera-lifecycle`
- `androidx.camera:camera-view`
- AndroidX Core KTX
- Material Components

## 权限要求

应用需要以下权限：
- `android.permission.CAMERA` - 相机访问权限
- `android.permission.RECORD_AUDIO` - 录音权限（用于视频录制）
- `android.permission.WRITE_EXTERNAL_STORAGE` - 存储写入权限

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

1. **拍照**:
   - 启动应用后，相机预览会自动显示
   - 点击拍照按钮即可捕获照片
   - 照片保存到设备相册

2. **录像**:
   - 点击录像按钮开始录制
   - 再次点击停止录制
   - 视频保存到设备相册

## 截图与演示

### 📸 拍照页面

![拍照页面](../images/p1.png)

*图 1: CameraX 拍照页面，显示相机预览和拍照按钮*

### 📁 照片存储位置

![照片存储](../images/p2.png)

*图 2: 拍摄的照片自动保存到设备相册*

### 🎥 视频存储位置

![视频存储](../images/p3.png)

*图 3: 录制的视频保存到设备相册*

### 🎬 功能演示视频

<video src="../images/QQ 录屏 20260516124910.mp4" controls="controls" style="max-width: 100%;"></video>

*视频：CameraX 拍照和录像功能完整演示*

## 实验信息

- **实验编号**: 实验 2.3
- **实验名称**: CameraX 相机应用开发
- **开发环境**: Android Studio
- **开发语言**: Kotlin

## 注意事项

1. 首次运行需要在设备上授予相机和存储权限
2. 部分模拟器可能不支持相机功能，建议使用真机测试
3. 确保设备有足够的存储空间来保存照片和视频

## 许可证

本项目仅用于学习和实验目的。
