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
                        def appImage = docker.build("vdksystem/techtalks:${env.BUILD_ID}")
                        appImage.push()
                        appImage.push('latest')
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
                    echo "deploy to kubernetes"
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