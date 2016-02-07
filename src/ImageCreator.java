import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

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


public class ImageCreator {
	
	void createCollages(File dir, CollageOptions collageOptions, GUI gui) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		Mat result = new Mat(collageOptions.getResultHeight(), collageOptions.getResultWidth(), CvType.CV_8UC3, new Scalar(0, 0, 0));
		
		File[] liste = dir.listFiles();
		
		int x = 0;
		int y = 0;
		int imgCount = 1;
		
		for (int i = 0; i < liste.length; i++){
			if (liste[i].isFile()) {
				Mat tmp1 = Imgcodecs.imread(liste[i].getAbsolutePath());
				// rotate image?
				if (tmp1.rows() > tmp1.cols()) {
					tmp1 = rotate90(tmp1);
				}
				// resize
				float format = (float) tmp1.cols() / (float) tmp1.rows();
	
				if (format == collageOptions.getFormat()) {
					Imgproc.resize(tmp1, tmp1, new Size(collageOptions.getPartWidth(), collageOptions.getPartHeight()));
				} else if (format < collageOptions.getFormat()) {
					float newWidth = tmp1.cols() * ((float) collageOptions.getPartHeight() / tmp1.rows());
					Imgproc.resize(tmp1, tmp1, new Size(newWidth, collageOptions.getPartHeight()));
				} else {
					float newHeight = tmp1.rows() * ((float) collageOptions.getPartWidth() / tmp1.cols());
					Imgproc.resize(tmp1, tmp1, new Size(collageOptions.getPartWidth(), newHeight));
				}
				
				// copy
				Mat destinationROI = new Mat(result, new Rect (new Point(x, y), tmp1.size()));
				tmp1.copyTo(destinationROI);
				x += collageOptions.getPartWidth();
				if (x >= collageOptions.getResultWidth()) {
					x = 0;
					y += collageOptions.getPartHeight();
				}
				if (y >= collageOptions.getResultHeight() || i == liste.length - 1) {
					// save image
					Imgcodecs.imwrite(dir.getAbsolutePath() + collageOptions.getFileName(imgCount), result);
					System.out.println(dir.getAbsolutePath() + collageOptions.getFileName(imgCount));
					result = new Mat(collageOptions.getResultHeight(), collageOptions.getResultWidth(), CvType.CV_8UC3, new Scalar(0, 0, 0));
					x = 0;
					y = 0;
					imgCount++;
				}
			}
		}
		gui.showImage(mat2Img(result));
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
}
