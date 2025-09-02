from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver import ActionChains
from selenium.webdriver.common.actions.wheel_input import ScrollOrigin
import time

navegador = webdriver.Chrome()
navegador.get("https://www.saucedemo.com/")
input_nome = navegador.find_element(By.ID,"user-name")
input_senha = navegador.find_element(By.ID,"password")
botao = navegador.find_element(By.ID,"login-button")
navegador.maximize_window()
navegador.execute_script("arguments[0].scrollIntoView();", botao)
input_nome.send_keys("standard_user")
input_senha.send_keys("secret_sauce")
botao.click()
botoes = navegador.find_elements(By.TAG_NAME, "button")
for botao in botoes:
        botao.click()
        time.sleep(1)