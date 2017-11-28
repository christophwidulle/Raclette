#!/usr/bin/env bash
# Requires $1 argument realOriginRepoUrl
#   Example from bamboo:
#     release-tag.sh ${bamboo.planRepository.repositoryUrl}
set -x

if [ -z "$1" ]; then
    echo "No \$1 argument(=realOriginRepoUrl) supplied"
    exit 1
fi
realOriginRepoUrl=$1

docker pull dr.chefkoch.net/android-build/base
tagname=$(docker run --rm -v "$(pwd)":/builddir -w /builddir dr.chefkoch.net/android-build/base ./gradlew --quiet app:printTagName)

# https://stackoverflow.com/questions/27371629/how-to-tag-a-git-repo-in-a-bamboo-build/27441732#27441732
git remote add realOrigin "$realOriginRepoUrl"
git tag "$tagname"
git push realOrigin "$tagname"
git ls-remote --exit-code --tags realOrigin "$tagname"
