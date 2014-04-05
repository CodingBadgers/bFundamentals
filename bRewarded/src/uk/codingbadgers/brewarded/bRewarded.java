package uk.codingbadgers.brewarded;

import com.vexsoftware.votifier.model.Vote;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import uk.codingbadgers.bFundamentals.DatabaseSettings;
import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.module.Module;
import uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase;
import uk.thecodingbadgers.bDatabaseManager.DatabaseTable.DatabaseTable;
import uk.thecodingbadgers.bDatabaseManager.bDatabaseManager;
import uk.thecodingbadgers.bDatabaseManager.bDatabaseManager.DatabaseType;

/**
 * The Class bRewarded.
 * Main entry point to the module
 */
public class bRewarded extends Module {
	
	/**
	 * The economy instance
	 */
	private Economy economy = null;
	
	/**
	 * The amount to reward players every time they vote
	 */
	private double voteRewardAmount = 10000.0f;
	
	/**
	 * The amount to reward players for voting on 10 different services in a day
	 */
	private double voteRewardBonusAmount = 100000.0f;
	
	/**
	 * 
	 */
	private List<Integer> voteRewardBonusLevels = new ArrayList<Integer>();
	
	/**
	 * 
	 */
	private List<ItemStack> rewardItemsCommon = new ArrayList<ItemStack>();
	
	/**
	 * 
	 */
	private List<ItemStack> rewardItemsRare = new ArrayList<ItemStack>();
	
	/**
	 * 
	 */
	private List<ItemStack> rewardItemsSuperRare = new ArrayList<ItemStack>();
	
	/**
	 * 
	 */
	private Double rewardItemsCommonChance = 0.9;
	
	/**
	 * 
	 */
	private Double rewardItemsRareChance = 0.09;
	
	/**
	 * 
	 */
	private Double rewardItemsSuperRareChance = 0.01;
	
	/**
	 * 
	 */
	private BukkitDatabase database = null;
	
	/**
	 * 
	 */
	private DatabaseTable voteTable = null;
	
	/**
	 * 
	 */
	final private OutputMessages message = new OutputMessages();
	
	/**
	 * This is called when the module is unloaded
	 */
	@Override
	public void onDisable() {
		log(Level.INFO,  getName() + " version " + getVersion() + " disabled.");
	}

	/**
	 * Called when the module is loaded.
	 * Allowing us to register the player and block listeners
	 */
	@Override
	public void onEnable() {
		
		loadLanguageFile();
		loadConfig();
		
		this.economy = bFundamentals.getEconomy();
		this.database = bDatabaseManager.createDatabase("BadgerNetwork", bFundamentals.getInstance(), DatabaseType.SQL);
		
		DatabaseSettings settings = bFundamentals.getConfigurationManager().getDatabaseSettings();		
		if (!this.database.login(settings.host, settings.user, settings.password, settings.port)) {
			// disable logging
			this.database = null;
			this.voteTable = null;
		}
		else {
			this.voteTable = this.database.createTable("bRewarded-Votes", VoteTableData.class);
		}
		
		register(new RewardedVotifierListener(this));
		
		this.message.messageAnnounceOther = this.getLanguageValue("ANNOUNCE-VOTE-OTHERS");
		this.message.messageAnnounce = this.getLanguageValue("ANNOUNCE-VOTE-TO-PLAYER");
		this.message.messageAnnounceOtherRewardAmount = this.getLanguageValue("ANNOUNCE-VOTE-REWARD-AMOUNT-OTHERS");
		this.message.messageAnnounceRewardAmount = this.getLanguageValue("ANNOUNCE-VOTE-REWARD-AMOUNT");
		this.message.messageAnnounceOtherRewardBonus = this.getLanguageValue("ANNOUNCE-VOTE-REWARD-BONUS-OTHERS");
		this.message.messageAnnounceRewardBonus = this.getLanguageValue("ANNOUNCE-VOTE-REWARD-BONUS");
		this.message.messageAnnounceRandomReward = this.getLanguageValue("ANNOUNCE-REWARD-REWARD");
		
		log(Level.INFO,  getName() + " version " + getVersion() + " enabled.");
	}
	
