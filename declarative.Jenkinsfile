@Library('jsl') _

pipeline {
    agent any
    options {
        disableConcurrentBuilds()
        skipDefaultCheckout()
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
                    sh 'go test -v 2>&1 | go-junit-report > report.xml'
                }
            }
            post {
                success {
                    junit '*.xml'
                    archiveArtifacts 'app'
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