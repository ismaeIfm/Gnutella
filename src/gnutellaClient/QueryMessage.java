package gnutellaClient;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.ArrayList;

/**
 * Class that defines a QueryMessage defined in Gnutella Protocol v0.4
 * 
 * @author Ismael Fernandez
 * @author Miguel Vilchis
 * @version 2.0
 * 
 */
public class QueryMessage extends Message {
	private BigInteger minSpeed;
	private String searchCriteria;

	/**
	 * Creates a QueryHitMessage with the specified idMessage, ttl, hop, payload
	 * length, receptor node, min speed, search criteria
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
	 * @param minSpeed
	 *            The minimum speed (in kB/second) of servents that should
	 *            respond to this message.
	 * @param searchCriteria
	 *            The minimum speed (in kB/second) of servents that should
	 *            respond to this message.
	 */
	public QueryMessage(byte[] idMessage, byte ttl, byte hop, int paytloadL,
			InetSocketAddress receptorNode, short minSpeed,
			String searchCriteria) {
		super(idMessage, GnutellaConstants.QUERY, ttl, hop, paytloadL,
				receptorNode);
		this.minSpeed = new BigInteger(minSpeed + "");
		this.searchCriteria = searchCriteria;
	}

	/**
	 * Creates a QueryHitMessage with the specified idMessage, ttl, hop, payload
	 * length, receptor node, min speed, search criteria
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
	 * @param minSpeed
	 *            The minimum speed (in kB/second) of servents that should
	 *            respond to this message.
	 * @param searchCriteria
	 *            The minimum speed (in kB/second) of servents that should
	 *            respond to this message.
	 */
	public QueryMessage(byte[] idMessage, byte ttl, byte hop, int paytloadL,
			InetSocketAddress receptorNode, byte[] minSpeed,
			String searchCriteria) {
		super(idMessage, GnutellaConstants.QUERY, ttl, hop, paytloadL,
				receptorNode);
		this.minSpeed = new BigInteger(minSpeed);
		this.searchCriteria = searchCriteria;
	}

	/**
	 * * Creates a QueryHitMessage with the specified idMessage, ttl, hop,
	 * payload length, receptor node, min speed, search criteria. The
	 * idMessage is generated random.
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
	 * @param minSpeed
	 *            The minimum speed (in kB/second) of servents that should
	 *            respond to this message.
	 * @param searchCriteria
	 *            The minimum speed (in kB/second) of servents that should
	 *            respond to this message.
	 */
	public QueryMessage(byte ttl, byte hop, int paytloadL,
			InetSocketAddress receptorNode, short minSpeed,
			String searchCriteria) {
		super(GnutellaConstants.QUERY, ttl, hop, paytloadL, receptorNode);
		this.minSpeed = new BigInteger(minSpeed + "");
		this.searchCriteria = searchCriteria;
	}

	/**
	 * Returns the minimum speed (in kB/second) of servents that should respond
	 * to this message
	 * 
	 * @return the minimun speed
	 */
	public short getMinSpeed() {
		return minSpeed.shortValue();
	}

	/**
	 * Returns the search criteria in the query message. A null (i.e. 0x00)
	 * terminated search string. The maximum length of this string is bounded by
	 * the Payload_Length field of the descriptor header.
	 * 
	 * @return the search criteria
	 */
	public String getSearchCriteria() {
		return searchCriteria;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Message#toByteArray()
	 */
	public byte[] toByteArray() {

		byte[] query = new byte[GnutellaConstants.HEADER_LENGTH
				+ searchCriteria.length() + 2 + GnutellaConstants.EOS_L];
		byte[] temp = super.toByteArray();
		int i = 0;
		for (byte b : temp) {
			query[i++] = b;
		}
		if (minSpeed.toByteArray().length < 2) {
			query[i++] = 0;
		}
		for (byte a : minSpeed.toByteArray()) {
			query[i++] = a;

		}
		for (char c : searchCriteria.toCharArray()) {
			query[i++] = (byte) c;
		}
		query[i++] = GnutellaConstants.EOS;
		return query;
	}
}
