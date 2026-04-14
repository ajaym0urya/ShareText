package com.sharetext.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.Instant;

@Data
public class ShareRequest {
    @NotBlank
    private String content;
    private Instant expirationDate;
    private String password;
    private String customAlias;
    
	public String getCustomAlias() {
		return customAlias;
	}
	public void setCustomAlias(String customAlias) {
		this.customAlias = customAlias;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Instant getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Instant expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    

    
}
