package com.example.administrator.hearinghelp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Cache
{
	static private Date lastTime=null;
	static private String token=null;

	public static void main(String[] args)
	{
		Cache cache=new Cache();
		String token="abcd";
		String newtoken=null;
		try
		{
			if(cache.ifTimeNull())
				cache.updateAll(token);
			else
			if(cache.ifTimeOld())
				cache.updateAll(token);
			else
				newtoken=cache.getToken();


		} catch (Exception e)
		{
			System.err.println("Cache update error");
		}
	}




	public String getLastTime()
	{
		return lastTime.toString();
	}

	public String getToken()
	{
		return token;
	}

	public void setTimeNull()
	{
		lastTime=null;
	}

	public boolean ifTimeNull()
	{
		if(lastTime==null)
			return true;
		else
			return false;
	}

	public boolean ifTimeOld() throws Exception
	{
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm");
		Date timeNow=simpleDateFormat.parse(simpleDateFormat.format(new Date()));

		long from1 = lastTime.getTime();
		long to1 = timeNow.getTime();
		int days = (int) ((to1 - from1) / (1000 * 60 * 60 * 24));
		if(days>=20)
			return true;
		else
			return false;
	}

	public void updateAll(String newToken) throws Exception
	{
		updateTime();
		updateToken(newToken);
	}

	public void updateTime() throws Exception
	{
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm");
		lastTime=simpleDateFormat.parse(simpleDateFormat.format(new Date()));
	}

	public void updateToken(String newToken)
	{
		token=newToken;
	}
}
