
public class RandomNumber {

	public String generate(int size) {
		String sNum = "";
		for (int i = 0; i < size; i++) {
			sNum += genOneRandom();
		}
		return sNum;
	}
	
	public int genOneRandom() {
		return genOneRandom(0, 9);
	}
	
	public int genOneRandom(int min, int max) {
		int range = max - min + 1;
		return (int)(Math.random() * range) + min;
	}
	
	
	public static void main(String[] args) {
		RandomNumber rn = new RandomNumber();
		
		for (int i = 0; i < 4; i++) {
			System.out.println(rn.genOneRandom(5, 9) + "" + rn.generate(3));
		}
		for (int i = 0; i < 6; i++) {
			System.out.println(rn.genOneRandom(1, 3) + "" + rn.generate(4));
		}
	}
}
