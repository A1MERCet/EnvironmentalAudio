演示视频
https://www.bilibili.com/video/BV1d1421B7un/?spm_id_from=333.999.0.0
pkVQ3E4.md.png

暂无龙核兼容实现 需要可以到GAudioManager和GAudioEffectFactory自行实现  

github



插件加载后并不会保存默认配置文件

请拷贝插件.jar包里的resource文件夹至配置文件夹

配置文件结构:

[SPOILER]

plugins/

-- EnvironmentalAudio/

---- areas/区域配置

-------- default_world_area.yml

-------- xxx.yml

---- audios/音效配置

-------- default_world_audio.yml

-------- xxx.yml

---- states/音效状态配置

-------- default_world_state.yml

-------- xxx.yml

---- AudioConfig.yml

---- Config.yml

[/SPOILER]



配置文件:

[SPOILER]

区域.yml

[CODE=yaml]Area:

  test_area:

    Name: 区域1

    # 世界名

    World: world

    # 区块表

    # 坐标1(x y z) 坐标2(x y z)

    # 多个区块组成一个区块表 用于监测玩家坐标是否在这些区块中

    Chunks:

      - 0 0 0 0 0 0

      - 0 0 0 0 0 0

      - 0 0 0 0 0 0[/CODE]



音效.yml

[CODE=yaml]Audio:

  default_audio1:

    Name: 测试音效1

    # 音效种类

    # DEFAULT   默认圆形距离检测范围音效

    # AREA      不规则区域检测音效

    Type: DEFAULT

    # 播放时执行事件

    # COMMAND 执行指令

    PlayEvent:

      Event1:

        Type: COMMAND

        # %player%替换玩家名

        # %state_id%替换音效状态ID

        # %state_name%替换音效状态名称

        Cmd: m %player% Play

        AsOP: false

        AsConsole: true

    # 停止时执行事件

    StopEvent:

      Event1:

        Type: COMMAND

        Cmd: m %player% Stop

        AsOP: false

        AsConsole: true

    AudioData:

      # 设置音效组 多个音效可以设置同个音效组

      Type: 主音效组

      # 支持 萌芽音效 龙核音效

      # 原版音效minecraft:xxx或modid:xxx

      # 萌芽音效加前缀"germmod:"

      # 龙核音效加前缀"dragon:"

      Sound: test

      # MASTER MUSIC RECORDS WEATHER BLOCKS HOSTILE NEUTRAL PLAYERS AMBIENT VOICE

      Category: MASTER

      # 音效是否循环

      Cycle: true

      # 重设循环时间

      # 启用时 进入音效范围内优先播放音效后再进入循环延迟

      # 关闭时 是否播放取决于全局延迟 这能距离内使全部玩家都同时播放音效 (如果准备播放时玩家不在区域内 音效不会为该玩家播放)

      CycleReset: false

      # 需要能被AudioConfig-Threshold整除的数

      # 循环延迟(固定每多少毫秒播放一次 比音效长度短可能会造成音效重复播放) 小等于0禁用

      CycleDelay: 0

      # 需要能被AudioConfig-Threshold整除的数

      # 音效长度(秒)(暂无用)

      Length: 0

      # 音效量与音高

      Volume: 1

      Pitch: 1

      # 音效触发范围

      Range: 100

      # 音效淡出效果(仅支持萌芽)

      # 开启Enhance后无效

      FadeOut: 80

      # 增强效果

      # 使音效支持3D效果 仅基础音效种类(圆形距离检测)可用

      # 仅支持单通道的音效文件 双通道没有3D效果

      # 使用的原版音效播放

      Enhance: true

      # 冲突音效 在以下音效播放的时候禁止播放

      Exclude:

        - "冲突音效"

      # 静音音效 播放时静音以下音效

      Mute:

        - "将静音的音效"

  area_audio1:

    Name: 测试音效1

    Type: AREA

    # 使用的区域配置(area/xxx.yml)

    Area: test_area

    AudioData:

      Type: 主音效组

      Sound: test

      Category: MASTER

      Cycle: true

      Delay: 0

      Length: 0

      Volume: 1

      Pitch: 1

      Range: 100

      FadeOut: 80

      Enhance: true

      Exclude:

        - "冲突音效"

      Mute:

        - "将静音的音效"[/CODE]



音效状态.yml

