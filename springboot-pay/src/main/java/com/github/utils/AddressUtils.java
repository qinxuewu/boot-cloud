package com.github.utils;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;



 /**
  * 功能描述:  根据IP地址获取详细的地域信息
  * @author: qinxuewu
  * @date: 2019/9/9 15:29
  * @since 1.0.0
  */
public class AddressUtils { 


	/**
	 * 功能描述:   请求的参数 格式为：name=xxx&pwd=xxx
	 * @author: qinxuewu
	 * @date: 2019/9/9 15:30
	 * @since 1.0.0
	 */
	public static String getAddresses(String ip) throws UnsupportedEncodingException {
		String urlStr ="http://ip.taobao.com/service/getIpInfo.php";
		String returnStr = getResult(urlStr, ip);
		if (returnStr != null) {
			// 处理返回的省市区信息
			String[] temp = returnStr.split(",");
			if (temp.length < 3) {
				// 无效IP，局域网测试
				return "0";
			}
			String region = (temp[5].split(":"))[1].replaceAll("\"", "");
			// 省份
			region = decodeUnicode(region);
			String country = "";
			String area = "";
			// String region = "";
			String city = "";
			String county = "";
			String isp = "";
			for (int i = 0; i < temp.length; i++) {
				switch (i) {
				case 1:
					country = (temp[i].split(":"))[2].replaceAll("\"", "");
					country = decodeUnicode(country);
					break;
				case 3:
					area = (temp[i].split(":"))[1].replaceAll("\"", "");
					area = decodeUnicode(area);
					break;
				case 5:
					region = (temp[i].split(":"))[1].replaceAll("\"", "");
					region = decodeUnicode(region);
					break;
				case 7:
					city = (temp[i].split(":"))[1].replaceAll("\"", "");
					city = decodeUnicode(city);
					break;
				case 9:
					county = (temp[i].split(":"))[1].replaceAll("\"", "");
					county = decodeUnicode(county);
					break;
				case 11:
					isp = (temp[i].split(":"))[1].replaceAll("\"", "");
					isp = decodeUnicode(isp);
					break;
				}
			}
			String address = region+city;
			if(StringUtils.isBlank(address)){
				address = "地球村";
			}
			return address;
		}
		return null;
	}

	/**
	 * @param urlStr 请求的参数 格式为：name=xxx&pwd=xxx
	 * @param ip
	 * @return
	 */
	private static String getResult(String urlStr, String ip) {
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			/**
			 * 超时错误 由 2s改为5s
			 */
			// 设置连接超时时间，单位毫秒
			connection.setConnectTimeout(5000);
			// 设置读取数据超时时间，单位毫秒
			connection.setReadTimeout(5000);
			// 是否打开输出流 true|false
			connection.setDoOutput(true);
			// 是否打开输入流true|false
			connection.setDoInput(true);
			// 提交方法POST|GET
			connection.setRequestMethod("POST");
			// 是否缓存true|false
			connection.setUseCaches(false);
			// 打开连接端口
			connection.connect();
			// 打开输出流往对端服务器写数据
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
			out.writeBytes("ip="+ip);
			out.flush();// 刷新
			out.close();// 关闭输出流
			// 往对端写完数据对端服务器返回数据
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
			// ,以BufferedReader流来读取
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();
			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();// 关闭连接
			}
		}
		return null;
	}

	/**
	 * unicode 转换成 中文
	 * @param theString
	 * @return
	 */
	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed      encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't') {
						aChar = '\t';
					} else if (aChar == 'r') {
						aChar = '\r';
					} else if (aChar == 'n') {
						aChar = '\n';
					} else if (aChar == 'f') {
						aChar = '\f';
					}
					outBuffer.append(aChar);
				}
			} else {
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}
	/**
	 * 获取IP地址
	 * @Author  科帮网
	 * @param request
	 * @return  String
	 * @Date	2017年7月31日
	 * 更新日志
	 * 2017年7月31日  科帮网 首次创建
	 *
	 */
    public static String getIpAddr(HttpServletRequest request)
    {
        String ip = request.getHeader("X-Real-IP");
        if(!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
        ip = request.getHeader("X-Forwarded-For");
        if(!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip))
        {
            int index = ip.indexOf(',');
            if(index != -1) {
				return ip.substring(0, index);
			}else {
				return ip;
			}
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("Proxy-Client-IP");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("WL-Proxy-Client-IP");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("HTTP_CLIENT_IP");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getRemoteAddr();
        return ip;
    }
}  
