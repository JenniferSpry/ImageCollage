import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
import org.opencv.core.MatOfByte;
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
	private JLabel picLabel;
	
	public ImageCollage(){
		super("Create collage");
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
				createCollages(dir);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}	
	
	private void createCollages(File dir) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat result = new Mat(300, 300, CvType.CV_8UC3, new Scalar(0, 0, 0));
		File[] liste = dir.listFiles();
		
		int x = 0;
		int y = 0;
		
		for (int i = 0; i < chosenCollageRows * chosenCollageRows; i++){
			System.out.println("inserting image: " + liste[i].getName());
			Mat tmp1 = Imgcodecs.imread(liste[i].getAbsolutePath());
			Imgproc.resize(tmp1, tmp1, new Size(100,100));
			Mat destinationROI = new Mat(result, new Rect (new Point(x, y), tmp1.size()));
			tmp1.copyTo(destinationROI);
			x += 100;
			if (x >= 300) {
				x = 0;
				y += 100;
			}
			if (y >= 300) {
				// save image
				Imgcodecs.imwrite(dir.getAbsolutePath() + "/collage.jpg", result);
				System.out.println("saved image to: " + dir.getAbsolutePath() + "\\collage.jpg");
			}
		}
		showImage(mat2Img(result));
//		this.dispose();
	}
	
	private void showImage(BufferedImage img) {
		picLabel.setIcon(new ImageIcon(img.getScaledInstance(500, 500, 0)));
		pack();
		picLabel.repaint();
	}
	
	public static BufferedImage mat2Img(Mat inmat){
		MatOfByte bytemat = new MatOfByte();
		Imgcodecs.imencode(".jpg", inmat, bytemat);
		byte[] bytes = bytemat.toArray();
		InputStream in = new ByteArrayInputStream(bytes);
		BufferedImage img = new BufferedImage(1, 1, 1);
		try {
			img = ImageIO.read(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return img;
    }

	public static void main(String[] args){
		new ImageCollage();
	}
}
