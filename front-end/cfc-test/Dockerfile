FROM node:latest as build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY ./ .
RUN npm run build

# Dockerfile do serviço Nginx
FROM nginx as production-stage
RUN mkdir /app
COPY --from=build-stage /app/dist /app
RUN rm /etc/nginx/nginx.conf
# USER nginx

COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

# RUN echo "$(/etc/nginx/nginx.conf)"
CMD ["nginx","-g","daemon off;"]
