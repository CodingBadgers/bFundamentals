/**
 * bFundamentalsBuild 1.2-SNAPSHOT
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
package uk.codingbadgers.blogbackup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.command.CommandSender;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;

public class bLogBackup extends Module {

	/**
	 * Called when the module is disabled.
	 */
	public void onDisable() {
		backupLog();
	}

	/**
	 * Called when the module is loaded.
	 */
	public void onEnable() {
		ModuleCommand backupCommand = new ModuleCommand("logbackup", "/logbackup");
		//backupCommand.("Backup the log file.");
		registerCommand(backupCommand);
	}
	
	/**
	 * Handle the command /logbackup
	 */
	public boolean onCommand(CommandSender sender, String label, String[] args) {
		
		if (!label.equalsIgnoreCase("logbackup"))
			return false;
		
		if (!bFundamentals.getPermissions().has(sender, "bLogBackup.backup"))
			return true;
		
		sender.sendMessage("[bLogBackup] Backing up log file...");
		
		final String backedupLogName = backupLog();
		if (backedupLogName != null) {
			sender.sendMessage("[bLogBackup] Backup complete");
			sender.sendMessage("[bLogBackup] Backup can be found at...");
			sender.sendMessage("[bLogBackup] " + backedupLogName);
		} else {
			sender.sendMessage("[bLogBackup] Backup failed");
		}
		
		return true;
		
	}
	
	/**
	 * Backs up the log file to a date+time log file. and clears the active log file
	 */
	private String backupLog() {
		
		final Date date = new Date();
		final String currentDir = System.getProperty("user.dir");
		final File logFile = new File(currentDir + File.separatorChar + "server.log");
			
		String logFolder = currentDir + File.separatorChar + "logBackups";
				
		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		logFolder += File.separatorChar + dateFormat.format(date);
		
		dateFormat = new SimpleDateFormat("MMMM");
		logFolder += File.separatorChar + dateFormat.format(date);
		
		dateFormat = new SimpleDateFormat("dd");
		logFolder += File.separatorChar + dateFormat.format(date);
		
		final File logBackupFolder = new File(logFolder);
		if (!logBackupFolder.exists())
			logBackupFolder.mkdirs();
		
		dateFormat = new SimpleDateFormat("yyyy-MM-dd.kk-mm-ss");
		final String backupLogName = logFolder + File.separatorChar + "server." + dateFormat.format(date) + ".log";
				
		try {
			copyFile(logFile, new File(backupLogName));
		} catch(Exception ex) {
			return null;
		}
		
		try {
			FileOutputStream erasor = new FileOutputStream(currentDir + File.separatorChar + "server.log");
			erasor.write((new String()).getBytes());
			erasor.close();	
		} catch (IOException e) {

		}
		
		return backupLogName;
	}
	
	/**
	 * Copies a file from a source to a destination
	 */
	private void copyFile(File sourceFile, File destFile) throws IOException {
	    if(!destFile.exists()) {
	        destFile.createNewFile();
	    }

	    FileChannel source = null;
	    FileChannel destination = null;

	    try {
	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();
	        destination.transferFrom(source, 0, source.size());
	    }
	    finally {
	        if(source != null) {
	            source.close();
	        }
	        if(destination != null) {
	            destination.close();
	        }
	    }
	}


}
