package gps.base;

import gps.base.DTO.GymDTO;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class KakaoGymCrawler {

    public List<GymDTO> crawlAllGyms() {
        // Selenium WebDriver 경로 설정
        System.setProperty("webdriver.chrome.driver", "C:/chromedriver-win64/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        WebDriver driver = new ChromeDriver(options);
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        List<GymDTO> gyms = new ArrayList<>();
        try {
            // 카카오맵 URL로 이동
            driver.get("https://map.kakao.com/");

            // 검색창에 "헬스장" 입력 후 검색
            WebElement searchBox = driver.findElement(By.id("search.keyword.query"));
            searchBox.sendKeys("헬스장");
            WebElement searchButton = driver.findElement(By.id("search.keyword.submit"));
            searchButton.click();

            // dimmedLayer 대기 및 숨기기
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dimmedLayer")));
            jsExecutor.executeScript("document.getElementById('dimmedLayer').style.display='none';");

            // 페이지네이션을 통해 모든 페이지를 순회하며 헬스장 정보 수집
            boolean hasNextPage = true;
            while (hasNextPage) {
                // 현재 페이지의 헬스장 정보 수집
                List<WebElement> gymElements = driver.findElements(By.xpath("//li[contains(@class, 'PlaceItem')]"));
                for (WebElement gymElement : gymElements) {
                    try {
                        String name = gymElement.findElement(By.xpath(".//a[contains(@class, 'link_name')]")).getText();
                        String address = gymElement.findElement(By.xpath(".//p[contains(@class, 'addr')]")).getText();
                        String longitude = gymElement.getAttribute("data-longitude");
                        String latitude = gymElement.getAttribute("data-latitude");

                        GymDTO gym = new GymDTO();
                        gym.setGName(name);
                        gym.setAddress(address);
                        gym.setGLongitude(Double.parseDouble(longitude));
                        gym.setGLatitude(Double.parseDouble(latitude));
                        gyms.add(gym);
                    } catch (Exception e) {
                        System.out.println("헬스장 데이터 추출 중 오류 발생: " + e.getMessage());
                    }
                }

                // 다음 페이지 버튼 클릭 (다음 페이지가 없으면 종료)
                try {
                    WebElement nextPageButton = driver.findElement(By.className("next"));
                    if (nextPageButton.getAttribute("aria-disabled") != null && nextPageButton.getAttribute("aria-disabled").equals("true")) {
                        hasNextPage = false;
                    } else {
                        jsExecutor.executeScript("arguments[0].click();", nextPageButton);
                        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("dimmedLayer")));
                    }
                } catch (Exception e) {
                    hasNextPage = false;
                    System.out.println("다음 페이지로 이동하는 동안 오류 발생: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        return gyms;
    }
}
