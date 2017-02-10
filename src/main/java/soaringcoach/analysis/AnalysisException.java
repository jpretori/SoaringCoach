package soaringcoach.analysis;

public class AnalysisException extends Exception {

	public AnalysisException(String message) {
		super(message);
	}

	public AnalysisException(String msg, Exception e) {
		super(msg, e);
	}

}
