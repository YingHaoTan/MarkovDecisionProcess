package mdp;

import java.util.List;

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

	@Override
	protected double estimateUtility(State state) {
		Ruleset<State, Action> ruleset = getRuleset();
		double utility = ruleset.getImmediateReward(state);
		double future = 0;
		
		List<Tuple<State, Double>> stateProbabilities = ruleset.getNextStateProbabilities(state, getPolicyAction(state));
		
		for(Tuple<State, Double> tuple: stateProbabilities)
			future += tuple.second * getCurrentUtility(tuple.first);
		
		utility = utility + (getDiscount() * future);
		
		return utility;
	}

}
