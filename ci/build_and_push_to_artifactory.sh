#!/usr/bin/env bash
# Requires 3 arguments:
#   Example from bamboo:
#       build_and_push_to_artifactory.sh ${bamboo.artifactory.url} ${bamboo.artifactory.username} ${bamboo.artifactory.password}
set -x

artifactoryUrl=$1
artifactoryUsername=$2
artifactoryPassword=$3

docker run --rm -v $(pwd):/builddir -w /builddir dr.chefkoch.net/android-build/base "mkdir /opt/android-sdk-linux/licenses/; echo -e \n8933bad161af4178b1185d1a37fbf41ea5269c55 > /opt/android-sdk-linux/licenses/android-sdk-license; ./gradlew clean build && ./gradlew publish -Partifactory_url=$(artifactoryUrl) -Partifactory_user=$(artifactoryUsername) -Partifactory_password=$(artifactoryPassword)"