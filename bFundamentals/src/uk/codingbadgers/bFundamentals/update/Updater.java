package uk.codingbadgers.bFundamentals.update;

import java.io.File;
import java.net.URL;
import java.util.logging.Logger;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.module.Module;

public abstract class Updater {

	protected final String m_updaterName;
	protected final Module m_module;
	protected final File m_downloadFolder;
	protected final File m_backupFolder;
	protected final Logger m_log;
	protected URL m_downloadLink = null;
	protected URL m_repository = null;
	protected String m_newVersion = null;
	protected boolean m_download = false;
	
	public Updater(Module module, String string) {
		m_module = module;	
		m_updaterName = string;
		m_log = new UpdateLogger(this);
		
		m_downloadFolder = new File(bFundamentals.getModuleLoader().getModuleDir() + File.separator + ".update" + File.separator);
		m_downloadFolder.mkdirs();
		m_backupFolder = new File(bFundamentals.getModuleLoader().getModuleDir() + File.separator + ".backup" + File.separator);
		m_backupFolder.mkdirs();
	}
	
	public Module getModule() {
		return m_module;
	}
	
	public Logger getLogger() {
		return m_log;
	}
	
	public String getUpdater() {
		return m_updaterName;
	}
	
	public abstract boolean checkUpdate();
	public abstract void downloadUpdate() throws Exception;
	public abstract void applyUpdate() throws Exception;
	
	public void update() throws Exception {
		if (checkUpdate()) {
			if (bFundamentals.getConfigurationManager().getAutoDownload())
				downloadUpdate();
			if (bFundamentals.getConfigurationManager().getAutoApply())
				applyUpdate();
		}
	}
	
	protected static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();	 	
		Node nValue = (Node) nlList.item(0);	 
		return nValue.getNodeValue();
	}

	public URL getURL() {
		return m_downloadLink;
	}
}
