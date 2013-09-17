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
package uk.codingbadgers.bFundamentals.module;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import uk.codingbadgers.bFundamentals.module.annotation.ModuleInfo;
import uk.codingbadgers.bFundamentals.utils.CollectionUtils;

/**
 * The Class LoadableDescriptionFile, represents the data stored in the
 * about a module, either by a path.yml file of a {@link Module} or a
 * {@link ModuleInfo} annotation.
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
     * Instantiates a new module description from the data in a path.yml
     * file.
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

    /**
     * Instantiates a new module description from the data in a 
     * {@link ModuleInfo} annotation.
     *
     * @param info the {@link ModuleInfo} annotation
     * @param mainclass the mainclass loaded
     */
    public ModuleDescription(ModuleInfo info, String mainclass) {
    	this.name = info.value();
    	this.version = info.version();
    	this.mainClass = mainclass;
    	this.description = info.description();
    	this.authors = CollectionUtils.toImmutableList(Arrays.asList(info.authors()));
    	this.dependencies = Collections.unmodifiableCollection(new ArrayList<String>());
	}

	/**
     * Gets the name of this module.
     *
     * @return the name of this module
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the version of this module.
     *
     * @return the version of this module
     */
    public String getVersion() {
        return version;
    }
    
    /**
     * Gets the description for this module.
     * 
     * @return the description of this module
     */
    public String getDescription() {
    	return description;
    }
    
    /**
	 * Gets the main class of this module.
	 * 
	 * @return the fully qualified name of the main class of this module
	 * @deprecated Main classes should not have to be defined anymore
	 */
    public String getMainClass() {
        return mainClass;
    }

	/**
	 * Gets the authors of this module.
	 *
	 * @return a immutable list of the authors of this module
	 */
	public List<String> getAuthors() {
		return authors;
	}
	
	/**
	 * Gets the module dependencies of this loadable, this module will load after
	 * all of the dependencies have module.
	 * 
	 * @return a unmodifiable collection of the dependencies
	 */
	public Collection<String> getDependencies() {
		return dependencies;
	}
}
