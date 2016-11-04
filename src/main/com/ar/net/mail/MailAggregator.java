package com.ar.net.mail;

import com.sun.mail.pop3.POP3Store;

import javax.mail.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * @author Alan Ross
 * @version 0.1
 */
public class MailAggregator extends Thread
{
	private IMailCheckObserver _observer;
	private MailConfig _config;

	public MailAggregator( MailConfig config, IMailCheckObserver observer )
	{
		_observer = observer;
		_config = config;
	}

	public void run()
	{
		receiveEmail();
	}

	private void receiveEmail()
	{
		try
		{
			ArrayList<MailData> data = new ArrayList<MailData>();

			Properties properties = new Properties();
			properties.put( "mail.pop3.host", _config.pop3Host );
			Session emailSession = Session.getDefaultInstance( properties );

			POP3Store emailStore = ( POP3Store ) emailSession.getStore( _config.storeType );
			emailStore.connect( _config.user, _config.password );

			Folder emailFolder = emailStore.getFolder( "INBOX" );
			emailFolder.open( Folder.READ_ONLY );

			Message[] messages = emailFolder.getMessages();
			int n = ( messages.length < _config.maxAmount ) ? messages.length : _config.maxAmount;

			for( int i = 0; i < n; i++ )
			{
				Message message = messages[ i ];
				MailData m = new MailData();

				m.subject = message.getSubject();
				m.from = "" + message.getFrom()[ 0 ];

				Object content = message.getContent();

				if( content instanceof String )
				{
					String body = ( String ) content;
					m.message = body;
				}
				else if( content instanceof Multipart )
				{
					Multipart mp = ( Multipart ) content;
					m.message = mp.getBodyPart( 0 ).toString();
				}

				data.add( m );

				System.out.println( "==============================" );
				System.out.println( "Email #" + ( i + 1 ) );
				System.out.println( "Subject: " + message.getSubject() );
				System.out.println( "ReceiveDate: " + message.getSentDate() );
				System.out.println( "From: " + message.getFrom()[ 0 ] );
				System.out.println( "Text: " + m.message );
				System.out.println( "Type: " + message.getContentType() );
			}

			emailFolder.close( false );
			emailStore.close();

			_observer.onMailCheckCompleted( data );
		}
		catch( NoSuchProviderException e )
		{
			_observer.onMailCheckFailed( e.getMessage() );
			e.printStackTrace();
		}
		catch( MessagingException e )
		{
			_observer.onMailCheckFailed( e.getMessage() );
			e.printStackTrace();
		}
		catch( IOException e )
		{
			_observer.onMailCheckFailed( e.getMessage() );
			e.printStackTrace();
		}
	}

	@Override
	public String toString()
	{
		return "[MailAggregator]";
	}
}
