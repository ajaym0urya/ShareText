package com.sharetext.dto;

import lombok.Data;

@Data
public class ContentResponse {
    private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
    
    
}
