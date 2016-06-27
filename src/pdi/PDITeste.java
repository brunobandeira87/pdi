package pdi;
import java.util.ArrayList;
import java.util.Collections;

public class PDITeste {

	public static String PATH = "/home/bandeira/Documents/university/2016.1/pdi/workspace/DocumentBinary/src/images/";
//	public static String PATH = "/Users/Vinicius/Documents/workspace/pdi/src/images/";
	public static String INPUTEXT = ".png";
	public static String OUTPUTEXT = ".jpg";
	public static String[] IMAGES = {"H01", "H02","H03", "H04", "H05", "H06", "H07", "H08", "H09", "H10" };
//	public static String[] IMAGES = {"H01"};
	
	public static void main(String[] args) {	
		ourAlgorithm();
		//f();
		
		
	
		
//		gatos();
		//freak();
	}
	
	public static void onlyOtsu(){
		for (int i = 0; i < IMAGES.length; i++) {
			double[][][] colorida = PDI.lerImagemColorida(PATH + IMAGES[i] + INPUTEXT);
			double[][] cinza = PDI.retornaImagemCinza(colorida);
			PDI.salvaImagem(PATH + IMAGES[i] + "_otsu_" + OUTPUTEXT, PDI.inverse(PDI.otsuMethod(cinza)));
			
		}
	}
	

	
	public static void ourAlgorithm(){
		for (int i = 0; i < IMAGES.length; i++) {
			System.out.println("IMAGE: " + IMAGES[i]);
			double[][][] colorida = PDI.lerImagemColorida(PATH + IMAGES[i] + INPUTEXT);
			double[][] cinza = PDI.retornaImagemCinza(colorida);
			double[][] background;
			int radius = 30;
			double kt = -0.2;
			//primeiro passo - niblack
			double[][] niblack = PDI.niblackMethod(cinza, radius, kt, false);
			PDI.salvaImagem(PATH + IMAGES[i] + "_bn" + OUTPUTEXT,niblack);
			//double[][] niblack = PDI.otsuMethod(cinza);
			niblack = PDI.dilatation(niblack);
			
				
			//segundo passo - background estimation inpainting method
			background = PDI.inpainting(cinza, niblack);
			
			//ŧerceiro passo - normalization between original and background inpainting
			double[][] normal = PDI.imageNormalization(cinza, background);
			
			//quarto passo - otsu on normalized image
			double[][] otsu = PDI.otsuMethod(normal);
			
			PDI.salvaImagem(PATH + IMAGES[i] + "_final_otsu_" + OUTPUTEXT,otsu);
			
			double[][] label = PDI.globalBinarization(otsu, radius);
			PDI.salvaImagem(PATH + IMAGES[i] + "_final_label_" + OUTPUTEXT,label);
			//break;
			
			
			
			//PDI.salvaImagem(PATH + IMAGES[i] +"_laplacean" + OUTPUTEXT,PDI.laplacean(PDI.lerImagem(PATH + IMAGES[i] + "_final_otsu" + OUTPUTEXT)));
			/*
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
	
	
	public static void f(){
		double[][] img= {
				
				{255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255},
				{255, 255, 0, 0, 255, 255, 0, 0, 255, 0, 255, 255},
				{255, 255, 0, 0, 0, 0, 0, 0, 255, 0, 255, 255},
				{255, 255, 0, 0, 0, 0, 0, 0, 255, 0, 255, 255},
				{255, 255, 255, 0, 255, 0, 255, 0, 0, 0, 0, 255},
				{255, 255, 0, 0, 255, 255, 0, 0, 0, 0, 0, 255},
				{255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255}
				
		};
		/*
		for (int i = 0; i < img.length; i++) {
			for (int j = 0; j < img[0].length; j++) {
				if(img[i][j] == 255){
					System.out.print("0  ");
				}
				else{
					System.out.print("1  ");
				}
			}
			System.out.println();
		}
		
		System.out.println("");
		*/
		double[][] ret = PDI.globalBinarization(img,0);
		/*
		for (int i = 0; i < ret.length; i++) {
			for (int j = 0; j < ret[0].length; j++) {
				if(ret[i][j] == 255){
					System.out.print("0  ");
				}
				else{
					System.out.print("1  ");
				}
			}
			System.out.println();
		}
		*/
		
		
	}
	
	public static void gatos(){
		double[][][] colorida = PDI.lerImagemColorida(PATH + IMAGES[3] + INPUTEXT);
		double[][] cinza = PDI.retornaImagemCinza(colorida);
		
		/*
		 */
		int radius = 30;
		double kt = 0.3;
		
		double[][] niblack = PDI.niblackMethod(cinza, radius, kt,false);
		//cinza = PDI.inverse(cinza);
		niblack = PDI.dilatation(niblack);
		
		PDI.salvaImagem(PATH + IMAGES[3] + "_bn" + OUTPUTEXT,niblack);
		//cinza = PDI.inverse(cinza);
		cinza = PDI.inpainting(cinza, niblack);
		PDI.salvaImagem(PATH + IMAGES[3] + "_final" + OUTPUTEXT,cinza);
		//double[][] v = PDI.lerImagem(PATH + IMAGES[0] + "_final" +  OUTPUTEXT);
		
		//PDI.salvaImagem(PATH + IMAGES[0] + "_final_inpaint" + OUTPUTEXT, PDI.inpainting(cinza, (v)));
	}

	


}
