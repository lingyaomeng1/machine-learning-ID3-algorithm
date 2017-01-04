import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;




public class ID3 {
	
	static String[] attribute = null;
	static int nodeNum = 0;
	static int leafNum = 0;
	ArrayList<int[]> data = new ArrayList<int[]>();
	HashSet<Integer> index = new HashSet<Integer>();
//	int pp = 9;

	public static void main(String[] args){
//		System.out.println("\"" + args[0] + "\"");
		
		
//		double defaultedEntropy = 0;
//		double percent = 0;
//		
//		int posstiveNum = 0;
//		
//		int num = 0;
	
//		boolean[] attributes = new boolean[attribute.length - 1];
		
//		if(posstiveNum == 0 || posstiveNum == num){
//			defaultedEntropy = 0;
//		} else{
//			percent = (double) posstiveNum / num;
////			System.out.println(posstiveNum);
////			System.out.println(percent);
//			defaultedEntropy = -1 * ( ( percent ) * ( Math.log( percent ) / Math.log(2) ) + ( 1 - percent ) * ( Math.log( 1 - percent ) / Math.log(2) ) );
////			defaultedEntropy = Math.log( percent );
//		}
//		
//		System.out.println("defaulted entropy: " + defaultedEntropy);
		
//		System.out.println(id3.data.size());
		
		
		
//		attributes[bestAttribute] = true;
		
		ID3 id3 = new ID3();
		int[] readFile = id3.readFile(args[0]);
		
		
		Node root = id3.makeTree(id3.data);
//		System.out.println("over!");
		
		leafNum = 0;
		nodeNum = 0;
		id3.printTree(root, new HashSet<Integer>());
		System.out.println();
		System.out.println("Number of training instances: " + readFile[0]);
		System.out.println("Number of training attributes: " + readFile[1]);
		System.out.println("Total number of nodes in the tree = " + nodeNum);
		System.out.println("Number of leaf nodes in the tree = " + leafNum);
		System.out.println("Accuracy: " + id3.getAccuracy(id3.data, root, new HashSet<Integer>()));
		
		System.out.println();
		readFile = id3.readFile(args[1]);
		System.out.println("Number of testing instances: " + readFile[0]);
		System.out.println("Number of testing attributes: " + readFile[1]);
		System.out.println("Accuracy: " + id3.getAccuracy(id3.data, root, new HashSet<Integer>()));
		
		
		
		
		
		
		
		
		HashSet<Integer> pruning = new HashSet<Integer>();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("After pruning: ");
		
		pruning = id3.prune(nodeNum, Double.parseDouble(args[2]));
//		System.out.println(pruning.size());
		readFile = id3.readFile(args[0]);
		leafNum = 0;
		nodeNum = 0;
		id3.printTree(root, pruning);
		System.out.println();
		System.out.println("Number of training instances: " + readFile[0]);
		System.out.println("Number of training attributes: " + readFile[1]);
		System.out.println("Total number of nodes in the tree = " + nodeNum);
		System.out.println("Number of leaf nodes in the tree = " + leafNum);
		System.out.println("Accuracy: " + id3.getAccuracy(id3.data, root, pruning));
		
		System.out.println();
		readFile = id3.readFile(args[1]);
		System.out.println("Number of testing instances: " + readFile[0]);
		System.out.println("Number of testing attributes: " + readFile[1]);
		System.out.println("Accuracy: " + id3.getAccuracy(id3.data, root, pruning));
		
	}
	
	
	public void printTree(Node root, HashSet<Integer> index){
//		nodeNum++;
		
		if(root == null)
			return;
		nodeNum++;
//		System.out.println("xxx");
		if(index.contains(root.index)){
			leafNum++;
			System.out.println(root.label);
//			System.out.println();
			return;
		}
		if(root.attribute != -1){
			System.out.println();
			for(int i = 0 ; i < root.level ; i++){
				System.out.print("| ");
			}
//			nodeNum++;
			System.out.print(attribute[root.attribute] + " = 0 : ");
			printTree(root.lChild, index);
			
			
			for(int i = 0 ; i < root.level ; i++){
				System.out.print("| ");
			}
//			nodeNum++;
			System.out.print(attribute[root.attribute] + " = 1 : ");
			printTree(root.rChild, index);
		}else{
			leafNum++;
			System.out.println(root.label);
		}
		
	}
	
	
	
