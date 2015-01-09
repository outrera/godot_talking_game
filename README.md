## TalkingData游戏分析模块 for [GodotEngine](http://godotengine.org)

### 安装
下载TalkingGameSDK, 将其中的jar拷贝至该文件所在目录     
[Godot的Android Module教程](https://github.com/okamstudio/godot/wiki/tutorial_android_module)     
[编译Godot的Android版本](https://github.com/okamstudio/godot/wiki/compiling_android)

### 设置
- **(必须)**在godot的export中添加Android的ACCESS_NETWORK_STATE, READ_PHONE_STATE, ACCESS_WIFI_STATE, WRITE_EXTERNAL_STORAGE权限
- **(必须)**在godot的project setting中添加string字段"*android/modules*", 在value中填入"*com/android/godot/GodotTalkingGame*"
- **(必须)**在godot的project setting中添加string字段"*talking_game/app_id*"和"*talking_game/channel*",分别填入app id和渠道号

### 使用

    func _init():
        var talking_game = Globals.get_singleton("TalkingGame")

提供一个封装好的[gdscript](https://gist.github.com/quabug/09fe11b8b8245e7579e7), 可以用在跨平台的调试

### LICENSE
MIT
