pipeline {
    agent any

    stages {
        stage('Checkout') {
            agent {label 'agent2'}
            steps {
                // traer el repo
                git branch: 'main', url: 'https://github.com/boterol/OUTCURR.git'
            }
        }
        stage('Build, test and docker deploy') {
            agent { label 'agent2' }
            steps {
                //sh "mvn -Dmaven.test.failure.ignore=true clean package"
                bat 'mvn clean install -DskipTests -X'
                bat 'mvn test'
            }
            post {
                //crea y corre la imagen
                success {
                    bat 'docker build -t outcurr-img .'
                    bat 'docker run -d -p 9092:9092 outcurr-img'
                }
            }
        }
        stage('Smoke Tests') {
            agent {label 'agent2'}
            steps {
                //modifiqué la dependencia de failsafe del pom para
                //que se ejecuten los smoke-tests con el comando integration-test ya que
                //de estos no hay
                //se ejecutacontra la app desplegada
                bat 'cd outcome-curr-mgmt-system-tests'
                bat 'mvn integration-test'
            }
            post {
                // If Maven was able to run the tests, even if some of the test
                // failed, record the test results and archive the jar file.
                success {
                    bat 'cd ..'
                    //bat ' git remote add dokkuOutcurrJenkins dokku@dokku.docker:outcurr'
                    bat 'git push dokkuOutcurrJenkins'
                }
            }
        }
    }
}