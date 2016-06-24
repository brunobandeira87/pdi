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
	
	private static LookupTable table = new LookupTable();

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
		for (int i = 0; i < ret.length; i++) {
			for (int j = 0; j < ret.length; j++) {
				if (image[i][j] == min || image[i][j] == max) {
					ret[i][j] = (image[i][j] == min) ? 0 : 1;

				} else {
					System.out.println("ops, galera! ");
				}

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
				ret[i][j] = (table.getAssociatedValue(image[i][j]) >= threshold) ? 255 : 0;
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
				// int linha = 0;
				// int coluna = 0;
				for (int k = 0; k < window.length; k++) {
					for (int l = 0; l < window[0].length; l++) {
						if (k + i < image.length - radius
								&& l + j < image[0].length - radius)
							window[k][l] = image[k + i][l + j];
						// coluna++;
					}
					// linha++;
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
		printTime();
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

	public static double[][] savoula(double[][] image) {
		double[][] ret = new double[image.length][image[0].length];
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
	
	public static double[][] globalBinarization(double[][] image) {
		double[][] ret = new double[image.length][image[0].length];
		System.out.println("Global Binarization has begun!  " + printTime());
		int[][] temp = assignLabels(image);
		
		
		
		// aquela razão RPj/RCj 
		int height = getBestHeight(temp);
		
		
		ret = inverse(threshImage(temp, height));

		
		System.out.println("Global Binarization has finished!  " + printTime());
		return ret;
	}
	
	
	/**
	 * Vai agrupar pixels por subconjuntos através da vizinha-4
	 * @param image
	 * @return
	 */
	private static int[][] assignLabels(double[][] image){
		int[][] ret = new int[image.length][image[0].length];
		int bg = -1;
		int label = 1;
		for (int i = 1; i < image.length; i++) {
			for (int j = 1; j < image[0].length; j++) {
				if(image[i][j] == 255){ // se for background
					ret[i][j] = bg; 
				}
				// a classificacao será feita de acordo com o pixel acima e o pixel a esquerda do pixel central da vizinhança de 4
				else{
					// pixel à esquerda e a acima são bg, pixel central ganha label
					if(ret[i-1][j] == bg && ret[i][j-1] == bg){
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
						table.addRelation(ret[i][j-1], ret[i-1][j]);
						ret[i][j] = label;
					}
				}
			}
		}
		table.map();
		// segundo passo, reduzir a quantidade de labels
		for (int i = 0; i < ret.length; i++) {
			for (int j = 0; j < ret.length; j++) {
				if(ret[i][j] != bg)
				ret[i][j] = table.getAssociatedValue(ret[i][j]);
				
					
			}
		}
		// Metodo pra calcular a altura de cada Label 
		table.associateLabelWithHeight(ret);
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
				if(image[i][j] > 0 && table.getHeightByLabel((int)image[i][j]) == altura){
					count++;
				}
			}
		}
		
		return count;
	}
	
	
	private static int getBestHeight(int[][] image){
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
		int count1Geral = table.getReducedTableSize();		
		double ret = (double) countConnected / (count1Geral);
		return ret;
	}

	private static double razaoRC(int altura) {		
		int ncj = table.getNumberOfLabeledByHeight(altura);
		int nco = table.getReducedTableSize();		
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
	
	
	public static double[][] skeleton(double[][] image){
		//TODO : Implementar a esqueletização
		
		double[][] ret = new double[image.length][image[0].length];
		return ret;
	}

	/*
	 * BEGIN OF UTILS FUNCTIONS
	 */

	private static String printTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String ret = "[ " + (dateFormat.format(date)) +  " ]"; 
		return ret; // 2014/08/06 15:59:48
	}
	

}
