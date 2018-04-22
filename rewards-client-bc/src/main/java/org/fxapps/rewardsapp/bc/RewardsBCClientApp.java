package org.fxapps.rewardsapp.bc;

import javafx.application.Application;
import javafx.stage.Stage;

public class RewardsBCClientApp extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		new RewardsAppView(RewardsServiceBC.getInstance(), stage);
	}
}
