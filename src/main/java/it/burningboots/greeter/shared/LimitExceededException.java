package it.burningboots.greeter.shared;

public class LimitExceededException extends Exception {
	private static final long serialVersionUID = 8618269701736198769L;

	private String message;

	public LimitExceededException() {
		super();
		message="";
	}
	
	public LimitExceededException(String message) {
		super(message);
		this.message=message;
	}
	
	public LimitExceededException(String message, Throwable e) {
		super(message, e);
		this.message=message;
	}
	
	public String getMessage() {
		return message;
	}

}
