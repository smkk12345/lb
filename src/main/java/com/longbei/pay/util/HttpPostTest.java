package com.longbei.pay.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@SuppressWarnings({"unused"})
public class HttpPostTest {
	private static StringBuffer sb;

	void testPost(String urlStr) {
		try {
			URL url = new URL(urlStr);
			URLConnection con = url.openConnection();
			con.setDoOutput(true);
			con.setRequestProperty("Pragma:", "no-cache");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Content-Type", "text/xml");

			OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
			// String xmlInfo = getXmlInfo();
			String xmlInfo = "<xml><appid>wx0485889085055127</appid><body><![CDATA[苹果6]]></body><mch_id>10028787</mch_id><nonce_str>5e7d00134ba3a8b3e37edf5038bc51fc</nonce_str><notify_url>http://wstssb.com/weixinpaynew/payNotifyUrl.jsp</notify_url><openid>oxoJZuPbFbaYiqJ1eedZ2kdP1-mU</openid><out_trade_no>1055135431</out_trade_no><sign><![CDATA[4841B6B436F50F30B5743EC76FFA97E0]]></sign><spbill_create_ip>211.103.191.243</spbill_create_ip><total_fee>1</total_fee><trade_type>JSAPI</trade_type></xml>";
			System.out.println("urlStr=" + urlStr);
			System.out.println("xmlInfo=" + xmlInfo);
			out.write(new String(xmlInfo.getBytes("UTF-8")));
			out.flush();
			out.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = "";
			for (line = br.readLine(); line != null; line = br.readLine()) {
				System.out.println(line);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getXmlInfo() {
		StringBuilder sb = new StringBuilder();
		// sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		sb.append("<xml>");
		sb.append("    <OpenId><![CDATA[oUX3rjrfRgK9ECifL3wDxrrt6ue8]]></OpenId>");
		sb.append("        <AppId><![CDATA[wxe6d5b67d430ae996]]></AppId>");
		sb.append("        <TimeStamp>1401206434</TimeStamp>");
		sb.append("    <MsgType><![CDATA[request]]></MsgType>");
		sb.append("    <FeedBackId>13275936403980775178</FeedBackId>");
		sb.append("        <TransId><![CDATA[2014050500001]]></TransId>");
		sb.append("           <Reason><![CDATA[质量问题]]></Reason>");
		sb.append("        <Solution><![CDATA[换货吧]]></Solution>");
		sb.append("        <AppSignature><![CDATA[1f4a626f59f9ae8007158b0a9510e88db56fa80b]]></AppSignature>");
		sb.append("    <SignMethod><![CDATA[sha1]]></SignMethod>");
		sb.append("</xml>");
		return sb.toString();
	}

	public static void main(String[] args) {
		String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		new HttpPostTest().testPost(url);
	}
}
