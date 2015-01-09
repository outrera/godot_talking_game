package com.android.godot;

import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.util.Log;

import com.tendcloud.tenddata.TalkingDataGA;
import com.tendcloud.tenddata.TDGAAccount;
import com.tendcloud.tenddata.TDGAMission;
import com.tendcloud.tenddata.TDGAItem;
import com.tendcloud.tenddata.TDGAVirtualCurrency;

public class GodotTalkingGame extends Godot.SingletonBase {

    private Activity activity;

    public void onVirtualCurrencyReward(float virtualCurrencyAmount, String reason) {
        TDGAVirtualCurrency.onReward(virtualCurrencyAmount, reason);
    }

    public void onItemPurchase(String item, int itemNumber, float priceInVirtualCurrency) {
        TDGAItem.onPurchase(item, itemNumber, priceInVirtualCurrency);
    }

    public void onItemUse(String item, int itemNumber) {
        TDGAItem.onUse(item, itemNumber);
    }

    public void onMissionBegin(String missionId) {
        TDGAMission.onBegin(missionId);
    }

    public void onMissionCompleted(String missionId) {
        TDGAMission.onCompleted(missionId);
    }

    public void onMissionFailed(String missionId, String cause) {
        TDGAMission.onFailed(missionId, cause);
    }

    // custom event
    public void onEvent(String eventId, Dictionary eventData) {
        _onEvent(eventId, eventData);
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
        activity = p_activity;
        activity.runOnUiThread(new Runnable() {
            public void run() {
                //boolean enable_debug = GodotLib.getGlobal("talking_game/debug") == "True";
                //TalkingDataGA.setDebugMode(enable_debug);
                //Log.d("godot", "TalkingGame DebugMode: "+enable_debug);
                String app_id = GodotLib.getGlobal("talking_game/app_id");
                String channel = GodotLib.getGlobal("talking_game/channel");
                Log.d("godot", "TalkingGame Init: "+app_id+" "+channel);
                TalkingDataGA.init(activity, app_id, channel);
                TDGAAccount.setAccount(TalkingDataGA.getDeviceId(activity));
            }
        });

    }

    protected void onMainPause() {
        TalkingDataGA.onPause(activity);
    }

    protected void onMainResume() {
        TDGAAccount.setAccount(TalkingDataGA.getDeviceId(activity));
        TalkingDataGA.onResume(activity);
    }
}
