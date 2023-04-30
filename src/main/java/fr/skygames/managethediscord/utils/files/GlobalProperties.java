package fr.skygames.managethediscord.utils.files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GlobalProperties extends DefaultProperties {

	/**
	 *
	 */
	private static final long serialVersionUID = -2466318655747892039L;
	private final Logger logger = LoggerFactory.getLogger(GlobalProperties.class);

	@Override
	public String filename() {
		return "global.properties";
	}

	public String getBotToken() {
		final String value = "bot_token";

		if(!this.containsKey(value)) {
			logger.warn("Unable to find \"" + value + "\" in " + this.filename());
			return ""; //return default
		}

		String property = this.getProperty(value);

		if(property == null || property.isEmpty()) {
			logger.error("\"" + property + "\" value for \"" + value + "\" is invalid ! ( return default false )");
			return "";
		}else {
			return property;
		}
	}

	public int getPort() {
		final String value = "port";

		if(!this.containsKey(value)) {
			logger.warn("Unable to find \"" + value + "\" in " + this.filename());
			return 8686; //return default
		}

		String property = this.getProperty(value);
		try {
			int port = Integer.parseInt(property);
			return port;
		} catch (Exception e) {
			logger.error("\"" + value + "\" must be an integer ! ( invalid " + property + " )", e);
			return 8686; //return default
		}
	}

	public long getWelcomeChannel() {
		final String value = "welcome_channel";

		if(!this.containsKey(value)) {
			logger.warn("Unable to find \"" + value + "\" in " + this.filename());
			return 0; //return default
		}

		String property = this.getProperty(value);
		try {
			return Long.parseLong(property);
		} catch (Exception e) {
			logger.error("\"" + value + "\" must be an integer ! ( invalid " + property + " )", e);
			return 0; //return default
		}

	}

	public String getGuildID() {
		final String value = "guild_id";

		if(!this.containsKey(value)) {
			logger.warn("Unable to find \"" + value + "\" in " + this.filename());
			return "723615515171487827"; //return default
		}

		String property = this.getProperty(value);
		try {
			return property;
		} catch (Exception e) {
			logger.error("\"" + value + "\" must be an integer ! ( invalid " + property + " )", e);
			return "0"; //return default
		}
	}

	public List<String> getBadWords() {
		List<String> result = new ArrayList<String>();
		String[] words = this.getProperty("bad_words").split(",");
		for (String word : words) {
			result.add(word.trim());
		}

		if(result.size() > 0) {
			return result;
		}else {
			return Collections.emptyList();
		}
	}
}
