package pdi;
import images.*;

public class PDITeste {

	public static String PATH = "/home/bandeira/Documents/university/2016.1/pdi/workspace/DocumentBinary/src/images/";
	public static String INPUTEXT = ".png";
	public static String OUTPUTEXT = ".jpg";
	public static void main(String[] args) {
		
		
		String filename = "";
		int windowRadius = 10;		
		double[][][] colorida = PDI.lerImagemColorida(PATH + filename + INPUTEXT);
		double[][] cinza = PDI.retornaImagemCinza(colorida);
		//PDI.niblackMethod(cinza, 1);
		PDI.salvaImagem(PATH + filename + "_gray_otsu" + OUTPUTEXT, PDI.binaryImage(cinza));
		//PDI.salvaImagem(path + filename + "_gray_niblack_" + windowRadius + "_" + outputExt, PDI.niblackMethod(cinza, windowRadius));

	}
	


}
