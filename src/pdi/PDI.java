package pdi;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

public class PDI {

	/*
	 * BEGIN OF READ IMAGE FILE
	 */
	
	private static LookupTable HEIGHT_TABLE = new LookupTable();

	public static double[][] lerImagem(String caminho) throws RuntimeException {
		File f = new File(caminho);
		BufferedImage image;

		try {
			image = ImageIO.read(f);
		} catch (IOException e) {
			throw new RuntimeException("Erro ao tentar ler o arquivo. CAUSE: "
					+ e.getCause());
		}

		double[][] retorno = new double[image.getHeight()][image.getWidth()];

		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				double[] tmp = new double[1];
				image.getRaster().getPixel(i, j, tmp);
				retorno[j][i] = tmp[0];
			}
		}

		return retorno;
	}

	public static double[][] lerImagem(BufferedImage image) {
		double[][] retorno = new double[image.getHeight()][image.getWidth()];

		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				double[] tmp = new double[1];
				image.getRaster().getPixel(i, j, tmp);
				retorno[j][i] = tmp[0];
			}
		}

		return retorno;
	}

	public static double[][][] lerImagemColorida(String caminho) {
		File f = new File(caminho);
		BufferedImage image = null;

		try {
			image = ImageIO.read(f);
		} catch (IOException e) {
			throw new RuntimeException("Erro ao tentar ler o arquivo. CAUSE: "
					+ e.getCause());
		}

		double[][][] retorno = new double[3][image.getHeight()][image
				.getWidth()];

		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				double[] tmp = new double[3];
				image.getRaster().getPixel(i, j, tmp);
				retorno[0][j][i] = tmp[0];
				retorno[1][j][i] = tmp[1];
				retorno[2][j][i] = tmp[2];
			}
		}

		return retorno;
	}

	public static void salvaImagem(String caminho, double[][] imagem) {
		BufferedImage image = new BufferedImage(imagem[0].length,
				imagem.length, BufferedImage.TYPE_BYTE_GRAY);

		for (int y = 0; y < imagem.length; ++y) {
			for (int x = 0; x < imagem[0].length; ++x) {
				double[] tmp = new double[] { imagem[y][x] };
				image.getRaster().setPixel(x, y, tmp);
			}
		}

		try {
			ImageIO.write(image, "jpg", new File(caminho));
		} catch (IOException e) {
			throw new RuntimeException(
					"Erro ao tentar escrever no arquivo. CAUSE: "
							+ e.getCause());
		}
	}

	public static void salvaImagem(String caminho, double[][][] imagem) {
		BufferedImage image = new BufferedImage(imagem[0][0].length,
				imagem[0].length, 5);

		for (int y = 0; y < imagem[0].length; ++y) {
			for (int x = 0; x < imagem[0][0].length; ++x) {
				double[] tmp = new double[] { imagem[0][y][x], imagem[1][y][x],
						imagem[2][y][x] };
				image.getRaster().setPixel(x, y, tmp);
			}
		}

		try {
			ImageIO.write(image, "bmp", new File(caminho));
		} catch (IOException e) {
			throw new RuntimeException(
					"Erro ao tentar escrever no arquivo. CAUSE: "
							+ e.getCause());
		}
	}

	public static double[][] retornaImagemCinza(double[][][] imagem) {
		BufferedImage image = new BufferedImage(imagem[0][0].length,
				imagem[0].length, BufferedImage.TYPE_BYTE_GRAY);

		for (int y = 0; y < imagem[0].length; ++y) {
			for (int x = 0; x < imagem[0][0].length; ++x) {
				double[] tmp = new double[] { imagem[0][y][x], imagem[1][y][x],
						imagem[2][y][x] };
				image.getRaster().setPixel(x, y, tmp);
			}
		}

		return lerImagem(image);
	}

	/*
	 * END OF READ/WRITE IMAGE
	 */

	/*
	 * BEGIN OF UTILITY IMAGES
	 */

	private static double[][] createWhiteImage(int width, int height) {
		double[][] ret = new double[width][height];

		for (int i = 0; i < ret.length; i++) {
			for (int j = 0; j < ret[0].length; j++) {
				ret[i][j] = 255;
			}
		}

		return ret;
	}

	public static double[][] nullsDouble(int xSize, int ySize) {
		double[][] retorno = new double[xSize][ySize];

		for (int i = 0; i < retorno.length; i++) {
			for (int j = 0; j < retorno[0].length; j++) {
				retorno[i][j] = -1;
			}
		}

		return retorno;
	}

	public static double[][] zeros(int xSize, int ySize) {
		return new double[xSize][ySize];
	}

	/*
	 * END OF UTILITY IMAGES
	 */

	/*
	 * BEGIN OF STATISTICS AREA
	 */

	private static double getDesvioPadrao(double[][] subImage, double avg) {

		double ret = 0.0;
		// double avg = getAvarage(subImage);
		for (int i = 0; i < subImage.length; i++) {
			for (int j = 0; j < subImage[0].length; j++) {
				ret += Math.pow((subImage[i][j] - avg), 2);
			}
		}
		ret /= ((subImage.length * subImage[0].length) + 1);
		return Math.sqrt(ret);
	}

	private static double getAvarage(double[][] subImage) {

		double ret = 0.0;

		for (int i = 0; i < subImage.length; i++) {
			for (int j = 0; j < subImage[0].length; j++) {
				ret += subImage[i][j];
			}
		}
		ret /= (subImage.length * subImage[0].length);
		return ret;
	}

	/**
	 * Retorna o pixel de maior valor numa imagem
	 * 
	 * @param image
	 * @return
	 */
	private static double getHighestPixel(double[][] image) {
		double ret = Double.MIN_VALUE;
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				if (ret < image[i][j]) {
					ret = image[i][j];
				}
			}
		}

		return ret;
	}

	private static double getLowestPixel(double[][] image) {
		double ret = Double.MAX_VALUE;

		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				if (image[i][j] < ret) {
					ret = image[i][j];
				}
			}
		}

		return ret;
	}

	private static int[] getHistogram(double[][] image) {
		int[] ret = new int[(int) (getHighestPixel(image) + 1)];

		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				ret[(int) image[i][j]]++;
			}
		}

		return ret;
	}

	/**
	 * Calcular a probabilidade de cada intensidade de cinza da imagem aparece
	 * na imagem
	 * 
	 * @param histogram
	 * @param pixels
	 * @return
	 */
	private static double[] probabilidade(int[] histogram, int pixels) {
		double[] ret = new double[histogram.length];
		for (int i = 0; i < histogram.length; i++) {

			ret[i] = (double) histogram[i] / pixels;
		}

		return ret;
	}

	/**
	 * Equalização do Histograma.
	 * 
	 * @param image
	 * @return double[][] imagem
	 */

	public static double[][] histogramEqualization(double[][] image) {

		int[] histogram = getHistogram(image);
		// double[] probabilidade = new double[histogram.length];

		int size = image.length * image[0].length;
		double[] probabilidade = probabilidade(histogram, size);

		double[][] ret = new double[image.length][image[0].length];

		// calculando a probabilidade para cada valor de pixel aparecer na
		// imagem

		// calculando a probabilidade acumulativa
		int[] cumulativeProbabilidade = cumulativeDistribution(probabilidade,
				size);

		// mapear os valores de níveis de cinza para os novos valores
		// printHistogram(cumulativeProbabilidade);
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				int pixelValue = (int) image[i][j];

				ret[i][j] = cumulativeProbabilidade[pixelValue];

			}
		}


		return ret;
	}

	private static double[][] normalization(double[][] image,
			double[][] background) {
		double[][] ret = new double[image.length][image[0].length];

		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				image[i][j] = (image[i][j] > 255) ? 255 : image[i][j];
				background[i][j] = (background[i][j] > 255) ? 255
						: background[i][j];
				ret[i][j] = (double) ((image[i][j] + 1) / (background[i][j] + 1));

				// System.out.println(image[i][j] +"\t " +background[i][j] );
				// System.out.println(background[i][j]);
			}
		}

		return ret;
	}

	public static double[][] imageNormalization(double[][] image,
			double[][] background) {
		System.out.println("Image Normalization has begun! " + printTime());
		double iMax = getHighestPixel(image);
		double iMin = getLowestPixel(image);
		double[][] normalized = normalization(image, background);
		double fMax = getHighestPixel(normalized);
		double fMin = getLowestPixel(normalized);
		double[][] ret = new double[image.length][image[0].length];
		// System.out.println(fMax);
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				// double q = ((double)((normalized[i][j] - fMin)/(fMax -
				// fMin)));
				// System.out.println(q);
				ret[i][j] = (iMax - iMin)
						* (double) ((double) (normalized[i][j] - fMin) / (fMax - fMin))
						+ iMin;

			}
		}
		System.out.println("Image Normalization has finished! " + printTime());
		return ret;
	}

	/*
	 * transformar as probabilidades, somando a atual com a anterior. depois
	 * multiplicar pela quantidade de tons de cinza e arrendondar esse valor.
	 */

	private static int[] cumulativeDistribution(double[] probabilidade,
			int pixels) {
		int[] ret = new int[probabilidade.length];
		double cumulative = 0;

		for (int i = 0; i < probabilidade.length; i++) {

			cumulative += (double) (probabilidade[i]);

			ret[i] = (int) Math.round(cumulative * (probabilidade.length - 1));

			// System.out.println(ret[i]);
		}

		return ret;
	}

	/*
	 * BEGIN OF THRESHOLDING AREA
	 */

	public static double[][] binaryImage(double[][] image) {
		double min = 0.0;
		double max = 255.0;
		double[][] ret = new double[image.length][image[0].length];
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {				
					ret[i][j] = (image[i][j] == max) ? 0 : 1;
			}
		}
		return ret;
	}

	private static double[][] threshImage(double[][] image, int threshold) {
		double[][] ret = new double[image.length][image[0].length];

		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				ret[i][j] = (image[i][j] <= threshold) ? 0 : 255;
			}
		}

		return ret;
	}
	
	private static int[][] threshImage(int[][] image, int threshold) {
		int[][] ret = new int[image.length][image[0].length];

		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				//ret[i][j] = (image[i][j] <= threshold) ? 0 : 255;
				if(image[i][j] == -1){
					ret[i][j] = 0;
					continue;
				}
				//ret[i][j] = table.getHeightByLabel((table.getAssociatedValue(image[i][j]))) >= threshold) ? 255 : 0;
				ret[i][j] = HEIGHT_TABLE.getMeasureByLabel(HEIGHT_TABLE.getAssociatedValue(image[i][j])) >= threshold ? 255 : 0;
			}
		}

		return ret;
	}


	public static double[][] inpainting(double[][] image, double[][] mask) {

		System.out.println("Background Estimation has begun! " + printTime());

		int radius = 1;
		double[][] tempImage = new double[image.length][image[0].length];

		double[][] background = new double[image.length][image[0].length];
		for (int l = 0; l < background.length; l++) {
			for (int k = 0; k < background[0].length; k++) {
				background[l][k] = Double.MAX_VALUE;
			}
		}

		// double[][] binary = PDI.binaryImage(mask);
		double[][] tempMask = mask.clone();
		for (int i = radius; i < background.length - radius; i++) {
			for (int j = radius; j < background[0].length - radius; j++) {
				if (tempMask[i][j] == 0) {

					double x1 = image[i][j - 1] * tempMask[i][j - 1];
					double x2 = image[i - 1][j] * tempMask[i - 1][j];
					double x3 = image[i][j + 1] * tempMask[i][j + 1];
					double x4 = image[i + 1][j] * tempMask[i + 1][j];

					double avg = (double) (x1 + x2 + x3 + x4) / (4);

					tempImage[i][j] = avg;
					if (avg < background[i][j]) {
						background[i][j] = avg;
					}
					tempMask[i][j] = 1;
				}
			}
		}

		tempMask = mask.clone();
		for (int i = radius; i < background.length - radius -1; i++) {
			for (int j = background[0].length - radius -1 ; j > 0; j--) {
				if (tempMask[i][j] == 0) {

					double x1 = image[i][j - 1] * tempMask[i][j - 1];
					double x2 = image[i - 1][j] * tempMask[i - 1][j];
					double x3 = image[i][j + 1] * tempMask[i][j + 1];
					double x4 = image[i + 1][j] * tempMask[i + 1][j];
					double avg = (double) (x1 + x2 + x3 + x4) / (4);

					tempImage[i][j] = avg;
					if (avg < background[i][j]) {

						background[i][j] = avg;
					}
					tempMask[i][j] = 1;
				}
			}
		}
		tempMask = mask.clone();
		for (int i = background.length - radius - 1; i > 0; i--) {
			for (int j = radius; j < background[0].length - radius - 1; j++) {
				if (tempMask[i][j] == 0) {

					double x1 = image[i][j - 1] * tempMask[i][j - 1];
					double x2 = image[i - 1][j] * tempMask[i - 1][j];
					double x3 = image[i][j + 1] * tempMask[i][j + 1];
					double x4 = image[i + 1][j] * tempMask[i + 1][j];

					double avg = (double) (x1 + x2 + x3 + x4) / (4);

					tempImage[i][j] = avg;
					if (avg < background[i][j]) {

						background[i][j] = avg;
					}
					tempMask[i][j] = 1;
				}
			}
		}
		tempMask = mask.clone();
		for (int i = background.length - radius -1; i > 0; i--) {
			for (int j = background[0].length - radius - 1; j > 0; j--) {
				if (tempMask[i][j] == 0) {

					double x1 = image[i][j - 1] * tempMask[i][j - 1];
					double x2 = image[i - 1][j] * tempMask[i - 1][j];
					double x3 = image[i][j + 1] * tempMask[i][j + 1];
					double x4 = image[i + 1][j] * tempMask[i + 1][j];

					double avg = (double) (x1 + x2 + x3 + x4) / (4);

					tempImage[i][j] = avg;
					if (avg < background[i][j]) {

						background[i][j] = avg;
					}
					tempMask[i][j] = 1;
				}
			}
		}
		System.out.println("Background Estimation has finished! "
				+ printTime());
		return background;

	}

	public static double[][] otsuMethod(double[][] image) {
		System.out.println("Otsu method has begun!  " + printTime());
		int[] histogram = getHistogram(image);
		// total de pixels da imagem
		int total = image.length * image[0].length;
		int lowestPixel = (int) getLowestPixel(image);
		int highestPixel = (int) getHighestPixel(image);
		// probabilidade do grupo 0
		int w0 = 0;
		// probabilidade do grupo 1
		int w1 = 0;

		int threshold = 0;

		double sum = 0;
		double sum0 = 0;
		// media do grupo 0
		double m0 = 0;
		// media do grupo 1
		double m1 = 0;
		double varMax = 0;
		double varianceBetweenClasses = 0.0;

		for (int i = lowestPixel; i < highestPixel; i++) {
			sum += i * histogram[i];
		}

		for (int i = 0; i < 256; i++) {
			w0 += histogram[i];

			if (w0 == 0)
				continue;
			// calculo da probabilidade do grupo 1
			w1 = total - w0;
			// condição de parada, pois não há mais dois grupos
			if (w1 == 0)
				break;

			sum0 += (double) (i * histogram[i]);
			m0 = sum0 / w0;
			m1 = (sum - sum0) / w1;

			varianceBetweenClasses = (double) w0 * (double) w1 * (m0 - m1)
					* (m0 - m1);

			if (varianceBetweenClasses > varMax) {
				varMax = varianceBetweenClasses;
				threshold = i;
			}
		}

		double[][] ret = inverse(threshImage(image, threshold));
		System.out.println("Otsu method has finished!  " + printTime());
		return ret;

	}

	/**
	 * 
	 * @param image
	 * @param radius
	 * @return
	 */

	public static double[][] niblackMethod(double[][] image, int radius, double kt, boolean inverse) {

		int windowSize = 2 * radius + 1;
		System.out.println("Niblack has begun! Radius: " + radius + " "
				+ printTime());
		// double[][] ret = new double[image.length][image[0].length];
		double[][] ret = createWhiteImage(image.length, image[0].length);
		double[][] window = new double[windowSize][windowSize];
		double min = 0;
		double max = 255;
		
		for (int i = radius; i < image.length - radius; i++) {
			for (int j = radius; j < image[0].length - radius; j++) {								
				for (int k = 0; k < window.length; k++) {
					for (int l = 0; l < window[0].length; l++) {
						if (k + i < image.length - radius && l + j < image[0].length - radius){
							window[k][l] = image[k + i][l + j];							
						}						
					}
					
				}
				double avg = getAvarage(window);				
				double desvio = getDesvioPadrao(window, avg);
				double threshold = avg + kt * desvio;
				ret[i][j] = (image[i][j] >= threshold) ? max : min;
			}
			

		}
		
		
		if (inverse) {
			ret = inverse(ret);
		}
		System.out.println("Niblack has finished! Radius: " + radius + " "
				+ printTime());
		//printTime();
		return ret;
	}

	public static double[][] inverse(double[][] image) {
		double[][] ret = new double[image.length][image[0].length];
		double min = 0.0;
		double max = 255.0;
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				ret[i][j] = (image[i][j] == min) ? max : min;
			}
		}

		return ret;
	}
	
	public static double[][] inverse(int[][] image) {
		double[][] ret = new double[image.length][image[0].length];
		double min = 0.0;
		double max = 255.0;
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				ret[i][j] = (image[i][j] == min) ? max : min;
			}
		}

		return ret;
	}

	public static double[][] savoula(double[][] image, int radius) {
		
		double[][] ret = new double[image.length][image[0].length];
		double kt = 0.5;
		double r = 128;		
		double max = 255;
		double min = 0;
		double[][] window = new double[2 * radius + 1][2 * radius + 1];
		System.out.println("Savoula has begun! "+ printTime());
		
		for (int i = radius; i < image.length - radius; i++) {
			for (int j = radius; j < image[0].length - radius; j++) {								
				for (int k = 0; k < window.length; k++) {
					for (int l = 0; l < window[0].length; l++) {
						if (k + i < image.length - radius && l + j < image[0].length - radius){
							window[k][l] = image[k + i][l + j];							
						}						
					}
					
				}
				double avg = getAvarage(window);				
				double desvio = getDesvioPadrao(window, avg);
				//double threshold = avg + kt * desvio;
				double threshold = avg * ( 1 + kt * ((desvio/r) - 1) );
				//System.out.println(threshold + "  " + image[i][j] );
				
				ret[i][j] = (image[i][j] >= threshold) ? max : min;
				
			}
			

		}
		System.out.println("Savoula has finished! "+ printTime());
		return ret;
	}

	/*
	 * public static double[][] binaryImage(double[][] image){ int threshold =
	 * otsuMethod(image); //double[][] ret = new
	 * double[image.length][image[0].length]; double[][] ret =
	 * createWhiteImage(image.length, image[0].length); double min = 0; double
	 * max = 255; for (int i = 0; i < image.length; i++) { for (int j = 0; j <
	 * image[0].length; j++) { ret[i][j] = (image[i][j] >= threshold) ? max :
	 * min; } }
	 * 
	 * return ret; }
	 */
	/*
	 * END OF THRESHOLDING AREA
	 */

	/*
	 * BEGIN OF FILTERS
	 */

	public static double[][] laplacean(double[][] image) {
		double[][] ret = new double[image.length][image[0].length];
		for (int i = 0; i < ret.length; i++) {
			for (int j = 0; j < ret[0].length; j++) {
				if ((i + 1 < ret.length - 10 && j + 1 < ret[0].length - 10
						&& i - 1 >= 0 && j - 1 >= 0)) {
					// System.out.println(i + "\t" + j);
					double temp = image[i + 1][j] + image[i - 1][j]
							+ image[i][j + 1] + image[i][j - 1] - 4
							* image[i][j];

					if (temp >= 0) {
						ret[i][j] = temp;
					} else {
						ret[i][j] = 0;
					}
				} else {
					ret[i][j] = 0;
				}
			}
		}

		return ret;
	}

	public static double[][] medianFilter(double[][] image, int radius) {

		double[][] ret = new double[image.length][image[0].length];
		double[][] mask = new double[2 * radius + 1][2 * radius + 1];

		for (int i = radius; i < image.length - (2 * radius + 1); i++) {
			for (int j = radius; j < image[0].length - (2 * radius + 1); j++) {
				double[] array = new double[mask.length * mask.length];
				int count = 0;
				for (int k = 0; k < mask.length; k++) {
					for (int l = 0; l < mask[0].length; l++) {

						array[count] = image[k + i][l + j];
						count++;
					}
					// System.out.println(count);
				}
				Arrays.sort(array);
				ret[i][j] = array[array.length / 2];
			}
		}

		return ret;
	}
	

	/*
	 * END OF FILTERS
	 */


	
	/*
	 * BEGIN OF GLOBAL BINARIZATION
	 */
	
	public static double[][] globalBinarization(double[][] image, int radius) {
		double[][] ret = new double[image.length][image[0].length];
		System.out.println("Global Binarization has begun!  " + printTime());
		int[][] temp = assignLabels(image, radius);	
		
		int Measure = getBestMeasure(temp);
		
		System.out.println("Measure: " + Measure);
		ret = inverse(threshImage(temp, Measure));
		
		System.out.println("Global Binarization has finished!  " + printTime());
		return ret;
	}
	
	
	/**
	 * Vai agrupar pixels por subconjuntos através da vizinha-4
	 * @param image
	 * @return
	 */
	private static int[][] assignLabels(double[][] image, int radius){
		int[][] ret = new int[image.length][image[0].length];
		int bg = -1;
		int label = 1;		
		
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				if(image[i][j] == 255){
					image[i][j] = bg;
				}				
				ret[i][j] = (int)image[i][j];				
			}
		}
		
		
		for (int i = radius+1; i < image.length-radius; i++) {
			for (int j = radius+1; j < image[0].length-radius; j++) {
				if(image[i][j] == 255 || image[i][j] == bg){ // se for background
					ret[i][j] = bg; 
				}
				
				// a classificacao será feita de acordo com o pixel acima e o pixel a esquerda do pixel central da vizinhança de 4
				else{
					// pixel à esquerda e a acima são bg, pixel central ganha label
					if(ret[i-1][j] == bg && ret[i][j-1] == bg){
						HEIGHT_TABLE.addRelation(label,label);
						ret[i][j] = label++;
					}
					//pixel a esquerda já esta marcado, então pixel central recebe o valor do label do pixel da esquerda
					else if(ret[i-1][j] != bg && ret[i][j-1] == bg){
						ret[i][j] = ret[i-1][j];
					}
					else if(ret[i][j-1] != bg && ret[i-1][j] == bg){
						ret[i][j] = ret[i][j-1];
					}
					else if(ret[i][j-1] != bg && ret[i-1][j] != bg){
						if(ret[i][j-1] != ret[i-1][j]){
							HEIGHT_TABLE.addRelation(ret[i][j-1], ret[i-1][j]);
						}
						
						ret[i][j] = ret[i-1][j];
						//ret[i][j] = label;
					}
					
				}
			}
		}
		
		/*
		for (int i = 0; i < ret.length; i++) {
			for (int j = 0; j < ret[0].length; j++) {
				if(ret[i][j] == bg){
					System.out.print("0  ");
				}
				else{
					System.out.print(ret[i][j] + "  ");
					
				}
			}
			System.out.println("");
		}
		System.out.println();
		 */
		// label = quantidade de subconjuntos
		HEIGHT_TABLE.map(label);
		// segundo passo, reduzir a quantidade de labels
		//System.out.println(HEIGHT_TABLE.getReducedHEIGHT_TABLESize());
		for (int i = 0; i < ret.length; i++) {
			for (int j = 0; j < ret[0].length; j++) {				
				ret[i][j] = (ret[i][j] != bg && ret[i][j] != 0) ? HEIGHT_TABLE.getAssociatedValue(ret[i][j]) : 0;	
			}
		}
		/*
		for (int i = 0; i < ret.length; i++) {
			for (int j = 0; j < ret[0].length; j++) {
				if(ret[i][j] == bg){
					System.out.print("0  ");
				}
				else{
					System.out.print(ret[i][j] + "  ");
					
				}
			}
			System.out.println("");
		}
		System.out.println();
		 */
		
		// Metodo pra calcular a altura de cada Label 
		HEIGHT_TABLE.associateLabelWithMeasure(ret);
		HEIGHT_TABLE.calculateNumberOfForeGroundPixel(ret, radius);
		return ret;
	}
	
	
	/**
	 * Count the number of connected pixels which its height is equal of method's parameter.
	 * @param image
	 * @param altura
	 * @return
	 */
	private static int countConnected(int[][] image, int altura) {
		int count = 0;
		
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				int temp = HEIGHT_TABLE.getAssociatedValue(image[i][j]);
				if(temp > 0 && HEIGHT_TABLE.getMeasureByLabel(temp) == altura){
					count++;
				}
			}
		}
		
		
		return count;
	}
	
	
	private static int getBestMeasure(int[][] image){
		// o algoritmo diz pra iniciar a altura = 1 
		int ret = 1;
		double razaoRP = 0;
		double razaoRC = 1;
		while (razaoRP < razaoRC) {
			razaoRP = razaoRP(image, ret);
			razaoRC = razaoRC(ret);
		
			ret++;
		}
		return ret;
	}

	private static double razaoRP(int[][] image, int altura) {
		int countConnected = countConnected(image, altura);
		int count1Geral = HEIGHT_TABLE.getNumberOfConnectedPixels();		
		double ret = (double) countConnected / (count1Geral);
		return ret;
	}

	private static double razaoRC(int altura) {		
		int ncj = HEIGHT_TABLE.getNumberOfLabeledByMeasure(altura);
		int nco = HEIGHT_TABLE.getReducedTableSize();		
		double ret = ((double)ncj / nco) ;		
		return ret;
	}
	
	/*
	 * END OF GLOBAL BINARIZATION
	 */
	


	/*
	 * BEGIN OF MORPHOLOGY
	 */

	public static double[][] dilatation(double[][] image) {
		double[][] ret = new double[image.length][image[0].length];
		/*
		 * double[][] se = { {255,0,255}, {0,0,0}, {255,0,255} };
		 */
		double[][] se = { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };

		for (int i = 1; i < image.length - 1; i++) {
			for (int j = 1; j < image[0].length - 1; j++) {
				if (image[i - 1][j] == se[0][1]
						|| image[i - 1][j - 1] == se[0][0]
						|| image[i - 1][j + 1] == se[1][1]
						|| image[i][j - 1] == se[1][0]
						|| image[i][j] == se[1][1]
						|| image[i][j + 1] == se[0][2]
						|| image[i + 1][j - 1] == se[2][0]
						|| image[i + 1][j + 1] == se[2][2]
						|| image[i + 1][j] == se[2][1]

				) {
					ret[i][j] = 0;
				} else {
					ret[i][j] = 255;
				}
			}
		}

		return ret;
	}
	
	/**
	 * Zhang Suen skeletization's method
	 * @param image
	 * @return
	 */
	
	public static double[][] skeleton(double[][] image){
		//TODO : Implementar a esqueletização
		boolean hasFoundPixel;
		double[][] tmp = new double[image.length][image[0].length];
		double[][] img = binaryImage(image);;
		//double[][] ret = new double[image.length][image[0].length];
		//double[][] slidingWindow = new double[3][3];
		
		System.out.println("Skeletization has begun! " + printTime());
		do{
			hasFoundPixel = false;
			
		
			//step 1
			for (int i = 1; i < img.length - 1; i++) {
				for (int j = 1; j < img[0].length - 1; j++) {
					double p1 = img[i][j]; double p2 = img[i-1][j]; double p3 = img[i-1][j+1];
					double p4 = img[i][j+1]; double p5 = img[i+1][j+1]; double p6 = img[i+1][j];
					double p7 = img[i+1][j-1]; double p8 = img[i][j-1]; double p9 = img[i-1][j-1];
					double sum = p2 + p3 + p4 + p5 + p6 + p7 + p8 + p9;
					
					// verifica se p1 é um border pixel 
					
					if(p1 == 1 && (sum >= 2 && sum <= 6) && countNumberOfTransition(p2, p3, p4, p5, p6, p7, p8, p9) == 1 &&
							p2 * p4 * p6 == 0 && p4 * p6 * p8 == 0){
						
						tmp[i][j] = -1;
						hasFoundPixel = true;
					}
					else{
						tmp[i][j] = p1;
					}
				}
			}
			//transforming all those pixels which have been marked 
			for (int i = 1; i < tmp.length - 1; i++) {
				for (int j = 1; j < tmp[0].length - 1; j++) {
					if(tmp[i][j] == -1){
						tmp[i][j] = 0;
					}
				}
			}
			
			img = tmp.clone();
			
			//step 2
			
			for (int i = 1; i < img.length-1; i++) {
				for (int j = 1; j < img[0].length-1; j++) {
					double p1 = img[i][j]; double p2 = img[i-1][j]; double p3 = img[i-1][j+1];
					double p4 = img[i][j+1]; double p5 = img[i+1][j+1]; double p6 = img[i+1][j];
					double p7 = img[i+1][j-1]; double p8 = img[i][j-1]; double p9 = img[i-1][j-1];
					double sum = p2 + p3 + p4 + p5 + p6 + p7 + p8 + p9;
					
					if(p1 == 1 && (sum >= 2 && sum <= 6) && countNumberOfTransition(p2, p3, p4, p5, p6, p7, p8, p9) == 1 &&
							p2 * p4 * p8 == 0 && p2 * p6 * p8 == 0){
						
						tmp[i][j] = -1;
						hasFoundPixel = true;
					}
					else{
						tmp[i][j] = p1;
					}
					
				}
			}
			//transforming all those pixels which have been marked 
			for (int i = 1; i < tmp.length - 1; i++) {
				for (int j = 1; j < tmp[0].length - 1; j++) {
					if(tmp[i][j] == -1){
						tmp[i][j] = 0;
					}
				}
			}
			
			img = tmp.clone();
		
		}while(hasFoundPixel);
		
		
		
		double[][] ret = grayScale(tmp);
		System.out.println("Skeletization has finished! " + printTime());
		return ret;
	}
	
	
	public static double[][] fdp(double[][] image){
		double foreground = 0;
		double background = 255;
		
		double[][] ret = new double[image.length][image[0].length];
		
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				//System.out.println(image[i][j]);
				if(image[i][j] == 0){
					ret[i][j] = background;
				}
				else if(image[i][j] == 1){
					ret[i][j] = foreground;
					System.out.println("VAI TOMR NO CU FDP DO CARALHO!");
				}
				else{
					System.out.println("hauehua!");
				}
				
			}
		}
		
		return ret;
	}
	
	public static double[][] grayScale(double[][] image){
		double foreground = 0;
		double background = 255;
		
		double[][] ret = new double[image.length][image[0].length];
		
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				//System.out.println(image[i][j]);
				ret[i][j] = (image[i][j] == 0) ? background: foreground;
			}
		}
		
		return ret;
				
	}
	
	private static int countNumberOfTransition(double p2, double p3, double p4, double p5, double p6, double p7, double p8, double p9){
		int ret = 0;
		
		if(p2 == 0 && p3 == 1)
			ret++;
		if(p3 == 0 && p4 == 1)
			ret++;
		if(p4 == 0 && p5 == 1)
			ret++;
		if(p5 == 0 && p6 == 1)
			ret++;
		if(p6 == 0 && p7 == 1)
			ret++;
		if(p7 == 0 && p8 == 1)
			ret++;
		if(p8 == 0 && p9 == 1)
			ret++;
		
		return ret;
	}
	
	
	
	public static double strokeWidth(double[][] skeleton, double[][] contour){
		//TODO: Implementar o Stroke Width
		
		double ret = 0;
		
		for (int i = 0; i < skeleton.length; i++) {
			for (int j = 0; j < skeleton[0].length; j++) {
				if(skeleton[i][j] == 1){
					
				}
			}
		}
		
		return ret;
	}
	
	
	
	public static double[][] contour(double[][] image){
		//TODO: Implementar algorimto de contorno
		image = binaryImage(image);
		double[][] ret = new double[image.length][image[0].length];
		
		for (int i = 1; i < image.length-1; i++) {
			for (int j = 1; j < image[0].length-1; j++) {
				if(image[i][j] == 1 &&
					(image[i-1][j-1] == 0 || image[i-1][j] == 0 || image[i-1][j+1] == 0 ||
					 image[i][j-1]   == 0 || image[i][j]   == 0 || image[i][j+1]   == 0 || 
					 image[i+1][j+1] == 0 || image[i+1][j] == 0 || image[i+1][j+1] == 0 )){
					
					//marcado como contorno, usando 8-vizinhanca
					ret[i][j] = -1;  
					
					
				}
				else{
					ret[i][j] = image[i][j];
				}
						
				
			}
		}
		
		return ret;
	}
	
	public static double[][] erode(double[][] image, int maskRadius) {
		double[][] erodedImage = image.clone();
		int windowSize = maskRadius * 2 + 1;
		double[][] mask = new double[windowSize][windowSize];
		for(int i = maskRadius; i < image.length - maskRadius; i++) {
			for(int j = maskRadius; j < image[0].length - maskRadius; j++) {
				double[][] window = getWindow(image, i, j, maskRadius);
				if(checkImageEquality(mask, window)) {
					erodedImage[i][j] = 0;
				}
			}
		}
		return erodedImage;
	}
	
	private static double[][] removeWindowLeavingPixel(double[][] image, int x, int y, double[][] window, int maskRadius) {
		double[][] returnedImage = image.clone();
		for(int i = x - maskRadius; i < x + maskRadius; i++) {
			for(int j = x - maskRadius; j < y + maskRadius; j++) {
				if(x == maskRadius && y == maskRadius) {
					returnedImage[i][j] = 0;
				} else {
					returnedImage[i][j] = 255;
				}
			}	
		}
		return returnedImage;
	}
	
	public static boolean checkImageEquality(double[][] image1, double[][] image2) {
		if(image1.length != image2.length || image1[0].length != image2.length) {
			return false;
		}
		for(int i = 0; i < image1.length; i++) {
			for(int j = 0; j < image1[0].length; j++) {
				if(image1[i][j] != image2[i][j]) {
					return false;
				}
			}
		}
		return true;
	}
	
	private static double[][] getWindow(double[][] image, int x, int y, int windowRadius) {
		int windowSize = windowRadius*2 + 1;
		double[][] window = new double[windowSize][windowSize];
		for(int i = x - windowRadius, indexX = 0; i < x + windowRadius; i++, indexX++) {
			for(int j = y - windowRadius, indexY = 0; j < y + windowRadius; j++, indexY++) {
				window[indexX][indexY] = image[i][j];
			}
		}
		return window;
	}
	
	
	private static double[][] movelAverageCalculation(double[][] image, int radius){
		double[][] ret = new double[image.length][image[0].length];
		

		for (int i = 0; i < image.length; i++) {
			double acc = 0;
			for (int j = 0; j < image[0].length; j++) {
				
				if(j < radius - 1){
					ret[i][j] = image[i][j];
				}
				
				else{
					if( j == radius - 1){
						for (int j2 = j; j2 >= 0; j2--) {
							acc += image[i][j2];
						}
					}
					else{
						acc += image[i][j];
						acc -= image[i][j-radius];
					}
					//System.out.println(acc/radius);
					ret[i][j] = (image[i][j] > (acc)/radius) ? 1 : 0;
					//System.out.println(ret[i][j]);
					
				}
			}
		}
		
		
		return ret;
	}
	
	
	public static double[][] movelAverage(double[][] image, int radius){
		System.out.println("begin");
		double[][] ret = new double[image.length][image[0].length];
		double[][] otsu = inverse(otsuMethod(image));
		double[][] aux = new double[image.length][radius];
		double[][] temp = new double[image.length][radius];
		/*
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < radius - 1; j++) {
				 temp[i][j] = image[i][j];
			}
		}
		*/
		int tempRadius = radius;
		
		while(tempRadius > 0) {
			
			double[][] km = movelAverageCalculation(temp, tempRadius);
			//System.out.println(aux[0].length);
			for (int i = 0; i < km.length; i++) {
				for (int j = 0; j < km[0].length; j++) {
					aux[i][j] = km[i][j];
				}
			}
			
			tempRadius /= 2;
		}
		
		
		
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < radius - 1; j++) {
				 ret[i][j] = otsu[i][j];
			}
		}
		
		temp = movelAverageCalculation(image, radius);
		
		for (int i = 0; i < temp.length; i++) {
			for (int j = radius - 1; j < temp[0].length; j++) {
				ret[i][j] = temp[i][j];
			}
		}
		
		ret = grayScale(ret);
		System.out.println("end");
		return inverse(ret);
		
	}
	

	/*
	 * BEGIN OF UTILS FUNCTIONS
	 */

	public static String printTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String ret = "[ " + (dateFormat.format(date)) +  " ]"; 
		return ret; // 2014/08/06 15:59:48
	}
	

}
