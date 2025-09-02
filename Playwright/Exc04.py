from playwright.sync_api import sync_playwright
import time

with sync_playwright() as pw:
    navegador = pw.chromium.launch(headless=False)
    pagina = navegador.new_page()
    pagina.goto("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login")
    nome = pagina.locator('xpath=//*[@id="app"]/div[1]/div/div[1]/div/div[2]/div[2]/form/div[1]/div/div[2]/input')  
    nome.fill('Admin')
    senha = pagina.locator('xpath=//*[@id="app"]/div[1]/div/div[1]/div/div[2]/div[2]/form/div[2]/div/div[2]/input')
    senha.fill('admin123')
    botao = pagina.locator('xpath=//*[@id="app"]/div[1]/div/div[1]/div/div[2]/div[2]/form/div[3]/button')
    botao.click()
    time.sleep(2)