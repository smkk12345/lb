package com.longbei.appservice.service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTaskService {
	/**
	 * @Title: ScheduledTask
	 * @Description: Spring boot 定时任务
	 * @auther IngaWu
	 * @currentdate:2017年3月10日
	 */
	@Scheduled(cron="0/5 * * * * ? ")
	public void ScheduledTask(){

		};
}
