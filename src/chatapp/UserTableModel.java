package chatapp;

import java.util.*;

import javax.swing.table.AbstractTableModel;

public class UserTableModel extends AbstractTableModel{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> users = new ArrayList<>();
	
	@Override
	public boolean isCellEditable(int row, int column){
		return false;
	}
	
	public String getValueAt(int row, int col){
		return users.get(row);
	}
	
	public synchronized void addRow(String userName){
		users.add(userName);
		sortUsers();
		fireTableDataChanged();
	}
	
	public synchronized void removeRow(String rowToRemove){
		users.remove(rowToRemove);
		sortUsers();
		fireTableDataChanged();
	}

	public void clearUsers(){
		users.clear();
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public int getRowCount() {
		return users.size();
	}
	
	private void sortUsers(){
		Collections.sort(users, String.CASE_INSENSITIVE_ORDER);		
	}
	
}
