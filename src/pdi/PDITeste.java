package pdi;
import images.*;

public class PDITeste {

	public static String PATH = "/home/bandeira/Documents/university/2016.1/pdi/workspace/DocumentBinary/src/images/";
//	public static String PATH = "/Users/Vinicius/Documents/workspace/pdi/src/images/";
	public static String INPUTEXT = ".png";
	public static String OUTPUTEXT = ".jpg";
	public static String[] IMAGES = {"H01", "H02","H03", "H04", "H05", "H06", "H07", "H08", "H09", "H10" };
	
	public static void main(String[] args) {	
		//transformToGray();
		gatos();
	}
	
	public static void transformToGray(){
		for (int i = 0; i < 1; i++) {
			
			double[][][] colorida = PDI.lerImagemColorida(PATH + IMAGES[i] + INPUTEXT);
			double[][] cinza = PDI.retornaImagemCinza(colorida);
			
			PDI.salvaImagem(PATH + IMAGES[i] + "_niblack30" + OUTPUTEXT, PDI.niblackMethod(cinza, 30, false));
			
			//double[][] gray = PDI.lerImagem(PATH + IMAGES[i] + "_inv" + OUTPUTEXT);//dilatation
			//PDI.salvaImagem(PATH + IMAGES[i] + "_di21" + OUTPUTEXT, PDI.inverse(PDI.dilatation(gray)));
			/*
			PDI.salvaImagem(PATH + IMAGES[i] + "_gray" + OUTPUTEXT,(cinza));
			PDI.salvaImagem(PATH + IMAGES[i] + "_otsu" + OUTPUTEXT,(cinza));
			PDI.salvaImagem(PATH + IMAGES[i] + "_his" + OUTPUTEXT,PDI.histogramEqualization(cinza));			
			PDI.salvaImagem(PATH + IMAGES[i] + "_inv" + OUTPUTEXT,PDI.niblackMethod(cinza, 30, true));
			*/
			//PDI.salvaImagem(PATH + IMAGES[i] + "_niblack15" + OUTPUTEXT, PDI.niblackMethod(cinza, 15));
			
		}
	}
	
	public static void gatos(){
		double[][][] colorida = PDI.lerImagemColorida(PATH + IMAGES[0] + INPUTEXT);
		double[][] cinza = PDI.retornaImagemCinza(colorida);
		
		/*
		 */
		int radius = 30;
		
		double[][] niblack = PDI.niblackMethod(cinza, 1, false);
		//cinza = PDI.inverse(cinza);
		niblack = PDI.dilatation(niblack);
		
		PDI.salvaImagem(PATH + IMAGES[0] + "_bn" + OUTPUTEXT,niblack);
		//cinza = PDI.inverse(cinza);
		cinza = PDI.inpainting(cinza, niblack);
		PDI.salvaImagem(PATH + IMAGES[0] + "_final" + OUTPUTEXT,cinza);
		//double[][] v = PDI.lerImagem(PATH + IMAGES[0] + "_final" +  OUTPUTEXT);
		
		//PDI.salvaImagem(PATH + IMAGES[0] + "_final_inpaint" + OUTPUTEXT, PDI.inpainting(cinza, (v)));
	}

}