	public HashSet<Integer> prune(int rootNum, double pruningFactor){
		int temp = 0;
//		System.out.println((int)(rootNum * pruningFactor));
		for(int i = 0 ; i < (int)(rootNum * pruningFactor) ; i++ ){
			temp = (int)(Math.random() * rootNum);
//			System.out.println(temp);
			while(temp == 0 || index.contains(temp)){
				temp = (int)(Math.random() * rootNum);
			}
			index.add(temp);
//			System.out.println(temp);
		}
		
		return index;
	}
	
//	public void traversalTree
	
	public double getAccuracy(ArrayList<int[]> data, Node root, HashSet<Integer> index){
		int correctNum = 0;
		for(int[] item : data){
//			System.out.println(root.lChild.lChild.attribute);
			if(getResult(root, item, index)){
				correctNum++;
//				System.out.println("correct!");
			} else{
				
			}
		}
		return (double)correctNum/data.size();
	}
	
	public boolean getResult(Node root, int[] sample, HashSet<Integer> index){
		
//			System.out.println(root.label);
//		System.out.println(root.attribute);
//		System.out.println(sample[root.attribute]);
//		System.out.println(root.lChild.attribute);
		while(root.attribute != -1 && !index.contains(root.index)){
//			System.out.println(root.attribute);
//			System.out.println(sample[root.attribute]);
			if(sample[root.attribute] == 0){
//				System.out.println(root.lChild.attribute);
				root = root.lChild;
				
			} else{
//				System.out.println(root.rChild.attribute);
				root = root.rChild;
			}
		}
		if(root.label == sample[sample.length - 1]){
			return true;
		} else{
			return false;
		}
		
	}
	
