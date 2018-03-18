package mdp;

import java.util.List;

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

	@Override
	protected double estimateUtility(State state) {
		Ruleset<State, Action> ruleset = getRuleset();
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
		
		return utility;
	}
	
}
