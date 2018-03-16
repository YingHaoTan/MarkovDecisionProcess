package mdp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MdpOptimizer is an optimizer that attempts to maximize the utilities of a given ruleset of a game
 * using markov decision process
 * 
 * @author Ying Hao
 */
public abstract class MdpOptimizer<State, Action> {
	private Map<State, Double> utilityMap;
	private Map<State, Action> policy;
	private Ruleset<State, Action> ruleset;
	private List<Map<State, Double>> dump;
	private double discount;
	private double convergence;
	
	public MdpOptimizer(Ruleset<State, Action> ruleset, double discount, double convergence) {
		this.utilityMap = new HashMap<>();
		this.policy = new HashMap<>();
		this.ruleset = ruleset;
		this.dump = new ArrayList<>();
		this.discount = discount;
		this.convergence = convergence;
	}
	
	/**
	 * Gets discount value
	 * @return
	 */
	public double getDiscount() {
		return discount;
	}

	/**
	 * Gets convergence value
	 * @return
	 */
	public double getConvergence() {
		return convergence;
	}

	/**
	 * Gets the ruleset of the game
	 * @return
	 */
	public Ruleset<State, Action> getRuleset() {
		return ruleset;
	}
	
	/**
	 * Gets the current utility of the specified state
	 * @param state
	 * @return
	 */
	public double getCurrentUtility(State state) {
		return utilityMap.getOrDefault(state, 0.0);
	}
	
	/**
	 * Sets the current utility of the specified state
	 * @param state
	 * @param utility
	 */
	protected void setCurrentUtility(State state, double utility) {
		utilityMap.put(state, utility);
	}
	
	/**
	 * Gets the policy action for the specified state
	 * @param state
	 * @return
	 */
	public Action getPolicyAction(State state) {
		return policy.get(state);
	}
	
	/**
	 * Sets the policy action for the specified state
	 * @param state
	 */
	protected void setPolicyAction(State state, Action action) {
		policy.put(state, action);
	}
	
	/**
	 * Evaluates a policy that maximizes the utility value based on current utilities
	 * @return
	 */
	protected boolean evaluate() {
		boolean changed = false;
		Ruleset<State, Action> ruleset = getRuleset();
		
		for(State state: ruleset.getAllPossibleStates()) {
			Action actionToTake = null;
			List<Action> actions = ruleset.getAvailableActions(state);
			double max = 0;
			
			for(int i = 0; i < actions.size(); i++) {
				double future = 0;
				
				List<Tuple<State, Double>> stateProbabilities = ruleset.getNextStateProbabilities(state, actions.get(i));
				
				for(Tuple<State, Double> tuple: stateProbabilities)
					future += tuple.second * getCurrentUtility(tuple.first);
				
				if(i == 0 || future > max) {
					actionToTake = actions.get(i);
					max = future;
				}
			}
			
			changed |= (actionToTake != getPolicyAction(state));
			setPolicyAction(state, actionToTake);
		}
		
		return changed;
	}
	
	/**
	 * Dumps the current utility map for book keeping purposes
	 * @param error
	 */
	protected void dump() {
		Map<State, Double> map = new HashMap<State, Double>();
		for(State state: ruleset.getAllPossibleStates())
			map.put(state, getCurrentUtility(state));
		
		dump.add(map);
	}
	
	public abstract void optimize();
	
	public String dumpCSV() {
		StringBuilder strbuilder = new StringBuilder();
		
		for(State state: ruleset.getAllPossibleStates()) {
			strbuilder.append("\"").append(state).append("\", ");
			for(int i = 0; i < dump.size(); i++)
				strbuilder.append(dump.get(i).get(state)).append(", ");
			
			strbuilder.setLength(strbuilder.lastIndexOf(","));
			strbuilder.append("\n");
		}
		
		return strbuilder.toString();
	}

	@Override
	public String toString() {
		StringBuilder strbuilder = new StringBuilder();
		
		strbuilder.append("Total number of iterations: ").append(dump.size()).append("\n");
		
		for(State state: ruleset.getAllPossibleStates())
			strbuilder.append(state + "[" + getCurrentUtility(state) + "]: " + getPolicyAction(state) + "\n");
		
		return strbuilder.toString();
	}

}
