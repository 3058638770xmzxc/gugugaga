# Android 移动开发实验报告

本仓库为 Android 移动开发课程的全部实验内容，涵盖 Android 开发环境搭建、Kotlin 语法学习、Jetpack Compose UI 开发、CameraX 相机应用、数据分析、TensorFlow Lite 移动端部署以及 TensorFlow 模型训练等实验。

---

## 实验总览

| # | 实验名称 | 内容简介 |
|---|---------|---------|
| 1 | [**开发环境搭建**](exp1/README.md) | 安装 Android Studio、Jupyter Notebook（Anaconda）、VS Code 等开发工具，并附安装过程截图 |
| 2.1 | [**Kotlin 基本语法**](exp2/exp2_1/README.md) | 学习 Kotlin 变量、空安全、控制流、函数、Lambda、集合、类与 data class 等核心语法，完成 Kotlin Koans 练习 |
| 2.2 | [**Jetpack Compose 应用开发**](exp2/exp2_2/README.md) | 使用 Kotlin + Jetpack Compose + Material Design 3 开发 AI 图像识别应用界面，构建 APK |
| 2.3 | [**CameraX 相机应用**](exp2/exp2_3/README.md) | 基于 Android Jetpack CameraX API 开发相机应用，实现拍照和录像功能 |
| 3 | [**Fortune 500 数据分析**](exp3/README.md) | 使用 pandas / matplotlib / seaborn 对 1955-2005 年财富 500 强数据进行探索性分析与可视化 |
| 4 | [**TFLite 花卉识别 Android 应用**](exp4/README.md) | 基于 TensorFlow Lite + CameraX 在 Android 端实时识别花卉（雏菊、蒲公英、玫瑰、向日葵、郁金香） |
| 5.1 | [**TensorFlow 模型训练与 TFLite 导出**](exp5/exp5_1/README.md) | 使用 MobileNetV2 迁移学习训练花卉分类模型，导出 TFLite 量化模型供移动端部署 |
| 5.2 | [**石头剪刀布图像分类**](exp5/exp5_2/README.md) | 使用 TensorFlow Keras 构建 CNN 对石头剪刀布手势图像进行分类，训练 25 个 epoch 后验证准确率达 97.58% |

---

## 实验详情

### exp1 — 开发环境搭建

- 安装 Android Studio 4.1+（LiteRT 支持）
- 通过 Anaconda 安装 Jupyter Notebook 及 Python 科学计算环境
- 安装 VS Code 并配置 Python / Jupyter 插件
- 新建 Android 项目并编译运行

### exp2 — Android 开发基础

**exp2_1 — Kotlin 基本语法**
- 变量声明（val / var）、类型推断
- 空安全机制（?、?:、?.、let）
- 条件表达式（if / when）、循环与区间
- 函数、Lambda、高阶函数
- 集合操作（List、Set、Map）、data class、object 单例
- 完成 Kotlin Koans 练习 >= 85%

**exp2_2 — Jetpack Compose 应用开发**
- 使用 Jetpack Compose 声明式 UI 框架
- Material Design 3 设计语言
- AI 图像识别应用界面实现
- 构建并生成 APK 安装包

**exp2_3 — CameraX 相机应用**
- 使用 CameraX API 实现拍照与录像
- 最低支持 Android 7.0，兼容 Android 16

### exp3 — Fortune 500 数据分析

- 使用 pandas 读取和处理 CSV 数据
- 清洗利润字段中的非数值数据（N.A.）
- 按年份分组统计平均利润与营收
- 使用 matplotlib / seaborn 绘制趋势图（含标准差阴影）

### exp4 — TFLite 花卉识别 Android 应用

- CameraX 实时摄像头捕获画面
- TensorFlow Lite 模型 `FlowerModel.tflite` 推理
- 实时识别 5 种花卉：雏菊、蒲公英、玫瑰、向日葵、郁金香

### exp5 — TensorFlow 机器学习

**exp5_1 — 花卉分类模型训练与导出**
- MobileNetV2 迁移学习，5 个 epoch 训练
- 训练集准确率 91.86%，验证集 91.10%
- 导出动态范围量化的 TFLite 模型

**exp5_2 — 石头剪刀布 CNN 分类**
- 4 层卷积神经网络，数据增强训练
- 训练集 2520 张图片（每类 840 张）
- 25 个 epoch 后训练准确率 95.08%，验证准确率 97.58%

---

## 运行环境

| 工具 | 版本 / 说明 |
|------|------------|
| Android Studio | 4.1+（支持 LiteRT） |
| Kotlin | 用于 Android 应用开发 |
| Python 3 | 数据分析与机器学习 |
| Jupyter Notebook | 通过 Anaconda 安装 |
| TensorFlow | 2.x，模型训练与 TFLite 转换 |
| VS Code | 代码编辑器 |

---

## 文件结构

```
gugugaga/
├── exp1/                   # 实验一：开发环境搭建
├── exp2/
│   ├── exp2_1/             # 实验 2.1：Kotlin 语法
│   ├── exp2_2/             # 实验 2.2：Jetpack Compose
│   └── exp2_3/             # 实验 2.3：CameraX 相机
├── exp3/                   # 实验三：Fortune 500 数据分析
├── exp4/                   # 实验四：TFLite 花卉识别
├── exp5/
│   ├── exp5_1/             # 实验 5.1：TFLite 模型训练导出
│   └── exp5_2/             # 实验 5.2：石头剪刀布分类
└── README.md               # 本文件
```