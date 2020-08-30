package com.example.administrator.hearinghelp.bean;

import com.example.administrator.hearinghelp.R;

import java.util.ArrayList;

public class GoodsInfo {
	public int pic_id;
	public String title;
	public String desc;
	public boolean bPressed;
	public int id;
	private static int seq = 0;
	
	public GoodsInfo(int pic_id, String title, String desc) {
		this.pic_id = pic_id;
		this.title = title;
		this.desc = desc;
		this.bPressed = false;
		this.id = this.seq;
		this.seq++;
	}

	private static int[] listImageArray = {R.drawable.count, R.drawable.network
			, R.drawable.cat, R.drawable.car, R.drawable.door};
	private static String[] listTitleArray = {
			"无法识别", "网络错误", "猫", "车", "敲门"};
	private static String[] listDescArray = {
			"1",
			"2",
			"3",
			"4",
			"5"};
	public static ArrayList<GoodsInfo> getDefaultList() {
		ArrayList<GoodsInfo> listArray = new ArrayList<GoodsInfo>();
		for (int i=0; i<listImageArray.length; i++) {
			listArray.add(new GoodsInfo(listImageArray[i], listTitleArray[i], listDescArray[i]));
		}
		return listArray;
	}//listImageArray.length
	public static ArrayList<GoodsInfo> getDefaultList1() {
		ArrayList<GoodsInfo> listArray = new ArrayList<GoodsInfo>();
		/*for (int i=0; i<listImageArray.length; i++) {
			listArray.add(new GoodsInfo(listImageArray[i], listTitleArray[i], listDescArray[i]));
		}*/
		return listArray;
	}//listImageArray.length

}