	public int[] readFile(String file){
		data.clear();
		int num = 0;
		try {
			Scanner sc = new Scanner(new FileReader(file));
			String temp = null;
			temp = sc.nextLine();
			attribute = temp.split("\t");
//			System.out.println(temp);
//			System.out.println("\"" + args[0] + "\"");
			
			
			
			
			
			while(sc.hasNextLine()){
				temp = sc.nextLine();
				if(temp.equals(null) || temp.isEmpty()){
					continue;
				} else{
					num++;
//					System.out.println(temp);
					String[] instance = temp.split("\t");
					int[] row = new int [instance.length];
					for(int i = 0 ; i < instance.length ; i++){
						row[i] = Integer.parseInt(instance[i]);
//						if(i == instance.length - 1){
//							if(row[i] == 1){
//								posstiveNum++;
//							}
//						}
					}
					data.add(row);
				}
				
			}
			sc.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return new int[]{num, data.get(0).length - 1};
	}
	
	
	
	//create new branch, return -1 if no unused attribute and need to be labeled as 0, return -2 if no unused attribute and need to be labeled as 1; return -3 if already pure and need to be labeled as 0, return -4 if already pure and need to be labeled as 1
	public int[] newBranch(ArrayList<int[]> data, int[] attributes){

		
		int num0 = 0;
		int num1 = 0;
		double Entropy = 0;
		double minEntropy = 1;
		int bestAttribute = -1;
		int posstiveNum0 = 0;
		int posstiveNum1 = 0;
		double percent = 0;
		double percent0 = 0;
		double percent1 = 0;
		
//		System.out.println(data.size());
		ArrayList<int[]> subset = new ArrayList<int[]>();
		for(int i = 0 ; i < data.size() ; i++){
			for(int j = 0 ; j < attributes.length ; j++){
//				if(attributes[j] != -1 && data.get(i)[j] != attributes[j] && attributes[j] != 2){
//					break;
//				} else if(j == attributes.length - 1){
////					System.out.println(data.get(i)[0]);
//					subset.add(data.get(i));
//				} else{
//					continue;
//				}
				if(attributes[j] != -1){
//					System.out.println("q");
					if(data.get(i)[j] == attributes[j]){
//						System.out.println("2");
						if(j == attributes.length - 1){
							subset.add(data.get(i));
//							System.out.println("added! ");
						}
					} else{
						break;
					}
				}else{
					if(j == attributes.length - 1){
						subset.add(data.get(i));
//						System.out.println("added! ");
					}
				}
			}
		}
		
		
		
		int temp1 = 0;
		double tempPercent = 0;
		for(int[] item : subset){
			if(item[attributes.length] == 1){
				temp1++;
			}
		}
//		System.out.println(temp1);
//		System.out.println(subset.size());
//		System.out.println((double)temp1 / subset.size());
//		System.out.println((double)5/10);
		tempPercent = (double) temp1 / subset.size();
//		System.out.println(tempPercent);
		
		
		if(!arrayContains(attributes, -1)){
			if(tempPercent > 0.5){
				return new int[]{-2, 1};
			} else if(tempPercent == 0.5){
				if(Math.random() < 0.5){
					return new int[]{-1, 0};
				} else{
					return new int[]{-2, 1};
				}
			} else{
				return new int[]{-1, 0};
			}
		}
		
		if(entropy(tempPercent, 1-tempPercent)== 0){
			if(tempPercent > 0.5){
				return new int[]{-4, 1};
			} else if(tempPercent == 0.5){
				if(Math.random() < 0.5){
					return new int[]{-3, 0};
				} else{
					return new int[]{-4, 1};
				}
			} else{
				return new int[]{-3, 0};
			}
		}
		
//		ArrayList<Integer> unusedAttributes = new ArrayList<Integer>();
//		for(int i = 0 ; i < attributes.length ; i++){
//			if(attributes[i] == -1){
//				unusedAttributes.add(i);
//			}
//		}
		double tempPercent1 = 0;
		
		
		//find best attribute
		for(int i = 0 ; i < attributes.length; i++){
//			int[] usedAttributes = new int[];
			if(attributes[i] != -1){
				continue;
			}
			
			
			
			for(int[] item : subset){
//				System.out.println(item[i]);
				if(item[i] == 0){
					num0++;
					if(item[item.length - 1] == 1){
						posstiveNum0++;
					}
				} else{
					num1++;
					if(item[item.length - 1] == 1){
						posstiveNum1++;
					}
				}
			}
//			System.out.println(num0);
//			System.out.println(posstiveNum0);
//			System.out.println(num1);
//			System.out.println(posstiveNum1);
			percent = (double) num0 / (num0 + num1);
			percent0 = (double) posstiveNum0 / num0;
			percent1 = (double) posstiveNum1 / num1;
//			System.out.println(percent);
//			System.out.println(percent0);
//			System.out.println(percent1);
//			percent = (double) posstiveNum / num;
			if(percent0 == 0 || percent1 == 0){
				if(percent0 == 0 && percent1 == 0){
					Entropy = 0;
				} else if (percent0 == 0){
					Entropy = (double)(1-percent)*entropy(percent1, 1-percent1);
				} else{
					Entropy = (double)percent*entropy(percent0, 1-percent0);
				}
			} else{
				Entropy = (double)percent*entropy(percent0, 1-percent0)+(1-percent)*entropy(percent1, 1-percent1);
			}
//			System.out.println(Entropy);
			
			if(Entropy < minEntropy){
				minEntropy = Entropy;
				bestAttribute = i;
				tempPercent1 = tempPercent;
			}else if(Entropy == minEntropy){
//				if(Entropy == 0)
//					return new int[]{-3, };
				minEntropy = Entropy;
				bestAttribute = i;
				tempPercent1 = tempPercent;
			}
			
		}
		
//		System.out.println(bestAttribute);
//		System.out.println(minEntropy);
		
//		if(minEntropy == 0){
//			return attributes.length + bestAttribute;
//		}
//		System.out.println(tempPercent1);
		if(tempPercent > 0.5){
			return new int[]{bestAttribute, 1};
		} else if(tempPercent == 0.5){
			if(Math.random() < 0.5){
				return new int[]{bestAttribute, 0};
			} else{
				return new int[]{bestAttribute, 1};
			}
		} else{
			return new int[]{bestAttribute, 0};
		}
		
//		return bestAttribute;
	}
	
	public double entropy(double a, double b){
		if(a == 0 || b == 0)
			return 0;
		return (double)-1*a*Math.log(a)/Math.log(2)-b*Math.log(b)/Math.log(2);
	}
	
	public boolean arrayContains(int[] array, int key){
		int i = 0;
		while(i < array.length){
			if(array[i] == key)
				return true;
			i++;
		}
		return false;
	}
	
	public Node makeTree(ArrayList<int[]> data){
		int[] attributes = new int[attribute.length - 1];
		for(int i = 0 ; i < attributes.length ; i++){
			attributes[i] = -1;
		}
		
		Node root = new Node(null);
		root.index = nodeNum;
		root = makeNode(data, attributes, root, 0, -1);
		if(root == null){
			if(data.get(0)[attributes.length] == 0){
//				root.label = 0;
			} else{
//				root.label = 1;
			}
			return root;
		} else{
//			System.out.println(root.lChild.attribute);
			return root;
		}
		
		
	}
	
	public Node makeNode(ArrayList<int[]> data, int[] attributes, Node now, int att, int key){
		attributes[att] = key;
		int[] result = newBranch(data, attributes);
//		System.out.println(result[0]);
//		System.out.println("111: " + result[1]);
		now.index = nodeNum;
		nodeNum++;
		now.label = result[1];
		if(result[0] >= 0){
//			System.out.println();
//			int n = 0;
//			for(int i = 0 ; i < attributes.length ; i++){
//				if(attributes[i] == -1){
//					n++;
//				}
//			}
//			while(attributes.length - n > 0){
////				System.out.print("| ");
//				n++;
//			}
			now.name  = attribute[result[0]];
			now.attribute = result[0];
			int[] a = Arrays.copyOf(attributes, attributes.length);
			
//			System.out.print(attribute[result[0]] + " = 0 : ");
			now.lChild = new Node(null);
			now.lChild.level = now.level + 1;
			now.lChild = makeNode(data, a, now.lChild, result[0], 0);
			
			
			
			
//			n = 0;
//			for(int i = 0 ; i < attributes.length ; i++){
//				if(attributes[i] == -1){
//					n++;
//				}
//			}
//			while(attributes.length - n > 0){
////				System.out.print("| ");
//				n++;
//			}
			int[] b = Arrays.copyOf(attributes, attributes.length);
//			System.out.print(attribute[result[0]] + " = 1 : ");
			now.rChild = new Node(null);
			now.rChild.level = now.level + 1;
			now.rChild = makeNode(data, b, now.rChild, result[0], 1);
			
			
		} else{
//			System.out.println(now.label);
//			leafNum++;
			
		}
		return now;
		
		
		
		
		
		
		
//		now.attribute = result;
//		if(result[0] == -1 || result[0] == -3){
//			
//			now.label = 0;
//			leafNum++;
//			System.out.println(now.label);
//			return now;
//		} else if(result[0] == -2 || result[0] == -4){
//			
//			now.label = 1;
//			leafNum++;
//			System.out.println(now.label);
//			return now;
//		} else{
//			nodeNum++;
//			System.out.println();
//			int n = 0;
//			for(int item : attributes){
//				if(item == -1){
//					n++;
//				}
//			}
//			for(int m = 0 ; m < attributes.length - n ; m++){
//				System.out.print("| ");
//			}
//			
//			now.name = attribute[result[0]];
//			now.attribute = result[0];
//			now.label = result[1];
//			
//			Node leftChild = new Node(null);
//			
//			int[] a = Arrays.copyOf(attributes, attributes.length);
//			//int[] b = attributes;
//			a[result[0]] = 0;
//			System.out.print(now.name + " = " + a[result[0]] + " : ");
//			now.lChild = makeNode(data, a, leftChild);
//			nodeNum++;
//			
////			int[] info = newBranch(data, a);
////			if(now.lChild == null){
////				
////				int r = info[0];
//////				now.attribute = result;
////				if(r == -1 || r == -3){
////					
////					now.lChild.label = 0;
////					now.lChild.attribute = -1;
////					leafNum++;
////					
////				} else if(r == -2 || r == -4){
////					
////					now.label = 1;
////					now.attribute = -1;
////					leafNum++;
////					
////				}
//////				return now;
////			} else{
////				leftChild.index = nodeNum;
////				now.label = info[1];
////				nodeNum++;
////			}
//			
//			
//			Node rightChild = new Node(null);
//			
////			rightChild.index = nodeNum;
//			a[result[0]] = 1;
////			nodeNum++;
//			
//			
//			
//			for(int m = 0 ; m < attributes.length - n ; m++){
//				System.out.print("| ");
//			}
//			System.out.print(now.name + " = " + a[result[0]] + " : ");
//			now.rChild = makeNode(data, a, rightChild);
//			nodeNum++;
////			info = newBranch(data, a);
////			if(now.rChild == null){
////				int r = info[0];
//////				now.attribute = result;
////				if(r == -1 || r == -3){
////					
////					now.label = 0;
////					now.attribute = -1;
////					leafNum++;
////					
////				} else if(r == -2 || r == -4){
////					
////					now.label = 1;
////					now.attribute = -1;
////					leafNum++;
////					
////				}
////				return now;
////			} else{
////				rightChild.index = nodeNum;
////				now.label = info[1];
////				nodeNum++;
////			}
//			return now;
//		}
	}

	
	public class Node{
		String name;
		Node lChild;
		Node rChild;
		int label;
		int index;
		int attribute;
		int level;
		
		public Node(String name){
			name = this.name;
			lChild = null;
			rChild = null;
			label = -1;
			index = -1;
			attribute = -1;
			level = 0;
		}
	}
}