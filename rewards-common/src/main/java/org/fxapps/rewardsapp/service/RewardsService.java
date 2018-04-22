package org.fxapps.rewardsapp.service;

import java.util.List;

import org.fxapps.rewardsapp.model.RewardTask;

public interface RewardsService {
	
	final String P_EMPLOYEE = "employeeName";
	final String P_RESULT = "result";
	final String T_APPROVAL_VAR = "_approval";
	final String PROCESS_ID = "com.sample.rewards-basic";
	final String DEPLOYMENT_ID = "example:rewards-project:1.0";

	public List<RewardTask> getTasks();

	public void doTask(long taskId, boolean approve);

	public void startRewardProcess(String employeeName);

	public List<String> getAllProcessesSummary();

	public String getEmployeeName(long piid);

	public void clearHistory();
}
