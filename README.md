# Rinha Lang

![banner](./img/banner.png)
>O ideal da rinha é fazer um interpretador ou compilador que rode em uma maquina com 2 núcleos e 2G de RAM.  [ler mais](https://github.com/aripiprazole/rinha-de-compiler)

## Como rodar

1. Clone o repositorio
   ```bash	
   git clone https://github.com/itallo-david/rinha_lang.git
   ```

2. Entre no projeto
   ```bash
   cd rinha_lang
   ```

3. Rodando:
    - Docker:
      ```bash
      docker build --tag rinha_lang .
      ```
      ```bash
      docker run -v ./source.rinha.json:/var/rinha/source.rinha.json rinha_lang
      ```