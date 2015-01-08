package com.android.godot;

import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.util.Log;

import com.tendcloud.tenddata.TalkingDataGA;
import com.tendcloud.tenddata.TDGAMission;
import com.tendcloud.tenddata.TDGAItem;
import com.tendcloud.tenddata.TDGAVirtualCurrency;

public class GodotTalkingGame extends Godot.SingletonBase {

    private boolean is_enable = true;
    private Activity activity;

    public void onVirtualCurrencyReward(double virtualCurrencyAmount, String reason) {
        if (is_enable) TDGAVirtualCurrency.onReward(virtualCurrencyAmount, reason);
    }

    public void onItemPurchase(String item, int itemNumber, double priceInVirtualCurrency) {
        if (is_enable) TDGAItem.onPurchase(item, itemNumber, priceInVirtualCurrency);
    }

    public void onItemUse(String item, int itemNumber) {
        if (is_enable) TDGAItem.onUse(item, itemNumber);
    }

    public void onMissionBegin(String missionId) {
        if (is_enable) TDGAMission.onBegin(missionId);
    }

    public void onMissionCompleted(String missionId) {
        if (is_enable) TDGAMission.onCompleted(missionId);
    }

    public void onMissionFailed(String missionId, String cause) {
        if (is_enable) TDGAMission.onFailed(missionId, cause);
    }

    // custom event
    public void onEvent(String eventId, Dictionary eventData) {
        if (is_enable) _onEvent(eventId, eventData);
    }

    private void _onEvent(String eventId, Dictionary eventData) {
        Map<String, Object> params = new HashMap<String, Object>();
        String[] keys = eventData.get_keys();
        for (String key : keys) {
            params.put(key, eventData.get(key));
        };
        TalkingDataGA.onEvent(eventId, params);
    }

    // Initialize
    static public Godot.SingletonBase initialize(Activity p_activity) {
        return new GodotTalkingGame(p_activity);
    }

    public GodotTalkingGame(Activity p_activity) {
        //register class name and functions to bind
        registerClass("TalkingGame", new String[]{
                "onVirtualCurrencyReward", "onItemPurchase", "onItemUse",
                "onMissionBegin", "onMissionCompleted", "onMissionFailed",
                "onEvent"
        });
        is_enable = GodotLib.getGlobal("talking_game/enable") != "False";
        Log.d("godot", "TalkingGame Enabled: "+is_enable);
        activity = p_activity;
        if (!is_enable) return;

        activity.runOnUiThread(new Runnable() {
            public void run() {
                //boolean enable_debug = GodotLib.getGlobal("talking_game/debug") == "True";
                //TalkingDataGA.setDebugMode(enable_debug);
                //Log.d("godot", "TalkingGame DebugMode: "+enable_debug);
                String app_id = GodotLib.getGlobal("talking_game/app_id");
                String channel = GodotLib.getGlobal("talking_game/channel");
                Log.d("godot", "TalkingGame Init: "+app_id+" "+channel);
                TalkingDataGA.init(activity, app_id, channel);
            }
        });

    }

    protected void onMainPause() {
        TalkingDataGA.onPause(activity);
    }

    protected void onMainResume() {
        TalkingDataGA.onResume(activity);
    }
}
