# vbrowse
该项目是基于mvp+vitamio来实现的本地视频播放器，实现了大部分播放器相关的功能，同时支持全手势操作控制。源码个人维护，仅供参考学习使用，欢迎issues！



#### 基础功能说明

* 设备内所有视频文件的展示
* 视频的基础播放功能（含手势操作）



#### lib包说明

> implementation project(':vitamio')//视频播放包  <br/>
> implementation 'com.jakewharton:butterknife:9.0.0-rc1'//view注入 <br/>
> implementation 'org.greenrobot:eventbus:3.1.1'//消息传递 <br/>
> implementation 'com.facebook.fresco:fresco:1.11.0'//图片加载 <br/>



#### 更新日志

v1---->>20181015更新

* 主项目框架mvp+butterknfe+eventbus
* vitamio包的引入
* 全局配置
* 资源文件

v2---->>20181018更新

* mediastore取设备所有视频相关信息并主页展示
* helper工具结合mvp播放本地视频
* 播放界面相关的ui布局
* 生命周期管理以及播放状态管理

v3---->>201810123更新

* 播放界面控制ui显示/隐藏
* 音量/亮度手势操作逻辑
* 视频的比例适配
* 增加了contentlayout，内容包裹的基础parentview（展示空/错误/加载状态）

注：readme是做了一段时间之后才更新的，前几个版本的更新内容有点混一块儿了，不详细区分了。

v4---->>20181024更新

* 增加了自带的亮度&音量的提示dialog
* 修改音量dialog的记录模式，目前使用的receiver方式
* 增加了视频截图的获取（读的mediastore数据库直接拿到的bitmap）



#### 相关问题记录

v4

1. 目前的音量/亮度调节在手势触摸的时候更改的跨度太大，要么一下空了要么一下满了，得再研究一下实际控制值的计算方式

