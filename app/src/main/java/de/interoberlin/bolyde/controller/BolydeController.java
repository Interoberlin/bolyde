package de.interoberlin.bolyde.controller;

import android.app.Application;
import android.content.Context;

import de.interoberlin.bolyde.controller.accelerometer.AcceleratorListener;

/**
 * Main controller
 */
public class BolydeController extends Application {
    private Context context;

    private boolean running = true;

    private final float MAX_VALUE = 10.0f;
    private final float MIN_VALUE = -10.0f;

    private float MAX_FACTOR = 5.0f;
    private float MIN_FACTOR = 1.0f;

    private int canvasHeight;
    private int canvasWidth;

    private int fps;
    private int currentFps;

    private float offsetX = 0F;
    private float offsetY = 0F;

    // Display
    private int circleCount = 4;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public int getMinDimension() {
        return canvasHeight > canvasWidth ? canvasWidth : canvasHeight;
    }

    public Context getContext() {
        return context;
    }

    public int getCanvasHeight() {
        return canvasHeight;
    }

    public void setCanvasHeight(int canvasHeight) {
        this.canvasHeight = canvasHeight;
    }

    public int getCanvasWidth() {
        return canvasWidth;
    }

    public void setCanvasWidth(int canvasWidth) {
        this.canvasWidth = canvasWidth;
    }

    public void start() {
        running = true;
    }

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public float getAccelerometerX() {
        return AcceleratorListener.getDataX();
    }

    public float getAccelerometerY() {
        return AcceleratorListener.getDataY();
    }

    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public int getCurrentFps() {
        return currentFps;
    }

    public void setCurrentFps(int currentFps) {
        this.currentFps = currentFps;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public int getCircleLineWidth() {
        return getCanvasWidth() / 72;
    }

    public int getPointRadius() {
        return getCanvasWidth() / 180;
    }

    public int getCircleCount() {
        return circleCount;
    }

    public void setCircleCount(int circleCount) {
        this.circleCount = circleCount;
    }

    public float getMAX_VALUE() {
        return MAX_VALUE;
    }

    public float getMIN_VALUE() {
        return MIN_VALUE;
    }

    public float getMAX_FACTOR() {
        return MAX_FACTOR;
    }

    public void setMAX_FACTOR(float mAX_FACTOR) {
        MAX_FACTOR = mAX_FACTOR;
    }

    public float getMIN_FACTOR() {
        return MIN_FACTOR;
    }

    public void setMIN_FACTOR(float mIN_FACTOR) {
        MIN_FACTOR = mIN_FACTOR;
    }
}