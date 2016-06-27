package pdi;

import java.util.ArrayList;
import java.util.List;
import pdi.PDI;

public class LookupTable {

	private List<Label> labels;
	private int[] reducedTable;
	private int[] labelHeight;
	private int numberOfConnectedPixels;



	public LookupTable(){
		this.labels = new ArrayList<Label>();
	}
	
	public void printTable(){
		System.out.println(getMaximumLabel());
	}

	public void addRelation(int x, int y){
		Label l1 = new Label(x,y);
		Label l2 = new Label(y,x);

		// evita add linha repetida
		if(!this.hasRow(l1)){
			this.labels.add(l1);
		}

		// evita add linha repetida
		
		if(!this.hasRow(l2)){
			this.labels.add(l2);
		}


	}
	public int getAssociatedValue(int i){
		int ret = this.reducedTable[i];
		return ret;
	}
	
	public int getHeightByLabel(int label){
		return this.labelHeight[label];
	}
	
	public int getLabelHeightSize(){
		return this.labelHeight.length;
	}
	
	/**
	 * Aqui vai reduzir repetição de linha. Ou seja, 
	 * Se eu tenho a associacao 5 -> 2 e 2 -> 5, ele vai 
	 * apenas usar 2 -> 5
	 */
	public void map(int l){
		int maximumLabel = l  +1;
		boolean[] mapBool = new boolean[maximumLabel];
		int[] allLabels = new int[maximumLabel];
		
		for (int i = 1; i < allLabels.length; i++) {
			allLabels[i] = Integer.MAX_VALUE;
		}
		for (int j = 1; j < allLabels.length; j++) {
			List<Label> temp = getLabels(j);
			// Verifica se um pixel tem vizinho
			if(temp.size() == 0)
				continue;
			for (Label label : temp) {
				int brother = label.getBrother();
				int value = label.getValue();
				if(brother < allLabels.length){
					if(mapBool[brother] == false){
						allLabels[brother] = (brother <= value ) ? brother : allLabels[value];
						mapBool[brother] = true;
					}
					else if (mapBool[value] && allLabels[brother] < allLabels[value] && brother != 0){
						allLabels[value] = allLabels[brother];
					}
					else if(mapBool[value] && allLabels[value] < brother && brother !=0){
						allLabels[brother] = allLabels[value];
					}
					else if(mapBool[brother] && allLabels[value] < allLabels[brother] && brother !=0){
						allLabels[brother] = allLabels[value];
					}
					
				}
			}
		}
		int reducedSize = 1;
		
		for (int i = 0; i < allLabels.length; i++) {
			if(allLabels[i] != Integer.MAX_VALUE){
				reducedSize++;
			}
		}
		
		this.reducedTable = new int[reducedSize+1];
		System.out.println(reducedSize);
		// Existe pixels que não tem vizinho e daí eu associo ele com ele mesmo
		for (int i = 1; i < reducedTable.length - 1 && i < allLabels.length - 1; i++) {
			if(allLabels[i] == Integer.MAX_VALUE){			
				this.reducedTable[i] = i;
			}
			else{
				this.reducedTable[i] = allLabels[i];
			}
			
			
		}
		
		//this.reducedTable = allLabels;
		this.labelHeight = new int[maximumLabel];
		
		
	}
	
	public void calculateNumberOfForeGroundPixel(int[][] image, int radius){
		int begin = 2 * radius + 1;
		int foregroundPixels = 0;
		for (int i = begin; i < image.length-begin; i++) {
			for (int j = begin; j < image[0].length-begin; j++) {
				if(image[i][j] > 0){
					foregroundPixels++;
				}
			}
		}
		this.setNumberOfConnectedPixels(foregroundPixels);
		
	}
	
	
	public void associateLabelWithHeight(int[][] image){
		for (int i = 1; i < reducedTable.length - 1; i++) {
			if(this.reducedTable[i] != i && i < labelHeight.length){
				//this.labelHeight[i] = this.labelHeight[this.reducedTable[i]];
				// atribuindo -1 pois na hora da contagem não haver repetição
				this.labelHeight[i] = -1;
				continue;
			}
			else if(i >= labelHeight.length){
				break;
			}
			int maxY = Integer.MIN_VALUE;
			int minY = Integer.MAX_VALUE;
			for (int j = 0; j < image.length; j++) {
				for (int k = 0; k < image[0].length; k++) {
					if(image[j][k] == i){
						if(j > maxY){
							maxY = j;
						}
						if(j < minY){
							minY = j;
						}
					}
				}
				
			}
			
			this.labelHeight[i] = maxY - minY;
			
			//System.out.println(i + " -> " +  this.labelHeight[i]);
		}
		
	}
	
	public void setNumberOfConnectedPixels(int number){
		this.numberOfConnectedPixels = number;
	}
	
	public int getNumberOfConnectedPixels(){
		return this.numberOfConnectedPixels;
	}
	
	public int getReducedTableSize(){
		return this.reducedTable.length;
	}
	
	private List<Label> getLabels(int value){
		List<Label> ret = new ArrayList<>();
		
		for (Label label : labels) {
			if(label.getValue() == value){
				ret.add(label);
			}
			
		}
		
		return ret;
	}
	
	private int getMaximumLabel(){
		int ret = Integer.MIN_VALUE;
		for (Label label : labels) {
			int value = label.getValue();
			int brother = label.getBrother();
			if(value > ret || brother > ret){
				ret = (value > brother) ? value : brother;
			}
		}
		return ret;
	}
	
	

	private boolean hasRow(Label l){
		boolean ret = false;
		for (Label label : this.labels) {
			if(label.equals(l)){
				//System.out.println("YEAH!");
				ret = true;
				break;
			}
		}

		return ret;
	}
	

	
	
	public int getNumberOfLabeledByHeight(int height){
		int ret = 0;
		
		for (int i = 1; i < labelHeight.length; i++) {
			if(labelHeight[i] == height)
				ret++;
		}
		
		return ret;
	}
	


	class Label{
		private int value;
		private int brother;

		public Label(int value, int brother){
			this.value = value;
			this.brother = brother;

		}

		public boolean equals(Label l) {

			boolean ret = ( (this.value == l.getValue() && this.brother == l.getBrother()) || 
					(this.brother == l.getValue() && this.value == l.getBrother())) ? true : false;		
			return ret;
		}

		public int getValue(){
			return this.value;
		}

		public int getBrother(){
			return this.brother;
		}
		
		public String toString(){
			return this.value + "\t" + this.brother;
		}
		
		
	}


}
