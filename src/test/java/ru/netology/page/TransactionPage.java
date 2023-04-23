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

    private void fieldClearing() {
        amountField.sendKeys(Keys.CONTROL + "a");
        amountField.sendKeys(Keys.DELETE);
        fromField.sendKeys(Keys.CONTROL + "a");
        fromField.sendKeys(Keys.DELETE);
    }


    public CardBalance validTransfer(String amountTransfer, DataHelper.CardInfo cardInfo) {
        amountField.setValue(amountTransfer);
        fromField.setValue(cardInfo.getCardNumber());
        uploadButton.click();
        return new CardBalance();
    }

    public void findErrorMessage (String expectedText) {
        errorMessage.shouldHave(exactText(expectedText), Duration.ofSeconds(20)).shouldBe(visible);
    }

}
