package org.fxapps.rewardsapp;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.fxapps.rewardsapp.bc.RewardsAppView;
import org.fxapps.rewardsapp.conf.cdi.StartupScene;
import org.fxapps.rewardsapp.service.RewardsService;

import javafx.stage.Stage;

public class RewardsCDIEmbeddedApp {

	@Inject
	RewardsService rewardsService;
	
	public void start(@Observes @StartupScene Stage stage) {
		new RewardsAppView(rewardsService, stage);
	}

}