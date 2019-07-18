package testProg.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testProg.helpStuff.Logger;
import testProg.realPages.*;

import java.io.IOException;

public class TestingTask3 {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver","D:\\chromedriver\\chromedriver.exe");
        try {
            Logger l = new Logger();
            l.startLog();

            WebDriver driver = new ChromeDriver();
            driver.get("http://prestashop-automation.qatestlab.com.ua/ru/search?s=dress&SubmitCurrency=1&id_currency=3");
            poDressSearch dressSearch = new poDressSearch(driver);

            dressSearch.setNewSorting(dressSearch.priceHToL);

            (new WebDriverWait(driver, 10)).until(ExpectedConditions.
                    attributeToBe(dressSearch.itemPriceHToLLocator, "class", "select-list current js-search-link"));

            dressSearch.compareAllPrices(l);

        }catch (IOException e){}

    }
}
