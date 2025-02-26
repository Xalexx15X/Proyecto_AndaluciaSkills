FROM node:18

# Instalar Angular CLI globalmente
RUN npm install -g @angular/cli

WORKDIR /app

# Copiar package.json y package-lock.json
COPY package*.json ./

# Instalar dependencias
RUN npm install

# Copiar el resto del código
COPY . .

RUN ng build

FROM nginx:alpine
COPY --from=0 /app/dist/frontend /usr/share/nginx/html

EXPOSE 80  

CMD ["nginx", "-g", "daemon off;"]

