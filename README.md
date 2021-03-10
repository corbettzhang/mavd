[英文说明/English Documentation](https://github.com/corbettzhang/MAVD/blob/main/README_EN.md)

> MAVD是什么，以及MAVD实现的功能？
---
- MAVD 是 Monkey App Video Download 的首字母简写，MAVD是一款开放源代码、免费软件，集成了视频采集，上载到云对象存储，并发布到Wordpress博客的自动化视频采集桌面应用程序。
- MAVD 依赖**youtube-dl**库实现核心功能，也即是MAVD支持youtube-dl支持下载的网站，具体支持网站列表请查看[支持网站列表](https://monkeyapp.cn/help)
- [官网](https://monkeyapp.cn),[Gitee](https://gitee.com/corbettzhang/MAVD),[Github](https://github.com/corbettzhang/MAVD)

<br/>
<br/>
<br/>

> 特性
---
- 系统快捷键侦听，支持应用失去焦点时使用快捷键唤醒应用
- 让应用程序在Mac OS系统更接近于原生应用，菜单栏，程序坞等
- 系统通知栏，MacOS、Windows系统下如何通过通知栏发送应用消息
- 动画问题，如何让线程配合界面联动
- 在JavaFX应用中调用外部库，例如youtube-dl,ffmpeg,webp...
- 在JavaFX应用程序国际化
- 在JavaFX应用程序全局切换暗黑、明亮样式

<br/>
<br/>
<br/>

> 快速开始
---
```
git clone https://github.com/corbettzhang/MAVD.git
```

<br/>
<br/>
<br/>

> 构建
---
```
jfx:native
```
Windows系统中打包exe可执行文件需要安装[Inno Setup Compiler](https://jrsoftware.org/isdl.php)

<br/>
<br/>
<br/>

> 感谢
> MAVD应用依赖以下库：
- jfoenix
- controlsfx
- fontawesomefx
- jnativehook
- ffmpeg
- youtube-dl
- webp-io

<br/>
<br/>
<br/>

> 需要开发的新功能：
- 新增界面，预览功能，支持下载不同清晰度的视频
- 外部依赖库，由用户自行安装，而不是通过应用打包的方式。以减少应用安装包的大小
- 支持不同的云平台OOS上传
- 下载时，可设置线程速度调节

<br/>
<br/>
<br/>

> 应用截图

---

- 登录
- <img src="https://raw.githubusercontent.com/corbettzhang/MAVD/main/assets/login.png" height="350" width="300" alt="登录"/>

- 首页
- <img src="https://raw.githubusercontent.com/corbettzhang/MAVD/main/assets/main.png" height="470" width="640" alt="首页"/>

- 偏好设置
- <img src="https://raw.githubusercontent.com/corbettzhang/MAVD/main/assets/preference.png" height="470" width="640" alt="偏好设置"/>

- 任务
- <img src="https://raw.githubusercontent.com/corbettzhang/MAVD/main/assets/loading.png" height="420" width="640" alt="任务"/>
