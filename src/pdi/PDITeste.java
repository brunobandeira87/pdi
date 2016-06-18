package pdi;
import images.*;

public class PDITeste {

	public static String PATH = "/home/bandeira/Documents/university/2016.1/pdi/workspace/DocumentBinary/src/images/";
//	public static String PATH = "/Users/Vinicius/Documents/workspace/pdi/src/images/";
	public static String INPUTEXT = ".png";
	public static String OUTPUTEXT = ".jpg";
	public static String[] IMAGES = {"H01", "H02","H03", "H04", "H05", "H06", "H07", "H08", "H09", "H10" };
	
	public static void main(String[] args) {	
		transformToGray();
		//gatos();
		//freak();
	}
	
	public static void transformToGray(){
		for (int i = 0; i < IMAGES.length; i++) {
			System.out.println("IMAGE: " + IMAGES[i]);
			double[][][] colorida = PDI.lerImagemColorida(PATH + IMAGES[i] + INPUTEXT);
			double[][] cinza = PDI.retornaImagemCinza(colorida);
			double[][] background;
			int radius = 20;
			
			//primeiro passo - niblack
			double[][] niblack = PDI.niblackMethod(cinza, radius, false);
			niblack = PDI.dilatation(niblack);
			
			//cinza = PDI.inverse(cinza);
			
			//PDI.salvaImagem(PATH + IMAGES[i] + "_bn" + OUTPUTEXT,niblack);
			//cinza = PDI.inverse(cinza);
			
			//segundo passo - background estimation inpainting method
			background = PDI.inpainting(cinza, niblack);
			
			//ŧerceiro passo - normalization between original and background inpainting
			double[][] normal = PDI.imageNormalization(cinza, background);
			
			//quarto passo - otsu on normalized image
			
			double[][] otsu = PDI.otsuMethod(normal);
			
			
			PDI.salvaImagem(PATH + IMAGES[i] + "_final_otsu" + OUTPUTEXT,otsu);
			
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
	
	public static void gatos(){
		double[][][] colorida = PDI.lerImagemColorida(PATH + IMAGES[3] + INPUTEXT);
		double[][] cinza = PDI.retornaImagemCinza(colorida);
		
		/*
		 */
		int radius = 30;
		
		double[][] niblack = PDI.niblackMethod(cinza, radius, false);
		//cinza = PDI.inverse(cinza);
		niblack = PDI.dilatation(niblack);
		
		PDI.salvaImagem(PATH + IMAGES[3] + "_bn" + OUTPUTEXT,niblack);
		//cinza = PDI.inverse(cinza);
		cinza = PDI.inpainting(cinza, niblack);
		PDI.salvaImagem(PATH + IMAGES[3] + "_final" + OUTPUTEXT,cinza);
		//double[][] v = PDI.lerImagem(PATH + IMAGES[0] + "_final" +  OUTPUTEXT);
		
		//PDI.salvaImagem(PATH + IMAGES[0] + "_final_inpaint" + OUTPUTEXT, PDI.inpainting(cinza, (v)));
	}

	
	public static void freak(){
		double[][] image = {
								{1,1,3,2,1,6},
								{4,2,3,3,2,2},
								{1,0,5,4,6,3},
								{5,0,6,5,7,3},
								{6,7,1,7,7,0},
								{7,1,0,0,1,0},
						   };
		double[][] mask = {
				{1,1,0,0,1,0},
				{1,1,1,0,0,0},
				{1,0,1,1,0,0},
				{0,0,1,0,1,1},
				{0,0,0,1,1,0},
				{1,1,0,0,1,0},
		   };
		
	}
	public static double[][] inpainting(double[][] image, double[][] mask){
		//FIXME
		
		/**
		 * TA MEIO ESCROTO ESSA PORRA AQUI! ELE TEM QUE FAZER 4 ITERACOES
		 * 
		 * EDCB, EDBC, DECB, DEBC
		 */
		
		System.out.println("begin!");
		
		
		int radius = 1;
		int height = image[0].length;
		int width = image.length;
		
		/*
		int height = 8;
		int width = 9;
		System.out.println(image.length); //coluna
		System.out.println(image[0].length); // linha
		System.out.println(); 
		System.out.println(mask.length); //coluna
		System.out.println(mask[0].length); // linha
		 */
		double[][] tempImage = new double[image.length][image[0].length];
		int[] xstart = {radius, radius, height - radius, height - radius};
		int[] xend = {height - radius, height - radius, radius, radius};
		
		int[] ystart = {radius, width - radius, radius, width - radius};
		int[] yend = {width - radius, radius, width - radius, radius};
		int step = 0;
	
		
		double[][] background = new double[image.length][image[0].length];		
		for (int l = 0; l < background.length; l++) {
			for (int k = 0; k < background[0].length; k++) {
				background[l][k] = Double.MAX_VALUE; 
			}
		}
		
		//double[][] binary = PDI.binaryImage(mask);
		double[][] tempMask = mask.clone();
		for (int i = radius; i < background.length; i++) {
			for (int j = radius; j < background[0].length; j++) {
				if(tempMask[i][j] == 0){
				
				double x1 = image[j][i-1] * tempMask[j][i-1] ;
				double x2 = image[j-1][i] * tempMask[j-1][i];						
				double x3 = image[j][i+1] * tempMask[j][i+1];				
				double x4 = image[j+1][i] * tempMask[j+1][i];
				
				double avg = (double)(x1 + x2 + x3 + x4)/(4);
				
				
				tempImage[i][j] = avg;
				if(avg < background[i][j]){
					
					background[i][j] = avg;
				}
				tempMask[i][j] = 1;
				}
			}
		}
		
		
		for (int i = 0; i < background.length; i++) {
			for (int j = background[0].length - radius; j > 0; j--) {
				if(tempMask[i][j] == 0){
					
					double x1 = image[j][i-1] * tempMask[j][i-1] ;
					double x2 = image[j-1][i] * tempMask[j-1][i];						
					double x3 = image[j][i+1] * tempMask[j][i+1];				
					double x4 = image[j+1][i] * tempMask[j+1][i];
					
					double avg = (double)(x1 + x2 + x3 + x4)/(4);
					
					
					tempImage[i][j] = avg;
					if(avg < background[i][j]){
						
						background[i][j] = avg;
					}
					tempMask[i][j] = 1;
					}
			}
		}
		for (int i = background.length - radius; i > 0; i--) {
			for (int j = 0; j < background[0].length; j++) {
				if(tempMask[i][j] == 0){
					
					double x1 = image[j][i-1] * tempMask[j][i-1] ;
					double x2 = image[j-1][i] * tempMask[j-1][i];						
					double x3 = image[j][i+1] * tempMask[j][i+1];				
					double x4 = image[j+1][i] * tempMask[j+1][i];
					
					double avg = (double)(x1 + x2 + x3 + x4)/(4);
					
					
					tempImage[i][j] = avg;
					if(avg < background[i][j]){
						
						background[i][j] = avg;
					}
					tempMask[i][j] = 1;
					}
			}
		}
		
		for (int i = background.length - radius; i > 0; i--) {
			for (int j = background[0].length - radius; j > 0; j--) {
				if(tempMask[i][j] == 0){
					
					double x1 = image[j][i-1] * tempMask[j][i-1] ;
					double x2 = image[j-1][i] * tempMask[j-1][i];						
					double x3 = image[j][i+1] * tempMask[j][i+1];				
					double x4 = image[j+1][i] * tempMask[j+1][i];
					
					double avg = (double)(x1 + x2 + x3 + x4)/(4);
					
					
					tempImage[i][j] = avg;
					if(avg < background[i][j]){
						
						background[i][j] = avg;
					}
					tempMask[i][j] = 1;
					}
			}
		}
		return background;
		
		
		/*
		do{
			double[][] tempMask = mask.clone();			
			j = xstart[step];
			
			while(true) {
				
				if((j == xend[step] && (step == 0 || step == 1) ) || (j == xend[step] && (step == 2 || step == 3))){
					//System.out.println("morre");
					break;
				}
				
				i = (step == 0 || step == 2) ? ystart[0] : ystart[1];
				
				
				while (true) {
					if( (i == yend[step]  && (step == 0 || step == 2) || (i == yend[step] && (step == 1 || step == 3))) ){
											
						break;
					}
				
					
					//System.out.println(step + "\t" + i + "\t" + j);
					//if(tempMask[j][i] == 0){
						
						double x1 = image[j][i-1]; //* tempMask[j][i-1] ;
						double x2 = image[j-1][i]; //* tempMask[j-1][i];						
						double x3 = image[j][i+1]; //* tempMask[j][i+1];
						//System.out.println("step:" + step + "\ti:" + i + "\tj+1:" + (j+1) + "\tyend[step]:" + yend[step]);
						double x4 = image[j+1][i]; //* tempMask[j+1][i];
						System.out.println(x1 +"\t"+ x2 +"\t"+ x3+"\t"+ x4 + "\t step:" + step +"\ti:" + i + "\tj:" +j);
						double avg = (double)(x1 + x2 + x3 + x4)/(4);
						//double avg = (double)(image[j][i-1] * tempMask[j][i-1] + image[j-1][i] * tempMask[j-1][i] +  image[j][i+1] * tempMask[j][i+1] +  image[j+1][i] * tempMask[j+1][i])/(4);
						
						tempImage[j][i] = avg;
						if(avg < background[j][i]){
							
							background[j][i] = avg;
						}
						tempMask[j][i] = 1;
					//}
						
					i = (step == 0|| step == 2) ? i + 1 : i - 1;
					
					//System.out.println(i + "\t" + j);
				
					
				}
				
				j = (step == 0 || step == 1) ? j + 1 : j - 1;
			}
				
			
			System.out.println("");
			step++;
		}while(step < 4);
		*/
	
	}
}
