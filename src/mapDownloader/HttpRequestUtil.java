package mapDownloader;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * HTTP请求工具
 * 
 * @author webber_dy
 */
public class HttpRequestUtil {
	/**
	 * 发起post请求并获取结果
	 * 
	 * @param path url
	 * @return
	 */
	public static Detect401 postDownTerrain(String path, File file) {
		URL url = null;
		try {
			System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
			url = new URL(path);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//			httpURLConnection.setRequestProperty("cookie", "CONSENT=YES+DE.zh-CN+V14+BX; 1P_JAR=2021-02-24-01; NID=210=BhF1MWXmfgRpe1LHKRp3pFjRrqQUVMwYdk1wOURHPD1wM4qvjnOpAxy2rwOm8CEUuXzAraTENEiS9vBFPE1MIW52HCk0SDXDfAN5YTGPfvg_WCGGWiftvPmF9P4G6LbazSxaItpcx_7pL6jcbalE5_oS_2OJzEsWjp9aWS5Vu0g");
			httpURLConnection.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			httpURLConnection.setRequestProperty("accept-encoding", "gzip, deflate, br");
			httpURLConnection.setRequestProperty("accept-language", "zh-CN,zh;q=0.9");
			httpURLConnection.setRequestProperty("sec-fetch-dest", "document");
			httpURLConnection.setRequestProperty("sec-fetch-mode", "navigate");
			httpURLConnection.setRequestProperty("sec-fetch-site", "none");
			httpURLConnection.setRequestProperty("sec-fetch-user", "?1");
			httpURLConnection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36 QIHU 360EE");
			httpURLConnection.setRequestProperty("upgrade-insecure-requests", "1");
//			httpURLConnection.setRequestProperty("x-client-data", "CIa2yQEIorbJAQjBtskBCKmdygEIlqzKAQjHw8oBCPjHygEIpM3KAQjc1coBCN6IywEIp5zLAQjGnMsBCNWcywEI5JzLAQioncsB\r\n"
//					+ "Decoded:\r\n"
//					+ "message ClientVariations {\r\n"
//					+ "  // Active client experiment variation IDs.\r\n"
//					+ "  repeated int32 variation_id = [3300102, 3300130, 3300161, 3313321, 3315222, 3318215, 3318776, 3319460, 3320540, 3327070, 3329575, 3329606, 3329621, 3329636, 3329704];\r\n"
//					+ "}");
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setConnectTimeout(0);
			httpURLConnection.setReadTimeout(0);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			
			if (httpURLConnection.getResponseCode() == 401) {
				return new Detect401(401);
			}
			if (httpURLConnection.getResponseCode() == 503) {
				return new Detect401(503);
			}
			if (httpURLConnection.getResponseCode() == 500) {
				return new Detect401(500);
			}
			BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
			FileOutputStream bos = new FileOutputStream(file);
			int len;
			byte[] arr = new byte[1024];
			while ((len = bis.read(arr)) != -1) {
				bos.write(arr, 0, len);
				bos.flush();
			}
			bos.close();
			return new Detect401();
		} catch (Exception e) {
			if (e.getMessage().equals("Read timed out") || e.getMessage().equals("Connection reset")
					|| e.getMessage().equals("connect timed out")) {
				return new Detect401(300);
			} else if (e.getMessage().equals("assets.cesium.com")) {
				return new Detect401(401);
			} else {
				System.err.println(e.toString());
				// e.printStackTrace();
			}
		}
		return null;
	}

	public static byte[] getAT(String path) {
		URL url = null;
		try {
			url = new URL(path);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setConnectTimeout(2500);
			httpURLConnection.setReadTimeout(2500);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len;
			byte[] arr = new byte[1024];
			while ((len = bis.read(arr)) != -1) {
				bos.write(arr, 0, len);
				bos.flush();
			}
			bos.close();
			return bos.toByteArray();
		} catch (Exception e) {
			if (e.getMessage().equals("Read timed out") || (e.getMessage().equals("connect timed out"))) {
				return null;
			} else {
				System.err.println(e.toString());
				// e.printStackTrace();
			}
		}
		return null;
	}

}
