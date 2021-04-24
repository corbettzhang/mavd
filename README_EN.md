**[中文说明](https://github.com/corbettzhang/mavd/blob/main/README.md)**

### What functions does **mavd** achieve, and what are the characteristics of **mavd**?

**mavd** is an open source, free software developed based on **JavaFX**. It integrates video capture, uploads to cloud object storage, and publishes to **Wordpress** blog, an automated video capture desktop application

**mavd** relies on the **youtube-dl** library to achieve core functions, **mavd** supports **youtube-dl** supports downloading sites, check **[list](http:ytdl-org.github.ioyoutube-dlsupportedsites.html)** to learn more

> feature

---

- Support multi-platform **Windows**, **MacOS**, **Linux**, and maintain roughly the same UI interface and functions
- System shortcut key monitoring, support the use of shortcut keys to wake up the application when the application loses focus
- Make the application closer to the native application, menu bar, dock, etc. in the MacOS system
- System notification bar, how to send application messages through the notification bar under MacOS and Windows
- Animation problem, how to make the thread cooperate with the interface linkage
- Call external libraries in **JavaFX** applications, such as **youtube-dl**, **ffmpeg**, **webp** . . .
- Implement internationalization in **JavaFX** applications
- Realize global switching of dark and bright styles in **JavaFX** applications

### Quick Start

Get the source code from **[Gitee](https:gitee.comcorbettzhangmavd)** or **[Github](https:github.comcorbettzhangmavd)** and start using
```
git clone https://github.com/corbettzhang/mavd.git
```

Construct

```
jfx:native
```

Requires attention

1. JDK version is jdk1.8.0_271 and later
2. To package the **exe** executable file in the Windows system, you need to install **[Inno Setup Compiler](https:jrsoftware.orgisdl.php)**
3. To use **mavd** in a **Windows** system, you need to install the dependency **[vcredist_x86](http://www.microsoft.com/downloads/info.aspx?na=41&srcfamilyid=a7b7a05e-6de6-4d3a-a423-37bf0912db84&srcdisplaylang=en&u=http%3a%2f%2fdownload.microsoft.com%2fdownload%2f5%2fB%2fC%2f5BC5DBB3-652D-4DCE-B14A-475AB85EEF6E%2fvcredist_x86.exe)**
4. To use **mavd** in a **Linux** system, you need to install and rely on **python2**. For specific steps, please refer to **[here](http:ytdl-org.github.ioyoutube-dlsupportedsites.html)**
5. To use **Wordpress** user login, you need to install **JSON API User** and **JSON API** plug-in on the **Wordpress** website, and enable the login API

### Update record

---

January 28, 2021

v1.0.0 update content

1.Package and release v 1.0.0 version

2.Fix some bugs

---

February 15, 2021

v1.0.1 update content

1.Increase the dark style switching function

2.Increase language internationalization function

3.Fix some bugs

---

### Thanks to the following open source projects

The **mavd** application depends on the following open source libraries

- jfoenix
- controlsfx*______________*
- fontawesomefx
- jnativehook
- ffmpeg
- youtube-dl
- webp-io

### New features that need to be developed

- New interface, preview function, support for downloading videos of different definitions

- External dependency libraries are installed by users themselves, not through application packaging. To reduce the size of the application installation package

- Support different cloud platforms OOS upload

- When downloading, you can set the thread speed adjustment

### App screenshot

<img src="https://raw.githubusercontent.com/corbettzhang/mavd/main/assets/login.png" height="350" width="300" alt="Login"/>

<img src="https://raw.githubusercontent.com/corbettzhang/mavd/main/assets/main.png" height="470" width="640" alt="Home page"/>

<img src="https://raw.githubusercontent.com/corbettzhang/mavd/main/assets/preference.png" height="470" width="640" alt="Preferences"/>

<img src="https://raw.githubusercontent.com/corbettzhang/mavd/main/assets/loading.png" height="420" width="640" alt="Task"/>
