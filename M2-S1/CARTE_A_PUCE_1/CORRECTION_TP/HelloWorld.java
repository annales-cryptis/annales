package com.sun.javacard.samples.HelloWorld;

import javacard.framework.*;

public class HelloWorld extends Applet
{
    private byte[] echoBytes;
    private static final short LENGTH_ECHO_BYTES = 256;

    protected HelloWorld()
    {
        echoBytes = new byte[LENGTH_ECHO_BYTES];
        register();
    }

    public static void install(byte[] bArray, short bOffset, byte bLength)
    {
        new HelloWorld();
    }

    public void process(APDU apdu)
    {
        byte buffer[] = apdu.getBuffer();

        if ((buffer[ISO7816.OFFSET_CLA] == (byte) 0x80) &&
			(buffer[ISO7816.OFFSET_INS] == (byte) 0x80))
			ISOException.throwIt(ISO7816.SW_WRONG_P1P2);

		short bytesRead = apdu.setIncomingAndReceive();
		short echoOffset = (short)0;

		while ( bytesRead > 0 ) {
            Util.arrayCopyNonAtomic(buffer, ISO7816.OFFSET_CDATA, echoBytes, echoOffset, bytesRead);
            echoOffset += bytesRead;
            bytesRead = apdu.receiveBytes(ISO7816.OFFSET_CDATA);
        }

        apdu.setOutgoing();
        apdu.setOutgoingLength( (short) (echoOffset + 5) );

        // echo header
        apdu.sendBytes( (short)0, (short) 5);
        // echo data
        apdu.sendBytesLong( echoBytes, (short) 0, echoOffset );
    }
