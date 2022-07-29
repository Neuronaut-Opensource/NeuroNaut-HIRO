FROM nginx:latest
RUN rm /etc/nginx/conf.d/default.conf
COPY ./Docker/Nginx/nginx-prod.conf /etc/nginx/conf.d
COPY ./Docker/Nginx/certs/self-signed.crt /etc/nginx/ssl/selfsigned.crt
COPY ./Docker/Nginx/certs/self-signed.key /etc/nginx/ssl/selfsigned.key
CMD ["nginx", "-g", "daemon off;"]