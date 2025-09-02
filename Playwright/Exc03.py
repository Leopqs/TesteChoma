from playwright.sync_api import sync_playwright
import time

with sync_playwright() as pw:
    navegador = pw.chromium.launch(headless=False)
    pagina = navegador.new_page()
    pagina.goto("https://practicetestautomation.com/practice-test-login/")
    nome = pagina.locator("#username")
    nome.fill('student')
    senha = pagina.locator("#password")
    senha.fill('Password123')
    botao = pagina.get_by_role("button", name="Submit")
    botao.click()
    time.sleep(2)
    botao2 = pagina.locator('xpath=//*[@id="loop-container"]/div/article/div[2]/div/div/div/a')
    botao2.click()
    time.sleep(2)