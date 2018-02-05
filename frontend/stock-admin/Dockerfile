FROM hub.c.163.com/nce2/nodejs:0.12.2

# Create app directory
RUN mkdir -p /home/frontend
WORKDIR /home/frontend

# Bundle app source
COPY . /home/frontend
RUN npm install

#Export 4200 port

EXPOSE 4200
CMD [ "npm", "start" ]