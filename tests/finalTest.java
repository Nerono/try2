package testProg.tests;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testProg.helpStuff.*;
import testProg.realPages.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class finalTest {
    static Logger l;
    public static void main(String[] args) {
        poMainPage mainPage;
        poDressSearch dressSearch;
        try(Logger l = new Logger()){

            l.startLog();
            l.logLine("Trying set property...");
            System.setProperty("webdriver.chrome.driver","D:\\chromedriver\\chromedriver.exe");
            l.logSucces();

            l.logLine("Trying create webdriver and call get()...");
            WebDriver driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.get("http://prestashop-automation.qatestlab.com.ua/ru/");
            l.logSucces();

            l.logLine("Creating poMainPage...");
            mainPage = new poMainPage(driver);
            l.logSucces();

            l.logLine("Testing currency list from the top bar. Perfomance of each item and the correspondence " +
                    "of currencies in the products...");
            mainPage.setNewCurrency(BasicPage.EUR);
            mainPage.compareCurrAndProdCurr(mainPage.getCurrentProducts(),mainPage.getCurrentCurrency(),l);
            l.logAndPrintLine("Next currency");
            mainPage.setNewCurrency(BasicPage.UAH);
            mainPage.compareCurrAndProdCurr(mainPage.getCurrentProducts(),mainPage.getCurrentCurrency(),l);
            l.logAndPrintLine("Next currency");
            mainPage.setNewCurrency(BasicPage.USD);
            mainPage.compareCurrAndProdCurr(mainPage.getCurrentProducts(),mainPage.getCurrentCurrency(),l);
            l.logSucces();

            l.logLine("Searching \"dress\" in the search line and checking the opened page(include searching \"Товаров:\")...");
            dressSearch = mainPage.dressSearch();
            l.logSucces();

            l.logLine("Comparing of the number in the \"Товаров:\" line with the number of displayed products...");
            dressSearch.compareTitleNumWithPage(l);
            l.logSucces();

            l.logLine("Checking is products currency is $...");
            dressSearch.compareCurrAndProdCurr(dressSearch.getCurrentProducts(),dressSearch.getCurrentCurrency(),l);
            l.logSucces();

            l.logLine("Change sorting to \"Price: from high to low\"...");
            dressSearch.setNewSorting(dressSearch.priceHToL);
            l.logSucces();

            (new WebDriverWait(driver, 10)).until(ExpectedConditions.
                    attributeToBe(dressSearch.itemPriceHToLLocator, "class", "select-list current js-search-link"));
            l.logAndPrintLine("Checking the result of a sort change...");
            dressSearch.checkSortingAccuracy(l);
            l.logSucces();

            l.logLine("Validation of data in the fields that form the price...");
            dressSearch.compareAllPrices(l);
            l.logSucces();


        }
        catch (NoSuchElementException e ){
            try {
                l = new Logger();
                l.logAndPrintLine("Forced closure(NoSuchElementException) cause:");
                l.logAndPrintLine(e.getMessage());
            }catch (Exception ex){ex.printStackTrace();}finally {
                try{l.finishWithoutLog();}catch (IOException exc){exc.printStackTrace();}
            }



        }catch (Exception e){e.printStackTrace();}
    }

}
