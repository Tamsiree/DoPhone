package com.example.dophone.bean;


/**
 * 应用程序信息的业务bean
 */
public class VirusInfo {

	private int virusId;   //程序图标
	private String name;   //程序名
	private String md5;    //每当
	private String type;  //类别
	public String desc;    //描述
	public int getVirusId() {
		return virusId;
	}
	public void setVirusId(int virusId) {
		this.virusId = virusId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}


	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "AppInfo [virusId=" + virusId + ", name=" + name + ", md5="
				+ md5 +",type="+type+ ", desc=" + desc + "]";
	}

}

