#!/bin/sh
. ../common.sh

java_home="$local_maven_dir/default"

if [ ! -d $local_java_dir/jdk1.8.0_151/ ]; then
  downloadable="$jdk_version-linux-x64.tar.gz"
  if [ ! -f /tmp/$downloadable ]; then
    echo "downloading $downloadable..."
    BASE_URL_8=http://download.oracle.com/otn-pub/java/jdk/8u151-b12/e758a0de34e24606bca991d704f6dcbf/$jdk_version-linux-x64.tar.gz
    wget -P /tmp/ -c --header "Cookie: oraclelicense=accept-securebackup-cookie" "${BASE_URL_8}${platform}"
  fi

  if [ ! -d $local_java_dir ]; then
    mkdir -pv $local_java_dir
  fi

  tar -xvf /tmp/$jdk_version-linux-x64.tar.gz -C $local_java_dir \
    && ln -s $local_java_dir/jdk1.8.0_151/ $java_home \
    && rm -f $jdk_version-linux-x64.tar.gz

    export JAVA_HOME=$java_home
    if ! grep -q JAVA_HOME ~/.bash_profile; then
      sed -i "s@^PATH=@JAVA_HOME=$JAVA_HOME\n&@" ~/.bash_profile
      sed -i '/^PATH=/ s/$/:$JAVA_HOME\/bin/' ~/.bash_profile
      display_warn "$jdk_version has been installed and JAVA_HOME is set to $JAVA_HOME. Please source your ~/.bash_profile to include JAVA_HOME/bin your PATH"
    fi

else

    display_info "$jdk_version already appears to be installed. skipping."

fi
