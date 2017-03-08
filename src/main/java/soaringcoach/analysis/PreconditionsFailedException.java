package soaringcoach.analysis;

@SuppressWarnings("serial")
public class PreconditionsFailedException extends AnalysisException {

	public PreconditionsFailedException(String message) {
		super(message);
	}

	public PreconditionsFailedException(String msg, Exception e) {
		super(msg, e);
	}

}
