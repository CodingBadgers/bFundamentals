package uk.codingbadgers.bFundamentals.update;

/**
 * The Updater thread
 */
public class UpdateThread extends Thread {

	/** The updater to use. */
	private final Updater m_updater;
	
	/**
	 * Instantiates a new update thread.
	 *
	 * @param updater the updater
	 */
	public UpdateThread(Updater updater) {
		m_updater = updater;
		this.setName("Updater: "+updater.getModule());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		try {
			m_updater.update();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the updater from the thread.
	 *
	 * @return the updater
	 */
	public Updater getUpdater() {
		return m_updater;
	}
}
