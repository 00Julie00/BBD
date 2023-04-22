package ru.netology.test;
import lombok.val;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;
import static com.codeborne.selenide.Selenide.open;


public class MoneyTransferTest {

    @Test
    void shouldTransferFromFirstToSecond() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val cardBalance = verificationPage.validVerify(verificationCode);
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = cardBalance.getCardBalance(firstCardInfo);
        var secondCardBalance = cardBalance.getCardBalance(secondCardInfo);
        var amount = generateValidAmount(firstCardBalance);
        var expectedBalanceFirstCard = firstCardBalance - amount;
        var expectedBalanceSecondCard = secondCardBalance + amount;
        var transactionPage = cardBalance.selectCardToTransfer(secondCardInfo);
        transactionPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);
        var actualBalanceFirstCard = cardBalance.getCardBalance(firstCardInfo);
        var actualBalanceSecondCard = cardBalance.getCardBalance(secondCardInfo);
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

    @Test
    void shouldGetErrorMessageIfAmountMoreBalance() {
        var loginPage = Selenide.open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var cardBalance = verificationPage.validVerify(verificationCode);
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = cardBalance.getCardBalance(firstCardInfo);
        var secondCardBalance = cardBalance.getCardBalance(secondCardInfo);
        var amount = generateInvalidAmount(secondCardBalance);
        var transactionPage = cardBalance.selectCardToTransfer(firstCardInfo);
        transactionPage.makeTransfer(String.valueOf(amount), secondCardInfo);
        transactionPage.findErrorMessage("Выполнена попытка перевода суммы,превышающей баланс карты");
        var actualBalanceFirstCard = cardBalance.getCardBalance(firstCardInfo);
        var actualBalanceSecondCard = cardBalance.getCardBalance(secondCardInfo);
        assertEquals(firstCardBalance, actualBalanceFirstCard);
        assertEquals(secondCardBalance, actualBalanceSecondCard);
    }
    @Test
    void shouldFailToAuthorizeWithInvalidAuthData() {
        val loginPage = new LoginPage();
        val badAuthInfo = DataHelper.getOtherAuthInfo(DataHelper.getAuthInfo());
        loginPage.invalidLogin(badAuthInfo);
    }

    @Test
    void shouldFailToAuthorizeWithInvalidVerificationCode() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val badVerificationCode = DataHelper.getOtherVerificationCodeFor(authInfo);
        verificationPage.invalidVerify(badVerificationCode);
    }
}
