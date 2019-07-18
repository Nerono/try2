package testProg.realPages;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testProg.helpStuff.BasicPage;
import testProg.helpStuff.Logger;

import java.io.IOException;

public class poDressSearch extends testProg.helpStuff.BasicPage {
    public static final int priceHToL=0;
    By searchLocator = By.xpath("//div[@id='search_widget']//input[@type='text' and @name='s']");
    private int productNumber;

    static By  productPriceLocator = By.xpath("//section[@id='products']//div[@class='product-price-and-shipping']/span[@class = 'price']");
    public By productNumberLocator = By.xpath("//section[@id='main']/section[@id='products']/div/div/div/p");
    public By sortingDropDownLocator = By.xpath("//div[@class='col-md-6']//a[@class='select-title']");
    public By itemPriceHToLLocator = By.xpath("//div[@class='dropdown-menu']/a[contains(.,'от высокой')]");
    public By productLocator = By.xpath("//section[@id='products']//div[@class='product-price-and-shipping']");
    public By finalPriceLocator = By.xpath("span[@class='price']");
    public By regularPriceLocator = By.xpath("span[@class='regular-price']");
    public By discountPercentageLocator = By.xpath("span[@class='discount-percentage']");

    public poDressSearch(WebDriver driver){
        super(driver,productPriceLocator);
       if (!((driver.findElement(searchLocator).getAttribute("value")).equals("dress")) &&
                !driver.findElement(productNumberLocator).getText().contains("Товаров:")) {
            throw new IllegalStateException("This is not the \"dress\" search page");
        }
    }

    //Устанавливает сортировку от большей цены к меньшей
    public poDressSearch setNewSorting(int i){
        driver.findElement(sortingDropDownLocator).click();
        switch (i){
            case 0:
                (new WebDriverWait(driver,10)).until(ExpectedConditions.visibilityOfElementLocated(itemPriceHToLLocator)).click();
                break;
        }
        return this;
    }

    //Присваивает полю productNumber число товаров указанное в строке "Товаров: x"
    public poDressSearch setProductNumber(){
        productNumber = Integer.parseInt(driver.findElement(productNumberLocator).getText().substring("Товаров: ".length(),
                driver.findElement(productNumberLocator).getText().length()-1));
        return this;
    }

    //Сравнивает число товаров, отображаемых на странице с числом товаров указанным в "Товаров: x"
    public poDressSearch compareTitleNumWithPage(Logger l)throws IOException{
        setProductNumber();
        if(productNumber == getCurrentProducts().size()){
            l.logAndPrintLine("Количество товаров в заголовке \"Товаров: x.\" совпадает с числом товаров на странице");
        }else{
            l.logAndPrintLine("Количество товаров в заголовке \"Товаров: x.\" НЕ совпадает с числом товаров на странице");
            l.logAndPrintLine("Товаров на странице: "+getCurrentProducts().size()+
                    " Количество товаров указанное в заголовке: " + productNumber);
        }
        return this;
    }

    //Проверяет, размещены ли товары на странице согласно выбранной сортировке(цена по убыванию)
    public poDressSearch checkSortingAccuracy(Logger l) throws IOException{
        boolean checker = true;
        double currVal;
        double pastVal=0;
        int i = 1;
        for(WebElement element :driver.findElements(productLocator)){
            try {
                currVal = getPriceNumbers(element.findElement(regularPriceLocator));
            }catch (NoSuchElementException e){
                currVal = getPriceNumbers(element.findElement(finalPriceLocator));
            }
            if(currVal>pastVal && i!=1){
                checker = false;
                l.logAndPrintLine("Ошибка при размещении товаров согласно сортировке \n" +
                        "Ошибка на товаре #"+i+" "+currVal + " "+pastVal);
            }
            pastVal = currVal;
            i++;
        }
        if(checker){l.logAndPrintLine("Все товары размещены согласно указанной сортировке");}

        return this;
    }

    //Проверяет, что в поле показывающем скидку скидка указана в процентах и поля показывающие скидку, цену до скидки и
    //цену после скидки хранят значения типа double
    public poDressSearch checkDiscountFields (WebElement element,int i,Logger l) throws NoSuchElementException,IOException{
        try{
            if ('%' == getLastChar(element.findElement(discountPercentageLocator))){
                getPriceNumbers(element.findElement(discountPercentageLocator));
                getPriceNumbers(element.findElement(regularPriceLocator));
                getPriceNumbers(element.findElement(finalPriceLocator));
                l.logAndPrintLine("Товар #"+i+": поля для скидки, цены до и цены после есть, формат значений полей верный");
            }

        }catch (NoSuchElementException e){throw e;}
        catch (NumberFormatException e){l.logAndPrintLine("Неправильное значение в одном из полей товара #"+i);}

        return this;
    }

    //Проверяет совпадение скидки с ценами до и после нее. Также каждый продукт проверяется методом checkDiscountFields
    public poDressSearch compareAllPrices(Logger l) throws IOException{
        int i =1;
        for(WebElement element: driver.findElements(productLocator)){
            try{
                double myDiscPrice = (1-getPercentageFraction(element.findElement(discountPercentageLocator)))*
                        getPriceNumbers(element.findElement(regularPriceLocator));
                double discPrice = getPriceNumbers(element.findElement(finalPriceLocator));
                checkDiscountFields(element,i,l);
                if(Math.abs(myDiscPrice-discPrice)<0.01){
                    System.out.println("Товар #"+i+": цена до и после скидки совпадает с размером скидки");
                }else {
                    l.logAndPrintLine("Товар #"+i+": имеет скидку. Цена до и после скидки НЕ совпадает с размером скидки. Правильная цена: "
                            + myDiscPrice+" Указанная цена: "+discPrice); }
            }catch (NoSuchElementException e){l.logAndPrintLine("Товар #"+i+": НЕ имеет скидку.");}
            finally { i++;}
        }
        return this;
    }
}
