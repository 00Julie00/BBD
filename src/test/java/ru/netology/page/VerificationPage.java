package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;
import static java.time.Duration.ofSeconds;

public class VerificationPage {
    private SelenideElement codeField = $("[data-test-id=code] input");
    private SelenideElement verifyButton = $("[data-test-id=action-verify]");
    private SelenideElement errorBox = $("[data-test-id=error-notification]");

    public VerificationPage() {
        codeField.shouldBe(visible);
    }

    public CardBalance validVerify(DataHelper.VerificationCode verificationCode) {
        codeField.setValue(verificationCode.getCode());
        verifyButton.click();
        return new CardBalance();
    }

    public void invalidVerify(DataHelper.VerificationCode verificationCode) {
        codeField.setValue(verificationCode.getCode());
        sleep(15000);
        verifyButton.click();
        errorBox.shouldBe(visible, ofSeconds(15));
        $("[data-test-id=error-notification]>.notification__title")
                .shouldHave(text("Ошибка"));
        $("[data-test-id=error-notification]>.notification__content")
                .shouldHave(text("Ошибка! \nНеверно указан код! Попробуйте ещё раз."));
    }
}