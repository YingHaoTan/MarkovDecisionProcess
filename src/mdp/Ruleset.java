package mdp;

import java.util.List;

/**
 * Ruleset defines the rules of a game
 * 
 * @author Ying Hao
 */
public interface Ruleset<State, Action> {
	
	/**
	 * Gets the maximum reward
	 * @return
	 */
	public double getMaxReward();
	
	/**
	 * Gets a list of all the possible states
	 * @return
	 */
	public List<State> getAllPossibleStates();
	
	/**
	 * Gets the probabilities of the subsequent states given the current state and follow up action
	 * @param state
	 * @param action
	 * @return
	 */
	public List<Tuple<State, Double>> getNextStateProbabilities(State state, Action action);
	
	/**
	 * Gets the available actions at the given state
	 * @param state
	 * @return
	 */
	public List<Action> getAvailableActions(State state);
	
	/**
	 * Gets the immediate reward at a specified state
	 * @param state
	 * @return
	 */
	public double getImmediateReward(State state);

}
