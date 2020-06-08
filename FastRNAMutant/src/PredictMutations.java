
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

import net.sf.javaml.core.Dataset;

public class PredictMutations {

	private String RNAsequence;
	private int numOfMutations;
	private int ammountOfMutationGroup;
	private int k;
	private double e_range;
	private int distanceForFiltering;
	private int centroidOrFar;

	PredictMutations(String RNAsequence, int numOfMutations, int ammountOfMutationGroup, int k, double e_range, int distanceForFiltering,int centroidOrFar) {
		this.RNAsequence = RNAsequence;
		this.numOfMutations = numOfMutations;
		this.ammountOfMutationGroup = ammountOfMutationGroup;
		this.k = k;
		this.e_range = e_range;
		this.distanceForFiltering=distanceForFiltering;
		this.centroidOrFar = centroidOrFar;
	}

	public void start(JFrame menu) {

		Thread t = new Thread() {
			public void run() {
				Splash splash = new Splash();
				splash.setVisible(true);
				while (!this.interrupted()) {
					// Do something
				}
				splash.setVisible(false);
				System.out.println("splash");
			}
			

		};

		ViennaRNA vienna = new ViennaRNA();
		AlgoritmStep steps = new AlgoritmStep();
		System.out.println(RNAsequence);
		RNAInfo RNAoptimal = steps.findOptimal(RNAsequence);
		RNAMultiInfo RNAsubOptimalBeforeFilttering=steps.findSubOptimal(RNAsequence,e_range);
		//Filttering
		RNAMultiInfo RNAsubOptimal=new RNAMultiInfo();
		for (int i=0;i<RNAsubOptimalBeforeFilttering.getSize();i++) {
			if (Integer.parseInt(vienna.RNAdistance(RNAoptimal.getStructure(), RNAsubOptimalBeforeFilttering.getStructure(i)))>=distanceForFiltering) {
				RNAsubOptimal.setStructure(RNAsubOptimalBeforeFilttering.getStructure(i));
				RNAsubOptimal.setEnergy(RNAsubOptimalBeforeFilttering.getEnergy(i));
			}
		}
		//double[][] matrixDistance=steps.makeDistanceMatrix(RNAsubOptimal); //not used
		Dataset dataset=steps.createDataSetForKMedoids(RNAsubOptimal);
		HashMap<String, Double> mapData=new HashMap<String, Double>();
		//Calculating distances
		for (int i = 0; i < dataset.size(); i++) {
			mapData.put(dataset.classValue(i).toString() + dataset.classValue(i).toString(), 0.0);
			for (int j = i + 1; j < dataset.size(); j++) {
				double distance = Double.parseDouble(
						vienna.RNAdistance(dataset.classValue(i).toString(), dataset.classValue(j).toString()));
				mapData.put(dataset.classValue(i).toString() + dataset.classValue(j).toString(), distance);
				mapData.put(dataset.classValue(j).toString() + dataset.classValue(i).toString(), distance);
				if (i%20==0 && j%100==0)
				System.out.println("distance " + i + " to " + j + " is   " + distance);
			}
		}
		//finish Calculating distances
		TreeDecomposition td = new TreeDecomposition();
		DynamicProgramming dp = new DynamicProgramming();
		ArrayList<ArrayList<String>> O_BEST = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> topO_BEST = new ArrayList<ArrayList<String>>();

		// =========Testing==============//

		// ========================Rafi=====================================================
		// The sequence is AAAAAAAAAAGGGGGUUUUUUUUUU
		/*
		 * String sequence = "AAAAAAAAAAGGGGGUUUUUUUUUU"; ArrayList<ArrayList<String>>
		 * dataForMutation=new ArrayList<ArrayList<String>>();//Part of Rafi
		 * dataForMutation.add(new ArrayList<String>()); dataForMutation.add(new
		 * ArrayList<String>()); dataForMutation.add(new ArrayList<String>());
		 * dataForMutation.add(new ArrayList<String>()); dataForMutation.add(new
		 * ArrayList<String>()); dataForMutation.get(0).add("A0UG11UU24G");
		 * dataForMutation.get(0).add("10.5");
		 * dataForMutation.get(1).add("A1UG11UU24G"); dataForMutation.get(1).add("10");
		 * dataForMutation.get(2).add("A0UG13UU24G"); dataForMutation.get(2).add("9");
		 * dataForMutation.get(3).add("A0UG12UU23G"); dataForMutation.get(3).add("8");
		 * dataForMutation.get(4).add("A5UG11UU22G"); dataForMutation.get(4).add("7");
		 * 
		 * 
		 * //Here put the RESULT GUI ResultWindow r = new
		 * ResultWindow(dataForMutation,sequence);
		 */

		// ====================================================================================
		/*
		 * ArrayList<String> data1=new ArrayList<String>(); data1.add("((..))");
		 * data1.add("(....)"); data1.add(".(..)."); DataCheckBox myJTD1 = new
		 * DataCheckBox(data1); myJTD1.setVisible(true); data1 = myJTD1.getPost(); Node
		 * treeCheck=td.MakeTreeComposition("((.)).","((..))");
		 */
		/*
		 * ArrayList<ArrayList<Integer>>
		 * matrixGraphChurkin=td.makeMatrix("((..))","((.))."); Node treeChurkin =new
		 * Node(1); treeChurkin.GetChildren().add(new Node(1,4));
		 * treeChurkin.GetChildren().get(0).GetChildren().add(new Node(1,4,0));
		 * treeChurkin.GetChildren().get(0).GetChildren().add(new Node(1,4,3));
		 * treeChurkin.GetChildren().get(0).GetChildren().get(0).GetChildren().add(new
		 * Node(0,4,5));
		 * treeChurkin.GetChildren().get(0).GetChildren().get(1).GetChildren().add(new
		 * Node(1,3,2)); DynamicProgramming dpChurkin=new DynamicProgramming();
		 * ArrayList<ArrayList<String>> resultList =
		 * dpChurkin.StartPredict(treeChurkin,matrixGraphChurkin,"AACUUU","((.)).",
		 * "((..))",3,5); ResultWindow ri = new ResultWindow(resultList,"AACUUU");
		 * ri.setVisible(true);
		 */
		// ======End Testing=============//

		if (dataset.size() > k) {
			//Clustering
			Dataset[] clustering = steps.findCluster(dataset, k, mapData);
			//finish Clustering
			for (int i = 0; i < clustering.length; i++) {
				for (int j = 0; j < clustering[i].size(); j++) {
					for (int c = j + 1; c < clustering[i].size(); c++) {
						if (j%20==0 && c%20==0)
						System.out.println("cluster " + i + " distance from " + j + " to " + c + " is "
								+ mapData.get(clustering[i].get(j).classValue().toString()
										+ clustering[i].get(c).classValue().toString()));
					}
				}
			}
			Dataset centroid = steps.findCentroids(clustering, mapData);
			for (int i = 0; i < centroid.size(); i++) {
				for (int j = i + 1; j < centroid.size(); j++) {
					System.out.println("distance from " + i + " to " + j + " is " + mapData
							.get(centroid.get(i).classValue().toString() + centroid.get(j).classValue().toString()));
				}
			}
			if (centroidOrFar == 1) {
				ArrayList<String> data = new ArrayList<String>();
				for (int i = 0; i < centroid.size(); i++) {
					data.add(centroid.classValue(i).toString());
				}
				// Part of Combobox that we can choose the with which data we want.update data

				DataCheckBox myJTD = new DataCheckBox(data);
				myJTD.setVisible(true);
				data = myJTD.getPost();
				t.start();
				menu.setEnabled(false);

				for (int i = 0; i < data.size(); i++) {
					ArrayList<ArrayList<Integer>> matrixGraph = td.makeMatrix(data.get(i), RNAoptimal.getStructure());
					Node tree = td.MakeTree(matrixGraph);
					// Dynamic Programming
					System.out.println("Now is:  " + data.get(i));
					ArrayList<ArrayList<String>> partOfO_BEST = dp.StartPredict(tree, matrixGraph, RNAsequence,
							RNAoptimal.getStructure(), data.get(i), numOfMutations, ammountOfMutationGroup);
					for (int j = 0; j < partOfO_BEST.size(); j++) {
						O_BEST.add(partOfO_BEST.get(j));
						System.out.println(partOfO_BEST.get(j));
						CheckForTopO_BEST(topO_BEST,partOfO_BEST.get(j),ammountOfMutationGroup);
						
					}
				}
				System.out.println("Success");
				// ResultWindow r = new ResultWindow(O_BEST,RNAsequence);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				t.interrupt();
				menu.setEnabled(true);
				try {
					new ResultsFrame(O_BEST, RNAsequence).setVisible(true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Dataset far = steps.findFar(clustering, centroid, mapData);
				for (int i = 0; i < far.size(); i++) {
					for (int j = i + 1; j < far.size(); j++) {
						System.out.println("distance from " + i + " to " + j + " is "
								+ mapData.get(far.get(i).classValue().toString() + far.get(j).classValue().toString()));
					}
				}
				ArrayList<String> data = new ArrayList<String>();
				for (int i = 0; i < far.size(); i++) {
					data.add(far.classValue(i).toString());
				}
				// Part of Combobox that we can choose the with which data we want. -update data

				DataCheckBox myJTD = new DataCheckBox(data);
				myJTD.setVisible(true);
				data = myJTD.getPost();
				t.start();
				menu.setEnabled(false);
				for (int i = 0; i < data.size(); i++) {
					ArrayList<ArrayList<Integer>> matrixGraph = td.makeMatrix(data.get(i), RNAoptimal.getStructure());
					Node tree = td.MakeTree(matrixGraph);
					// Dynamic Programming
					System.out.println("Now is:  " + data.get(i) + "  " + String.valueOf(i) + " of "
							+ String.valueOf(data.size() - 1));
					ArrayList<ArrayList<String>> partOfO_BEST = dp.StartPredict(tree, matrixGraph, RNAsequence,
							RNAoptimal.getStructure(), data.get(i), numOfMutations, ammountOfMutationGroup);
					for (int j = 0; j < partOfO_BEST.size(); j++) {
						O_BEST.add(partOfO_BEST.get(j));
						System.out.println(partOfO_BEST.get(j));
						CheckForTopO_BEST(topO_BEST,partOfO_BEST.get(j),ammountOfMutationGroup);
					}
				}
				System.out.println("Success");
//				ResultWindow r = new ResultWindow(O_BEST,RNAsequence);
//				r.setVisible(true);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				t.interrupt();
				menu.setEnabled(true);
				try {
					new ResultsFrame(O_BEST, RNAsequence).setVisible(true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			ArrayList<String> data = new ArrayList<String>();
			for (int i = 0; i < dataset.size(); i++) {
				data.add(dataset.classValue(i).toString());
			}

			// Part of Combobox that we can choose the with which data we want.update data
			DataCheckBox myJTD = new DataCheckBox(data);
			myJTD.setVisible(true);
			data = myJTD.getPost();
			t.start();
			menu.setEnabled(false);
			for (int i = 0; i < data.size(); i++) {
				ArrayList<ArrayList<Integer>> matrixGraph = td.makeMatrix(data.get(i), RNAoptimal.getStructure());
				Node tree = td.MakeTree(matrixGraph);
				// Dynamic Programming
				System.out.println("Now is:  " + data.get(i));
				ArrayList<ArrayList<String>> partOfO_BEST = dp.StartPredict(tree, matrixGraph, RNAsequence,
						RNAoptimal.getStructure(), data.get(i), numOfMutations, ammountOfMutationGroup);
				for (int j = 0; j < partOfO_BEST.size(); j++) {
					O_BEST.add(partOfO_BEST.get(j));
					System.out.println(partOfO_BEST.get(j));
					CheckForTopO_BEST(topO_BEST,partOfO_BEST.get(j),ammountOfMutationGroup);
				}
			}
			System.out.println("Success Dynamic Progrr");
//			ResultWindow r = new ResultWindow(O_BEST,RNAsequence);
//			r.setVisible(true);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			t.interrupt();
			menu.setEnabled(true);
			try {
				new ResultsFrame(O_BEST, RNAsequence).setVisible(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	
	public void CheckForTopO_BEST(ArrayList<ArrayList<String>> topO_BEST,ArrayList<String> infoPartO_BEST ,int ammountOfMutationGroup) {
		if (topO_BEST.size()==0) {
			topO_BEST.add(infoPartO_BEST);
		}
		else if (topO_BEST.size()==ammountOfMutationGroup && Double.parseDouble(infoPartO_BEST.get(1))<Double.parseDouble(topO_BEST.get(ammountOfMutationGroup-1).get(1))) {return;}
		else {
			int flag=1;
			for(int t=0;t<topO_BEST.size();t++) {
				if (Double.parseDouble(infoPartO_BEST.get(1))>Double.parseDouble(topO_BEST.get(t).get(1))) {
					topO_BEST.add(t,infoPartO_BEST);
					t=topO_BEST.size();
					flag=0;
				}
			}
			if(flag==1) {
				topO_BEST.add(infoPartO_BEST);
				
			}
			if (topO_BEST.size()>ammountOfMutationGroup) {
				topO_BEST.remove(ammountOfMutationGroup);
			}
		}
	}

}
