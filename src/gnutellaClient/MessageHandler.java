package gnutellaClient;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.net.InetSocketAddress;

/*
 * CLASE TERMINADA EN COPARACION CON LA VERSION ANTERIOR
 */
public class MessageHandler {
	/**
	 * Class that given a inputStream handles with everything that it reads ands
	 * constructs Messages
	 * 
	 * @author Ismael Fernandez
	 * @author Miguel Vilchis
	 * 
	 * @version 2.0
	 */
	private InetSocketAddress receptorNode;

	public MessageHandler(InetSocketAddress receptorNode) {
		this.receptorNode = receptorNode;
	}

	/**
	 * Construct a Message read in the given DataInputStream. The message is
	 * read byte by byte
	 * 
	 * @param inStream
	 *            DataInputStream in which the Message is read in bytes
	 * @return Message of the Gnutella Protocol v0.4
	 */
	public Message getMessage(DataInputStream inStream) {
		ArrayList<Byte> message = new ArrayList<Byte>();
		int idx = 0;

		try {
			while (inStream.available() > 0
					&& idx < GnutellaConstants.HEADER_LENGTH) {
				message.add(inStream.readByte());
				idx++;

			}
			/* Declaracion de lo que almacenara el header del message */
			byte[] idMessage = new byte[GnutellaConstants.ID_LENGTH];
			byte payloadD;
			byte ttl;
			byte hop;
			byte[] payloadL = new byte[GnutellaConstants.PLL_LENGTH];
			int i = 0, j = 0;
			Byte stream[] = new Byte[message.size()];
			message.toArray(stream);

			// Leemos el id
			for (i = 0; i < GnutellaConstants.ID_LENGTH; i++) {
				idMessage[i] = stream[j++];
			}
			// Leemos el payload descriptor

			payloadD = stream[j++];

			// Leemos el ttl
			ttl = stream[j++];

			// Leemos el hop
			hop = stream[j++];

			// Leemos el payload length
			for (i = 0; i < GnutellaConstants.PLL_LENGTH; i++) {
				payloadL[i] = stream[j++];
			}
			switch (payloadD) {
			case GnutellaConstants.PING:
				return new PingMessage(idMessage, ttl, hop, receptorNode);

			case GnutellaConstants.PONG:
				ArrayList<Byte> partPong = new ArrayList<Byte>();
				while (inStream.available() > 0
						&& idx < GnutellaConstants.HEADER_LENGTH
								+ GnutellaConstants.PONG_PLL) {
					partPong.add(inStream.readByte());
					idx++;
				}
				stream = new Byte[partPong.size()];

				partPong.toArray(stream);
				// Declaracion de donde almacenaremos los atributos del mensaje
				// pong
				byte[] port = new byte[GnutellaConstants.PORT_LENGTH];
				byte[] ip = new byte[GnutellaConstants.IP_LENGTH];
				byte[] nfilesh = new byte[GnutellaConstants.NF_LENGTH];
				byte[] nkbsh = new byte[GnutellaConstants.NK_LENGTH];
				j = 0;
				// Llenamos cada campo con lo que habia en el stream
				for (i = 0; i < GnutellaConstants.PORT_LENGTH; i++) {
					port[i] = stream[j++];
				}
				for (i = 0; i < GnutellaConstants.IP_LENGTH; i++) {
					ip[i] = stream[j++];
				}
				for (i = 0; i < GnutellaConstants.NF_LENGTH; i++) {
					nfilesh[i] = stream[j++];
				}

				for (i = 0; i < GnutellaConstants.NK_LENGTH; i++) {
					nkbsh[i] = stream[j++];
				}
				/* Convertimos el arreglo de byte del puerto a short */

				return new PongMessage(idMessage, ttl, hop, receptorNode, port,
						ip, nfilesh, nkbsh);
			case GnutellaConstants.QUERY:

				ArrayList<Byte> partQuery = new ArrayList<Byte>();

				while (inStream.available() > 0) {
					byte b = inStream.readByte();
					partQuery.add(b);

					idx++;
				}

				stream = new Byte[partQuery.size()];
				partQuery.toArray(stream);
				// Declaracion de donde almacenaremos los atributos del mensaje
				// pong
				byte[] minSpeed = new byte[GnutellaConstants.MINSPEEDL];

				int searchCriteriaL = partQuery.size()
						- GnutellaConstants.MINSPEEDL - GnutellaConstants.EOS_L;

				byte[] searchCriteria = new byte[searchCriteriaL];

				j = 0;
				// Llenamos cada campo con lo que habia en el stream
				for (i = 0; i < GnutellaConstants.MINSPEEDL; i++) {
					minSpeed[i] = stream[j++];
				}
				for (i = 0; i < searchCriteriaL; i++) {
					searchCriteria[i] = stream[j++];
				}
				return new QueryMessage(idMessage, ttl, hop, searchCriteriaL,
						receptorNode, minSpeed, new String(searchCriteria));

			case GnutellaConstants.QUERY_HIT:
				ArrayList<Byte> partQueryH = new ArrayList<Byte>();

				while (inStream.available() > 0) {
					byte b = inStream.readByte();
					partQueryH.add(b);

					idx++;
				}

				stream = new Byte[partQueryH.size()];

				partQueryH.toArray(stream);
				// Declaracion de donde almacenaremos los atributos del mensaje
				// pong
				j = 0;
				byte nHits = stream[j++];
				
				byte[] portQ = new byte[GnutellaConstants.PORT_LENGTH];
				byte[] ipQ = new byte[GnutellaConstants.IP_LENGTH];
				byte[] speedQ = new byte[4];
				byte[][] fIQ = new byte[nHits][4];
				byte[][] fSQ = new byte[nHits][4];

				int nameL = partQueryH.size() - nHits
						* GnutellaConstants.QUERYHIT_PART_L
						- GnutellaConstants.SERVER_ID_L;
				byte[][] name = new byte[nHits][nameL];
				byte[] idServent = new byte[GnutellaConstants.SERVER_ID_L];

				// Llenamos cada campo con lo que habia en el stream
				for (i = 0; i < GnutellaConstants.PORT_LENGTH; i++) {
					portQ[i] = stream[j++];
				}
				for (i = 0; i < GnutellaConstants.IP_LENGTH; i++) {
					ipQ[i] = stream[j++];
				}

				for (i = 0; i < 4; i++) {
					speedQ[i] = stream[j++];
				}
				for (int k = 0; k < nHits; k++) {

					for (i = 0; i < 4; i++) {
						fIQ[k][i] = stream[j++];
					}
					for (i = 0; i < 4; i++) {
						fSQ[k][i] = stream[j++];
					}

					for (i = 0; i < nameL; i++) {
						if (stream[j] == GnutellaConstants.END) {
							j++;
							break;
						}
						name[k][i] = stream[j++];
					}

				}
				for (i = 0; i < GnutellaConstants.SERVER_ID_L; i++) {
					idServent[i] = stream[j++];
				}

				for (byte[] a : name) {
				}
				QueryHitMessage m = new QueryHitMessage(idMessage, ttl, hop,
						stream.length, receptorNode, nHits, portQ,
						InetAddress.getByAddress(ipQ), speedQ, fIQ, fSQ, name,
						idServent);

				return m;

			case GnutellaConstants.PUSH:

			default:
				return null;

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
