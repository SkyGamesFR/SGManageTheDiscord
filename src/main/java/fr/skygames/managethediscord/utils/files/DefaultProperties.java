package fr.skygames.managethediscord.utils.files;

import fr.skygames.managethediscord.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

public abstract class DefaultProperties extends Properties{

	private static final long serialVersionUID = -7474515633562421485L;

	public DefaultProperties() {
		super();
		Logger logger = LoggerFactory.getLogger(DefaultProperties.class);
		try {

			File file = new File(this.filename());
			InputStream input = Main.class.getClassLoader().getResourceAsStream(this.filename());

			/**
			 *
			 */
			if (input == null) {
				logger.error("Sorry, unable to find " + this.filename() + " in JAR Resources");
				return;
			}

			if(!file.exists()) {
				// create file
				Files.copy(input, file.toPath());
				logger.debug( this.filename() + " successfuly imported from JAR Resources !");
			}

			input = Files.newInputStream(file.toPath());
			this.load(input);

		} catch (IOException e) {
			logger.error("Exception during SqlProperties initialization !", e);
		}
	}
	
	public abstract String filename();
	
}
