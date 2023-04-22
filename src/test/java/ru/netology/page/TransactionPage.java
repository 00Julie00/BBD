package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.back;
import static java.time.Duration.ofSeconds;

public class TransactionPage {
    private SelenideElement addMoneyHeading = $(withText("Пополнение карты"));
    private SelenideElement amountField = $("[data-test-id='amount'] [type='text']");
    private SelenideElement fromField = $("[data-test-id='from'] [type='tel']");
    private SelenideElement uploadButton = $("[data-test-id='action-transfer']");
    private SelenideElement errorMessage = $("[data-test-id=error-notification]");

    public TransactionPage() {
        addMoneyHeading.shouldBe(Condition.visible, ofSeconds(10));
    }

    public CardBalance makeValidTransfer(String amountToTransfer, DataHelper.CardInfo cardInfo) {
        makeTransfer(amountToTransfer, cardInfo);
        return new CardBalance();
    }

    public void makeTransfer(String amountToTransfer, DataHelper.CardInfo cardInfo) {
        amountField.setValue(amountToTransfer);
        fromField.setValue(cardInfo.getCardNumber());
        uploadButton.click();
    }

    public void findErrorMessage(String expectedText) {
        errorMessage.shouldHave(exactText(expectedText), Duration.ofSeconds(15)).shouldBe(visible);
    }
}
