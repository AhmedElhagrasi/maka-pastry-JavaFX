package all;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class InvoiceController implements Initializable {
	@FXML
	 AnchorPane myinvoice;
	@FXML
	TableView<OrderDataMain> fintable;
	@FXML
	TableColumn<OrderDataMain,String> item,size;
	@FXML
	TableColumn<OrderDataMain,Double> price,amount;
	@FXML
	TableColumn<OrderDataMain,Integer> qty;
	ObservableList<OrderDataMain> list = FXCollections.observableArrayList();

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
		item.setCellValueFactory(new PropertyValueFactory<OrderDataMain, String>("typeOrder"));
		size.setCellValueFactory(new PropertyValueFactory<OrderDataMain, String>("sizeOrder"));
		price.setCellValueFactory(new PropertyValueFactory<OrderDataMain, Double>("price"));
		amount.setCellValueFactory(new PropertyValueFactory<OrderDataMain, Double>("valProduct"));
		qty.setCellValueFactory(new PropertyValueFactory<OrderDataMain, Integer>("count"));
		fintable.setItems(list);
		
		
 
 	}
	
	
	
}
