package pdi;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PDI {

	public static double[][] lerImagem(String caminho) throws RuntimeException {
		File f = new File(caminho);
		BufferedImage image;
		
		try {
			image = ImageIO.read(f);
		} catch (IOException e) {
			throw new RuntimeException("Erro ao tentar ler o arquivo. CAUSE: " + e.getCause());
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
			throw new RuntimeException("Erro ao tentar ler o arquivo. CAUSE: " + e.getCause());
		}
		
		double[][][] retorno = new double[3][image.getHeight()][image.getWidth()];
		
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
		BufferedImage image = new BufferedImage(imagem[0].length, imagem.length, BufferedImage.TYPE_BYTE_GRAY);
		
		for (int y = 0; y < imagem.length; ++y) {
			for (int x = 0; x < imagem[0].length; ++x) {
				double[] tmp = new double[] { imagem[y][x] };
				image.getRaster().setPixel(x, y, tmp);
			}
		}
		
		try {
			ImageIO.write(image, "jpg", new File(caminho));
		} catch (IOException e) {
			throw new RuntimeException("Erro ao tentar escrever no arquivo. CAUSE: " + e.getCause());
		}
	}
	
	public static void salvaImagem(String caminho, double[][][] imagem) {
			BufferedImage image = new BufferedImage(imagem[0][0].length, imagem[0].length, 5);
			
			for (int y = 0; y < imagem[0].length; ++y) {
				for (int x = 0; x < imagem[0][0].length; ++x) {
					double[] tmp = new double[] { imagem[0][y][x], imagem[1][y][x], imagem[2][y][x] };
					image.getRaster().setPixel(x, y, tmp);
				}
			}
		
			try {
				ImageIO.write(image, "bmp", new File(caminho));
			} catch (IOException e) {
				throw new RuntimeException("Erro ao tentar escrever no arquivo. CAUSE: " + e.getCause());
			}
	}

	public static double[][] retornaImagemCinza(double[][][] imagem) {
		BufferedImage image = new BufferedImage(imagem[0][0].length, imagem[0].length, BufferedImage.TYPE_BYTE_GRAY);

		for (int y = 0; y < imagem[0].length; ++y) {
			for (int x = 0; x < imagem[0][0].length; ++x) {
				double[] tmp = new double[] { imagem[0][y][x], imagem[1][y][x], imagem[2][y][x] };
				image.getRaster().setPixel(x, y, tmp);
			}
		}

		return lerImagem(image);
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
	
	public static int otsuMethod(double[][] image) {
		 
	    int[] histogram = getHistogram(image);
	    // total de pixels da imagem
	    int total = image.length * image[0].length;
	    int lowestPixel = (int) getLowestPixel(image);
	    int highestPixel = (int) getHighestPixel(image);
	    //probabilidade do grupo 0
	    int w0 = 0;
	    //probabilidade do grupo 1
	    int w1 = 0;
	    
	    
	    int threshold = 0;
	    
	    double sum = 0;
	    double sum0 = 0;
	    //media do grupo 0
	    double m0 = 0;
	    //media do grupo 1
	    double m1 = 0;
	    double varMax = 0;
	    double varianceBetweenClasses = 0.0;
	    
	    for(int i= lowestPixel; i< highestPixel; i++){
	    	sum += i * histogram[i];
	    }
	 
	 
	    for(int i=0 ; i<256 ; i++) {
	        w0 += histogram[i];
	        
	        if(w0 == 0) continue;
	        // calculo da probabilidade do grupo 1
	        w1 = total - w0;
	        // condição de parada, pois não há mais dois grupos
	        if(w1 == 0) break;
	 
	        sum0 += (double) (i * histogram[i]);
	        m0 = sum0 / w0;
	        m1 = (sum - sum0) / w1;
	 
	        varianceBetweenClasses = (double) w0 * (double) w1 * (m0 - m1) * (m0 - m1);
	 
	        if(varianceBetweenClasses > varMax) {
	            varMax = varianceBetweenClasses;
	            threshold = i;
	        }
	    }
	 
	    return threshold;
	 
	}
	
	
	public static int niblackMethod(double[][] image, int radius){
		int threshold = 0;
		double[][] window = new double[2 * radius + 1][2 * radius + 1];
		
		for (int i = radius; i < image.length - radius; i++) {
			for (int j = radius; j < image[0].length - radius; j++) {
				int linha = 0;
				int coluna = 0;
				for (int k = i; k < i + window.length; k++) {
					for (int l = j; l < j + window[0].length; l++) {
						window[linha][coluna] = image[k+i][l+j];
						coluna++;
					}
					linha++;
					
				}
				
				//break;
			}
			//break;
		}
		
		return threshold;
	}
	
	public static double[][] binaryImage(double[][] image){
		int threshold = otsuMethod(image);
		double[][] ret = new double[image.length][image[0].length];
		double min = 0;
		double max = 255;
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				ret[i][j] = (image[i][j] >= threshold) ?   max :  min; 
			}
		}
		
		return ret;
	}
	
	/**
	 * Retorna o pixel de maior valor numa imagem
	 * @param image
	 * @return
	 */
	private static double getHighestPixel(double[][] image){
		double ret = Double.MIN_VALUE;		
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				if(ret < image[i][j]){
					ret = image[i][j];
				}
			}			
		}
		
		return ret;
	}
	
	private static double getLowestPixel(double[][] image){
		double ret = Double.MAX_VALUE;
		
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				if(image[i][j] < ret){
					ret = image[i][j];
				}
			}
		}
		
		return ret;
	}
	
	private static int[] getHistogram(double[][] image){
		int[] ret = new int[(int)(getHighestPixel(image) + 1)];
		
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				ret[(int)image[i][j]]++; 			
			}
		}		
		
		return ret;
	}
	
	/**
	 * Calcular a probabilidade de cada intensidade de cinza da imagem aparece na imagem
	 * @param histogram
	 * @param pixels
	 * @return
	 */
	private static double[] probabilidade(int[] histogram, int pixels){
		double[] ret = new double[histogram.length];		
		for (int i = 0; i < histogram.length; i++) {
			ret[i] = (double) histogram[i] / pixels;
		}
		
		return ret;
	}

}
