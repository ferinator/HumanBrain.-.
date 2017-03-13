import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class SeleniumExecute
{
    public File getChromeDriverExe()
    {
        Thread.currentThread().getContextClassLoader().getResourceAsStream("chromedriver.exe");
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("chromedriver.exe").getFile());
        return file;
    }

    public static void main(String[] args)
            throws IOException, URISyntaxException
    {
        new SeleniumExecute.ButtonSample();
    }

    public static void runMemory()
            throws IOException, URISyntaxException
    {
        URI uri = getJarURI();
        URI exe = getFile(uri, "chromedriver.exe");

        new SeleniumExecute.ButtonSample();

        SeleniumExecute seleniumExecute = new SeleniumExecute();
        File directory = seleniumExecute.getChromeDriverExe();
        System.setProperty("webdriver.chrome.driver", exe.getPath());
        WebDriver driver = (WebDriver) new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 500L);

        driver.get("http://www.humanbenchmark.com/tests/memory");
        try
        {
            Thread.sleep(4000L);
        }
        catch (InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
        driver.findElement(By.cssSelector(".pulse-faint.test-hero-badge")).click();
        wait.until(visibilityOfElementLocated(By.cssSelector(".square.ng-scope.active")));
        int levelCounter = 1;
        for (;;)
        {
            List<WebElement> squareList = new ArrayList();
            Map<Integer, Boolean> position = new HashMap();
            Integer counter = Integer.valueOf(0);
            WebElement square;
            Integer localInteger1;
            Integer localInteger2;
            for (Iterator localIterator = driver.findElements(By.cssSelector(".square.ng-scope")).iterator(); localIterator.hasNext(); localInteger2 = counter = Integer.valueOf(counter.intValue() + 1))
            {
                square = (WebElement)localIterator.next();
                if (square.getAttribute("class").contains("active")) {
                    position.put(counter, Boolean.valueOf(true));
                } else {
                    position.put(counter, Boolean.valueOf(false));
                }
                localInteger1 = counter;
            }
            try
            {
                Thread.sleep(2000L);
            }
            catch (InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
            Integer derCounter = Integer.valueOf(0);
            for (WebElement square : driver.findElements(By.cssSelector(".square.ng-scope"))) {
                try
                {
                    if (((Boolean)position.get(derCounter)).booleanValue()) {
                        square.click();
                    }
                    localInteger2 = derCounter;Integer localInteger3 = derCounter = Integer.valueOf(derCounter.intValue() + 1);
                }
                catch (Exception localException1) {}
            }
            boolean gwag = true;
            while (gwag)
            {
                String levelString = driver.findElement(By.cssSelector(".hud > .score:first-child")).getText();
                if (levelString.contains(Integer.toString(levelCounter))) {
                    break;
                }
                levelCounter++;
            }
        }
    }

    private static URI getJarURI()
            throws URISyntaxException
    {
        ProtectionDomain domain = SeleniumExecute.class.getProtectionDomain();
        CodeSource source = domain.getCodeSource();
        URL url = source.getLocation();
        URI uri = url.toURI();

        return uri;
    }

    private static URI getFile(URI where, String fileName)
            throws ZipException, IOException
    {
        File location = new File(where);
        URI fileURI;
        if (location.isDirectory())
        {
            fileURI = URI.create(where.toString() + fileName);
        }
        else
        {
            ZipFile zipFile = new ZipFile(location);
            try
            {
                fileURI = extract(zipFile, fileName);
            }
            finally
            {
                zipFile.close();
            }
        }
        return fileURI;
    }

    private static URI extract(ZipFile zipFile, String fileName)
            throws IOException
    {
        File tempFile = File.createTempFile(fileName, Long.toString(System.currentTimeMillis()));
        tempFile.deleteOnExit();
        ZipEntry entry = zipFile.getEntry(fileName);
        if (entry == null) {
            throw new FileNotFoundException("cannot find file: " + fileName + " in archive: " + zipFile.getName());
        }
        InputStream zipStream = zipFile.getInputStream(entry);
        OutputStream fileStream = null;
        try
        {
            fileStream = new FileOutputStream(tempFile);
            byte[] buf = new byte['?'];
            int i = 0;
            while ((i = zipStream.read(buf)) != -1) {
                fileStream.write(buf, 0, i);
            }
        }
        finally
        {
            close(zipStream);
            close(fileStream);
        }
        return tempFile.toURI();
    }

    private static void close(Closeable stream)
    {
        if (stream != null) {
            try
            {
                stream.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public static class ButtonSample
            extends JFrame
            implements ActionListener
    {
        public ButtonSample()
        {
            setLayout(null);
            setDefaultCloseOperation(3);
            setSize(500, 500);
            setLocation(100, 100);

            JButton button1 = new JButton("Memory");
            button1.setBounds(20, 20, 100, 30);
            button1.addActionListener(this);
            add(button1);

            JButton button2 = new JButton("Words");
            button2.addActionListener(this);
            button2.setBounds(20, 60, 100, 30);
            add(button2);

            JButton button3 = new JButton("Numbers");
            button3.addActionListener(this);
            button3.setBounds(20, 100, 100, 30);
            add(button3);

            setVisible(true);
        }

        public void actionPerformed(ActionEvent e)
        {
            try
            {
                if (e.getActionCommand().equalsIgnoreCase("memory")) {
                    SeleniumExecute.runMemory();
                } else if (e.getActionCommand().equalsIgnoreCase("words")) {
                    runWords();
                } else if (e.getActionCommand().equalsIgnoreCase("Numbers")) {
                    runNumbers();
                }
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
            catch (URISyntaxException e1)
            {
                e1.printStackTrace();
            }
        }

        private void runNumbers()
                throws URISyntaxException, IOException
        {
            URI uri = SeleniumExecute.access$000();
            URI exe = SeleniumExecute.getFile(uri, "chromedriver.exe");

            new ButtonSample();

            SeleniumExecute seleniumExecute = new SeleniumExecute();
            File directory = seleniumExecute.getChromeDriverExe();
            System.setProperty("webdriver.chrome.driver", exe.getPath());
            WebDriver driver = new ChromeDriver();
            WebDriverWait wait = new WebDriverWait(driver, 500L);
            driver.get("http://www.humanbenchmark.com/tests/number-memory");
            driver.findElement(By.cssSelector(".button.start")).click();
            for (;;)
            {
                wait.until(visibilityOfElementLocated(By.cssSelector("div.big-number.ng-binding")));
                String number = driver.findElement(By.cssSelector("div.big-number.ng-binding")).getText();
                wait.until(visibilityOfElementLocated(By.cssSelector("input")));
                driver.findElement(By.cssSelector("input")).sendKeys(new CharSequence[] { number });
                driver.findElement(By.cssSelector("input")).sendKeys(new CharSequence[] { Keys.ENTER });
                wait.until(visibilityOfElementLocated(By.cssSelector(".button.next-question")));
                driver.findElement(By.cssSelector(".button.next-question")).click();
            }
        }

        private void runWords()
                throws IOException, URISyntaxException
        {
            URI uri = SeleniumExecute.access$000();
            URI exe = SeleniumExecute.getFile(uri, "chromedriver.exe");

            new ButtonSample();

            SeleniumExecute seleniumExecute = new SeleniumExecute();
            File directory = seleniumExecute.getChromeDriverExe();
            System.setProperty("webdriver.chrome.driver", exe.getPath());
            WebDriver driver = new ChromeDriver();
            WebDriverWait wait = new WebDriverWait(driver, 500L);
            driver.get("http://www.humanbenchmark.com/tests/verbal-memory");
            boolean wordExists = false;
            boolean gwag = true;

            wait.until(visibilityOfElementLocated(By.cssSelector(".button.start")));
            driver.findElement(By.cssSelector(".button.start")).click();
            List<String> wordList = new ArrayList();
            wordList.add("retnugioerngho�iergionrtgbeprtgiougerntg�erotigner�");
            wait.until(visibilityOfElementLocated(By.cssSelector(".word.ng-scope span")));
            WebElement buttonSeen = driver.findElement(By.cssSelector(".button[ng-click='test.onAnswer(true)'"));
            WebElement buttonNew = driver.findElement(By.cssSelector(".button[ng-click='test.onAnswer(false)'"));
            for (;;)
            {
                String foundWord = driver.findElement(By.cssSelector(".word.ng-scope span")).getText();
                while (gwag)
                {
                    for (String word : wordList)
                    {
                        if (foundWord.equals(word))
                        {
                            wordExists = true;
                            break;
                        }
                        wordExists = false;
                    }
                    wordList.add(driver.findElement(By.cssSelector(".word.ng-scope span")).getText());
                    gwag = false;
                }
                gwag = true;
                if (wordExists)
                {
                    buttonSeen.click();
                    wait.until(visibilityOfElementLocated(By.cssSelector(".word.ng-scope span")));
                }
                else
                {
                    buttonNew.click();
                    wait.until(visibilityOfElementLocated(By.cssSelector(".word.ng-scope span")));
                }
            }
        }

        public void myMethod()
        {
            JOptionPane.showMessageDialog(this, "Hello, World!!!!!");
        }
    }
}
