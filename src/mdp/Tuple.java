package mdp;

/**
 * Tuple is a model class for storing a tuple value
 * 
 * @author Ying Hao
 *
 * @param <K>
 * @param <V>
 */
public class Tuple<K, V> {
	public final K first;
	public final V second;
	
	public Tuple(K first, V second) {
		this.first = first;
		this.second = second;
	}
	
}
