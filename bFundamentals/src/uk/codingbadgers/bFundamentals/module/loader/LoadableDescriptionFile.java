package uk.codingbadgers.bFundamentals.module.loader;

import java.io.InputStream;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * The Class LoadableDescriptionFile, represents the data stored in the
 * path.yml file of a {@link Loadable}.
 *
 * @author James Fitzpatrick
 */
public class LoadableDescriptionFile {
    
    /** The loadable's name. */
    private final String name;
    
    /** The loadable's version. */
    private final String version;
    
    /** The loadable's main class. */
    private final String mainClass;
	
	/** The loadable's authors. */
	private final List<String> authors;
    
    /**
     * Instantiates a new loadable description file.
     *
     * @param istream the input stream that this file is loaded from
     */
    public LoadableDescriptionFile(InputStream istream) {        
        YamlConfiguration ldf = YamlConfiguration.loadConfiguration(istream);
        
        name = ldf.getString("name", "Unknown Module");
        version = ldf.getString("version", "0.0");
        mainClass = ldf.getString("main-class");
        authors = ldf.getStringList("authors");
    }
    
    /**
     * Gets the name of this loadable.
     *
     * @return the name of this loadable
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the version of this loadable.
     *
     * @return the version of this loadable
     */
    public String getVersion() {
        return version;
    }
    
    /**
     * Gets the main class of this loadable.
     *
     * @return the main class of this loadable
     */
    public String getMainClass() {
        return mainClass;
    }

	/**
	 * Gets the authors of this loadable.
	 *
	 * @return the authors of this loadable
	 */
	public List<String> getAuthors() {
		return authors;
	}
}
