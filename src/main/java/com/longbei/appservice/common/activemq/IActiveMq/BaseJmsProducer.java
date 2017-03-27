package com.longbei.appservice.common.activemq.IActiveMq;


import java.util.List;

/**
 * Created by yinxiong on 2016/6/30.
 */
public interface BaseJmsProducer {

	/**
	 * 发送格式: [type]:[domainName]:[domainIds]
	 * @param action
	 * @param domainName
	 * @param ids
	 */
	public void sendMsg(final String action, final String domainName, final String ids);

	/**
	 * 发送格式:  [type]:[domainName]:[domainIds]
	 * @param action
	 * @param domainName
	 * @param idList
	 */
	public void sendMsg(final String action, final String domainName, final List<String> idList);

	public void sendMsg(final String action, final Class<?> claszz, final String ids);


	public void sendMsg(final String action, final Class<?> claszz, final List<String> idList);

}
