package all;

 
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

 
 
 
public class mainscreenController implements Initializable {
 
	@FXML
	private Button bt_menu;

	@FXML
	public TextField cstName, cstAddress, cstPhone, cstPhone2;

	@FXML
	private DatePicker cstDate;

	@FXML
	Label val_total,totalINV, labelCheck, labelCheck1, labelCheck11, labelCheck2, numinvoice;
	@FXML
	private TableColumn<OrderDataMain, Integer> num, count;

	@FXML
	private TableColumn<OrderDataMain, Double> price, valProduct;

	@FXML
	private TableColumn<OrderDataMain, String> typeOrder, sizeOrder;

	@FXML
	private TableView<OrderDataMain> tablee;

	ObservableList<OrderDataMain> list = FXCollections.observableArrayList();
	SecondScreenMenuController controller;

	boolean phoneNotReady1 = true;
	boolean phoneNotReady2 = true;

	private AnchorPane myInvoice;

	// Event Listener on Button[#bt_menu].onAction
	public void bt_menu(ActionEvent event) throws Exception {

		try {
			
 
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SecondScreenMenu.fxml"));
			Parent root = fxmlLoader.load();
			TableofOrder();

			SecondScreenMenuController controller = fxmlLoader.getController();
			controller.text_name = cstName.getText();
			controller.text_phone = cstPhone.getText();
			controller.text_phone2 = cstPhone2.getText();
			controller.text_add = cstAddress.getText();

			Stage stage = new Stage();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public void TableofOrder() {
		typeOrder.setCellValueFactory(new PropertyValueFactory<OrderDataMain, String>("typeOrder"));
		sizeOrder.setCellValueFactory(new PropertyValueFactory<OrderDataMain, String>("sizeOrder"));
		price.setCellValueFactory(new PropertyValueFactory<OrderDataMain, Double>("price"));
		valProduct.setCellValueFactory(new PropertyValueFactory<OrderDataMain, Double>("valProduct"));
		count.setCellValueFactory(new PropertyValueFactory<OrderDataMain, Integer>("count"));
		num.setCellValueFactory(new PropertyValueFactory<OrderDataMain, Integer>("num"));
		tablee.setItems(list);

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		forceValuetoNumric();
		TableofOrder();

	}

	public void clickprint() {

		if(!list.isEmpty()) {
		if (!getall_CST()) {
			settoDBCust();
			Invoice();
 		} else {
			labelCheck.setTextFill(Color.BLUE);
			labelCheck.setText("# already exist");
			Invoice();
 
		}


		}else { val_total.setText("    no Item(s)");
			val_total.setTextFill(Color.TOMATO);
 		}
	}

	public void customers(MouseEvent event) {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("customers.fxml"));
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

	public void settoDBCust() {

		PreparedStatement createStatement = null;
		DBconnection dBconnection = new DBconnection();
		if (dBconnection.is_connection()) {

			String query = "INSERT INTO customers (name,address,phone,phone2)" + " VALUES (?,?,?,?)";

			try {
				if (validation_val() && phoneNotReady1 && phoneNotReady2) {

					createStatement = dBconnection.db_connector().prepareStatement(query);
					createStatement.setString(1, cstName.getText());
					createStatement.setString(2, cstAddress.getText());
					createStatement.setString(3, cstPhone.getText());
					createStatement.setString(4, cstPhone2.getText());

					createStatement.execute();
					labelCheck2.setText("");
					labelCheck.setText("# Done");
					labelCheck.setTextFill(Color.GREEN);
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
		if (cstName.getText() == "") {
			labelCheck.setText("Can't Customer Name be Blank");
			labelCheck.setTextFill(Color.RED);
			return false;

		}

		if (cstPhone.getText() == "") {
			labelCheck.setText("");

			labelCheck1.setText("Can't Customer Phone be Blank  ");
			labelCheck1.setTextFill(Color.RED);
			return false;

		}
		if (cstAddress.getText() == "") {
			labelCheck.setText("");

			labelCheck2.setText("Can't Customer Address be Blank  ");
			labelCheck2.setTextFill(Color.RED);
			return false;

		}

		return true;
	}

	private void forceValuetoNumric() {

		cstPhone.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.length() <= 11) {
					labelCheck1.setText("");
					phoneNotReady1 = true;
					if (!newValue.matches("\\d*")) {
						cstPhone.setText(newValue.replaceAll("[^\\d]", ""));
					}
				} else {
					cstPhone.setText(newValue.replaceAll("[^\\d]", ""));
					labelCheck1.setText("Can't Customer Phone be " + newValue.length() + " Digit  ");
					labelCheck1.setTextFill(Color.ORANGERED);
					phoneNotReady1 = false;

				}

			}
		});
		cstPhone2.textProperty().addListener(new ChangeListener<String>() {
			@Override
 			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.length() <= 11) {
					labelCheck11.setText("");
					phoneNotReady2 = true;

					if (!newValue.matches("\\d*")) {
						cstPhone2.setText(newValue.replaceAll("[^\\d]", ""));
					}
				} else {
					cstPhone2.setText(newValue.replaceAll("[^\\d]", ""));
					labelCheck11.setText("Can't Customer Phone 2 be " + newValue.length() + " Digit  ");
					labelCheck11.setTextFill(Color.ORANGERED);
					phoneNotReady2 = false;

				}
			}

		});
	}

	public void bt_Reset(ActionEvent actionEvent) {
 
		cstName.setText("");
		cstAddress.setText("");
		cstPhone.setText("");
		cstPhone2.setText("");

		labelCheck.setText("");
		labelCheck1.setText("");
		labelCheck11.setText("");
		labelCheck2.setText("");
		val_total.setText("");
		numinvoice.setText("INV0000");
		tablee.getItems().removeAll(list);
		
		
	

	}

	public boolean getall_CST() {

		
		boolean b = false;
		PreparedStatement statement = null;
		ResultSet rs = null;
		DBconnection dBconnection = new DBconnection();
		if (dBconnection.is_connection() && cstName.getText() != "") {

			try {
				statement = dBconnection.db_connector()
						.prepareStatement("Select * from customers WHERE (name=?) AND (phone=?)");

				statement.setString(1, cstName.getText());
				statement.setString(2, cstPhone.getText());

				rs = statement.executeQuery();
				b = rs.next();

				rs.close();
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());

			}

		}
		return b;

	}

	public void Invoice() {

		  Printer printer = Printer.getDefaultPrinter();
 		  PageLayout pageLayout = printer.createPageLayout(Paper.DESIGNATED_LONG,  
 	             PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
			Pane node = new Pane();

 
 	    Scene sc=new Scene(node);


	        PrinterJob job = PrinterJob.createPrinterJob();
	        if (job != null  && job.showPrintDialog(sc.getWindow()) ) {
	            boolean success = job.printPage(pageLayout , getPrintableText());
	            if (success) {
	                job.endJob();
	            }
	}
 	}
	public Node getPrintableText() {
 
		Text text1 = new Text();
		Text text2 = new Text();
		Text text3 = new Text();
		Text text4 = new Text();
  		Text text5 = new Text();


		StringBuilder header1 = new StringBuilder();
		StringBuilder header2 = new StringBuilder();
		StringBuilder header3 = new StringBuilder();
		StringBuilder header4 = new StringBuilder();
		StringBuilder header5 = new StringBuilder();

		header1.append("\n   فطائر   مكة           ");
		header2.append("      اجود انواع الفطائر                 " + "\n");
		header2.append("      ----------------------------------------- " + "\n");

		header2.append("      Receipt No: " + numinvoice.getText() + "\n");
		header2.append("      Date : " + new SimpleDateFormat("dd/MMM/yyyy").format(new Date()) + "\n");
		header2.append("      Time : " + new SimpleDateFormat("hh:mm:ss a").format(new Date()) + "\n");

		header2.append("      ----------------------------------------- " + "\n\n");

		header2.append("      ITEM     	      " + "SIZE  " + " QTY " + " PRICE " + " AMOUNT " + "\n");
		header2.append("      _________________________________________ \n");
		for (OrderDataMain orderDataMain : tablee.getItems()) {

			
			header2.append("      " + orderDataMain.getTypeOrder() + "      \n\t\t     " + orderDataMain.getSizeOrder() + "  "
					+ orderDataMain.getCount() + "   " + orderDataMain.getPrice() + "  " + orderDataMain.getValProduct()
					+ "\n");
			header2.append("      ----------------------------------------- \n");
		}

 
		header3.append("\t\t\t                     " + val_total.getText() + "\n");

 
		header4.append("خدمة التوصيل:                              " + "\n");
		header4.append("         01019290655-01016694676-01274994122" + "\n");

		header4.append("                FACEBOOK.COM/F5BAR" + "\n");
		header4.append("                INSTAGRAM.COM/f5bar1" + "\n\n");
		header4.append("ميت مزاح ش/الترعة بجوار محل ملابس F5-STORE      \n");

		header4.append("                  Thanks, Come Again    \n\n");
	  	header5.append("      ************************************************\n");

  		header5.append("       SOFTWARE DEV. BY 'AHMED-ELHAGRASI' 01026629512" + "\n");
	  	header5.append("      ************************************************ " + "\n\n\n\n\n\n");
 

		text1.setText(header1.toString());

		  InputStream fontStream = mainscreenController.class.getResourceAsStream("font1.ttf");
 	            Font font = Font.loadFont(fontStream, 28);

  		text1.setFont(font);
		
		text2.setText(header2.toString());
 		text2.setFont(new Font("Courier New Bold",7 ));
		
 		text3.setText(header3.toString());
 		text3.setFont(new Font("Franklin Gothic Demi Cond",10 ));
 		
 		text4.setText(header4.toString());
 		text4.setFont(new Font("Courier New Bold",7 ));
 		text5.setText(header5.toString());
   		text5.setFont(new Font("Courier New Bold",6 ));
  		
  		VBox  pane =new VBox (text1,text2,text3,text4,text5);
  		pane.setAlignment(Pos.CENTER_LEFT); 	
   	
 
		return pane;
	}
	 
	}

  
