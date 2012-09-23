package uk.codingbadgers.bFundamentals.update;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * The Updater utils class
 */
public class UpdaterUtils {

	/**
	 * Download a file from a specific url.
	 *
	 * @param link the link
	 * @param outputFile the output file
	 * @throws Exception the exception
	 */
	public static void download(URL link, File outputFile) throws Exception{
		if (link == null) 
			throw new IllegalArgumentException ("dl link cannot be null");
		
        ReadableByteChannel rbc = Channels.newChannel(link.openStream());

        FileOutputStream output = new FileOutputStream(outputFile);
        output.getChannel().transferFrom(rbc, 0, 1 << 24);
        output.close();
	}
}
