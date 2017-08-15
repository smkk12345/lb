package com.longbei.appservice.common.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 
 * @ClassName: OSSService
 * @Description: oss 相关操作 上传图片，下载图片等
 * @author luye
 * @date 2016年4月20日 下午3:20:08
 */

@Service("oSSService")
public class OSSService {
	/**
	 * 对oss云对象服务器操作客户端
	 */
	@Autowired
	private OSSClient ossClient;

	/**
	 * 
	 * @Title: createBucket @Description: 创建bucket @param @param
	 * bucketName @param @return @return boolean 返回值类型 @throws
	 */
	public boolean createBucket(String bucketName) {
		Bucket bucket = ossClient.createBucket(bucketName);
		if (bucket == null) {
			return false;
		}
		return true;

	}

	/**
	 * 
	 * @Title: putObject @Description: 上传图片到OSS @param @param
	 *         bucketName @param @param key @param @param
	 *         in @param @return @return String 返回值类型 @throws
	 */
	public String putObject(String bucketName, String key, InputStream in) {
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentType("image/jpeg");
		PutObjectResult putobj = ossClient.putObject(bucketName, key, in, meta);

		putobj.getETag();
		return null;
	}
	/**
	 * 
	 * @param bucketName
	 * @param key
	 * @return
	 */
	public String putFile(String bucketName, String key, MultipartFile file) {
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentLength(file.getSize());
		PutObjectResult putobj;
		try {
			putobj = ossClient.putObject(bucketName, key, file.getInputStream(), meta);
			putobj.getETag();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * @Title: getObject @Description: 获取OSS上的图片 @param @param
	 *         bucketName @param @param key @param @return @return String
	 *         返回值类型 @throws
	 */
	public String getObject(String bucketName, String key) {
		StringBuffer result = new StringBuffer();
		OSSObject object = ossClient.getObject(new GetObjectRequest(bucketName, key));
		InputStream content = object.getObjectContent();
		if (content != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
			while (true) {
				String line = null;
				try {
					line = reader.readLine();
					result.append(line);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (line == null) {
					break;
				}
			}
			try {
				content.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.toString();
	}

	/**
	 * 
	 * @Title: deleteObject @Description: 删除OSS上的图片 @param @param
	 *         bucketName @param @param key @param @return @return String
	 *         返回值类型 @throws
	 */
	public void deleteObject(String bucketName, String key) {
		ossClient.deleteObject(bucketName, key);
	}

	/**
	 * 
	 * @Title: getObjectByKeyword @Description: 对OSS上的图片，进行检索病获得结果（如;
	 *         获取以A开头的所有的key，所对应的图片） @param @param bucketName @param @param
	 *         keyword @param @return @return String 返回值类型 @throws
	 */
	public String getObjectByKeyword(String bucketName, String keyword) {
		ObjectListing objectListing = ossClient.listObjects(new ListObjectsRequest(bucketName).withPrefix(keyword));

		for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
			OSSObject object = ossClient.getObject(new GetObjectRequest(bucketName, objectSummary.getKey()));
			InputStream content = object.getObjectContent();
			if (content != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				while (true) {
					String line = null;
					try {
						line = reader.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (line == null) {
						break;
					}
				}
				try {
					content.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}


}
