
# Black        0;30     Dark Gray     1;30
# Red          0;31     Light Red     1;31
# Green        0;32     Light Green   1;32
# Brown/Orange 0;33     Yellow        1;33
# Blue         0;34     Light Blue    1;34
# Purple       0;35     Light Purple  1;35
# Cyan         0;36     Light Cyan    1;36
# Light Gray   0;37     White         1;37

RESET="\033[0m"
BOLD="\033[1m"
YELLOW="\033[38;5;11m"
GREEN="\033[1,32m"
BLUE="\033[1;36m"
VIOLET="\033[1;34m"
RED="\033[1;31m"
ORANGE="\033[0,33m"
# ORANGE=$'\e[33;40m'

function display_info() {
  local msg="$1"
  echo -e $BOLD$BLUE"[info] $msg"$RESET
}

function display_error() {
  local msg="$1"
  echo -e $BOLD$RED"[error] $msg"$RESET
}

function display_warn() {
  local msg="$1"
  echo -e $BOLD$YELLOW"[warn] $msg"$RESET
}

function display_H1() {
  local msg="$1"
  echo -e $BOLD$VIOLET"[info] $msg"$RESET
}

function display_break() {
  echo -e ""
}

function display_command() {
  local cmd="$1"
  echo -e $BOLD$VIOLET"[info] $cmd"$RESET
}

validate_url() {
  local url=$1
  if [ -e $url ]; then
    echo "variable url is not set. cannot continue"
    return 1
  fi
  response_code=$(curl --write-out %{http_code} --silent --output /dev/null $url)
  if [[ ${response_code:0:1} != "2" ]] ; then
    return 1
  else
    return 0
  fi
}


function confirm_execute() {
  local cmd="$1"
  local prompt="about to run command, confirm (y/n): "
  read -e -p "$(echo -e $BOLD$YELLOW$prompt $cmd $GREEN)" -i "y" response
  echo -e $RESET
  if [ "$run_it" == "y" ]; then
    eval "$cmd"
  fi
}

function prompt() {
  local prompt=$1
  local default_value=$2
  # local d_prompt="$(echo -e $BOLD$YELLOW$prompt)"
  # local d_default_value="$(echo -e $GREEN$default_value)"
  local value=""

  read -e -p "$prompt" -i "$d_default_value" value
  echo -e "$RESET"
  echo $value
}

project_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

jdk_version='jdk-8u151'
scala_version="2.11"
kafka_version="0.10.2.1"
maven_version='3.3.9'

local_dir=$project_dir/usr
docker_dir=$project_dir/docker
install_dir=$project_dir/install
keystore_dir=$project_dir/keystore
data_dir=$project_dir/data
kafka_dir=$project_dir/kafka
kafka_config_dir=$install_dir/config/kafka
utils_dir=$project_dir/utils
local_kafka_dir=$local_dir/kafka
local_maven_dir=$local_dir/maven
local_java_dir=$local_dir/java

kafka_installation_dir="$local_kafka_dir/kafka_$scala_version-$kafka_version"

# echo -e $BOLD$BLUE"[info] "'$local_dir='$local_dir$RESET
# echo -e $BOLD$BLUE"[info] "'$docker_dir='$docker_dir$RESET
# echo -e $BOLD$BLUE"[info] "'$install_dir'=$install_dir$RESET
# echo -e $BOLD$BLUE"[info] "'$keystore_dir='$keystore_dir$RESET
# echo -e $BOLD$BLUE"[info] "'$data_dir='$data_dir$RESET
# echo -e $BOLD$BLUE"[info] "'$kafka_dir='$kafka_dir$RESET
# echo -e $BOLD$BLUE"[info] "'$kafka_config_dir='$kafka_config_dir$RESET
# echo -e $BOLD$BLUE"[info] "'$utils_dir'=$utils_dir$RESET
# echo -e $BOLD$BLUE"[info] "'$local_kafka_dir'=$local_kafka_dir$RESET
# echo -e $BOLD$BLUE"[info] "'$local_maven_dir='$local_maven_dir$RESET
# echo -e $BOLD$BLUE"[info] "'$local_java_dir='$local_java_dir$RESET
