import java.util.ArrayList;
import java.util.HashMap;

public class DynamicProgramming {
	
	DynamicProgramming(){}
	
	public ArrayList<ArrayList<String>> StartPredict(Node tree,ArrayList<ArrayList<Integer>> matrixGraph,String RNAsequence,String optimalStructure,String suboptStructure,int numOfMutations ,int ammountOfMutationGroup) {
		ViennaRNA vienna=new ViennaRNA();
		double energyOptimalBeforeMutation=Double.parseDouble(vienna.RNAeval(RNAsequence, optimalStructure));
		double energySuboptBeforeMutation=Double.parseDouble(vienna.RNAeval(RNAsequence, suboptStructure));
		ArrayList<String> alefBet=new ArrayList<String>();
		alefBet.add("A");alefBet.add("C");alefBet.add("G");alefBet.add("U");
		ArrayList<HashMap<ArrayList<String>, ArrayList<ArrayList<String>>>> O_BEST=PredictDynamicProgramming(tree,matrixGraph,RNAsequence,optimalStructure,suboptStructure,numOfMutations,ammountOfMutationGroup,alefBet,energyOptimalBeforeMutation,energySuboptBeforeMutation);
		ArrayList<ArrayList<String>> topO_BEST=FindTopO_BEST(O_BEST,alefBet,numOfMutations,ammountOfMutationGroup);
		ArrayList<ArrayList<String>> topGroupOfMutations=new ArrayList<ArrayList<String>>();
		for(int i=0;i<topO_BEST.size();i++) {
			topGroupOfMutations.add(new ArrayList<String>());
			topGroupOfMutations.get(i).add(topO_BEST.get(i).get(2));
			topGroupOfMutations.get(i).add(topO_BEST.get(i).get(1));
			String mutationSequence=FindSequence(RNAsequence,topO_BEST.get(i).get(2));
			RNAInfo optMutation=vienna.RNAfold(mutationSequence);
			topGroupOfMutations.get(i).add(mutationSequence);
			topGroupOfMutations.get(i).add(suboptStructure);
			topGroupOfMutations.get(i).add(optMutation.getEnergy());
			topGroupOfMutations.get(i).add(optMutation.getStructure());
			topGroupOfMutations.get(i).add(String.valueOf(vienna.RNAdistance(optimalStructure, optMutation.getStructure())));
		}
		return topGroupOfMutations;
	}
	
