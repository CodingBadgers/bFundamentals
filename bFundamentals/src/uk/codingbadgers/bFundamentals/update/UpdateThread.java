package uk.codingbadgers.bFundamentals.update;

public class UpdateThread extends Thread {

	private final Updater m_updater;
	
	public UpdateThread(Updater updater) {
		m_updater = updater;
		this.setName("Updater: "+updater.getModule());
	}
	
	public void run() {
		try {
			m_updater.update();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Updater getUpdater() {
		return m_updater;
	}
}
