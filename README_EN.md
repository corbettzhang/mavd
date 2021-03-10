[中文说明/Chinese Documentation](https://github.com/corbettzhang/MAVD/blob/main/README.md)

> What is MAVD and what does MAVD implement?
---
- Mavd is an open source, free software, integrated Video capture, upload to the cloud object storage, and publish to the WordPress blog automated Video capture desktop application.
- Mavd relies on the **youtube-dl** library for its core functions, which are the sites that Mavd supports for **youtube-dl** downloads. See the list of supported sites[支持网站列表](https://monkeyapp.cn/help)
- [官网](https://monkeyapp.cn),[Gitee](https://gitee.com/corbettzhang/MAVD),[Github](https://github.com/corbettzhang/MAVD)

<br/>
<br/>
<br/>

> Features
---
- System shortcut key listening, support when the application loses focus to use the shortcut key to wake up the application
- Make applications on Mac OS closer to native apps, menu bars, docking stations, etc
- System Notification Bar, how to send application messages through the Notification Bar under MacOS and Windows
- Animation problem, how to let the thread with interface linkage
- Calling external libraries in JavaFX applications, such as YouTube-DL, FFmpeg, WebP...
- JavaFX application internationalization
- The JavaFX application switches the dark and bright styles globally

<br/>
<br/>
<br/>

> Quick Start
---
```
git clone https://github.com/corbettzhang/MAVD.git
```

<br/>
<br/>
<br/>

> Build
---
```
jfx:native
```
Packing the exe executable on a Windows system requires installation[Inno Setup Compiler](https://jrsoftware.org/isdl.php)

<br/>
<br/>
<br/>

#### Thanks
> The MAVD application relies on the following libraries：
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

> New features to be developed：
- New interface, preview function, support to download different definition of the video
- Externally dependent libraries, installed by the user, rather than by application packaging. To reduce the size of the application installation package
- Support OOS uploads for different cloud platforms
- Thread speed adjustment can be set when downloading

<br/>
<br/>
<br/>

> Application screenshots

---

- The login
- <img src="https://raw.githubusercontent.com/corbettzhang/MAVD/main/assets/login.png" height="350" width="300" alt="The login"/>

- Home page
- <img src="https://raw.githubusercontent.com/corbettzhang/MAVD/main/assets/main.png" height="470" width="640" alt="Home page"/>

- Preferences
- <img src="https://raw.githubusercontent.com/corbettzhang/MAVD/main/assets/preference.png" height="470" width="640" alt="Preferences"/>

- Task
- <img src="https://raw.githubusercontent.com/corbettzhang/MAVD/main/assets/loading.png" height="420" width="640" alt="Task"/>

