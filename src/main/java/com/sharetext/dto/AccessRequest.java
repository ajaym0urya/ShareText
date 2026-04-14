package com.sharetext.dto;

import lombok.Data;

@Data
public class AccessRequest {
    private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
    
}
