package all;

public class OrderDataMain {

	private int count;
	private int num;
	private double price;
	private String typeOrder;
	private String sizeOrder;
	private double valProduct;

	public OrderDataMain() {
		// TODO Auto-generated constructor stub
	}

	public OrderDataMain(int count, int num, double price, String typeOrder, String sizeOrder, double valProduct) {
		super();
		this.count = count;
		this.num = num;
		this.price = price;
		this.typeOrder = typeOrder;
		this.sizeOrder = sizeOrder;
		this.valProduct = valProduct;
	}

	public OrderDataMain(String typeOrder, String sizeOrder, double price, int count) {
		super();
		this.count = count;
		this.price = price;
		this.typeOrder = typeOrder;
		this.sizeOrder = sizeOrder;

	}

	public String getSizeOrder() {
		return sizeOrder;
	}

	public void setSizeOrder(String sizeOrder) {
		this.sizeOrder = sizeOrder;
	}

	public int getCount() {
		return count;
	}

	public int getNum() {
		return num;
	}

	public double getPrice() {
		return price;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setTypeOrder(String typeOrder) {
		this.typeOrder = typeOrder;
	}

	public void setValProduct(double valProduct) {
		this.valProduct = valProduct;
	}

	public String getTypeOrder() {
		return typeOrder;
	}

	public double getValProduct() {
		return valProduct;
	}


	@Override
	public boolean equals(Object obj) {

		 if ((obj instanceof OrderDataMain)) {
			OrderDataMain ord=(OrderDataMain)	obj;

		  if (this.getTypeOrder() == ord.getTypeOrder() && this.getSizeOrder() == ord.getSizeOrder() ) return true;



	        }else  return false;

		 return false;

	}

}
