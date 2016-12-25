package net.tarine.ibbchianti.client;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.datepicker.client.DateBox;

import net.tarine.ibbchianti.shared.AppConstants;

public class ClientConstants {
	
	//WebSession
	public static final int WEBSESSION_RELOAD_TIME = 1000*60*1; //1 minute
	public static final String WEBSESSION_COOKIE_NAME = "ibbchianti";
	
	//CLIENT DEFAULTS
	public static final String DEFAULT_FRAME_TITLE = "";
	public static final int COOKIE_EXPIRATION_DAYS = 15;
	public static final String EOL = "\r\n";
	
	//FORMATS
	public static final DateTimeFormat FORMAT_TIMESTAMP = DateTimeFormat.getFormat(AppConstants.PATTERN_TIMESTAMP);
	public static final DateTimeFormat FORMAT_DAY = DateTimeFormat.getFormat(AppConstants.PATTERN_DAY);
	public static final DateBox.Format BOX_FORMAT_DAY = new DateBox.DefaultFormat(FORMAT_DAY);
	public static final DateTimeFormat FORMAT_MONTH = DateTimeFormat.getFormat(AppConstants.PATTERN_DAY);
	public static final DateBox.Format BOX_FORMAT_MONTH = new DateBox.DefaultFormat(FORMAT_DAY);
	public static final DateTimeFormat FORMAT_YEAR = DateTimeFormat.getFormat("yyyy");
	public static final NumberFormat FORMAT_CURRENCY = NumberFormat.getFormat(AppConstants.PATTERN_CURRENCY);
	public static final NumberFormat FORMAT_INTEGER = NumberFormat.getFormat("#0");

	//Icons
	public static final String ICON_LOADING_BIG = "<img src='img/chat_loading.gif' style='vertical-align:middle;border:none;' title='In corso...' />";
	public static final Integer ICON_LOADING_WIDTH = 131;
	public static final Integer ICON_LOADING_HEIGHT = 23;
	public static final String ICON_LOADING_SMALL = "<img src='img/ajax-loader-small.gif' style='vertical-align:middle;border:none;' title='In corso...' />";
	public static final String ICON_HUT = "<img src='img/icon_hut.png' style='vertical-align:middle' title='hut' />";
	public static final String ICON_TENT = "<img src='img/icon_tent.png' style='vertical-align:middle' title='tent' />";	
	public static final String ICON_HUT_GREY = "<img src='img/icon_hut_grey.png' style='vertical-align:middle' title='hut' />";
	public static final String ICON_TENT_GREY = "<img src='img/icon_tent_grey.png' style='vertical-align:middle' title='tent' />";	
	public static final String MSG_ICON_INFO = "<img src='img/dialog-information.png' style='vertical-align:middle' />";
	public static final String MSG_ICON_WARN = "<img src='img/dialog-warning.png' style='vertical-align:middle' />";
	public static final String MSG_ICON_ERROR = "<img src='img/dialog-error.png' style='vertical-align:middle' />";
	//LABELS
	public static final String LABEL_EMPTY_RESULT = "<i>Nessun risultato</i>";
	public static final String LABEL_LOADING = ICON_LOADING_SMALL+" <i>caricamento in corso...</i>";
	
	//COOKIE
	public static final String COOKIE_VERSION = "appVersion";
	public static final String COOKIE_ACCESS_KEY = "ak";
	public static final String COOKIE_FILTER_CONFIRMED = "confirmed";
	
	//TRACKING PIWIK
	public static final String TRACKING_PIWIK_CODE =
			"<!-- Pollicino -->"+EOL+
			"<script type=\"text/javascript\">"+EOL+
			"var _paq = _paq || [];public static final int COOKIE_EXPIRATION_DAYS = 15;"+EOL+
			"_paq.push([\"setDocumentTitle\", document.domain + \"/\" + document.title]);"+EOL+
			"_paq.push([\"setCookieDomain\", \"*.burningboots.it\"]);"+EOL+
			"_paq.push([\"setDomains\", [\"*.burningboots.it\"]]);"+EOL+
			"_paq.push(['trackPageView']);"+EOL+
			"_paq.push(['enableLinkTracking']);"+EOL+
			"(function() {"+EOL+
				"var u=\"//pollicino.tarine.net/\";"+EOL+
				"_paq.push(['setTrackerUrl', u+'piwik.php']);"+EOL+
				"_paq.push(['setSiteId', 1]);"+EOL+
				"var d=document, g=d.createElement('script'), s=d.getElementsByTagName('script')[0];"+EOL+
				"g.type='text/javascript'; g.async=true; g.defer=true; g.src=u+'piwik.js'; s.parentNode.insertBefore(g,s);"+EOL+
			"})();"+EOL+
			"</script>"+EOL+
			"<noscript><p><img src=\"//pollicino.tarine.net/piwik.php?idsite=1\" style=\"border:0;\" /></p></noscript>"+EOL+
			"<!-- End Pollicino Code -->"+EOL;
}
