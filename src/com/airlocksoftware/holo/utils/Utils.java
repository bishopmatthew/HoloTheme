package com.airlocksoftware.holo.utils;

import java.util.Date;
import java.util.regex.Pattern;

import android.content.Context;

public class Utils {

	public static String getLocalizedDate(Context context, Date transactionDate) {
		java.text.DateFormat dateForm = android.text.format.DateFormat.getDateFormat(context);
		String localizedDate = dateForm.format(transactionDate);
		return localizedDate.replace("/", ".");
	}

	public final static Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
			+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

	public static boolean checkEmail(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

}
