package rafael.ordonez.revolut.controllers.transactions.validator;

import org.springframework.util.StringUtils;
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

        if(StringUtils.isEmpty(transferRequestBody.getSourceAccount())) {
            errors.rejectValue("sourceAccount", "sourceAccount.empty", "Source account number is null or empty");
        }
        if(StringUtils.isEmpty(transferRequestBody.getTargetAccount())) {
            errors.rejectValue("targetAccount", "targetAccount.empty", "Target account number is null or empty");
        }
        if(transferRequestBody.getAmount() < 0) {
            errors.rejectValue("amount", "amount.negative", "Amount can not be negative");
        }
        if(transferRequestBody.getSourceAccount() != null && transferRequestBody.getSourceAccount().equals(transferRequestBody.getTargetAccount())) {
            errors.rejectValue("sourceAccount", "sourceAccount.same", "Source and target account must be different");
        }
    }
}
