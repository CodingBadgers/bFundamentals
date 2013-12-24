/**
 * bHelpful 1.2-SNAPSHOT
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
	public static final String REGISTER_LABEL = "register";
	@Element("label.rules")
	public static final String RULES_LABEL = "rules";
	@Element("label.vote")
	public static final String VOTE_LABEL = "vote";

	@Element("announce.interval")
	public static final int ANNOUNCEMENT_INTERVAL = 15 * 60;
	@Element("news.link")
	public static final String NEWS_LINK = "http://mcbadgercraft.com/server/news.php";
}
