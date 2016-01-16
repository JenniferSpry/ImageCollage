public class ImageCollage{
	private GUI gui;
	private CollageOptions collageOptions;
	private ImageCreator imageCreator;
	
	public ImageCollage() {
		collageOptions = new CollageOptions();
		imageCreator = new ImageCreator();
		gui = new GUI(collageOptions, imageCreator);
	}
	
	public static void main(String[] args){
		new ImageCollage();
	}
}
