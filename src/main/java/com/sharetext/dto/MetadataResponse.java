package com.sharetext.dto;

import lombok.Data;

@Data
public class MetadataResponse {
    private boolean requiresPassword;
    private boolean isExpired;
	public boolean isRequiresPassword() {
		return requiresPassword;
	}
	public void setRequiresPassword(boolean requiresPassword) {
		this.requiresPassword = requiresPassword;
	}
	public boolean isExpired() {
		return isExpired;
	}
	public void setExpired(boolean isExpired) {
		this.isExpired = isExpired;
	}
    
    
}
