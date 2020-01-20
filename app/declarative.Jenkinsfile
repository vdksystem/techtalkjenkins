@Library('jsl') _

pipeline {
    agent any
    options {
        disableConcurrentBuilds()
        timestamps
        skipDefaultCheckout()
    }

    environment {
        GO = tool name: 'go', type: 'com.cloudbees.jenkins.plugins.customtools.CustomTool'
        PATH = "${GO}:$PATH"
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
                    sh 'go get -u github.com/jstemmer/go-junit-report'
                    sh 'go test -v 2>&1'
                }
                post {
                    success {
                        junit '*.xml'
                        archiveArtifacts 'app'
                    }
                }
            }
        }
        stage("Package") {
            steps {
                script {
                    sh "docker build -t dcr-docker-registry:5000/app:v1 ."
                    sh "docker push dcr-docker-registry:5000/app:v1"
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