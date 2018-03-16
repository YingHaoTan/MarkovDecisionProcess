import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import maze.Action;
import maze.Coordinate;
import maze.q1.GameOneRuleset;
import mdp.MdpOptimizer;
import mdp.MdpPolicyOptimizer;
import mdp.MdpValueOptimizer;
import mdp.Ruleset;

public class Program {
	
	public static void main(String[] args) throws IOException {
		Ruleset<Coordinate, Action> rule = new GameOneRuleset();
		List<MdpOptimizer<Coordinate, Action>> optimizers = Arrays.asList(new MdpValueOptimizer<>(rule, 0.99, 0.001), new MdpPolicyOptimizer<>(rule, 0.99, 0.001));
		
		for(MdpOptimizer<Coordinate, Action> optimizer: optimizers) {
			optimizer.optimize();
			System.out.println(optimizer);
		}
		
		try(FileWriter writer = new FileWriter("value.csv")) {
			writer.write(optimizers.get(0).dumpCSV());
		}
		
		try(FileWriter writer = new FileWriter("policy.csv")) {
			writer.write(optimizers.get(1).dumpCSV());
		}
	}

}
