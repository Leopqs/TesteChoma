from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver import ActionChains
from selenium.webdriver.common.actions.wheel_input import ScrollOrigin
import time

navegador = webdriver.Chrome()
navegador.get("https://the-internet.herokuapp.com/login")
input_nome = navegador.find_element(By.ID,"username")
input_senha = navegador.find_element(By.ID,"password")
botao = navegador.find_element(By.CSS_SELECTOR,"button")
navegador.maximize_window()
navegador.execute_script("arguments[0].scrollIntoView();", botao)
input_nome.send_keys("tomsmith")
input_senha.send_keys("SuperSecretPassword!")
botao.click()
time.sleep(2)
botao2 = navegador.find_element(By.XPATH,'/html/body/div[2]/div/div/a')
botao2.click()