public class cwe {
		public void fun() {
		Long count = 0L;
		for (long i = 0; i < Integer.MAX_VALUE; i++) {
				count += i;
		}
	}
}
