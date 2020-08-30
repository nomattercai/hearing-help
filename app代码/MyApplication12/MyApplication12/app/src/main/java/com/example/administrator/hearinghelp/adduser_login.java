package com.example.administrator.hearinghelp;

import android.text.Editable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;

public class adduser_login {
	
	private static final String url = "106.13.37.133";
	private static final int port=8999;
	
	public static void main(String[] args) {
		String username="nuoni";
		String password="123456";
		//adduser(username, password);
		//loadin(username, password);
		//setlevel(username, 3);
		//System.out.println(adduser(username, password));
		//System.out.println(login(username, password));
		//System.out.println(setlevel(username, 3));
	}
	
	public static String login(String username, String password)
	{
		try
		{
			Socket socket=new Socket(url,port);
			//System.out.println("Client run");
			
			OutputStream os=socket.getOutputStream();
			String msg=username+" "+getMD5String(password)+" loadin\n";
			sendMsg(os,msg);
			
			String resp=new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
			os.close();
			socket.close();
			return resp;
			
		} catch (Exception e)
		{
			// TODO: handle exception
			return "loadin error";
		}
	}

	public static String setlevel(String username,int left_level,int right_level)
	{
		try
		{
			Socket socket=new Socket(url,port);
			//System.out.println("Client run");

			OutputStream os=socket.getOutputStream();
			String msg=username+" "+left_level+";"+right_level+" setlevel\n";
			sendMsg(os,msg);

			String resp=new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
			os.close();
			socket.close();
			return resp;

		} catch (Exception e)
		{
			// TODO: handle exception
			return "setlevel error";
		}
	}
	public static String getlevel(String username)
	{
		try
		{
			Socket socket=new Socket(url,port);
			//System.out.println("Client run");

			OutputStream os=socket.getOutputStream();
			String msg=username+" 0 getlevel\n";
			sendMsg(os,msg);

			String resp=new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
			socket.close();
			return resp;

		} catch (Exception e)
		{
			// TODO: handle exception
			return "getlevel error";
		}
	}

	public static String adduser(String username,String password)
	{
		try
		{
			Socket socket=new Socket(url,port);
			//System.out.println("Client run");
			
			OutputStream os=socket.getOutputStream();
			String msg=username+" "+getMD5String(password)+" adduser\n";
			sendMsg(os,msg);
			
			String resp=new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
			os.close();
			socket.close();
			return resp;
			
		} catch (Exception e)
		{
			// TODO: handle exception
			return "adduser error";
		}
	}

	public static void sendMsg(OutputStream os, String s) throws IOException {
		 
		// �����Ϣ
		byte[] bytes = s.getBytes();
		os.write(bytes);
		os.flush();
		}

	public static String getMD5String(String str) {
        try {
            // ����һ��MD5���ܼ���ժҪ
            MessageDigest md = MessageDigest.getInstance("MD5");
            // ����md5����
            md.update(str.getBytes());
            // digest()���ȷ������md5 hashֵ������ֵΪ8λ�ַ�������Ϊmd5 hashֵ��16λ��hexֵ��ʵ���Ͼ���8λ���ַ�
            // BigInteger������8λ���ַ���ת����16λhexֵ�����ַ�������ʾ���õ��ַ�����ʽ��hashֵ
            //һ��byte�ǰ�λ�����ƣ�Ҳ����2λʮ�������ַ���2��8�η�����16��2�η���
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
           e.printStackTrace();
           return null;
        }
    }
}