[CODE=yaml]AudioState:

  world_default_audio1:

    # 可删除 默认使用音效的配置名(Name)

    Name: 测试音效1

    # 指向配置的音效(audio/xxx.yml)

    Audio: default_audio1

    Location: world 0 0 0

    # 可删除 默认音效的配置(AudioData)

    AudioData:

      Type: 主音效组

      Sound: germmod:test

      Category: MASTER

      Cycle: true

      CycleDelay: 0

      Length: 0

      Volume: 1

      Pitch: 1

      Range: 100

      FadeOut: 80

      Enhance: true

      Exclude: []

      Mute: []

  world_area_audio1:

    # 可删除 默认使用音效的配置名(Name)

    Name: 测试音效1

    # 指向配置的音效(audio/xxx.yml)

    Audio: area_audio1

    Location: world 0 0 0[/CODE]



AudioConfig.yml

[CODE=yaml]# 控制台信息输出

Debug: true

# 是否开启检测文件修改后重载配置文件

EnableFileCheck: true

# 异步检测

# 功能暂未完成

Async: true

# 当启用TickCheck时 每Threshold个tick 监测一次玩家位置以计算播放音效

# 当关闭TickCheck时 玩家每移动Threshold个tick 监测一次玩家位置以计算播放音效

# 所有音效长度 循环延迟设置都需要能整除Threshold 否则音效将会失效

EnableTickCheck: true

Threshold: 1[/CODE]



[/SPOILER]





特性

[SPOILER]

支持副本世界 使用指令/audio copy (原世界) (目标世界) 复制原世界的所有音效状态到目标世界

[/SPOILER]



指令

[SPOILER]

/audio debug  显示全部已注册的音效

/audio debug  [音效ID]  显示已注册的音效

/audio debug  [音效ID]  [世界]  显示已注册的音效

/audio clear  [世界]  清除世界全部音效状态

/audio reload  重新加载配置文件

/audio copy  [世界]  [副本世界]  拷贝所有世界音效状态至副本世界

/audio player  [玩家名]  查看玩家播放状态

/audio show  [音效ID]  查看已注册世界音效

/audio show  [音效ID]  [世界]  查看已注册世界音效

/audio show  查看已注册世界音效

/audio state  enable  [音效状态ID]  打开音效状态

/audio state  disable  [音效状态ID]  关闭音效状态

/audio list  state  查看全部已注册的音效状态

/audio list  查看全部音效

/audio register  [音效ID]  [世界]  [X]  [Y]  [Z]  [保存文件]  注册音效至世界

/audio register  [音效ID]  [创建ID]  [创建名]  [世界]  [X]  [Y]  [Z]  [保存文件]  注册音效至世界

/audio register  [音效ID]  [保存文件]  在当前位置注册音效至世界

/audio remove  [创建ID]  删除音效状态

/audio remove  [世界]  [创建ID]  删除音效状态

/audio check  [音效ID]  查看音效配置

/audio help  查看全部指令

[/SPOILER]



变量

[SPOILER]

%audio_is_id_(value)%

[true/false]

获取当前是否播放了指定value的音效ID



%audio_is_state_(value)%

[true/false]

获取当前是否播放了指定value的音效状态ID



%audio_is_type_(value)%

[true/false]



获取当前是否播放了指定value的音效数据type

%audio_getaudio_(filed)_(value)%

[string]

获取指定音效配置



%audio_size%

[number]

获取当前音效播放列表大小



%audio_list%

[string]

获取当前音效播放列表 以'@'分隔:test_audio1@test_audio2@...

[/SPOILER]







插件API

[SPOILER]



事件

AudioPlayEvent(Cancellable)

AudioStopEvent(Cancellable)

AudioCheckEvent(Cancellable)

AudioReloadEvent(配置文件重载事件)



音效创建实例

[CODE=java]    public static void example()

    {

        //创建音效

        //创建自动添加到全局音效库里(AudioManager.audios)

        audio = new Audio("example","实例音效");



        //可设置回调

        audio.registerCallbackCheck((p,s)->{return true;});

        audio.registerCallbackStop((p,s)-> {return true;});

        audio.registerCallbackPlay((p,s)-> {return true;});



        //通过音效数据修改内容

        //详细请看配置文件或者内部定义

        AudioData audioData = audio.data;

        //设置音效请用AudioData.setSound(String)

        //# 支持 萌芽音效 龙核音效

        //# 原版音效minecraft:xxx或modid:xxx

        //# 萌芽音效加前缀"germmod:"

        //# 龙核音效加前缀"dragon:"

        audioData.setSound("germmod:xxx");





        //以音效可创建多个音效状态

        //id name data默认为音效的配置

        AudioState state = audio.createState(new Location(Bukkit.getWorld("world"),0,0,0));



        //音效状态生效需到AudioManager注册

        AudioManager.registerAudioState(state);



        //可随时设置音效是否开启

        state.setEnable(false);

    }[/CODE]

[/SPOILER]
