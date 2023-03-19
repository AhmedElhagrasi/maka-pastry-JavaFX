package all;


import javafx.scene.control.ComboBox;

public class OrderDateMenu {

	private String name_order ;
	private double salS_order ;
	private double salM_order ;
	private double salL_order ;
	private double salC_order ;
	private ComboBox<Integer> order_count =new ComboBox<>();
	private ComboBox<String> order_size=new ComboBox<>();
 	double val_price;
 	int count =1 ;


	public OrderDateMenu(String name_order, double salS_order, double salM_order, double salL_order, double salC_order ) {
		super();
		this.name_order = name_order;
		this.salS_order = salS_order;
		this.salM_order = salM_order;
		this.salL_order = salL_order;
		this.salC_order = salC_order;
  		order_size.getItems().addAll(new String[] { "Small ", "Mediam", "Large " ,"Cono  "});


	}



	public OrderDateMenu() {
		// TODO Auto-generated constructor stub
	}






	public String getName_order() {
		return name_order;
	}



	public void setName_order(String name_order) {
		this.name_order = name_order;
	}



	public double getSalS_order() {
		return salS_order;
	}



	public void setSalS_order(double salS_order) {
		this.salS_order = salS_order;
	}



	public double getSalM_order() {
		return salM_order;
	}



	public void setSalM_order(double salM_order) {
		this.salM_order = salM_order;
	}



	public double getSalL_order() {
		return salL_order;
	}



	public void setSalL_order(double salL_order) {
		this.salL_order = salL_order;
	}



	public double getSalC_order() {
		return salC_order;
	}



	public void setSalC_order(double salC_order) {
		this.salC_order = salC_order;
	}



	public ComboBox<String> getOrder_size() {
		order_size.setOnAction(event -> getitem(order_size)	);
		return order_size;
	}



	public void setOrder_size(ComboBox<String> order_size) {
		this.order_size = order_size;
		}


	public ComboBox<Integer> getOrder_count() {

  		order_count.getItems().addAll( 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
  		order_count.setOnAction(event -> count = this.order_count.getValue().intValue());

		return order_count;
	}





	public void setOrder_count( ComboBox<Integer> order_count) {

		this.order_count = order_count;
	}




	private  void getitem(ComboBox< String > order_size) {

			try {
				String seletval = order_size.getSelectionModel().getSelectedItem().toString().trim();
 					if (seletval.equals("Small")) {

						  val_price = salS_order;

					} else if (seletval.equals("Mediam")) {
							  val_price = salM_order;


					} else if (seletval.equals("Large")) {
						val_price = salL_order;

					}else if (seletval.equals("Cono")) {
						val_price = salC_order;


				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e);
			}

		}






}
