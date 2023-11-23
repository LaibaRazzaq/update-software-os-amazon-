package com.example.appUpdate.activities;

import java.util.Objects;

public class TountPoint {

    private int id;
    private float x;
    private float y;
    private float z;
    private float screenWidth;
    private float screenHeight;

    public TountPoint(int id, float x, float y, float z, float screenWidth, float screenHeight) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(float screenWidth) {
        this.screenWidth = screenWidth;
    }

    public float getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(float screenHeight) {
        this.screenHeight = screenHeight;
    }

    public float getNormalizedX() {
        return x / screenWidth;
    }

    public float getNormalizedY() {
        return y / screenHeight;
    }

    public float getNormalizedZ() {
        return z / 2.0f; // maximum pressure value is 2.0f
    }

    public float getRealX() {
        return x * screenWidth;
    }

    public float getRealY() {
        return y * screenHeight;
    }

    public float getRealZ() {
        return z * 2.0f; // maximum pressure value is 2.0f
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TountPoint that = (TountPoint) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

