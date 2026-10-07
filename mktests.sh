git_url_res="git@github.com:Elflodesbois/compil-tests-resources.git"

init_res_and_pull() {
  echo "Init du git des tests (resources)"
  git init
  git remote add origin $git_url_res

  echo "Fetch du projet de tests (resources)"
  git fetch origin
  git checkout -b master origin/master
}

install_for_maven() {
  echo "Création du répertoire de tests"
  mkdir -p src/test/resources
  cd src/test/resources
  init_res_and_pull
}

install_for_intellij() {
  echo "Création du répertoire de tests"
  mkdir -p tests

  mkdir tests-resources
  cd tests-resources

  init_res_and_pull
}

if [ -f pom.xml ]; then
  echo "Installing tests for Maven"
  install_for_maven
else
  echo "Installing tests for IntelliJ"
  install_for_intellij
fi

