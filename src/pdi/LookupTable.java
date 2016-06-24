package pdi;

import java.util.ArrayList;
import java.util.List;
import pdi.PDI;

public class LookupTable {

	private List<Label> labels;
	private int[] reducedTable;
	private int[] labelHeight;



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
	
	/**
	 * Aqui vai reduzir repetição de linha. Ou seja, 
	 * Se eu tenho a associacao 5 -> 2 e 2 -> 5, ele vai 
	 * apenas usar 2 -> 5
	 */
	public void map(){
		int maximumLabel = getMaximumLabel() +1;
		boolean[] mapBool = new boolean[maximumLabel];
		int[] allLabels = new int[maximumLabel];
		
		for (int i = 1; i < allLabels.length; i++) {
			allLabels[i] = Integer.MAX_VALUE;
		}
		for (int j = 1; j < allLabels.length; j++) {
			List<Label> temp = getLabelByNeighbours(j);
			// Verifica se um pixel tem vizinho
			if(temp.size() == 0)
				continue;
			for (Label label : temp) {
				int brother = label.getBrother();
			
				if(mapBool[brother] == false){
					allLabels[j] = brother;
					mapBool[brother] = true;
				}
				else if (mapBool[brother] && brother < allLabels[j] && brother != 0){
					allLabels[j] = brother;
				}
			}
		}
		// Existe pixels que não tem vizinho e daí eu associo ele com ele mesmo
		for (int i = 1; i < allLabels.length; i++) {
			if(allLabels[i] == Integer.MAX_VALUE){
			
				allLabels[i] = i;
			}
			
			
		}
		
		this.reducedTable = allLabels;
		this.labelHeight = new int[reducedTable.length];
		
		
	}
	
	public void associateLabelWithHeight(int[][] image){
		for (int i = 1; i < reducedTable.length; i++) {
			int maxY = Integer.MIN_VALUE;
			int minY = Integer.MAX_VALUE;
			for (int j = 0; j < image.length; j++) {
				for (int k = 0; k < image[0].length; k++) {
					if(image[j][k] == i){
						if(k > maxY){
							maxY = k;
						}
						if(k < minY){
							minY = k;
						}
					}
				}
				
			}
			
			this.labelHeight[i] = maxY - minY;
			//System.out.println(i + " -> " +  this.labelHeight[i]);
		}
		
	}
	
	public int getReducedTableSize(){
		return this.reducedTable.length;
	}
	
	private List<Label> getLabelByNeighbours(int value){
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
	
	public String getSize(){
		map();
		return "Table Size: " + getMaximumLabel();
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
