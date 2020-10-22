import java.util.List;

import javax.smartcardio.*;

public class ClientPC {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Selection du terminal
		TerminalFactory tf = TerminalFactory.getDefault();

		CardTerminals list = tf.terminals();

		List<CardTerminal> terminals = null;
		try{
			terminals = list.list();
		}catch (CardException ce) {
			ce.printStackTrace();
		}

		System.out.println("Terminals: "+terminals);

//		CardTerminal cad = list.getTerminal("PC/SC terminal Alcor Micro USB Smart Card Reader 0");
		CardTerminal cad = terminals.get(0);

		if (cad == null)
		{
			System.out.println("Pas de lecteur détecté");
		}
		else
		{
				try {
					if (!cad.isCardPresent())
					{
						System.out.println("Inserer une carte");
						cad.waitForCardPresent(0);
					}

					// Etablissons la connexion avec la carte
					Card c = cad.connect("*");

					// Ouverture du canal
					CardChannel canal = c.getBasicChannel();

					CommandAPDU commande = new CommandAPDU(
							new byte[]{(byte)0x00,(byte)0xA4,(byte)0x04, (byte)0x00, (byte)0x0E,
									(byte)'1', (byte)'P',(byte)'A', (byte)'Y',(byte)'.', (byte)'S',(byte)'Y', (byte)'S',(byte)'.', (byte)'D',(byte)'D', (byte)'F',(byte)'0', (byte)'1'});

					System.out.println("Commande:"+ByteArrayToHexString(commande.getBytes()));

					ResponseAPDU reponse = canal.transmit(commande);
					System.out.println("Reponse:"+ByteArrayToHexString(reponse.getBytes()));

					c.disconnect(true);

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

		}

	}

    /**
     * Utility method to convert a byte array to a hexadecimal string.
     *
     * @param bytes Bytes to convert
     * @return String, containing hexadecimal representation.
     */
    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2]; // Each byte has two hex characters (nibbles)
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF; // Cast bytes[j] to int, treating as unsigned value
            hexChars[j * 2] = hexArray[v >>> 4]; // Select hex character from upper nibble
            hexChars[j * 2 + 1] = hexArray[v & 0x0F]; // Select hex character from lower nibble
        }
        return new String(hexChars);
    }
    /**
     * Utility method to convert a hexadecimal string to a byte string.
     *
     * <p>Behavior with input strings containing non-hexadecimal characters is undefined.
     *
     * @param s String containing hexadecimal characters to convert
     * @return Byte array generated from input
     * @throws java.lang.IllegalArgumentException if input length is incorrect
     */
    public static byte[] HexStringToByteArray(String s) throws IllegalArgumentException {
        int len = s.length();
        if (len % 2 == 1) {
            throw new IllegalArgumentException("Hex string must have even number of characters");
        }
        byte[] data = new byte[len / 2]; // Allocate 1 byte per 2 hex characters
        for (int i = 0; i < len; i += 2) {
            // Convert each character into a integer (base-16), then bit-shift into place
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }


}
