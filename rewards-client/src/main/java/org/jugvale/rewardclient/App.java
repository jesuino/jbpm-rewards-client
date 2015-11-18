package org.jugvale.rewardclient;

import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class App extends Application {

	static RewardService service;

	public static void main(String[] args) throws Exception {
		service = RewardService.getInstance();
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Accordion accordionActions = new Accordion(giveReward(), tasks(),
				history());
		Label lblTitle = new Label("Rewards APP");
		lblTitle.setFont(Font.font(25));
		Scene scene = new Scene(new VBox(lblTitle, accordionActions), 700, 600);
		stage.setScene(scene);
		stage.setTitle("Rewards App!");
		stage.show();
	}

	private TitledPane giveReward() {
		TextField txtEmployeeName = new TextField();
		Button btnStartRewardsProcess = new Button("Start RewardProcess");
		HBox hbGiveReward = new HBox(new Label("Colleague name"),
				txtEmployeeName, btnStartRewardsProcess);
		hbGiveReward.setSpacing(15);
		btnStartRewardsProcess.setOnAction(e -> service
				.startRewardProcess(txtEmployeeName.getText()));
		return new TitledPane("Give a Reward to someone!", hbGiveReward);
	}

	private TitledPane tasks() {
		Button btnUpdate = new Button("Update List");
		Button btnSubmit = new Button("Submit");
		CheckBox chkApprove = new CheckBox("Approve?");
		HBox hbBottom = new HBox(chkApprove, btnSubmit);
		TableView<RewardTask> tbl = new TableView<>();
		VBox vbHistory = new VBox(btnUpdate, tbl, hbBottom);
		tbl.getColumns().add(
				propertyColumn("Employee Name", "employeeName", 130));
		tbl.getColumns().add(propertyColumn("Created On", "created", 260));
		tbl.getColumns().add(propertyColumn("Name", "name", 160));
		hbBottom.setSpacing(20);
		vbHistory.setSpacing(10);
		EventHandler<ActionEvent> updateAction = e -> {
			tbl.getItems().setAll(service.getTasks());
		};
		BooleanBinding selected = tbl.getSelectionModel()
				.selectedItemProperty().isNull();
		chkApprove.disableProperty().bind(selected);
		btnSubmit.disableProperty().bind(selected);
		btnUpdate.setOnAction(updateAction);
		btnSubmit.setOnAction(e -> {
			RewardTask rt = tbl.getSelectionModel().getSelectedItem();
			service.doTask(rt.getTaskId(), chkApprove.isSelected());
			updateAction.handle(e);
		});
		updateAction.handle(null);
		return new TitledPane("Approve Rewards", vbHistory);
	}

	private TitledPane history() {
		ListView<String> listHistory = new ListView<>();
		Button btnUpdate = new Button("Update");
		Button btnClear = new Button("Clear");
		HBox hbTop = new HBox(btnUpdate, btnClear);
		hbTop.setSpacing(20);
		VBox vbHistory = new VBox(hbTop, listHistory);
		vbHistory.setSpacing(15);
		EventHandler<ActionEvent> updateAction = e -> listHistory.getItems()
				.setAll(service.getAllProcessesSummary());
		btnUpdate.setOnAction(updateAction);
		btnClear.setOnAction(e -> {
			service.clearHistory();
			updateAction.handle(e);
		});
		updateAction.handle(null);
		return new TitledPane("Rewards history", vbHistory);
	}

	private TableColumn<RewardTask, ?> propertyColumn(String title,
			String property, int width) {
		TableColumn<RewardTask, String> column = new TableColumn<>(title);
		column.setCellValueFactory(new PropertyValueFactory<>(property));
		column.setMinWidth(width);
		return column;
	}

}