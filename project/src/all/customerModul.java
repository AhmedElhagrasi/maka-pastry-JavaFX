package all;

public class customerModul {


	private int id;
	String phone,phone2;
	private String name , address;


	public customerModul() {

	}



	public customerModul(int id, String phone, String phone2, String name, String address) {
		super();
		this.id = id;
		this.phone = phone;
		this.phone2 = phone2;
		this.name = name;
		this.address = address;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}






}