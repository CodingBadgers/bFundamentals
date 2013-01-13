package uk.codingbadgers.bFundamentals.module.loader;

import java.io.InputStream;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author james
 */
public class LoadableDescriptionFile {
    
    private final String name;
    private final String version;
    private final String mainClass;
	private final List<String> authors;
    
    public LoadableDescriptionFile(InputStream istream) {        
        YamlConfiguration ldf = YamlConfiguration.loadConfiguration(istream);
        
        name = ldf.getString("name", "Unknown Module");
        version = ldf.getString("version", "0.0");
        mainClass = ldf.getString("main-class");
        authors = ldf.getStringList("authors");
    }
    
    public String getName() {
        return name;
    }
    
    public String getVersion() {
        return version;
    }
    
    public String getMainClass() {
        return mainClass;
    }

	public List<String> getAuthors() {
		return authors;
	}
}
