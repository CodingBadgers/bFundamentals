/**
 * bFundamentals 1.2-SNAPSHOT
 * Copyright (C) 2013  CodingBadgers <plugins@mcbadgercraft.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.codingbadgers.bFundamentals.module.loader;

import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.collect.ImmutableList;

/**
 * The Class LoadableDescriptionFile, represents the data stored in the
 * path.yml file of a {@link Loadable}.
 *
 * @author James Fitzpatrick
 */
public class LoadableDescriptionFile {
    
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
    @SuppressWarnings("deprecation")
	public LoadableDescriptionFile(InputStream istream) {        
        YamlConfiguration ldf = YamlConfiguration.loadConfiguration(istream);
        
        name = ldf.getString("name", "Unknown Module");
        version = ldf.getString("version", "0.0");
        description = ldf.getString("description", "");
        mainClass = ldf.getString("main-class");
        authors = ImmutableList.of(ldf.getStringList("authors").toArray(new String[0]));
        dependencies = Collections.unmodifiableCollection(ldf.getStringList("dependencies"));
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
