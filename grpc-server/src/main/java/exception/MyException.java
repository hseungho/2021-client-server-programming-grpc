package exception;

public class MyException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public MyException(String errorMessage) {
		super(errorMessage);
	}
	
	public static class NullDataException extends MyException{
		private static final long serialVersionUID = 1L;
		public NullDataException(String errorMessage) {
			super(errorMessage);
		}
	}
	
	public static class DuplicationDataException extends MyException{
		private static final long serialVersionUID = 1L;
		public DuplicationDataException(String errorMessage) {
			super(errorMessage);
		}
	}
	
	public static class InvalidedDataException extends MyException {
		private static final long serialVersionUID = 1L;
		public InvalidedDataException(String errorMessage) {
			super(errorMessage);
		}
	}



	
}
