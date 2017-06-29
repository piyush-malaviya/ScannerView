package com.pcm.scannerview;

import android.os.Parcel;
import android.os.Parcelable;

public class RadarObject implements Parcelable {
    public static final Creator<RadarObject> CREATOR = new Creator<RadarObject>() {
        @Override
        public RadarObject createFromParcel(Parcel in) {
            return new RadarObject(in);
        }

        @Override
        public RadarObject[] newArray(int size) {
            return new RadarObject[size];
        }
    };
    private String name = "", desc = "";
    private float distance, angle;

    public RadarObject() {
    }

    public RadarObject(String name, float distance, float angle) {
        this.name = name;
        this.distance = distance;
        this.angle = angle;
    }

    protected RadarObject(Parcel in) {
        name = in.readString();
        desc = in.readString();
        distance = in.readFloat();
        angle = in.readFloat();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "RadarObject{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", distance=" + distance +
                ", angle=" + angle +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeFloat(distance);
        dest.writeFloat(angle);
    }
}
