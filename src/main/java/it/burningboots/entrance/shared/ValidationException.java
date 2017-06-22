package it.burningboots.entrance.shared;

public class ValidationException extends Exception {
	private static final long serialVersionUID = 1747640450899892816L;
	
	private String message;

	public ValidationException() {
		super();
		message="";
	}
	
	public ValidationException(String message) {
		super(message);
		this.message=message;
	}
	
	public ValidationException(String message, Throwable e) {
		super(message, e);
		this.message=message;
	}
	
	public String getMessage() {
		return message;
	}

}
