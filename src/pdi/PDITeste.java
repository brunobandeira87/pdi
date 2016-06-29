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
		//ourAlgorithm();
		//testAlgorithm();
		//doideira();
		//rola();
		anotherMethod();
		
	}
	
	public static void anotherMethod() {
		// otsu, savoula, niblack, media movel;
		// ourAlgorithm(), testAlgorithm()
		
		for (int i = 0; i < IMAGES.length; i++) {
			System.out.println("=======================================");
			System.out.println(IMAGES[i] + " has started! " + PDI.printTime());
			double[][][] colorida = PDI.lerImagemColorida(PATH + IMAGES[i] + INPUTEXT);
			double[][] cinza = PDI.retornaImagemCinza(colorida);
			
			int radius = 30;
			double kt = -0.2;
			
			double[][] otsu = PDI.binaryImage(PDI.inverse(PDI.otsuMethod(cinza)));
			double[][] savoula = PDI.binaryImage(PDI.savoula(cinza, radius));
			double[][] niblack = PDI.binaryImage(PDI.niblackMethod(cinza, radius, kt, false));
			double[][] movel = PDI.binaryImage(PDI.movelAverage(cinza, radius));
			
			double[][] testAlg = PDI.binaryImage(testAlgorithm(i));
			double[][] ourAlg = PDI.binaryImage(ourAlgorithm(i));
			
			double[][] ret = new double[cinza.length][cinza[0].length];
			/*
			PDI.salvaImagem(PATH + IMAGES[i] + "_otsu" + OUTPUTEXT,PDI.grayScale(otsu));
			PDI.salvaImagem(PATH + IMAGES[i] + "_savoula" + OUTPUTEXT,PDI.grayScale(savoula));
			PDI.salvaImagem(PATH + IMAGES[i] + "_niblack" + OUTPUTEXT,PDI.grayScale(niblack));
			PDI.salvaImagem(PATH + IMAGES[i] + "_movel" + OUTPUTEXT,PDI.grayScale(movel));
			PDI.salvaImagem(PATH + IMAGES[i] + "_test" + OUTPUTEXT,PDI.grayScale(testAlg));
			PDI.salvaImagem(PATH + IMAGES[i] + "_our" + OUTPUTEXT,PDI.grayScale(ourAlg));
			*/
			for (int x = 0; x < cinza.length; x++) {
				for (int j = 0; j < cinza[0].length; j++) {
					int count = 0;
					
					if(otsu[x][j] == 1) {
						count++;
					} else {
						count--;
					}
					
					if(savoula[x][j] == 1) {
						count++;
					} else {
						count--;
					}
					
					if(niblack[x][j] == 1) {
						count++;
					} else {
						count--;
					}
					if(movel[x][j] == 1) {
						count++;
					} else {
						count--;
					}
					if(testAlg[x][j] == 1) {
						count++;
					} else {
						count--;
					}
					if(ourAlg[x][j] == 1) {
						count++;
					} else {
						count--;
					}
					
					if(count > 0) {
						ret[x][j] = 1;
						
					} else if(count == 0) {
						ret[x][j] = otsu[x][j];
					} else {
						ret[x][j] = 0;
					}
					
				}
			}
			ret = PDI.grayScale(ret);
			PDI.salvaImagem(PATH + IMAGES[i] + "_ret" + OUTPUTEXT,ret);
			System.out.println(IMAGES[i] + " has finished! " + PDI.printTime());
			System.out.println("=======================================");
			System.out.println("");
		}
		
	}
	
	
	public static void rola(){
		for (int i = 0; i < IMAGES.length; i++) {
			double[][][] colorida = PDI.lerImagemColorida(PATH + IMAGES[i] + INPUTEXT);
			double[][] cinza = PDI.retornaImagemCinza(colorida);
			double[][] movel = PDI.savoula(cinza, 20);
			/*
			double[][] background = PDI.inpainting(cinza, movel);
			double[][] normal = PDI.imageNormalization(cinza, background);
			double[][] ot = PDI.otsuMethod(normal);
			double[][] label = PDI.globalBinarization(ot, 5);
			//label = PDI.dilatation(PDI.skeleton(label));
			 * 
			 */
			PDI.salvaImagem(PATH + IMAGES[i] + "_savoula" + OUTPUTEXT,movel);
			
			//PDI.salvaImagem(PATH + IMAGES[i] + "_movel" + OUTPUTEXT, PDI.movelAverage(cinza, 50));
			
		}
	}
	
	public static void onlyOtsu(){
		for (int i = 0; i < IMAGES.length; i++) {
			double[][][] colorida = PDI.lerImagemColorida(PATH + IMAGES[i] + INPUTEXT);
			double[][] cinza = PDI.retornaImagemCinza(colorida);
			PDI.salvaImagem(PATH + IMAGES[i] + "_otsu_" + OUTPUTEXT, PDI.inverse(PDI.otsuMethod(cinza)));
			
		}
	}
	
	public static double[][] testAlgorithm(int i) {
		//for (int i = 0; i < IMAGES.length; i++) {
			System.out.println("IMAGE: " + IMAGES[i]);
			double[][][] colorida = PDI.lerImagemColorida(PATH + IMAGES[i] + INPUTEXT);
			double[][] cinza = PDI.retornaImagemCinza(colorida);
			double[][] otsu = PDI.inverse(PDI.otsuMethod(cinza));
//			PDI.salvaImagem(PATH + IMAGES[i] + "_otsu_not_eroded" + OUTPUTEXT,otsu);
			//otsu = PDI.skeleton(otsu);
			int radius = 30;
			double[][] erode = PDI.erode(otsu, 10);
			erode = PDI.dilatation(erode);
			double[][] background = PDI.inpainting(cinza, erode);
			
			//ŧerceiro passo - normalization between original and background inpainting
			double[][] normal = PDI.imageNormalization(cinza, background);
			double[][] ot = PDI.otsuMethod(normal);
			double[][] label = PDI.globalBinarization(ot, 5);
			//label = PDI.dilatation(PDI.skeleton(label));
			
			double[][] movel = PDI.savoula(cinza, radius);
			//PDI.salvaImagem(PATH + IMAGES[i] + "_eroded1" + OUTPUTEXT,label);
			for (int j = 0; j < label.length; j++) {
				for (int j2 = 0; j2 < label[0].length; j2++) {
					if(!(j < radius || j2 < radius || j >= label.length - radius || j2 >= label[0].length - radius)){
						if(label[j][j2] == 0 || movel[j][j2] == 0) {
							label[j][j2] = 0;						
						}
						
					}
				}
			}
			label = PDI.medianFilter(label, 1);
			
			//PDI.salvaImagem(PATH + IMAGES[i] + "_eroded3" + OUTPUTEXT,label);
		//}
			return label;
	}
	

	
	public static double[][] ourAlgorithm(int i){
		//for (int i = 0; i < IMAGES.length; i++) {
			//System.out.println("IMAGE: " + IMAGES[i]);
			double[][][] colorida = PDI.lerImagemColorida(PATH + IMAGES[i] + INPUTEXT);
			double[][] cinza = PDI.retornaImagemCinza(colorida);
			double[][] background;
			int radius = 30;
			double kt = -0.2;
			//primeiro passo - niblack
			double[][] niblack = PDI.niblackMethod(cinza, radius, kt, false);
			//PDI.salvaImagem(PATH + IMAGES[i] + "_bn" + OUTPUTEXT,niblack);
			//double[][] niblack = PDI.otsuMethod(cinza);
			niblack = PDI.dilatation(niblack);
			
				
			//segundo passo - background estimation inpainting method
			background = PDI.inpainting(cinza, niblack);
			
			//ŧerceiro passo - normalization between original and background inpainting
			double[][] normal = PDI.imageNormalization(cinza, background);
			
			//quarto passo - otsu on normalized image
			double[][] otsu = PDI.otsuMethod(normal);
			
			//PDI.salvaImagem(PATH + IMAGES[i] + "_final_otsu_" + OUTPUTEXT,otsu);
			
			double[][] label = PDI.globalBinarization(otsu, radius);
			//PDI.salvaImagem(PATH + IMAGES[i] + "_gatos" + OUTPUTEXT,label);
			
			//double[][] skeleton = PDI.skeleton(label);
			
			//double[][] contour = PDI.contour(label);
			//PDI.strokeWidth(skeleton, contour);
			//PDI.salvaImagem(PATH + IMAGES[i] + "_gatos" + OUTPUTEXT,skeleton);
			
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
			
		//}
			return label;
	}
	
	
	public static void doideira(){
		for (int i = 0; i < IMAGES.length; i++) {
			System.out.println("IMAGE: " + IMAGES[i]);
			double[][][] colorida = PDI.lerImagemColorida(PATH + IMAGES[i] + INPUTEXT);
			double[][] cinza = PDI.retornaImagemCinza(colorida);
			double[][] background;
			int radius = 30;
			double kt = -0.2;
			//primeiro passo - niblack
			//double[][] niblack = PDI.niblackMethod(cinza, radius, kt, false);
			//PDI.salvaImagem(PATH + IMAGES[i] + "_bn" + OUTPUTEXT,niblack);
			//double[][] niblack = PDI.otsuMethod(cinza);
			//niblack = PDI.dilatation(niblack);
			
				
			//segundo passo - background estimation inpainting method
			//background = PDI.inpainting(cinza, niblack);
			
			//ŧerceiro passo - normalization between original and background inpainting
			//double[][] normal = PDI.imageNormalization(cinza, background);
			
			//quarto passo - otsu on normalized image
			double[][] otsu = PDI.otsuMethod(cinza);
			
			//PDI.salvaImagem(PATH + IMAGES[i] + "_final_otsu_" + OUTPUTEXT,otsu);
			
			double[][] label = PDI.inverse(PDI.globalBinarization(otsu, radius));
			PDI.salvaImagem(PATH + IMAGES[i] + "_ours" + OUTPUTEXT,label);
			
			/*
			double[][] skeleton = PDI.skeleton(label);
			
			double[][] contour = PDI.contour(label);
			PDI.strokeWidth(skeleton, contour);
			
			PDI.salvaImagem(PATH + IMAGES[i] + "_skeleton" + OUTPUTEXT,skeleton);
			//break;
			
			*/
			
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
	
	public static void other(){
		for (int i = 0; i < IMAGES.length; i++) {
			System.out.println("IMAGE: " + IMAGES[i]);
			double[][][] colorida = PDI.lerImagemColorida(PATH + IMAGES[i] + INPUTEXT);
			double[][] cinza = PDI.retornaImagemCinza(colorida);
			double[][] background;
			
			double[][] otsu = PDI.inverse(PDI.otsuMethod(cinza));
			double[][] dilation = PDI.dilatation(otsu);
			double[][] skeleton = (PDI.dilatation(PDI.skeleton(dilation)));
			PDI.salvaImagem(PATH + IMAGES[i] + "_other" + OUTPUTEXT,(skeleton));
			
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
	
	
	public static void e(){
		
		
		double[][] img = {
				
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0},
				{0,1,1,1,0,0,0,1,1,1,1,0,0,0,0,0,1,1,1,1,0,0,1,1,1,1,0,0,0,0,0,0},
				{0,1,1,1,0,0,0,0,1,1,1,0,0,0,0,0,1,1,1,0,0,0,0,1,1,1,0,0,0,0,0,0},
				{0,1,1,1,0,0,0,1,1,1,1,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,1,1,1,0,1,1,1,1,0,0,0,0,0,0,0,1,1,1,0,0,0,0,1,1,1,0,0,0,0,0,0},
				{0,1,1,1,0,0,1,1,1,1,0,0,1,1,1,0,1,1,1,1,0,0,1,1,1,1,0,1,1,1,0,0},
				{0,1,1,1,0,0,0,1,1,1,1,0,1,1,1,0,0,1,1,1,1,1,1,1,1,0,0,1,1,1,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
				
		};
		
		for (int i = 0; i < img.length; i++) {
			for (int j = 0; j < img[0].length; j++) {
				if(img[i][j] == 1){
					System.out.print("#");
				}
				else{
					System.out.print((int)img[i][j]);
				}
			}
			System.out.println();
		}
		System.out.println();
		double[][] ret = PDI.skeleton(img);
		
		for (int i = 0; i < ret.length; i++) {
			for (int j = 0; j < ret[0].length; j++) {
				if(ret[i][j] == 1){
					System.out.print("#");
				}
				else{
					System.out.print((int)ret[i][j]);
				}
			}
			System.out.println();
		}
		
	}
	
	
	public static void eur(){
		final LookupTable table;
		double[][] image = {
				
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0},
				{0,1,1,1,0,0,0,1,1,1,1,0,0,0,0,0,1,1,1,1,0,0,1,1,1,1,0,0,0,0,0,0},
				{0,1,1,1,0,0,0,0,1,1,1,0,0,0,0,0,1,1,1,0,0,0,0,1,1,1,0,0,0,0,0,0},
				{0,1,1,1,0,0,0,1,1,1,1,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,1,1,1,0,1,1,1,1,0,0,0,0,0,0,0,1,1,1,0,0,0,0,1,1,1,0,0,0,0,0,0},
				{0,1,1,1,0,0,1,1,1,1,0,0,1,1,1,0,1,1,1,1,0,0,1,1,1,1,0,1,1,1,0,0},
				{0,1,1,1,0,0,0,1,1,1,1,0,1,1,1,0,0,1,1,1,1,1,1,1,1,0,0,1,1,1,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
				
		};
		
		
		double[][] ret = new double[image.length][image[0].length];
		
		
		for (int i = 0; i < ret.length; i++) {
			for (int j = 0; j < ret[0].length; j++) {
				if(image[i][j] == 1){
					System.out.print("#");
				}
				else{
					System.out.print(" ");
				}
			}
			System.out.println();
		}
		
		System.out.println();
		for (int i = 1; i < image.length-1; i++) {
			for (int j = 1; j < image[0].length-1; j++) {
				if(image[i][j] == 1 &&
					(image[i-1][j-1] == 0 || image[i-1][j] == 0 || image[i-1][j+1] == 0 ||
					 image[i][j-1]   == 0 || image[i][j]   == 0 || image[i][j+1]   == 0 || 
					 image[i+1][j+1] == 0 || image[i+1][j] == 0 || image[i+1][j+1] == 0 )){
					
					//marcado como contorno
					ret[i][j] = -1;  
					
					
				}
				else{
					ret[i][j] = image[i][j];
				}
						
				
			}
		}
		
		for (int i = 0; i < ret.length; i++) {
			for (int j = 0; j < ret[0].length; j++) {
				if(ret[i][j] == -1){
					System.out.print("#");
				}
				else{
					System.out.print(" ");
				}
			}
			System.out.println();
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
