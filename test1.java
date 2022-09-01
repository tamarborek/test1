import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebElement;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;

public class test1 {

    public static void main(String[] args) throws Exception {

        WebElement table;
        test1 test1 = new test1();
        //Read the csv file
        List<csvParams> params = test1.ReadCSVFile();

        //Set chrome driver
        System.setProperty("webdriver.chrome.driver","C:\\Users\\tamarb\\chromedriver_win32\\chromedriver.exe");
        WebDriver wd = new ChromeDriver();
       //open url
        String baseUrl = "http://www.w3schools.com/html/html_tables.asp";
        wd.get(baseUrl);
        //wait for web page to dwonload
        wd.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        table=wd.findElement(By.xpath("//*[@id=\"customers\"]"));

        //For each line in the csv file, verify that the expected return value exists
        for(csvParams line:params){
            Assert.assertTrue ("Bad value returned", verifyTableCellText(table,line.searchColumn,line.searchText,line.returnColumnText, line.expectedText));
        }
    }

    public static boolean verifyTableCellText(WebElement table, String searchColumn, String searchText, String returnColumnText, String expectedText) throws Exception {
        //Get the return value according to the given parameters
        String returnedVal = getTableCellTextByXpath(table, searchColumn, searchText, returnColumnText);

        //returning true if the expected text is received, and false if not
        return  (expectedText.contains(returnedVal));
    }

    public static String getTableCellTextByXpath(WebElement table, String searchColumn, String searchText, String returnColumnText) {
        List<WebElement> column = table.findElements(By.xpath("//tbody/tr[1]"));

        //Find the exact column to work on
        int _searchColumn = 0;
        try{
            _searchColumn = (Integer.parseInt(searchColumn));
        } catch(Exception e){
            List<WebElement> rows = table.findElements(By.xpath("tbody/tr/th"));
            for (WebElement element : rows) {
                if (element.getText().contains(searchColumn)) {
                    _searchColumn = rows.indexOf(element) + 1;
                }
            }
        }

        WebElement col = table.findElement(By.xpath("tbody/tr/td["+_searchColumn+"][.='" + searchText + "']"));
        WebElement parent = col.findElement(By.xpath("./.."));

        //Find the exact text
        int _returnColumnText = 0;
        try{
            _returnColumnText = (Integer.parseInt(returnColumnText));
        } catch(Exception e){
            List<WebElement> rows = table.findElements(By.xpath("tbody/tr/th"));
            for (WebElement element : rows) {
                if (element.getText().contains(returnColumnText)) {
                    _returnColumnText = rows.indexOf(element) + 1;
                }
            }
        }
        String element = parent.findElement(By.xpath("td[position()=" + _returnColumnText + "]")).getText();

        return element;
    }

    private List<csvParams> ReadCSVFile() throws FileNotFoundException {
       //Read the csv file to a list
        List<csvParams> list = new ArrayList<csvParams>();
        Scanner sc = new Scanner(new File("C:\\Users\\tamarb\\Documents\\customers.csv"));
        while(sc.hasNext()){
            String line = sc.nextLine();
            String[] params = line.split(",");
            csvParams _line = new csvParams();
            _line.searchColumn = (params[0]);
            _line.searchText = params[1];
            _line.returnColumnText = (params[2]);
            _line.expectedText = params[3];
            list.add(_line);
        }

        sc.close();  //closes the scanner

        return list;
    }
}




