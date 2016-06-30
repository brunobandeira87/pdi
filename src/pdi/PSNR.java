package pdi;

public class PSNR {
	private double[][] correctImage;
	private double[][] image;
	
	public PSNR(double[][] correctImage, double[][] image) {
		this.correctImage = correctImage;
		this.image = image;
	}
	
	public double calculatePSR() {
		double mse = calculateMSE();
		double c = calculateC();
		return 10*Math.log10(c/mse);
	}
	
	private double calculateC() {
		double max = 0;
		double min = 1;
	
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				if(image[i][j] > max) {
					max = image[i][j];
				}
				if(image[i][j] < min) {
					min = image[i][j];
				}
			}
		}
		
		return Math.pow(max - min, 2);
	}
	
	private double countMeasures() {
		double count = 0;
		for (int i = 0; i < correctImage.length; i++) {
			for (int j = 0; j < correctImage[0].length; j++) {
				count += Math.pow(correctImage[i][j] - image[i][j], 2);
			}
		}
		return count;
	}
	

	private double calculateMSE() {
		return (double) countMeasures()/(correctImage.length*correctImage[0].length);
	}
	

}