	/**
	 * 
	 */
	public void loadConfig() {
		
		File confFile = new File(getDataFolder(), "config.yml");
		if (!confFile.exists()) {
			createDefaultConfig(confFile);
		}
		
		FileConfiguration config = this.getConfig();
		this.voteRewardAmount = config.getDouble("vote.reward.amount");
		this.voteRewardBonusAmount = config.getDouble("vote.reward.bonus.amount");
		this.voteRewardBonusLevels = config.getIntegerList("vote.reward.bonus.levels");
		
		List<String> commonRewards = config.getStringList("vote.bonus.items.common.items");		
		List<String> rareRewards = config.getStringList("vote.bonus.items.rare.items");
		List<String> superRareRewards = config.getStringList("vote.bonus.items.superrare.items");
		
		for (String reward : commonRewards) {
			ItemStack material = parseMaterial(reward);
			if (material != null) {
				this.rewardItemsCommon.add(material);
				this.log(Level.INFO, "Loaded: " + reward);
			}
		}
		
		for (String reward : rareRewards) {
			ItemStack material = parseMaterial(reward);
			if (material != null) {
				this.rewardItemsRare.add(material);
				this.log(Level.INFO, "Loaded: " + reward);
			}
		}
		
		for (String reward : superRareRewards) {
			ItemStack material = parseMaterial(reward);
			if (material != null) {
				this.rewardItemsSuperRare.add(material);
				this.log(Level.INFO, "Loaded: " + reward);
			}
		}	
		
		this.rewardItemsCommonChance = config.getDouble("vote.bonus.items.common.chance");
		this.rewardItemsRareChance = config.getDouble("vote.bonus.items.rare.chance");
		this.rewardItemsSuperRareChance = config.getDouble("vote.bonus.items.superrare.chance");
	}
	
