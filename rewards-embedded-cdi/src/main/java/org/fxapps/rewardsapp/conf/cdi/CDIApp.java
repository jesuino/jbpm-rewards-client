package org.fxapps.rewardsapp.conf.cdi;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.enterprise.util.AnnotationLiteral;

import org.fxapps.rewardsapp.conf.jbpm.DatasourceSetup;

import javafx.application.Application;
import javafx.stage.Stage;

public class CDIApp extends Application {
	static DatasourceSetup datasourceSetup = new DatasourceSetup();

	public static void main(String[] args) {
		datasourceSetup.buildMySQLDatasource();
		launch(args);
		datasourceSetup.closeDataSource();
	}

	@Override
	@SuppressWarnings("serial")
	public void start(Stage primaryStage) throws Exception {
		SeContainerInitializer initializer = SeContainerInitializer.newInstance();
		final SeContainer container = initializer.initialize();
		container.getBeanManager().fireEvent(primaryStage, new AnnotationLiteral<StartupScene>() {});
	}

}