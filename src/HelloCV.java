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


public class HelloCV extends JFrame{
		
	private JLabel label;
	private JButton button;
	private JFileChooser fileChooser;
	private JPanel panel1;
	JPanel containerBottom;
	
	public HelloCV(){
		super("Create collage");
		setLayout(new BorderLayout());
		
		JPanel containerTop = new JPanel();
		containerBottom = new JPanel();
		containerTop.setLayout(new FlowLayout());
		panel1 = new JPanel();
		
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
		Mat result = new Mat(3000, 3000, CvType.CV_8UC1, new Scalar(0, 0, 0));
		File[] liste = dir.listFiles();
		
		int x = 0;
		int y = 0;
		
		System.out.println("inserting image: " + liste[0].getName());
		showImage(ImageIO.read(liste[0]));
//		Mat tmp1 = Imgcodecs.imread(liste[i].getAbsolutePath());
//		Imgproc.resize(tmp1, tmp1, new Size(100,100));
//		Mat destinationROI = result.submat(new Rect (new Point(x, y), tmp1.size()));
//		tmp1.copyTo(destinationROI);
//		showImage(mat2Img(result));
	}
	
	private void showImage(BufferedImage img) {
		// TODO Auto-generated method stub
		JLabel picLabel = new JLabel(new ImageIcon(img.getScaledInstance(500, 500, 0)));
		containerBottom.add(picLabel);
		pack();
	}
	
	public static BufferedImage mat2Img(Mat in){
        BufferedImage out;
        byte[] data = new byte[320 * 240 * (int)in.elemSize()];
        int type;
        in.get(0, 0, data);

        if(in.channels() == 1)
            type = BufferedImage.TYPE_BYTE_GRAY;
        else
            type = BufferedImage.TYPE_3BYTE_BGR;

        out = new BufferedImage(320, 240, type);

        out.getRaster().setDataElements(0, 0, 320, 240, data);
        return out;
    } 


	public static void main(String[] args){
		new HelloCV();
	}
}
