package testProg.helpStuff;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.List;

//Шаблон для базовой страницы, которая отображает список продуктов и имеет верхнюю панель для выбора валюты, языка и тд
//
//
public class BasicPage {

    protected WebDriver driver;

    public static final int USD = 0;
    public static final int UAH = 1;
    public static final int EUR = 2;


    public By productPriceLocator;
    public By searchLocator = By.xpath("//div[@id='search_widget']//input[@type='text' and @name='s']");
    public By currentCurrencyLocator = By.xpath("//div[@id='_desktop_currency_selector']//span[@class='expand-more _gray-darker hidden-sm-down']");
    public By itemUSDLocator =By.xpath("//ul[@class='dropdown-menu hidden-sm-down']/li/a[@title='Доллар США']");
    public By itemUAHLocator =By.xpath("//ul[@class='dropdown-menu hidden-sm-down']/li/a[@title='Украинская гривна']");
    public By itemEURLocator = By.xpath("//ul[@class='dropdown-menu hidden-sm-down']/li/a[@title='Евро']");

    public BasicPage(WebDriver driver, By productPriceLocator){
        this.driver = driver;
        this.productPriceLocator = productPriceLocator;
    }

    //Возвращает список WebElement содержащих текущую цену для каждого товара на странице
    public List<WebElement> getCurrentProducts(){
        return driver.findElements(productPriceLocator);
    }

    //Возвращает последний знак из текстового поля(процент, валюта)
    public char getLastChar(WebElement element){
        return element.getText().charAt(element.getText().length()-1);
    }

    //Возвращает знак соответсвующий текущей валюте, выбранной в верхней панели
    public char getCurrentCurrency(){
        return getLastChar(driver.findElement(currentCurrencyLocator));
    }

    //Переводит текстовое значение скидки //span[@class='discount-percentage'] в double дробь <1 удобную для вычислений
    public double getPercentageFraction(WebElement element){
        return Double.parseDouble(element.getText().substring(1,element.getText().length()-1).replace(',','.'))/100;
    }

    //Переводит текстовое значение стоимости в double
    public double getPriceNumbers(WebElement element){
        return Double.parseDouble(element.getText().substring(0,element.getText().length()-1).replace(',','.'));

    }

    //Сравнивает валюту товаров в переданном списке с текущей валютой, выбранной в верхней панели
    public void compareCurrAndProdCurr(List<WebElement> currentProducts,char currentCurrency,Logger l) throws IOException{
        (new WebDriverWait(driver,10)).until(ExpectedConditions.presenceOfElementLocated(productPriceLocator));
        int i = 1;
        for(WebElement element : currentProducts){
            if(getLastChar(element) == currentCurrency){
                l.logAndPrintLine("Валюта продукта #"+i+" совпадает с выбранной валютой - "+currentCurrency);
            }else{
                l.logAndPrintLine("Валюта продукта #"+i+" НЕ совпадает с выбранной валютой - "+currentCurrency);
            }
            i++;
        }
    }

    //Выбирает новую валюту в верхней панели
    public BasicPage setNewCurrency(int a) throws NoSuchElementException{
        driver.findElement(currentCurrencyLocator).click();

        switch (a){
            case BasicPage.USD:
                driver.findElement(currentCurrencyLocator);
                (new WebDriverWait(driver,10)).until(ExpectedConditions.visibilityOfElementLocated(itemUSDLocator)).click();
                break;
            case BasicPage.UAH:
                driver.findElement(currentCurrencyLocator);
                (new WebDriverWait(driver,10)).until(ExpectedConditions.visibilityOfElementLocated(itemUAHLocator)).click();
                break;
            case BasicPage.EUR:
                driver.findElement(currentCurrencyLocator);
                (new WebDriverWait(driver,10)).until(ExpectedConditions.visibilityOfElementLocated(itemEURLocator)).click();
                break;
        }
        return this;
    }

}
