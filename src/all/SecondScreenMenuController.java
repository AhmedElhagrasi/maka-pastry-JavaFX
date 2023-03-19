package all;

import java.io.IOException;
import java.net.URL;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SecondScreenMenuController implements Initializable {

	double val_price;
	double valProduct;
	double total_price;
	String val_order;
	String val_orderSize;
	int count = 1;
	int num = 1;

	ArrayList<OrderDataMain> listOfOrder = new ArrayList<>();
	ObservableList<OrderDateMenu> list = FXCollections.observableArrayList();
	ObservableList<OrderDataMain> list2 = FXCollections.observableArrayList();
	OrderDateMenu orderDateMenu;

	// columns first table
	 
	@FXML
	TableView<OrderDateMenu> tableView;
	@FXML
	TableColumn<OrderDateMenu, String> type_order, order_size;
	@FXML
	TableColumn<OrderDateMenu, Double> order_S, order_M, order_L, order_cpno;
	@FXML
	TableColumn<OrderDateMenu, Integer> order_count;

	// columns second table
	@FXML
	TableView<OrderDataMain> tableView2;
	@FXML
	TableColumn<OrderDataMain, String> typeOrder, sizeOrder;
	@FXML
	TableColumn<OrderDataMain, Double> price;
	@FXML
	TableColumn<OrderDataMain, Integer> Count;

	@FXML
	Button Done, btback, btadd_item, btDelete;
	@FXML
	Label labelCheck;
	@FXML
	TextField itmName, itmSmall, itmMediam, itmLarge, itmCono, searchText;
	String text_name, text_phone, text_phone2, text_add;
	mainscreenController controller;
	private ArrayList<String> tableList = new ArrayList<String>();

	public SecondScreenMenuController() {

	}

	public void btback(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainscreen.fxml"));
			Parent root = fxmlLoader.load();
			controller = fxmlLoader.getController();

			controller.cstName.setText(text_name);
			controller.cstPhone.setText(text_phone);
			controller.cstPhone2.setText(text_phone2);
			controller.cstAddress.setText(text_add);

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void Done(ActionEvent event) throws IOException {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainscreen.fxml"));
		Parent root = fxmlLoader.load();
		controller = fxmlLoader.getController();

		controller.cstName.setText(text_name);
		controller.cstPhone.setText(text_phone);
		controller.cstPhone2.setText(text_phone2);
		controller.cstAddress.setText(text_add);

		ObservableList<OrderDataMain> items = tableView2.getItems();

		items.forEach((e) -> {
			e.setNum(items.indexOf(e) + 1);
		});

		controller.list.addAll(items);

		if (total_price != 0.0)
			controller.val_total.setText("Total : " + total_price + " EG");
		controller.numinvoice.setText("INV00" + new SimpleDateFormat("ddMMyyhhmmss").format(new Date()) + "00");
		Stage stage = new Stage();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		forceValuetoNumric();
		addTextFilter();
		GetFromDBmenu();

	}

	@FXML
	private void btadd_item() {

		GetdataCell();

		typeOrder.setCellValueFactory(new PropertyValueFactory<>("typeOrder"));
		sizeOrder.setCellValueFactory(new PropertyValueFactory<>("sizeOrder"));
		price.setCellValueFactory(new PropertyValueFactory<>("price"));
		Count.setCellValueFactory(new PropertyValueFactory<>("Count"));

		for (OrderDataMain o1 : listOfOrder) {

			for (OrderDataMain o2 : list2) {

				if (o1.equals(o2)) {
					o1.setTypeOrder("");

					Alert alert = new Alert(AlertType.WARNING);
					alert.getDialogPane().setPrefSize(350, 80);
					alert.setHeaderText("This Order Was Added  " + o2.getTypeOrder());
					alert.showAndWait();
				}
			}
			if (!o1.getTypeOrder().equals("")) {
				list2.addAll(o1);

 				total_price += valProduct;
				tableView2.setItems(list2);

			}

		}

		listOfOrder.removeAll(list2);
	}

	public void btDelete() {

		OrderDataMain si = tableView2.getSelectionModel().getSelectedItem();
		if (si != null) {
			tableView2.getItems().remove(si);
			total_price -= si.getPrice() * si.getCount();
		}
	}

	public void GetFromDBmenu() {
		Statement createStatement = null;
		ResultSet resultSet = null;
		DBconnection dBconnection = new DBconnection();
		if (dBconnection.is_connection()) {

			try {
				DatabaseMetaData metaData = dBconnection.db_connector().getMetaData();
				String[] types = { "TABLE" };

				ResultSet rs = metaData.getTables("null", "null", "%", types);
				while (rs.next()) {

					String tableName = rs.getString(3);
					tableList.add(tableName);

				}

				createStatement = dBconnection.db_connector().createStatement();

				if (tableList.contains("MenuOrders")) {
					createStatement.execute("select * from MenuOrders");

					resultSet = createStatement.getResultSet();
					while (resultSet.next()) {
//						int id_order = resultSet.getInt(1);
						String name_order = resultSet.getString(2);
						double salS_order = resultSet.getDouble(3);
						double salM_order = resultSet.getDouble(4);
						double salL_order = resultSet.getDouble(5);
						double salC_order = resultSet.getDouble(6);

						list.add(new OrderDateMenu(name_order, salS_order, salM_order, salL_order, salC_order));

						type_order.setCellValueFactory(new PropertyValueFactory<>("name_order"));
						order_S.setCellValueFactory(new PropertyValueFactory<>("salS_order"));
						order_M.setCellValueFactory(new PropertyValueFactory<>("salM_order"));
						order_L.setCellValueFactory(new PropertyValueFactory<>("salL_order"));
						order_cpno.setCellValueFactory(new PropertyValueFactory<>("salC_order"));
						order_size.setCellValueFactory(new PropertyValueFactory<>("order_size"));
						order_count.setCellValueFactory(new PropertyValueFactory<>("order_count"));

						tableView.setItems(list);
					}
					resultSet.close();
				} else {
					int executeUpdate = createStatement.executeUpdate(
							"CREATE TABLE MenuOrders ( id INTEGER NOT NULL UNIQUE, order_name TEXT NOT NULL, price_S NUMERIC , price_M NUMERIC, price_l	NUMERIC, cono NUMERIC, PRIMARY KEY(id AUTOINCREMENT)");
 				}

				createStatement.close();
				dBconnection.db_connector().close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}

		}

	}

	public void settoMenu(MouseEvent event) {

		PreparedStatement createStatement = null;
		DBconnection dBconnection = new DBconnection();
		if (dBconnection.is_connection()) {

			String query = "INSERT INTO MenuOrders (order_name,price_S,price_M,price_l,cono)" + " VALUES (?,?,?,?,?)";

			try {
				if (validation_val()) {
					createStatement = dBconnection.db_connector().prepareStatement(query);
					createStatement.setString(1, itmName.getText());
					createStatement.setDouble(2, Double.valueOf(itmSmall.getText()));
					createStatement.setDouble(3, Double.valueOf(itmMediam.getText()));
					createStatement.setDouble(4, Double.valueOf(itmLarge.getText()));
					createStatement.setDouble(5, Double.valueOf(itmCono.getText()));

					list.add(new OrderDateMenu(itmName.getText().toString(), Double.valueOf(itmSmall.getText()),
							Double.valueOf(itmMediam.getText()), Double.valueOf(itmLarge.getText()),
							Double.valueOf(itmCono.getText())));

					createStatement.execute();

				}

			} catch (Exception e) {
				try {
					createStatement.close();
					createStatement.getConnection().close();
					dBconnection.db_connector().close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println(e.getMessage());

			}

		}
	}

	private boolean validation_val() {

		if (itmName.getText() != "" && itmSmall.getText() != "" && itmMediam.getText() != "" && itmLarge.getText() != ""
				&& itmCono.getText() != "") {

			for (OrderDateMenu orderDateMenu : tableView.getItems()) {

				if (itmName.getText().equals(orderDateMenu.getName_order())) {
					labelCheck.setText("Item Is Repeated ");
					labelCheck.setTextFill(Color.RED);
					return false;
				}
			}

			labelCheck.setText("Item Is added");
			labelCheck.setTextFill(Color.GREEN);
			return true;
		} else {
			labelCheck.setText("Must Add Item And Prices");
			labelCheck.setTextFill(Color.RED);

			return false;
		}
	}

	private void forceValuetoNumric() {

		(itmSmall).textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					itmSmall.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}

		});

		(itmMediam).textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					itmMediam.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}

		});

		(itmLarge).textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					itmLarge.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}

		});
		(itmCono).textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					itmCono.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}

		});

	}

	public void deletefromMenu(MouseEvent event) {
		OrderDateMenu selectedItem1 = tableView.getSelectionModel().getSelectedItem();
		if (selectedItem1 != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.getDialogPane().setPrefSize(500, 60);
			alert.setHeaderText("Are You Sure Remove '" + selectedItem1.getName_order() + "' ?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				tableView.getItems().remove(selectedItem1);
				delfromDB(selectedItem1.getName_order());
			} else if (result.get() == ButtonType.CANCEL) {
				alert.close();
			}
		}

	}

	private void delfromDB(String name) {
		PreparedStatement createStatement;
		try {
			DBconnection dBconnection = new DBconnection();
			if (dBconnection.is_connection()) {

				String query = "DELETE FROM MenuOrders WHERE order_name = (?)";

				createStatement = dBconnection.db_connector().prepareStatement(query);
				createStatement.setString(1, name);
				createStatement.executeUpdate();

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
	}

	public void GetdataCell() {

//		ObservableList<TablePosition> selectedCells = 
//		for (TablePosition tablePosition : selectedCells) {
//			int row = tablePosition.getRow();

		orderDateMenu = tableView.getSelectionModel().getSelectedItem();
		if (orderDateMenu != null) {
			val_order = orderDateMenu.getName_order();
			val_orderSize = orderDateMenu.getOrder_size().getValue();
			val_price = orderDateMenu.val_price;
			count = orderDateMenu.count;
			valProduct = val_price;
		}
		if (val_price != 0.0) {
			valProduct *= count;

			listOfOrder.add(new OrderDataMain(count, num, val_price, val_order, val_orderSize, valProduct));
		} else {

			Alert alert = new Alert(AlertType.WARNING);
			alert.getDialogPane().setPrefSize(300, 50);
			alert.setHeaderText("Must Choose Size Or Price Not Equal 0");
			alert.showAndWait();
		}

	}

	public void addTextFilter() {

		FilteredList<OrderDateMenu> filteredData = new FilteredList<>(list, p -> true);
		searchText.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(person -> {
				// If filter text is empty, display all persons.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				// Compare first name and last name of every person with filter text.
				String lowerCaseFilter = newValue.toLowerCase();

				if (person.getName_order().toLowerCase().contains(lowerCaseFilter)) {
					return true; // Filter matches first name.
				} else
					return false; // Filter matches last name.

			});
			tableView.setItems(filteredData);
		}

		);

	}

}
