<div align="center">

# PlayTask

一个以积分为反馈的 Android 任务管理应用。完成任务获得积分，使用积分兑换奖励，并通过账单和图表回顾收支情况。

[![License](https://img.shields.io/github/license/qxf-72/Playtask?style=flat-square)](LICENSE)
![Platform](https://img.shields.io/badge/platform-Android-3DDC84?style=flat-square&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Java-8%2B-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Min SDK](https://img.shields.io/badge/minSdk-26%2B-3F51B5?style=flat-square&logo=android&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-8.0-02303A?style=flat-square&logo=gradle&logoColor=white)
[![Top language](https://img.shields.io/github/languages/top/qxf-72/Playtask?style=flat-square)](https://github.com/qxf-72/Playtask)
[![Stars](https://img.shields.io/github/stars/qxf-72/Playtask?style=flat-square)](https://github.com/qxf-72/Playtask/stargazers)

</div>

## ✨ 功能

- **任务管理**：创建、编辑、删除任务；支持每日、每周和普通三类任务。
- **积分激励**：完成任务自动记入积分；达到设定完成次数的任务会自动移出列表。
- **奖励兑换**：创建单次或可重复兑换的奖励，并在兑换时扣减积分。
- **数据查看**：按日、周、月、年查看积分收入、支出和结余趋势。
- **账单明细**：按月份浏览积分流水，方便追溯每笔积分的来源与去向。
- **本地存储**：任务、奖励和积分流水均保存于设备本地 SQLite 数据库，无需登录或网络服务。

## 🛠️ 技术栈

| 类别 | 方案 |
| --- | --- |
| 开发语言 | Java |
| UI | AndroidX、Material Components、ViewBinding、RecyclerView、ViewPager2 |
| 架构辅助 | ViewModel、LiveData、Navigation |
| 数据存储 | SQLite |
| 图表 | MPAndroidChart |
| 构建 | Gradle 8.0、Android Gradle Plugin 8.1.2 |

## 📋 运行环境

- Android Studio（建议使用内置 JDK 17）
- Android SDK：`compileSdk 34`
- 最低 Android 版本：Android 8.0（API 26）
- 目标 Android 版本：Android 13（API 33）

## 🚀 快速开始

### 使用 Android Studio

1. 克隆或下载本仓库。
2. 在 Android Studio 中打开 [`playtask`](playtask) 目录，而不是仓库根目录。
3. 等待 Gradle 同步完成，连接设备或启动模拟器。
4. 运行 `app` 配置。

### 使用命令行

在 `playtask` 目录执行：

```powershell
# Windows
.\gradlew.bat assembleDebug
.\gradlew.bat test
```

```bash
# macOS / Linux
./gradlew assembleDebug
./gradlew test
```

调试 APK 默认输出至 `app/build/outputs/apk/debug/`。构建 release 包可执行 `assembleRelease`。

仓库也附带了已构建的 [release APK](playtask/app/release/app-release.apk)，可复制到 Android 设备安装；安装第三方 APK 前请确认来源可信，并按设备提示开启相应安装权限。

## 📖 使用说明

1. 在“任务”页右上角点击 **+**，新建任务并设置类型、积分和目标完成次数。
2. 勾选任务即可完成一次，系统会增加完成次数并记录一笔正向积分；任务完成到设定次数后会从列表中移除。
3. 在“奖励”页新建奖励，点击奖励并确认兑换后，系统会记录一笔负向积分。单次奖励兑换后会移除；多次奖励会保留并累计使用次数。
4. 在“统计”页按日、周、月、年查看收入、支出和结余折线图；点击右上角“账单”可按月查看流水。

> 数据仅保存在当前设备。卸载应用或清除应用数据会删除本地任务、奖励和积分记录。

## 🖼️ 界面预览

| 新建任务 | 完成任务 |
| --- | --- |
| <img src="https://picture-in-md.oss-cn-guangzhou.aliyuncs.com/2024-01-09_214332.png" alt="新建任务" width="360"> | <img src="https://picture-in-md.oss-cn-guangzhou.aliyuncs.com/2024-01-09_214515.png" alt="完成任务" width="360"> |

| 任务排序 | 积分统计 |
| --- | --- |
| <img src="https://picture-in-md.oss-cn-guangzhou.aliyuncs.com/2024-01-09_214834.png" alt="任务排序" width="360"> | <img src="https://picture-in-md.oss-cn-guangzhou.aliyuncs.com/2024-01-09_215551.png" alt="积分统计" width="360"> |

## 📂 项目结构

```text
.
├─ README.md
├─ LICENSE
└─ playtask/                 # Android Studio / Gradle 工程根目录
   ├─ app/
   │  └─ src/main/
   │     ├─ java/com/jnu/playtask/
   │     │  ├─ data/         # SQLite 数据库、数据模型与 DAO
   │     │  ├─ ui/           # 任务、奖励、统计、账单等界面
   │     │  └─ util/         # ViewModel 与通用组件
   │     └─ res/             # 布局、菜单、图片和字符串资源
   └─ gradle/
```

## 📄 许可证

本项目采用 [MIT License](LICENSE) 开源。
