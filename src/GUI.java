import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class GUI extends JFrame{
	
	private CollageOptions collageOptions;
	private ImageCreator imageCreator;

	private JLabel fileChooserLabel;
	private JButton fileChooserButton;
	private JFileChooser fileChooser;
	private JLabel imageLabel;
	
	public GUI(CollageOptions collageOptions, ImageCreator imageCreator){
		super("Create collage");
		
		this.collageOptions = collageOptions;
		this.imageCreator = imageCreator;
		
		setLayout(new BorderLayout());
		
		// format
		JLabel formatListLabel = new JLabel("Format");
		JComboBox formatList = new JComboBox(collageOptions.FORMATS_AS_TEXT);
		formatList.setSelectedIndex(0);
		formatList.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JComboBox cb = (JComboBox)e.getSource();
		        collageOptions.setFormat(cb.getSelectedIndex());
			}
		});
		
		// filechooser
		fileChooserLabel = new JLabel("Choose a folder");
		fileChooserButton = new JButton("search");
		
		fileChooser = new JFileChooser();
	    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		fileChooserButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onLoad();
			}
		});
		
		// image
		imageLabel = new JLabel();
		
		// put it all together
		JPanel containerTop = new JPanel();
		add(containerTop, BorderLayout.NORTH);
		containerTop.setLayout(new FlowLayout());
		JPanel controlsPanel = new JPanel();
		containerTop.add(controlsPanel);
		controlsPanel.add(formatListLabel);
		controlsPanel.add(formatList);
		controlsPanel.add(fileChooserLabel);
		controlsPanel.add(fileChooserButton);
		
		
		JPanel containerBottom = new JPanel();
		add(containerBottom, BorderLayout.CENTER);	
		containerBottom.add(imageLabel);
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dim = tk.getScreenSize();
			
		pack();
		setSize(dim.width / 2, dim.height / 2);
		setLocation(dim.width / 2 - this.getWidth() / 2, dim.height / 2 - this.getHeight() / 2);
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
		imageLabel.setIcon(new ImageIcon(img.getScaledInstance(500, 500, 0)));
		pack();
		imageLabel.repaint();
	}
}
