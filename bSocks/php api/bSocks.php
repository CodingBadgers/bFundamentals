<!--
	This file is used as part of the bFundamentals module bSocks.
	Allowing php socket connections to exist between a minecraft server and a webserver.
	To use simply include this php file into the file you wish to utalize this functionallity.
	
	Copywrite TheCodingBadgers 2012
-->
<?php
	class bSocks {
	
		/** The port to send and recieve socket connections on **/
		private $m_port;
		
		/** The ip or domainname to connect too **/
		private $m_host;
		
		/** An md5 hash of the required connection password **/
		private $m_passwordHash;
		
		/** The socket connection to the game server **/
		private $m_sock;
		
		/**
		*	Construct the bSocks php api object taking the required
		*	host, port and plain text password.
		**/
		public function __construct($host, $port, $password) {
			$this->$m_host = $host;
			$this->$m_port = $port;
			$this->$m_passwordHash = md5($password);
		}
		
		/**
		*	Create a connection to the remote game server using the given
		* 	host name and port. This should not be called externally.
		**/
		private function connectSocket() {
			$this->m_sock = socket_create(AF_INET, SOCK_STREAM, 0) //Creating a TCP socket
			or die("error: could not create socket\n");
			
			socket_connect($this->m_sock, $this->m_host, $this->m_port) //Connecting to to server using that socket
			or die("error: could not connect to host\n");				
		}
	
		/**
		*	Send a message to a given player
		* 	$playerName - The players name to whom we should send the message
		*	$message - The message to send to the player
		**/
		public function sendMessage($playername, $message) {
			$json = json_encode(array('password' => $this->$m_passwordHash, 'type' => 'sendMessage', 'playerName' => $playername, 'context' => $message));
			$this->connectSocket();
			
			socket_write($this->m_sock, $json . "\n", strlen($json) + 1)
			or die("error: failed to send message to all\n");
		}

		/**
		*	Send a message to all online players
		*	$message - The message to send to the players
		**/		
		public function sendMessageAll($message) {
			$json = json_encode(array('password' => $this->$m_passwordHash, 'type' => 'sendMessageAll', 'context' => $message));
			$this->connectSocket();
			
			socket_write($this->m_sock, $json . "\n", strlen($json) + 1)
			or die("error: failed to send message to all\n");
		}
	
		/**
		*	Send a message to all online players, exculding one player
		*	$message - The message to send to the player
		* 	$excludedPlayer - The name of a player whom should not recieve the message
		**/
		public function sendMessageAllEx($message, $excludedPlayer) {
			$json = json_encode(array('password' => $this->$m_passwordHash, 'type' => 'sendMessageAllEx', 'exludedPlayer' => $excludedPlayer, 'context' => $message));
			$this->connectSocket();
			
			socket_write($this->m_sock, $json . "\n", strlen($json) + 1)
			or die("error: failed to send message to all\n");
		}
	
	};
?>