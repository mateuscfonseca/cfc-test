# Dockerfile do serviço Nginx
FROM nginx

# Copia o arquivo de configuração personalizado do Nginx para o local correto
RUN rm /etc/nginx/nginx.conf
COPY nginx.conf /etc/nginx/nginx.conf

CMD ["nginx","-g","daemon off;"]

# Exponha a porta 80
EXPOSE 80
