FROM node:18 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
# Añadir log para verificar la estructura de directorios
RUN npm run build && ls -la dist/

FROM nginx:alpine
# Modificar la ruta según la estructura real
COPY --from=build /app/dist/* /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

