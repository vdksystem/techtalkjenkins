pipeline {
    agent {
        label 'default'
    }
    options {
        disableConcurrentBuilds()
        timestamps()
        skipDefaultCheckout()
        buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '365', numToKeepStr: '')
    }

    environment {
        NOOP = 'noop'
    }

    library {
    }

    stages {
        stage("Checkout") {
            steps {
                cleanWs()
                checkout scm
            }
        }
        stage("Test") {
            steps {
                script {
                    sh "go test"
                }
                post {
                    success {
                        junit allowEmptyResults: true, testResults: '*/target/surefire-reports/*TestSuite.xml'
                    }
                }
            }
        }
        stage("Build") {
            steps {
                script {
                    sh "go build"
                }
            }
        }
        stage("Publish") {
            when {
                branch 'master'
            }
            steps {
                script {
                    sh "aws ecr get-login --no-include-email"
                    sh "docker push"
                }
            }
        }
        stage("Deploy") {
            when {
                branch 'master'
            }
            steps {
                script {
                    sh "deploy to kubernetes"
                }
            }
        }
    }
    post {
        success {
            echo "Actions on SUCCESS"
        }
        fixed {
            echo "Actions on FIXED"
        }
        always {
            echo "Always actions - last execution"
        }
    }
}