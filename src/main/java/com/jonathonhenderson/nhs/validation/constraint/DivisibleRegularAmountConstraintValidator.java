package com.jonathonhenderson.nhs.validation.constraint;

import com.jonathonhenderson.nhs.model.RegularAmount;
import com.jonathonhenderson.nhs.validation.annotation.DivisibleRegularAmount;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DivisibleRegularAmountConstraintValidator
        implements ConstraintValidator<DivisibleRegularAmount, RegularAmount> {
    /**
     * Determine if the given RegularAmount object is valid. The amount is extracted from the RegularAmount object and
     * converted into an integer representing its smallest unit. In the case of GBP, this will be the number of pennies
     * for example a value of "1.23" will return an integer value of 123. The divisor (number of weeks in that
     * Frequency) is extracted from the Frequency enum and the modulus operator is used to determine if our smallest
     * unit amount is divisible by the divisor. True is returned if it is divisible and false is returned if it is not.
     * If the divisor is less than 1, we want to avoid a division by 0 so we allow validation to pass. The MONTH
     * Frequency is set to 0 as it cannot be directly divided into a number of weeks since a month is of variable
     * length.
     *
     * @param regularAmount The RegularAmount object to be validated.
     * @param constraintValidatorContext The constraint validation context.
     * @return True if the RegularAmount is valid, false otherwise.
     */
    public boolean isValid(RegularAmount regularAmount, ConstraintValidatorContext constraintValidatorContext) {
        int divisor = this.getDivisor(regularAmount);
        if (divisor < 1) {
            return true;
        }

        int pence = this.getAmountInSmallestUnit(regularAmount);

        return pence % divisor == 0;
    }

    /**
     * Extract the string based amount from the given RegularAmount object and convert it into its smallest unit of
     * currency. When dealing with currency, it is better to preserve accuracy and work with whole numbers than to deal
     * with precision loss that results from floating points. Assumes the amount of the RegularAmount instance is a
     * string representing either a whole number or uses a dot (.) to separate units.
     *
     * @param amount The RegularAmount instance.
     * @return The amount of currency as an integer in its smallest unit (Pence for GBP, Cents in EUR or USD etc.)
     */
    private int getAmountInSmallestUnit(RegularAmount amount) {
        String[] parts = amount.getAmount().split("\\.");
        int pounds = Integer.parseInt(parts[0]);
        int pence = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;

        return (pounds * 100) + pence;
    }

    /**
     * Get the divisor i.e. the number of weeks in the Frequency fo the given RegularAmount.
     *
     * @param amount The RegularAmount object.
     * @return An integer representing the number of weeks in that Frequency, if appropriate.
     */
    private int getDivisor(RegularAmount amount) {
        return amount.getFrequency().getWeeks();
    }
}
