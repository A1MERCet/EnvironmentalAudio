暂无龙核兼容实现 需要可以到GAudioManager和GAudioEffectFactory自行实现  

插件加载后并不会保存默认配置文件
请拷贝插件.jar包里的resource文件夹至配置文件夹
结构:
plugins/
  - EnvironmentalAudio/
    区域配置
    - areas/
      - default_world_area.yml
      - xxx.yml
    音效配置
    - audios/
      - default_world_audio.yml
      - xxx.yml
    音效状态配置
    - states/
      - default_world_state.yml
      - xxx.yml
    - AudioConfig.yml
    - Config.yml

指令
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

变量
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




插件API

事件
AudioPlayEvent(Cancellable)
AudioStopEvent(Cancellable)
AudioCheckEvent(Cancellable)
AudioReloadEvent(配置文件重载事件)

音效创建实例
    public static void example()
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
    }
