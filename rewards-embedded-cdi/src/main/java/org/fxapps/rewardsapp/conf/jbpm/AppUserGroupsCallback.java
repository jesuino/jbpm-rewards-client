package org.fxapps.rewardsapp.conf.jbpm;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.kie.api.task.UserGroupCallback;

/**
 * 
 * This class provide user and groups callback for human tasks. In a real
 * application you should retrieve user groups callback from a database.
 * 
 * @author wsiqueir
 *
 */
@ApplicationScoped
public class AppUserGroupsCallback implements UserGroupCallback {

	public boolean existsUser(String userId) {
		return true;
	}

	public boolean existsGroup(String groupId) {
		return true;
	}

	@Override
	public List<String> getGroupsForUser(String userId, List<String> groupIds, List<String> allExistingGroupIds) {
		System.out.println("NOW GETTING ROLES");
		return Arrays.asList("HR", "PM", "Administrators");
	}
}