package uk.codingbadgers.bFundamentals.update;

import java.io.File;
import java.net.URL;
import java.util.logging.Logger;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.module.Module;

/**
 * The Base updater class.
 */
public abstract class Updater {

	/** The updater name. */
	protected final String m_updaterName;
	
	/** The module. */
	protected final Module m_module;
	
	/** The download folder. */
	protected final File m_downloadFolder;
	
	/** The backup folder. */
	protected final File m_backupFolder;
	
	/** The logger. */
	protected final Logger m_log;
	
	/** The download link. */
	protected URL m_downloadLink = null;
	
	/** The repository. */
	protected URL m_repository = null;
	
	/** The new version. */
	protected String m_newVersion = null;
	
	/** The download. */
	protected boolean m_download = false;
	
	/**
	 * Instantiates a new updater.
	 *
	 * @param module the module
	 * @param string the updater name
	 */
	public Updater(Module module, String string) {
		m_module = module;	
		m_updaterName = string;
		m_log = new UpdateLogger(this);
		
		m_downloadFolder = new File(bFundamentals.getModuleLoader().getModuleDir() + File.separator + ".update" + File.separator);
		m_downloadFolder.mkdirs();
		m_backupFolder = new File(bFundamentals.getModuleLoader().getModuleDir() + File.separator + ".backup" + File.separator);
		m_backupFolder.mkdirs();
	}
	
	/**
	 * Gets the module.
	 *
	 * @return the module
	 */
	public Module getModule() {
		return m_module;
	}
	
	/**
	 * Gets the logger.
	 *
	 * @return the logger
	 */
	public Logger getLogger() {
		return m_log;
	}
	
	/**
	 * Gets the updater.
	 *
	 * @return the updater
	 */
	public String getUpdater() {
		return m_updaterName;
	}
	
	/**
	 * Check update.
	 *
	 * @return true, if update is needed
	 */
	public abstract boolean checkUpdate();
	
	/**
	 * Download update.
	 *
	 * @throws Exception the exception
	 */
	public abstract void downloadUpdate() throws Exception;
	
	/**
	 * Apply update.
	 *
	 * @throws Exception the exception
	 */
	public abstract void applyUpdate() throws Exception;
	
	/**
	 * Update.
	 *
	 * @throws Exception the exception
	 */
	public void update() throws Exception {
		if (checkUpdate()) {
			if (bFundamentals.getConfigurationManager().getAutoDownload())
				downloadUpdate();
			if (bFundamentals.getConfigurationManager().getAutoApply())
				applyUpdate();
		}
	}
	
	/**
	 * Gets a xml tag value.
	 *
	 * @param sTag the tag
	 * @param eElement the element
	 * @return the tag value
	 */
	protected static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();	 	
		Node nValue = (Node) nlList.item(0);	 
		return nValue.getNodeValue();
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public URL getURL() {
		return m_downloadLink;
	}
}
