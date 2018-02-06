#sudo docker -t stockadmin:v1
FROM docker.io/node:latest

# Create app directory
RUN mkdir -p /home/frontend
WORKDIR /home/frontend

# Bundle app source
COPY . /home/frontend
RUN npm install

#Export 4200 port

EXPOSE 4201
CMD [ "npm", "start" ]
