package maze;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;

public class MainMenu extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu frame = new MainMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainMenu() {
		setTitle("Adam & Jason's Amazing Maze!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		/**
		 * Working, but I'm going to rework the maze so it's not a JApplet
		 * 
		 * Jason
		 */
		JButton btnBuildMaze = new JButton("Build A Maze");
		btnBuildMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Maze maze = new Maze();
				maze.setVisible(true);
				maze.init();
			}
		});
		panel.add(btnBuildMaze);

		JButton btnRamdomMaze = new JButton("Random Maze");
		panel.add(btnRamdomMaze);

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.CENTER);

		JLabel lblTitle = new JLabel("Adam & Jason's Amazing Maze!");
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblTitle);
	}

}
