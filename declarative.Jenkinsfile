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
                    golang.test()
                }
            }
            post {
                success {
                    junit '*.xml'
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
                    helm.upgrade('techtalks', "--set image.tag = ${env.BUILD_ID}")
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