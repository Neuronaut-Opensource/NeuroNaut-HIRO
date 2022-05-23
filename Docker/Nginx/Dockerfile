FROM nginx:latest
RUN rm /etc/nginx/conf.d/default.conf
COPY ./Docker/Nginx/nginx.conf /etc/nginx/conf.d
COPY ./Docker/Nginx/certs/fullchain.pem /etc/nginx/ssl/fullchain.pem
COPY ./Docker/Nginx/certs/privkey.pem /etc/nginx/ssl/privkey.pem
CMD ["nginx", "-g", "daemon off;"]