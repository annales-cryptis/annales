
import java.util.Iterator;

import javax.smartcardio.*;

public class PCClientApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//Sélectionner votre lecteur de carte
		TerminalFactory tf = TerminalFactory.getDefault();
		CardTerminals list = tf.terminals();

		CardTerminal cad = list.getTerminal("SCM Microsystems Inc. SCR35xx USB Smart Card Reader 0");
/*		try{
			Iterator<CardTerminal> iter = list.list().iterator();
			while (iter.hasNext()) {
				System.out.println(iter.next().getName());
			}
		} catch(Exception e){
			e.printStackTrace();
		}
*/
		if (cad == null)
		{
			System.out.println("Pas de lecteur détecté !");
		}
		else
		{
			try
			{
				if (!cad.isCardPresent())
				{
					System.out.println("Inserer une carte");

					cad.waitForCardPresent(0);
				}

				//  Etablir la connexion avec la carte à puce
				Card c = cad.connect("*");

				//Ouverture d'un canal de communication
				CardChannel canal = c.getBasicChannel();

				CommandAPDU commande = new CommandAPDU(new byte[]
						{
						(byte) 0x00, (byte) 0xA4, (byte) 0x04,(byte) 0x00, (byte) 0x07,
						(byte) 0xA0, (byte) 0x00, (byte) 0x00,(byte) 0x00, (byte) 0x42,(byte) 0x10, (byte) 0x10
						}
				);
				System.out.println("Commande:"+ByteArrayToHexString(commande.getBytes()));

				ResponseAPDU reponse = canal.transmit(commande);
				System.out.println("Reponse:"+ByteArrayToHexString(reponse.getBytes()));

				// Déconnexion
				c.disconnect(true);

			} catch(Exception e){
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

}PCCl
