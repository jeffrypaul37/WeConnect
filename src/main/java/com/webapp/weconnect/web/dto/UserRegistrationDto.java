package com.webapp.weconnect.web.dto;

public class UserRegistrationDto {
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String location;
//	private String profilePhotoPath;
	private byte[] profile_photo;
	
	public UserRegistrationDto(){
		
	}
	
	public UserRegistrationDto(String firstName, String lastName, String email, String password,String location,byte[] profile_photo) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.location = location;
//		this.profilePhotoPath = profilePhotoPath;
		this.profile_photo = profile_photo;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getLocation() { return location;}
	public void setLocation(String location) { this.location = location; }

//	public String getProfilePhotoPath() {return profilePhotoPath; }

//	public void setProfilePhotoPath(String profilePhotoPath) { this.profilePhotoPath = profilePhotoPath;}

	public byte[] getProfile_photo() {
		return profile_photo;
	}

	public void setProfile_photo(byte[] profile_photo) {
		this.profile_photo = profile_photo;
	}
}
