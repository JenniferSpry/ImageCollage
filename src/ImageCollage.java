import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class ImageCollage extends JFrame{
	
	
	private static int[] FORMATS = {9, 10, 11, 13, 15};
	private static float INCH = 2.54f;
	private static int RESOLUTION = 250;
	private static int[] COLLAGE_ROWS = {2, 3, 4};
	
	// width in px = 13 / 2,54 * 200
	
	private int chosenFormat = 13;
	private int chosenCollageRows = 3;
	
	private JLabel label;
	private JButton button;
	private JFileChooser fileChooser;
	
	public ImageCollage(){
		super("Create collage");
		setLayout(new BorderLayout());
		
		JPanel containeroben = new JPanel();
		containeroben.setLayout(new FlowLayout());
		JPanel panel1 = new JPanel();
		
		containeroben.add(panel1);
		add(containeroben, BorderLayout.NORTH);
		
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
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}	
	
	private void onLoad(){
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION){
			File dir = fileChooser.getSelectedFile();
			createCollages(dir);
		}
	}	
	
	private void createCollages(File dir) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat result = new Mat(300, 300, CvType.CV_8UC1, new Scalar(0, 0, 0));
		File[] liste = dir.listFiles();
		
		int x = 0;
		int y = 0;
		
		for (int i = 0; i < chosenCollageRows * chosenCollageRows; i++){
			try {
				System.out.println("inserting image: " + liste[i].getName());
				BufferedImage img = ImageIO.read(liste[i]);
				Mat tmp1 = bufferedImageToMat(img);
				Imgproc.resize(tmp1, tmp1, new Size(100,100));
				Rect roi = new Rect (new Point(x, y), tmp1.size()); //region of interest
				Mat destinationROI = result.submat(roi);
				tmp1.copyTo(destinationROI);
				x += 100;
				if (x >= 300) {
					x = 0;
					y += 100;
				}
				if (y >= 300) {
					// save image
					Imgcodecs.imwrite(dir.getAbsolutePath() + "/frunobulax.jpg", result);
					System.out.println("saved image");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private Mat bufferedImageToMat(BufferedImage img) {
		byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		Mat result = new Mat(img.getWidth(), img.getHeight(), CvType.CV_8UC1, new Scalar(0));
		result.put(0, 0, pixels);
		return result;
	}

	public static void main(String[] args){
		new ImageCollage();
	}
}
