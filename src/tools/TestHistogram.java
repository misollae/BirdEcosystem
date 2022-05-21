package tools;

public class TestHistogram {
	private static int nclasses = 5;
	private static int nsamples = 100;
	
	public static void main(String[] args) {
		int[] data = new int[nsamples];
		for (int i = 0 ; i < nsamples ; i++) {
			data[i] = (int) (nclasses*Math.random());
		}
		Histogram h = new Histogram(data, nclasses);
		int[] d = h.getDistribution();
		for (int i = 0 ; i < nclasses ; i++) {
			System.out.println(d[i]);
		}
		System.out.println("");
		System.out.println(h.getMode(0));
	}

}
