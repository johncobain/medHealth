#!/bin/bash

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m'

declare -A services
services["api"]="medHealthAPI"
services["ms"]="medHealthNotifications"
services["eureka"]="medHealthEureka"
services["front"]="medHealthFE"

if command -v docker &> /dev/null && docker compose version &> /dev/null; then
  DOCKER_COMPOSE="docker compose"
elif command -v docker-compose &> /dev/null; then
  DOCKER_COMPOSE="docker-compose"
else
  echo -e "${RED}❌ Neither 'docker compose' nor 'docker-compose' found. Please install Docker Compose.${NC}"
  exit 1
fi

wait_for_service() {
  local service=$1
  local max_attempts=30
  echo -e "${YELLOW}⏳ Waiting for $service to be ready...${NC}"
  
  for i in $(seq 1 $max_attempts); do
    if $DOCKER_COMPOSE ps | grep -q "$service.*healthy\|$service.*running"; then
      echo -e "${GREEN}✅ $service is ready!${NC}"
      return 0
    fi
    sleep 2
  done
  
  echo -e "${RED}❌ $service failed to start${NC}"
  return 1
}

is_db_up() {
  DB_CONTAINER=$($DOCKER_COMPOSE ps -q db 2>/dev/null)
  if [ -n "$DB_CONTAINER" ] && docker exec "$DB_CONTAINER" pg_isready -U meduser &>/dev/null; then
    return 0
  fi
  return 1
}

run_local() {
  local service_key=$1
  
  if [ -z "${services[$service_key]}" ]; then
    echo -e "${RED}❌ Unknown service: '$service_key'${NC}"
    echo -e "${YELLOW}Available: api, ms, eureka, front${NC}"
    exit 1
  fi

  local service_dir=${services[$service_key]}
  echo -e "${BLUE}🚀 Running $service_key locally...${NC}"
  
  if [ "$service_key" == "front" ]; then
    (cd "$service_dir" && npm run dev)
  else
    (cd "$service_dir" && mvn spring-boot:run)
  fi
}

