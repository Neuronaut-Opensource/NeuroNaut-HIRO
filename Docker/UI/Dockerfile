# pull the official base image
FROM node:alpine
# add `/app/node_modules/.bin` to $PATH
ENV PATH /ui/node_modules/.bin:$PATH
# install application dependencies
COPY ./ui/package.json ./
COPY ./ui/package-lock.json ./
RUN npm i
# add app
COPY ./ui ./
# start app
CMD ["npm", "start"]