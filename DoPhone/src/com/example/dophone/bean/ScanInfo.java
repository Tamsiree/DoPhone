package com.example.dophone.bean;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Administrator
 *   扫描 内部类
 */
public class ScanInfo implements Parcelable {
	//Drawable  icon;
	String packname;
	String name;
	int isVirus;
	public String getPackname() {
		return packname;
	}
	public void setPackname(String packname) {
		this.packname = packname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int isVirus() {
		return isVirus;
	}
	public void setVirus(int isVirus) {
		this.isVirus = isVirus;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(name);
		dest.writeString(packname);
		dest.writeInt(isVirus);
	}

	public static final Parcelable.Creator<ScanInfo> CREATOR = new Creator<ScanInfo>() {
		public ScanInfo createFromParcel(Parcel source) {
			ScanInfo scaninfo = new ScanInfo();
			scaninfo.name = source.readString();
			scaninfo.packname = source.readString();
			scaninfo.isVirus = source.readInt();
			return scaninfo;
		}
		public ScanInfo[] newArray(int size) {
			return new ScanInfo[size];
		}
	};



}