case "$1" in
  # =========== COMANDOS DE DESENVOLVIMENTO LOCAL ============
  "run")
    if [ -z "$2" ]; then
      echo -e "${RED}❌ Specify service: api, ms, front${NC}"
      exit 1
    fi
    
    if [ "$2" != "front" ] && ! is_db_up; then
      echo -e "${RED}❌ Database not running. Start with: ./app.sh db${NC}"
      exit 1
    fi
    
    run_local "$2"
    ;;

  "clean")
    if [ -z "$2" ]; then
      echo -e "${RED}❌ Specify service: api, ms, eureka, front${NC}"
      exit 1
    fi
    
    local service_dir=${services[$2]}
    echo -e "${YELLOW}🧹 Cleaning $2...${NC}"
    
    if [ "$2" == "front" ]; then
      (cd "$service_dir" && rm -rf node_modules dist)
    else
      (cd "$service_dir" && mvn clean)
    fi
    ;;

  "test")
    if [ -z "$2" ]; then
      echo -e "${RED}❌ Specify service: api, ms, eureka, front${NC}"
      exit 1
    fi
    
    if [ "$2" != "front" ] && ! is_db_up; then
      echo -e "${RED}❌ Database not running. Start with: ./app.sh db${NC}"
      exit 1
    fi
    
    local service_dir=${services[$2]}
    echo -e "${YELLOW}🧪 Testing $2...${NC}"
    
    if [ "$2" == "front" ]; then
      (cd "$service_dir" && npm run test)
    else
      (cd "$service_dir" && mvn test)
    fi
    ;;

  # ============ COMANDOS DE BANCO DE DADOS ============
  "db")
    echo -e "${YELLOW}🗄️  Starting databases (db + db-mail)...${NC}"
    $DOCKER_COMPOSE up -d db db-mail
    wait_for_service "db"
    wait_for_service "db-mail"
    echo -e "${GREEN}✅ Databases running!${NC}"
    ;;

  "db-stop")
    echo -e "${YELLOW}🛑 Stopping databases...${NC}"
    $DOCKER_COMPOSE stop db db-mail
    ;;

  "db-reset")
    echo -e "${RED}⚠️  This will DELETE ALL DATA in both databases!${NC}"
    read -p "Continue? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
      echo -e "${YELLOW}🗑️  Resetting databases...${NC}"
      $DOCKER_COMPOSE down db db-mail -v
      $DOCKER_COMPOSE up -d db db-mail
      wait_for_service "db"
      wait_for_service "db-mail"
      echo -e "${GREEN}✅ Databases reset!${NC}"
    else
      echo -e "${RED}❌ Cancelled${NC}"
    fi
    ;;

  "db-shell")
    echo -e "${BLUE}🐘 Accessing main database...${NC}"
    $DOCKER_COMPOSE exec db psql -U meduser -d medhealth
    ;;

  "db-tables")
    echo -e "${YELLOW}📋 Tables in main database:${NC}"
    if ! is_db_up; then
      echo -e "${RED}❌ Database not running. Start with: ./app.sh db${NC}"
      exit 1
    fi
    $DOCKER_COMPOSE exec db psql -U meduser -d medhealth -c "\dt"
    ;;

  # ============ COMANDOS DE RABBITMQ ============
  "rabbit")
    echo -e "${YELLOW}🐰 Starting RabbitMQ...${NC}"
    $DOCKER_COMPOSE up -d rabbitmq
    wait_for_service "rabbitmq"
    echo -e "${GREEN}✅ RabbitMQ running at http://localhost:15672${NC}"
    echo -e "${BLUE}   User: rabbituser / Pass: rabbitpass${NC}"
    ;;

  "rabbit-stop")
    echo -e "${YELLOW}🛑 Stopping RabbitMQ...${NC}"
    $DOCKER_COMPOSE stop rabbitmq
    ;;

  # ============ COMANDOS DE BACKEND DOCKER ============
  "backend")
    echo -e "${YELLOW}🔧 Starting backend stack (DB + RabbitMQ + API + MS + EUREKA)...${NC}"
    $DOCKER_COMPOSE up -d db db-mail rabbitmq
    wait_for_service "db"
    wait_for_service "db-mail"
    wait_for_service "rabbitmq"
    
    $DOCKER_COMPOSE up -d api notifications eureka
    echo -e "${GREEN}✅ Backend running!${NC}"
    echo -e "${BLUE}   API: http://localhost:8081${NC}"
    echo -e "${BLUE}   Notifications: http://localhost:8082${NC}"
    ;;

  "backend-stop")
    echo -e "${YELLOW}🛑 Stopping backend...${NC}"
    $DOCKER_COMPOSE stop api notifications eureka rabbitmq
    ;;

  "backend-logs")
    $DOCKER_COMPOSE logs -f api notifications eureka rabbitmq
    ;;

  # ============ COMANDOS DE FRONTEND DOCKER ============
  "frontend")
    echo -e "${YELLOW}🎨 Starting frontend in Docker...${NC}"
    $DOCKER_COMPOSE up -d frontend
    echo -e "${GREEN}✅ Frontend running at http://localhost:5173${NC}"
    ;;

  "frontend-stop")
    echo -e "${YELLOW}🛑 Stopping frontend...${NC}"
    $DOCKER_COMPOSE stop frontend
    ;;

  # ============ COMANDOS COMPLETOS DOCKER ============
  "up")
    echo -e "${YELLOW}🚀 Starting EVERYTHING (databases + backend + frontend)...${NC}"
    $DOCKER_COMPOSE up -d
    echo -e "${GREEN}✅ All services running!${NC}"
    echo -e "${BLUE}   Frontend: http://localhost:5173${NC}"
    echo -e "${BLUE}   API: http://localhost:8081${NC}"
    echo -e "${BLUE}   Notifications: http://localhost:8082${NC}"
    echo -e "${BLUE}   RabbitMQ: http://localhost:15672${NC}"
    ;;

  "down")
    echo -e "${YELLOW}🛑 Stopping all services...${NC}"
    $DOCKER_COMPOSE down
    ;;

  "restart")
    echo -e "${YELLOW}🔄 Restarting all services...${NC}"
    $DOCKER_COMPOSE restart
    ;;

  "build")
    echo -e "${YELLOW}🔨 Building all Docker images...${NC}"
    $DOCKER_COMPOSE build --no-cache
    ;;

  "rebuild")
    echo -e "${YELLOW}🔨 Rebuilding and restarting...${NC}"
    $DOCKER_COMPOSE down
    $DOCKER_COMPOSE build --no-cache
    $DOCKER_COMPOSE up -d
    echo -e "${GREEN}✅ Rebuild complete!${NC}"
    ;;

  # ============ COMANDOS DE MONITORAMENTO ============
  "logs")
    if [ -z "$2" ]; then
      $DOCKER_COMPOSE logs -f
    else
      $DOCKER_COMPOSE logs -f "$2"
    fi
    ;;

  "status"|"ps")
    $DOCKER_COMPOSE ps
    ;;

  # ============ COMANDOS DE LIMPEZA ============
  "clean-volumes")
    echo -e "${YELLOW}🧹 Removing volumes (will delete all data)...${NC}"
    read -p "Continue? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
      $DOCKER_COMPOSE down -v
      echo -e "${GREEN}✅ Volumes removed${NC}"
    fi
    ;;

  "clean-all")
    echo -e "${RED}⚠️  This will remove EVERYTHING (containers, volumes, images)${NC}"
    read -p "Continue? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
      $DOCKER_COMPOSE down -v --rmi all
      docker system prune -f
      echo -e "${GREEN}✅ Everything cleaned${NC}"
    fi
    ;;

  # ============ HELP ============
  *)
    echo -e "${BLUE}═══════════════════════════════════════════════${NC}"
    echo -e "${BLUE}  medHealth - Application Manager${NC}"
    echo -e "${BLUE}═══════════════════════════════════════════════${NC}"
    echo
    echo -e "${YELLOW}LOCAL DEVELOPMENT:${NC}"
    echo -e "  ${GREEN}run <service>${NC}      Run locally (api|ms|eureka|front)"
    echo -e "  ${GREEN}clean <service>${NC}    Clean build artifacts"
    echo -e "  ${GREEN}test <service>${NC}     Run tests"
    echo
    echo -e "${YELLOW}DATABASE:${NC}"
    echo -e "  ${GREEN}db${NC}                Start both databases"
    echo -e "  ${GREEN}db-stop${NC}           Stop databases"
    echo -e "  ${GREEN}db-reset${NC}          Reset databases (deletes data)"
    echo -e "  ${GREEN}db-shell${NC}          Open PostgreSQL shell"
    echo -e "  ${GREEN}db-tables${NC}         List all tables from main database"
    echo
    echo -e "${YELLOW}RABBITMQ:${NC}"
    echo -e "  ${GREEN}rabbit${NC}            Start RabbitMQ"
    echo -e "  ${GREEN}rabbit-stop${NC}       Stop RabbitMQ"
    echo
    echo -e "${YELLOW}BACKEND DOCKER:${NC}"
    echo -e "  ${GREEN}backend${NC}           Start backend stack (API + MS + RabbitMQ + Eureka)"
    echo -e "  ${GREEN}backend-stop${NC}      Stop backend"
    echo -e "  ${GREEN}backend-logs${NC}      View backend logs"
    echo
    echo -e "${YELLOW}FRONTEND DOCKER:${NC}"
    echo -e "  ${GREEN}frontend${NC}          Start frontend in Docker"
    echo -e "  ${GREEN}frontend-stop${NC}     Stop frontend"
    echo
    echo -e "${YELLOW}FULL STACK DOCKER:${NC}"
    echo -e "  ${GREEN}up${NC}                Start everything"
    echo -e "  ${GREEN}down${NC}              Stop everything"
    echo -e "  ${GREEN}restart${NC}           Restart all services"
    echo -e "  ${GREEN}build${NC}             Build all images"
    echo -e "  ${GREEN}rebuild${NC}           Rebuild and restart"
    echo
    echo -e "${YELLOW}MONITORING:${NC}"
    echo -e "  ${GREEN}logs <service>${NC}    View logs (all or specific)"
    echo -e "  ${GREEN}status / ps${NC}       Show service status"
    echo
    echo -e "${YELLOW}CLEANUP:${NC}"
    echo -e "  ${GREEN}clean-volumes${NC}     Remove volumes (deletes data)"
    echo -e "  ${GREEN}clean-all${NC}         Remove everything (containers, volumes, images)"
    echo
    echo -e "${BLUE}═══════════════════════════════════════════════${NC}"
    echo -e "${YELLOW}Recommended workflow:${NC}"
    echo -e "   ${BLUE}1.${NC} ./app.sh db              ${GREEN}# Start databases${NC}"
    echo -e "   ${BLUE}2.${NC} ./app.sh rabbit          ${GREEN}# Start RabbitMQ${NC}"
    echo -e "   ${BLUE}3.${NC} ./app.sh run eureka      ${GREEN}# Run Eureka locally${NC}"
    echo -e "   ${BLUE}4.${NC} ./app.sh run api         ${GREEN}# Run API locally${NC}"
    echo -e "   ${BLUE}5.${NC} ./app.sh run ms          ${GREEN}# Run MS locally${NC}"
    echo -e "   ${BLUE}6.${NC} ./app.sh run front       ${GREEN}# Run frontend locally${NC}"
    echo
    echo -e "${YELLOW}Or use Docker for everything:${NC}"
    echo -e "   ${BLUE}./app.sh up${NC}                ${GREEN}# Start full stack${NC}"
    echo -e "${BLUE}═══════════════════════════════════════════════${NC}"
    exit 1
    ;;
esac
