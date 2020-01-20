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
        stage("Build") {
            steps {
                script {
                    sh 'go test -v 2>&1 | go-junit-report > report.xml'
                    sh 'go build -o app'
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
                    withDockerRegistry(credentialsId: 'docker-credentials') {
                        sh "docker build -t vdksystem/techtalks:latest ."
                        sh "docker push vdksystem/techtalks:latest"
                    }
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