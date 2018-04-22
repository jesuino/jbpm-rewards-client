package org.fxapps.rewardsapp.conf.jbpm;

import java.util.Arrays;
import java.util.List;

import org.kie.internal.identity.IdentityProvider;


public class AppIdentityProvider implements IdentityProvider {

	private List<String> roles = Arrays.asList("HR", "PM", "Administrators");

	@Override
	public String getName() {
		return "dummy";
	}

	@Override
	public List<String> getRoles() {
		return roles;
	}

	@Override
	public boolean hasRole(String s) {
		return true;
	}
}
