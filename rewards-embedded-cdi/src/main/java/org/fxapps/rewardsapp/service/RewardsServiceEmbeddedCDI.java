package org.fxapps.rewardsapp.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.fxapps.rewardsapp.model.RewardTask;
import org.jbpm.kie.services.impl.KModuleDeploymentUnit;
import org.jbpm.services.api.DeploymentService;
import org.jbpm.services.api.ProcessService;
import org.jbpm.services.api.RuntimeDataService;
import org.jbpm.services.api.UserTaskService;
import org.jbpm.services.api.model.DeploymentUnit;
import org.jbpm.services.api.model.ProcessInstanceDesc;
import org.jbpm.services.api.model.VariableDesc;
import org.jbpm.services.cdi.Kjar;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.query.QueryContext;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.query.QueryFilter;

@ApplicationScoped
public class RewardsServiceEmbeddedCDI implements RewardsService {

	@Kjar
	@Inject
	private DeploymentService deploymentService;

	@Inject
	private ProcessService processService;

	@Inject
	private UserTaskService taskService;
	
	@Inject
	private RuntimeDataService dataService;
	
	@PostConstruct
	private void doDeploy() {
		String [] gavParts = DEPLOYMENT_ID.split(":");
		String g = gavParts[0];
		String a = gavParts[1];
		String v = gavParts[2];
		DeploymentUnit unit = new KModuleDeploymentUnit(g, a ,v);
		deploymentService.deploy(unit);
	}
	

	@Override
	public List<RewardTask> getTasks() {
		return dataService.getTasksAssignedAsBusinessAdministrator("Administrator", new QueryFilter()).stream().map(this::toRewardTask).collect(Collectors.toList());
	}

	@Override
	public void doTask(long taskId, boolean approve) {
		Map<String, Object> params = new HashMap<>();
		params.put(T_APPROVAL_VAR, approve);
		taskService.claim(taskId, "dummy");
		taskService.start(taskId, "dummy");
		taskService.complete(taskId, "dummy", params);
	}

	@Override
	public void startRewardProcess(String employeeName) {
		Map<String, Object> params = new HashMap<>();
		params.put(P_EMPLOYEE, employeeName);
		processService.startProcess(DEPLOYMENT_ID, PROCESS_ID, params);
	}

	@Override
	public List<String> getAllProcessesSummary() {
		return dataService.getProcessInstancesByProcessDefinition(PROCESS_ID, null).stream().map(this::toSummary).collect(Collectors.toList());
	}

	@Override
	public String getEmployeeName(long piid) {
		return getVariable(piid, P_EMPLOYEE);
	}


	private String getVariable(long piid, String vid) {
		Collection<VariableDesc> v = dataService.getVariableHistory(piid, vid, new QueryContext());
		String vv = null;
		Iterator<VariableDesc> iterator = v.iterator();
		while(iterator.hasNext()) vv = iterator.next().getNewValue();
		return vv;
	}

	@Override
	public void clearHistory() {
	}
	
	private RewardTask toRewardTask(TaskSummary t) {
		RewardTask rt = new RewardTask();
		rt.setCreated(t.getCreatedOn());
		rt.setEmployeeName(getEmployeeName(t.getProcessInstanceId()));
		rt.setName(t.getName());
		rt.setTaskId(t.getId());
		return rt;
	}
	
	private String toSummary(ProcessInstanceDesc pi) {
		long piid = pi.getId();
		String employee = getEmployeeName(piid);
		String result = getVariable(piid, P_RESULT);
		String status = "";
		switch (pi.getState()) {
		case ProcessInstance.STATE_ABORTED:
			status = "aborted";
			break;
		case ProcessInstance.STATE_ACTIVE:
			status = "active";
			break;
		case ProcessInstance.STATE_COMPLETED:
			status = "completed";
			break;
		case ProcessInstance.STATE_PENDING:
			status = "pending";
			break;
		case ProcessInstance.STATE_SUSPENDED:
			status = "suspended";
			break;
		default:
			status = "unknown";
			break;
		}
		if (Objects.isNull(result))
			result = "reward still waiting for approval";
		String summary = "Reward process for employee '%s' is %s and result is '%s'.";
		return String.format(summary, employee, status, result);
	}

}
