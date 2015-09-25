package gnutellaClient;
import java.math.BigInteger;
import java.net.InetSocketAddress;

/**
 * Class that represents the structure of the header of a Message specified in
 * the Gnutella Protocol v0.4
 * 
 * @author Ismael Fernandez
 * @author Miguel Vilchis
 * @version 2.0
 * @see
 * 
 */

public class Message {
	private BigInteger idMessage;
	private byte payloadD;
	private byte ttl;
	private byte hop;
	private int payloadL;
	protected InetSocketAddress receptorNode;

	/**
	 * Creates a header used on Gnutella Protocol v0.4
	 * 
	 * @param idMessage
	 *            A 16-byte string uniquely identifying the descriptor on the
	 *            network
	 * @param payloadD
	 *            0x00 = Ping, 0x01 = Pong, 0x40 = Push, 0x80 = Query, 0x81 =
	 *            QueryHit PayLoader Descriptor
	 * @param ttl
	 *            Time to live. The number of times the descriptor will be
	 *            forwarded by Gnutella servents before it is removed from the
	 *            network
	 * @param hop
	 *            The number of times the descriptor has been forwarded
	 * @param payloadL
	 *            The length of the descriptor immediately following this header
	 * @param receptorNode
	 *            Id of the thread that received the message
	 */
	protected Message(byte[] idMessage, byte payloadD, byte ttl, byte hop,
			int payloadL, InetSocketAddress receptorNode) {
		this.idMessage = new BigInteger(idMessage);
		this.payloadD = payloadD;
		this.ttl = ttl;
		this.hop = hop;
		this.payloadL = payloadL;
		this.receptorNode = receptorNode;

	}

	/**
	 * @param idMessage
	 *            A 16-byte string uniquely identifying the descriptor on the
	 *            network
	 * @param payloadD
	 *            0x00 = Ping, 0x01 = Pong, 0x40 = Push, 0x80 = Query, 0x81 =
	 *            QueryHit PayLoader Descriptor
	 * @param ttl
	 *            Time to live. The number of times the descriptor will be
	 *            forwarded by Gnutella servents before it is removed from the
	 *            network
	 * @param hop
	 *            The number of times the descriptor has been forwarded
	 * @param payloadL
	 *            The length of the descriptor immediately following this header
	 * @param receptorNode
	 *            thread that received the message
	 */
	protected Message(byte payloadD, byte ttl, byte hop, int payloadL,
			InetSocketAddress receptorNode) {
		this.idMessage = new BigInteger(IdGenerator.getIdMessage());
		this.payloadD = payloadD;
		this.ttl = ttl;
		this.hop = hop;
		this.payloadL = payloadL;
		this.receptorNode = receptorNode;

	}

	/**
	 * Returns the id of this Message
	 * 
	 * @return the id in a BigInteger representation
	 */
	public BigInteger getIdMessage() {
		return idMessage;
	}

	public boolean refreshMessage() {
		// Hop se incializa en -1 si nosotros creamos el mensaje
		if (hop == GnutellaConstants.MY_MESSAGE) {
			hop++;
			return true;
		}
		if (ttl > 0) {
			hop++;
			ttl--;
			return true;
		}
		return false;
	}

	/**
	 * Returns the payloader descriptor of this Message
	 * 
	 * @return the payloader descriptor in a byte representation
	 */
	public byte getPayloadD() {
		return payloadD;
	}

	/**
	 * Returns the ttl of this Message
	 * 
	 * @return the ttl in a byte representation
	 */
	public byte getTtl() {
		return ttl;
	}

	/**
	 * Returns the hop of this Message
	 * 
	 * @return the hop in a byte representation
	 */
	public byte getHop() {
		return hop;
	}

	/**
	 * Returns the payloader length of this Message
	 * 
	 * @return the payloader length in a BigInteger representation
	 */
	public int getPayloadL() {
		return payloadL;
	}

	/**
	 * Returns the representation of this Message in bytes
	 * 
	 * @return the representation in bytes
	 */
	public byte[] toByteArray() {
		byte header[] = new byte[GnutellaConstants.HEADER_LENGTH];
		byte id[] = getIdMessage().toByteArray();
		int i = 0;
		for (; i < GnutellaConstants.ID_LENGTH; i++) {
			header[i] = id[i];
		}
		header[i++] = getPayloadD();
		header[i++] = getTtl();
		header[i++] = getHop();
		byte pl[] = new BigInteger(getPayloadL() + "").toByteArray();

		i += GnutellaConstants.PLL_LENGTH - pl.length;
		for (int j = 0; j < pl.length; j++) {
			header[i++] = pl[j];

		}
		return header;

	}

	/**
	 * Returns the id of this Message in textual presentation
	 * 
	 * @return the id in a string format
	 */
	public synchronized String idMessageToString() {
		return getIdMessage() + "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {

		return getIdMessage().toString() + "|" + getPayloadD() + "|" + getTtl()
				+ "|" + getHop() + "|" + getPayloadL();
	}

	/**
	 * Returns the InetSocketAddress bound with the thread that received the
	 * message
	 * 
	 * @return the InetSocketAddress
	 */
	public InetSocketAddress getReceptorNode() {
		return receptorNode;
	}
}