	public ArrayList<HashMap<ArrayList<String>, ArrayList<ArrayList<String>>>> PredictDynamicProgramming(Node node, ArrayList<ArrayList<Integer>> matrixGraph,String RNAsequence,String optimalStructure,String suboptStructure,int numOfMutations,int ammountOfMutationGroup,ArrayList<String> alefBet,Double energyOptimalBeforeMutation,Double energySuboptBeforeMutation) {
		int first=node.GetFirst();
		int second=node.GetSecond();
		int third=node.GetThird();
		int countOfChildren=node.GetChildren().size();
		
		//Build O-BEST for Mutation
		ArrayList<HashMap<ArrayList<String>, ArrayList<ArrayList<String>>>> O_BEST = new ArrayList<HashMap<ArrayList<String>, ArrayList<ArrayList<String>>>>();
		for(int i=0;i<=numOfMutations;i++) {
			O_BEST.add(new HashMap<ArrayList<String>, ArrayList<ArrayList<String>>>());
		}
		
		/*
		ArrayList<ArrayList<ArrayList<String>>> topO_BEST=new ArrayList<ArrayList<ArrayList<String>>>();
		for(int i=0;i<=numOfMutations;i++) {
			topO_BEST.add(new ArrayList<ArrayList<String>>());
		}
		*/
		
		//If leaf
		if (countOfChildren==0) {
			double deltaEnergy;
			double deltaEnergyOptimal;
			double deltaEnergySubopt;
			
			//looking for edges
			int edgeOptimal=-1;
			int edgeSubopt=-1;
			int whichOneOptimal=-1;
			int whichOneIndexOptimal=-1;
			int whichOneSubopt=-1;
			int whichOneIndexSubopt=-1;
			
			//If node is (m,k)
			if (first>=0 && second>=0 && third==-1) {
				for(int i=0;i<matrixGraph.size();i++) {
					if (matrixGraph.get(second).get(i)==2)
						edgeOptimal=i;
					else if (matrixGraph.get(second).get(i)==3)
						edgeSubopt=i;
					else if (matrixGraph.get(second).get(i)==4) {
						edgeOptimal=i;
						edgeSubopt=i;
					}
				}
				for (int i=0;i<alefBet.size();i++) {
					ArrayList<String> key=new ArrayList<String>();
					key.add(alefBet.get(i));
					for (int j=0;j<alefBet.size();j++) {
						
						//Optimal edge
						//No edge optimal
						if (edgeOptimal==-1) {
							double energyOptimalAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, optimalStructure, alefBet.get(j).charAt(0), second);
							deltaEnergyOptimal=energyOptimalAfterMutation-energyOptimalBeforeMutation;			
						}
						//Edge with relation
						else if (edgeOptimal==first) {
								whichOneOptimal=first;
								whichOneIndexOptimal=i;
								double energyOptimalAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, optimalStructure,alefBet.get(i).charAt(0),first ,alefBet.get(j).charAt(0), second);
								deltaEnergyOptimal=energyOptimalAfterMutation-energyOptimalBeforeMutation;
						}
						//Edge without relation
						else {
							deltaEnergyOptimal=0;
						}
						
						//Subopt edge
						//No edge optimal
						if (edgeSubopt==-1) {
							double energySuboptAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, suboptStructure, alefBet.get(j).charAt(0), second);
							deltaEnergySubopt=energySuboptBeforeMutation-energySuboptAfterMutation;
						}
						//Edge with relation
						else if (edgeSubopt==first) {
							whichOneSubopt=first;
							whichOneIndexSubopt=i;
							double energySuboptAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, suboptStructure,alefBet.get(i).charAt(0),first ,alefBet.get(j).charAt(0), second);
							deltaEnergySubopt=energySuboptBeforeMutation-energySuboptAfterMutation;
						}
						//Edge without relation
						else {
							deltaEnergySubopt=0;
						}
						
						deltaEnergy=deltaEnergyOptimal+deltaEnergySubopt;
						String letter=alefBet.get(j).substring(0);
						ArrayList<String> keyChild= new ArrayList<String>();
						//CheckForO_BEST(topO_BEST.get(numOfMutation(letter,RNAsequence.substring(second,second+1),0)), letter, "","", deltaEnergy, ammountOfMutationGroup);
						PushInformationToO_BEST(O_BEST, key,keyChild,keyChild, letter, "","", deltaEnergy, RNAsequence.substring(second,second+1),second,"", 0, numOfMutations,ammountOfMutationGroup);
					}
				}
				node.AddInfoNode(O_BEST);
				//node.AddTopO_BEST(topO_BEST);
				return O_BEST;
			}
			//If node is (m,k,l)
			if (first>=0 && second>=0 && third>=0) {
				for(int i=0;i<matrixGraph.size();i++) {
					if (matrixGraph.get(third).get(i)==2)
						edgeOptimal=i;
					else if (matrixGraph.get(third).get(i)==3)
						edgeSubopt=i;
					else if (matrixGraph.get(third).get(i)==4) {
						edgeOptimal=i;
						edgeSubopt=i;
					}
				}
				for (int i=0;i<alefBet.size();i++) {
					for (int j=0;j<alefBet.size();j++) {
						ArrayList<String> key=new ArrayList<String>();
						key.add(alefBet.get(i));
						key.add(alefBet.get(j));
						
						for(int k=0;k<alefBet.size();k++) {
							//Optimal edge
							//No edge optimal
							if (edgeOptimal==-1) {
								double energyOptimalAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, optimalStructure, alefBet.get(k).charAt(0), third);
								deltaEnergyOptimal=energyOptimalAfterMutation-energyOptimalBeforeMutation;			
							}
							//Edge with relation
							else if (edgeOptimal==first || edgeOptimal==second) {
									whichOneOptimal=first;
									whichOneIndexOptimal=i;
									if (second==edgeOptimal) {
										whichOneOptimal=second;
										whichOneIndexOptimal=j;
									}
									double energyOptimalAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, optimalStructure,alefBet.get(whichOneIndexOptimal).charAt(0),whichOneOptimal ,alefBet.get(k).charAt(0), third);
									deltaEnergyOptimal=energyOptimalAfterMutation-energyOptimalBeforeMutation;
							}
							//Edge without relation
							else {
								deltaEnergyOptimal=0;
							}
							
							//Subopt edge
							//No edge optimal
							if (edgeSubopt==-1) {
								double energySuboptAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, suboptStructure, alefBet.get(k).charAt(0), third);
								deltaEnergySubopt=energySuboptBeforeMutation-energySuboptAfterMutation;
							}
							//Edge with relation
							else if (edgeSubopt==first || edgeSubopt==second) {
								whichOneSubopt=first;
								whichOneIndexSubopt=i;
								if (second==edgeSubopt) {
									whichOneSubopt=second;
									whichOneIndexSubopt=j;
								}
								double energySuboptAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, suboptStructure,alefBet.get(whichOneIndexSubopt).charAt(0),whichOneSubopt ,alefBet.get(k).charAt(0), third);
								deltaEnergySubopt=energySuboptBeforeMutation-energySuboptAfterMutation;
							}
							//Edge without relation
							else {
								deltaEnergySubopt=0;
							}
							
							deltaEnergy=deltaEnergyOptimal+deltaEnergySubopt;
							String letter=alefBet.get(k).substring(0);
							ArrayList<String> keyChild= new ArrayList<String>();
							//CheckForO_BEST(topO_BEST.get(numOfMutation(letter,RNAsequence.substring(third,third+1),0)), letter, "","", deltaEnergy, ammountOfMutationGroup);
							PushInformationToO_BEST(O_BEST, key,keyChild, keyChild, letter, "","", deltaEnergy, RNAsequence.substring(third,third+1),third,"", 0, numOfMutations,ammountOfMutationGroup);
						}
					}
				}
				
				node.AddInfoNode(O_BEST);
				//node.AddTopO_BEST(topO_BEST);
				return O_BEST;
			}
		}
		
		//If no leaf
		ArrayList<ArrayList<HashMap<ArrayList<String>, ArrayList<ArrayList<String>>>>> O_BESTForChildren=new ArrayList<ArrayList<HashMap<ArrayList<String>, ArrayList<ArrayList<String>>>>>();

		//Call children
		for(int i=0;i<countOfChildren;i++) {
			O_BESTForChildren.add(PredictDynamicProgramming(node.GetChildren().get(i),matrixGraph,RNAsequence,optimalStructure,suboptStructure,numOfMutations,ammountOfMutationGroup,alefBet,energyOptimalBeforeMutation,energySuboptBeforeMutation));
		}
		
		double deltaEnergy;
		double deltaEnergyOptimal;
		double deltaEnergySubopt;
		
		//looking for edges
		int edgeOptimal=-1;
		int edgeSubopt=-1;
		int whichOneOptimal=-1;
		int whichOneIndexOptimal=-1;
		int whichOneSubopt=-1;
		int whichOneIndexSubopt=-1;
		
		//If one child
		if (countOfChildren==1) {
			
			//If node is (m)
			if (first>=0 && second==-1 && third==-1) {
				for(int i=0;i<matrixGraph.size();i++) {
					if (matrixGraph.get(first).get(i)==2)
						edgeOptimal=i;
					else if (matrixGraph.get(first).get(i)==3)
						edgeSubopt=i;
					else if (matrixGraph.get(first).get(i)==4) {
						edgeOptimal=i;
						edgeSubopt=i;
					}
				}
				for (int i=0;i<alefBet.size();i++) {
					ArrayList<String> key=new ArrayList<String>();
					key.add(alefBet.get(i));
					
					//Optimal edge
					//No edge optimal
					if (edgeOptimal==-1) {
						double energyOptimalAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, optimalStructure, alefBet.get(i).charAt(0), first);
						deltaEnergyOptimal=energyOptimalAfterMutation-energyOptimalBeforeMutation;	
					}
					//Edge with optimal
					else {
						deltaEnergyOptimal=0;
					}
					
					//Subopt edge
					//No edge optimal
					if (edgeSubopt==-1) {
						double energySuboptAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, suboptStructure, alefBet.get(i).charAt(0), first);
						deltaEnergySubopt=energySuboptBeforeMutation-energySuboptAfterMutation;
					}
					//Edge with subopt
					else {
						deltaEnergySubopt=0;
					}
					
					deltaEnergy=deltaEnergyOptimal+deltaEnergySubopt;
		
					double deltaEnergyWithChild;

					for (int m=0;m<=O_BESTForChildren.get(0).size()-1;m++) {
						if (O_BESTForChildren.get(0).get(m).containsKey(key)) {
							for(int index=0;index<O_BESTForChildren.get(0).get(m).get(key).size();index++) {
								double energyChild=Double.parseDouble(O_BESTForChildren.get(0).get(m).get(key).get(index).get(1));
								deltaEnergyWithChild=deltaEnergy+energyChild;
								//CheckForO_BEST(topO_BEST.get(numOfMutation(alefBet.get(i),RNAsequence.substring(first,first+1),m)), alefBet.get(i), O_BESTForChildren.get(0).get(m).get(key).get(index).get(0),"", deltaEnergyWithChild, ammountOfMutationGroup);
								PushInformationToO_BEST(O_BEST, key,key,new ArrayList<String>(), alefBet.get(i), O_BESTForChildren.get(0).get(m).get(key).get(index).get(0),"", deltaEnergyWithChild, RNAsequence.substring(first,first+1),first,O_BESTForChildren.get(0).get(m).get(key).get(index).get(2), m, numOfMutations, ammountOfMutationGroup);
							}
						} 
					}
					
				}
				
				node.AddInfoNode(O_BEST);
				//node.AddTopO_BEST(topO_BEST);
				return O_BEST;
			}
			
			//If node is (m,k)
			if (first>=0 && second>=0 && third==-1) {
				for(int i=0;i<matrixGraph.size();i++) {
					if (matrixGraph.get(second).get(i)==2)
						edgeOptimal=i;
					else if (matrixGraph.get(second).get(i)==3)
						edgeSubopt=i;
					else if (matrixGraph.get(second).get(i)==4) {
						edgeOptimal=i;
						edgeSubopt=i;
					}
				}
				for (int i=0;i<alefBet.size();i++) {
					ArrayList<String> key=new ArrayList<String>();
					key.add(alefBet.get(i));
					for (int j=0;j<alefBet.size();j++) {
						
						//Optimal edge
						//No edge optimal
						if (edgeOptimal==-1) {
							double energyOptimalAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, optimalStructure, alefBet.get(j).charAt(0), second);
							deltaEnergyOptimal=energyOptimalAfterMutation-energyOptimalBeforeMutation;			
						}
						//Edge with relation
						else if (edgeOptimal==first) {
								whichOneOptimal=first;
								whichOneIndexOptimal=i;
								double energyOptimalAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, optimalStructure,alefBet.get(i).charAt(0),first ,alefBet.get(j).charAt(0), second);
								deltaEnergyOptimal=energyOptimalAfterMutation-energyOptimalBeforeMutation;
						}
						//Edge without relation
						else {
							deltaEnergyOptimal=0;
						}
						
						//Subopt edge
						//No edge optimal
						if (edgeSubopt==-1) {
							double energySuboptAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, suboptStructure, alefBet.get(j).charAt(0), second);
							deltaEnergySubopt=energySuboptBeforeMutation-energySuboptAfterMutation;
						}
						//Edge with relation
						else if (edgeSubopt==first) {
							whichOneSubopt=first;
							whichOneIndexSubopt=i;
							double energySuboptAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, suboptStructure,alefBet.get(i).charAt(0),first ,alefBet.get(j).charAt(0), second);
							deltaEnergySubopt=energySuboptBeforeMutation-energySuboptAfterMutation;
						}
						//Edge without relation
						else {
							deltaEnergySubopt=0;
						}
						
						deltaEnergy=deltaEnergyOptimal+deltaEnergySubopt;
						String letter=alefBet.get(j).substring(0);
						
						ArrayList<String> keyChild;
						
						if (O_BESTForChildren.get(0).get(0).size()==16)
							keyChild=createKeyChild(alefBet, i, j);
						else
							keyChild=createKeyChild(alefBet, j);
			
						double deltaEnergyWithChild;
	
						for (int m=0;m<=O_BESTForChildren.get(0).size()-1;m++) {
							if (O_BESTForChildren.get(0).get(m).containsKey(keyChild)) {
								for(int index=0;index<O_BESTForChildren.get(0).get(m).get(keyChild).size();index++) {
									double energyChild=Double.parseDouble(O_BESTForChildren.get(0).get(m).get(keyChild).get(index).get(1));
									deltaEnergyWithChild=deltaEnergy+energyChild;
									//CheckForO_BEST(topO_BEST.get(numOfMutation(letter,RNAsequence.substring(second,second+1),m)), letter, O_BESTForChildren.get(0).get(m).get(key).get(index).get(0),"", deltaEnergyWithChild, ammountOfMutationGroup);
									PushInformationToO_BEST(O_BEST, key,keyChild,new ArrayList<String>(), letter, O_BESTForChildren.get(0).get(m).get(keyChild).get(index).get(0),"", deltaEnergyWithChild, RNAsequence.substring(second,second+1),second,O_BESTForChildren.get(0).get(m).get(keyChild).get(index).get(2), m, numOfMutations, ammountOfMutationGroup);
								}
							} 
						}
					}
				}
				
				node.AddInfoNode(O_BEST);
				//node.AddTopO_BEST(topO_BEST);
				return O_BEST;
			}
			//If node is (m,k,l)
			if (first>=0 && second>=0 && third>=0) {
				for(int i=0;i<matrixGraph.size();i++) {
					if (matrixGraph.get(third).get(i)==2)
						edgeOptimal=i;
					else if (matrixGraph.get(third).get(i)==3)
						edgeSubopt=i;
					else if (matrixGraph.get(third).get(i)==4) {
						edgeOptimal=i;
						edgeSubopt=i;
					}
				}
				int whoIs1=-1;
				int whoIs2=-1;
				if(node.GetChildren().get(0).GetFirst()>=0 && node.GetChildren().get(0).GetSecond()>=0 && node.GetChildren().get(0).GetThird()>=0) {
					whoIs1=findWhoIs(first, second, third, node.GetChildren().get(0).GetFirst());
					whoIs2=findWhoIs(first, second, third, node.GetChildren().get(0).GetSecond());
				}
				else if(node.GetChildren().get(0).GetFirst()>=0 && node.GetChildren().get(0).GetSecond()>=0 && node.GetChildren().get(0).GetThird()==-1) {
					whoIs1=findWhoIs(first, second, third, node.GetChildren().get(0).GetFirst());
				}
				for (int i=0;i<alefBet.size();i++) {
					for (int j=0;j<alefBet.size();j++) {
						ArrayList<String> key=new ArrayList<String>();
						key.add(alefBet.get(i));
						key.add(alefBet.get(j));
						
						for(int k=0;k<alefBet.size();k++) {
							//Optimal edge
							//No edge optimal
							if (edgeOptimal==-1) {
								double energyOptimalAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, optimalStructure, alefBet.get(k).charAt(0), third);
								deltaEnergyOptimal=energyOptimalAfterMutation-energyOptimalBeforeMutation;			
							}
							//Edge with relation
							else if (edgeOptimal==first || edgeOptimal==second) {
									whichOneOptimal=first;
									whichOneIndexOptimal=i;
									if (second==edgeOptimal) {
										whichOneOptimal=second;
										whichOneIndexOptimal=j;
									}
									double energyOptimalAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, optimalStructure,alefBet.get(whichOneIndexOptimal).charAt(0),whichOneOptimal ,alefBet.get(k).charAt(0), third);
									deltaEnergyOptimal=energyOptimalAfterMutation-energyOptimalBeforeMutation;
							}
							//Edge without relation
							else {
								deltaEnergyOptimal=0;
							}
							
							//Subopt edge
							//No edge optimal
							if (edgeSubopt==-1) {
								double energySuboptAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, suboptStructure, alefBet.get(k).charAt(0), third);
								deltaEnergySubopt=energySuboptBeforeMutation-energySuboptAfterMutation;
							}
							//Edge with relation
							else if (edgeSubopt==first || edgeSubopt==second) {
								whichOneSubopt=first;
								whichOneIndexSubopt=i;
								if (second==edgeSubopt) {
									whichOneSubopt=second;
									whichOneIndexSubopt=j;
								}
								double energySuboptAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, suboptStructure,alefBet.get(whichOneIndexSubopt).charAt(0),whichOneSubopt ,alefBet.get(k).charAt(0), third);
								deltaEnergySubopt=energySuboptBeforeMutation-energySuboptAfterMutation;
							}
							//Edge without relation
							else {
								deltaEnergySubopt=0;
							}
							
							deltaEnergy=deltaEnergyOptimal+deltaEnergySubopt;
							String letter=alefBet.get(k);
							ArrayList<String> keyChild;
							
							if (O_BESTForChildren.get(0).get(0).size()==16)
								keyChild=createKeyChild(whoIs1, whoIs2, alefBet, i, j, k);
							else
								keyChild=createKeyChild(alefBet, k);
				
							double deltaEnergyWithChild;

							for (int m=0;m<=O_BESTForChildren.get(0).size()-1;m++) {
								if (O_BESTForChildren.get(0).get(m).containsKey(keyChild)) {
									for(int index=0;index<O_BESTForChildren.get(0).get(m).get(keyChild).size();index++) {
										double energyChild=Double.parseDouble(O_BESTForChildren.get(0).get(m).get(keyChild).get(index).get(1));
										deltaEnergyWithChild=deltaEnergy+energyChild;
										//CheckForO_BEST(topO_BEST.get(numOfMutation(letter,RNAsequence.substring(third,third+1),m)), letter, O_BESTForChildren.get(0).get(m).get(key).get(index).get(0),"", deltaEnergyWithChild, ammountOfMutationGroup);
										PushInformationToO_BEST(O_BEST, key,keyChild,new ArrayList<String>(), letter, O_BESTForChildren.get(0).get(m).get(keyChild).get(index).get(0),"", deltaEnergyWithChild, RNAsequence.substring(third,third+1),third,O_BESTForChildren.get(0).get(m).get(keyChild).get(index).get(2), m, numOfMutations, ammountOfMutationGroup);
									}
								} 
							}
						}
					}
				}
				
				node.AddInfoNode(O_BEST);
				//node.AddTopO_BEST(topO_BEST);
				return O_BEST;
			}
		}
		
		//If two children
		else if (countOfChildren==2) {
			//If node is (m,k)
			if (first>=0 && second>=0 && third==-1) {
				for(int i=0;i<matrixGraph.size();i++) {
					if (matrixGraph.get(second).get(i)==2)
						edgeOptimal=i;
					else if (matrixGraph.get(second).get(i)==3)
						edgeSubopt=i;
					else if (matrixGraph.get(second).get(i)==4) {
						edgeOptimal=i;
						edgeSubopt=i;
					}
				}
				for (int i=0;i<alefBet.size();i++) {
					ArrayList<String> key=new ArrayList<String>();
					key.add(alefBet.get(i));
					for (int j=0;j<alefBet.size();j++) {
						
						//Optimal edge
						//No edge optimal
						if (edgeOptimal==-1) {
							double energyOptimalAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, optimalStructure, alefBet.get(j).charAt(0), second);
							deltaEnergyOptimal=energyOptimalAfterMutation-energyOptimalBeforeMutation;		
						}
						//Edge with relation
						else if (edgeOptimal==first) {
								whichOneOptimal=first;
								whichOneIndexOptimal=i;
								double energyOptimalAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, optimalStructure,alefBet.get(i).charAt(0),first ,alefBet.get(j).charAt(0), second);
								deltaEnergyOptimal=energyOptimalAfterMutation-energyOptimalBeforeMutation;
						}
						//Edge without relation
						else {
							deltaEnergyOptimal=0;
						}
						
						//Subopt edge
						//No edge optimal
						if (edgeSubopt==-1) {
							double energySuboptAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, suboptStructure, alefBet.get(j).charAt(0), second);
							deltaEnergySubopt=energySuboptBeforeMutation-energySuboptAfterMutation;
						}
						//Edge with relation
						else if (edgeSubopt==first) {
							whichOneSubopt=first;
							whichOneIndexSubopt=i;
							double energySuboptAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, suboptStructure,alefBet.get(i).charAt(0),first ,alefBet.get(j).charAt(0), second);
							deltaEnergySubopt=energySuboptBeforeMutation-energySuboptAfterMutation;
						}
						//Edge without relation
						else {
							deltaEnergySubopt=0;
						}
						
						deltaEnergy=deltaEnergyOptimal+deltaEnergySubopt;
						String letter=alefBet.get(j).substring(0);
						
						ArrayList<String> keyChild1;
						if (O_BESTForChildren.get(0).get(0).size()==16)
							keyChild1=createKeyChild(alefBet, i, j);
						else
							keyChild1=createKeyChild(alefBet, j);
						
						ArrayList<String> keyChild2;
						if (O_BESTForChildren.get(1).get(0).size()==16)
							keyChild2=createKeyChild(alefBet, i, j);
						else
							keyChild2=createKeyChild(alefBet, j);
									
						for (int m=0;m<=numOfMutations;m++) {
							for (int m1=0;m1<=numOfMutations;m1++) {
								for (int m2=0;m2<=numOfMutations;m2++) {
									if (letter.equals(RNAsequence.substring(second,second+1))){
										if (m1+m2==m) {
											if (O_BESTForChildren.get(0).get(m1).containsKey(keyChild1) && O_BESTForChildren.get(1).get(m2).containsKey(keyChild2)) {
												for(int index1=0;index1<O_BESTForChildren.get(0).get(m1).get(keyChild1).size();index1++) {
													double energyChild1=Double.parseDouble(O_BESTForChildren.get(0).get(m1).get(keyChild1).get(index1).get(1));
													for(int index2=0;index2<O_BESTForChildren.get(1).get(m2).get(keyChild2).size();index2++) {
														double energyChild2=Double.parseDouble(O_BESTForChildren.get(1).get(m2).get(keyChild2).get(index2).get(1));
														double deltaEnergyWithChild=deltaEnergy+energyChild1+energyChild2;
														//CheckForO_BEST(topO_BEST.get(numOfMutation(alefBet.get(j),RNAsequence.substring(second,second+1),m)), alefBet.get(j), O_BESTForChildren.get(0).get(m1).get(keyChild1).get(index1).get(0),O_BESTForChildren.get(1).get(m2).get(keyChild2).get(index2).get(0), deltaEnergyWithChild, ammountOfMutationGroup);
														PushInformationToO_BEST(O_BEST, key,keyChild1,keyChild2, alefBet.get(j), O_BESTForChildren.get(0).get(m1).get(keyChild1).get(index1).get(0),O_BESTForChildren.get(1).get(m2).get(keyChild2).get(index2).get(0), deltaEnergyWithChild, RNAsequence.substring(second,second+1),second,O_BESTForChildren.get(0).get(m1).get(keyChild1).get(index1).get(2)+O_BESTForChildren.get(1).get(m2).get(keyChild2).get(index2).get(2), m, numOfMutations, ammountOfMutationGroup);
													}
												}
											}
										}
										else if(m1+m2>m)
											m2=numOfMutations;
									}
									else {
										if(m1+m2+1==m) {
											if (O_BESTForChildren.get(0).get(m1).containsKey(keyChild1) && O_BESTForChildren.get(1).get(m2).containsKey(keyChild2)) {
												for(int index1=0;index1<O_BESTForChildren.get(0).get(m1).get(keyChild1).size();index1++) {
													double energyChild1=Double.parseDouble(O_BESTForChildren.get(0).get(m1).get(keyChild1).get(index1).get(1));
													for(int index2=0;index2<O_BESTForChildren.get(1).get(m2).get(keyChild2).size();index2++) {
														double energyChild2=Double.parseDouble(O_BESTForChildren.get(1).get(m2).get(keyChild2).get(index2).get(1));
														double deltaEnergyWithChild=deltaEnergy+energyChild1+energyChild2;
														//CheckForO_BEST(topO_BEST.get(numOfMutation(alefBet.get(j),RNAsequence.substring(second,second+1),m1+m2)), alefBet.get(j), O_BESTForChildren.get(0).get(m1).get(keyChild1).get(index1).get(0),O_BESTForChildren.get(1).get(m2).get(keyChild2).get(index2).get(0), deltaEnergyWithChild, ammountOfMutationGroup);
														PushInformationToO_BEST(O_BEST, key,keyChild1,keyChild2, alefBet.get(j), O_BESTForChildren.get(0).get(m1).get(keyChild1).get(index1).get(0),O_BESTForChildren.get(1).get(m2).get(keyChild2).get(index2).get(0), deltaEnergyWithChild, RNAsequence.substring(second,second+1),second,O_BESTForChildren.get(0).get(m1).get(keyChild1).get(index1).get(2)+O_BESTForChildren.get(1).get(m2).get(keyChild2).get(index2).get(2), m1+m2, numOfMutations, ammountOfMutationGroup);
													}
												}
											}
										}
										else if(m1+m2+1>m)
											m2=numOfMutations;
										
									}
								}
							}
						}
					}
				}
				
				node.AddInfoNode(O_BEST);
				//node.AddTopO_BEST(topO_BEST);
				return O_BEST;
			}
			
			//If node is (m,k,l)
			if (first>=0 && second>=0 && third>=0) {
				for(int i=0;i<matrixGraph.size();i++) {
					if (matrixGraph.get(third).get(i)==2)
						edgeOptimal=i;
					else if (matrixGraph.get(third).get(i)==3)
						edgeSubopt=i;
					else if (matrixGraph.get(third).get(i)==4) {
						edgeOptimal=i;
						edgeSubopt=i;
					}
				}
				int whoIs1=-1;
				int whoIs2=-1;
				if(node.GetChildren().get(0).GetFirst()>=0 && node.GetChildren().get(0).GetSecond()>=0 && node.GetChildren().get(0).GetThird()>=0) {
					whoIs1=findWhoIs(first, second, third, node.GetChildren().get(0).GetFirst());
					whoIs2=findWhoIs(first, second, third, node.GetChildren().get(0).GetSecond());
				}
				else if(node.GetChildren().get(0).GetFirst()>=0 && node.GetChildren().get(0).GetSecond()>=0 && node.GetChildren().get(0).GetThird()==-1) {
					whoIs1=findWhoIs(first, second, third, node.GetChildren().get(0).GetFirst());
				}
				for (int i=0;i<alefBet.size();i++) {
					for (int j=0;j<alefBet.size();j++) {
						ArrayList<String> key=new ArrayList<String>();
						key.add(alefBet.get(i));
						key.add(alefBet.get(j));
						
						for(int k=0;k<alefBet.size();k++) {
							//Optimal edge
							//No edge optimal
							if (edgeOptimal==-1) {
								double energyOptimalAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, optimalStructure, alefBet.get(k).charAt(0), third);
								deltaEnergyOptimal=energyOptimalAfterMutation-energyOptimalBeforeMutation;		
							}
							//Edge with relation
							else if (edgeOptimal==first || edgeOptimal==second) {
									whichOneOptimal=first;
									whichOneIndexOptimal=i;
									if (second==edgeOptimal) {
										whichOneOptimal=second;
										whichOneIndexOptimal=j;
									}
									double energyOptimalAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, optimalStructure,alefBet.get(whichOneIndexOptimal).charAt(0),whichOneOptimal ,alefBet.get(k).charAt(0), third);
									deltaEnergyOptimal=energyOptimalAfterMutation-energyOptimalBeforeMutation;
							}
							//Edge without relation
							else {
								deltaEnergyOptimal=0;
							}
							
							//Subopt edge
							//No edge optimal
							if (edgeSubopt==-1) {
								double energySuboptAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, suboptStructure, alefBet.get(k).charAt(0), third);
								deltaEnergySubopt=energySuboptBeforeMutation-energySuboptAfterMutation;
							}
							//Edge with relation
							else if (edgeSubopt==first || edgeSubopt==second) {
								whichOneSubopt=first;
								whichOneIndexSubopt=i;
								if (second==edgeSubopt) {
									whichOneSubopt=second;
									whichOneIndexSubopt=j;
								}
								double energySuboptAfterMutation=calcDeltaEnergyAfterMutation(RNAsequence, suboptStructure,alefBet.get(whichOneIndexSubopt).charAt(0),whichOneSubopt ,alefBet.get(k).charAt(0), third);
								deltaEnergySubopt=energySuboptBeforeMutation-energySuboptAfterMutation;
							}
							//Edge without relation
							else {
								deltaEnergySubopt=0;
							}
							
							deltaEnergy=deltaEnergyOptimal+deltaEnergySubopt;
							String letter=alefBet.get(k);
							
							ArrayList<String> keyChild1;
							ArrayList<String> keyChild2;
							
							if (O_BESTForChildren.get(0).get(0).size()==16)
								keyChild1=createKeyChild(whoIs1, whoIs2, alefBet, i, j, k);
							else
								keyChild1=createKeyChild(alefBet, k);
							
							if (O_BESTForChildren.get(1).get(0).size()==16)
								keyChild2=createKeyChild(whoIs1, whoIs2, alefBet, i, j, k);
							else
								keyChild2=createKeyChild(alefBet, k);
				
							for (int m=0;m<=numOfMutations;m++) {
								for (int m1=0;m1<=numOfMutations;m1++) {
									for (int m2=0;m2<=numOfMutations;m2++) {
										if (letter.equals(RNAsequence.substring(third,third+1))){
											if (m1+m2==m) {
												if (O_BESTForChildren.get(0).get(m1).containsKey(keyChild1) && O_BESTForChildren.get(1).get(m2).containsKey(keyChild2)) {
													for(int index1=0;index1<O_BESTForChildren.get(0).get(m1).get(keyChild1).size();index1++) {
														double energyChild1=Double.parseDouble(O_BESTForChildren.get(0).get(m1).get(keyChild1).get(index1).get(1));
														for(int index2=0;index2<O_BESTForChildren.get(1).get(m2).get(keyChild2).size();index2++) {
															double energyChild2=Double.parseDouble(O_BESTForChildren.get(1).get(m2).get(keyChild2).get(index2).get(1));
															double deltaEnergyWithChild=deltaEnergy+energyChild1+energyChild2;
															//CheckForO_BEST(topO_BEST.get(numOfMutation(alefBet.get(k),RNAsequence.substring(third,third+1),m)), alefBet.get(k), O_BESTForChildren.get(0).get(m1).get(keyChild1).get(index1).get(0),O_BESTForChildren.get(1).get(m2).get(keyChild2).get(index2).get(0), deltaEnergyWithChild, ammountOfMutationGroup);
															PushInformationToO_BEST(O_BEST, key,keyChild1,keyChild2, alefBet.get(k), O_BESTForChildren.get(0).get(m1).get(keyChild1).get(index1).get(0),O_BESTForChildren.get(1).get(m2).get(keyChild2).get(index2).get(0), deltaEnergyWithChild, RNAsequence.substring(third,third+1),third,O_BESTForChildren.get(0).get(m1).get(keyChild1).get(index1).get(2)+O_BESTForChildren.get(1).get(m2).get(keyChild2).get(index2).get(2), m, numOfMutations, ammountOfMutationGroup);
														}
													}
												}
											}
											else if(m1+m2>m)
												m2=numOfMutations;
										}
										else {
											if(m1+m2+1==m) {
												if (O_BESTForChildren.get(0).get(m1).containsKey(keyChild1) && O_BESTForChildren.get(1).get(m2).containsKey(keyChild2)) {
													for(int index1=0;index1<O_BESTForChildren.get(0).get(m1).get(keyChild1).size();index1++) {
														double energyChild1=Double.parseDouble(O_BESTForChildren.get(0).get(m1).get(keyChild1).get(index1).get(1));
														for(int index2=0;index2<O_BESTForChildren.get(1).get(m2).get(keyChild2).size();index2++) {
															double energyChild2=Double.parseDouble(O_BESTForChildren.get(1).get(m2).get(keyChild2).get(index2).get(1));
															double deltaEnergyWithChild=deltaEnergy+energyChild1+energyChild2;
															//CheckForO_BEST(topO_BEST.get(numOfMutation(alefBet.get(k),RNAsequence.substring(third,third+1),m1+m2)), alefBet.get(k), O_BESTForChildren.get(0).get(m1).get(keyChild1).get(index1).get(0),O_BESTForChildren.get(1).get(m2).get(keyChild2).get(index2).get(0), deltaEnergyWithChild, ammountOfMutationGroup);
															PushInformationToO_BEST(O_BEST, key,keyChild1,keyChild2, alefBet.get(k), O_BESTForChildren.get(0).get(m1).get(keyChild1).get(index1).get(0),O_BESTForChildren.get(1).get(m2).get(keyChild2).get(index2).get(0), deltaEnergyWithChild, RNAsequence.substring(third,third+1),third,O_BESTForChildren.get(0).get(m1).get(keyChild1).get(index1).get(2)+O_BESTForChildren.get(1).get(m2).get(keyChild2).get(index2).get(2), m1+m2, numOfMutations, ammountOfMutationGroup);
														}
													}
												}
											}
											else if(m1+m2+1>m)
												m2=numOfMutations;
											
										}
									}
								}
							}
						}
					}
				}
				
				node.AddInfoNode(O_BEST);
				//node.AddTopO_BEST(topO_BEST);
				return O_BEST;
			}
		}
		
		
		return O_BEST;
	}
	
	
	public String FindNewEdge(String sequence,String structure,int index) {
		int count=0;
		String newStructure;
		if(sequence.charAt(index)=='A') {
			//left contact
			for(int i=index-1;i>=0;i--) {
				if (structure.charAt(i)=='.' && sequence.charAt(i)=='U' && count==0) {
					newStructure=structure.substring(0,i)+"("+structure.substring(i+1,index)+")"+structure.substring(index+1);
					return newStructure;
				}
				else if (structure.charAt(i)=='(' && count==0)
					i=-1;
				else if (structure.charAt(i)==')')
					count++;
				else if (structure.charAt(i)=='(')
					count--;
			}
			//right contact
			for(int i=index+1;i<sequence.length();i++) {
				if (structure.charAt(i)=='.' && sequence.charAt(i)=='U' && count==0) {
					newStructure=structure.substring(0,index)+"("+structure.substring(index+1,i)+")"+structure.substring(i+1);
					return newStructure;
				}
				else if (structure.charAt(i)==')' && count==0)
					i=sequence.length();
				else if (structure.charAt(i)=='(')
					count++;
				else if (structure.charAt(i)==')')
					count--;
			}
			return structure;
		}
		else if(sequence.charAt(index)=='C') {
			//left contact
			for(int i=index-1;i>=0;i--) {
				if (structure.charAt(i)=='.' && sequence.charAt(i)=='G' && count==0) {
					newStructure=structure.substring(0,i)+"("+structure.substring(i+1,index)+")"+structure.substring(index+1);
					return newStructure;
				}
				else if (structure.charAt(i)=='(' && count==0)
					i=-1;
				else if (structure.charAt(i)==')')
					count++;
				else if (structure.charAt(i)=='(')
					count--;
			}
			//right contact
			for(int i=index+1;i<sequence.length();i++) {
				if (structure.charAt(i)=='.' && sequence.charAt(i)=='G' && count==0) {
					newStructure=structure.substring(0,index)+"("+structure.substring(index+1,i)+")"+structure.substring(i+1);
					return newStructure;
				}
				else if (structure.charAt(i)==')' && count==0)
					i=sequence.length();
				else if (structure.charAt(i)=='(')
					count++;
				else if (structure.charAt(i)==')')
					count--;
			}
			return structure;
		}
		else if(sequence.charAt(index)=='G') {
			//left contact
			for(int i=index-1;i>=0;i--) {
				if (structure.charAt(i)=='.' && (sequence.charAt(i)=='U' || sequence.charAt(i)=='C') && count==0) {
					newStructure=structure.substring(0,i)+"("+structure.substring(i+1,index)+")"+structure.substring(index+1);
					return newStructure;
				}
				else if (structure.charAt(i)=='(' && count==0)
					i=-1;
				else if (structure.charAt(i)==')')
					count++;
				else if (structure.charAt(i)=='(')
					count--;
			}
			//right contact
			for(int i=index+1;i<sequence.length();i++) {
				if (structure.charAt(i)=='.' && (sequence.charAt(i)=='U' || sequence.charAt(i)=='C') && count==0) {
					newStructure=structure.substring(0,index)+"("+structure.substring(index+1,i)+")"+structure.substring(i+1);
					return newStructure;
				}
				else if (structure.charAt(i)==')' && count==0)
					i=sequence.length();
				else if (structure.charAt(i)=='(')
					count++;
				else if (structure.charAt(i)==')')
					count--;
			}
			return structure;
		}
		else if(sequence.charAt(index)=='U') {
			//left contact
			for(int i=index-1;i>=0;i--) {
				if (structure.charAt(i)=='.' && (sequence.charAt(i)=='A' || sequence.charAt(i)=='G') && count==0) {
					newStructure=structure.substring(0,i)+"("+structure.substring(i+1,index)+")"+structure.substring(index+1);
					return newStructure;
				}
				else if (structure.charAt(i)=='(' && count==0)
					i=-1;
				else if (structure.charAt(i)==')')
					count++;
				else if (structure.charAt(i)=='(')
					count--;
			}
			//right contact
			for(int i=index+1;i<sequence.length();i++) {
				if (structure.charAt(i)=='.' && (sequence.charAt(i)=='A' || sequence.charAt(i)=='G') && count==0) {
					newStructure=structure.substring(0,index)+"("+structure.substring(index+1,i)+")"+structure.substring(i+1);
					return newStructure;
				}
				else if (structure.charAt(i)==')' && count==0)
					i=sequence.length();
				else if (structure.charAt(i)=='(')
					count++;
				else if (structure.charAt(i)==')')
					count--;
			}
			return structure;
		}
		return structure;
	}
	
	public String checkContact(String structure,int v1,int v2,char c1,char c2) {
		if(c1=='A' && c2=='U') 
			return structure;
		else if(c1=='C' && c2=='G') 
			return structure;
		else if(c1=='G' && (c2=='C' || c2=='U')) 
			return structure;
		else if(c1=='U' && (c2=='A' || c2=='G')) 
			return structure;

		StringBuilder newStructure = new StringBuilder(structure);
		newStructure.setCharAt(v1, '.');
		newStructure.setCharAt(v2, '.');
		return newStructure.toString();
	}
	
	public String changeSequence(String sequence,char letter,int index) {
		StringBuilder newStructure = new StringBuilder(sequence);
		newStructure.setCharAt(index, letter);
		return newStructure.toString();
	}
	
	public String changeSequence(String sequence,char letter1,char letter2,int index1,int index2) {
		StringBuilder newStructure = new StringBuilder(sequence);
		newStructure.setCharAt(index1, letter1);
		newStructure.setCharAt(index2, letter2);
		return newStructure.toString();
	}
	
	public int findWhoIs(int first,int second,int third,int child) {
		if (first==child)
			return 0;
		else if (second==child)
			return 1;
		else if (third==child)
			return 2;
		return child;
	}
	public void CheckForO_BEST(ArrayList<ArrayList<String>> O_BEST,String letter1,String letter2,String letter3,String mutationUntilHere,double energy ,String nextKey1 ,String nextKey2 ,int ammountOfMutationGroup) {
		if (O_BEST.size()==0) {
			O_BEST.add(new ArrayList<String>());
			O_BEST.get(0).add(letter1);
			O_BEST.get(0).add(String.valueOf(energy));
			O_BEST.get(0).add(mutationUntilHere);
			O_BEST.get(0).add(nextKey1);
			O_BEST.get(0).add(letter2);
			if (!(nextKey2.equals("")))
				O_BEST.get(0).add(nextKey2);
			if (!(letter3.equals("")))
				O_BEST.get(0).add(letter3);
		}
		else if (O_BEST.size()==ammountOfMutationGroup && energy<Double.parseDouble(O_BEST.get(ammountOfMutationGroup-1).get(1))) {return;}
		else {
			int flag=1;
			for(int t=0;t<O_BEST.size();t++) {
				if (energy>Double.parseDouble(O_BEST.get(t).get(1))) {
					O_BEST.add(t,new ArrayList<String>());
					O_BEST.get(t).add(letter1);
					O_BEST.get(t).add(String.valueOf(energy));
					O_BEST.get(t).add(mutationUntilHere);
					O_BEST.get(t).add(nextKey1);
					O_BEST.get(t).add(letter2);
					if (!(nextKey2.equals("")))
						O_BEST.get(t).add(nextKey2);
					if (!(letter3.equals("")))
						O_BEST.get(t).add(letter3);
					t=O_BEST.size();
					flag=0;
				}
			}
			if(flag==1) {
				O_BEST.add(new ArrayList<String>());
				O_BEST.get(O_BEST.size()-1).add(letter1);
				O_BEST.get(O_BEST.size()-1).add(String.valueOf(energy));
				O_BEST.get(O_BEST.size()-1).add(mutationUntilHere);
				O_BEST.get(O_BEST.size()-1).add(nextKey1);
				O_BEST.get(O_BEST.size()-1).add(letter2);
				if (!(nextKey2.equals("")))
					O_BEST.get(O_BEST.size()-1).add(nextKey2);
				if (!(letter3.equals("")))
					O_BEST.get(O_BEST.size()-1).add(letter3);
				
			}
			if (O_BEST.size()>ammountOfMutationGroup) {
				O_BEST.remove(ammountOfMutationGroup);
			}
		}
	}
	public double calcDeltaEnergyAfterMutation(String RNAsequence,String structure,char letter,int index) {
		ViennaRNA vienna =new ViennaRNA();
		String newSequence=changeSequence(RNAsequence, letter, index);
		return Double.parseDouble(vienna.RNAfoldConstraint(newSequence, structure));	
	}
	
	public double calcDeltaEnergyAfterMutation(String RNAsequence,String structure,char letter1,int index1,char letter2,int index2) {
		ViennaRNA vienna =new ViennaRNA();
		String newSequence=changeSequence(RNAsequence, letter1,letter2,index1,index2);
		String newStructure=checkContact(structure,index1,index2,letter1,letter2);
		return Double.parseDouble(vienna.RNAeval(newSequence, newStructure));
	}
	
	public String strMutation(String RNAsequence,ArrayList<String> alefBet,int location,int indexLocation,int whichOneOptimal,int whichOneIndexOptimal,int whichOneSubopt,int whichOneIndexSubopt) {
		String let;
		String let1="";
		String let2="";
		String let3="";
		if (RNAsequence.charAt(location)!=alefBet.get(indexLocation).charAt(0))
			let3=RNAsequence.substring(location,location+1)+String.valueOf(location)+alefBet.get(indexLocation).substring(0);
		if (whichOneOptimal!=-1) {
			if(RNAsequence.charAt(whichOneOptimal)!=alefBet.get(whichOneIndexOptimal).charAt(0)) {
				let1=RNAsequence.substring(whichOneOptimal,whichOneOptimal+1)+String.valueOf(whichOneOptimal)+alefBet.get(whichOneIndexOptimal).substring(0);
			}
		}
		if (whichOneSubopt!=-1) {
			if(RNAsequence.charAt(whichOneSubopt)!=alefBet.get(whichOneIndexSubopt).charAt(0)) {
				let1=RNAsequence.substring(whichOneSubopt,whichOneSubopt+1)+String.valueOf(whichOneSubopt)+alefBet.get(whichOneIndexSubopt).substring(0);
			}
		}
		if (whichOneOptimal!=whichOneSubopt)
			let=let1+let2+let3;
		else
			let=let2+let3;
		return let;
	}
	
	public void PushInformationToO_BEST(ArrayList<HashMap<ArrayList<String>,ArrayList<ArrayList<String>>>> O_BEST,ArrayList<String> key,ArrayList<String> keyChild1,ArrayList<String> keyChild2 ,String letter1,String letter2,String letter3,double deltaEnergy,String letterSequence,int index,String mutationUntilHere ,int mutations,int numOfMutations,int ammountOfMutationGroup) {
		String nextKey1="";
		for (int i=0;i<keyChild1.size();i++)
			nextKey1=nextKey1+keyChild1.get(i);
		String nextKey2="";
		for (int i=0;i<keyChild2.size();i++)
			nextKey2=nextKey2+keyChild2.get(i);
		if (!(letter1.equals(letterSequence))) {
			mutations++;
			mutationUntilHere=mutationUntilHere+letterSequence+String.valueOf(index)+letter1;
		}
		if(mutations>=0 && mutations<=numOfMutations) {
			if (O_BEST.get(mutations).containsKey(key)) {
				ArrayList<ArrayList<String>> listOfO_BEST=O_BEST.get(mutations).get(key);
				CheckForO_BEST(listOfO_BEST, letter1, letter2,letter3,mutationUntilHere, deltaEnergy,nextKey1,nextKey2, ammountOfMutationGroup);
				O_BEST.get(mutations).put(key, listOfO_BEST);
			}
			else {
				ArrayList<ArrayList<String>> listOfO_BEST=new ArrayList<ArrayList<String>>();
				CheckForO_BEST(listOfO_BEST, letter1, letter2,letter3,mutationUntilHere, deltaEnergy,nextKey1,nextKey2, ammountOfMutationGroup);
				O_BEST.get(mutations).put(key, listOfO_BEST);
			}
		}
	}
	
	/*
	public void PushInformationAndCheckMaximum(ArrayList<HashMap<ArrayList<String>,ArrayList<String>>> matrixForDynamicProgramming,ArrayList<String> key,String letter,double deltaEnergy,String letterSequence,int mutations,int numOfMutations) {
		if (!(letter.equals(letterSequence)))
			mutations++;
		if(mutations>=0 && mutations<=numOfMutations) {
			if (matrixForDynamicProgramming.get(mutations).containsKey(key)) {
				if(deltaEnergy>Double.parseDouble(matrixForDynamicProgramming.get(mutations).get(key).get(1))) {
					ArrayList<String> value=new ArrayList<String>();
					value.add(letter);
					value.add(String.valueOf(deltaEnergy));
					matrixForDynamicProgramming.get(mutations).put(key, value);
				}
			}
			else {
				ArrayList<String> value=new ArrayList<String>();
				value.add(letter);
				value.add(String.valueOf(deltaEnergy));
				matrixForDynamicProgramming.get(mutations).put(key, value);
			}
		}
	}
	*/
	
	/*
	public void pushInformationToMatrixAndO_BEST(ArrayList<HashMap<ArrayList<String>, ArrayList<String>>> matrixForDynamicProgramming,ArrayList<ArrayList<String>> O_BEST,String letters,ArrayList<String> key,double deltaEnergy,int numOfMutations,int ammountOfMutationGroup) {
		int mutations=letters.length()/3;
		if (numOfMutations==mutations) {
			//public void CheckForO_BEST(O_BEST,letter1,String letter2,double energy,int ammountOfMutationGroup) {
		}
		if(mutations>=1 && mutations<=numOfMutations) {
			if (matrixForDynamicProgramming.get(mutations).containsKey(key)) {
				if(deltaEnergy>Double.parseDouble(matrixForDynamicProgramming.get(mutations).get(key).get(1))) {
					ArrayList<String> value=new ArrayList<String>();
					value.add(letters);
					value.add(String.valueOf(deltaEnergy));
					matrixForDynamicProgramming.get(mutations).put(key, value);
				}
			}
			else {
				ArrayList<String> value=new ArrayList<String>();
				value.add(letters);
				value.add(String.valueOf(deltaEnergy));
				matrixForDynamicProgramming.get(mutations).put(key, value);
			}
		}
	}
	*/
	
	public ArrayList<String> createKeyChild(int whoIs1,int whoIs2,ArrayList<String> alefBet,int i,int j,int k){
		String key1="";
		if (whoIs1==0)
			key1=alefBet.get(i);
		if (whoIs1==1)
			key1=alefBet.get(j);
		if (whoIs1==2)
			key1=alefBet.get(k);
		String key2="";
		if (whoIs2==0)
			key2=alefBet.get(i);
		if (whoIs2==1)
			key2=alefBet.get(j);
		if (whoIs2==2)
			key2=alefBet.get(k);
		ArrayList<String> keyChild=new ArrayList<String>();	
		keyChild.add(key1);
		keyChild.add(key2);
		return keyChild;
	}
	public ArrayList<String> createKeyChild(ArrayList<String> alefBet,int k){
		ArrayList<String> keyChild=new ArrayList<String>();	
		keyChild.add(alefBet.get(k));
		return keyChild;
	}
	
	public ArrayList<String> createKeyChild(ArrayList<String> alefBet,int i,int j){
		ArrayList<String> keyChild=new ArrayList<String>();	
		keyChild.add(alefBet.get(i));
		keyChild.add(alefBet.get(j));
		return keyChild;
	}
	
	public ArrayList<ArrayList<String>> FindTopO_BEST(ArrayList<HashMap<ArrayList<String>, ArrayList<ArrayList<String>>>> O_BEST,ArrayList<String> alefBet,int numOfMutations,int ammountOfMutationGroup){
		ArrayList<ArrayList<String>> topO_BEST=new ArrayList<ArrayList<String>>();
		for (int i=0;i<alefBet.size();i++) {
			ArrayList<String> key=new ArrayList<String>();
			key.add(alefBet.get(i));
			if (O_BEST.get(numOfMutations).containsKey(key)) {
				for (int j=0;j<O_BEST.get(numOfMutations).get(key).size();j++) {
					CheckForO_BEST(topO_BEST, O_BEST.get(numOfMutations).get(key).get(j).get(0), O_BEST.get(numOfMutations).get(key).get(j).get(4),"",O_BEST.get(numOfMutations).get(key).get(j).get(2), Double.parseDouble(O_BEST.get(numOfMutations).get(key).get(j).get(1)),O_BEST.get(numOfMutations).get(key).get(j).get(3),"", ammountOfMutationGroup);
				}
			}
		}		
		return topO_BEST;
	}
	
	public String FindSequence(String RNAsequnce,String groupOfMutations) {
		StringBuilder sequenceMutation = new StringBuilder(RNAsequnce);
		for (int i=0;i<groupOfMutations.length();i++) {
			i++;
			int j=i;
			String index="";
			while(groupOfMutations.charAt(j)>=48 && groupOfMutations.charAt(j)<=57) {
				index=index+groupOfMutations.substring(j,j+1);
				j++;
			}
			sequenceMutation.setCharAt(Integer.parseInt(index), groupOfMutations.charAt(j));
			i=j;
		}
		return String.valueOf(sequenceMutation);
	}
	
}
