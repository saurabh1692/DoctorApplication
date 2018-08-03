package io.doctorApp.doc;

public class SubUserDetails 
{
	private String Id;
	private String FirstName;
	private String LastName;
	private String ClinicName;
	private String UserName;
	private String Address;
	private String MobileNumber;
	private String Role;
	private String EmailId;
	private String isActive;
	
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		this.Id = id;
	}
	public String getClinicName() 
	{
		return ClinicName;
	}
	public void setClinicName(String clinicName)
	{
		this.ClinicName = clinicName;
	}
	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		this.FirstName = firstName;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		this.LastName = lastName;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		this.UserName = userName;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		this.Address = address;
	}
	public String getMobileNumber() {
		return MobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.MobileNumber = mobileNumber;
	}
	public String getRole() {
		return Role;
	}
	public void setRole(String role) {
		this.Role = role;
	}
	public String getEmailId() {
		return EmailId;
	}
	public void setEmailId(String emailId) {
		this.EmailId = emailId;
	}
}

