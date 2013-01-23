public class Pair<S, T> {
	
	public S first;
	public T second;
	
	protected Pair(S first, T second) {
		this.first = first;
		this.second = second;
	}
	
	public static <S, T> Pair<S, T> of(S first, T second) {
		return new Pair<S, T>(first, second);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Pair) {
			Pair<?, ?> otherPair = (Pair<?, ?>) other;
			return first.equals(otherPair.first) && second.equals(otherPair.second);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return first.hashCode() * 1337 + second.hashCode(); 
	}
}
