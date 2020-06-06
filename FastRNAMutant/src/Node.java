import java.util.ArrayList;
import java.util.HashMap;

public class Node {
	private int first;
	private int second;
	private int third;
	private ArrayList<Node> children;
	private ArrayList<HashMap<ArrayList<String>, ArrayList<ArrayList<String>>>> infoNode;
	//private ArrayList<ArrayList<ArrayList<String>>> topO_BEST;
	
	Node(){
		this.first=0;
		this.second=-1;
		this.third=-1;
		this.children=new ArrayList<Node>();
		this.infoNode=new ArrayList<HashMap<ArrayList<String>, ArrayList<ArrayList<String>>>>();
		//this.topO_BEST=new ArrayList<ArrayList<ArrayList<String>>>();
	}
	
	Node(int num){
		this.first=num;
		this.second=-1;
		this.third=-1;
		this.children=new ArrayList<Node>();
		this.infoNode=new ArrayList<HashMap<ArrayList<String>, ArrayList<ArrayList<String>>>>();
		//this.topO_BEST=new ArrayList<ArrayList<ArrayList<String>>>();
	}
	
	Node(int num1,int num2){
		this.first=num1;
		this.second=num2;
		this.third=-1;
		this.children=new ArrayList<Node>();
		this.infoNode=new ArrayList<HashMap<ArrayList<String>, ArrayList<ArrayList<String>>>>();
		//this.topO_BEST=new ArrayList<ArrayList<ArrayList<String>>>();
	}
	
	Node(int num1,int num2,int num3){
		this.first=num1;
		this.second=num2;
		this.third=num3;
		this.children=new ArrayList<Node>();
		this.infoNode=new ArrayList<HashMap<ArrayList<String>, ArrayList<ArrayList<String>>>>();
		//this.topO_BEST=new ArrayList<ArrayList<ArrayList<String>>>();
	}
	public int GetFirst() {
		return this.first;
	}
	
	public int GetSecond() {
		return this.second;
	}
	
	public int GetThird() {
		return this.third;
	}
	
	public ArrayList<Node> GetChildren() {
		return this.children;
	}
	
	public ArrayList<HashMap<ArrayList<String>, ArrayList<ArrayList<String>>>> GetInfoNode() {
		return this.infoNode;
	}
	
	/*
	public ArrayList<ArrayList<ArrayList<String>>> GetTopO_BEST() {
		return this.topO_BEST;
	}
	*/
	
	public void AddInfoNode(ArrayList<HashMap<ArrayList<String>, ArrayList<ArrayList<String>>>> data) {
		this.infoNode=data;
	}
	
	public void AddNode(Node node) {
		this.children.add(node);
	}
	
	/*
	public void AddTopO_BEST(ArrayList<ArrayList<ArrayList<String>>> data) {
		this.topO_BEST=data;
	}
	*/
	
}
