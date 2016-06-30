package pdi;

public class FMeasure {

	private static final int BACKGROUND = 255;
	private static final int FOREGROUND = 0;
	
	private double[][] correctImage;
	private double[][] image;
	
	private int false_positive;
	private int false_negative;
	private int true_positive;
	
	public FMeasure(double[][] correctImage, double[][] image) {
		this.correctImage = correctImage;
		this.image = image;
	}
	
	
	private void countMeasures() {
		false_positive = 0;
		false_negative = 0;
		true_positive = 0;
		
		for (int i = 0; i < correctImage.length; i++) {
			for (int j = 0; j < correctImage[0].length; j++) {
				if(correctImage[i][j] == FOREGROUND && image[i][j] == FOREGROUND) {
					true_positive++;
				} else if(correctImage[i][j] == FOREGROUND && image[i][j] == BACKGROUND) {
					false_negative++;
				} else if(correctImage[i][j] == BACKGROUND && image[i][j] == FOREGROUND) {
					false_positive++;
				}
			}
		}
		
	}
	

	public double calculateFMeasure() {
		countMeasures();
		double recall = true_positive/(true_positive+false_negative);
		double precision = true_positive/(true_positive + false_positive);
		double fMeasures = 2*recall*precision/(recall+precision);
		return fMeasures;
	}
	
	

}
