package mdp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MdpOptimizer is an optimizer that attempts to maximize the utilities of a given ruleset of a game
 * using markov decision process value iteration
 * 
 * @author Ying Hao
 */
public class MdpValueOptimizer<State, Action> extends MdpOptimizer<State, Action> {
	
	public MdpValueOptimizer(Ruleset<State, Action> ruleset, double discount, double convergence) {
		super(ruleset, discount, convergence);
	}

	@Override
	public void optimize() {
		converge();
		evaluate();
	}
	
	private void converge() {
		Ruleset<State, Action> ruleset = getRuleset();
		double cvalue = getConvergence() * ruleset.getMaxReward();
		double error;
		
		do {
			error = 0;
			
			Map<State, Double> utilityMap = new HashMap<State, Double>();
			for(State state: ruleset.getAllPossibleStates()) {
				List<Action> actions = ruleset.getAvailableActions(state);
				double utility = ruleset.getImmediateReward(state);
				double maxfuture = 0;
				
				for(int i = 0; i < actions.size(); i++) {
					double future = 0;
					
					List<Tuple<State, Double>> stateProbabilities = ruleset.getNextStateProbabilities(state, actions.get(i));
					
					for(Tuple<State, Double> tuple: stateProbabilities)
						future += tuple.second * getCurrentUtility(tuple.first);
					
					if(i == 0 || future > maxfuture)
						maxfuture = future;
				}
				
				utility = utility + (getDiscount() * maxfuture);
				error = Math.max(error, Math.abs(utility - getCurrentUtility(state)));
				utilityMap.put(state, utility);
			}
			
			for(State state: ruleset.getAllPossibleStates())
				setCurrentUtility(state, utilityMap.get(state));
			
			dump();
		} while(error > cvalue);
	}
	
}
