abstract class AbstractAssessmentFlow {
    public final void executeAssessment() {
        validate();
        prepare();
        evaluate();
        publishResult();
    }

    protected void validate() {
        System.out.println("Validation completed");
    }

    protected void prepare() {
        System.out.println("Preparation completed");
    }

    protected abstract void evaluate();
    protected void publishResult() {
        System.out.println("Result published");
    }
}

interface AutoAssessment {
    default void evaluate() {
        System.out.println("Auto evaluation logic executed");
    }
}

interface ManualAssessment {
    default void evaluate() {
        System.out.println("Manual evaluation logic executed");
    }
}

public class UnifiedAssessmentExecutor extends AbstractAssessmentFlow
        implements AutoAssessment, ManualAssessment {
    private boolean isProctored;
    public UnifiedAssessmentExecutor(boolean isProctored) {
        this.isProctored = isProctored;
    }

    @Override
    protected void evaluate() {
        if (isProctored) {
            ManualAssessment.super.evaluate();
        } else {
            AutoAssessment.super.evaluate();
        }}}
