package com.longbei.appservice.common;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import com.longbei.appservice.common.persistence.SFSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 内存方式分配全局唯一ID
 * 
 * @author shuzc 总共四种方式，getNextId，getUniqueIdAsString，getNextId和数据库自增id.
 *         1.用户id用getNextId；如果分表按最后2 ~ 3位分即可
 *         2.订单id用getUniqueIdAsString，最好在这个串的前面添加某个前缀，如果想在中间添加，修改下代码；如果分表按最后2 ~
 *         3位分即可 3.商品id用getNextId，如果分表按最后2 ~ 3位分即可 4.进步id用getNextId，这个应该没有分表需求
 *         5.点赞记录的id，或者一些流水性质的，比如用户操作记录等，这些记录应该只有查询的场景，没有更新等场景；用数据库自增id，
 *         或者getUniqueIdAsLong，这种分表也不会利用主键的
 * @return
 */
@Service
@SuppressWarnings("hiding")
public class IdGenerateService  {
	private static int MAX_TRY_TIMES = 20;
	private static long machineId = calculateMachineId();
	private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final SimpleDateFormat SHORT_DATE_TIME_FORMAT = new SimpleDateFormat("yyMMddHHmmss");
	private static final String MACHINE_ID_FORMAT = "%1d";
	private static final AtomicLong seq = new AtomicLong(0);

	private static long calculateMachineId() {
		try {
			for (NetworkInterface iface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
				for (InetAddress raddr : Collections.list(iface.getInetAddresses())) {
					if (raddr.isSiteLocalAddress() && !raddr.isLoopbackAddress() && !(raddr instanceof Inet6Address)) {
						return getLocalMac(raddr);
					}
				}
			}
		} catch (SocketException e) {
			throw new IllegalStateException("fail to get host ip");
		}

		throw new IllegalStateException("fail to get host ip");
	}
	
	private static long getLocalMac(InetAddress ia) throws SocketException {
		byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		int temp = 0;
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			temp ^= (mac[i] & 0xff);
		}
		// 与时间异或，更有利于排除重复的可能
		return temp ^ (System.currentTimeMillis() % 100);
	}
	
	
	public String getUniqueIdAsString(String prefix) {
		long now = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder(64);

		sb.append(prefix);
		sb.append(DATE_TIME_FORMAT.format(now));
		sb.append(String.format(MACHINE_ID_FORMAT, machineId));
		sb.append(String.format("%05d", seq.getAndIncrement() % 100000));

		return sb.toString();
	}

	public Long getUniqueIdAsLong() {
//		long now = System.currentTimeMillis();
//		StringBuilder sb = new StringBuilder(64);
//
//		sb.append(SHORT_DATE_TIME_FORMAT.format(now));
//		sb.append(String.format(MACHINE_ID_FORMAT, machineId));
//		sb.append(String.format("%03d", seq.getAndIncrement() % 100000));

		return sequ.nextId();
	}

	private static SFSequence sequ = new SFSequence(machineId%32,0);
	
}
