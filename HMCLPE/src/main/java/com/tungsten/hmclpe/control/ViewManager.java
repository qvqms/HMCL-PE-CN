package com.tungsten.hmclpe.control;

import static android.content.Context.SENSOR_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.core.view.GravityCompat;

import com.tungsten.hmclpe.control.bean.BaseButtonInfo;
import com.tungsten.hmclpe.control.view.BaseButton;
import com.tungsten.hmclpe.control.view.LayoutPanel;
import com.tungsten.hmclpe.control.view.MenuFloat;
import com.tungsten.hmclpe.control.view.MenuView;
import com.tungsten.hmclpe.control.view.TouchPad;
import com.tungsten.hmclpe.launcher.setting.game.GameMenuSetting;

public class ViewManager implements SensorEventListener {

    public Context context;
    public Activity activity;
    public MenuHelper menuHelper;
    public LayoutPanel layoutPanel;
    public int launcher;
    public int screenWidth;
    public int screenHeight;

    public int gameCursorMode = 0;
    public TouchPad touchPad;

    public MenuFloat menuFloat;
    public MenuView menuView;

    public SensorManager sensorManager;
    public Sensor sensor;
    public static final float NS2S = 1.0f / 1000000000.0f;
    public float timestamp;
    public float[] angle = new float[3];

    public float pointerX;
    public float pointerY;
    public float currentX;
    public float currentY;

    public int viewMovingType = 0;

    public ViewManager (Context context, Activity activity, MenuHelper menuHelper, LayoutPanel layoutPanel,int launcher) {
        this.context = context;
        this.activity = activity;
        this.menuHelper = menuHelper;
        this.layoutPanel = layoutPanel;
        this.launcher = launcher;

        screenWidth = layoutPanel.getWidth();
        screenHeight = layoutPanel.getHeight();
        init();
    }

    private void init(){
        /*
         *初始化触控板和陀螺仪
         */
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        setSensorEnable(menuHelper.gameMenuSetting.enableSensor);
        touchPad = new TouchPad(context,launcher,layoutPanel.getWidth(),layoutPanel.getHeight(),menuHelper);
        layoutPanel.addView(touchPad);
        /*
         *初始化菜单键
         */
        menuFloat = new MenuFloat(context,layoutPanel.getWidth(),layoutPanel.getHeight(),menuHelper.gameMenuSetting.menuFloatSetting.positionX,menuHelper.gameMenuSetting.menuFloatSetting.positionY);
        menuFloat.addCallback(new MenuFloat.MenuFloatCallback() {
            @Override
            public void onClick() {
                menuHelper.drawerLayout.openDrawer(GravityCompat.END,true);
                menuHelper.drawerLayout.openDrawer(GravityCompat.START,true);
            }

            @Override
            public void onMove(float xPosition, float yPosition) {
                menuHelper.gameMenuSetting.menuFloatSetting.positionX = xPosition;
                menuHelper.gameMenuSetting.menuFloatSetting.positionY = yPosition;
                GameMenuSetting.saveGameMenuSetting(menuHelper.gameMenuSetting);
            }
        });
        menuView = new MenuView(context,layoutPanel.getWidth(),layoutPanel.getHeight(),menuHelper.gameMenuSetting.menuViewSetting.mode,menuHelper.gameMenuSetting.menuViewSetting.yPercent);
        menuView.addCallback(new MenuView.MenuCallback() {
            @Override
            public void onRelease() {
                menuHelper.drawerLayout.openDrawer(GravityCompat.END,true);
                menuHelper.drawerLayout.openDrawer(GravityCompat.START,true);
            }

            @Override
            public void onMoveModeStart() {

            }

            @Override
            public void onMove(int mode, float yPercent) {
                menuHelper.gameMenuSetting.menuViewSetting.mode = mode;
                menuHelper.gameMenuSetting.menuViewSetting.yPercent = yPercent;
                GameMenuSetting.saveGameMenuSetting(menuHelper.gameMenuSetting);
            }

            @Override
            public void onMoveModeStop() {

            }
        });
        if (menuHelper.gameMenuSetting.menuFloatSetting.enable){
            layoutPanel.addView(menuFloat);
        }
        if (menuHelper.gameMenuSetting.menuViewSetting.enable){
            layoutPanel.addView(menuView);
        }
    }

    public void addButton (BaseButtonInfo baseButtonInfo) {
        BaseButton baseButton = new BaseButton(context,screenWidth,screenHeight,baseButtonInfo,menuHelper);
        layoutPanel.addView(baseButton);
        baseButton.updateSizeAndPosition(baseButtonInfo);
    }

    public void enableCursor(){
        gameCursorMode = 0;
        /*
        if (touchPad != null){
            InputBridge.setPointer(launcher,(int) (touchPad.cursorX * menuHelper.scaleFactor),(int) (touchPad.cursorY * menuHelper.scaleFactor));
        }

         */
    }

    public void disableCursor(){
        gameCursorMode = 1;
        /*
        if (!menuHelper.gameMenuSetting.enableSensor){
            InputBridge.setPointer(launcher,(int) pointerX,(int) pointerY);
        }

         */
    }

    public void setSensorEnable(boolean enable){
        if (enable) {
            sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_FASTEST);
        }
        else {
            sensorManager.unregisterListener(this);
        }
    }

    public void setGamePointer(int type,float deltaX,float deltaY){
        if (viewMovingType == 0 || viewMovingType == type || type == 0){
            if (!menuHelper.gameMenuSetting.enableSensor){
                InputBridge.setPointer(launcher,(int) (pointerX + deltaX * menuHelper.gameMenuSetting.mouseSpeed),(int) (pointerY + deltaY * menuHelper.gameMenuSetting.mouseSpeed));
            }
            currentX = pointerX + deltaX * menuHelper.gameMenuSetting.mouseSpeed;
            currentY = pointerY + deltaY * menuHelper.gameMenuSetting.mouseSpeed;
            if (type == 0){
                pointerX = pointerX + deltaX * menuHelper.gameMenuSetting.mouseSpeed;
                pointerY = pointerY + deltaY * menuHelper.gameMenuSetting.mouseSpeed;
            }
            viewMovingType = type;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (gameCursorMode == 1){
            if (timestamp != 0){
                final float dT = (sensorEvent.timestamp - timestamp) * NS2S;
                angle[0] += sensorEvent.values[0] * dT;
                angle[1] += sensorEvent.values[1] * dT;
                float angleX = (float) Math.toDegrees(angle[0]);
                float angleY = (float) Math.toDegrees(angle[1]);
                InputBridge.setPointer(launcher,(int) (currentX + angleX * menuHelper.gameMenuSetting.sensitivity),(int) (currentY + angleY * menuHelper.gameMenuSetting.sensitivity));
            }
            timestamp = sensorEvent.timestamp;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}