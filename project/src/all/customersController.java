package all;


import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class customersController implements Initializable {

	@FXML
	TableView<customerModul> csttable;
	@FXML
	private TableColumn<customerModul, String> cstname, cstaddress;
	@FXML
	private TableColumn<customerModul, Integer> cstid, cstphone, cstphone2;
	@FXML
	TextField search;

	ObservableList<customerModul> list = FXCollections.observableArrayList();

	public customersController() {
		// TODO Auto-generated constructor stub
	}
	private void loadDateoftable() {

		cstid.setCellValueFactory(new PropertyValueFactory<>("id"));
		cstname.setCellValueFactory(new PropertyValueFactory<>("name"));
		cstaddress.setCellValueFactory(new PropertyValueFactory<>("address"));
		cstphone.setCellValueFactory(new PropertyValueFactory<>("phone"));
		cstphone2.setCellValueFactory(new PropertyValueFactory<>("phone2"));

	}

	public void getting_cst() {

		Statement createStatement;
		ResultSet resultSet = null;
		DBconnection dBconnection = new DBconnection();
		if (dBconnection.is_connection()) {

			try {
				createStatement = dBconnection.db_connector().createStatement();
				boolean done = createStatement.execute("select * from  customers");
				if (done) {
					int cstid = 0;

					resultSet = createStatement.getResultSet();
					while (resultSet.next()) {
						String cstname = resultSet.getString(2);
						String cstaddress = resultSet.getString(3);
						String cstphone = resultSet.getString(4);
						String cstphone2 = resultSet.getString(5);



						cstid++;
						list.add(new customerModul(cstid, cstphone, cstphone2, cstname, cstaddress));
						loadDateoftable();

						csttable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

					}
					csttable.setItems(list);

				}
				resultSet.close();
				createStatement.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		getting_cst();
		addTextFilter();
		directMain();

	}

	private void directMain() {
		csttable.setRowFactory(tv -> {
			TableRow<customerModul> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					customerModul rowData = row.getItem();

					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainscreen.fxml"));
					try {
						Parent root = fxmlLoader.load();
						mainscreenController controller = fxmlLoader.getController();
						controller.cstName.setText(rowData.getName());
						controller.cstPhone.setText(rowData.getPhone());
						controller.cstPhone2.setText(rowData.getPhone2());
						controller.cstAddress.setText(rowData.getAddress());

						Stage stage = new Stage();
						stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
						Scene scene = new Scene(root);
						stage.setScene(scene);
						stage.show();

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
			return row;
		});
	}

	public void backhome(MouseEvent event) {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainscreen.fxml"));
		Parent root;
		try {
			root = fxmlLoader.load();
			Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();

			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addTextFilter() {

		FilteredList<customerModul> filteredData = new FilteredList<>(list, p -> true);
		search.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(person -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				String lowerCaseFilter = newValue.toLowerCase();

				if (person.getName().toLowerCase().contains(lowerCaseFilter)) {
					return true; // Filter matches first name.
				} else if (String.valueOf(person.getPhone()).toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else
					return false; // Filter matches last name.

			});
			csttable.setItems(filteredData);
		}

		);

	}

	public void cstRemove(MouseEvent event) {

		customerModul selectedItem = csttable.getSelectionModel().getSelectedItem();
		if (selectedItem != null) {
			PreparedStatement createStatement;
			try {
				DBconnection dBconnection = new DBconnection();
				if (dBconnection.is_connection()) {

					String query = "DELETE FROM customers WHERE (name = ?) AND ( phone = ? )";

					createStatement = dBconnection.db_connector().prepareStatement(query);
					createStatement.setString(1, selectedItem.getName());
					createStatement.setString(2, selectedItem.getPhone());

					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.getDialogPane().setPrefSize(500, 60);
					alert.setHeaderText("Are You Sure Remove '" + selectedItem.getName() + "' ?");

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK) {

						createStatement.executeUpdate();
						csttable.getItems().remove(selectedItem);

					} else if (result.get() == ButtonType.CANCEL) {
						alert.close();
					}
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());

			}

		}
	}

}
