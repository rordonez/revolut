package rafael.ordonez.revolut.controllers.transactions.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import rafael.ordonez.revolut.controllers.transactions.beans.TransferRequestBody;

/**
 * Created by rafa on 28/9/15.
 */
public class TransferRequestBodyValidator implements Validator {
    public TransferRequestBodyValidator() {
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return TransferRequestBody.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TransferRequestBody transferRequestBody = (TransferRequestBody) target;
        if(transferRequestBody.getSourceAccount().equals(transferRequestBody.getTargetAccount())) {
            errors.rejectValue("sourceAccount", "sourceAccount.same");
        }
    }
}
