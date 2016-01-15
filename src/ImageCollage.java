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
		
	private static int[] HEIGHTS = {9, 10, 11, 13, 15};
	private static float INCH = 2.54f;
	private static int RESOLUTION = 250; //ppi
	private static int[] COLLAGE_ROWS = {2, 3, 4};
	private static float FORMAT_WIDTH = 16;
	private static float FORMAT_HEIGHT = 9;
	// resulting format 16 to 9
	
	// width in px = 13 / 2,54 * 250
	
	private int chosenHeight = 13;
	private int chosenCollageRows = 3;
	private float chosenFormat = FORMAT_WIDTH / FORMAT_HEIGHT;
	
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
		
		double resultHeight = (chosenHeight / INCH) * RESOLUTION;
		resultHeight = (Math.ceil(resultHeight / chosenCollageRows)) * chosenCollageRows;
		double resultWidth = (resultHeight / FORMAT_HEIGHT) * FORMAT_WIDTH; 
		resultWidth = (Math.ceil(resultWidth / chosenCollageRows)) * chosenCollageRows;
		Mat result = new Mat((int) resultHeight, (int) resultWidth, CvType.CV_8UC3, new Scalar(0, 0, 0));
		
		File[] liste = dir.listFiles();
		
		int x = 0;
		int y = 0;
		int partWidth = (int) (resultWidth / chosenCollageRows);
		int partHeight = (int) (resultHeight / chosenCollageRows);
		int imgCount = 1;
		
		for (int i = 0; i < liste.length; i++){
			System.out.println("inserting image: " + liste[i].getName());
			System.out.println("at: " + x + " / " + y);
			Mat tmp1 = Imgcodecs.imread(liste[i].getAbsolutePath());
			// rotate image?
			if (tmp1.rows() > tmp1.cols()) {
				tmp1 = rotate90(tmp1);
			}
			// resize
			System.out.println("before:" + tmp1.rows() + " / " + tmp1.cols());
			float format = (float) tmp1.cols() / (float) tmp1.rows();
			System.out.println("format chosen: " + chosenFormat);
			System.out.println("format: " + format);
			if (format == chosenFormat) {
				System.out.println("SAME");
				Imgproc.resize(tmp1, tmp1, new Size(partWidth, partHeight));
			} else if (format < chosenFormat) {
				System.out.println("is smaller");
				float newWidth = tmp1.cols() * ((float) partHeight / tmp1.rows());
				Imgproc.resize(tmp1, tmp1, new Size(newWidth, partHeight));
			} else {
				System.out.println("is larger");
				float newHeight = tmp1.rows() * ((float) partWidth / tmp1.cols());
				Imgproc.resize(tmp1, tmp1, new Size(partWidth, newHeight));
			}
			System.out.println("size as:" + partWidth + " / " + partHeight);
			System.out.println("size is:" + tmp1.size().width + " / " + tmp1.size().height);
			
			// copy
			Mat destinationROI = new Mat(result, new Rect (new Point(x, y), tmp1.size()));
			tmp1.copyTo(destinationROI);
			x += partWidth;
			if (x >= resultWidth) {
				x = 0;
				y += partHeight;
			}
			if (y >= resultHeight || i == liste.length - 1) {
				// save image
				Imgcodecs.imwrite(dir.getAbsolutePath() + "/collage" + imgCount + ".jpg", result);
				System.out.println("saved image to: " + dir.getAbsolutePath() + "\\collage" + imgCount + ".jpg");
				result = new Mat((int) resultHeight, (int) resultWidth, CvType.CV_8UC3, new Scalar(0, 0, 0));
				x = 0;
				y = 0;
				imgCount++;
			}
		}
		showImage(mat2Img(result));
		this.dispose();
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
	
	public static Mat rotate90(Mat m) {
		Core.transpose(m, m);
		Core.flip(m, m, 1);
		return m;
	}

	public static void main(String[] args){
		new ImageCollage();
	}
}
