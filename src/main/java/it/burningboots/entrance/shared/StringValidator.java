package it.burningboots.entrance.shared;

import com.google.gwt.regexp.shared.RegExp;

public class StringValidator {
	public static final String REGEXP_EMAIL = "^([\\w_!#\\$%&'\\*\\+\\-/=\\?\\^`\\{\\|\\}~\\.])+@([\\w\\-\\.]+\\.)+[\\w]{2,8}$";
	//public static final String REGEXP_EMAIL = "^([a-zA-Z0-9_\\-\\.])+@([\\w\\-\\.]+\\.)+[A-Z]{2,6}$";
	public static final String REGEXP_NAME = "^([a-zA-Z\\s'])+$";
	
	public static void validateName(String name) throws ValidationException {
		if (name == null) {
			throw new ValidationException("Please enter your name / Inserisci il tuo nome per favore");
		} else {
			RegExp nameRegExp = RegExp.compile(REGEXP_NAME);
			boolean matches = nameRegExp.test(name);
			if (!matches) {
				throw new ValidationException("Please enter only alphanumeric values for your name / Inserisci solo numeri e lettere per favore");
			}
		}
	}
	
	public static void validateEmail(String email) throws ValidationException {
		if (email == null) {
			throw new ValidationException("Please enter your email / Inserisci la tua email");
		} else {
			RegExp emailRegExp = RegExp.compile(REGEXP_EMAIL);
			boolean matches = emailRegExp.test(email);
			if (!matches) {
				throw new ValidationException("Please enter a valid email / Inserisci un'email valida per favore");
			}
		}
	}
	
	public static void validateItemNumber(String itemNumber) throws ValidationException {
		if (itemNumber == null) {
			throw new ValidationException("Please enter a valid replacement code / Inserisci il codice di sostituzione corretto per favore");
		} else {
			if (itemNumber.length() != AppConstants.ITEM_NUMBER_LENGHT) {
				throw new ValidationException("Please enter a valid replacement code / Inserisci il codice di sostituzione corretto per favore");
			}
		}
	}
	
//	private static final String emailRegExp = "^([\\w_!#\\$%&'\\*\\+\\-/=\\?\\^`\\{\\|\\}~\\.])+@([\\w\\-\\.]+\\.)+[\\w]{2,8}$";
//	//private static final String emailRegExp = "^([a-zA-Z0-9_\\-\\.])+@([\\w\\-\\.]+\\.)+[A-Z]{2,6}$";
//	private static final Pattern emailPattern = Pattern.compile(emailRegExp, Pattern.CASE_INSENSITIVE);
//	private static final String nameRegExp = "^([a-zA-Z\\s'])+$";
//	private static final Pattern namePattern = Pattern.compile(nameRegExp, Pattern.CASE_INSENSITIVE);
//	
//	public static void validateName(String name) throws ValidationException {
//		if (name == null) {
//			throw new ValidationException("Please enter your name / Inserisci il tuo nome per favore");
//		} else {
//			Matcher matcher = namePattern.matcher(name);
//			if (!matcher.matches()) {
//				throw new ValidationException("Please enter only alphanumeric values for your name / Inserisci solo numeri e lettere per favore");
//			}
//		}
//	}
//	
//	public static void validateEmail(String email) throws ValidationException {
//		if (email == null) {
//			throw new ValidationException("Please enter your email / Inserisci la tua email");
//		} else {
//			Matcher matcher = emailPattern.matcher(email);
//			if (!matcher.matches()) {
//				throw new ValidationException("Please enter a valid email / Inserisci un'email valida per favore");
//			}
//		}
//	}
	
}
