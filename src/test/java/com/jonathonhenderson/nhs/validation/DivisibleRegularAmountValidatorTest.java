package com.jonathonhenderson.nhs.validation;

import com.jonathonhenderson.nhs.model.Frequency;
import com.jonathonhenderson.nhs.model.RegularAmount;
import com.jonathonhenderson.nhs.validation.annotation.DivisibleRegularAmount;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DivisibleRegularAmountValidatorTest {
    private static Validator validator;

    private static class Stub {
        @DivisibleRegularAmount
        private RegularAmount regularAmount;

        public Stub(String amount, Frequency frequency) {
            this.regularAmount = new RegularAmount();
            this.regularAmount.setAmount(amount);
            this.regularAmount.setFrequency(frequency);
        }
    }

    private static class StubWithCustomMessage {
        @DivisibleRegularAmount(message = "Uh oh, try again!")
        private RegularAmount regularAmount;

        public StubWithCustomMessage(String amount, Frequency frequency) {
            this.regularAmount = new RegularAmount();
            this.regularAmount.setAmount(amount);
            this.regularAmount.setFrequency(frequency);
        }
    }

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void oneWeekFrequencyShouldAlwaysPass() {
        this.assertValidationPasses(
                new Stub("0", Frequency.WEEK),
                new Stub("1", Frequency.WEEK),
                new Stub("2", Frequency.WEEK),
                new Stub("50", Frequency.WEEK),
                new Stub("100000", Frequency.WEEK),
                new Stub("1.11", Frequency.WEEK),
                new Stub("1.01", Frequency.WEEK),
                new Stub("100.99", Frequency.WEEK)
        );
    }

    @Test
    public void twoWeekFrequencyShouldOnlyPassForEvenAmounts() {
        this.assertValidationPasses(
                new Stub("0", Frequency.TWO_WEEK),
                new Stub("2", Frequency.TWO_WEEK),
                new Stub("3", Frequency.TWO_WEEK),
                new Stub("50", Frequency.TWO_WEEK),
                new Stub("100", Frequency.TWO_WEEK),
                new Stub("1.10", Frequency.TWO_WEEK),
                new Stub("1.00", Frequency.TWO_WEEK),
                new Stub("100.88", Frequency.TWO_WEEK),
                new Stub("123456789.98", Frequency.TWO_WEEK)
        );

        this.assertValidationFails(
                new Stub("0.01", Frequency.TWO_WEEK),
                new Stub("0.51", Frequency.TWO_WEEK),
                new Stub("3.59", Frequency.TWO_WEEK),
                new Stub("100.01", Frequency.TWO_WEEK),
                new Stub("1.11", Frequency.TWO_WEEK),
                new Stub("50000.23", Frequency.TWO_WEEK)
        );
    }

    @Test
    public void fourWeekFrequencyShouldOnlyPassWhenDivisibleByFour() {
        this.assertValidationPasses(
                new Stub("0", Frequency.FOUR_WEEK),
                new Stub("0.04", Frequency.FOUR_WEEK),
                new Stub("2", Frequency.FOUR_WEEK),
                new Stub("50", Frequency.FOUR_WEEK),
                new Stub("1.08", Frequency.FOUR_WEEK),
                new Stub("1.88", Frequency.FOUR_WEEK),
                new Stub("100.44", Frequency.FOUR_WEEK),
                new Stub("123456789.96", Frequency.FOUR_WEEK)
        );

        this.assertValidationFails(
                new Stub("0.01", Frequency.FOUR_WEEK),
                new Stub("0.02", Frequency.FOUR_WEEK),
                new Stub("0.03", Frequency.FOUR_WEEK),
                new Stub("0.05", Frequency.FOUR_WEEK),
                new Stub("0.41", Frequency.FOUR_WEEK),
                new Stub("3.59", Frequency.FOUR_WEEK),
                new Stub("100.01", Frequency.FOUR_WEEK),
                new Stub("1.11", Frequency.FOUR_WEEK),
                new Stub("50000.23", Frequency.FOUR_WEEK)
        );
    }

    @Test
    public void quarterFrequencyShouldOnlyPassWhenDivisibleByThirteen() {
        this.assertValidationPasses(
                new Stub("0", Frequency.QUARTER),
                new Stub("0.13", Frequency.QUARTER),
                new Stub("0.65", Frequency.QUARTER),
                new Stub("13", Frequency.QUARTER),
                new Stub("13.13", Frequency.QUARTER),
                new Stub("130.26", Frequency.QUARTER),
                new Stub("1.04", Frequency.QUARTER)
        );

        this.assertValidationFails(
                new Stub("0.01", Frequency.QUARTER),
                new Stub("0.05", Frequency.QUARTER),
                new Stub("0.12", Frequency.QUARTER),
                new Stub("0.14", Frequency.QUARTER),
                new Stub("0.25", Frequency.QUARTER),
                new Stub("12.99", Frequency.QUARTER),
                new Stub("13.01", Frequency.QUARTER)
        );
    }

    @Test
    public void yearFrequencyShouldOnlyPassWhenDivisibleByFiftyTwo() {
        this.assertValidationPasses(
                new Stub("0", Frequency.YEAR),
                new Stub("0.52", Frequency.YEAR),
                new Stub("52.00", Frequency.YEAR),
                new Stub("104.52", Frequency.YEAR),
                new Stub("105.04", Frequency.YEAR),
                new Stub("520.52", Frequency.YEAR)
        );

        this.assertValidationFails(
                new Stub("0.01", Frequency.YEAR),
                new Stub("0.13", Frequency.YEAR),
                new Stub("0.26", Frequency.YEAR),
                new Stub("0.39", Frequency.YEAR),
                new Stub("0.51", Frequency.YEAR),
                new Stub("0.53", Frequency.YEAR),
                new Stub("52.01", Frequency.YEAR),
                new Stub("5200.01", Frequency.YEAR)
        );
    }

    @Test
    public void monthFrequencyShouldAlwaysPassRegardlessOfValue() {
        this.assertValidationPasses(
                new Stub("0", Frequency.MONTH),
                new Stub("1", Frequency.MONTH),
                new Stub("2", Frequency.MONTH),
                new Stub("50", Frequency.MONTH),
                new Stub("100000", Frequency.MONTH),
                new Stub("1.01", Frequency.MONTH),
                new Stub("1.02", Frequency.MONTH),
                new Stub("1.03", Frequency.MONTH),
                new Stub("1.04", Frequency.MONTH),
                new Stub("1.05", Frequency.MONTH),
                new Stub("1.01", Frequency.MONTH),
                new Stub("100.99", Frequency.MONTH)
        );
    }

    @Test
    public void customValidationMessagesAreUsedWhenSpecified() {
        StubWithCustomMessage stub = new StubWithCustomMessage("1.01", Frequency.TWO_WEEK);
        Set<ConstraintViolation<StubWithCustomMessage>> violations = validator.validate(stub);
        assertEquals(1, violations.size());
        assertEquals("Uh oh, try again!", violations.iterator().next().getMessage());
    }

    private void assertValidationPasses(Stub... tests) {
        for (Stub test : tests) {
            assertEquals(0, validator.validate(test).size());
        }
    }

    private void assertValidationFails(Stub... tests) {
        for (Stub test : tests) {
            Set<ConstraintViolation<Stub>> violations = validator.validate(test);
            assertNotEquals(0, violations.size());
            assertEquals("Invalid amount", violations.iterator().next().getMessage());
        }
    }
}
