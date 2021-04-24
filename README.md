**[English Documentation](https://github.com/corbettzhang/mavd/blob/main/README_EN.md)**

### **mavd** 实现了什么功能，以及 **mavd** 的特性？

**mavd** 是一款基于 **JavaFX** 开发的开放源代码、免费软件，集成了视频采集，上载到云对象存储，并发布到 **Wordpress** 博客的自动化视频采集桌面应用程序

**mavd** 依赖 **youtube-dl** 库实现核心功能，**mavd** 支持 **youtube-dl** 支持下载的网站，查看 **[列表](http://ytdl-org.github.io/youtube-dl/supportedsites.html)** 以了解更多

> 特性

---

- 支持多平台 **Windows** ，**MacOS** ，**Linux** ，并保持大致相同的UI界面和功能
- 系统快捷键侦听，支持应用失去焦点时使用快捷键唤醒应用
- 让应用程序在 **MacOS** 系统更接近于原生应用，菜单栏，程序坞等
- 系统通知栏，**MacOS** 、**Windows** 系统下如何通过通知栏发送应用消息
- 动画问题，如何让线程配合界面联动
- 在 **JavaFX** 应用中调用外部库，例如 **youtube-dl** , **ffmpeg** , **webp** . . .
- 在 **JavaFX** 应用程序中实现国际化功能
- 在 **JavaFX** 应用程序中实现全局切换暗黑、明亮样式等

### 快速开始

从 **[Gitee](https://gitee.com/corbettzhang/mavd)** 或 **[Github](https://github.com/corbettzhang/mavd)** 上获取源代码并开始使用

```
git clone https://github.com/corbettzhang/mavd.git
```

构建

```
jfx:native
```

需要注意

1. **JDK** 版本为 **jdk1.8.0_271** 及以后
2. 在 **Windows** 系统中打包 **exe** 可执行文件需要安装 **[Inno Setup Compiler](https://jrsoftware.org/isdl.php)**
3. 在 **Windows** 系统中使用 **mavd** ，需要安装依赖 **[vcredist_x86](http://www.microsoft.com/downloads/info.aspx?na=41&srcfamilyid=a7b7a05e-6de6-4d3a-a423-37bf0912db84&srcdisplaylang=en&u=http%3a%2f%2fdownload.microsoft.com%2fdownload%2f5%2fB%2fC%2f5BC5DBB3-652D-4DCE-B14A-475AB85EEF6E%2fvcredist_x86.exe)**
4. 在 **Linux** 系统中使用 **mavd** ，需要安装依赖 **python2** ，具体操作步骤可以参考 **[这里](http://ytdl-org.github.io/youtube-dl/supportedsites.html)**
5. 使用 **Wordpress** 用户登录需要在 **Wordpress** 网站安装 **JSON API User** 和 **JSON API** 插件，并启用登录API

### 更新记录

---

2021年01月28日

v1.0.0更新内容

1.打包发布v1.0.0版本

2.修复若干BUG

---

2021年02月15日

v1.0.1更新内容

1.增加暗黑样式切换功能

2.增加语言国际化功能

3.修复若干BUG

---

### 感谢以下开源项目

**mavd** 应用依赖以下库

- jfoenix
- controlsfx
- fontawesomefx
- jnativehook
- ffmpeg
- youtube-dl
- webp-io

### 需要开发的新功能

- 新增界面，预览功能，支持下载不同清晰度的视频
  
- 外部依赖库，由用户自行安装，而不是通过应用打包的方式。以减少应用安装包的大小
  
- 支持不同的云平台OOS上传
  
- 下载时，可设置线程速度调节

### 应用截图

<img src="https://raw.githubusercontent.com/corbettzhang/mavd/main/assets/login.png" height="350" width="300" alt="登录"/>

<img src="https://raw.githubusercontent.com/corbettzhang/mavd/main/assets/main.png" height="470" width="640" alt="首页"/>

<img src="https://raw.githubusercontent.com/corbettzhang/mavd/main/assets/preference.png" height="470" width="640" alt="偏好设置"/>

<img src="https://raw.githubusercontent.com/corbettzhang/mavd/main/assets/loading.png" height="420" width="640" alt="任务"/>
