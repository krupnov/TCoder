public class ShrooksOnTheBoard {

	private static final int mod = 100003;

	public int count(int K, int H, int W) {
	
		int result = f(K, W) + 1;
		int power = 1;
		for (int i = 0 ; i < H ; ++i) {
			power = (power * result) % mod;
		
		}
		
		power = (power - 1) % mod;
		
		return power;
	}
	
	private int f(int K, int W) {
		if (W <= 0) {
			return 0;
		}
		
		int result = 0;
		for (int i = 0 ; i < W ; ++i) {
			result = (result + 1 + f(K, W - K - 1 - i)) % mod;
		}
		return result;
	}
	
	public static void main(String[] args) {
		ShrooksOnTheBoard board = new ShrooksOnTheBoard();
		System.out.println(board.count(3,4,9));
	}
}
