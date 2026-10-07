git_url_res="git@github.com:Elflodesbois/compil-tests-resources.git"

install_for_maven() {
  echo "Création du répertoire de tests"
  mkdir -p src/test/resources
  cd src/test/resources

  echo "Init du git des tests (resources)"
  git init
  git remote add origin $git_url_res

  echo "Fetch du projet de tests (resources)"
  git fetch origin
  git checkout -b master origin/master
}

if [ -f pom.xml ]; then
  echo "Installing tests for Maven"
  install_for_maven
fi

