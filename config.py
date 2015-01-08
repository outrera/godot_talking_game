def can_build(platform):
    return platform == 'android'

def configure(env):
    if env['platform'] == 'android':
        env.android_module_file('GodotTalkingGame.java')
        env.android_module_library('Game_Analytics_SDK_Android.jar')
