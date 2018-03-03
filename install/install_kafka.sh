 #!/bin/sh
 . ../common.sh

java -version > /dev/null 2>&1
if [ $? -eq 127 ]; then
  display_error "Jdk8 is not installed. Install Jdk8"
  exit 1
fi

kafka_base_location=$local_kafka_dir
if [ ! -d $kafka_base_location ]; then
  mkdir -pv $kafka_base_location
fi

downloadable="kafka_$scala_version-$kafka_version.tgz"
kafka_home=$kafka_base_location/default

if [ ! -d $kafka_installation_dir ]; then
  # install and link
  if [ -L $kafka_home ]; then
    rm -fv $kafka_home
  fi

 if [ ! -f /tmp/$downloadable ]; then
    kafka_link_file=$kafka_config_dir/$kafka_version/link
    if [ ! -f $kafka_link_file ]; then
      display_error "Cannot find a link file in location $kafka_config_dir/$kafka_version/link. Add a link file that contains the downloadable kafka url specific to $downloadable. Cannot continue"
      exit 1
    fi
    download_url=`cat $kafka_link_file`
    display_info "Verifying url $download_url..."

    if ! `validate_url $download_url`; then
      display_error "Bad url specified as $download_url. Server returned $response_code. Check link. Cannot continue"
      exit 1
    fi

    display_info "Downloading kafka to $kafka_base_location..."
    curl -o /tmp/$downloadable $download_url
  fi

  tar -xvf /tmp/$downloadable -C $kafka_base_location

  ln -vs $kafka_installation_dir $kafka_home

  export KAFKA_HOME=$kafka_home
  if ! grep -q KAFKA_HOME ~/.bash_profile; then
    sed -i "s@^PATH=@KAFKA_HOME=$KAFKA_HOME\n&@" ~/.bash_profile
    sed -i '/^PATH=/ s/$/:$KAFKA_HOME\/bin/' ~/.bash_profile
    display_warn "$jdk_version has been installed and KAFKA_HOME is set to $KAFKA_HOME. Please source your ~/.bash_profile to include KAFKA_HOME/bin your PATH"
  fi

  if [ ! -d $kafka_installation_dir/config/orig ]; then
    display_info "making copy of the original config files..."
    mkdir -pv $kafka_installation_dir/config/orig
    cp -fv $kafka_installation_dir/config/* $kafka_installation_dir/config/orig
  fi

  cp -fv $kafka_config_dir/$kafka_version/* $kafka_installation_dir/config

else
  display_info "kafka-$kafka_version already appears to be installed. skipping."
fi
