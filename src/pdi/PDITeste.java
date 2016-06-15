package pdi;
import images.*;

public class PDITeste {

	public static void main(String[] args) {
		
		String path = "";
		String filename = "";
		String inputExt = ".png";
		String outputExt = ".jpg";
				
		double[][][] colorida = PDI.lerImagemColorida(path + filename + inputExt);
		double[][] cinza = PDI.retornaImagemCinza(colorida);
		PDI.niblackMethod(cinza, 1);
		//PDI.salvaImagem(path + filename + "_gray_otsu" + outputExt, PDI.binaryImage(cinza));

	}

}
