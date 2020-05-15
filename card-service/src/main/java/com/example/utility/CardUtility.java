package com.example.utility;

import com.example.model.CardStatus;
import com.example.model.CreditCardPayment;

public class CardUtility {

    public static double calculateCashWithdrawLimit(double creditCardLimit, double withdrawPercentage) {
        if (creditCardLimit <= 0) return 0;
        return creditCardLimit * withdrawPercentage / 100;
    }

    public static double getTotalAmountSpent(CreditCardPayment creditCardPayment) {
        return creditCardPayment.getMaxLimit() - creditCardPayment.getCreditLimit();
    }

    public static boolean isPaymentMadeForThisMonth(CreditCardPayment creditCardPayment) {
        return creditCardPayment.getPaymentReceivedDate() != null;
    }

    public static double calculateMinimumDueAmount(CreditCardPayment creditCardPayment) {
        double totalAmountSpent = getTotalAmountSpent(creditCardPayment);
        if (totalAmountSpent < 100) // if less than 100$, the full amount is min due
            return totalAmountSpent;
        double minCalcPercentAmount = totalAmountSpent * creditCardPayment.getMinDueCalcPercent() / 100;
        return minCalcPercentAmount < creditCardPayment.getMinDueCalcPercent() ? creditCardPayment.getMinDueCalcPercent() : minCalcPercentAmount; // 100 or min %age whichever is more
    }

    public static boolean isLatePaymentMade(CreditCardPayment creditCardPayment) {

        if (!isPaymentMadeForThisMonth(creditCardPayment)) { // if no payment made, then it's late payment
            return true;
        }

        if (creditCardPayment.getPaymentDueDate().before(creditCardPayment.getPaymentReceivedDate())) { // if due date is before
            // actual payment date, then
            // late payment
            return true;
        }

        if (creditCardPayment.getAmount() < calculateMinimumDueAmount(creditCardPayment)) { // if payment made
            // less than min due
            // amount
            return true;
        }

        return false;
    }

    public static double getLatePaymentCharge(CreditCardPayment creditCardPayment) {
        if (isLatePaymentMade(creditCardPayment) && CardStatus.DELINQUENT.equals(creditCardPayment.getStatus())) {
            return creditCardPayment.getLatePaymentCharge();
        } else {
            return 0;
        }
    }

    public static double getTotalCashWithdrawn(CreditCardPayment creditCardPayment) {
        double maxCashLimit = calculateCashWithdrawLimit(creditCardPayment.getMaxLimit(), creditCardPayment.getAllowedCashWithdrawal());
        return maxCashLimit - creditCardPayment.getCashLimit();
    }

    public static double getInterest(CreditCardPayment creditCardPayment, float interestRate) {
        //CreditCardDetails cardDetails = this.lastMonthCardDetails;
        return (getTotalAmountSpent(creditCardPayment) - creditCardPayment.getAmount()) * interestRate / 100;
    }
}
