#!/usr/bin/env bash
# Requires 3 arguments
#   Example invocations
#     From bamboo:
#       build_and_push_to_artifactory.sh ${bamboo.artifactory.url} ${bamboo.artifactory.username} ${bamboo.artifactory.password}
#     Local with Docker installed - use correct values for artifactoryUsername and artifactoryPassword
#       ./ci/build_and_push_to_artifactory.sh "http://artifactory.prod.chefkoch.de:30046/artifactory/libs-snapshot-local" "artifactoryUsername" "artifactoryPassword"
set -x

artifactoryUrl=$1
artifactoryUsername=$2
artifactoryPassword=$3

# https://github.com/mindrunner/docker-android-sdk
docker run --rm -v "$(pwd)":/builddir -w /builddir runmymind/docker-android-sdk bash -xc "./gradlew clean build publish -Partifactory_url=$artifactoryUrl -Partifactory_user=$artifactoryUsername -Partifactory_password=$artifactoryPassword"