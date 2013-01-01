package uk.codingbadgers.bsocks.web;

public enum RequestType {

	POST("POST"),
	GET("GET");
	
	private String value;

	private RequestType(String value) {
		this.value = value;
	}
	
	public String toString() {
		return value;
	}
}
