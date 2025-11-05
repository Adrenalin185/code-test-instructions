FROM node:lts AS builder
WORKDIR /app
COPY /url-ui-service/package*.json ./
COPY /url-ui-service/ ./
RUN npm clean-install
RUN npm run build

FROM nginx:stable-alpine
WORKDIR /usr/share/nginx/html
COPY --from=builder /app/dist .
expose 80
CMD ["nginx", "-g", "daemon off;"]
