package pdi;
import images.*;

public class PDITeste {

	public static String PATH = "/home/bandeira/Documents/university/2016.1/pdi/workspace/DocumentBinary/src/images/";
	public static String INPUTEXT = ".png";
	public static String OUTPUTEXT = ".jpg";
	public static String[] IMAGES = {"H01", "H02","H03", "H04", "H05", "H06", "H07", "H08", "H09", "H10" };
	public static void main(String[] args) {
		
		/*
		String filename = "";
		int windowRadius = 10;		
		double[][][] colorida = PDI.lerImagemColorida(PATH + filename + INPUTEXT);
		double[][] cinza = PDI.retornaImagemCinza(colorida);

		PDI.salvaImagem(PATH + filename + "_gray_otsu" + OUTPUTEXT, PDI.binaryImage(cinza));
		//PDI.salvaImagem(path + filename + "_gray_niblack_" + windowRadius + "_" + outputExt, PDI.niblackMethod(cinza, windowRadius));
		 
		 */
		transformToGray();

	}
	
	public static void transformToGray(){
		for (int i = 0; i < IMAGES.length; i++) {
			System.out.println(IMAGES[i] + INPUTEXT);
			double[][][] colorida = PDI.lerImagemColorida(PATH + IMAGES[i] + INPUTEXT);
			double[][] cinza = PDI.retornaImagemCinza(colorida);
			PDI.salvaImagem(PATH + IMAGES[i] + "_gray" + OUTPUTEXT, PDI.binaryImage(cinza));
			
		}
	}
	


}
