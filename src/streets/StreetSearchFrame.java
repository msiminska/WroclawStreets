package streets;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class StreetSearchFrame extends JFrame {

	private JFrame frame;
	private JButton btnSearch;
	private JScrollPane scrollPaneSearch;
	JTextField txtFieldSearch;
	JTextArea txtAreaResultSet;
	JPanel pnlLoadingCircle;

	public StreetSearchFrame() {

		frame = new JFrame();
		Container content = frame.getContentPane();

		frame.setTitle("Streets of Wroclaw");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		txtFieldSearch = new JTextField(20);
		txtAreaResultSet = new JTextArea();

		scrollPaneSearch = new JScrollPane(txtAreaResultSet);

		btnSearch = new JButton("OK");
		btnSearch.setEnabled(false);
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent zd) {
				txtAreaResultSet.setText(null);
				Thread thread = new Thread(new SearchThread(StreetSearchFrame.this));
				pnlLoadingCircle.setVisible(true);
				thread.start();
				frame.repaint();
			}
		});

		frame.getRootPane().setDefaultButton(btnSearch);

		txtFieldSearch.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				change();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				change();
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				change();
			}

			private void change() {
				if (txtFieldSearch.getText().equals("")) {
					btnSearch.setEnabled(false);
				} else {
					btnSearch.setEnabled(true);
				}
			}
		});

		scrollPaneSearch.setPreferredSize(new Dimension(300, 200));

		Box verticalBox = Box.createVerticalBox();
		verticalBox.add(new JLabel("Enter the name of the street: "));
		verticalBox.add(Box.createVerticalStrut(10));
		verticalBox.add(txtFieldSearch);
		verticalBox.add(Box.createVerticalStrut(10));
		verticalBox.add(btnSearch);
		verticalBox.add(Box.createVerticalStrut(25));
		verticalBox.add(new JLabel("List of streets: "));
		verticalBox.add(Box.createVerticalStrut(20));
		verticalBox.add(scrollPaneSearch);

		ImageIcon loadingIcon = new ImageIcon("src/ajax-loader.gif");
		JLabel lblLoading = new JLabel(loadingIcon);
		lblLoading.setOpaque(true);
		lblLoading.setBackground(new Color(0, 0, 0, 0));

		Box verticalBox2 = Box.createVerticalBox();
		verticalBox2.add(Box.createVerticalStrut(120));
		verticalBox2.add(lblLoading);
		verticalBox2.add(Box.createHorizontalGlue());

		pnlLoadingCircle = new JPanel();
		pnlLoadingCircle.setBackground(new Color(142, 152, 156, 20));
		pnlLoadingCircle.setVisible(false);
		pnlLoadingCircle.setSize(new Dimension(400, 400));
		pnlLoadingCircle.add(verticalBox2);
		content.add(pnlLoadingCircle);

		JPanel pnlSearch = new JPanel();
		pnlSearch.add(verticalBox);
		content.add(pnlSearch);

		frame.setSize(400, 400);
		frame.setLocationRelativeTo(null);
		frame.show();
	}

	public static void main(String[] args) {

		StreetSearchFrame test = new StreetSearchFrame();
	}

}
