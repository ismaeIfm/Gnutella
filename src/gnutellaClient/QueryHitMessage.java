package gnutellaClient;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;

/**
 * Class that defines a QueryHitMessage defined in Gnutella Protocol v0.4. The
 * response to a Query.
 * 
 * @author Ismael Fernandez
 * @author Miguel Vilchis
 * 
 * @version 1.0
 */
public class QueryHitMessage extends Message {

	private byte numberOfHits;
	private BigInteger port;
	private InetAddress myIpAddress;
	private BigInteger speed;
	private BigInteger fileIndex[];
	private BigInteger fileSize[];
	private String fileName[];
	private byte[] idServent;

	/**
	 * Creates a QueryHitMessage with the specified idMessage, ttl, hop, payload
	 * length, receptor node, number of hits, port, a InetAddress ip, speed,
	 * file Index, file Size, fileName, id Servent
	 * 
	 * @param idMessage
	 *            A 16-byte string uniquely identifying the descriptor on the
	 *            network
	 * @param ttl
	 *            Time to live. The number of times the descriptor will be
	 *            forwarded by Gnutella servents before it is removed from the
	 *            network
	 * @param hop
	 *            The number of times the descriptor has been forwarded
	 * @param paytloadL
	 *            The length of the descriptor immediately following this
	 *            header.
	 * @param receptorNode
	 *            Id of the thread that received the message
	 * @param numberOfHits
	 *            The number of query hits in the result set
	 * @param port
	 *            The port number on which the responding host can accept
	 *            incoming connections.
	 * @param The
	 *            IP address of the responding host.
	 * @param speed
	 *            The speed (in kB/second) of the responding host.
	 * 
	 * @param fileIndex
	 *            A number, assigned by the responding host, which is used to
	 *            uniquely identify the file matching the corresponding query.
	 * 
	 * 
	 * @param fileSize
	 *            The size (in bytes) of the file whose index is File_Index
	 * @param fileName
	 *            The double-nul (i.e. 0x0000) terminated name of the file whose
	 *            index is File_Index.
	 * 
	 * @param idServent
	 *            A 16-byte string uniquely identifying the responding servent
	 *            on the network. This is typically some function of the
	 *            servent’s network address.
	 */

	public QueryHitMessage(byte[] idMessage, byte ttl, byte hop, int paytloadL,
			InetSocketAddress receptorNode, byte numberOfHits, short port,
			InetAddress myIpAddress, int speed, int fileIndex[],
			int fileSize[], String fileName[], byte[] idServent) {
		super(idMessage, GnutellaConstants.QUERY_HIT, ttl, hop, paytloadL,
				receptorNode);

		this.numberOfHits = numberOfHits;
		this.port = new BigInteger(port + "");
		this.myIpAddress = myIpAddress;
		this.speed = new BigInteger(speed + "");
		this.fileIndex = new BigInteger[numberOfHits];
		int i = 0;
		for (int a : fileIndex) {
			this.fileIndex[i++] = new BigInteger(a + "");
		}
		this.fileSize = new BigInteger[numberOfHits];
		i = 0;
		

		for (int a : fileSize) {
			this.fileSize[i++] = new BigInteger(a + "");
		}

		this.fileName = fileName;
		this.idServent = idServent;

	}

