#!/bin/bash

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m'

if command -v docker &> /dev/null && docker compose version &> /dev/null; then
  DOCKER_COMPOSE="docker compose"
elif command -v docker-compose &> /dev/null; then
  DOCKER_COMPOSE="docker-compose"
else
  echo -e "${RED}❌ Neither 'docker compose' nor 'docker-compose' found. Please install Docker Compose.${NC}"
  exit 1
fi

wait_for_db() {
  echo -e "${YELLOW}⏳ Waiting for database to be ready...${NC}"

  DB_CONTAINER=$($DOCKER_COMPOSE ps -q db)

  for i in {1..30}; do
    if docker exec "$DB_CONTAINER" pg_isready -U meduser &>/dev/null; then
      echo -e "${GREEN}✅ Database is ready!${NC}"
      return 0
    fi
    echo -e "${YELLOW}⏳ Attempt $i/30 - Database not ready yet...${NC}"
    sleep 2
  done

  if [ $i -eq 30 ]; then
    echo -e "${RED}⚠️  Database may not be fully ready. Try waiting a bit more before starting the application.${NC}"
    return 1
  fi
}

is_db_up() {
  DB_CONTAINER=$($DOCKER_COMPOSE ps -q db)
  if docker exec "$DB_CONTAINER" pg_isready -U meduser &>/dev/null; then
    return 0
  fi
  return 1
}

case "$1" in
  "clean")
    mvn clean
    ;;
  "run")
    echo -e "${YELLOW}🚀 Running application...${NC}"
    if ! is_db_up; then
      echo -e "${RED}❌ Database is not up. Please start the database first using './app.sh start-db'${NC}"
      exit 1
    fi

    mvn spring-boot:run
    ;;
  "test")
    echo -e "${YELLOW}🧪 Running tests...${NC}"
    if ! is_db_up; then
      echo -e "${RED}❌ Database is not up. Please start the database first using './app.sh start-db'${NC}"
      exit 1
    fi
    mvn test
    ;;

  "start-db"|"up")
    echo -e "${YELLOW}🚀 Starting database...${NC}"
    $DOCKER_COMPOSE up -d
    wait_for_db
    ;;
  "stop-db"|"down")
    echo -e "${YELLOW}🛑 Stopping database...${NC}"
    $DOCKER_COMPOSE down
    ;;
  "clean-db")
    echo -e "${YELLOW}🧹 Cleaning everything...${NC}"
    $DOCKER_COMPOSE down -v --rmi all
    docker system prune -f
    ;;
  "reset-db")
    echo -e "${YELLOW}🗄️  Resetting database...${NC}"
    echo "⚠️  All data in the database will be lost!"
    read -p "Continue? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
      $DOCKER_COMPOSE down -v
      $DOCKER_COMPOSE up -d

      echo -e "${GREEN}✅ Database reset! Waiting for initialization...${NC}"

      wait_for_db
    else
      echo -e "${RED}❌ Operation canceled${NC}"
    fi
    ;;
  "access-db")
    $DOCKER_COMPOSE exec db psql -U meduser -d medhealth
    ;;
  "show-tables"|"tables")
    echo -e "${YELLOW}📋 Showing all tables...${NC}"
    if ! is_db_up; then
      echo -e "${RED}❌ Database is not up. Please start the database first using './app.sh start-db'${NC}"
      exit 1
    fi
    $DOCKER_COMPOSE exec db psql -U meduser -d medhealth -c "\dt"
    ;;
  "logs")
    $DOCKER_COMPOSE logs -f
    ;;
  "status")
    $DOCKER_COMPOSE ps
    ;;
  "clean-vol")
    echo -e "${YELLOW}🧹 Cleaning volumes...${NC}"
    $DOCKER_COMPOSE down -v
    ;;
  "fix")
    echo -e "${YELLOW}🔧 Fixing container conflicts...${NC}"
    $DOCKER_COMPOSE down --remove-orphans
    docker container prune -f
    $DOCKER_COMPOSE up -d
    ;;
  *)
    echo -e "${BLUE}medHealthApi - App Manager${NC}"
    echo
    echo -e "${YELLOW}Application commands:${NC}"
    echo -e "  ${GREEN}clean${NC}         - Clean application"
    echo -e "  ${GREEN}run${NC}           - Run application"
    echo -e "  ${GREEN}test${NC}          - Run tests"
    echo
    echo -e "${YELLOW}Database commands:${NC}"
    echo -e "  ${GREEN}start-db/up${NC}   - Start database"
    echo -e "  ${GREEN}stop-db/down${NC}  - Stop database"
    echo -e "  ${GREEN}clean-db${NC}      - Clean database"
    echo -e "  ${GREEN}reset-db${NC}      - Reset database (all data will be lost)"
    echo -e "  ${GREEN}access-db${NC}     - Access database cli"
    echo -e "  ${GREEN}show-tables/tables${NC}   - Show all tables in database"
    echo -e "  ${GREEN}fix${NC}           - Fix container conflicts"
    echo
    echo -e "${YELLOW}Utils:${NC}"
    echo -e "  ${GREEN}logs${NC}          - View logs"
    echo -e "  ${GREEN}status${NC}        - Container status"
    echo -e "  ${GREEN}clean-vol${NC}     - Clean volumes"
    echo
    exit 1
    ;;
esac