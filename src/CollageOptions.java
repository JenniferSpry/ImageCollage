public class CollageOptions {
	private static float INCH = 2.54f;
	
	public static final float[] HEIGHTS = {9, 10, 11, 13, 15};
	public static final int[] COLLAGE_ROWS = {2, 3, 4};
	public static final float[][] FORMATS = {{4,3}, {16,9}};
	
	private float resolution = 250; //ppi
	private float chosenHeight = 10;
	private float[] chosenFormat = {16, 9};
	private int chosenCollageRows = 3;
	
	private float format = chosenFormat[0] / chosenFormat[1];
	private int resultWidth;
	private int resultHeight;
	
	public CollageOptions() {
		calculateSizes();
	}
	
	// Getter
	public float getResolution() {
		return resolution;
	}
	public float getChosenHeight() {
		return chosenHeight;
	}
	public int getChosenCollageRows() {
		return chosenCollageRows;
	}
	public float getFormat() {
		return format;
	}
	public int getResultWidth() {
		return resultWidth;
	}
	public int getResultHeight() {
		return resultHeight;
	}
	public int getPartWidth() {
		return (int) (resultWidth / chosenCollageRows);
	}
	public int getPartHeight() {
		return (int) (resultHeight / chosenCollageRows);
	}
	public String getFileName(int count) {
		return "/" + (int) chosenFormat[0] + "-" + (int) chosenFormat[1] + "_collage_" + count + ".jpg";
	}
	
	// Setter
	public void setResolution(float resolution) {
		this.resolution = resolution;
		calculateSizes();
	}
	
	public void setChosenHeight(float chosenHeight) {
		this.chosenHeight = chosenHeight;
		calculateSizes();
	}
	
	public void setChosenCollageRows(int chosenCollageRows) {
		this.chosenCollageRows = chosenCollageRows;
		calculateSizes();
	}
	
	public void setFormat(float[] format) {
		chosenFormat = format;
		this.format = format[0] / format[1];
		calculateSizes();
	}
	
	// calculate sizes
	private void calculateSizes() {
		resultHeight = (int) ((Math.ceil((chosenHeight / INCH) * resolution / chosenCollageRows)) * chosenCollageRows);
		resultWidth = (int) ((Math.ceil(resultHeight * format / chosenCollageRows)) * chosenCollageRows);
	}
}
