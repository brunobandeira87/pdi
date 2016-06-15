package pdi;
import images.*;

public class PDITeste {

	public static void main(String[] args) {
		
		String path = "/home/bandeira/Documents/university/2016.1/pdi/workspace/DocumentBinary/src/images/";
		String filename = "H10";
		String inputExt = ".png";
		String outputExt = ".jpg";
				
		double[][][] colorida = PDI.lerImagemColorida(path + filename + inputExt);
		double[][] cinza = PDI.retornaImagemCinza(colorida);
		PDI.niblackMethod(cinza, 1);
		//PDI.salvaImagem(path + filename + "_gray_otsu" + outputExt, PDI.binaryImage(cinza));

	}

}
