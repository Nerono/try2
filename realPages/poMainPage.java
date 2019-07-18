package testProg.realPages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import testProg.helpStuff.BasicPage;

public class poMainPage extends BasicPage{
    private String quote = "dress";

    String mainPageUrl = "http://prestashop-automation.qatestlab.com.ua/ru/";

    static By productPriceLocator = By.xpath("//section[@class='featured-products clearfix']//div[@class='product-price-and-shipping']/span[@class = 'price']");


    public poMainPage(WebDriver driver){
        super(driver,productPriceLocator);
        if (!driver.getCurrentUrl().equals(mainPageUrl)) {
            throw new IllegalStateException("This is not the main page");
        }

    }

    //Осуществляет поиск слова "dress" в поисковом окне.
    public poDressSearch dressSearch(){
        driver.findElement(searchLocator).sendKeys(quote);
        driver.findElement(searchLocator).submit();
        return new poDressSearch(driver);
    }


}
