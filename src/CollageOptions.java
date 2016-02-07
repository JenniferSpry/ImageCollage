public class CollageOptions {
	private static float INCH = 2.54f;
	
	public static final float[] HEIGHTS = {9, 10, 11, 13, 15};
	public static final String[] HEIGHTS_AS_TEXT = {"9", "10", "11", "13", "15"};
	public static final int HEIGHT_DEFAULT_INDEX = 1;
	
	public static final int[] COLLAGE_ROWS = {2, 3, 4};
	public static final String[] COLLAGE_ROWS_AS_TEXT = {"2x2", "3x3", "4x4"};
	public static final int COLLAGE_ROWS_DEFAULT_INDEX = 1;
	
	public static final float[][] FORMATS = {{1,1}, {5,4}, {11, 8.5f}, {4,3}, {7,5}, {3,2}, {16,9}};
	public static final String[] FORMATS_AS_TEXT = {"1 x 1", "5 x 4", "11 x 8.5", "4 x 3", "7 x 5", "3 x 2", "16 x 9"};
	public static final int FORMATS_DEFAULT_INDEX = 6;
	
	private int resolution = 250; //ppi
	private float chosenHeight = HEIGHTS[HEIGHT_DEFAULT_INDEX];
	private float[] chosenFormat = FORMATS[FORMATS_DEFAULT_INDEX];
	private int chosenCollageRows = COLLAGE_ROWS[COLLAGE_ROWS_DEFAULT_INDEX];
	
	private float format = chosenFormat[0] / chosenFormat[1];
	private int resultWidth;
	private int resultHeight;
	
	private String directoryName = "/";
	
	public CollageOptions() {
		calculateValues();
	}
	
	// Getter
	public int getResolution() {
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
		return directoryName + "/collage_" + count + ".jpg";
	}
	public String getDirectoryName(){
		return directoryName;
	}
	
	// Setter
	public void setResolution(int resolution) {
		this.resolution = resolution;
		calculateValues();
	}
	
	public void setHeight(int index) {
		this.chosenHeight = HEIGHTS[index];
		calculateValues();
	}
	
	public void setChosenCollageRows(int index) {
		this.chosenCollageRows = COLLAGE_ROWS[index];
		calculateValues();
	}
	
	public void setFormat(int index) {
		chosenFormat = FORMATS[index];
		this.format = chosenFormat[0] / chosenFormat[1];
		calculateValues();
	}
	
	// calculate sizes
	private void calculateValues() {
		resultHeight = (int) ((Math.ceil((chosenHeight / INCH) * resolution / chosenCollageRows)) * chosenCollageRows);
		resultWidth = (int) ((Math.ceil(resultHeight * format / chosenCollageRows)) * chosenCollageRows);
		directoryName = "/" +
						(int) chosenHeight +
						"cm_" +
						(int) chosenFormat[0] +
						"x" +
						(int) chosenFormat[1] +
						"_" +
						chosenCollageRows +
						"x" +
						chosenCollageRows +
						"_" +
						(int) resolution + 
						"dpi";
	}
}
