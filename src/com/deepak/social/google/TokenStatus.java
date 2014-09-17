package com.deepak.social.google;

public class TokenStatus {

	public boolean valid;
	public String gplus_id;
	public String message;
	public String email;
	public String firstName;
	public String lastName;
	public String gender;
	

	public TokenStatus() {
		valid = false;
		gplus_id = "";
		message = "";
	}

	public void setValid(boolean v) {
		this.valid = v;
	}

	public void setId(String gplus_id) {
		this.gplus_id = gplus_id;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
}
