package mdp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MdpPolicyOptimizer<State, Action> extends MdpOptimizer<State, Action> {

	public MdpPolicyOptimizer(Ruleset<State, Action> ruleset, double discount, double convergence) {
		super(ruleset, discount, convergence);
	}

	@Override
	public void optimize() {
		while(evaluate()) {
			dump();
			converge();
		}
		
		dump();
	}
	
	private void converge() {
		Ruleset<State, Action> ruleset = getRuleset();
		double cvalue = getConvergence() * ruleset.getMaxReward();
		double error;
		
		do {
			error = 0;
			
			Map<State, Double> utilityMap = new HashMap<State, Double>();
			for(State state: ruleset.getAllPossibleStates()) {
				double utility = ruleset.getImmediateReward(state);
				double future = 0;
				
				List<Tuple<State, Double>> stateProbabilities = ruleset.getNextStateProbabilities(state, getPolicyAction(state));
				
				for(Tuple<State, Double> tuple: stateProbabilities)
					future += tuple.second * getCurrentUtility(tuple.first);
				
				utility = utility + (getDiscount() * future);
				error = Math.max(error, Math.abs(utility - getCurrentUtility(state)));
				utilityMap.put(state, utility);
			}
			
			for(State state: ruleset.getAllPossibleStates())
				setCurrentUtility(state, utilityMap.get(state));
		} while(error > cvalue);
	}

}
