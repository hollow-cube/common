# HollowCube Common

## Background
This is a common package for libraries which will be used by more than a single game/mode on the HollowCube network. 

## Setup

* Install Docker 
  * Navigate to https://docs.docker.com/engine/install/ and follow the steps for your operating system

## Testing with databases

### Intellij
* Navigate to /common/docker-compose.yml
* Press the start icon next to services

### Terminal
* Navigate to /common/
* Run `docker compose up -d` to start the docker container
  * Remove the `-d` flag if you'd like the output in the terminal, otherwise it will run in the background
* Run `docker compose down` to kill the docker container
# common