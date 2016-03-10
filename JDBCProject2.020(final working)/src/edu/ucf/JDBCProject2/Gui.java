package edu.ucf.JDBCProject2;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Gui {
	JComboBox<String> cbBikeName;
	JComboBox<String> cbRiderName;

	private JFrame frame;
	private JTextField tfCountryOfOrigin;
	private JTextField tfCost;
	private JTextField tfTeamName;
	private JTextField tfNationality;
	private JTextField tfProWins;
	private Connection con;
	private Statement stmt;
	private ResultSet rs;
	private boolean isInitializingCb = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
					window.frame.setVisible(true);
				} catch (Exception e) {
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Gui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		InitializeDatabaseConnection();
		frame = new JFrame();
		frame.setBounds(100, 100, 388, 359);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel bikesPnl = new JPanel();
		tabbedPane.addTab("Bikes", null, bikesPnl, null);
		bikesPnl.setLayout(null);

		cbBikeName = new JComboBox<String>();
		cbBikeName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isInitializingCb)
					return;
				String sql = "SELECT * FROM racebikes WHERE bikename = '"
						+ cbBikeName.getSelectedItem() + "'";
				try {
					rs = stmt.executeQuery(sql);
					while (rs.next()) {
						tfCountryOfOrigin.setText(rs.getString(2));
						tfCost.setText(rs.getString(3));
					}
				} catch (SQLException e1) {
				}
			}
		});
		cbBikeName.setEditable(true);
		cbBikeName.setBounds(139, 44, 142, 20);
		bikesPnl.add(cbBikeName);
		populateCb("BIKENAME", "RACEBIKES");

		tfCountryOfOrigin = new JTextField();
		tfCountryOfOrigin.setBounds(139, 75, 142, 20);
		bikesPnl.add(tfCountryOfOrigin);
		tfCountryOfOrigin.setColumns(10);

		tfCost = new JTextField();
		tfCost.setBounds(139, 106, 142, 20);
		bikesPnl.add(tfCost);
		tfCost.setColumns(10);

		JLabel lblBikeName = new JLabel("Bike Name");
		lblBikeName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBikeName.setBounds(56, 47, 73, 14);
		bikesPnl.add(lblBikeName);

		JLabel lblCountryOfOrigin = new JLabel("Country of Origin");
		lblCountryOfOrigin.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCountryOfOrigin.setBounds(32, 78, 97, 14);
		bikesPnl.add(lblCountryOfOrigin);

		JLabel lblCost = new JLabel("Cost");
		lblCost.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCost.setBounds(83, 109, 46, 14);
		bikesPnl.add(lblCost);

		JLabel lblRaceBikes = new JLabel("Race Bikes");
		lblRaceBikes.setHorizontalAlignment(SwingConstants.CENTER);
		lblRaceBikes.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblRaceBikes.setBounds(139, 11, 142, 22);
		bikesPnl.add(lblRaceBikes);

		JButton btnUpdateBike = new JButton("Update Bike");
		btnUpdateBike.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateBike();
			}
		});
		btnUpdateBike.setBounds(53, 182, 117, 23);
		bikesPnl.add(btnUpdateBike);

		JButton btnDeleteBike = new JButton("Delete Bike");
		btnDeleteBike.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteBike();
			}
		});
		btnDeleteBike.setBounds(180, 182, 117, 23);
		bikesPnl.add(btnDeleteBike);

		JButton btnAddNewBike = new JButton("Add New Bike");
		btnAddNewBike.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addBike();
			}
		});
		btnAddNewBike.setBounds(180, 216, 117, 23);
		bikesPnl.add(btnAddNewBike);

		JButton button_3 = new JButton("Clear Fields");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearBike();
			}
		});
		button_3.setBounds(53, 216, 117, 23);
		bikesPnl.add(button_3);

		JButton button_4 = new JButton("Exit Program");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(1);
			}
		});
		button_4.setBounds(240, 259, 117, 23);
		bikesPnl.add(button_4);

		JPanel ridersPnl = new JPanel();
		tabbedPane.addTab("Riders", null, ridersPnl, null);
		ridersPnl.setLayout(null);

		JLabel lblRaceRiders = new JLabel("Race Riders");
		lblRaceRiders.setBounds(139, 11, 142, 22);
		lblRaceRiders.setHorizontalAlignment(SwingConstants.CENTER);
		lblRaceRiders.setFont(new Font("Tahoma", Font.PLAIN, 18));
		ridersPnl.add(lblRaceRiders);

		JLabel lblRiderName = new JLabel("Rider Name");
		lblRiderName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRiderName.setBounds(53, 47, 76, 14);
		ridersPnl.add(lblRiderName);

		cbRiderName = new JComboBox<String>();
		cbRiderName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isInitializingCb)
					return;
				String sql = "SELECT * FROM raceriders WHERE ridername = '"
						+ cbRiderName.getSelectedItem() + "'";
				try {
					rs = stmt.executeQuery(sql);
					while (rs.next()) {
						tfTeamName.setText(rs.getString(2));
						tfNationality.setText(rs.getString(3));
						tfProWins.setText(rs.getString(4));
					}
				} catch (SQLException e1) {
				}
			}
		});
		cbRiderName.setEditable(true);
		cbRiderName.setBounds(139, 44, 142, 20);
		ridersPnl.add(cbRiderName);
		populateCb("RIDERNAME", "RACERIDERS");

		JLabel lblTeamName = new JLabel("Team Name");
		lblTeamName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTeamName.setBounds(53, 78, 76, 14);
		ridersPnl.add(lblTeamName);

		tfTeamName = new JTextField();
		tfTeamName.setBounds(139, 75, 142, 20);
		ridersPnl.add(tfTeamName);
		tfTeamName.setColumns(10);

		tfNationality = new JTextField();
		tfNationality.setBounds(139, 106, 142, 20);
		ridersPnl.add(tfNationality);
		tfNationality.setColumns(10);

		JLabel lblNationality = new JLabel("Nationality");
		lblNationality.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNationality.setBounds(53, 109, 76, 14);
		ridersPnl.add(lblNationality);

		tfProWins = new JTextField();
		tfProWins.setBounds(139, 137, 142, 20);
		ridersPnl.add(tfProWins);
		tfProWins.setColumns(10);

		JLabel lblProWins = new JLabel("Number of Pro Wins");
		lblProWins.setHorizontalAlignment(SwingConstants.RIGHT);
		lblProWins.setBounds(10, 140, 119, 14);
		ridersPnl.add(lblProWins);

		JButton btnAddNewRider = new JButton("Add New Rider");
		btnAddNewRider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addRider();
			}
		});
		btnAddNewRider.setBounds(180, 216, 117, 23);
		ridersPnl.add(btnAddNewRider);

		JButton btnUpdateRider = new JButton("Update Rider");
		btnUpdateRider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateRider();
			}
		});
		btnUpdateRider.setBounds(53, 182, 117, 23);
		ridersPnl.add(btnUpdateRider);

		JButton btnDeleteRider = new JButton("Delete Rider");
		btnDeleteRider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteRider();
			}
		});
		btnDeleteRider.setBounds(180, 182, 117, 23);
		ridersPnl.add(btnDeleteRider);

		JButton btnClearFields = new JButton("Clear Fields");
		btnClearFields.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearRider();
			}
		});
		btnClearFields.setBounds(53, 216, 117, 23);
		ridersPnl.add(btnClearFields);

		JButton btnExitProgram = new JButton("Exit Program");
		btnExitProgram.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});
		btnExitProgram.setBounds(240, 259, 117, 23);
		ridersPnl.add(btnExitProgram);

		JPanel teamsPnl = new JPanel();
		tabbedPane.addTab("Teams", null, teamsPnl, null);
		teamsPnl.setLayout(null);

		JLabel lblNewLabel = new JLabel("Not part of assignment");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
		lblNewLabel.setBounds(172, 111, 46, 14);
		teamsPnl.add(lblNewLabel);

		JLabel lblNotPartOf = new JLabel("Not part of assignment");
		lblNotPartOf.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblNotPartOf.setBounds(10, 62, 270, 50);
		teamsPnl.add(lblNotPartOf);

		JLabel lblTeams = new JLabel("Teams");
		lblTeams.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblTeams.setBounds(10, 11, 270, 40);
		teamsPnl.add(lblTeams);

		JPanel winnersPnl = new JPanel();
		tabbedPane.addTab("Winners", null, winnersPnl, null);
		winnersPnl.setLayout(null);

		JLabel label = new JLabel("Not part of assignment");
		label.setFont(new Font("Tahoma", Font.PLAIN, 24));
		label.setBounds(10, 62, 270, 48);
		winnersPnl.add(label);

		JLabel lblWinners = new JLabel("Winners");
		lblWinners.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblWinners.setBounds(10, 11, 270, 40);
		winnersPnl.add(lblWinners);
	}

	// Initializes the database connection
	public boolean InitializeDatabaseConnection() {
		String dbURL = "jdbc:oracle:thin:@localhost:1521:bikerace";

		try {
			Class.forName("oracle.jdbc.OracleDriver");
			con = DriverManager.getConnection(dbURL, "SYSTEM", "advdbm");
			stmt = con.createStatement();
			System.out.println("Connected");
			return true;
		} catch (ClassNotFoundException e) {
			System.out.println("Couldn't register JDBC driver.");
			System.out.println("Application Ending.");
		} catch (SQLException e) {
			System.out.println("Login failed.");
		}
		return false;
	}

	//  Retrieves database bike names or rider names and populates the respective ComboBoxes
	public void populateCb(String selectName, String tableName) {
		isInitializingCb = true;
		if (tableName == "RACEBIKES")
			cbBikeName.removeAllItems();
		if (tableName == "RACERIDERS")
			cbRiderName.removeAllItems();

		String queryString = "SELECT " + selectName + " FROM " + tableName
				+ " ORDER BY " + selectName;

		try {
			rs = stmt.executeQuery(queryString);
			while (rs.next()) {
				if (tableName == "RACEBIKES")
					cbBikeName.addItem(rs.getString(1));
				if (tableName == "RACERIDERS")
					cbRiderName.addItem(rs.getString(1));
			}
		} catch (SQLException e) {
		}
		isInitializingCb = false;
	}

	//	Clears all input fields in Bike window
	public void clearBike() {
		cbBikeName.setSelectedIndex(0);
		tfCountryOfOrigin.setText(null);
		tfCost.setText(null);
	}

	//	Adds a new row in database Bike table using values in fields
	public void addBike() {
		if (cbBikeName.getSelectedItem().toString().contentEquals("")) {
			JOptionPane.showMessageDialog(frame,
					"No value entered for bike name.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (validateRowExists("racebikes", "bikename", cbBikeName
				.getSelectedItem().toString())) {
			JOptionPane.showMessageDialog(frame, "Bike named "
					+ cbBikeName.getSelectedItem().toString()
					+ " already exists", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String pSqlStatement = ("INSERT INTO racebikes VALUES (?, ?, ?)");
		PreparedStatement preparedSqlStatement = null;
		try {
			preparedSqlStatement = con.prepareStatement(pSqlStatement);
			preparedSqlStatement.setString(1, cbBikeName.getSelectedItem()
					.toString());
			preparedSqlStatement.setString(2, tfCountryOfOrigin.getText());
		} catch (SQLException ex) {
		}
		try {
			preparedSqlStatement.setInt(3, Integer.valueOf(tfCost.getText()));
			if (Integer.valueOf(tfCost.getText()) < 0) {
				JOptionPane.showMessageDialog(frame, "Not a valid bike cost",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} catch (NumberFormatException e) {
			try {
				if (tfCost.getText().equals(""))
					preparedSqlStatement.setNull(3, java.sql.Types.INTEGER);
				else {
					JOptionPane.showMessageDialog(frame,
							"Not a valid bike cost", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (SQLException e1) {
			}
		} catch (SQLException e) {
		}
		try {
			preparedSqlStatement.executeUpdate();
		} catch (SQLException e) {
		}
		populateCb("BIKENAME", "RACEBIKES");
		JOptionPane.showMessageDialog(frame, "The bike "
				+ cbBikeName.getSelectedItem().toString()
				+ " has been added to the database", "Success!",
				JOptionPane.PLAIN_MESSAGE);
		clearBike();
	}

	//	Updates the selected database row in Bike table with new values
	public void updateBike() {
		if (cbBikeName.getSelectedItem().toString().contentEquals("")) {
			JOptionPane.showMessageDialog(frame,
					"No value entered for bike name.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!validateRowExists("racebikes", "bikename", cbBikeName
				.getSelectedItem().toString())) {
			JOptionPane.showMessageDialog(frame, "Bike named "
					+ cbBikeName.getSelectedItem().toString()
					+ " does not exist", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String pSqlStatement = ("UPDATE racebikes SET country_of_origin = ?, cost = ? WHERE bikename = ?");
		PreparedStatement preparedSqlStatement = null;
		try {
			preparedSqlStatement = con.prepareStatement(pSqlStatement);
			preparedSqlStatement.setString(1, tfCountryOfOrigin.getText());

		} catch (SQLException e) {
		}
		try {
			preparedSqlStatement.setInt(2, Integer.valueOf(tfCost.getText()));
			if (Integer.valueOf(tfCost.getText()) < 0) {
				JOptionPane.showMessageDialog(frame, "Not a valid bike cost",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} catch (NumberFormatException e) {
			try {
				if (tfCost.getText().equals(""))
					preparedSqlStatement.setNull(2, java.sql.Types.INTEGER);
				else {
					JOptionPane.showMessageDialog(frame,
							"Not a valid bike cost", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (SQLException e1) {
			}
		} catch (SQLException e) {
		}
		try {
			preparedSqlStatement.setString(3, cbBikeName.getSelectedItem()
					.toString());
			preparedSqlStatement.executeUpdate();
		} catch (SQLException e) {
		}
		JOptionPane.showMessageDialog(frame, "The bike "
				+ cbBikeName.getSelectedItem().toString()
				+ " has been updated.", "Success!", JOptionPane.PLAIN_MESSAGE);
		clearBike();
	}

	//	Deletes the selected row from the database Bike table
	public void deleteBike() {
		if (cbBikeName.getSelectedItem().toString().contentEquals("")) {
			JOptionPane.showMessageDialog(frame,
					"No value entered for bike name.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!validateRowExists("racebikes", "bikename", cbBikeName
				.getSelectedItem().toString())) {
			JOptionPane.showMessageDialog(frame, "Bike named "
					+ cbBikeName.getSelectedItem().toString()
					+ " does not exist", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (validateRowExists("raceteams", "bikename", cbBikeName
				.getSelectedItem().toString())) {
			JOptionPane.showMessageDialog(frame, "Bike "
					+ cbBikeName.getSelectedItem().toString()
					+ " exists on Raceteams table.  Unable to delete.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String tempBikeName = cbBikeName.getSelectedItem().toString();
		int response = JOptionPane.showConfirmDialog(frame,
				"Are you sure you want to delete " + tempBikeName + "?",
				"Confirm Delete", JOptionPane.YES_NO_OPTION);
		if ((response == JOptionPane.NO_OPTION)
				|| (response == JOptionPane.CLOSED_OPTION))
			return;
		String pSqlStatement = ("DELETE FROM racebikes WHERE bikename = ?");
		PreparedStatement preparedSqlStatement = null;
		try {
			preparedSqlStatement = con.prepareStatement(pSqlStatement);
			preparedSqlStatement.setString(1, cbBikeName.getSelectedItem()
					.toString());
			preparedSqlStatement.executeUpdate();
		} catch (SQLException e) {
		}
		populateCb("BIKENAME", "RACEBIKES");
		JOptionPane.showMessageDialog(frame, "The bike " + tempBikeName
				+ " has been deleted.", "Success!", JOptionPane.PLAIN_MESSAGE);
		clearBike();
	}

	//	Clears all input fields in Rider window
	public void clearRider() {
		cbRiderName.setSelectedIndex(0);
		tfTeamName.setText(null);
		tfNationality.setText(null);
		tfProWins.setText(null);
	}

	//	Adds a new row in database Rider table using values in fields
	public void addRider() {
		if (cbRiderName.getSelectedItem().toString().contentEquals("")) {
			JOptionPane.showMessageDialog(frame,
					"No value entered for rider name.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (validateRowExists("raceriders", "ridername", cbRiderName
				.getSelectedItem().toString())) {
			JOptionPane.showMessageDialog(frame, "Rider named "
					+ cbRiderName.getSelectedItem().toString()
					+ " already exists", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (tfTeamName.getText().equals("")) {
			JOptionPane.showMessageDialog(frame,
					"No value entered for team name.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!validateRowExists("raceteams", "teamname", tfTeamName.getText())) {
			JOptionPane.showMessageDialog(frame,
					"Team named " + tfTeamName.getText() + " does not exist",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String pSqlStatement = ("INSERT INTO raceriders VALUES (?, ?, ?, ?)");
		PreparedStatement preparedSqlStatement = null;
		try {
			preparedSqlStatement = con.prepareStatement(pSqlStatement);
			preparedSqlStatement.setString(1, cbRiderName.getSelectedItem()
					.toString());
			preparedSqlStatement.setString(2, tfTeamName.getText());
			preparedSqlStatement.setString(3, tfNationality.getText());
		} catch (SQLException ex) {
		}
		try {
			preparedSqlStatement
					.setInt(4, Integer.valueOf(tfProWins.getText()));
			if (Integer.valueOf(tfProWins.getText()) < 0) {
				JOptionPane.showMessageDialog(frame,
						"Not a valid number of wins.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		} catch (NumberFormatException | SQLException e) {
			try {
				if (tfProWins.getText().equals(""))
					preparedSqlStatement.setNull(4, java.sql.Types.INTEGER);
				else {
					JOptionPane.showMessageDialog(frame,
							"Not a valid number of wins.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (SQLException e1) {
			}
		}
		try {
			preparedSqlStatement.executeUpdate();
		} catch (SQLException e) {
		}
		populateCb("RIDERNAME", "RACERIDERS");
		JOptionPane.showMessageDialog(frame, "The rider "
				+ cbRiderName.getSelectedItem().toString()
				+ " has been added to the database", "Success!",
				JOptionPane.PLAIN_MESSAGE);
		clearRider();
	}

	//	Updates the selected database row in Rider table with new values
	public void updateRider() {
		if (cbRiderName.getSelectedItem().toString().contentEquals("")) {
			JOptionPane.showMessageDialog(frame,
					"No value entered for rider name.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!validateRowExists("raceriders", "ridername", cbRiderName
				.getSelectedItem().toString())) {
			JOptionPane.showMessageDialog(frame, "Rider named "
					+ cbRiderName.getSelectedItem().toString()
					+ " does not exist", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (tfTeamName.getText().equals("")) {
			JOptionPane.showMessageDialog(frame,
					"No value entered for team name.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!validateRowExists("raceteams", "teamname", tfTeamName.getText())) {
			JOptionPane.showMessageDialog(frame,
					"Team named " + tfTeamName.getText() + " does not exist",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String pSqlStatement = ("UPDATE raceriders SET teamname = ?, nationality = ?, num_pro_wins = ? WHERE ridername = ?");
		PreparedStatement preparedSqlStatement = null;
		try {
			preparedSqlStatement = con.prepareStatement(pSqlStatement);
			preparedSqlStatement.setString(1, tfTeamName.getText());
			preparedSqlStatement.setString(2, tfNationality.getText());
		} catch (SQLException e) {
		}
		try {
			preparedSqlStatement
					.setInt(3, Integer.valueOf(tfProWins.getText()));
			if (Integer.valueOf(tfProWins.getText()) < 0) {
				JOptionPane.showMessageDialog(frame,
						"Not a valid number of wins.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		} catch (NumberFormatException | SQLException e) {
			try {
				if (tfProWins.getText().equals(""))
					preparedSqlStatement.setNull(3, java.sql.Types.INTEGER);
				else {
					JOptionPane.showMessageDialog(frame,
							"Not a valid number of wins.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (SQLException e1) {
			}
		}
		try {
			preparedSqlStatement.setString(4, cbRiderName.getSelectedItem()
					.toString());
			preparedSqlStatement.executeUpdate();
		} catch (SQLException e) {
		}
		JOptionPane.showMessageDialog(frame, "The rider "
				+ cbRiderName.getSelectedItem().toString()
				+ " has been updated.", "Success!", JOptionPane.PLAIN_MESSAGE);
		clearRider();
	}

	//	Deletes the selected row from the database Rider table
	public void deleteRider() {
		if (cbRiderName.getSelectedItem().toString().contentEquals("")) {
			JOptionPane.showMessageDialog(frame,
					"No value entered for rider name.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (validateRowExists("racewinners", "ridername", cbRiderName
				.getSelectedItem().toString())) {
			JOptionPane.showMessageDialog(frame, "Rider "
					+ cbRiderName.getSelectedItem().toString()
					+ " exists on Racewinners table.  Unable to delete.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!validateRowExists("raceriders", "ridername", cbRiderName
				.getSelectedItem().toString())) {
			JOptionPane.showMessageDialog(frame, "Rider "
					+ cbRiderName.getSelectedItem().toString()
					+ " does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String tempRiderName = cbRiderName.getSelectedItem().toString();
		int response = JOptionPane.showConfirmDialog(frame,
				"Are you sure you want to delete " + tempRiderName + "?",
				"Confirm Delete", JOptionPane.YES_NO_OPTION);
		if ((response == JOptionPane.NO_OPTION)
				|| (response == JOptionPane.CLOSED_OPTION))
			return;
		String pSqlStatement = ("DELETE FROM raceriders WHERE ridername = ?");
		PreparedStatement preparedSqlStatement = null;
		try {
			preparedSqlStatement = con.prepareStatement(pSqlStatement);
			preparedSqlStatement.setString(1, cbRiderName.getSelectedItem()
					.toString());
			preparedSqlStatement.executeUpdate();
		} catch (SQLException e) {
		}
		populateCb("RIDERNAME", "RACERIDERS");
		JOptionPane.showMessageDialog(frame, "The rider " + tempRiderName
				+ " has been deleted.", "Success!", JOptionPane.PLAIN_MESSAGE);
		clearRider();
	}

	//	Verifies that requested values either are or are not in the requested table
	public boolean validateRowExists(String tableName, String fieldName,
			String fieldValue) {
		try {
			rs = stmt.executeQuery("SELECT " + fieldName + " FROM " + tableName
					+ " WHERE " + fieldName + " = '" + fieldValue + "'");
			if (rs.next())
				return true;
		} catch (SQLException e1) {
		}
		return false;
	}
}