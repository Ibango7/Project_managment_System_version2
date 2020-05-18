/**
 * Person class<br> 
 * Class allows the  instantiation of  person objects.
 * <br>
 * Person could be a <b>customer</b>, a <b>contractor</b> or an <b>architect</b>
 * @author Israel Bango
 */
public class Person {
	/**
	*  Default constructor.
    * <br>
    * This is executed when a new object of class person is instantiated
    * with no arguments
    *
    * @since version 2 (cap_stone 2)
	*/
	public Person() {
		name = "";
		telephone = "";
		email = "";
		address = "";
		role_in_project = "";
	}
	
	/**
	 *  Constructor with parameters.<br>
	 * This is executed when a new object of class Person is instantiated with arguments passed.
	 * 
	 * @param name name of person
	 * @param phone phone of person
	 * @param email email of person
	 * @param address address of person
	 * @param role role of person: customer, architect or contractor
	 * @since version 1
	 */
	public Person(String name, String phone, String email, String address, String role) {
		// call setter functions
		setName(name);
		setPhone(phone);
		setEmail(email);
		setAddress(address);
		setRole(role);	
	}
	
	/**
	 * Simple method.<br>
	 * Receives a name as an argument and assigns it to the person object
	 * 
	 * @param n name of person
	 * @since version 1
	 */
	public void setName(String n) {
		name = n;
	}
	
	/**
	 * Simple method.<br>
	 * Receives a phone number as an argument and assigns it to the person object
	 * 
	 * @param n Phone of the person
	 * @since version 1
	 */
	public void setPhone(String n) {
		telephone = n;
	}
	
	/**
	 * Simple method.<br>
	 * This method receives an email as an argument and assigns it to the person object
	 * 
	 * @param e email of the person
	 * @since version 1
	 */
	public void setEmail(String e) {
		email = e;
	}
	
	/**
	 * Simple method.<br>
	 * This method receives an address an argument and assigns it to the person object
	 * 
	 * @param a Address of the person
	 * @since version 1
	 */
	public void setAddress(String a) {
		address = a;
	}
	
	/**
	 * Simple method.<br>
	 * This method receives a role as an argument and assigns it to the person
	 * roles are either customer, architect or contractor
	 * 
	 * @param r role of the person
	 * @since version 1
	 */
	public void setRole(String r) {
		role_in_project = r;	
	}
	
	/**
	 * Simple method.<br>
	 * This method returns the name of the person object
	 * 
	 * @return name of person
	 * @since version 1
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Simple method.<br>
	 * This method returns the phone of the person object
	 * 
	 * @return phone of person
	 * @since version 1
	 */
	public String getPhone() {
		return telephone;
	}
	
	/**
	 * Simple method.<br>
	 * This method returns the email of the person object
	 * 
	 * @return email of person
	 * @since version 1
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Simple method.<br>
	 * This method returns the address of the Person object
	 * 
	 * @return address of person
	 * @since version 1
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Simple method.<br>
	 * This method returns the role of the person object
	 * roles are either <b>customer</b>, <b>architect</b> or <b>contractor</b>
	 * 
	 * @return role of person
	 * @since version 1
	 */
	public String getRole() {
		return role_in_project;
	}
	
	/**
	 * 
	 * Person class attributes
	 */
	private String name;
	private String telephone;
	private String email;
	private String address;
	private String role_in_project; // who is the person? customer, contractor etc
			
}