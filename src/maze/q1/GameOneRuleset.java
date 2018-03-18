package maze.q1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import maze.Action;
import maze.Coordinate;
import mdp.Ruleset;
import mdp.Tuple;

public class GameOneRuleset implements Ruleset<Coordinate, Action> {
	private static final int WIDTH = 6;
	private static final int HEIGHT = 6;
	private static final List<Coordinate> WALL_STATES = Arrays.asList(new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(3, 1), new Coordinate(1, 5), new Coordinate(4, 4));
	private static final List<Coordinate> NEGATIVE_STATES = Arrays.asList(new Coordinate(4, 1), new Coordinate(3, 2), new Coordinate(2, 3), new Coordinate(1, 4), new Coordinate(5, 4));
	private static final List<Coordinate> POSITIVE_STATES = Arrays.asList(new Coordinate(0, 5), new Coordinate(2, 5), new Coordinate(3, 4), new Coordinate(4, 3), new Coordinate(5, 2), new Coordinate(5, 5));
	
	@Override
	public double getMaxReward() {
		return 1;
	}
	
	@Override
	public List<Coordinate> getAllPossibleStates() {
		List<Coordinate> states = new ArrayList<>();
		
		for(int x = 0; x < WIDTH; x++) {
			for(int y = 0; y < HEIGHT; y++) {
				Coordinate coordinate = new Coordinate(x, y);
				
				if(!WALL_STATES.contains(coordinate))
					states.add(coordinate);
			}
		}
				
		
		return states;
	}
	
	private Coordinate validateState(Coordinate ocoordinate, Coordinate ncoordinate) {
		if(ncoordinate.x < 0 || ncoordinate.x >= WIDTH || ncoordinate.y < 0 || ncoordinate.y >= HEIGHT || WALL_STATES.contains(ncoordinate))
			return ocoordinate;
		else
			return ncoordinate;
	}
	
	@Override
	public List<Tuple<Coordinate, Double>> getNextStateProbabilities(Coordinate state, Action action) {
		List<Tuple<Coordinate, Double>> probabilities = new ArrayList<>();
		
		switch(action) {
			case UP:
				probabilities.add(new Tuple<>(validateState(state, new Coordinate(state.x, state.y + 1)), 0.8));
				probabilities.add(new Tuple<>(validateState(state, new Coordinate(state.x - 1, state.y)), 0.1));
				probabilities.add(new Tuple<>(validateState(state, new Coordinate(state.x + 1, state.y)), 0.1));
				break;
			case DOWN:
				probabilities.add(new Tuple<>(validateState(state, new Coordinate(state.x, state.y - 1)), 0.8));
				probabilities.add(new Tuple<>(validateState(state, new Coordinate(state.x + 1, state.y)), 0.1));
				probabilities.add(new Tuple<>(validateState(state, new Coordinate(state.x - 1, state.y)), 0.1));
				break;
			case LEFT:
				probabilities.add(new Tuple<>(validateState(state, new Coordinate(state.x - 1, state.y)), 0.8));
				probabilities.add(new Tuple<>(validateState(state, new Coordinate(state.x, state.y - 1)), 0.1));
				probabilities.add(new Tuple<>(validateState(state, new Coordinate(state.x, state.y + 1)), 0.1));
				break;
			case RIGHT:
				probabilities.add(new Tuple<>(validateState(state, new Coordinate(state.x + 1, state.y)), 0.8));
				probabilities.add(new Tuple<>(validateState(state, new Coordinate(state.x, state.y + 1)), 0.1));
				probabilities.add(new Tuple<>(validateState(state, new Coordinate(state.x, state.y - 1)), 0.1));
				break;
			default:
				break;
		}
		
		return probabilities;
	}

	@Override
	public List<Action> getAvailableActions(Coordinate state) {
		return Arrays.asList(Action.values());
	}

	@Override
	public double getImmediateReward(Coordinate state) {
		if(NEGATIVE_STATES.contains(state))
			return -1;
		else if(POSITIVE_STATES.contains(state))
			return 1;
		else
			return -0.04;
	}
	
}
