# Install dependencies with yarn.
# Node 16 is the version Vercel uses.
FROM node:16-alpine AS deps

RUN mkdir -p /usr/app/
WORKDIR /usr/app/
COPY ./ /usr/app/


RUN yarn --frozen-lockfile
RUN yarn build

EXPOSE 3000
CMD ["yarn", "start"]