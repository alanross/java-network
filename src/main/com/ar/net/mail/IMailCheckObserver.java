package com.ar.net.mail;

import java.util.ArrayList;

/**
 * @author Alan Ross
 * @version 0.1
 */
public interface IMailCheckObserver
{
	public void onMailCheckCompleted( ArrayList<MailData> data );

	public void onMailCheckFailed( String message );
}
