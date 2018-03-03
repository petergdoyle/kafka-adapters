#!/bin/sh
. ../common.sh

java -version > /dev/null 2>&1
if [ $? -eq 127 ]; then
  display_error "Jdk8 is not installed. Install Jdk8"
  exit 1
fi

maven_home="$local_maven_dir/default"
if [ ! -d $local_maven_dir/apache-maven-$maven_version ]; then
  downloadable="apache-maven-$maven_version-bin.tar.gz"
  if [ ! -f /tmp/$downloadable ]; then
    echo "downloading $downloadable..."
    download_url="http://www-us.apache.org/dist/maven/maven-3/$maven_version/binaries/$downloadable"
    curl -o /tmp/$downloadable $download_url
  fi

  if [ ! -d $local_maven_dir ]; then
    mkdir -pv $local_maven_dir
  fi

  tar -xvf /tmp/$downloadable -C $local_maven_dir
  ln -s $local_maven_dir/apache-maven-$maven_version $maven_home


  export MAVEN_HOME=$maven_home
  if ! grep -q MAVEN_HOME ~/.bash_profile; then
    sed -i "s@^PATH=@MAVEN_HOME=$MAVEN_HOME\n&@" ~/.bash_profile
    sed -i '/^PATH=/ s/$/:$MAVEN_HOME\/bin/' ~/.bash_profile
    display_warn "$jdk_version has been installed and MAVEN_HOME is set to $MAVEN_HOME. Please source your ~/.bash_profile to include MAVEN_HOME/bin your PATH"
  fi


else
  display_info "apache-maven-$maven_version already appears to be installed. skipping."
fi