	/**
	 * Creates a QueryHitMessage with the specified idMessage, ttl, hop, payload
	 * length, receptor node, number of hits, port, a InetAddress ip, speed,
	 * file Index, file Size, fileName, id Servent
	 * 
	 * @param idMessage
	 *            A 16-byte string uniquely identifying the descriptor on the
	 *            network
	 * @param ttl
	 *            Time to live. The number of times the descriptor will be
	 *            forwarded by Gnutella servents before it is removed from the
	 *            network
	 * @param hop
	 *            The number of times the descriptor has been forwarded
	 * @param paytloadL
	 *            The length of the descriptor immediately following this
	 *            header.
	 * @param receptorNode
	 *            Id of the thread that received the message
	 * @param numberOfHits
	 *            The number of query hits in the result set
	 * @param port
	 *            The port number on which the responding host can accept
	 *            incoming connections.
	 * @param The
	 *            IP address of the responding host.
	 * @param speed
	 *            The speed (in kB/second) of the responding host.
	 * 
	 * @param fileIndex
	 *            A number, assigned by the responding host, which is used to
	 *            uniquely identify the file matching the corresponding query.
	 * 
	 * 
	 * @param fileSize
	 *            The size (in bytes) of the file whose index is File_Index
	 * @param fileName
	 *            The double-nul (i.e. 0x0000) terminated name of the file whose
	 *            index is File_Index.
	 * 
	 * @param idServent
	 *            A 16-byte string uniquely identifying the responding servent
	 *            on the network. This is typically some function of the
	 *            servent’s network address.
	 */
	public QueryHitMessage(byte[] idMessage, byte ttl, byte hop, int paytloadL,
			InetSocketAddress receptorNode, byte numberOfHits, byte[] port,
			InetAddress myIpAddress, byte[] speed, byte[][] fileIndex,
			byte[][] fileSize, byte[][] fileName, byte[] idServent) {
		super(idMessage, GnutellaConstants.QUERY_HIT, ttl, hop, paytloadL,
				receptorNode);

		this.numberOfHits = numberOfHits;
		this.port = new BigInteger(port);
		this.myIpAddress = myIpAddress;
		this.speed = new BigInteger(speed);
		this.fileIndex = new BigInteger[numberOfHits];
		this.fileSize = new BigInteger[numberOfHits];
		this.fileName = new String[numberOfHits];

		for (int k = 0; k < numberOfHits; k++) {

			this.fileIndex[k] = new BigInteger(fileIndex[k]);

			this.fileSize[k] = new BigInteger(fileSize[k]);

			this.fileName[k] = new String(fileName[k]);

		}
		this.idServent = idServent;
		

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Message#toByteArray()
	 */
	public byte[] toByteArray() {
		int totalLen = 0;
		totalLen += GnutellaConstants.HEADER_LENGTH;
		totalLen += numberOfHits * GnutellaConstants.QUERYHIT_PART_L;
		totalLen += GnutellaConstants.SERVER_ID_L;
		totalLen += numberOfHits * GnutellaConstants.EOS_L;

		for (String tmp : fileName) {
			totalLen += tmp.length();
		}
		int i = 0;
		byte queryHit[] = new byte[totalLen];
		byte superTmp[] = super.toByteArray();
		for (byte a : superTmp) {
			queryHit[i++] = a;
		}

		queryHit[i++] = numberOfHits;

		if (port.toByteArray().length < 2) {
			queryHit[i++] = 0;
		}
		for (byte a : port.toByteArray()) {
			queryHit[i++] = a;

		}
		for (byte a : myIpAddress.getAddress()) {
			queryHit[i++] = a;
		}

		int speedL = speed.toByteArray().length;
		int tmpL = 4 - speedL;
		int j = 0;
		byte popo[] = new byte[4];
		while (j < tmpL) {
			queryHit[i] = 0;
			
			i++;
			++j;
		}

		for (byte a : speed.toByteArray()) {
			queryHit[i++] = a;
			
		}
	
		for (int k = 0; k < numberOfHits; k++) {

			int fileIndexL = fileIndex[k].toByteArray().length;
			tmpL = 4 - fileIndexL;
			j = 0;

			while (j < tmpL) {
				queryHit[i++] = 0;
				
				j++;
			}
			for (byte a : fileIndex[k].toByteArray()) {
				queryHit[i++] = a;
				
			}
		

			int fileSizeL = fileSize[k].toByteArray().length;
			tmpL = 4 - fileSizeL;
			j = 0;

			while (j < tmpL) {
				queryHit[i++] = 0;
				
				j++;
			}

			for (byte a : fileSize[k].toByteArray()) {
				queryHit[i++] = a;
				popo[j++] = a;
			}
			
			for (char c : fileName[k].toCharArray()) {
				queryHit[i++] = (byte) c;
			}
			queryHit[i++] = GnutellaConstants.END;
		}

		for (byte a : idServent) {
			queryHit[i++] = a;
		}

		return queryHit;

	}

	/**
	 * Returns the number of hits
	 * 
	 * @return number of hits
	 */
	public byte getNumberOfHits() {
		return numberOfHits;
	}

	/**
	 * Returns the number of port
	 * 
	 * @return the port
	 */
	public short getPort() {
		return port.shortValue();
	}

	/**
	 * Returns the ip address
	 * 
	 * @return the ip address
	 */
	public InetAddress getMyIpAddress() {
		return myIpAddress;
	}

	/**
	 * Returns the speed
	 * 
	 * @return the speed
	 */
	public int getSpeed() {
		return speed.intValue();
	}

	/**
	 * Returns the fileIndex
	 * 
	 * @return the fileIndex
	 */
	public int[] getFileIndex() {
		int fileIndex[] = new int[numberOfHits];
		int i = 0;
		for (BigInteger a : this.fileIndex) {
			fileIndex[i++] = a.intValue();
		}
		return fileIndex;
	}

	/**
	 * Returns the file size
	 * 
	 * @return the file size
	 */
	public int[] getFileSize() {
		int fileSize[] = new int[numberOfHits];
		int i = 0;
		for (BigInteger a : this.fileSize) {
			fileSize[i++] = a.intValue();
		}
		return fileSize;

	}

	/**
	 * Returns the file name
	 * 
	 * @return the file name
	 */
	public String[] getFileName() {

		return this.fileName;
	}

	/**
	 * Returns id Servent
	 * 
	 * @return id Servent
	 */
	public byte[] getIdServent() {
		return idServent;
	}

	/* (non-Javadoc)
	 * @see Message#toString()
	 */
	public String toString() {
		String query = "";
		query += super.toString() + "\n";
		query += numberOfHits + "|";
		query += port + "|";
		query += speed + "|";
		int tmpL, j;
		for (int k = 0; k < numberOfHits; k++) {

			int fileIndexL = fileIndex[k].toByteArray().length;
			tmpL = 4 - fileIndexL;
			j = 0;

			while (j < tmpL) {
				query += 0;
				j++;
			}
			for (byte a : fileIndex[k].toByteArray()) {
				query += a;
			}
			query += "|";
			int fileSizeL = fileSize[k].toByteArray().length;
			tmpL = 4 - fileSizeL;
			j = 0;

			while (j < tmpL) {
				query += 0;
				j++;
			}

			for (byte a : fileSize[k].toByteArray()) {
				query += a;
			}
			query += "|";
			for (char c : fileName[k].toCharArray()) {
				query += c;
			}
			query += GnutellaConstants.END;
			query += "|";
		}

		return query;
	}

}