	/**
	 * 
	 */
	private void createDefaultConfig(File file) {
		
		FileConfiguration config = this.getConfig();

		config.set("vote.reward.amount", 25000);
		config.set("vote.reward.bonus.amount", 50000);
		
		List<Integer> bonusLevels = new ArrayList<Integer>();
		bonusLevels.add(4);	bonusLevels.add(8); bonusLevels.add(12);
		config.set("vote.reward.bonus.levels", bonusLevels);
		
		// common items
		List<String> commonRewards = new ArrayList<String>();
		commonRewards.add(Material.IRON_CHESTPLATE.name());
		commonRewards.add(Material.IRON_INGOT.name() + ":" + 16);		
		commonRewards.add(Material.COAL.name() + ":" + 64);	
		config.set("vote.bonus.items.common.items", commonRewards);
		config.set("vote.bonus.items.common.chance", 0.9);
		
		// rare items
		List<String> rareRewards = new ArrayList<String>();
		rareRewards.add(Material.DIAMOND.name());	
		rareRewards.add(Material.GOLD_INGOT.name() + ":" + 64);	
		config.set("vote.bonus.items.rare.items", rareRewards);
		config.set("vote.bonus.items.rare.chance", 0.09);
		
		// superrare items
		List<String> superRareRewards = new ArrayList<String>();	
		superRareRewards.add(Material.DIAMOND_CHESTPLATE.name());		
		config.set("vote.bonus.items.superrare.items", superRareRewards);
		config.set("vote.bonus.items.superrare.chance", 0.01);
		
		try {
			config.save(file);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @param materialName
	 * @return 
	 */
	private ItemStack parseMaterial(String materialName) {
		
		// Format: <id>
		// Format: <id>:<quantity>
		// Format: <id>:<quantity>:<data>
		// Format: <id>:<quantity>:<data>:<name>
		
		String[] parts = materialName.split(":");
		
		Material material = null;
		
		try {
			material = Material.getMaterial(parts[0]);
		} catch (Exception ex) {
			try {
				material = Material.valueOf(parts[0]);
			} catch (Exception ex2) {
				return null;
			}
		}
		
		if (parts.length == 1) {
			return new ItemStack(material);
		}
		
		int quantity = 1;
		try {
			quantity = Integer.parseInt(parts[1]);
		}
		catch (Exception ex) {
			return null;
		}
		
		if (parts.length == 2) {
			ItemStack item = new ItemStack(material);
			item.setAmount(quantity);
			return item;
		}
		
		byte data = 0;
		try {
			data = Byte.parseByte(parts[2]);
		}
		catch (Exception ex) {
			return null;
		}
		
		if (parts.length == 3) {
			ItemStack item = new ItemStack(material);
			item.setAmount(quantity);
			MaterialData matData = item.getData();
			matData.setData(data);
			item.setData(matData);
			return item;
		}
		
		String name = parts[3];
		if (parts.length == 4) {
			ItemStack item = new ItemStack(material);
			item.setAmount(quantity);
			MaterialData matData = item.getData();
			matData.setData(data);
			item.setData(matData);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(name);
			item.setItemMeta(itemMeta);			
			return item;
		}
		
		return null;
		
	}
	
	/**
	 * Log a vote to a database
	 * @param vote The vote to log
	 */
	public void logVote(final Vote vote) {
		
		if (this.voteTable != null) {
			VoteTableData newVote = new VoteTableData();
			newVote.server = Bukkit.getServerName();
			newVote.service = vote.getServiceName();
			newVote.username = vote.getUsername();
			newVote.timestamp = System.currentTimeMillis();
			this.voteTable.insert(newVote, VoteTableData.class, true);
		}
		
	}
	
	/**
	 * Pay the person who voted
	 * @param vote The vote to process
	 */
	public void payVotee(final Vote vote) {

		if (this.economy != null) {
			final String user = vote.getUsername();
			this.economy.depositPlayer(user, this.voteRewardAmount);
			output(
				vote, 
				formatMessage(this.message.messageAnnounceOtherRewardAmount, vote, this.voteRewardAmount), 
				formatMessage(this.message.messageAnnounceRewardAmount, vote, this.voteRewardAmount)
			);
			
			int votesToday = getNumberOfServicesToday(user);
			
			final OfflinePlayer player = Bukkit.getOfflinePlayer(user);
			if (player.isOnline()) {
				String site = votesToday == 1 ? "site" : "sites";
				Module.sendMessage(this.getName(), player.getPlayer(), "You have voted on " + votesToday + " " + site + " in the past 24 hours...");
			}
			
			for (int bonusLevel : voteRewardBonusLevels) {
				if (votesToday == bonusLevel) {
					this.economy.depositPlayer(user, this.voteRewardBonusAmount);
					output(
						vote, 
						formatMessage(this.message.messageAnnounceOtherRewardBonus, vote, this.voteRewardBonusAmount), 
						formatMessage(this.message.messageAnnounceRewardBonus, vote, this.voteRewardBonusAmount)
					);
					break;
				}
			}
		}
		
	}
	
	/**
	 * Give the person who voted a random reward
	 * @param vote The vote to process
	 */
	public void rewardVotee(final Vote vote) {
		
		Double[] tiers = new Double[] 
		{
			this.rewardItemsCommonChance, 
			this.rewardItemsRareChance, 
			this.rewardItemsSuperRareChance
		};
		
		Double totalChance = 0.0;
		for (Double tier : tiers) {
			totalChance += tier;
		}
		
		Random random = new Random();
		
		Double randomTier = random.nextDouble() * totalChance;
		Double currentTierLevel = 0.0;
		int tierIndex = 0;
		
		for (Double tier : tiers) {
			if (randomTier < tier) {
				break;
			}
			currentTierLevel += tier;
			tierIndex++;
		}
		
		List<ItemStack> items = null;
		
		// Common
		if (tierIndex == 0) {
			items = this.rewardItemsCommon;
		}
		// Rare
		else if (tierIndex == 1) {
			items = this.rewardItemsRare;
		}
		// Super Rare
		else if (tierIndex == 2) {
			items = this.rewardItemsSuperRare;
		}
		
		if (items == null) {
			items = this.rewardItemsCommon;
		}
		
		if (items.isEmpty()) {
			return;
		}
		
		int itemIndex = random.nextInt(items.size());
		
		ItemStack item = items.get(itemIndex).clone();
		
		OfflinePlayer player = Bukkit.getOfflinePlayer(vote.getUsername());
		if (player.isOnline()) {
			Player onlinePlayer = player.getPlayer();
			onlinePlayer.getInventory().addItem(item);
			onlinePlayer.updateInventory();
			
			Module.sendMessage(this.getName(), player.getPlayer(), formatRandomReward(item, vote));
		}
		
	}
	
	/**
	 * Announce a vote to the server network
	 * @param vote The vote to process
	 */
	public void announceVote(final Vote vote) {
		
		output(vote, formatMessage(this.message.messageAnnounceOther, vote, 0.0), formatMessage(this.message.messageAnnounce, vote, 0.0));
		
	}
	
	/**
	 * 
	 * @param vote
	 * @param othersMessage
	 * @param playerMessage 
	 */
	private void output(final Vote vote, String othersMessage, String playerMessage) {
		
		final OfflinePlayer player = Bukkit.getOfflinePlayer(vote.getUsername());
		
		// Tell the server
		for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
			if (otherPlayer.getName().equalsIgnoreCase(player.getName())) {
				continue;
			}
			Module.sendMessage(this.getName(), otherPlayer, othersMessage);
		}

		// Tell the player if they are online
		if (player.isOnline()) {
			Module.sendMessage(this.getName(), player.getPlayer(), playerMessage);
		}
		
	}
	
	/**
	 * 
	 * @param playerName
	 * @return 
	 */
	private int getNumberOfServicesToday(String playerName) {
		
		if (this.voteTable != null) {
			
			final long time24h = 86400000;
			final long minTime = System.currentTimeMillis() - time24h;
			
			ResultSet result = this.database.queryResult(
				"SELECT * FROM `bRewarded-Votes` WHERE `username`='" 
				+ playerName + "' AND `server`='" 
				+ Bukkit.getServerName() 
				+ "' AND `timestamp`>=" + minTime
			);
			
			if (result != null) {
				
				int votesToday = 0;
				try {
					while (result.next()) {
						votesToday++;
					}
				}
				catch (SQLException ex) {
					bFundamentals.log(Level.WARNING, "Failed to count todays votes for '" + playerName + "'.", ex);
				}
				return votesToday;				
			}			
		}
		
		return 0;
		
	}

	/**
	 * 
	 * @param item
	 * @return 
	 */
	private String formatRandomReward(ItemStack item, Vote vote) {
		String message = formatMessage(this.message.messageAnnounceRandomReward, vote, 0.0);
		
		String itemName = item.getAmount() + "x ";
		if (item.getItemMeta().hasDisplayName()) {
			itemName += item.getItemMeta().getDisplayName();
		}
		else {
			itemName += item.getType().name();
		}
		
		message = message.replaceAll("<<item>>", itemName);
		return message;
	}
	
	/**
	 * 
	 * @param message
	 * @param vote
	 * @param amount
	 * @return 
	 */
	private String formatMessage(String message, Vote vote, Double amount) {
		
		message = message.replaceAll("<<player>>", vote.getUsername());
		message = message.replaceAll("<<servername>>", Bukkit.getServerName());
		message = message.replaceAll("<<service>>", vote.getServiceName());
		message = message.replaceAll("<<amount>>", formatAmount(amount));
		
		return message;
	}
	
	/**
	 * 
	 * @param amount
	 * @return 
	 */
	private String formatAmount(double amount) {
		DecimalFormat format = new DecimalFormat("##0.00");
		String formatted = format.format(amount);

		if (formatted.endsWith(".")) {
			formatted = formatted.substring(0, formatted.length() - 1);
		}
		
		return formatted;
	}
}
