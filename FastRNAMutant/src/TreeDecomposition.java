import java.util.ArrayList;

public class TreeDecomposition {
	TreeDecomposition(){}
	
	public Node MakeTreeComposition(String structure,String optimal) {
		ArrayList<ArrayList<Integer>> matrixGraph=makeMatrix(optimal,structure);
		return MakeTree(matrixGraph);
	}
	
	public ArrayList<ArrayList<Integer>> makeMatrix(String str1,String str2){
		ArrayList<Integer> stack=new ArrayList<Integer>();
		ArrayList<Integer> stack2=new ArrayList<Integer>();
		int size=str1.length();
		ArrayList<ArrayList<Integer>> matrix= new ArrayList<ArrayList<Integer>>();
		for (int i=0;i<size;i++) {
			ArrayList<Integer> temp= new ArrayList<Integer>();
			for (int j=0;j<size;j++) {
				temp.add(0);
			}
			matrix.add(temp);
		}
		
		for(int i=0;i<size-1;i++) {
			matrix.get(i).set(i+1, 1);
			matrix.get(i+1).set(i, 1);
		}
		for(int i=0;i<size;i++) {
			if(str1.charAt(i)=='(') {
				stack.add(i);
			}
			else if(str1.charAt(i)==')') {
				int j=stack.get(stack.size()-1);
				stack.remove(stack.size()-1);
				matrix.get(i).set(j, 3);
				matrix.get(j).set(i, 3);
			}
			if(str2.charAt(i)=='(') {
				stack2.add(i);
			}
			else if(str2.charAt(i)==')') {
				int j=stack2.get(stack2.size()-1);
				stack2.remove(stack2.size()-1);
				if (matrix.get(i).get(j)!=3)
					matrix.get(i).set(j, 2);
				else
					matrix.get(i).set(j, 4);
				
				if (matrix.get(j).get(i)!=3)
					matrix.get(j).set(i, 2);
				else 
					matrix.get(j).set(i, 4);
			}	
		}
		return matrix;
	}
	public Node MakeTree(ArrayList<ArrayList<Integer>> matrix) {
		Node tree=new Node();
		ArrayList<Integer> used=new ArrayList<Integer>();
		used.add(0);
		int size=matrix.size();
		BuildRecusiveTree(tree,matrix,used,size);
		return tree;
	}
	
	public void BuildRecusiveTree(Node node,ArrayList<ArrayList<Integer>> matrix, ArrayList<Integer> used,int size) {
		if (used.size()==size)
			return;
		int first=node.GetFirst();
		int second=node.GetSecond();
		int third=node.GetThird();
		if (first>=0 && second==-1 && third==-1) {
			int i=first;
			for(int j=0;j<size;j++) {
				if (matrix.get(i).get(j)>=1 && i!=j && NotFound(j,used)) {
					Node child=new Node(i,j);
					node.GetChildren().add(child);
					int index=node.GetChildren().size()-1;
					used.add(j);
					BuildRecusiveTree(node.GetChildren().get(index),matrix,used,size);
					if (used.size()==size)
						return;
				}
			}
		}
		else if (first>=0 && second>=0 && third==-1) {
			int i=first;
			int j=second;
			for(int k=0;k<size;k++) {
				if (matrix.get(i).get(k)>=1 && matrix.get(j).get(k)>=1 && i!=k && j!=k && NotFound(k,used)) {
					Node child=new Node(i,j,k);
					node.GetChildren().add(child);
					int index=node.GetChildren().size()-1;
					used.add(k);
					BuildRecusiveTree(node.GetChildren().get(index),matrix,used,size);
					if (used.size()==size)
						return;
				}
			}
			for(int k=0;k<size;k++) {
				if (matrix.get(j).get(k)>=1 && j!=k && NotFound(k,used)) {
					Node child=new Node(j,k);
					node.GetChildren().add(child);
					int index=node.GetChildren().size()-1;
					used.add(k);
					BuildRecusiveTree(node.GetChildren().get(index),matrix,used,size);
					if (used.size()==size)
						return;
				}
			}
		}
		else if (first>=0 && second>=0 && third>=0) {
			int i=first;
			int j=second;
			int k=third;
			for(int m=0;m<size;m++) {
				if (matrix.get(i).get(m)>=1 && matrix.get(k).get(m)>=1 && i!=m && k!=m && NotFound(m,used)) {
					Node child=new Node(i,k,m);
					node.GetChildren().add(child);
					int index=node.GetChildren().size()-1;
					used.add(m);
					BuildRecusiveTree(node.GetChildren().get(index),matrix,used,size);
					if (used.size()==size)
						return;
				}
			}
			for(int m=0;m<size;m++) {
				if (matrix.get(j).get(m)>=1 && matrix.get(k).get(m)>=1 && j!=m && k!=m && NotFound(m,used)) {
					Node child=new Node(j,k,m);
					node.GetChildren().add(child);
					int index=node.GetChildren().size()-1;
					used.add(m);
					BuildRecusiveTree(node.GetChildren().get(index),matrix,used,size);
					if (used.size()==size)
						return;
				}
			}
			for(int m=0;m<size;m++) {
				if (matrix.get(k).get(m)>=1 && k!=m && NotFound(m,used)) {
					Node child=new Node(k,m);
					node.GetChildren().add(child);
					int index=node.GetChildren().size()-1;
					used.add(m);
					BuildRecusiveTree(node.GetChildren().get(index),matrix,used,size);
					if (used.size()==size)
						return;
				}
			}
		}	
		return;
	}
	
	public boolean NotFound(int num,ArrayList<Integer> used) {
		for(int i=0;i<used.size();i++) {
			if (used.get(i)==num)
				return false;
		}
		return true;
	}
}
