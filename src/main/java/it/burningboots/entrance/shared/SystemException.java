package it.burningboots.entrance.shared;

public class SystemException extends Exception {
	private static final long serialVersionUID = 8618269701736198769L;

	private String message;

	public SystemException() {
		super();
		message="";
	}
	
	public SystemException(String message) {
		super(message);
		this.message=message;
	}
	
	public SystemException(String message, Throwable e) {
		super(message, e);
		this.message=message;
	}
	
	public String getMessage() {
		return message;
	}

}
