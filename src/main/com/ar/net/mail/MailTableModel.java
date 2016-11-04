package com.ar.net.mail;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

/**
 * @author Alan Ross
 * @version 0.1
 */
public class MailTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;

	private static final int FROM_INDEX = 0;
	private static final int SUBJECT_INDEX = 1;
	private static final int TIME_INDEX = 2;

	protected String[] _columnNames = new String[]{ "From", "Subject", "Date Received" };
	protected Vector<MailData> _data = new Vector<MailData>();

	public MailTableModel()
	{
	}

	public void addRow( MailData row )
	{
		if( row == null )
		{
			_data.add( new MailData() );
		}
		else
		{
			_data.add( row );
		}

		fireTableRowsInserted( _data.size() - 1, _data.size() - 1 );
	}

	public MailData getRow( int index )
	{
		return _data.get( index );
	}

	public Class getColumnClass( int column )
	{
		switch( column )
		{
			case SUBJECT_INDEX:
			case FROM_INDEX:
			case TIME_INDEX:
				return String.class;
			default:
				return Object.class;
		}
	}

	public Object getValueAt( int row, int column )
	{
		MailData entry = _data.get( row );

		switch( column )
		{
			case SUBJECT_INDEX:
				return entry.subject;
			case FROM_INDEX:
				return entry.from;
			case TIME_INDEX:
				return entry.date;
			default:
				return new Object();
		}
	}

	public void setValueAt( Object value, int row, int column )
	{
		MailData entry = _data.get( row );

		switch( column )
		{
			case SUBJECT_INDEX:
				entry.subject = ( String ) value;
				break;
			case FROM_INDEX:
				entry.from = ( String ) value;
				break;
			case TIME_INDEX:
				entry.date = ( String ) value;
				break;
			default:
				System.out.println( "invalid index" );
		}

		fireTableCellUpdated( row, column );
	}

	public String getColumnName( int column )
	{
		return _columnNames[ column ];
	}

	public boolean isCellEditable( int row, int column )
	{
		return false;
	}

	public int getRowCount()
	{
		return _data.size();
	}

	public int getColumnCount()
	{
		return _columnNames.length;
	}

	public boolean hasEmptyRow()
	{
		if( _data.size() == 0 )
		{
			return false;
		}

		MailData entry = _data.get( _data.size() - 1 );

		return ( entry.subject.equals( "" ) && entry.from.equals( "" ) && entry.date.equals( "" ) );
	}

	@Override
	public String toString()
	{
		return "[MailTableModel]";
	}
}