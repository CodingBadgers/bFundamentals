package uk.codingbadgers.bFundamentals.module;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import uk.codingbadgers.bFundamentals.utils.CollectionUtils;

/**
 * The Class LoadableDescriptionFile, represents the data stored in the
 * path.yml file of a {@link Loadable}.
 *
 * @author James Fitzpatrick
 */
public class ModuleDescription {
    
    private final String name;
    private final String version;
    private final String description;
    private final String mainClass;
	private final List<String> authors;
	private final Collection<String> dependencies;
    
    /**
     * Instantiates a new loadable description file.
     *
     * @param istream the input stream that this file is loaded from
     */
	public ModuleDescription(InputStream istream) {        
        YamlConfiguration ldf = YamlConfiguration.loadConfiguration(istream);
        
        name = ldf.getString("name", "Unknown Module");
        version = ldf.getString("version", "0.0");
        description = ldf.getString("description", "");
        mainClass = ldf.getString("main-class");
        authors = CollectionUtils.toImmutableList(ldf.getStringList("authors"));
        dependencies = Collections.unmodifiableCollection(ldf.getStringList("dependencies"));
    }
    
    public ModuleDescription(String value, String version, String mainClass, String description, String[] authors) {
    	this.name = value;
    	this.version = version;
    	this.mainClass = mainClass;
    	this.description = description;
    	this.authors = CollectionUtils.toImmutableList(Arrays.asList(authors));
    	this.dependencies = Collections.unmodifiableCollection(new ArrayList<String>());
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
     * Gets the description for this loadable.
     * 
     * @return the description of this loadable
     */
    public String getDescription() {
    	return description;
    }
    
    /**
	 * Gets the main class of this loadable.
	 * 
	 * @return the fully qualified name of the main class of this loadable
	 * @deprecated Main classes should not have to be defined anymore
	 */
    public String getMainClass() {
        return mainClass;
    }

	/**
	 * Gets the authors of this loadable.
	 *
	 * @return a immutable list of the authors of this loadable
	 */
	public List<String> getAuthors() {
		return authors;
	}
	
	/**
	 * Gets the module dependencies of this loadable, this module will load after
	 * all of the dependencies have loaded.
	 * 
	 * @return a unmodifiable collection of the dependencies
	 */
	public Collection<String> getDependencies() {
		return dependencies;
	}
}
