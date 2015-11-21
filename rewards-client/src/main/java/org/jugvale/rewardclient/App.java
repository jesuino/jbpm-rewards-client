package org.jugvale.rewardclient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
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
	
	// Events
	private Runnable UPDATE_TASKS;
	private Runnable UPDATE_HISTORY_ACTION;
		
	// Panes
	private TitledPane PNL_TASKS;

	private Accordion ACCORDION_ACTIONS;

	public static void main(String[] args) throws Exception {
		service = RewardService.getInstance();
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		ACCORDION_ACTIONS = new Accordion(giveReward(), tasks(),
				history());
		Label lblTitle = new Label("Rewards APP");
		lblTitle.setFont(Font.font(25));
		Scene scene = new Scene(new VBox(lblTitle, ACCORDION_ACTIONS), 700, 600);
		stage.setScene(scene);
		stage.setTitle("Rewards App!");
		stage.show();
	}

	private TitledPane giveReward() {
		TextField txtEmployeeName = new TextField();
		txtEmployeeName.setPromptText("Suggest a reward for a colleague");
		Button btnStartRewardsProcess = new Button("Start RewardProcess");
		HBox hbGiveReward = new HBox(new Label("Colleague name"),
				txtEmployeeName, btnStartRewardsProcess);
		hbGiveReward.setSpacing(15);
		btnStartRewardsProcess.setOnAction(e -> { 
			service.startRewardProcess(txtEmployeeName.getText());
			txtEmployeeName.setText("");
			Platform.runLater(UPDATE_TASKS);
			ACCORDION_ACTIONS.setExpandedPane(PNL_TASKS);
		});
		return new TitledPane("Give a Reward to someone!", hbGiveReward);
	}

	private TitledPane tasks() {
		Button btnSubmit = new Button("Submit");
		CheckBox chkApprove = new CheckBox("Approve?");
		HBox hbBottom = new HBox(chkApprove, btnSubmit);
		TableView<RewardTask> tbl = new TableView<>();
		VBox vbHistory = new VBox(tbl, hbBottom);
		tbl.getColumns().add(
				propertyColumn("Employee Name", "employeeName", 130));
		tbl.getColumns().add(propertyColumn("Created On", "created", 260));
		tbl.getColumns().add(propertyColumn("Name", "name", 160));
		hbBottom.setSpacing(20);
		vbHistory.setSpacing(10);
		UPDATE_TASKS = () -> {
			tbl.getItems().setAll(service.getTasks());
		};
		BooleanBinding selected = tbl.getSelectionModel()
				.selectedItemProperty().isNull();
		chkApprove.disableProperty().bind(selected);
		btnSubmit.disableProperty().bind(selected);
		btnSubmit.setOnAction(e -> {
			RewardTask rt = tbl.getSelectionModel().getSelectedItem();
			service.doTask(rt.getTaskId(), chkApprove.isSelected());
			Platform.runLater(UPDATE_TASKS);
			Platform.runLater(UPDATE_HISTORY_ACTION);
		});
		Platform.runLater(UPDATE_TASKS);
		PNL_TASKS = new TitledPane("Approve Rewards", vbHistory);
		return PNL_TASKS;
	}

	private TitledPane history() {
		ListView<String> listHistory = new ListView<>();
		Button btnClear = new Button("Clear");
		VBox vbHistory = new VBox(btnClear, listHistory);
		vbHistory.setSpacing(15);
		UPDATE_HISTORY_ACTION = () -> listHistory.getItems()
				.setAll(service.getAllProcessesSummary());

		btnClear.setOnAction(e -> {
			service.clearHistory();
			Platform.runLater(UPDATE_HISTORY_ACTION);
		});
		Platform.runLater(UPDATE_HISTORY_ACTION);
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