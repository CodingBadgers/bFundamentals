package uk.codingbadgers.bFundamentals.module.loader;

import java.io.InputStream;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author james
 */
public class LoadableDescriptionFile {
    
    private final String name;
    private final String version;
    private final String mainClass;
    
    public LoadableDescriptionFile(InputStream istream) {        
        YamlConfiguration ldf = YamlConfiguration.loadConfiguration(istream);
        
        name = ldf.getString("name", "Unknown Module");
        version = ldf.getString("version", "0.0");
        mainClass = ldf.getString("main-class");
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
}
