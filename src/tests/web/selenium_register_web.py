from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import Select
from selenium.common.exceptions import NoSuchElementException
from selenium.common.exceptions import NoAlertPresentException
import unittest, time, re


class MozillaAddon(unittest.TestCase):
    def setUp(self):
        self.email = "tester" + str(time.time())[13:] + "@hotbsoftware.com"
        self.driver = webdriver.Chrome()
        self.driver.implicitly_wait(30)
        self.base_url = "http://ca-prototype-hotb-staging.s3-website-us-west-1.amazonaws.com"
        self.verificationErrors = []
        self.accept_next_alert = True

    def test_mozilla_addon(self):
        driver = self.driver
        driver.get(self.base_url + "/")
        driver.maximize_window()
        driver.find_element_by_xpath((
                                     "((//A[@href='register.html'][text()='Register'][text()='Register'])[1]/../../../../../../..//A[@href='register.html'][text()='Register'][text()='Register'][text()='Register'])[2]")).click()
        driver.find_element_by_id("email").send_keys(self.email)
        driver.find_element_by_id("password").send_keys("password123")
        driver.find_element_by_id("confirmpassword").send_keys("password123")
        driver.find_element_by_xpath("//BUTTON[@id='subscribe']/self::BUTTON").click()

    def is_element_present(self, how, what):
        try:
            self.driver.find_element(by=how, value=what)
        except NoSuchElementException as e:
            return False
        return True

    def is_alert_present(self):
        try:
            self.driver.switch_to.alert
        except NoAlertPresentException as e:
            return False
        return True

    def close_alert_and_get_its_text(self):
        try:
            alert = self.driver.switch_to.alert
            alert_text = alert.text
            if self.accept_next_alert:
                alert.accept()
            else:
                alert.dismiss()
            return alert_text
        finally:
            self.accept_next_alert = True

    def tearDown(self):
        time.sleep(5)
        self.assertEqual([], self.verificationErrors)


if __name__ == "__main__":
    unittest.main()