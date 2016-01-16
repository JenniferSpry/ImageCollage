import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class GUI extends JFrame{
	
	private CollageOptions collageOptions;
	private ImageCreator imageCreator;

	private JLabel label;
	private JButton button;
	private JFileChooser fileChooser;
	private JLabel picLabel;
	
	public GUI(CollageOptions collageOptions, ImageCreator imageCreator){
		super("Create collage");
		
		this.collageOptions = collageOptions;
		this.imageCreator = imageCreator;
		
		setLayout(new BorderLayout());
		
		JPanel containerTop = new JPanel();
		JPanel containerBottom = new JPanel();
		picLabel = new JLabel();
		containerBottom.add(picLabel);
		containerTop.setLayout(new FlowLayout());
		JPanel panel1 = new JPanel();
		
		containerTop.add(panel1);
		add(containerTop, BorderLayout.NORTH);
		add(containerBottom, BorderLayout.CENTER);
		
		label = new JLabel("Choose a folder");
		button = new JButton("search");
		panel1.add(label);
		panel1.add(button);
		
		fileChooser = new JFileChooser();
	    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onLoad();
			}
		});
			
		pack();
		setSize(500,500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void onLoad(){
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION){
			File dir = fileChooser.getSelectedFile();
			try {
				imageCreator.createCollages(dir, collageOptions);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void showImage(BufferedImage img) {
		picLabel.setIcon(new ImageIcon(img.getScaledInstance(500, 500, 0)));
		pack();
		picLabel.repaint();
	}
}
