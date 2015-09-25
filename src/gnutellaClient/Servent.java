package gnutellaClient;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Client of Gnutella network. Protocol v0.4 Features: +Connect to the network
 * +Share files +Answer to querys +Search and download. For downloads uses
 * protocol HTTP
 * 
 * @author Ismael Fernandez
 * @author Miguel Vilchis
 * @see http://www.stanford.edu/class/cs244b/gnutella_protocol_0.4.pdf
 * @see https 
 *      ://www.dropbox.com/sh/yyzy5e48qk7p5h0/EtzNfXlzAO/comp-dist-unam-proyecto
 *      -diapos.pdf
 * @version 2.0
 * 
 */

public class Servent {
	private InetAddress ipAddress;
	private short myPort;
	private Server myServer;
	private Client myClient;
	private ConcurrentHashMap<InetSocketAddress, ServentThread> neighbors;
	private ConcurrentHashMap<InetSocketAddress, ServentThread> downloads;
	private ConcurrentHashMap<String, InetSocketAddress> historyPing;
	private ConcurrentHashMap<String, InetSocketAddress> historyQuery;
	private ArrayList<QueryHitMessage> queryHitMessage;
	private IdGenerator myIdGenerator;
	private ConcurrentLinkedQueue<Message> pendingMessages;
	private File myDirectory;

	public ArrayList<QueryHitMessage> getQueryHitMessage() {
		return queryHitMessage;
	}

	/**
	 * Constructs a Servent that listens for upcoming connections in the
	 * specified port, and shares files of the especified directoryPath
	 * 
	 * @param pathName
	 *            Directory Path for sharinf files
	 * @throws IOException
	 *             IO error when opening the socket in which this Servent
	 *             listens for upcoming connections
	 */
	public Servent(String pathName) throws IOException {

		this.myPort = generateRandomPort();

		neighbors = new ConcurrentHashMap<InetSocketAddress, ServentThread>();
		downloads = new ConcurrentHashMap<InetSocketAddress, ServentThread>();
		historyPing = new ConcurrentHashMap<String, InetSocketAddress>();
		historyQuery = new ConcurrentHashMap<String, InetSocketAddress>();
		queryHitMessage = new ArrayList<QueryHitMessage>();

		pendingMessages = new ConcurrentLinkedQueue<Message>();
		ipAddress = InetAddress.getLocalHost();

		myIdGenerator = new IdGenerator();

		myDirectory = new File(pathName);

		this.myClient = new Client(myPort, neighbors, historyPing,
				historyQuery, pendingMessages, ipAddress, myIdGenerator,
				myDirectory, IdGenerator.getIdServent(), downloads,
				queryHitMessage);
		this.myServer = new Server(myPort, neighbors, historyPing,
				historyQuery, pendingMessages, myIdGenerator, downloads,
				myDirectory);
		new Thread(myClient).start();
		new Thread(myServer).start();

	}

	public String getMyDirectoryName() {
		return myDirectory.getName();
	}

	/**
	 * Connects to a Gnutella client with the specified ip and port
	 * 
	 * @param ip
	 *            The ip in string format of the Gnutella client
	 * @param port
	 *            The port in which the Gnutella client listens
	 * @return true if the connection is made succesfully, false otherwise
	 */
	public boolean connect(String ip, short port) {
		if (neighbors.size() > myClient.getMaxNodes()) {
			myClient.setMaxNodes();
		}
		boolean connection = myClient.connect(ip, port);
		myServer.setMaxNodes();
		return connection;
	}

	/**
	 * Used to actively discover hosts on the network
	 */
	public void makePing() {
		myClient.addAPing();
	}

	/**
	 * The primary mechanism for searching the distributed network.
	 * 
	 * @param searchCriteria
	 *            the name of file for the search
	 */
	public void makeQuery(String searchCriteria) {
		myClient.addAQuery(GnutellaConstants.DFLTMIN_SPEED, searchCriteria);
	}

	/**
	 * Establishes a connection to download the specified file from a specified
	 * Server
	 * 
	 * @param ip
	 *            The ip in string format of the Server
	 * @param port
	 *            The port in which the Server listens for upcoming connections
	 * @param file
	 *            Name of the file that for searching
	 * @param size
	 *            Size of the file for the search
	 */
	public void makeDownload(String ip, short port, String file, int size) {
		myClient.download(ip, port, file, size, 0);
	}



	/**
	 * List the current neighbors
	 */
	public void myNeighbors() {
		Enumeration<InetSocketAddress> e = neighbors.keys();
		System.out.println("\n NEIGHBORS AT THE MOMENT:  \n");
		System.out.print("( ");
		while (e.hasMoreElements()) {
			InetSocketAddress idNodeNext = e.nextElement();
			System.out.print(idNodeNext.getAddress() + "-"
					+ idNodeNext.getPort() + ", ");
		}
		System.out.println(")");

	}

	/**
	 * Close all connections
	 */
	public void close() {

		Enumeration<InetSocketAddress> n = neighbors.keys();
		while (n.hasMoreElements()) {
			InetSocketAddress idN = n.nextElement();
			neighbors.get(idN).close();
			neighbors.remove(idN);
		}
		Enumeration<InetSocketAddress> d = downloads.keys();
		while (d.hasMoreElements()) {
			InetSocketAddress idN = d.nextElement();
			downloads.get(idN).close();
			downloads.remove(idN);
		}
		System.exit(0);

	}

	private short generateRandomPort() {
		Random r = new Random();
		short port = (short) (r.nextInt(Short.MAX_VALUE)
				% (Short.MAX_VALUE - GnutellaConstants.MIN_PORT) + GnutellaConstants.MIN_PORT);
		return port;
	}

	/**
	 * Returns the port in which this Servent is listening
	 * 
	 * @return the port
	 */
	public short getMyPort() {
		return myPort;
	}

}
