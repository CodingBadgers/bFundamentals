/**
 * bSocks 1.2-SNAPSHOT
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
package uk.codingbadgers.bsocks.threading;

import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.bukkit.plugin.java.JavaPlugin;

import uk.codingbadgers.bsocks.bSocksModule;

public class ListenerThread extends Thread {

	/** The plugin instance. */
	final private JavaPlugin m_plugin;
	
	/** The module instance. */
	final private bSocksModule m_module;
	
	/** The listener socket. */
	final private ServerSocket m_listenSock;
	
	/** An md5 hash of the plain text password. */
	final private String m_passHash;

	/** Stores the running state of the listener thread. */
	private boolean m_running = true;

	/**
	 * Instantiates a new listener thread.
	 *
	 * @param plugin the owner plugin
	 * @param listenSock the listen socket
	 * @param password the plain text password
	 * @param module the owner module
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ListenerThread(JavaPlugin plugin, ServerSocket listenSock, String password, bSocksModule module) throws IOException {
		m_listenSock = listenSock;
		m_listenSock.setPerformancePreferences(1, 0, 0);
		
		m_passHash = createMD5Hash(password);
		m_plugin = plugin;
		m_module = module;
	}
	
	/**
	 * Creates an md5 hash of a given string.
	 *
	 * @param word the plain text word to create an md5 hash of
	 * @return the md5 has of the given word
	 */
	private static String createMD5Hash(String word) {
		try {
			// create the md5 digest, which will take the plain text word
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(word.getBytes());
			BigInteger hash = new BigInteger(1, md5.digest());
			
			// return the md5 hash
			return hash.toString(16);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		// an error occurred creating the md5 hash
		return "";
	}
	
	/**
	 * Set the listener thread running
	 */
	public void run() {		
		// while we are running we keep processing socket requests, creating handlers
		// to handle the request.
		while (m_running) {			
			try {
				// this command is blocking, it will wait here until a socket request is accepted
				Socket sock = m_listenSock.accept();
				
				// sanity check on the socket.
				if (!sock.isClosed()) {
					// create a new request handler to handle the new socket request.
					RequestHandler rh = new RequestHandler(m_plugin, m_passHash, sock, m_module);
					rh.start();
				}
			} catch (IOException e) {
				// we could throw an exception, however, often socket connections
				// are created at a fast rate which can produce a lot of console spam.
			}
		}
	}
	
	/**
	 * Kills the listener thread.
	 */
	public void kill() {
		m_running = false;
		
		try {
			m_listenSock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
