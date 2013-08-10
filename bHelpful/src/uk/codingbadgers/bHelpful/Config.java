package uk.codingbadgers.bHelpful;

import uk.codingbadgers.bFundamentals.config.ConfigFile;
import uk.codingbadgers.bFundamentals.config.annotation.Element;

public class Config implements ConfigFile {

	@Element("label.announce")
	public static final String ANNOUNCE_LABEL = "announce";
	@Element("label.maintenance")
	public static final String MAINTENANCE_LABEL = "maintenance";
	@Element("label.motd")
	public static final String MOTD_LABEL = "motd";
	@Element("label.news")
	public static final String NEWS_LABEL = "news";
	@Element("label.list")
	public static final String LIST_LABEL = "list";
	@Element("label.list")
	public static final String REGISTER_LABEL = "list";
	@Element("label.rules")
	public static final String RULES_LABEL = "rules";
	@Element("label.vote")
	public static final String VOTE_LABEL = "vote";

	@Element("announce.interval")
	public static final int ANNOUNCEMENT_INTERVAL = 15 * 60;
	@Element("news.link")
	public static final String NEWS_LINK = "http://mcbadgercraft.com/thecodingbadgers/bSocks/web/news.php";
}
